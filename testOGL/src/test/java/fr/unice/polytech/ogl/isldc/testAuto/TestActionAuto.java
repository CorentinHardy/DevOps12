package fr.unice.polytech.ogl.isldc.testAuto;

import static org.junit.Assert.*;

import fr.unice.polytech.ogl.isldc.automate.Auto;
import org.junit.Test;

import fr.unice.polytech.ogl.isldc.automate.ActionAuto;

public class TestActionAuto {
    
    @Test
    public void testGet() {
        Auto auto = new Auto();
        ActionAuto actionAuto = new ActionAuto(auto);
        assertEquals(actionAuto.getAI(), auto);
    }
}
