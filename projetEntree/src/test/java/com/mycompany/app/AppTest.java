package com.mycompany.app;

import main.java.com.mycompany.app.App;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    @test
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
        assertTure(App.methodTest() == 1);
    }
}
