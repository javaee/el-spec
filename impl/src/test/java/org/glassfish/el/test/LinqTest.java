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

    /*
     * Test a Linq query that returns an Iterable.
     * @param name of the test
     * @param query The EL query string
     * @param expected The items of the result, which should be an Iterable,
     *           when enumerated
     */
    void testIterable(String name, String query, String[] expected) {
        p("=== Testing " + name + " ===");
        p(query);
        Object ret = elp.getValue(query);
        int indx = 0;
        p(" = returns =");
        for (Object item: (Iterable) ret) {
            p(" " + item.toString());
            assertEquals(item.toString(), expected[indx++]);
        }
        assertTrue(indx == expected.length);
    }

    static String[] exp1 = {
        "Product: 200, Eagle, book, 12.5, 100",
        "Product: 203, History of Golf, book, 11.0, 30",
        "Product: 204, Toy Story, dvd, 10.0, 1000",
        "Product: 205, iSee, book, 12.5, 150"};

    @Test
    public void testWhere() {
        testIterable("where", "products.where(p->p.unitPrice >= 10)", exp1);
    }

    static String exp2[] = {"Eagle", "Coming Home", "Greatest Hits",
                       "History of Golf", "Toy Story" , "iSee"};
    @Test
    public void testSelect() {
        testIterable("select", "products.select(p->p.name)", exp2);
    }

    static String[] exp3 = {
        "[Eagle, 12.5]",
        "[History of Golf, 11.0]",
        "[Toy Story, 10.0]",
        "[iSee, 12.5]"};

    @Test
    public void testSelect2() {
        testIterable("select 2",
                     " products.where(p->p.unitPrice >= 10).\n" +
                     "          select(p->[p.name,p.unitPrice])",
                    exp3);
    }

    static String[] exp4 = {"0", "3", "4", "5"};
    @Test
    public void testSelect3() {
        testIterable("select 3",
                     " products.select((p,i)->{'product':p, 'index':i}).\n" +
                     "          where(p->p.product.unitPrice >= 10).\n" +
                     "          select(p->p.index)",
                     exp4);
    }

    static String[] exp5 = {
        "Order: 10, 100, 2/18/2010, 20.8",
        "Order: 11, 100, 5/3/2011, 34.5",
        "Order: 12, 100, 8/2/2011, 210.75",
        "Order: 13, 101, 1/15/2011, 50.23",
        "Order: 14, 101, 1/3/2012, 126.77"};

    @Test
    public void testSelectMany() {
        testIterable("selectMany",
                     " customers.where(c->c.country == 'USA').\n" +
                     "           selectMany(c->c.orders)",
                     exp5);
    }

    static String[] exp6 = {
        "[John Doe, 11]", "[John Doe, 12]", "[Mary Lane, 13]"};
    
    @Test
    public void testSelectMany2() {
        testIterable("selectMany 2",
                 " customers.where(c->c.country == 'USA').\n" +
                 "           selectMany(c->c.orders, (c,o)->{'o':o,'c':c}).\n" +
                 "           where(co->co.o.orderDate.year == 2011).\n" +
                 "           select(co->[co.c.name, co.o.orderID])",
                 exp6);
    }

    @Test
    public void testSelectMany2a() {
        testIterable("selectMany 2a",
             " customers.where(c->c.country == 'USA').\n" +
             "           selectMany(c->c.orders).\n" +
             "           where(o->o.orderDate.year == 2011).\n" +
             "           select(o-> [customers.where(c->c.customerID==o.customerID).\n" +
             "                                 select(c->c.name).\n" +
             "                                 single(),\n" +
             "                       o.orderID])",
             exp6);
    }

    static String[] exp7 = {
        "Product: 200, Eagle, book, 12.5, 100",
        "Product: 205, iSee, book, 12.5, 150",
        "Product: 203, History of Golf, book, 11.0, 30"};

    @Test
    public void testTake() {
        testIterable("take",
            " products.orderByDescending(p->p.unitPrice).\n" +
            "          take(3)",
            exp7);
    }
    
    static String[] exp8 = {
        "[John Doe, 2/18/2010, 20.8]",
        "[John Doe, 5/3/2011, 34.5]",
        "[John Doe, 8/2/2011, 210.75]",
        "[Mary Lane, 1/15/2011, 50.23]",
        "[Mary Lane, 1/3/2012, 126.77]",
        "[Charlie Yeh, 4/15/2011, 101.2]"};

    @Test
    public void testJoin() {
        testIterable("join",
            " customers.join(orders, c->c.customerID, o->o.customerID,\n" + 
            "                (c,o)->[c.name, o.orderDate, o.total])",
            exp8);
    }
    
    static String[] exp9 = {
        "[John Doe, 266.05]",
        "[Mary Lane, 177.0]",
        "[Charlie Yeh, 101.2]"};

    @Test
    public void testGroupJoin() {
        testIterable("groupJoin",
            " customers.groupJoin(orders, c->c.customerID, o->o.customerID,\n" +
            "                     (c,os)->[c.name, os.sum(o->o.total)])",
            exp9);
    }
    
    static String[] exp10 = {
        "Product: 200, Eagle, book, 12.5, 100",
        "Product: 205, iSee, book, 12.5, 150",
        "Product: 203, History of Golf, book, 11.0, 30",
        "Product: 202, Greatest Hits, cd, 6.5, 200",
        "Product: 204, Toy Story, dvd, 10.0, 1000",
        "Product: 201, Coming Home, dvd, 8.0, 50"};

    @Test
    public void testOrderBy() {
        testIterable("orderBy",
                     " products.orderBy(p->p.category).\n" + 
                     "          thenByDescending(p->p.unitPrice).\n" +
                     "          thenBy(p->p.name)",
                     exp10);
    }
    
    static String[] exp11 = {
        "Product: 201, Coming Home, dvd, 8.0, 50",
        "Product: 200, Eagle, book, 12.5, 100",
        "Product: 202, Greatest Hits, cd, 6.5, 200",
        "Product: 203, History of Golf, book, 11.0, 30",
        "Product: 205, iSee, book, 12.5, 150",
        "Product: 204, Toy Story, dvd, 10.0, 1000"};

    @Test
    public void testOrderBy2() {
        testIterable("orderBy 2",
            " products.orderBy(p->p.name,\n" +
            "                  T(java.lang.String).CASE_INSENSITIVE_ORDER)",
            exp11);
    }
}

