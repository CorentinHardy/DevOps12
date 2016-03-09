package fr.unice.polytech.ogl.isldc.testStrategies;

import static org.junit.Assert.*;

import fr.unice.polytech.ogl.isldc.automate.Auto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import fr.unice.polytech.ogl.isldc.strategies.StrategyBiomes;

public class TestStrategyBiomes {
    private Auto auto = new Auto();
    private String creek_id = "0000-4444-5655-ercc";
    private int budget = 3459;
    private int men = 5343;
    private String resource1 = "WOOD";
    private String resource2 = "FLOWER";
    private int amount1 = 5343;
    private int amount2 = 234;
    private String init = "{    \"creek\": \"" + creek_id + "\", \"budget\": "
            + budget + ", \"men\": " + men
            + ", \"objective\": [ { \"resource\": \"" + resource1
            + "\", \"amount\":" + amount1 + "}, { \"resource\": \""
            + resource2 + "\", \"amount\":" + amount2 + "} ] }";

    /**
     * Test the robot decisions according to simulated answers (biomes strategy)
     */
    @Test
    @Ignore
    public void testStrategyBiomes() {
        JSONObject json;
        auto = new Auto();
        auto.start(init);
        auto.act();
        int len = 20;
        int subSize;
        String res;
        String act;
        String action;
        String[] results = new String[len];
        /*
         * If automate is correct, it will perform actions according to simulated
         * answers
         */
        String[][] expected = { {}, { "3" }, {}, { "3", "W" },
                { "3", "E", "N" }, { "3", "E", "N", "W" }, { "WOOD" }, {},
                { "N", "S", "W" }, {}, { "3", "W", "E", "S" },
                { "3", "W", "E", "N" }, { "3", "W", "E", "N" }, {}, {},
                { "3", "W", "N", "S" }, { "3", "W", "E", "S" },
                { "3", "W", "E", "N" }, {}, {} };
        results[0] = "{ \"status\": \"OK\", \"cost\": 12}";
        results[1] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"MANGROVE\", 80.0], [\"BEACH\", 20.0]],[[\"SNOW\", 100.0]],[[\"SNOW\", 100.0]]]}}";//[\"MANGROVE\", 40.0], [\"TROPICAL_RAIN_FOREST\", 40.0], [\"TROPICAL_SEASONAL_FOREST\", 20.0]],[\"TROPICAL_RAIN_FOREST\", \"TROPICAL_SEASONAL_FOREST\"]]}}";
        results[2] = "{ \"status\": \"OK\", \"cost\": 39, \"extras\": { \"resources\": [{\"resource\": \"WOOD\", \"amount\": \"MEDIUM\", \"cond\": \"FAIR\"},{\"resource\": \"FUR\", \"amount\": \"HIGH\", \"cond\": \"EASY\"}], \"pois\":[{\"kind\": \"CREEK\", \"id\": \"creek_identifier_2\" }]}}";
        results[3] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"SNOW\", 80.0], [\"BEACH\", 20.0]],[[\"OCEAN\", 40.0], [\"LAKE\", 40.0], [\"OCEAN\", 20.0]],[\"TROPICAL_RAIN_FOREST\", \"TROPICAL_SEASONAL_FOREST\"]]}}";
        results[4] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"TUNDRA\", 80.0], [\"BEACH\", 20.0]],[[\"MANGROVE\", 40.0], [\"TROPICAL_RAIN_FOREST\", 40.0], [\"TROPICAL_SEASONAL_FOREST\", 20.0]],[\"TROPICAL_RAIN_FOREST\", \"TROPICAL_SEASONAL_FOREST\"]]}}";
        results[5] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"LAKE\", 80.0], [\"BEACH\", 20.0]],[[\"MANGROVE\", 40.0], [\"TROPICAL_RAIN_FOREST\", 40.0], [\"TROPICAL_SEASONAL_FOREST\", 20.0]],[\"TROPICAL_RAIN_FOREST\", \"TROPICAL_SEASONAL_FOREST\"]]}}";
        results[6] = "{ \"status\": \"OK\", \"cost\": 37, \"extras\": { \"amount\": 123 } }";
        results[7] = "{ \"status\": \"OK\", \"cost\": 39, \"extras\": { \"resources\": [{\"resource\": \"WOOD\", \"amount\": \"LOW\", \"cond\": \"FAIR\"},{\"resource\": \"FUR\", \"amount\": \"HIGH\", \"cond\": \"EASY\"}], \"pois\":[{\"kind\": \"CREEK\", \"id\": \"creek_identifier_3\" }]}}";
        results[8] = "{ \"status\": \"OK\", \"cost\": 21}";
        results[9] = "{ \"status\": \"OK\", \"cost\": 39, \"extras\": { \"resources\": [{\"resource\": \"WOOD\", \"amount\": \"LOW\", \"cond\": \"HARSH\"},{\"resource\": \"FUR\", \"amount\": \"HIGH\", \"cond\": \"EASY\"}], \"pois\":[{\"kind\": \"CREEK\", \"id\": \"creek_identifier_4\" }]}}";
        results[10] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"SNOW\", 80.0], [\"BEACH\", 20.0]],[[\"MANGROVE\", 40.0], [\"TROPICAL_RAIN_FOREST\", 40.0], [\"TROPICAL_SEASONAL_FOREST\", 20.0]],[\"TROPICAL_RAIN_FOREST\", \"TROPICAL_SEASONAL_FOREST\"]]}}";
        results[11] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 1,\"report\": [[[\"MANGROVE\", 80.0], [\"BEACH\", 20.0]]]}}";
        results[12] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"TUNDRA\", 80.0], [\"BEACH\", 20.0]],[[\"MANGROVE\", 40.0], [\"TROPICAL_RAIN_FOREST\", 40.0], [\"TROPICAL_SEASONAL_FOREST\", 20.0]],[\"TROPICAL_RAIN_FOREST\", \"TROPICAL_SEASONAL_FOREST\"]]}}";
        results[13] = "{ \"status\": \"OK\", \"cost\": 21}";
        results[14] = "{ \"status\": \"OK\", \"cost\": 39, \"extras\": { \"resources\": [{\"resource\": \"WOOD\", \"amount\": \"LOW\", \"cond\": \"HARSH\"}], \"pois\":[{\"kind\": \"CREEK\", \"id\": \"creek_identifier_2\" }]}}";
        results[15] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"MANGROVE\", 80.0], [\"BEACH\", 20.0]]]}}";
        results[16] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"SNOW\", 80.0], [\"BEACH\", 20.0]],[[\"MANGROVE\", 40.0], [\"TROPICAL_RAIN_FOREST\", 40.0], [\"TROPICAL_SEASONAL_FOREST\", 20.0]],[\"TROPICAL_RAIN_FOREST\", \"TROPICAL_SEASONAL_FOREST\"]]}}";
        results[17] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"MANGROVE\", 80.0], [\"BEACH\", 20.0]],[[\"MANGROVE\", 40.0], [\"TROPICAL_RAIN_FOREST\", 40.0], [\"TROPICAL_SEASONAL_FOREST\", 20.0]],[\"TROPICAL_RAIN_FOREST\", \"TROPICAL_SEASONAL_FOREST\"]]}}";
        results[18] = "{ \"status\": \"OK\", \"cost\": 21}";
        results[19] = "{ \"status\": \"OK\", \"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"MANGROVE\", 80.0], [\"BEACH\", 20.0]]]}}";

        for (int i = 1; i < len; i++) {
            act = StrategyBiomes.apply(auto);
            auto.setCurrentTile(auto.getMap().getCase(auto.getX(), auto.getY()));
            auto.setCurrentResources(auto.getCurrentTile().getResources());
            try {
                json = new JSONObject(act);
                action = json.getString("action");
                subSize = expected[i].length;
                if (action.equals("exploit")) {
                    assertTrue(action.equals("exploit"));
                    assertTrue(json.getJSONObject("parameters").get("resource")
                            .equals(expected[i][0]));
                } else if (action.equals("explore")) {
                    assertTrue(action.equals("explore"));
                } else if (action.equals("glimpse")) {
                    assertTrue(action.equals("glimpse"));
                    for (int j = 0; j < subSize; j++) {
                        if (j == 0)
                            assertTrue(!Integer.toString(
                                    json.getJSONObject("parameters").getInt(
                                            "range")).equals(
                                    Integer.parseInt(expected[i][j])));
                        else
                            assertTrue(!json.getJSONObject("parameters")
                                    .getString("direction")
                                    .equals(expected[i][j]));
                    }
                } else if (action.equals("land")) {
                    assertTrue(action.equals("land"));
                } else if (action.equals("move_to")) {
                    assertTrue(action.equals("move_to"));
                    for (int j = 0; j < subSize; j++)
                        assertTrue(!(auto.getDirection() == expected[i][j]
                                .charAt(0)));
                } else if (action.equals("scout")) {
                    assertTrue(action.equals("scout"));
                    for (int j = 0; j < subSize; j++)
                        assertTrue(!(auto.getDirection() == expected[i][j]
                                .charAt(0)));
                } else if (action.equals("stop")) {
                    assertTrue(action.equals("stop"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            res = results[i];
            auto.actionResult(res);
        }
    }
}
