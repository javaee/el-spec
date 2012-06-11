/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.el.test;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.el.ELProcessor;

/**
 *
 * @author Kin-man
 */
public class OperatorTest {
    
    static ELProcessor elp;

    public OperatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        elp = new ELProcessor();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    void testExpr(String testname, String expr, Long expected) {
        System.out.println("=== Test " + testname + " ===");
        System.out.println(" ** " + expr);
        Object result = elp.eval(expr);
        System.out.println("    returns " + result);
        assertEquals(expected, result);
    }
    
    void testExpr(String testname, String expr, String expected) {
        System.out.println("=== Test " + testname + " ===");
        System.out.println(" ** " + expr);
        Object result = elp.eval(expr);
        System.out.println("    returns " + result);
        assertEquals(expected, result);
    }
    
    @Test
    public void testConcat() {
        testExpr("concat", "a = null; b = null; a + b", 0L);
        testExpr("add", "10 + 11", 21L);
        testExpr("concat", "'10' + 11", "1011");
        testExpr("concat 2", "11 + '10'", "1110");
        testExpr("concat 3", "100 cat 10 ", "10010");
        testExpr("concat 4", "'100' cat 10", "10010");
        testExpr("concat 5", "'100' + 10 + 1", "100101");
        testExpr("concat 6", "'100' cat 10 + 1", "10011");
    }
    
    @Test
    public void testAssign() {
        elp.eval("vv = 10");
        testExpr("assign", "vv+1", 11L);
        elp.eval("vv = 100");
        testExpr("assign 2", "vv", 100L);
        testExpr("assign 3", "x = vv = vv+1; x + vv", 202L);
        elp.eval("map = {'one':100, 'two':200}");
        testExpr("assign 4", "map.two = 201; map.two", 201L);
        testExpr("assign string", "x='string'; x+1", "string1");
    }
    
    @Test
    public void testSemi() {
        testExpr("semi", "10; 20", 20L);
        elp.eval("x = 10; 20");
        testExpr("semi 2", "x", 10L);
        testExpr("semi 3", "(x = 10; 20) + (x ; x+1)", 31L);
        testExpr("semi 4", "(x = 10; y) = 11; x + y", 21L);
    }
}
