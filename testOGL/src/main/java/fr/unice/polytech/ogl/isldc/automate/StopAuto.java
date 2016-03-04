package fr.unice.polytech.ogl.isldc.automate;

/**
 * Stopping action : when we've finished our exploration, stop it by using this
 * action
 * 
 * @author user
 * 
 */
public class StopAuto extends ActionAuto {
    public static final String STOP = "stop";

    public StopAuto(Auto auto) {
        super(auto);
    }

    /**
     * 
     * @return the String defining the stop action
     */
    public String actionStop() {
        getAI().setPrevAction(STOP);
        return "{\"action\": \"stop\"}";
    }
}
