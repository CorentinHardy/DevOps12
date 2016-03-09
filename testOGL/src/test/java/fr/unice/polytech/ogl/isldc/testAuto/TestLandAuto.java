package fr.unice.polytech.ogl.isldc.testAuto;

import static org.junit.Assert.assertEquals;

import fr.unice.polytech.ogl.isldc.automate.Auto;
import org.junit.Test;

import fr.unice.polytech.ogl.isldc.automate.LandAuto;

/**
 * Test the landing of the automate
 * 
 * @author user
 * 
 */
public class TestLandAuto {

    LandAuto landAi = new LandAuto(new Auto());

    @Test
    public void testActionStop() {
        landAi.actionLand(29, "creek2");
        assertEquals(landAi.getAI().getPrevAction(), "land");
        assertEquals(landAi.getAI().getNbLanded(), 29);
        assertEquals(landAi.getAI().getBoatInCreek(), "creek2");
    }
}
