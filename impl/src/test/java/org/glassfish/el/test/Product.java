package org.glassfish.el.test;

public class Product {

    public int productID; 
    public String name;
    public String category;
    public double unitPrice;
    public int unitsInStock;

    Product (int productID, String name, String category,
             double unitPrice, int unitsInStock) {

        this.productID = productID;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
    }

    public String toString() {
        return "Product: " + productID + ", " + name + ", " +
            category + ", " + unitPrice + ", " + unitsInStock;
    }

    public int getProductID() { return productID; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getUnitPrice() { return unitPrice; }
    public int getUnitsInStock() { return unitsInStock; }

}
