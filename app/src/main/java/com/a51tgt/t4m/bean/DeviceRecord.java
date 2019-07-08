package com.a51tgt.t4m.bean;

/**
 * Created by pc on 2017/9/14.
 */
public class DeviceRecord {
    String ssid;
    String pwd;
    String bt_mac;
    long lastLogin;

    public DeviceRecord(String ssid, String pwd, long lastLogin){
        this.ssid=ssid;
        this.pwd = pwd;
        this.lastLogin = lastLogin;
    }

    public DeviceRecord(String ssid, long lastLogin){
        this.ssid=ssid;
        this.lastLogin = lastLogin;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public String getBt_mac() {
        return bt_mac;
    }

    public void setBt_mac(String bt_mac) {
        this.bt_mac = bt_mac;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public long getLastLogin() {
        return lastLogin;
    }
}
