package fr.unice.polytech.ogl.isldc.strategies;

import fr.unice.polytech.ogl.isldc.automate.Auto;

public final class StrategyLowBudget {
    private StrategyLowBudget() {

    }
    public static String apply(Auto auto) {
        // Land only one sailor to limit costs
        if (auto.getNbLanded() == 0) {
            return auto.getLandAi().actionLand(1, auto.getBoatInCreek());
        }
        return auto.getStopAi().actionStop();
    }
}
