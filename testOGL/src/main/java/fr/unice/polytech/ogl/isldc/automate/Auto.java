package fr.unice.polytech.ogl.isldc.automate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.unice.polytech.ogl.isldc.map.*;
import org.json.JSONException;
import org.json.JSONObject;

import fr.unice.polytech.ogl.isldc.Objective;
import fr.unice.polytech.ogl.isldc.strategies.StrategyBiomes;
import fr.unice.polytech.ogl.isldc.strategies.StrategyDefault;
import fr.unice.polytech.ogl.isldc.strategies.StrategyHighBudget;
import fr.unice.polytech.ogl.isldc.strategies.StrategyLowBudget;

/**
 * automate main class : this is where we'll call our different strategies. This class
 * also contains miscellaneous methods
 * 
 * @author user
 * 
 */
public class Auto {
    // Actors
    ExploitAuto exploitAi;
    ExploreAuto exploreAi;
    GlimpseAuto glimpseAi;
    LandAuto landAi;
    MoveAuto moveAi;
    ScoutAuto scoutAi;
    StopAuto stopAi;
    // Amount of people currently landed
    private int nbLanded = 0; 
    // Action performed during the previous turn
    private String prevAction;
    // Creek where the boat is
    private String boatInCreek;
    // List of the creekId available
    private Map<String, IslandTile> creekId = new HashMap<>();
    // map
    private IslandMap map;
    // Resource exploited during the previous action
    private String currentExploit;
    // Budget amount (initial + what we still have)
    private int budget;
    private int budgetInitial;
    // Total amount of sailors (dead sailors will reduce this number)
    private int men;
    // Move points used by previous actions (anticipate the "stop" action cost)
    private int movePoint = 0; 
    private int nbMoves;
    private int averageMoveCost;
    // Contains all the objectives : each resource with its amount
    private List<Objective> objective = new ArrayList<>();
    private Map<String, Integer> resourceHave = new HashMap<>();
    // JSON object to get all the results
    private JSONObject json = null;
    // String returned by the engine after an action has been chosen
    private String status;
    // Altitude average of the current tile or a scouted tile
    private int altitude;
    // Last direction used
    private char direction;
    // Current tile's information
    private int x, y; 
    private IslandTile currentTile; 
    private List<Resource> currentResources;

    private List<CraftAuto> craftResources = new ArrayList<>();
    // some 'public static final'
    public static final char[] ALL_DIRECTION = { 'E', 'N', 'W', 'S' };
    public static final String TEXT_EXCEPTION_NEXTSCOUTTILE = "no more direction for scouting";
    public static final int STEP_ALT = 5, STEP_ALT_PENALITY = 1;

    public void start(String context) {
        exploitAi = new ExploitAuto(this);
        exploreAi = new ExploreAuto(this);
        glimpseAi = new GlimpseAuto(this);
        landAi = new LandAuto(this);
        moveAi = new MoveAuto(this);
        scoutAi = new ScoutAuto(this);
        stopAi = new StopAuto(this);
        try {
            json = new JSONObject(context);
            boatInCreek = json.getString("creek");
            IslandTile initialCase = new IslandTile(0, true);
            initialCase.addInterestPoint("creek", boatInCreek);
            creekId.put(boatInCreek, initialCase);
            map = new IslandMap(initialCase);
            x = 0;
            y = 0;
            budget = json.getInt("budget");
            budgetInitial = budget;
            men = json.getInt("men");
            for (int i = 0; i < json.getJSONArray("objective").length(); i++) {
                boolean add = true;
                String resource = json.getJSONArray("objective")
                        .getJSONObject(i).getString("resource");
                int amount = json.getJSONArray("objective").getJSONObject(i)
                        .getInt("amount");
                for (CraftAuto s : CraftAuto.values())
                    if (s.name().equals(resource)) {
                        craftResources.add(s);
                        resourceHave.put(s.name(), amount);
                        boolean find = true;
                        for (Objective o : objective) {
                            if (o.getResource().equals(s.getRes1())) {
                                o.addAmount(s.getAmount1() * amount
                                        / s.getAmountRes());
                                find = false;
                                break;
                            }
                        }
                        if (find)
                            objective
                                    .add(new Objective(s.getRes1(), (s
                                            .getAmount1() * amount)
                                            / s.getAmountRes()));
                        if (!"None".equals(s.getRes2())) {
                            find = true;
                            for (Objective o : objective) {
                                if (o.getResource().equals(s.getRes2())) {
                                    o.addAmount(s.getAmount2() * amount
                                            / s.getAmountRes());
                                    find = false;
                                    break;
                                }
                            }
                            if (find)
                                objective.add(new Objective(s.getRes2(), s
                                        .getAmount2()
                                        * amount
                                        / s.getAmountRes()));
                        }
                        add = false;
                        break;
                    }
                if (add) {
                    boolean find = true;
                    for (Objective o : objective) {
                        if (o.getResource().equals(resource)) {
                            o.addAmount(amount);
                            find = false;
                            break;
                        }
                    }
                    if (find)
                        objective.add(new Objective(resource, amount));
                }
            }
            for (Objective o : objective) {
                resourceHave.put(o.getResource(), 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the decision taken by the automate
     */
    public String act() {
        currentTile = map.getCase(x, y);
        currentResources = currentTile.getResources();
        if (budgetInitial > 999000999) {
            // adopt a high cost strategy (exploring the whole island ?)
            return strategyHighBudget();
        } else if (budgetInitial < 10) {
            // adopt a low cost strategy (sending only one man on the island ?)
            return strategyLowBudget();
        } else {
            // if no particular situation, adopt the best default strategy
            return strategyBiomes();
        }

    }

    /**
     * Strategy that will let us spend action points to explore a lot
     * 
     * @return the result of the action chosen with this strategy
     */
    private String strategyHighBudget() {
        return StrategyHighBudget.apply(this);
    }

    /**
     * Strategy that won't let us move far from the boat to exploit enough
     * 
     * @return the result of the action chosen with this strategy
     */
    private String strategyLowBudget() {
        return StrategyLowBudget.apply(this);
    }

    /**
     * This strategy will be used with a higher priority than strategy default.
     * It will try to exploit on some biomes while avoiding some others. If any
     * resource marked as objective can't be found on a biome, move away from
     * it.
     * 
     * @return the result of the action chosen with this strategy
     */
    private String strategyBiomes() {
        return StrategyBiomes.apply(this);
    }

    /**
     * Default strategy, if there's no particular situation
     * 
     * @return the result of the action chosen with this strategy
     */
    private String strategyDefault() {
        return StrategyDefault.apply(this);
    }

    /**
     * we calculate this tile's score, with resources and altitude. If this tile
     * is already exploited, we return a negative score. If the altitude is
     * high, the score take a STEP_ALT_PENALITY for each STEP_ALT. We add score
     * for each resource in objective(with switchAmountRes).
     * 
     * @param currentCase
     *            a IslandTile we want the score.
     * @return this tile's score
     */
    public int calculateScore(IslandTile currentCase) {
        int currentScore = 0;
        if ((map.getCase(x, y).getAltitude() != 0)
                && (currentCase.getAltitude() != 0)) {
            int diffALtitude = map.getCase(x, y).getAltitude()
                    - currentCase.getAltitude();
            // we discard tile with a lot of altitude between the current tile
            for (int i = STEP_ALT; i < diffALtitude; i += STEP_ALT)
                currentScore -= STEP_ALT_PENALITY;
            for (int i = -STEP_ALT; i > diffALtitude; i -= STEP_ALT)
                currentScore -= STEP_ALT_PENALITY;
        }
        // in this case, this tile have zero advantage to be explored
        if (currentCase.isExploited()) {
            return currentScore - 5;
        }
        int sizeObjectives = objective.size(), size;
        // we look at all current objectives, and all this case's
        // resources.
        for (int iObj = 0; iObj < sizeObjectives; iObj++) {
            size = currentCase.getResources().size();
            // calculate score for resource is more accurate than with biome, so we try first with resource
            if(size > 0) {
                // we know this tile's exact resources.
                for (int iCur = 0; iCur < size; iCur++) {
                    if (objective.get(iObj).getResource()
                            .equals(currentCase.getResources().get(iCur).getName())) {
                        currentScore += Resource.switchAmount(currentCase
                                .getResources().get(iCur).getAmount());
                    }
                }
            }else{
                // we looking in this tile's biome, for know what resource can be find.
                size = currentCase.getBiomes().size();
                int sizeResourcesByBiome;
                for(int iCur = 0; iCur < size; iCur++){
                    sizeResourcesByBiome = Biomes.potentialResources(currentCase.getBiomes().get(iCur).getName()).size();
                    for(int iRes = 0; iRes < sizeResourcesByBiome; iRes++)
                        if(objective.get(iObj).getResource()
                                .equals(Biomes.potentialResources(currentCase
                                        .getBiomes().get(iCur).getName()).get(iRes).getName())){
                            currentScore += Resource.switchAmount(IslandTile.UNKNOWN);
                        }
                }
            }
        }
        return currentScore;
    }

    /**
     * This method will parse the JSON message sent by the island and update
     * information according to the action we performed
     * 
     * @param results
     *            message sent by the island engine to our explorer
     */
    public void actionResult(String results) {
        try {
            json = new JSONObject(results);
            // Get the status variable :
            status = json.getString("status");
            // Get the action cost :
            budget -= json.getInt("cost");
            // If we moved during previous turn
            if (MoveAuto.MOVE.equals(prevAction))
                moveAi.resultsMove();
            // If we scouted during previous turn
            else if (ScoutAuto.SCOUT.equals(prevAction))
                scoutAi.resultsScout();
            else if (ExploreAuto.EXPLORE.equals(prevAction))
                exploreAi.resultsExplore();
            else if (ExploitAuto.EXPLOIT.equals(prevAction))
                exploitAi.resultsExploit();
            else if (GlimpseAuto.GLIMPSE.equals(prevAction))
                glimpseAi.resultsGlimpse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*************************
     * MISCELLANEOUS METHODS *
     *************************/

    public boolean needToStop() {
        return (budget <= averageMoveCost*(nbMoves+1)) ? true : false;
    }

    public void updateAverageMoveCost(int nextCost) {
        int i = averageMoveCost * nbMoves;
        nbMoves++;
        i += nextCost;
        averageMoveCost = i / nbMoves;
    }

    /**
     * tell you the new x coordinate depending on the direction.
     * 
     * @param x
     *            your coordinate of x
     * @param direction
     *            a char E or W
     * @return x, x+1 or x-1.
     */
    public static int switchX(int x, char direction) {
        return Auto.switchX(x, direction, 1);
    }

    /**
     * tell you the new x coordinate depending on the direction.
     * 
     * @param x
     *            your coordinate of x
     * @param amount
     *            amount of change you want
     * @param direction
     *            a char E or W
     * @return x, x+1 or x-1.
     */
    public static int switchX(int x, char direction, int amount) {
        switch (direction) {
        case 'E':
            return x + amount;
        case 'W':
            return x - amount;
            default:
                break;
        }
        return x;
    }

    /**
     * tell you the new y coordinate depending on the direction.
     * 
     * @param y
     *            your coordinate of y
     * @param direction
     *            a char N or S
     * @return y, y+1, y-1.
     */
    public static int switchY(int y, char direction) {
        return switchY(y, direction, 1);
    }

    /**
     * tell you the new y coordinate depending on the direction.
     * 
     * @param y
     *            your coordinate of y
     * @param amount
     *            amount of change you want
     * @param direction
     *            a char N or S
     * @return y, y+1, y-1.
     */
    public static int switchY(int y, char direction, int amount) {
        switch (direction) {
        case 'N':
            return y - amount;
        case 'S':
            return y + amount;
            default:
                break;
        }
        return y;
    }

    /**
     * Take a direction and return the opposite
     * 
     * @param direction
     * @return the opposite direction
     * @throws Exception if direction is unknown.
     */
    public static char invertDirection(char direction) throws Exception {
        switch (direction) {
        case 'N':
            return 'S';
        case 'S':
            return 'N';
        case 'E':
            return 'W';
        case 'W':
            return 'E';
        default:
            throw new Exception("unknown direction");
        }
    }

    /**************************
     *         GETTERS        *
     **************************/

    /**
     * Determines if the exploited resource is an objective
     * 
     * @return true if the exploited resource is an objective
     */
    public boolean isObjective() {
        for (Objective o : objective)
            if (currentExploit.equals(o.getResource()))
                return true;
        return false;
    }

    public String getPrevAction() {
        return prevAction;
    }

    public String getBoatInCreek() {
        return boatInCreek;
    }

    public Map<String, IslandTile> getCreekId() {
        return creekId;
    }

    public String getCurrentExploit() {
        return currentExploit;
    }

    public int getBudget() {
        return budget;
    }

    public char getDirection() {
        return direction;
    }

    public int getMen() {
        return men;
    }

    public int getMovePoint() {
        return movePoint;
    }

    public List<Objective> getObjective() {
        return objective;
    }

    public JSONObject getJson() {
        return json;
    }

    public String getStatus() {
        return status;
    }

    public int getAltitude() {
        return altitude;
    }

    public int getNbLanded() {
        return nbLanded;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public IslandMap getMap() {
        return map;
    }

    public List<Resource> getCurrentResources() {
        return currentResources;
    }

    public LandAuto getLandAi() {
        return landAi;
    }

    public ScoutAuto getScoutAi() {
        return scoutAi;
    }

    public MoveAuto getMoveAi() {
        return moveAi;
    }

    public StopAuto getStopAi() {
        return stopAi;
    }

    public ExploreAuto getExploreAi() {
        return exploreAi;
    }

    public ExploitAuto getExploitAi() {
        return exploitAi;
    }

    public GlimpseAuto getGlimpseAi() {
        return glimpseAi;
    }

    public int getBudgetInitial() {
        return budgetInitial;
    }

    public IslandTile getCurrentTile() {
        return currentTile;
    }

    public Map<String, Integer> getResourceHave() {
        return resourceHave;
    }

    public void setResourceHave(Map<String, Integer> res){ resourceHave=res; }

    public List<CraftAuto> getCraftResource() {
        return craftResources;
    }

    /**************************
     * SETTERS FOR TESTS *
     **************************/

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public void setPrevAction(String action) {
        this.prevAction = action;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    public void addMovePoint(int movePoint) {
        this.movePoint += movePoint;
    }

    public void setNbLanded(int n) {
        nbLanded = n;
    }

    public void setCurrentExploit(String s) {
        currentExploit = s;
    }

    public void setCurrentResources(List<Resource> l) {
        currentResources = l;
    }

    public void setBoatInCreek(String s) {
        boatInCreek = s;
    }

    public void setCurrentTile(IslandTile t) {
        currentTile = t;
    }
}
