package com.a51tgt.t4m.bean;

/**
 * Created by pc on 2017/10/24.
 */

public class AcessPoint {
    String ssid;
    String pwd;
    int rssi;
    String state = "";

    public AcessPoint(){

    }

    public AcessPoint(String ssid, String pwd, int rssi, String state){
        this.ssid = ssid;
        this.pwd = pwd;
        this.rssi = rssi;
        this.state = state;
    }

    public int getRssi() {
        return rssi;
    }

    public String getPwd() {
        return pwd;
    }

    public String getSsid() {
        return ssid;
    }

    public String getState() {
        return state;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public void setState(String state) {
        this.state = state;
    }
}
