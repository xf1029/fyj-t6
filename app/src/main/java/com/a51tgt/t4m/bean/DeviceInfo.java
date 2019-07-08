package com.a51tgt.t4m.bean;

import android.text.TextUtils;

import com.a51tgt.t4m.utils.CommUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liu_w on 2017/9/13.
 */

/**
 * {
 "Apn1": "name:CT,mcc:460,mnc:11,apn:ctlte,type:default,dun,supl",
 "Apn2": "name:CT,mcc:460,mnc:11,apn:ctlte,type:default,dun,supl",
 "AppM1Version": "Version 2017-2-22-v2.69",
 "AppToVersion": "T3-V3.0.0",
 "AppVersion": "T3-V3.0.0",
 "CurrConnections": "1",
 "CurrSim": "2",
 "DataState0": "2",
 "DataState1": "2",
 "Network": "LTE",
 "NetworkTypeName0": "LTE",
 "NetworkTypeName1": "LTE",
 "Power": "76%",
 "Signal1": "-97",
 "Signal2": "-97",
 "SimCountryIso0": "cn",
 "SimCountryIso1": "cn",
 "SimState0": "0",
 "SimState1": "5",
 "SpeedLoad": "",
 "SpeedUp": "",
 "ToVersion": "",
 "Version": "T3_M2-V1.40",
 "VersionM1": "",
 "cellLocationGemini0": "[23058,97932337,0]",
 "cellLocationGemini1": "[23058,97932337,0]",
 "cycleUsedTraffic": "",
 "dayUsedTraffic": "1.0",
 "getFallInfoDate": "",
 "iccid0": "98683061140221477602",
 "iccid1": "98683061140221477602",
 "imei0": "869666028464376",
 "imei1": "869666028464376",
 "imsi0": "460110668160263",
 "imsi1": "460110668160263",
 "leaveSzie1": "",
 "leaveSzie2": "",
 "monUsedTraffic": "",
 "msisdn0": "",
 "msisdn1": "",
 "packType": "",
 "password": "2a3303f0",
 "rankNumber": "",
 "security": "WPA2_PSK",
 "ssid": "*TGT23170126536 途鸽",
 "tCountryNumber": "",
 "tEndTime": "1528861340207",
 "tStartTime": "1497283200",
 "tcInfo": "中国流量（China） 5G",
 "totalTraffic": "",
 "countryCode": "",
 "countryName": "",
 "dayFlow": "",
 "dayUseMaxValue": "9999999",
 "endTime": "1528861340207",
 "grandFlow": "471.6",
 "lastUpdateTime": "",
 "packageLevel": "S7",
 "packageName": "中国流量（China） 5G",
 "packageType": "",
 "startTime": "1497283200",
 "surplusFlow": "99999999",
 "threeHoursFlow": "",
 "totalFlow": "99999999"
 }
 */

public class DeviceInfo extends DevicePackageInfo{

    private String ssid = "";// 热点ssid
    private String password = "";// 热点password
    private String security = "";// WiFi加密模式
    private String Power = "";// WiFi加密模式
    private String CurrConnections = "";// WiFi连接数
    //省流量模式开关
    private boolean BlockSwitch = false;

    private String Network = "";// 网络类型
    private String Version = "";// Firmware版本号
    private String VersionM1 = "";// M1Firmware版本号
    private String AppVersion = "";// 设备app版本号
    private String AppM1Version = "";// 设备M1app版本号

    private String AppToVersion = "";// 设备app最高版本号
    private String Signal1 = "";// 卡槽一信号强度
    private String Signal2 = "";// 卡槽二信号强度
    private String Apn1 = "";// 卡一APN
    private String Apn2 = "";// 卡二APN
    private String DataState0 = "";// 卡一数据链接状态码
    private String DataState1 = "";// 卡二数据链接状态码
    private String NetworkTypeName0 = "";// 卡一网络注册码
    private String NetworkTypeName1 = "";// 卡二网络注册码
    private String SimState0 = "";// 卡一SIM状态码
    private String SimState1 = "";// 卡二SIM状态码
    private String SimCountryIso0 = "";// 卡一SIM国家码
    private String SimCountryIso1 = "";// 卡二SIM国家码
    private String CurrSim = "";// 当前SIM
    private String iccid0 = "";// 卡一SIMiccid号
    private String iccid1 = "";// 卡二SIMiccid号
    private String imsi0 = "";// 卡一SIMimsi号
    private String imsi1 = "";// 卡二SIMimsi号
    private String msisdn0 = "";// 卡一SIM卡号
    private String msisdn1 = "";// 卡二SIM卡号
    private String cellLocationGemini0 = "";// 卡槽一基站信息
    private String cellLocationGemini1 = "";// 卡槽二基站信息
    private String imei0 = "";// 卡槽一IMEI
    private String imei1 = "";// 卡槽二基站信息
    private String leaveSzie1 = "";//
    private String leaveSzie2 = "";//
    private String dayUsedTraffic = "";// 日使用流量
    private String SpeedUp = "";//
    private String SpeedLoad = "";//
    private String ToVersion = "";// Firmware最新版本
    private String rankNumber = "";//
    private String tStartTime = "";// 套餐开始时间
    private String tEndTime = "";// 套餐结束时间
    private String cycleUsedTraffic = "";// 周期使用流量
    private String getFallInfoDate = "";// 获取流量时间
    private String monUsedTraffic = "";// 月使用流量
    private String tCountryNumber = "";// 国家码
    private String packType = "";// 套餐包类型
    private String totalTraffic = "";// 总使用流量
    private String tcInfo = "";// 套餐名称
    private long currentTimeDevice=0;
    private String IPAddress="";//ip地址
    private String plmn="";//网络运营商
    private boolean roamingState=false;//漫游状态
    private String bluetoothMac="";//蓝牙mac地址
    private String deviceType="";//设备类型
    private boolean isFlow = false;

    public DeviceInfo(String ssid)

    {

        this.ssid = ssid;
    }

    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getBluetoothMac() {
        return bluetoothMac;
    }
    public void setBluetoothMac(String bluetoothMac) {
        this.bluetoothMac = bluetoothMac;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getPlmn() {
        return plmn;
    }

    public void setPlmn(String plmn) {
        this.plmn = plmn;
    }

    public boolean isRoamingState() {
        return roamingState;
    }

    public boolean isBlockSwitch() {
        return BlockSwitch;
    }

    public void setRoamingState(boolean roamingState) {
        this.roamingState = roamingState;
    }


    public void setisFlow(boolean isFlow) {
        this.isFlow = isFlow;
    }

    public void setBlockSwitch(boolean blockSwitch) {
        this.BlockSwitch = blockSwitch;}
    public long getCurrentTimeDevice() {
        return currentTimeDevice;
    }

    public void setCurrentTimeDevice(long currentTimeDevice) {
        this.currentTimeDevice = currentTimeDevice;
    }

    public String getSsid() {
        String temp = ssid;
        if(!TextUtils.isEmpty(temp) && temp.startsWith("*"))
            temp = temp.replace("*", "");

        if(!TextUtils.isEmpty(temp) && temp.startsWith("TUGE"))
            return CommUtil.getNumberAndCharacter(temp.replace("TUGE","GTWiFi"));
        else
            return CommUtil.getNumberAndCharacter(temp);
    }

    public boolean isFlow() {
        return isFlow;
    }


    public String getOrginalSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getPower() {
        return Power;
    }

    public void setPower(String power) {
        Power = power;
    }


    public String getCurrConnections() {
        return CurrConnections;
    }

    public void setCurrConnections(String currConnections) {
        CurrConnections = currConnections;
    }

    public String getNetwork() {
        return Network;
    }

    public void setNetwork(String network) {
        Network = network;
    }

    public String getVersion() {
        return Version;
    }

    public String getVersionPrefix() {
        if(!TextUtils.isEmpty(AppVersion)){
            String[] subs = AppVersion.split("_");
            if(subs != null && subs.length > 0)
                return subs[0];
        }
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getVersionM1() {
        return VersionM1;
    }

    public void setVersionM1(String versionM1) {
        VersionM1 = versionM1;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public String getAppM1Version() {
        return AppM1Version;
    }

    public void setAppM1Version(String appM1Version) {
        AppM1Version = appM1Version;
    }

    public String getAppToVersion() {
        return AppToVersion;
    }

    public void setAppToVersion(String appToVersion) {
        AppToVersion = appToVersion;
    }

    public int getSignal1() {
        try {
            return (int)Float.parseFloat(Signal1);
        }
        catch (Exception e){

        }
        return -100;
    }

    public void setSignal1(String signal1) {
        Signal1 = signal1;
    }

    public String getSignal2() {
        return Signal2;
    }

    public void setSignal2(String signal2) {
        Signal2 = signal2;
    }

    public String getApn1() {
        return Apn1;
    }

    public void setApn1(String apn1) {
        Apn1 = apn1;
    }

    public String getApn2() {
        return Apn2;
    }

    public void setApn2(String apn2) {
        Apn2 = apn2;
    }

    public String getDataState0() {
        return DataState0;
    }

    public void setDataState0(String dataState0) {
        DataState0 = dataState0;
    }

    public String getDataState1() {
        return DataState1;
    }

    public void setDataState1(String dataState1) {
        DataState1 = dataState1;
    }

    public String getNetworkTypeName0() {
        return NetworkTypeName0;
    }

    public void setNetworkTypeName0(String networkTypeName0) {
        NetworkTypeName0 = networkTypeName0;
    }

    public String getNetworkTypeName1() {
        return NetworkTypeName1;
    }

    public void setNetworkTypeName1(String networkTypeName1) {
        NetworkTypeName1 = networkTypeName1;
    }

    public String getSimState0() {
        return SimState0;
    }

    public void setSimState0(String simState0) {
        SimState0 = simState0;
    }

    public String getSimState1() {
        return SimState1;
    }

    public void setSimState1(String simState1) {
        SimState1 = simState1;
    }

    public String getSimCountryIso0() {
        return SimCountryIso0;
    }

    public void setSimCountryIso0(String simCountryIso0) {
        SimCountryIso0 = simCountryIso0;
    }

    public String getSimCountryIso1() {
        return SimCountryIso1;
    }

    public void setSimCountryIso1(String simCountryIso1) {
        SimCountryIso1 = simCountryIso1;
    }

    public String getCurrSim() {
        return CurrSim;
    }

    public void setCurrSim(String currSim) {
        CurrSim = currSim;
    }

    public String getIccid0() {
        return iccid0;
    }

    public void setIccid0(String iccid0) {
        this.iccid0 = iccid0;
    }

    public String getIccid1() {
        return iccid1;
    }

    public void setIccid1(String iccid1) {
        this.iccid1 = iccid1;
    }

    public String getImsi0() {
        return imsi0;
    }

    public void setImsi0(String imsi0) {
        this.imsi0 = imsi0;
    }

    public String getImsi1() {
        return imsi1;
    }

    public void setImsi1(String imsi1) {
        this.imsi1 = imsi1;
    }

    public String getMsisdn0() {
        return msisdn0;
    }

    public void setMsisdn0(String msisdn0) {
        this.msisdn0 = msisdn0;
    }

    public String getMsisdn1() {
        return msisdn1;
    }

    public void setMsisdn1(String msisdn1) {
        this.msisdn1 = msisdn1;
    }

    public String getCellLocationGemini0() {
        return cellLocationGemini0;
    }

    public void setCellLocationGemini0(String cellLocationGemini0) {
        this.cellLocationGemini0 = cellLocationGemini0;
    }

    public String getCellLocationGemini1() {
        return cellLocationGemini1;
    }

    public void setCellLocationGemini1(String cellLocationGemini1) {
        this.cellLocationGemini1 = cellLocationGemini1;
    }

    public String getImei0() {
        return imei0;
    }

    public void setImei0(String imei0) {
        this.imei0 = imei0;
    }

    public String getImei1() {
        return imei1;
    }

    public void setImei1(String imei1) {
        this.imei1 = imei1;
    }

    public String getLeaveSzie1() {
        return leaveSzie1;
    }

    public void setLeaveSzie1(String leaveSzie1) {
        this.leaveSzie1 = leaveSzie1;
    }

    public String getLeaveSzie2() {
        return leaveSzie2;
    }

    public void setLeaveSzie2(String leaveSzie2) {
        this.leaveSzie2 = leaveSzie2;
    }

    public String getDayUsedTraffic() {
        return dayUsedTraffic;
    }

    public void setDayUsedTraffic(String dayUsedTraffic) {
        this.dayUsedTraffic = dayUsedTraffic;
    }

    public String getSpeedUp() {
        return SpeedUp;
    }

    public void setSpeedUp(String speedUp) {
        SpeedUp = speedUp;
    }

    public String getSpeedLoad() {
        return SpeedLoad;
    }

    public void setSpeedLoad(String speedLoad) {
        SpeedLoad = speedLoad;
    }

    public String getToVersion() {
        return ToVersion;
    }

    public void setToVersion(String toVersion) {
        ToVersion = toVersion;
    }

    public String getRankNumber() {
        return rankNumber;
    }

    public void setRankNumber(String rankNumber) {
        this.rankNumber = rankNumber;
    }

    public String gettStartTime() {
        return tStartTime;
    }

    public void settStartTime(String tStartTime) {
        this.tStartTime = tStartTime;
    }

    public String gettEndTime() {
        return tEndTime;
    }

    public void settEndTime(String tEndTime) {
        this.tEndTime = tEndTime;
    }

    public String getCycleUsedTraffic() {
        return cycleUsedTraffic;
    }

    public void setCycleUsedTraffic(String cycleUsedTraffic) {
        this.cycleUsedTraffic = cycleUsedTraffic;
    }

    public String getGetFallInfoDate() {
        return getFallInfoDate;
    }

    public void setGetFallInfoDate(String getFallInfoDate) {
        this.getFallInfoDate = getFallInfoDate;
    }

    public String getMonUsedTraffic() {
        return monUsedTraffic;
    }

    public void setMonUsedTraffic(String monUsedTraffic) {
        this.monUsedTraffic = monUsedTraffic;
    }

    public String gettCountryNumber() {
        return tCountryNumber;
    }

    public void settCountryNumber(String tCountryNumber) {
        this.tCountryNumber = tCountryNumber;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getTotalTraffic() {
        return totalTraffic;
    }

    public void setTotalTraffic(String totalTraffic) {
        this.totalTraffic = totalTraffic;
    }

    public String getTcInfo() {
        return tcInfo;
    }

    public void setTcInfo(String tcInfo) {
        this.tcInfo = tcInfo;
    }
}
