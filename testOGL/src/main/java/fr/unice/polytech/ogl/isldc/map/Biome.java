package fr.unice.polytech.ogl.isldc.map;

/**
 * Specifications of a biome
 * 
 * @author user
 * 
 */
public class Biome {
    private String name; // Name of the biome
    private boolean alone; // True if the biome is alone in the tile
    private double percentage; // Percentage of the biome in the tile

    public Biome(String name, double percentage) {
        this.percentage = percentage;
        if (Math.abs(percentage - 100) < 0.001) {
            alone = true;
        } else {
            alone = false;
        }
        this.name = name;
    }

    /**
     * Set the boolean alone to false
     */
    public void notAlone() {
        alone = false;
    }

    /**
     * 
     * @return name of the biome
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return percentage of the biome on the tile
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * 
     * @return return true if the Biome is alone on the tile
     */
    public boolean getAlone() {
        return alone;
    }

    /**
     * Check if the biome is alone
     */
    public void checkAlone() {
        if (Math.abs(percentage - 100) < 0.001) {
            alone = true;
        }
    }

    /**
     * Set percentage of the biome to value give in parameters
     * 
     * @param percentage
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
        if (Math.abs(percentage - 100) < 0.001)
            this.notAlone();

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Biome) {
            Biome tmp = (Biome) o;
            if (this.getName().equals(tmp.getName())) {
                if (this.getAlone() == tmp.getAlone()
                        && Math.abs(this.getPercentage() - tmp.getPercentage()) < 0.001) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + name.hashCode();
        hash = hash * 32 + (int) percentage;
        return hash;
    }

    @Override
    public String toString() {
        return "Nom: " + name + "	" + "Alone: " + alone + "	" + "Pourentage: "
                + percentage;
    }
}
