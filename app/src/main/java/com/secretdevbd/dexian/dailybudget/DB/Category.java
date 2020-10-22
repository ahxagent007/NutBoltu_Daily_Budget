package com.secretdevbd.dexian.dailybudget.DB;

public class Category {
    int cid;
    String ctype, cname;

    public Category(int cid, String ctype, String cname) {
        this.cid = cid;
        this.ctype = ctype;
        this.cname = cname;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
