package com.secretdevbd.dexian.dailybudget.DB;

public class Budget {

    int bid, cid, bamount, year, month;

    public Budget(int bid, int cid, int bamount, int year, int month) {
        this.bid = bid;
        this.cid = cid;
        this.bamount = bamount;
        this.year = year;
        this.month = month;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getBamount() {
        return bamount;
    }

    public void setBamount(int bamount) {
        this.bamount = bamount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
