package org.glassfish.el.test;

import java.util.List;
import java.util.ArrayList;

public class Customer {
    int customerID;
    String name;
    String address;
    String city;
    String country;
    String phone;
    List<Order> orders;

    public Customer(int customerID, String name, String address, String city,
             String country, String phone) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.orders = new ArrayList<Order>();
    }

    public String toString() {
        return "Customer: " + customerID + ", " + name + ", " + city + ", " +
                country;
    }

    public int getCustomerID() { return customerID;}
    public String getName() { return name;}
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getPhone() { return phone; }
    public List<Order> getOrders() { return orders; }
}
