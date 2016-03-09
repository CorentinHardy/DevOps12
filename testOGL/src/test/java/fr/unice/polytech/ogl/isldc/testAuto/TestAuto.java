package fr.unice.polytech.ogl.isldc.testAuto;

import static org.junit.Assert.*;

import java.util.List;

import fr.unice.polytech.ogl.isldc.automate.Auto;
import org.junit.Test;

import fr.unice.polytech.ogl.isldc.Objective;
import fr.unice.polytech.ogl.isldc.map.IslandTile;

/**
 * This class will test the automate class
 * 
 * @author user
 * 
 */
public class TestAuto {

    private Auto auto = new Auto();
    private String creek_id = "0000-4444-5655-ercc";
    private int budget = 3459;
    private int men = 5343;
    private String resource1 = "WOOD";
    private String resource2 = "FLOWER";
    private String resource3 = "RUM";
    private String resource4 = "PLANK";
    private int amount1 = 5343;
    private int amount2 = 234;
    private String init = "{    \"creek\": \"" + creek_id + "\", \"budget\": "
            + budget + ", \"men\": " + men
            + ", \"objective\": [ { \"resource\": \"" + resource1
            + "\", \"amount\":" + amount1 + "}, { \"resource\": \""
            + resource2 + "\", \"amount\":" + amount2 + "} ] }";
    private String init2 = "{    \"creek\": \"" + creek_id + "\", \"budget\": "
            + budget + ", \"men\": " + men
            + ", \"objective\": [ { \"resource\": \"" + resource1
            + "\", \"amount\":" + amount1 + "}, { \"resource\": \""
            + resource3 + "\", \"amount\":" + amount2 + "} ] }";
    private String init3 = "{    \"creek\": \"" + creek_id + "\", \"budget\": "
            + budget + ", \"men\": " + men
            + ", \"objective\": [ { \"resource\": \"" + resource1
            + "\", \"amount\":" + amount1 + "}, { \"resource\": \""
            + resource4 + "\", \"amount\":" + amount2 + "} ] }";
    /**
     * Test the initialization of our robot for the championship
     */
    @Test
    public void testStart() {
        auto = new Auto();
        auto.start(init);
        assertEquals(auto.getBoatInCreek(), creek_id);
        assertEquals(auto.getBudget(), budget);
        assertEquals(auto.getMen(), men);
        List<Objective> obj = auto.getObjective();
        assertEquals(obj.get(0).getResource(), resource1);
        assertEquals(obj.get(1).getResource(), resource2);
        assertEquals(obj.get(0).getAmount(), amount1);
        assertEquals(obj.get(1).getAmount(), amount2);
        assertEquals((int) auto.getResourceHave().get(resource1),0);

        auto = new Auto();
        auto.start(init2);
        assertEquals(auto.getObjective().get(1).getResource(), "FRUITS");
        assertEquals(auto.getObjective().get(2).getResource(), "SUGAR_CANE");
        assertEquals(auto.getObjective().get(1).getAmount(), 1*amount2);
        assertEquals(auto.getObjective().get(2).getAmount(), 10 * amount2);

        auto = new Auto();
        auto.start(init3);
        assertEquals(auto.getObjective().get(0).getResource(), "WOOD");
        assertEquals(auto.getObjective().get(0).getAmount(), amount1 + amount2/4);
    }
    
    @Test
    public void testAct() {
        int budgetInit = budget;
        auto = new Auto();
        auto.start(init);
        auto.act();
        assertTrue(!auto.getCurrentResources().equals(null));
        assertTrue(!auto.getCurrentTile().equals(null));
        assertEquals(auto.getBudgetInitial(),budgetInit);
    }

    /**
     * Test the method calculateScore
     * 
     * this method: look that score aren't random, test the penalty for
     * altitude, test a IslandCase with only 1 resource we want, test a
     * IslandCase with only 1 resource we don't want.
     */
    @Test
    public void testCalculateScore() {
        IslandTile cc1, cc2;
        auto = new Auto();
        auto.start(init);

        // tests for altitude
        for (int i = 0; i < 10; i++) {
            cc1 = new IslandTile(i * Auto.STEP_ALT, false);
            cc1.setExploited(true);
            cc2 = new IslandTile(-i * Auto.STEP_ALT, false);
            cc2.setExploited(true);
            if ((i * Auto.STEP_ALT_PENALITY) < auto.calculateScore(cc1))
                fail("too much score, expected less");
            assertEquals(auto.calculateScore(cc1), auto.calculateScore(cc1));
            assertEquals(auto.calculateScore(cc2), auto.calculateScore(cc2));
            assertEquals(auto.calculateScore(cc1), auto.calculateScore(cc2));
            assertEquals(auto.calculateScore(cc2), auto.calculateScore(cc1));
        }

        // level for only one resource
        final String[] tabRes = { "unexpected", "LOW", "MEDIUM", "HIGH" };
        int size = tabRes.length;
        int[] scoresRes = new int[size];
        cc1 = new IslandTile(1, false);
        // 1) we want it
        for (int i = 0; i < size; i++) {
            cc1.removeResources();
            cc1.addExploreResource(resource1, tabRes[i], "EASY");
            scoresRes[i] = auto.calculateScore(cc1);
            if (scoresRes[i] < 0)
                fail("while this score is negative ? he should be higher");
            if (i > 0)
                if (scoresRes[i - 1] > scoresRes[i])
                    fail("switchAmountRes have a problem probably, or tabRes isn't in the right order");
        }
        // if the first resource and the last one have the same score, that is
        // not normal !
        if (scoresRes[0] >= scoresRes[size - 1])
            fail("switchAmountRes have a problem");
        // 2) this resource isn't for us.
        cc1.removeResources();
        cc1.addExploreResource("DELL", tabRes[size - 1], "EASY");
        if (auto.calculateScore(cc1) > 0)
            fail("this resource doesn't interest us, score should be negative or null.");
    }
}
