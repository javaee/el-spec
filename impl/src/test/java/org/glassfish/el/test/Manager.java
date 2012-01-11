package org.glassfish.el.test;

import javax.el.ELProcessor;

public class Manager {

    ELProcessor elp;
    DataBase database = null;

    public ELProcessor getElp() {
        if (elp == null) {
            elp = new ELProcessor();
        }
        return elp;
    }

    public void setupDB() {
        if (database == null) {
            database = new DataBase();
            database.init();
        }
        getElp();
        elp.defineBean("customers", database.getCustomers());
        elp.defineBean("products", database.getProducts());
        elp.defineBean("orders", database.getOrders());
    }

    static Manager manager;

    static Manager getManager() {
        if (manager == null) {
            manager = new Manager();
        }
        return manager;
    }
}

