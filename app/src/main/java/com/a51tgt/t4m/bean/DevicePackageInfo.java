package com.a51tgt.t4m.bean;

/**
 * Created by liu_w on 2017/12/14.
 */

public class DevicePackageInfo {

    private String startTime = "";// 套餐开始时间
    private String endTime = "";// 套餐结束时间
    private String totalFlow = "99999999";// 总流量
    private String surplusFlow = "99999999";// 剩余流量
    private String packageType = "";// 套餐类型
    private String packageName = "";// 套餐名
    private String countryName = "";// 国家名
    private String countryCode = "";// 国家代号
    private String dayFlow = "";// 日使用流量
    private String threeHoursFlow = "";// 中期使用流量
    private String lastUpdateTime = "";// 最后更新时间
    private String packageLevel = "";// 套餐level code
    private String dayUseMaxValue = "";// 日使用高速流量
    private String grandFlow = "";// 累计使用流量

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalFlow() {
        return totalFlow;
    }

    public void setTotalFlow(String totalFlow) {
        this.totalFlow = totalFlow;
    }

    public String getSurplusFlow() {
        return surplusFlow;
    }

    public void setSurplusFlow(String surplusFlow) {
        this.surplusFlow = surplusFlow;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDayFlow() {
        return dayFlow;
    }

    public void setDayFlow(String dayFlow) {
        this.dayFlow = dayFlow;
    }

    public String getThreeHoursFlow() {
        return threeHoursFlow;
    }

    public void setThreeHoursFlow(String threeHoursFlow) {
        this.threeHoursFlow = threeHoursFlow;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getPackageLevel() {
        return packageLevel;
    }

    public void setPackageLevel(String packageLevel) {
        this.packageLevel = packageLevel;
    }

    public String getDayUseMaxValue() {
        return dayUseMaxValue;
    }

    public void setDayUseMaxValue(String dayUseMaxValue) {
        this.dayUseMaxValue = dayUseMaxValue;
    }

    public String getGrandFlow() {
        return grandFlow;
    }

    public void setGrandFlow(String grandFlow) {
        this.grandFlow = grandFlow;
    }
}