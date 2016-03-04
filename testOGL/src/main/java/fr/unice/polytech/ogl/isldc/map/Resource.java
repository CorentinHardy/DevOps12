package fr.unice.polytech.ogl.isldc.map;

public class Resource {

    private String name; // Name of the resource
    private String amount; // Amount of resource available
    private String cond; // Exploiting difficulty

    public Resource(String name, String amount, String cond) {
        this.name = name;
        this.amount = amount;
        this.cond = cond;
    }

    /**
     * 
     * @return Name of the resource
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return Amount of resource available
     */
    public String getAmount() {
        return amount;
    }

    /**
     * 
     * @return Exploiting difficulty
     */
    public String getCond() {
        return cond;
    }

    /**
     * That calculate a score for the amount of resource amRes.
     * 
     * @param amRes
     *            a amount of a Resource.
     * @return a integer which describe a score. The higher is the best.
     */
    public static int switchAmount(String amRes) {
        switch (amRes) {
        case IslandTile.HIGH:
            return 10; // 10 au total
        case IslandTile.MEDIUM:
            return 5; // 5 au total
        case IslandTile.UNKNOWN:
            return 2;
        default: // aka "LOW"
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Resource resource = (Resource) o;
        if ((resource.getAmount().equals(this.getAmount()))
                && (resource.getCond().equals(this.getCond()))
                && (resource.getName().equals(this.getName())))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 13 + name.hashCode();
        hash = hash * 27 + amount.hashCode();
        hash = hash * 35 + cond.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "Nom: " + name + "	" + "Amount: " + amount + "		" + "Cond: "
                + cond;
    }
}
