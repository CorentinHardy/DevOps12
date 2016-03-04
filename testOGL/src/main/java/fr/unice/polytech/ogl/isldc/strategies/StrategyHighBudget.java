package fr.unice.polytech.ogl.isldc.strategies;

import fr.unice.polytech.ogl.isldc.automate.Auto;

public final class StrategyHighBudget {
    private StrategyHighBudget(){

    }
    public static String apply(Auto auto) {
        if (auto.getNbLanded() < 2) {
            // Land one explorer
            if (auto.getNbLanded() == 0) {
                return auto.getLandAi().actionLand(1, auto.getBoatInCreek());
            }
            // If we've explored enough, land exploiters
            else if (auto.getBudget() < auto.getBudgetInitial() * 0.9) {
                int nb = auto.getMen();
                if (nb > 50)
                    nb = 50;
                return auto.getLandAi().actionLand(nb - 1, auto.getBoatInCreek());
            }
            // Scout surrounding tiles if needed
            try {
                char newDirection = auto.getScoutAi().nextScoutTile();
                return auto.getScoutAi().actionScout(newDirection);
            } catch (Exception e) {
                if (Auto.TEXT_EXCEPTION_NEXTSCOUTTILE.equals(e.getMessage())) {
                }
            }
            // If we've checked everything, nothing to do but move
            return auto.getMoveAi().actionMove(auto.getMoveAi().findBestTile());
        }
        return StrategyDefault.apply(auto);
    }
}
