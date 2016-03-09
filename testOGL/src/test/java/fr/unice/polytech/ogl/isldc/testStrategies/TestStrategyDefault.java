package fr.unice.polytech.ogl.isldc.testStrategies;

import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import fr.unice.polytech.ogl.isldc.automate.Auto;
import fr.unice.polytech.ogl.isldc.strategies.StrategyDefault;

public class TestStrategyDefault {
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
     * Test the robot decisions according to simulated answers (default strategy)
     */
    @Test
    public void testStrategyDefault() {
        JSONObject json;
        auto = new Auto();
        auto.start(init);
        auto.act();
        int len = 13;
        int subSize;
        String res;
        String act;
        String action;
        String[] results = new String[len];
        /*
         * If automate is correct, it will perform actions according to simulated
         * answers : 0 : land 1 : scout East 2 : scout North 3 : scout West 4 :
         * scout South 5 : move_to North 6 : explore 7 : exploit WOOD 8 :
         * explore 9 : scout East 10 : scout North 11 : scout West 12 : move_to
         * West
         */
        String[][] expected = { {}, {}, { "E" }, { "E", "N" },
                { "E", "N", "W" }, { "E", "S", "W" }, {}, { "WOOD" }, {},
                { "S" }, { "S", "E" }, { "S", "E", "N" }, { "S", "E", "N" } };
        results[0] = "{ \"status\": \"OK\", \"cost\": 12}";
        results[1] = "{ \"status\": \"OK\", \"cost\": 12, \"extras\": {\"resources\":[], \"altitude\": 23, \"unreachable\": true}}";
        results[2] = "{ \"status\": \"OK\", \"cost\": 12, \"extras\": {\"resources\":[\"WOOD\", \"FUR\"], \"altitude\": 1}}";
        results[3] = "{ \"status\": \"OK\", \"cost\": 12, \"extras\": {\"resources\":[\"FUR\"], \"altitude\": 6}}";
        results[4] = "{ \"status\": \"OK\", \"cost\": 12, \"extras\": {\"resources\":[\"FLOWER\"], \"altitude\": 1}}";
        results[5] = "{ \"status\": \"OK\", \"cost\": 21}";
        results[6] = "{ \"status\": \"OK\", \"cost\": 39, \"extras\": { \"resources\": [{\"resource\": \"WOOD\", \"amount\": \"MEDIUM\", \"cond\": \"FAIR\"},{\"resource\": \"FUR\", \"amount\": \"HIGH\", \"cond\": \"EASY\"}], \"pois\":[{\"kind\": \"CREEK\", \"id\": \"creek_identifier_2\" }]}}";
        results[7] = "{ \"status\": \"OK\", \"cost\": 37, \"extras\": { \"amount\": 123 } }";
        results[8] = "{ \"status\": \"OK\", \"cost\": 39, \"extras\": { \"resources\": [{\"resource\": \"WOOD\", \"amount\": \"LOW\", \"cond\": \"HARSH\"}], \"pois\":[{\"kind\": \"CREEK\", \"id\": \"creek_identifier_2\" }]}}";
        results[9] = "{ \"status\": \"OK\", \"cost\": 12, \"extras\": {\"resources\":[\"FUR\", \"FISH\"], \"altitude\": -6}}";
        results[10] = "{ \"status\": \"OK\", \"cost\": 12, \"extras\": {\"resources\":[\"FUR\"], \"altitude\": 6}}";
        results[11] = "{ \"status\": \"OK\", \"cost\": 12, \"extras\": {\"resources\":[\"WOOD\", \"FISH\"], \"altitude\": -4}}";
        results[12] = "{ \"status\": \"OK\", \"cost\": 21}";

//        System.out.println("TESTSTRATEGYDEFAULT:"); // FIXME Delete this
        for (int i = 1; i < len; i++) {
            act = StrategyDefault.apply(auto);
            auto.setCurrentTile(auto.getMap().getCase(auto.getX(), auto.getY()));
            auto.setCurrentResources(auto.getCurrentTile().getResources());
            try {
                json = new JSONObject(act);
                action = json.getString("action");
//                System.out.println("Action num "+i+" : "+action+ " Prev : "+auto.getPrevAction()); // FIXME Delete this
                subSize = expected[i].length;
                if (action.equals("exploit")) {
                    assertTrue(action.equals("exploit"));
                    assertTrue(json.getJSONObject("parameters").get("resource")
                            .equals(expected[i][0]));
                } else if (action.equals("explore")) {
                    assertTrue(action.equals("explore"));
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
