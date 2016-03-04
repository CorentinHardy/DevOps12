package fr.unice.polytech.ogl.isldc.map;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will store the information relative to a tile
 *
 * @author user
 */
public class IslandTile {
    // List of the interest points
    private Map<String, String> interestPoint = new HashMap<>();
    // Resource list
    private List<Resource> resource = new ArrayList<>();
    // Altitude of the tile
    private int altitude;
    // If the tile has already been explored
    private boolean explored;
    // If the tile has already been exploited
    private boolean exploited;
    // If the tile has already been scouted
    private boolean scouted;
    // If the tile has already been glimpsed
    private boolean glimpsed;
    // If the tile is reachable (for the moment if it's in the map)
    private boolean reachable;
    // Biome's ArrayList
    private List<Biome> biomes = new ArrayList<Biome>();

    public static final String UNKNOWN = "unknown", MEDIUM = "MEDIUM", HIGH = "HIGH";

    /**
     * Initiate the Island tile, explored is initialize at false
     *
     * @param altitude  altitude of the tile
     * @param reachable if the tile is reachable
     */
    public IslandTile(int altitude, boolean reachable) {
        this.altitude = altitude;
        this.reachable = reachable;
        explored = false;
        exploited = false;
        glimpsed = false;
        scouted = true;
    }

    /**
     * Init without alt
     *
     * @param reachable
     */
    public IslandTile(boolean reachable) {
        this.reachable = reachable;
        explored = false;
        exploited = false;
        glimpsed = false;
        scouted = true;
    }

    /**
     * Constructor when a tile is glimpsed
     *
     * @param l list
     */
    public IslandTile(List<Biome> l) {
        int len = l.size();
        for (int i = 0; i < len; i++) {
            biomes.add(l.get(i));
        }
        reachable = false;
        explored = false;
        exploited = false;
        glimpsed = true;
        scouted = false;
    }

    /**
     * Return the tile altitude
     */
    public int getAltitude() {
        return altitude;
    }

    /**
     * Say if the tile has already been explored
     *
     * @return if the tile has already been explored
     */
    public boolean isExplored() {
        return explored;
    }

    /**
     * @param b change the tile if it's being explored
     */
    public void setExplored(boolean b) {
        explored = b;
    }

    /**
     * Say if the tile has already been exploited
     *
     * @return if the tile has already been exploited
     */
    public boolean isExploited() {
        return exploited;
    }

    /**
     * @param b change the tile if it's being exploited
     */
    public void setExploited(boolean b) {
        exploited = b;
    }

    /**
     * Say if the tile has already been scouted
     *
     * @return if the tile has already been scouted
     */
    public boolean isScouted() {
        return scouted;
    }

    /**
     * Say if the tile has already been glimpsed
     *
     * @return if the tile has already been glimpsed
     */
    public boolean isGlimpsed() {
        return glimpsed;
    }

    /**
     * @param b change the tile if it's being glimpsed
     */
    public void setGlimpsed(boolean b) {
        glimpsed = b;
    }

    /**
     * Say if the tile is reachable
     *
     * @return true if it's reachable
     */
    public boolean isReachable() {
        return reachable;
    }

    /**
     * Set the tile to reachable or not
     *
     * @param b true if tile is reachable, false otherwise
     */
    public void setReachable(boolean b) {
        reachable = b;
    }

    /**
     * Return the interest point id's which was passed on parameter if it's not
     * found, return "Not found"
     *
     * @return The interest point id's
     */
    public String getInterestPoint(String value) {
        if (interestPoint.containsKey(value)) {
            return interestPoint.get(value);
        }
        return "Not found";
    }

    /**
     * Return the resource ArrayList
     *
     * @return the resource ArrayList
     */
    public List<Resource> getResources() {
        return resource;
    }

    /**
     * Add the resource which was passed on parameter
     */
    public void addScoutedResource(String resource) {
        this.resource.add(new Resource(resource, UNKNOWN, UNKNOWN));
    }

    /**
     * Add the interest point which was passed on parameter
     */
    public void addInterestPoint(String type, String id) {
        interestPoint.put(type, id);
    }

    /**
     * Remove the resources from the list to add them back after (exploring)
     */
    public void removeResources() {
        resource.clear();
    }

    /**
     * Add a resource recovers by the explore methods at the list If she's
     * already here (in the scout form), it's updated
     */
    public void addExploreResource(String resource, String amount, String cond) {
        int ind = -1;
        for (int i = 0; i < this.resource.size(); i++) {
            if (this.resource.get(i).equals(
                    new Resource(resource, UNKNOWN, UNKNOWN))) {
                ind = i;
                break;
            }
        }
        if (ind >= 0)
            this.resource.remove(ind);
        this.resource.add(new Resource(resource, amount, cond));
    }

    /**
     * If the biome is equals or exist to one in memory
     *
     * @param biome
     * @return
     */
    public Biome findBiome(String biome) {
        for (Biome b : biomes) {
            if (b.getName().equals(biome))
                return b;
        }
        return null;
    }

    /**
     * Add a biome to the tile
     *
     * @throws ParseException
     */
    public void addBiome(String[] tab) throws ParseException {
        if (tab == null || tab.length == 0)
            return;
        Biome tmp = findBiome(tab[0]);

        // New biome
        if (tmp == null) {
            // No percentage
            if (tab.length == 1) {
                biomes.add(new Biome(tab[0], -1));
                biomes.get(0).notAlone();
                // Percentage
            } else {
                biomes.add(new Biome(tab[0], NumberFormat.getInstance()
                        .parse(tab[1]).doubleValue()));

            }
            // Biome already exist
        } else {

            // Only if he got a new percentage
            if (tab.length > 1) {
                tmp.setPercentage(Double.parseDouble(tab[1]));
                tmp.checkAlone();
            }
        }
    }

    /**
     * Specific method for JUnit tests
     *
     * @return ArrayList of the biomes
     */
    public List<Biome> getBiomes() {
        return biomes;
    }

    /**
     * tell if this tile is a tile mainly covered by water.
     *
     * @return true if it is made of water, false otherwise.
     */
    public boolean isOcean() {
        int i = 0, len = biomes.size();

        if(len > 0) {
            // we can see if it is correct with biomes.
            for (Biome biome : biomes)
                for (String s : Biomes.CONTAIN_WATER)
                    if (s.equals(biome.getName()))
                        i++;
            if (i == len)
                return true;
        }

        else {
            // we use altitude and resources instead
            if (this.getAltitude() <= 0)
                return true; //it is probably a tile covered by water.

            for (Resource ress : resource)
                if ("FISH".equals(ress.getName()))
                    i++;
            if (i == resource.size() && (i != 0))
                return true;
        }

        return false;
    }

    /**
     * New equals
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof IslandTile) {
            IslandTile tmp = (IslandTile) o;
            if (this.getInterestPoint().equals(tmp.getInterestPoint())) {
                if (this.getAltitude() == tmp.getAltitude()
                        && this.getBiomes().equals(tmp.getBiomes())) {
                    if (this.getResources().equals(tmp.getResources())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + interestPoint.hashCode();
        hash = hash * 32 + resource.hashCode();
        hash = hash * 42 + biomes.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        String rep = "Altitude:" + altitude + "	" + "Explored" + explored + "	"
                + "Exploited" + exploited + "		" + "Scouted" + scouted + "		"
                + "Glimpsed" + glimpsed;
        rep += "\nResource";
        for (Resource r : resource)
            rep += "\n	" + r;
        rep += "\n\n Biomes";
        for (Biome b : biomes)
            rep += "\n  " + b;
        rep += "\n\n InterestPoint: " + interestPoint.toString();
        return rep;
    }

    public Map<String, String> getInterestPoint() {
        return interestPoint;
    }
}
