package org.glassfish.el.test;

import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.el.ELProcessor;

public class StreamTest {

    static ELProcessor elp;
    static DataBase database = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
        elp = new ELProcessor();
        database = new DataBase();
        database.init();
        elp.defineBean("customers", database.getCustomers());
        elp.defineBean("products", database.getProducts());
        elp.defineBean("orders", database.getOrders());
    }

    @Before
    public void setup() {
    }

    void p(String msg) {
        System.out.println(msg);
    }
    /**
     * Test a Linq query that returns an Iterable.
     * @param name of the test
     * @param query The EL query string
     * @param expected The expected result of the Iterable.  The array
     *           element should equal the Iterable element, when enumerated.
     */

    void testStream(String name, String query, String[] expected) {
        p("=== Testing " + name + " ===");
        p(query);
        Object ret = elp.eval(query);
        int indx = 0;
        p(" = returns =");
        Iterator<Object> iter = ((javax.el.streams.Stream)ret).iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            p(" " + item.toString());
            assertEquals(item.toString(), expected[indx]);
            indx++;
        }
        assertTrue(indx == expected.length);
    }

    static String[] exp0 = {"1", "2", "3", "4", "5", "6"};
    static String[] exp1 = {"6", "5", "4", "3", "2", "1"};
    static String[] exp2 = {"q", "z", "yz", "aaa", "abc", "xyz"};
    static String[] exp3 = {"2", "3", "4"};
    static String[] exp4 = {"20", "30", "40"};

    @Test
    public void testFilterMap() {
        testStream("filter", "[1,2,3,4].stream().filter(i->i > 1)", exp3);
        testStream("map", "[2,3,4].stream().map(i->i*10)", exp4);
        testStream("filtermap", "[1,2,3,4].stream().filter(i->i > 1)\n" +
                                "                  .map(i->i*10)", exp4);
    }

    static String[] exp5 = {
        "Product: 201, Coming Home, dvd, 8.0, 50",
        "Product: 200, Eagle, book, 12.5, 100",
        "Product: 202, Greatest Hits, cd, 6.5, 200",
        "Product: 203, History of Golf, book, 11.0, 30",
        "Product: 204, Toy Story, dvd, 10.0, 1000",
        "Product: 205, iSee, book, 12.5, 150"};

    static String[] exp6 = {
        "Product: 203, History of Golf, book, 11.0, 30",
        "Product: 200, Eagle, book, 12.5, 100",
        "Product: 205, iSee, book, 12.5, 150",
        "Product: 202, Greatest Hits, cd, 6.5, 200",
        "Product: 201, Coming Home, dvd, 8.0, 50",
        "Product: 204, Toy Story, dvd, 10.0, 1000"};

    @Test
    public void testSorted() {
        testStream("sorted", "[1, 3, 5, 2, 4, 6].stream().sorted((i,j)->i-j)", exp0);
        testStream("sorted", "[1, 3, 5, 2, 4, 6].stream().sorted((i,j)->i.compareTo(j))", exp0);
        testStream("sorted", "['2', '4', '6', '5', '3', '1'].stream().sorted((s, t)->s.compareTo(t))", exp0);
        testStream("sorted", "[1, 3, 5, 2, 4, 6].stream().sorted((i,j)->j.compareTo(i))", exp1);
        testStream("sorted", "['xyz', 'yz', 'z', 'abc', 'aaa', 'q'].stream().sorted" +
                "((s,t)->(s.length()== t.length()? s.compareTo(t): s.length() - t.length()))",
                exp2);
        elp.eval("comparing = map->(x,y)->map(x).compareTo(map(y))");
        testStream("sorted", "products.stream().sorted(" +
            "(x,y)->x.name.compareTo(y.name))", exp5);
        testStream("sorted", "products.stream().sorted(" +
            "comparing(p->p.name))", exp5);
        elp.eval("compose = (m1,m2)->(x,y)->(tx = m1(x).compareTo(m1(y)); "
                + "tx!=0? tx: (m2(x).compareTo(m2(y))))");
        testStream("sorted", "products.stream().sorted(" +
                "compose(p->p.category, p->p.unitPrice))", exp6);
        testStream("sort", "lst = [1, 3, 5, 2, 4, 6]; lst.sort(comparing(i->i)); lst.stream()", exp0);
    }

    static String exp8[] = {"Eagle", "Coming Home", "Greatest Hits",
                       "History of Golf", "Toy Story" , "iSee"};

    @Test
    public void testForEach() {
        testStream("forEach",
            "lst = []; products.forEach(p->lst.add(p.name)); lst.stream()", exp8);
    }

    static String[] exp7 = {
        "Order: 10, 100, 2/18/2010, 20.8",
        "Order: 11, 100, 5/3/2011, 34.5",
        "Order: 12, 100, 8/2/2011, 210.75",
        "Order: 13, 101, 1/15/2011, 50.23",
        "Order: 14, 101, 1/3/2012, 126.77"};

    @Test
    public void testFlatMap() {
        testStream("flatMap",
            "customers.stream().filter(c->c.country=='USA')\n" +
            "                  .flatMap((s,c)->c.orders.forEach(o->s.apply(o)))",
            exp7);
        elp.eval("mapBy = m->(s,c)->m(c).forEach(o->s.apply(o))");
        testStream("flatMapBy",
            "customers.stream().filter(c->c.country=='USA')\n" +
            "                  .flatMap(mapBy(c->c.orders))",
            exp7);
    }
}
