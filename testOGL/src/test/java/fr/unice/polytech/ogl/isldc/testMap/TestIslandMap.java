package fr.unice.polytech.ogl.isldc.testMap;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.unice.polytech.ogl.isldc.map.IslandTile;
import fr.unice.polytech.ogl.isldc.map.IslandMap;

/**
 * This class will test the IslandMap class
 * 
 * @author user
 * 
 */
public class TestIslandMap {
    private IslandMap island = new IslandMap(new IslandTile(1, true));
    IslandTile tmp1 = new IslandTile(0, true);
    IslandTile tmp2 = new IslandTile(0, true);

    /**
     * Test when we add a case
     * 
     */
    @Test
    public void addCase() {
        island.addCase(1, 0, 0, true);
        assertEquals(island.getCase(1, 0), tmp1);
    }

    /**
     * Test if we try to remplace an existing case
     * 
     */
    @Test
    public void addSame() {
        island.addCase(1, 0, 0, true);
        island.addCase(1, 0, 0, false);
        assertEquals(island.getCase(1, 0), tmp1);
    }

}
