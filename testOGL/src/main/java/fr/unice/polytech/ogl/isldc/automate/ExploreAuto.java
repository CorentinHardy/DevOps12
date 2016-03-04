package fr.unice.polytech.ogl.isldc.automate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Exploring action : used to get useful information on the current tile
 * 
 * @author user
 * 
 */
public class ExploreAuto extends ActionAuto {
    public static final String EXPLORE = "explore";

    public ExploreAuto(Auto auto) {
        super(auto);
    }

    /**
     * 
     * @return the action of exploring the current tile
     */
    public String actionExplore() {
        getAI().setPrevAction(EXPLORE);
        return "{\"action\": \"" + EXPLORE + "\"}";
    }

    /**
     * If the previous action the automate performed is explore
     *
     * @throws JSONException
     */
    public void resultsExplore() throws JSONException {
        // Update : the tile has been explored
        getAI().getMap().getCase(getAI().getX(), getAI().getY())
                .setExplored(true);
        // Remove old resources to update them
        getAI().getMap().getCase(getAI().getX(), getAI().getY())
                .removeResources();
        // Array will contain resources
        JSONArray arrayResources = getAI().getJson().getJSONObject("extras")
                .getJSONArray("resources");
        int size = arrayResources.length();
        // We go through the list and we add the results :
        for (int i = 0; i < size; i++) {
            JSONObject currentJson = arrayResources.getJSONObject(i++);
            getAI().getMap()
                    .getCase(getAI().getX(), getAI().getY())
                    .addExploreResource(currentJson.getString("resource"),
                            currentJson.getString("amount"),
                            currentJson.getString("cond"));
        }
        // If JSON has a "pois" variable in "extras"
        if (getAI().getJson().getJSONObject("extras").has("pois")) {
            JSONArray arrayPois = getAI().getJson().getJSONObject("extras")
                    .getJSONArray("pois");
            // We have one (or more) interesting points like creeks
            size = arrayPois.length();
            for (int i = 0; i < size; i++) {
                JSONObject currentJson = arrayPois.getJSONObject(i++);
                getAI().getMap()
                        .getCase(getAI().getX(), getAI().getY())
                        .addInterestPoint(currentJson.getString("kind"),
                                currentJson.getString("id"));
                if ("CREEK".equals(currentJson.getString("kind"))) {
                    getAI().getCreekId().put(
                            currentJson.getString("id"),
                            getAI().getMap().getCase(getAI().getX(),
                                    getAI().getY()));
                }
            }
        }
    }
}
