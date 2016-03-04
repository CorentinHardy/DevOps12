package fr.unice.polytech.ogl.isldc.strategies;

import java.util.List;

import fr.unice.polytech.ogl.isldc.Objective;
import fr.unice.polytech.ogl.isldc.automate.Auto;
import fr.unice.polytech.ogl.isldc.map.Biome;
import fr.unice.polytech.ogl.isldc.map.Biomes;
import fr.unice.polytech.ogl.isldc.map.Resource;

public final class StrategyBiomes {

    private StrategyBiomes(){

    }

    public static String apply(Auto auto) {
        if (auto.getNbLanded() != 0
                && !(auto.getBudget() <= auto.getMovePoint() + 35)) {
            if (!auto.getCurrentTile().isExplored()) {
                List<Biome> currentBiomes = auto.getCurrentTile().getBiomes();
                List<Objective> objectives = auto.getObjective();
                for (Biome bio : currentBiomes) {
                    List<Resource> potentialResources = Biomes
                            .potentialResources(bio.getName());
                    for (Objective o : objectives) {
                        for (Resource r : potentialResources) {
                            if (r.getName().equals(o.getResource())
                                    && !o.isComplete())
                                return auto.getExploreAi().actionExplore();
                        }
                    }
                }
            }
            char gDir = auto.getGlimpseAi().nextGlimpseDirection();
            if (gDir != 'Z') {
                return auto.getGlimpseAi().actionGlimpse(gDir,
                        auto.getGlimpseAi().nextGlimpseRange(gDir));
            }
        }
        return StrategyDefault.apply(auto);
    }
}
