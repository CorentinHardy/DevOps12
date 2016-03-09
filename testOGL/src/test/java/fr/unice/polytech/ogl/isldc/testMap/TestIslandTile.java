package fr.unice.polytech.ogl.isldc.testMap;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Ignore;

import fr.unice.polytech.ogl.isldc.map.Biome;
import fr.unice.polytech.ogl.isldc.map.IslandTile;
import fr.unice.polytech.ogl.isldc.map.Resource;

/**
 * This class will test the Island case class and methods
 * 
 * @author user
 * 
 */
public class TestIslandTile {

    private int alt = 42;
    private String type = "Test1";
    private String id = "Test2";
    private String resource = "Bois";
    private String resource2 = "Fer";
    private String amount = "High";
    private String cond = "Hard";
    private final static String UNKNOWN = "unknown";
    private IslandTile island = new IslandTile(alt, true);
    String[] test = { "MANGROVE" };
    String[] test2 = { "TUNDRA", "80.0" };
    String[] test3 = { "MANGROVE", "100.0" };
    String[] test4 = {};
    String[] test5 = { "MANGROVE", "aze" };
    String[] test6 = { "MANGROVE", "100" };
    String[] test7 = { "TUNDRA" };
    String[] test8 = { "OCEAN" };
    String[] test9 = { "LAKE" };
    String[] nul = null;

    /**
     * Test for adding an interest point
     * 
     */
    @Test
    public void addInterestPointTest() {
        island.addInterestPoint("Test1", "Test2");
        assertEquals(island.getInterestPoint(type), id);
    }

    /**
     * Test for the altitude
     * 
     */
    @Test
    public void altitudeTest() {
        assertEquals(island.getAltitude(), 42);
    }

    /**
     * Test for adding a resource
     * 
     */
    @Test
    public void addRessourceTest() {

        island.addScoutedResource(resource);
        island.addExploreResource(resource2, amount, cond);
        assertEquals(island.getResources().get(0), new Resource(resource,
                UNKNOWN, UNKNOWN));
        assertEquals(island.getResources().get(1), new Resource(resource2,
                amount, cond));
        island.addExploreResource(resource, amount, cond);
        assertEquals(island.getResources().get(1), new Resource(resource,
                amount, cond));

    }

    /**
     * Test for the method which find if the biome already exist
     * 
     */
    @Test
    public void findBiome() {
        Biome tmp = new Biome("Neige", 80.0);
        island.getBiomes().add(tmp);
        assertNotNull(island.getBiomes());
        assertEquals(island.findBiome("Neige"), tmp);

    }

    /**
     * Test for adding a biome without perdentage
     * 
     * @throws ParseException
     *             If we got a problem with the percentage
     */
    @Test
    public void addNoPercentage() throws ParseException {
        island.addBiome(test);
        assertEquals(island.getBiomes().get(0).getName(), "MANGROVE");
        assertEquals(island.getBiomes().get(0).getPercentage(), -1, 0);
        assertEquals(island.getBiomes().get(0).getAlone(), false);
    }

    /**
     * If we add one with a percentage
     * 
     * @throws ParseException
     */
    @Test
    public void addPercentage() throws ParseException {
        island.addBiome(test);
        island.addBiome(test2);
        assertEquals(island.getBiomes().get(1).getName(), "TUNDRA");
        assertEquals(island.getBiomes().get(1).getPercentage(), 80.0, 0);
        assertEquals(island.getBiomes().get(1).getAlone(), false);
        assertEquals(island.getBiomes().get(0).getAlone(), false);
    }

    /**
     * Test if the biome is updated
     * 
     * @throws ParseException
     */
    @Test
    public void modPercentage() throws ParseException {
        island.addBiome(test);
        island.addBiome(test3);
        assertEquals(island.getBiomes().get(0).getPercentage(), 100.0, 0);
        assertEquals(island.getBiomes().get(0).getAlone(), true);
    }

    /**
     * Try add a biome without parameters
     * 
     * @throws ParseException
     */
    @Test
    public void noParameters() throws ParseException {
        island.addBiome(test4);
        assertEquals(island.getBiomes(), new ArrayList<Biome>());
    }

    /**
     * Try add a biome with a string instead of a percentage
     * 
     * @throws ParseException
     */
    @Test
    public void stringInsteadOfDouble() throws ParseException {
        try {
            island.addBiome(test5);
        } catch (ParseException e) {
        }
    }

    /**
     * Try add a biome with a integer instead of a double
     * 
     * @throws ParseException
     */
    @Test
    public void integerPercentage() throws ParseException {
        island.addBiome(test6);
        assertEquals(island.getBiomes().get(0).getPercentage(), 100, 0);
        assertEquals(island.getBiomes().get(0).getAlone(), true);
    }

    /**
     * Try add two biome instead without a percentage
     * 
     * @throws ParseException
     */
    @Test
    public void addTwoWithoutPercentage() throws ParseException {
        island.addBiome(test7);
        island.addBiome(test);
        assertEquals(island.getBiomes().get(0).getAlone(), false);
        assertEquals(island.getBiomes().get(1).getAlone(), false);
        assertEquals(island.getBiomes().get(1).getPercentage(), -1, 0);
        assertEquals(island.getBiomes().get(1).getPercentage(), -1, 0);
    }

    /**
     * Try add a null object
     * 
     * @throws ParseException
     */
    @Test
    public void addNull() throws ParseException {
        island.addBiome(nul);

    }

    /**
     * If we try to add a biome without a percentage, and already exist with a
     * percentage, do nothing
     * 
     * @throws ParseException
     */
    @Test
    public void testMod() throws ParseException {
        island.addBiome(test3);
        island.addBiome(test);
        assertEquals(island.getBiomes().get(0).getPercentage(), 100.0, 0);

    }

    @Test
    public void testHash() {
        assertEquals(island.hashCode(), ((17+island.getInterestPoint().hashCode())*32 + island.getResources().hashCode())* 42 + island.getBiomes().hashCode());
    }

    /**
     * Test for the equals
     * 
     */
    @Test
    public void testEquals() {
        IslandTile tmp = new IslandTile(1, true);
        IslandTile tmp2 = new IslandTile(2, true);
        IslandTile tmp3 = new IslandTile(1, false);
        assertTrue(tmp.equals(tmp));
        assertFalse(tmp.equals(tmp2));
        assertFalse(tmp3.equals(tmp2));
    }

    @Test
    /**
     * Test the isOceanMethod
     */
    public void testIsOcean() throws ParseException {
        // first we try with biome.
        // no biome with a good altitude
        IslandTile tmp = new IslandTile(0, true);
        assertEquals(tmp.isOcean(), true);
        // a correct biome with a bad altitude
        tmp.addBiome(test7);
        assertEquals(tmp.isOcean(), false);
        tmp.addBiome(test8);
        assertEquals(tmp.isOcean(), false);

        // a bad biome with a bad altitude
        tmp = new IslandTile(0, true);
        tmp.addBiome(test8);
        assertEquals(tmp.isOcean(), true);
        tmp.addBiome(test9);
        assertEquals(tmp.isOcean(), true);
        tmp.addBiome(test7);
        assertEquals(tmp.isOcean(), false);

        // no biome with a good altitude
        tmp = new IslandTile(1, true);
        assertEquals(tmp.isOcean(), false);
        // a bad biome with a good altitude
        tmp.addBiome(test8);
        assertEquals(tmp.isOcean(), true);
        // a correct biome with a good altitude
        tmp.addBiome(test);
        assertEquals(tmp.isOcean(), false);

        // then with resource
        tmp = new IslandTile(1, true);
        // first only bad
        tmp.addScoutedResource("FISH");
        assertEquals(tmp.isOcean(), true);
        // and with good
        tmp.addScoutedResource("FUR");
        assertEquals(tmp.isOcean(), false);
    }

}
