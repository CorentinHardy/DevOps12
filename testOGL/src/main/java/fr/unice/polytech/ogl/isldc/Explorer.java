package fr.unice.polytech.ogl.isldc;

import eu.ace_design.island.bot.*;
import fr.unice.polytech.ogl.isldc.automate.Auto;

/**
 * Our robot's main class, needed to play in the arena
 * 
 * @author user
 * 
 */
public class Explorer implements IExplorerRaid {
    private Auto bot = new Auto();

    @Override
    public void initialize(String context) {
        bot.start(context);
    }

    @Override
    public String takeDecision() {
        return bot.act();
    }

    @Override
    public void acknowledgeResults(String results) {
        bot.actionResult(results);
    }

}
