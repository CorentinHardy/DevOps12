package fr.unice.polytech.ogl.isldc;

/**
 * This class will contain our objectives; you can get the amount needed for a
 * given resource, and you can also know if you need to collect more of it.
 * 
 * @author user
 * 
 */

public class Objective {

    private String resource; // Resource name
    private int amount; // Amount we need for check the objective

    public Objective(String resource, int amount) {
        this.resource = resource;
        this.amount = amount;
    }

    /**
     *
     * @param amount of the resource we want to add
     */
    public void addAmount(int amount) {
        this.amount += amount;
    }

    /**
     * 
     * @return Name of the resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * 
     * @return The amount we need for check the objective
     */
    public int getAmount() {
        return amount;
    }

    /**
     * 
     * @return true if the objective is check, false in other case
     */
    public boolean isComplete() {
        return amount <= 0;
    }

    /**
     * For reduce the amount of resource you need for completed this objective.
     * 
     * @param value
     *            of resource you collect for this objective.
     * @return amount of resource you don't need for completed this objective.
     */
    public int subValue(int value) {
        amount -= value;
        if (amount < 0)
            return -amount;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass())
            return false;
        Objective obj = (Objective) o;
        if (obj.getAmount() == this.getAmount()
                && obj.getResource().equals(this.getResource()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 13 + amount;
        hash = hash * 22 + resource.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "Resource: " + resource + "  " + "Amount: " + amount;
    }
}