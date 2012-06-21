package org.glassfish.el.test;

public class Date {
    int year, month, date;

    public Date(int year, int month, int date) {
        this.year = year;
        this.month = month;
        this.date = date;
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDate() { return date; }

    public String toString() {
        return "" + month + "/" + date + "/" + year;
    }
}
