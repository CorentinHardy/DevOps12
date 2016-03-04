package fr.unice.polytech.ogl.isldc.strategies;

import fr.unice.polytech.ogl.isldc.automate.*;
import fr.unice.polytech.ogl.isldc.automate.Auto;

public final class StrategyDefault {

    private StrategyDefault(){

    }
    public static String apply(Auto auto) {
        // First we'll have to land; 30 sailors max (random)
        if (auto.getNbLanded() == 0) {
            int nb = auto.getMen();
            if (nb > 30)
                nb = 30;
            return auto.getLandAi().actionLand(nb - 1, auto.getBoatInCreek());
        }
        // If we've got few points, we leave the island
        else if (auto.needToStop()) {
            String resultCraft = CraftAuto.craft(auto);
            if("".equals(resultCraft)) {
                return auto.getStopAi().actionStop();
            }
            return resultCraft;
        }
        // Explore, if we were exploiting or if we've moved
        if (ExploitAuto.EXPLOIT.equals(auto.getPrevAction()))
            return auto.getExploreAi().actionExplore();
        else if (!auto.getCurrentResources().isEmpty()
                && MoveAuto.MOVE.equals(auto.getPrevAction())
                && "unknown".equals(auto.getCurrentResources().get(0).getAmount())) {
            return auto.getExploreAi().actionExplore();
            // If we explored and we have a resource, exploit it
        } else if (!auto.getCurrentResources().isEmpty()
                && (ExploreAuto.EXPLORE.equals(auto.getPrevAction()) || GlimpseAuto.GLIMPSE
                        .equals(auto.getPrevAction()))
                && (!auto.getExploitAi().resourceToExploit().equals(""))) {
            auto.getMap().getCase(auto.getX(), auto.getY()).setExploited(true);
            return auto.getExploitAi().actionExploit();
        }
        try {
            char newDirection = auto.getScoutAi().nextScoutTile();
            return auto.getScoutAi().actionScout(newDirection);
        } catch (Exception e) {
            if (Auto.TEXT_EXCEPTION_NEXTSCOUTTILE.equals(e.getMessage())) {
                // we have already scouted all near IslandCase
            }
        }
        // If we've checked everything, nothing to do but move
        return auto.getMoveAi().bestMove();
    }
}
