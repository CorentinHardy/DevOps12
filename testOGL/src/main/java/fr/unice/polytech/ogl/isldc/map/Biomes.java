package fr.unice.polytech.ogl.isldc.map;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the biomes available on the island
 *
 * @author user
 */
public enum Biomes {

    MANGROVE(new Resource("WOOD", "unknown", "unknown"),
            new Resource("FLOWER", "unknown", "unknown")),
    SNOW(),
    TROPICAL_RAIN_FOREST(new Resource("WOOD", "unknown", "unknown"),
            new Resource("SUGAR_CANE", "unknown", "unknown"),
            new Resource("FRUITS", "unknown", "unknown")),
    TROPICAL_SEASONAL_FOREST(new Resource("WOOD", "unknown", "unknown"),
            new Resource("SUGAR_CANE", "unknown", "unknown"),
            new Resource("FRUITS", "unknown", "unknown")),
    TAIGA(new Resource("WOOD", "unknown", "unknown")),
    TEMPERATE_RAIN_FOREST(new Resource("WOOD", "unknown", "unknown"),
            new Resource("FUR", "unknown", "unknown")),
    TEMPERATE_DECIDUOUS_FOREST(
            new Resource("WOOD", "unknown", "unknown")),
    GRASSLAND(
            new Resource("FUR", "unknown", "unknown")),
    SHRUBLAND(new Resource("FUR", "unknown", "unknown")),
    TUNDRA(new Resource("FUR", "unknown", "unknown")),
    ALPINE(new Resource("ORE", "unknown", "unknown"),
            new Resource("FLOWER", "unknown", "unknown")),
    BEACH(new Resource("QUARTZ", "unknown", "unknown")),
    SUB_TROPICAL_DESERT(new Resource("ORE", "unknown", "unknown"),
            new Resource("QUARTZ", "unknown", "unknown")),
    TEMPERATE_DESERT(new Resource("ORE", "unknown", "unknown"),
            new Resource("QUARTZ", "unknown", "unknown")),
    OCEAN(new Resource("FISH", "unknown", "unknown")),
    LAKE(new Resource("FISH", "unknown", "unknown")),
    GLACIER(new Resource("FLOWER", "unknown", "unknown"));

    public static final String[] CONTAIN_WATER = {"OCEAN", "LAKE"};
    private List<Resource> list = new ArrayList<>();

    Biomes(Resource... param) {
        for (Resource r : param)
            list.add(r);
    }

    /**
     * This method return resources that can be found on a tile
     *
     * @param biome content of the biome that you want to check
     * @return resources that can appear in this kind of biome
     */
    public static List<Resource> potentialResources(String biome) {
        return Biomes.valueOf(biome).list;
    }
}
