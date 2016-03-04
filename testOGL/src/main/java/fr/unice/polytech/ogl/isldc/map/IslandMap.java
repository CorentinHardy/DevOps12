package fr.unice.polytech.ogl.isldc.map;

import java.util.HashMap;
import java.util.Map;

/**
 * This class will stock the map
 * 
 * @author user
 * 
 */
public class IslandMap {
    // HashMap of the isle
    private Map<String, IslandTile> island = new HashMap<String, IslandTile>();

    /**
     * Constructor
     * 
     * @param init
     *            the first tile
     */

    public IslandMap(IslandTile init) {
        island.put("0_0", init);
    }

    /**
     * Return the tile at x, y
     */
    public IslandTile getCase(int x, int y) {
        return island.get(x + "_" + y);
    }

    /**
     * Add a case
     */
    public IslandTile addCase(int x, int y, int altitude, boolean reachable) {
        island.put(x + "_" + y, new IslandTile(altitude, reachable));
        return island.get(x + "_" + y);
    }

    /**
     * Add a tile without altitude
     * 
     * @param x
     * @param y
     * @param reachable
     * @return
     */
    public IslandTile addCase(int x, int y, boolean reachable) {
        island.put(x + "_" + y, new IslandTile(reachable));
        return island.get(x + "_" + y);
    }

}
