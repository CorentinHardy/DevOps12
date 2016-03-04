package fr.unice.polytech.ogl.isldc.automate;

/**
 * Class that will be used in every action for inheritance
 * 
 * @author user
 * 
 */
public class ActionAuto {
    private Auto auto;

    public ActionAuto(Auto auto) {
        this.auto = auto;
    }

    public Auto getAI() {
        return auto;
    }
}
