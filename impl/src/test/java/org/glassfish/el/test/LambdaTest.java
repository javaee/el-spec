package org.glassfish.el.test;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.el.ELProcessor;

public class LambdaTest {

    ELProcessor elp;

    @Before
    public void setUp() {
        System.out.println("=== Testing Lambda Expressions ===");
        elp = Manager.getManager().getElp();
    }

    void testExpr(String expr, Long expected) {
        System.out.println(" ** " + expr);
        Object result = elp.eval(expr);
        System.out.println("    returns " + result);
        assertEquals(expected, result);
    }

    @Test
    public void testImmediate() {
        testExpr("(x->x+1)(10)", 11L);
        testExpr("(((x,y)->x+y)(3,4))", 7L);
    }

    @Test
    public void testAssignInvoke() {
        testExpr("func = x->x+1; func(10)", 11L);
        testExpr("func = (x,y)->x+y; func(3,4)", 7L);
    }

    @Test
    public void testConditional() {
        elp.eval("cond = true");
        testExpr("(x->cond? x+1: x+2)(10)", 11L);
        elp.eval("cond = false");
        testExpr("func = cond? (x->x+1): (x->x+2); func(10)", 12L);
    }
}
