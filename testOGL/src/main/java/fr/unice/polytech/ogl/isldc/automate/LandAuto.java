package fr.unice.polytech.ogl.isldc.automate;

/**
 * Landing action : used at the beginning of the exploration; the more people
 * we'll land, the higher the costs/harvests will be
 * 
 * @author user
 * 
 */
public class LandAuto extends ActionAuto {
    public static final String LAND = "land";

    public LandAuto(Auto auto) {
        super(auto);
    }

    /**
     * 
     * @return where people will land on the map
     */
    public String actionLand(int nb, String creekID) {
        getAI().setPrevAction(LAND);
        getAI().setNbLanded(nb);
        getAI().setBoatInCreek(creekID);
        return "{\"action\": \"land\", \"parameters\": {\"creek\": \""
                + creekID + "\", \"people\": " + nb + " }}";
    }
}
