package fr.unice.polytech.ogl.isldc.automate;

import org.json.JSONArray;
import org.json.JSONException;

import fr.unice.polytech.ogl.isldc.map.IslandTile;

/**
 * Scouting action : used to collect informations on a tile in a given direction
 * 
 * @author user
 * 
 */
public class ScoutAuto extends ActionAuto {
    public static final String SCOUT = "scout";

    public ScoutAuto(Auto auto) {
        super(auto);
    }

    /**
     * 
     * @param dir
     *            direction where the automate has to scout
     * @return where the sailors will scout
     */
    public String actionScout(char dir) {
        getAI().setDirection(dir);
        getAI().setPrevAction(SCOUT);
        return "{\"action\": \"" + SCOUT
                + "\", \"parameters\": {\"direction\": \"" + dir + "\" } }";
    }

    /**
     * If the previous action the automate performed is scout
     *
     * @throws JSONException
     */
    public void resultsScout() throws JSONException {
        int alt = getAI().getJson().getJSONObject("extras").getInt("altitude");
        boolean reachable = true;
        if (getAI().getJson().getJSONObject("extras").has("unreachable")) {
            reachable = !getAI().getJson().getJSONObject("extras")
                    .getBoolean("unreachable");
        }
        JSONArray array = getAI().getJson().getJSONObject("extras")
                .getJSONArray("resources");
        IslandTile tile = getAI().getMap().addCase(
                Auto.switchX(getAI().getX(), getAI().getDirection(), 1),
                Auto.switchY(getAI().getY(), getAI().getDirection(), 1),
                getAI().getAltitude() + alt, reachable);
        int size = array.length();
        for (int i = 0; i < size; i++) {
            tile.addScoutedResource(array.getString(i++));
        }
    }

    public char nextScoutTile() throws Exception {
        for (char aDirection : Auto.ALL_DIRECTION) {
            if ((getAI().getMap().getCase(
                    Auto.switchX(getAI().getX(), aDirection, 1),
                    Auto.switchY(getAI().getY(), aDirection, 1)) == null)
                    || !getAI()
                            .getMap()
                            .getCase(Auto.switchX(getAI().getX(), aDirection, 1),
                                    Auto.switchY(getAI().getY(), aDirection, 1))
                            .isScouted())
                return aDirection;
        }
        throw new Exception(Auto.TEXT_EXCEPTION_NEXTSCOUTTILE);
    }
}
