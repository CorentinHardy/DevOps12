package fr.unice.polytech.ogl.isldc.testAuto;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.junit.Test;

import fr.unice.polytech.ogl.isldc.automate.Auto;
import fr.unice.polytech.ogl.isldc.automate.ExploreAuto;
import fr.unice.polytech.ogl.isldc.map.IslandTile;

public class TestExploreAuto {
    ExploreAuto exploreAi = new ExploreAuto(new Auto());
    
    @Test
    public void testActionExplore() {
        exploreAi.actionExplore();
        assertEquals(exploreAi.getAI().getPrevAction(),"explore");
    }
    
    @Test
    public void testResultsExplore() {
        exploreAi.getAI().start("{\"creek\": \"creek_id\", \"budget\": 600, \"men\": 50, \"objective\": [ { \"resource\": \"WOOD\", \"amount\": 600 } ] }");
        exploreAi.getAI().getMap().addCase(exploreAi.getAI().getX(),exploreAi.getAI().getY(),1,false);
        assertFalse(exploreAi.getAI().getMap().getCase(exploreAi.getAI().getX(), exploreAi.getAI().getY()).isExplored());
        String res = "{ \"status\": \"OK\", \"cost\": 39, \"extras\": { \"resources\": [{\"resource\": \"FUR\", \"amount\": \"HIGH\", \"cond\": \"EASY\"}], \"pois\":[{\"kind\": \"CREEK\", \"id\": \"creek_identifier_2\" }]}}"; 
        exploreAi.getAI().actionResult(res);
        try{ 
            exploreAi.resultsExplore(); 
        } catch(JSONException e) {
            System.err.println("JSONException in testResultsExplore");
            e.printStackTrace();
        }
        IslandTile tile = exploreAi.getAI().getMap().getCase(exploreAi.getAI().getX(), exploreAi.getAI().getY());
        assertTrue(tile.isExplored());
        assertFalse(tile.isReachable());
        assertEquals(tile.getResources().size(),1);
        assertEquals(tile.getInterestPoint().size(),1);
    }
}
