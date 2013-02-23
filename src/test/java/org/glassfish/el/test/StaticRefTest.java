/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.glassfish.el.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.el.*;

/**
 *
 * @author kichung
 */
public class StaticRefTest {

    ELProcessor elp;

    public StaticRefTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        elp = new ELProcessor();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testStaticRef() {
        // Pre imported java.lang classes
//        assertTrue((Boolean)elp.eval("T(java.lang.Boolean).TRUE"));
//        assertTrue((Boolean)elp.eval("T(Boolean).TRUE"));
        assertTrue((Boolean)elp.eval("Boolean.TRUE"));
        assertTrue((Boolean)elp.eval("Boolean.TRUE"));  // test caching Boolean
    }

/*
    @Test
    public void testClass() {
        assertEquals(String.class, elp.eval("String.class"));
    }
*/

    @Test
    public void testConstructor() {
//        assertEquals(new Integer(1001), elp.eval("T(Integer)(1001)"));
        assertEquals(new Integer(1001), elp.eval("Integer(1001)"));
    }

    @Test
    public void testStaticMethod() {
        assertEquals(4, elp.eval("Integer.numberOfTrailingZeros(16)"));
    }
}
