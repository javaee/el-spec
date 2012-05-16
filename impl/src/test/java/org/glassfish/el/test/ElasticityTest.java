/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.glassfish.el.test;

import java.util.*;
import javax.el.*;
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
public class ElasticityTest {

    ELProcessor elp;

    public ElasticityTest() {
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

    static public class Data {
        int s;
        int d;

        public Data(int s, int d) {
            this.s = s;
            this.d = d;
        }

        public int getS() {
            return this.s;
        }

        public int getD() {
            return this.d;
        }
    }

    static public class Metric {
        int limit;
        List<Data> list = new ArrayList<Data>();
        
        public Metric(int limit) {
            this.limit = limit;
        }
        
        public int getLimit() {
            return limit;
        }
        
        public List<Data> getList() {
            return list;
        }
    }
    Map<String, Metric> clusters = new HashMap<String, Metric>();

    private void init() {
        Metric m1 = new Metric(10);
        m1.getList().add(new Data(1, 80));
        m1.getList().add(new Data(3, 90));
        m1.getList().add(new Data(4, 100));
        m1.getList().add(new Data(5, 50));
        m1.getList().add(new Data(6, 60));

        Metric m2 = new Metric(10);
        m2.getList().add(new Data(1, 80));
        m2.getList().add(new Data(3, 82));
        m2.getList().add(new Data(7, 90));
        m2.getList().add(new Data(9, 140));
        m2.getList().add(new Data(15, 80));

        Metric m3 = new Metric(10);
        m3.getList().add(new Data(4, 100));
        m3.getList().add(new Data(5, 81));
        m3.getList().add(new Data(6, 200));
        m3.getList().add(new Data(20, 80));

        clusters.put("c1", m1);
        clusters.put("c2", m2);
        clusters.put("c3", m3);

        elp.defineBean("c", clusters);
    }
    @Test
    public void testElaticity() {
        init();
        Object obj;
        
        obj = elp.eval(
            "c.values().select(" +
                "v->v.list.where(d->d.s>1 && d.s<10)." +
                          "average(d->d.d)).toList()");

        System.out.println(obj);
        obj = elp.eval(
            "c.values().select(v->v.list." +
                       "where(d->d.s>1 && d.s<10)." +
                       "average(d->d.d) > 100).toList()");
        System.out.println(obj);
        obj = elp.eval(
            "c.values().select(v->v.list." +
                       "where(d->d.s>1 && d.s<10)." +
                       "average(d->d.d) > 100).any()");
        System.out.println(obj);
        obj = elp.eval(
            "c.entrySet().select(s->[s.key, s.value.list." +
                       "where(d->d.s>1 && d.s<10)." +
                       "average(d->d.d)]).toList()");
        System.out.println(obj);
    }
}
