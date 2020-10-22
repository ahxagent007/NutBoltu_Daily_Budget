package com.secretdevbd.dexian.dailybudget.DB;

public class Transaction {
    int tid, cid, tamount, day, month, year;
    String tnote;

    public Transaction(int tid, int cid, int tamount, int day, int month, int year, String tnote) {
        this.tid = tid;
        this.cid = cid;
        this.tamount = tamount;
        this.day = day;
        this.month = month;
        this.year = year;
        this.tnote = tnote;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getTamount() {
        return tamount;
    }

    public void setTamount(int tamount) {
        this.tamount = tamount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTnote() {
        return tnote;
    }

    public void setTnote(String tnote) {
        this.tnote = tnote;
    }
}
