package com.secretdevbd.dexian.dailybudget.DB;

public class Setting {
    int sid;
    String sname, ssetting;

    public Setting(int sid, String sname, String ssetting) {
        this.sid = sid;
        this.sname = sname;
        this.ssetting = ssetting;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSsetting() {
        return ssetting;
    }

    public void setSsetting(String ssetting) {
        this.ssetting = ssetting;
    }
}
