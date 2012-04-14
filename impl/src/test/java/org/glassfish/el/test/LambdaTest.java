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
        elp = Manager.getManager().getElp();
    }

    void testExpr(String testname, String expr, Long expected) {
        System.out.println("=== Test Lambda Expression:" + testname + " ===");
        System.out.println(" ** " + expr);
        Object result = elp.eval(expr);
        System.out.println("    returns " + result);
        assertEquals(expected, result);
    }

    @Test
    public void testImmediate() {
        testExpr("immediate", "(x->x+1)(10)", 11L);
        testExpr("immediate 2", "(((x,y)->x+y)(3,4))", 7L);
        testExpr("immediate 3", "(x->(y=x)+1)(10) + y", 21L);
    }

    @Test
    public void testAssignInvoke() {
        testExpr("assign", "func = x->x+1; func(10)", 11L);
        testExpr("assign 2", "func = (x,y)->x+y; func(3,4)", 7L);
    }

    @Test
    public void testConditional() {
        elp.eval("cond = true");
        testExpr("conditional", "(x->cond? x+1: x+2)(10)", 11L);
        elp.eval("cond = false");
        testExpr("conditional 2",
                 "func = cond? (x->x+1): (x->x+2); func(10)", 12L);
    }

    @Test
    public void testFact() {
        testExpr("factorial", "fact = n->n==0? 1: n*fact(n-1); fact(5)", 120L);
    }

    @Test
    public void testVar() {
        elp.setVariable("v", "x->x+1");
        testExpr("assignment to variable", "v(10)", 11L);
    }

    @Test
    public void testLambda() {
        testExpr("Lambda in Lambda", "f = x->y->x+y; f(100)(1)", 101L);
    }
}
