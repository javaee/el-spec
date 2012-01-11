package org.glassfish.el.test;

import java.util.List;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.el.ELProcessor;

public class LinqTest {

    ELProcessor elp;
    Object ret;

    @Before
    public void setUP() {
        Manager manager = Manager.getManager();
        manager.setupDB();
        elp = manager.getElp();
    }

    void p(String msg) {
        System.out.println(msg);
    }

    static int v[] = {200, 203, 204};

    @Test
    public void testWhere() {
        p("** Query ** products.where(p->p.unitPrice >= 10)");
        ret = elp.getValue("products.where(p->p.unitPrice >= 10)");
        assertTrue(Iterable.class.isInstance(ret));
        int indx = 0;
        for (Object item: (Iterable) ret) {
            p(item.toString());
            assertTrue(((Product)item).getProductID() == v[indx++]);
        }
    }

    static String pnames[] = {"Eagle", "Coming Home", "Greatest Hits",
                       "History of Golf", "Toy Story" };
    @Test
    public void testSelect() {
        p("** Query ** products.select(p->p.name)");
        ret = elp.getValue("products.select(p->p.name)");
        int indx = 0;
        for (Object item: (Iterable) ret) {
            p(item.toString());
            assertTrue(pnames[indx++].equals(item));
        }
    }

    static String p2names[] = {"Eagle", "History of Golf", "Toy Story"};
    static double p2price[] = {12.5, 11.0, 10.0};
    @Test
    public void testSelect2() {
        p("** Query ** products.where(p->p.unitPrice >= 10)");
        p("                    .select(p->[p.name, p.unitPrice])");
        ret = elp.getValue("products.where(p->p.unitPrice >= 10)" +
                                   ".select(p->[p.name,p.unitPrice])");
        int indx = 0;
        for (Object item: (Iterable) ret) {
            p(item.toString());
            assertTrue(List.class.isInstance(item));
            assertEquals(((List)item).get(0), p2names[indx]);
            assertTrue(((List)item).get(1).equals(p2price[indx++]));
        }
    }

    static int i3[] = {0, 3, 4};
    @Test
    public void testSelect3() {
        p("** Query ** products.select((p,i)->{'product':p,'index':i}).");
        p("                     where(p->p.product.unitPrice >= 10).");
        p("                     select(p->p.index)");
        ret = elp.getValue("products.select((p,i)->{'product':p, 'index':i})." +
                                    "where(p->p.product.unitPrice >= 10)." +
                                    "select(p->p.index)");
        int indx = 0;
        for (Object item: (Iterable) ret) {
            p(item.toString());
            assertTrue(item.equals(i3[indx++]));
        }
    }

    static int i2[] = {10, 11, 12, 13, 14};
    @Test
    public void testSelectMany() {
        p("** Query ** customers.where(c->c.country == 'USA').");
        p("                      selectMany(c->c.orders)");
        ret = elp.getValue("customers.where(c->c.country == 'USA')." +
                                     "selectMany(c->c.orders)");
        int indx = 0;
        for (Object item: (Iterable) ret) {
            p(item.toString());
            assertEquals(((Order)item).getOrderID(), i2[indx++]);
        }
    }

    @Test
    public void testSelectMany2() {
        p("** Query ** customers.where(c->c.country == 'USA').");
        p("                      selectMany(c->c.orders, (c,o)->{'o':o,'c':c}).");
        p("                      where(co->co.o.orderData.year == 2011)");
        p("                      select(co->[co.c.name, co.o.orderID])");
        ret = elp.getValue(
                 "customers.where(c->c.country == 'USA')." +
                           "selectMany(c->c.orders, (c,o)->{'o':o,'c':c})." +
                           "where(co->co.o.orderDate.year == 2011)." +
                           "select(co->[co.c.name, co.o.orderID])");
        int indx = 0;
        for (Object item: (Iterable) ret) {
            p(item.getClass().getName() + item.toString());
            indx++;
        }
        assertTrue(indx == 3);
    }

    @Test
    public void testSelectMany2a() {
        p("** Query **");
        p("customers.where(c->c.country == 'USA').");
        p("          selectMany(c->c.orders).");
        p("          where(o->o.orderData.year == 2011).");
        p("          select(o->[customers.where(c.customerID==o->o.customerID).");
        p("                               select(c.name).");
        p("                               single(),");
        p("                     o.orderID])");

        ret = elp.getValue(
                 "customers.where(c->c.country == 'USA')." +
                           "selectMany(c->c.orders)." +
                           "where(o->o.orderDate.year == 2011)." +
                           "select(o->" +
                               "[customers.where(c->c.customerID==o.customerID)." +
                                          "select(c->c.name)." +
                                          "single()," +
                               "o.orderID])");
        int indx = 0;
        for (Object item: (Iterable) ret) {
            p(item.getClass().getName() + item.toString());
            indx++;
        }
        assertTrue(indx == 3);
    }
}

