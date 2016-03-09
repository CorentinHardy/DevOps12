package fr.unice.polytech.ogl.isldc.testAuto;

import static org.junit.Assert.assertEquals;

import fr.unice.polytech.ogl.isldc.automate.Auto;
import org.junit.Test;

import fr.unice.polytech.ogl.isldc.automate.StopAuto;

public class TestStopAuto {
    StopAuto stopAi = new StopAuto(new Auto());

    @Test
    public void testActionStop() {
        stopAi.actionStop();
        assertEquals(stopAi.getAI().getPrevAction(),"stop");
    }
}
