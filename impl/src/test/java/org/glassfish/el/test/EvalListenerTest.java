/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.glassfish.el.test;

import java.util.ArrayList;
import javax.el.ELManager;
import javax.el.ELProcessor;
import javax.el.EvaluationEvent;
import javax.el.EvaluationListener;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kichung
 */
public class EvalListenerTest {

    ELProcessor elp = new ELProcessor();
    ELManager elm = elp.getELManager();

    public EvalListenerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEvalListener() {
        final ArrayList<String> msgs = new ArrayList<String>();
        elm.addListener(new EvaluationListener() {
            @Override
            public void beforeEvaluation(EvaluationEvent ev) {
                System.out.println("Before: " + ev.getExpressionString());
                msgs.add("Before: " + ev.getExpressionString());
            }
            @Override
            public void afterEvaluation(EvaluationEvent ev) {
                System.out.println("After: " + ev.getExpressionString());
                msgs.add("After: " + ev.getExpressionString());
            }
        });
        elp.eval("100 + 10");
        elp.eval("x = 5; x*101");
        String[] expected = {"Before: ${100 + 10}",
            "After: ${100 + 10}",
            "Before: ${x = 5; x*101}",
            "After: ${x = 5; x*101}" };
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], msgs.get(i));
        }
    }

}