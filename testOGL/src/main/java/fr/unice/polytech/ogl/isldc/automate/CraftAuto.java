package fr.unice.polytech.ogl.isldc.automate;

/**
 * Crafting action : use raw resources to get processed resources
 * 
 * @author user
 * 
 */
public enum CraftAuto {
    GLASS("WOOD", 5, "QUARTZ", 10, 1), INGOT("WOOD", 5, "ORE", 5, 1), PLANK(
            "WOOD", 1, 4), LEATHER("FUR", 3, 1), RUM(
            "FRUITS", 1, "SUGAR_CANE", 10, 1);

    public static final String NONE = "None";

    private String[] res; // list of resource use for this craft
    private int[] amount; // amount of resource (amount[0..size]) for amount[amount.length - 1] craft
    private int size; // is the number of resource for this craft.

    /**
     * The recipe for one craft, with only one resource.
     *
     * @param res1 the first resource
     * @param amount1 the amount for the first resource for 1 craft
     * @param amountRes la production pour 1 craft
     */
    CraftAuto(String res1, int amount1, int amountRes) {
        this.size = 1;
        this.res = new String[size];
        this.res[0] = res1;
        this.amount = new int[size + 1];
        this.amount[0] = amount1;
        this.amount[1] = amountRes;
    }

    /**
     * The recipe for one craft.
     *
     * @param res1 the first resource
     * @param amount1 the amount for the first resource for 1 craft
     * @param res2 the second resource
     * @param amount2 the amount for the second resource for 1 craft
     * @param amountRes la production pour 1 craft
     */
    CraftAuto(String res1, int amount1, String res2, int amount2, int amountRes) {
        this.size = 2;
        this.res = new String[size];
        this.res[0] = res1;
        this.res[1] = res2;
        this.amount = new int[size + 1];
        this.amount[0] = amount1;
        this.amount[1] = amount2;
        this.amount[2] = amountRes;
    }

    /**
     * use the method bestAmount for this craft.
     *
     * @param auto the automate
     * @return the result of bestAmount.
     * @throws Exception if the resource isn't a objective.
     */
    private int[] howManyCraft(Auto auto)throws Exception {
        int[] currentAmount = new int[size];
        Integer obj = auto.getResourceHave().get(this.name());
        if (obj == null)
            throw new Exception("not a necessary craft");
        for (int i = 0; i < size; i++)
            currentAmount[i] = auto.getResourceHave().get(res[i]);
        return bestAmount(obj, currentAmount, amount);
    }

    /**
     * craft a resource, and delete old resource.
     *
     * @param auto the automate
     * @param theCraft can be calculate with the method howManyCraft.
     * @return command to craft the resource.
     */
    private String craftThis(Auto auto, int[] theCraft){
        String result = "{   \"action\": \"transform\", \"parameters\": { \""
                + res[0] + "\": " + theCraft[0]; // part of the recipe
        // we have to set the new amount of resource:
        auto.getResourceHave().put(res[0], auto.getResourceHave().get(res[0]) - theCraft[0]);
        // if there are more than one resource, we do the same for those.
        for(int i = 1; i < size; i++) {
            result += ", \"" + res[i] + "\": " + theCraft[i];
            auto.getResourceHave().put(res[i], auto.getResourceHave().get(res[i]) - theCraft[i]);
        }
        // and the resource's amount we will craft
        auto.getResourceHave().put(this.name(), auto.getResourceHave().get(this.name()) - theCraft[size]);
        return result + " } }";
    }

    /**
     * craft the resource only if it is a objective and you have a correct amount of resource (not equals to 0)
     *
     * @param auto the automate
     * @return the correct command if you craft, or a empty string.
     */
    public String correctCraft(Auto auto){
        try{
            int[] theCraft = howManyCraft(auto);
            if (theCraft[size] <= 0)
                // you haven't craft anything.
                return "";
            // you can correctly craft.
            return craftThis(auto, theCraft);
        }catch(Exception e){
            // howManyCraft have produce a exception, this isn't a correct objective.
            return "";
        }
    }

    /*************************
     *    STATIC  METHODS    *
     *************************/

    /**
     * we try to craft all the resource we need. You can use this method until this return to you an empty String if you want to craft all the resource.
     *
     * @param auto the automate
     * @return the correct command if you craft, or a empty string.
     */
    public static String craft(Auto auto) {
        String aCraft;
        for (CraftAuto cr : auto.getCraftResource()) {
            // we try to craft each craft's recipe
            aCraft = cr.correctCraft(auto);
            if (! "".equals(aCraft))
                return aCraft;
        }
        return "";
    }

    /**
     * calculate the nearest amount of resource to bestResult we can do, by using a craft.
     *
     * @param bestResult is the amount we want.
     * @param currentAmount is the list of resource we have for craft this resource.
     * @param rate is the amount needed of rate[0..length-1] for rate[length] resource. (in the same order of currentAmount)
     *
     * @return a list with the amount of all resource we need, in the same order of rate.
     */
    public static int[] bestAmount(int bestResult, int[] currentAmount, int[] rate){
        int size = currentAmount.length;
        int[] result = new int[size + 1];
        if (bestResult <= 0){
            // so we stop now this method, with a valid result.
            for(int i = 0; i < size; i++)
                result[i] = 0;
            return result;
        }
        // we want to craft enough resource to complete bestResult's demand
        double dtmp = ((double) bestResult) / ((double) rate[size]);
        int stop = (int) dtmp + (((dtmp - ((int) dtmp)) > 0) ? 1: 0);

        int tmp;
        // we will try to do the highest number of resource.
        for(int k = 0; k < stop; k++) {
            // for each resource
            for (int i = 0; i < size; i++) {
                tmp = result[i] + rate[i];
                if (tmp <= currentAmount[i])
                    result[i] = tmp;
                // if we haven't enough resource, we stop this
                else
                    return result;
            }
            // we add the resource we produce.
            result[size] += rate[size];
        }
        return result;
    }

    /*************************
     *    GETTER  METHODS    *
     *************************/

    public String getRes(int num) {
        try {
            return res[num];
        }catch(ArrayIndexOutOfBoundsException e){
            return NONE;
        }
    }

    public String getRes1() {
        return getRes(0);
    }

    public String getRes2() {
        return getRes(1);
    }

    public int getAmount(int num) {
        try{
            return amount[num];
        }catch(ArrayIndexOutOfBoundsException e){
            return 0;
        }
    }

    public int getAmount1() {
        return getAmount(0);
    }

    public int getAmount2() {
        return getAmount(1);
    }

    public int getAmountRes() {
        return getAmount(size);
    }
}
