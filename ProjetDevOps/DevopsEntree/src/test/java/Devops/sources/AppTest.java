package Devops.sources;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
        public void testretourneVal() throws Exception {
 
                assertEquals(2,App.retourneVal());
        }
}
