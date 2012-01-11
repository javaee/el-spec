package org.glassfish.el.test;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.el.ELProcessor;

public class ELProcessorTest {

    ELProcessor elp;

    @Before
    public void setUp() {
        elp = Manager.getManager().getElp();
    }

    @Test
    public void testGetValue() {
        Object result = elp.getValue("10 + 1");
        assertEquals(result.toString(), "11");
        result = elp.getValue("10 + 2", String.class);
        assertEquals(result, "12");
    }

    @Test
    public void testSetVariable () {
        elp.setVariable("xx", "100");
        Object result = elp.getValue("xx + 11", String.class);
        assertEquals(result, "111");
        elp.setVariable("xx", null);
        assertEquals(elp.getValue("xx"), null);
    }
}
