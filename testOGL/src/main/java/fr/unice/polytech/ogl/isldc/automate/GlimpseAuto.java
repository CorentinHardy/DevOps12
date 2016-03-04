package fr.unice.polytech.ogl.isldc.automate;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Glimpsing action : look in a certain direction to get informations on several
 * tiles
 * 
 * @author user
 * 
 */
public class GlimpseAuto extends ActionAuto {
    public static final String GLIMPSE = "glimpse";

    public GlimpseAuto(Auto auto) {
        super(auto);
    }

    /**
     * 
     * @return the action of glimpsing the current tile
     */
    public String actionGlimpse(char dir, int range) {
        getAI().setPrevAction(GLIMPSE);
        getAI().setDirection(dir);
        return "{ \"action\": \"" + GLIMPSE
                + "\", \"parameters\": { \"direction\": \"" + dir
                + "\", \"range\": " + range + " }}";
    }

    /**
     * This method allows you to find the direction you should use glimpse on,
     * according to parameters like already glimpsed tiles
     * 
     * @return the direction where you should use glimpse, Z if you shouldn't
     *         use it at all
     */
    public char nextGlimpseDirection() {
        for (char aDirection : Auto.ALL_DIRECTION) {
            if ((getAI().getMap().getCase(
                    Auto.switchX(getAI().getX(), aDirection, 1),
                    Auto.switchY(getAI().getY(), aDirection, 1)) == null)
                    || !getAI()
                            .getMap()
                            .getCase(Auto.switchX(getAI().getX(), aDirection, 1),
                                    Auto.switchY(getAI().getY(), aDirection, 1))
                            .isGlimpsed())
                return aDirection;
        }
        return 'Z';
    }

    /**
     * This method allows you to find the range you should use glimpse on,
     * according to parameters like already glimpsed tiles
     * 
     * @return the range you should use while glimpsing, 1 is the default case
     */
    public int nextGlimpseRange(char dir) {
        int counter = 1;
        for (int i = 2; i < 4; i++) {
            if ((getAI().getMap().getCase(Auto.switchX(getAI().getX(), dir, i - 1),
                    Auto.switchY(getAI().getY(), dir, i - 1)) == null)
                    || !getAI()
                            .getMap()
                            .getCase(Auto.switchX(getAI().getX(), dir, i - 1),
                                    Auto.switchY(getAI().getY(), dir, i - 1))
                            .isGlimpsed())
                counter++;
            else break;
        }
        return counter;
    }

    /**
     * If the previous action the automate performed is glimpse
     *
     * @throws JSONException
     */
    public void resultsGlimpse() throws JSONException {
        int range = getAI().getJson().getJSONObject("extras")
                .getInt("asked_range");
        JSONArray array = getAI().getJson().getJSONObject("extras")
                .getJSONArray("report");
        int arrayLen = array.length();
        int xplus = Auto.switchX(getAI().getX(), getAI().getDirection(), 0);
        int yplus = Auto.switchY(getAI().getY(), getAI().getDirection(), 0);
        if(arrayLen<range) {
            int xUnreachable = Auto.switchX(getAI().getX(), getAI().getDirection(), arrayLen);
            int yUnreachable = Auto.switchY(getAI().getY(), getAI().getDirection(), arrayLen);
            if (getAI().getMap().getCase(xUnreachable, yUnreachable) == null) {
                getAI().getMap().addCase(xUnreachable, yUnreachable, false);
            }
            else getAI().getMap().getCase(xUnreachable, yUnreachable).setReachable(false);
            getAI().getMap().getCase(xUnreachable, yUnreachable).setGlimpsed(true);
        }
        // We go through the list, adding the results:
        for (int i = 0; i < 2; i++) {
            int j = 0;
            JSONArray subArray;
            try {
                subArray = array.getJSONArray(i);
            } catch (Exception e) {
                break;
            }
            while (true) {
                try {
                    String[] rep = new String[2];
                    rep[0] = subArray.getJSONArray(j).getString(0);
                    rep[1] = Double.toString(subArray.getJSONArray(j)
                            .getDouble(1));
                    if (getAI().getMap().getCase(xplus, yplus) == null) {
                        getAI().getMap().addCase(xplus, yplus, true);
                    }
                    getAI().getMap().getCase(xplus, yplus).addBiome(rep);
                    getAI().getMap().getCase(xplus, yplus).setGlimpsed(true);
                    j++;
                } catch (Exception e) {
                    break;
                }
            }
            xplus = Auto.switchX(xplus, getAI().getDirection(), 1);
            yplus = Auto.switchY(yplus, getAI().getDirection(), 1);
        }
        if (range > 2 && arrayLen > 2) {
            JSONArray subArray = array.getJSONArray(2);
            int j = 0;
            while (true) {
                try {
                    String[] rep = new String[1];
                    rep[0] = subArray.getString(j++);
                    if (getAI().getMap().getCase(xplus, yplus) == null) {
                        getAI().getMap().addCase(xplus, yplus, true);
                    }
                    getAI().getMap().getCase(xplus, yplus).addBiome(rep);
                    getAI().getMap().getCase(xplus, yplus).setGlimpsed(true);
                } catch (Exception e) {
                    break;
                }
            }
            xplus = Auto.switchX(xplus, getAI().getDirection(), 1);
            yplus = Auto.switchY(yplus, getAI().getDirection(), 1);
        }
        if (range == 4 && arrayLen == 4) {
            try {
                String[] rep = new String[1];
                rep[0] = array.getJSONArray(3).getString(0);
                if (getAI().getMap().getCase(xplus, yplus) == null) {
                    getAI().getMap().addCase(xplus, yplus, true);
                }
                getAI().getMap().getCase(xplus, yplus).addBiome(rep);
            } catch (Exception e) {
            }
        }
    }
}
