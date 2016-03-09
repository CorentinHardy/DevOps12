package fr.unice.polytech.ogl.isldc.testAuto;

import static org.junit.Assert.*;

import java.text.ParseException;

import fr.unice.polytech.ogl.isldc.automate.Auto;
import org.junit.Test;

import fr.unice.polytech.ogl.isldc.map.IslandTile;

/**
 * Will test the Glimpse class
 * 
 * @author user
 * 
 */
public class TestGlimpseAuto {
    String[] tmpString = { "MANGROVE", "80.0" };
    String[] tmpString2 = { "BEACH", "20.0" };
    String[] tmpString3 = { "MANGROVE", "40.0" };
    String[] tmpString4 = { "TROPICAL_RAIN_FOREST", "40.0" };
    String[] tmpString5 = { "TROPICAL_SEASONAL_FOREST", "20.0" };
    String[] tmpString6 = { "TROPICAL_RAIN_FOREST" };
    String[] tmpString7 = { "TROPICAL_SEASONAL_FOREST" };
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
     * This method will test the treatment of the glimpse in the north direction
     * 
     * @throws ParseException
     */

    @Test
    public void testResultGlimpseNorth() throws ParseException {
        auto = new Auto();
        auto.start(init);
        auto.setDirection('N');
        auto.setPrevAction("glimpse");
        auto.actionResult("{\"status\": \"OK\",\"cost\": 12,\"extras\": {\"asked_range\": 4,\"report\": [[[\"MANGROVE\",80],[\"BEACH\",20]],[[\"MANGROVE\",40],[\"TROPICAL_RAIN_FOREST\",40],[\"TROPICAL_SEASONAL_FOREST\",20]],[\"TROPICAL_RAIN_FOREST\",\"TROPICAL_SEASONAL_FOREST\"],[\"TROPICAL_RAIN_FOREST\"]]}}");
        IslandTile tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString);
        tmp.addBiome(tmpString2);
        tmp.addInterestPoint("creek", "0000-4444-5655-ercc");
        IslandTile shell = auto.getMap().getCase(0, 0);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(0, -1);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString3);
        tmp.addBiome(tmpString4);
        tmp.addBiome(tmpString5);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(0, -2);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString6);
        tmp.addBiome(tmpString7);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(0, -3);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString6);
        assertEquals(shell, tmp);
    }

    /**
     * Test the glimpse with a range of 1
     * 
     * @throws ParseException
     */
    @Test
    public void testResultGlimpseNorthRange1() throws ParseException {
        auto = new Auto();
        auto.start(init);
        auto.setDirection('N');
        auto.setPrevAction("glimpse");
        auto.actionResult("{\"status\": \"OK\",\"cost\": 12,\"extras\": {\"asked_range\": 1,\"report\": [[[\"MANGROVE\",80],[\"BEACH\",20]]]}}");
        IslandTile tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString);
        tmp.addBiome(tmpString2);
        tmp.addInterestPoint("creek", "0000-4444-5655-ercc");
        IslandTile shell = auto.getMap().getCase(0, 0);
        assertEquals(shell, tmp);

    }

    /**
     * Test the glimpse with a range of 2
     * 
     * @throws ParseException
     */
    @Test
    public void testResultGlimpseNorthRange2() throws ParseException {
        auto = new Auto();
        auto.start(init);
        auto.setDirection('N');
        auto.setPrevAction("glimpse");
        auto.actionResult("{\"status\": \"OK\",\"cost\": 12,\"extras\": {\"asked_range\": 2,\"report\": [[[\"MANGROVE\",80],[\"BEACH\",20]],[[\"MANGROVE\",40],[\"TROPICAL_RAIN_FOREST\",40],[\"TROPICAL_SEASONAL_FOREST\",20]]]}}");
        IslandTile tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString);
        tmp.addBiome(tmpString2);
        tmp.addInterestPoint("creek", "0000-4444-5655-ercc");
        IslandTile shell = auto.getMap().getCase(0, 0);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(0, -1);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString3);
        tmp.addBiome(tmpString4);
        tmp.addBiome(tmpString5);
        assertEquals(shell, tmp);
    }

    /**
     * Test the glimpse with a range of 3
     * 
     * @throws ParseException
     */
    @Test
    public void testResultGlimpseNorthRange3() throws ParseException {
        auto = new Auto();
        auto.start(init);
        auto.setDirection('N');
        auto.setPrevAction("glimpse");
        auto.actionResult("{\"status\": \"OK\",\"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"MANGROVE\",80],[\"BEACH\",20]],[[\"MANGROVE\",40],[\"TROPICAL_RAIN_FOREST\",40],[\"TROPICAL_SEASONAL_FOREST\",20]],[\"TROPICAL_RAIN_FOREST\",\"TROPICAL_SEASONAL_FOREST\"]]}}");
        IslandTile tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString);
        tmp.addBiome(tmpString2);
        tmp.addInterestPoint("creek", "0000-4444-5655-ercc");
        IslandTile shell = auto.getMap().getCase(0, 0);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(0, -1);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString3);
        tmp.addBiome(tmpString4);
        tmp.addBiome(tmpString5);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(0, -2);

    }

    /**
     * This method will test the treatment of the glimpse in the South direction
     * 
     * @throws ParseException
     */
    @Test
    public void testResultGlimpseSouth() throws ParseException {
        auto = new Auto();
        auto.start(init);
        auto.setDirection('S');
        auto.setPrevAction("glimpse");
        auto.actionResult("{\"status\": \"OK\",\"cost\": 12,\"extras\": {\"asked_range\": 4,\"report\": [[[\"MANGROVE\",80],[\"BEACH\",20]],[[\"MANGROVE\",40],[\"TROPICAL_RAIN_FOREST\",40],[\"TROPICAL_SEASONAL_FOREST\",20]],[\"TROPICAL_RAIN_FOREST\",\"TROPICAL_SEASONAL_FOREST\"],[\"TROPICAL_RAIN_FOREST\"]]}}");
        IslandTile tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString);
        tmp.addBiome(tmpString2);
        tmp.addInterestPoint("creek", "0000-4444-5655-ercc");
        IslandTile shell = auto.getMap().getCase(0, 0);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(0, 1);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString3);
        tmp.addBiome(tmpString4);
        tmp.addBiome(tmpString5);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(0, 2);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString6);
        tmp.addBiome(tmpString7);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(0, 3);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString6);
        assertEquals(shell, tmp);

    }

    /**
     * This method will test the treatment of the glimpse in the West direction
     * 
     * @throws ParseException
     */
    @Test
    public void testResultGlimpseWest() throws ParseException {
        auto = new Auto();
        auto.start(init);
        auto.setDirection('W');
        auto.setPrevAction("glimpse");
        auto.actionResult("{\"status\": \"OK\",\"cost\": 12,\"extras\": {\"asked_range\": 4,\"report\": [[[\"MANGROVE\",80],[\"BEACH\",20]],[[\"MANGROVE\",40],[\"TROPICAL_RAIN_FOREST\",40],[\"TROPICAL_SEASONAL_FOREST\",20]],[\"TROPICAL_RAIN_FOREST\",\"TROPICAL_SEASONAL_FOREST\"],[\"TROPICAL_RAIN_FOREST\"]]}}");
        IslandTile tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString);
        tmp.addBiome(tmpString2);
        tmp.addInterestPoint("creek", "0000-4444-5655-ercc");
        IslandTile shell = auto.getMap().getCase(0, 0);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(-1, 0);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString3);
        tmp.addBiome(tmpString4);
        tmp.addBiome(tmpString5);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(-2, 0);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString6);
        tmp.addBiome(tmpString7);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(-3, 0);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString6);
        assertEquals(shell, tmp);

    }

    /**
     * This method will test the treatment of the glimpse in the East direction
     * 
     * @throws ParseException
     */
    @Test
    public void testResultGlimpseEast() throws ParseException {
        auto = new Auto();
        auto.start(init);
        auto.setDirection('E');
        auto.setPrevAction("glimpse");
        auto.actionResult("{\"status\": \"OK\",\"cost\": 12,\"extras\": {\"asked_range\": 4,\"report\": [[[\"MANGROVE\",80],[\"BEACH\",20]],[[\"MANGROVE\",40],[\"TROPICAL_RAIN_FOREST\",40],[\"TROPICAL_SEASONAL_FOREST\",20]],[\"TROPICAL_RAIN_FOREST\",\"TROPICAL_SEASONAL_FOREST\"],[\"TROPICAL_RAIN_FOREST\"]]}}");
        IslandTile tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString);
        tmp.addBiome(tmpString2);
        tmp.addInterestPoint("creek", "0000-4444-5655-ercc");
        IslandTile shell = auto.getMap().getCase(0, 0);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(1, 0);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString3);
        tmp.addBiome(tmpString4);
        tmp.addBiome(tmpString5);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(2, 0);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString6);
        tmp.addBiome(tmpString7);
        assertEquals(shell, tmp);
        shell = auto.getMap().getCase(3, 0);
        tmp = new IslandTile(0, true);
        tmp.addBiome(tmpString6);
        assertEquals(shell, tmp);

    }

    /**
     * Test the next glimpse direction method
     * 
     * @throws ParseException
     */
    @Test
    public void testNextGlimpseDirection() throws ParseException {
        auto = new Auto();
        auto.start(init);
        auto.getMap().addCase(1, 0, true);
        auto.getMap().getCase(1, 0).setGlimpsed(true);
        assertEquals(auto.getGlimpseAi().nextGlimpseDirection(), 'N');
        auto.getMap().addCase(0, -1, true);
        auto.getMap().getCase(0, -1).setGlimpsed(true);
        assertEquals(auto.getGlimpseAi().nextGlimpseDirection(), 'W');
        auto.getMap().addCase(-1, 0, true);
        auto.getMap().getCase(-1, 0).setGlimpsed(true);
        assertEquals(auto.getGlimpseAi().nextGlimpseDirection(), 'S');
        auto.getMap().addCase(0, 1, true);
        auto.getMap().getCase(0, 1).setGlimpsed(true);
        assertEquals(auto.getGlimpseAi().nextGlimpseDirection(), 'Z');
    }
    
    /**
     * Test glimpse when encountering the edge of the map
     */
    @Test
    public void testGlimpseUnreachable() {
        auto = new Auto();
        auto.start(init);
        auto.setDirection('E');
        auto.setPrevAction("glimpse");
        auto.actionResult("{\"status\": \"OK\",\"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"MANGROVE\",80],[\"BEACH\",20]],[[\"MANGROVE\",40],[\"TROPICAL_RAIN_FOREST\",40],[\"TROPICAL_SEASONAL_FOREST\",20]]]}}");
        IslandTile tileYes = auto.getMap().getCase(1, 0);
        IslandTile tileNo = auto.getMap().getCase(2, 0);
        assertTrue(tileYes.isReachable());
        assertTrue(!tileNo.isReachable());
    }
    
    /**
     * Second test for unreachable tiles
     */
    @Test
    public void testGlimpseUnreachableBis() {
        auto = new Auto();
        auto.start(init);
        auto.setDirection('E');
        auto.setPrevAction("glimpse");
        auto.actionResult("{\"status\": \"OK\",\"cost\": 12,\"extras\": {\"asked_range\": 4,\"report\": [[[\"MANGROVE\",80],[\"BEACH\",20]],[[\"MANGROVE\",40],[\"TROPICAL_RAIN_FOREST\",40],[\"TROPICAL_SEASONAL_FOREST\",20]],[\"TROPICAL_RAIN_FOREST\",\"TROPICAL_SEASONAL_FOREST\"]]}}");
        IslandTile tileYes = auto.getMap().getCase(1, 0);
        IslandTile tileYesBis = auto.getMap().getCase(2, 0);
        IslandTile tileNo = auto.getMap().getCase(3, 0);
        assertTrue(tileYes.isReachable());
        assertTrue(tileYesBis.isReachable());
        assertTrue(!tileNo.isReachable());
    }
    
    /**
     * Third test for unreachable tiles
     */
    @Test
    public void testGlimpseUnreachableTer() {
        auto = new Auto();
        auto.start(init);
        auto.setDirection('E');
        auto.setPrevAction("glimpse");
        auto.actionResult("{\"status\": \"OK\",\"cost\": 12,\"extras\": {\"asked_range\": 3,\"report\": [[[\"MANGROVE\",80],[\"BEACH\",20]]]}}");
        IslandTile tileNo = auto.getMap().getCase(1, 0);
        assertTrue(!tileNo.isReachable());
    }

    /**
     * Test the next glimpse range direction
     */
    @Test
    public void testNextGlimpseRange() {
        auto = new Auto();
        auto.start(init);
        auto.getMap().addCase(1, 0, true);
        auto.getMap().getCase(1, 0).setGlimpsed(false);
        auto.getMap().addCase(2, 0, true);
        auto.getMap().getCase(2, 0).setGlimpsed(true);
        auto.getMap().addCase(3, 0, true);
        auto.getMap().getCase(3, 0).setGlimpsed(false);
        assertEquals(auto.getGlimpseAi().nextGlimpseRange('E'), 2);
        auto.getMap().getCase(1, 0).setGlimpsed(true);
        auto.getMap().getCase(2, 0).setGlimpsed(false);
        assertEquals(auto.getGlimpseAi().nextGlimpseRange('E'), 1);
        auto.getMap().getCase(1, 0).setGlimpsed(false);
        auto.getMap().getCase(2, 0).setGlimpsed(false);
        assertEquals(auto.getGlimpseAi().nextGlimpseRange('E'), 3);
    }
}
