package com.a51tgt.t4m.utils;

import android.content.Context;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.utils.WifiApUtil;
public class WiFiUtil {

    private Context mContext = null;
    private WifiManager wifiManager;
    private static WiFiUtil instance;
    public static String CurSN = "";

    public static WiFiUtil getInstance() {
        if (instance == null) {
            instance = new WiFiUtil(MZApplication.getInstance());
        }
        return instance;
    }

    private WiFiUtil(Context context) {
        mContext = context;
        checkWifiManager();
    }

    private void checkWifiManager() {
        if (wifiManager == null) {
            wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        }
    }

    public boolean setWifiEnable(boolean enable) {
        checkWifiManager();
        return wifiManager.setWifiEnabled(enable);
    }

    public boolean isWifiEnabled() {
        checkWifiManager();
        return wifiManager.isWifiEnabled();
    }

    public boolean openWiFi() {
        if (WifiApUtil.getInstance().ApIsEnabled()) {
            WifiApUtil.closeWifiAp(mContext);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!isWifiEnabled()) {
            setWifiEnable(true);
        }
        return true;
    }

    public int getWifiState() {
        checkWifiManager();
        return wifiManager.getWifiState();
    }

    // 开启wifi功能需要一段时间，所以要等到wifi状态变成WIFI_STATE_ENABLED的时候才能连接
    public boolean connectWiFi(String SSID, String Password) {
        boolean bRet;

        checkWifiManager();

        WifiConfiguration tempConfig = IsExsits(SSID);

        if (tempConfig != null) {
            bRet = wifiManager.enableNetwork(tempConfig.networkId, true);
        } else {
            WifiConfiguration wifiConfig = CreateWifiInfo(SSID, Password);
            if (wifiConfig == null) {
//                Log.i("wifiConfig create failed.");
                return false;
            }
            int netID = wifiManager.addNetwork(wifiConfig);
            bRet = wifiManager.enableNetwork(netID, true);
        }
        return bRet;
    }

    private WifiConfiguration CreateWifiInfo(String SSID, String Password) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        config.preSharedKey = "\"" + Password + "\"";
        config.hiddenSSID = true;

        // [WPA2-PSK-TKIP]
        config.preSharedKey = "\"" + Password + "\"";
        // config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.status = WifiConfiguration.Status.ENABLED;

        WifiConfiguration tempConfig = config;
        if (tempConfig != null) {
        }
        return config;
    }

    private WifiConfiguration IsExsits(String SSID) {
        checkWifiManager();

        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs == null || existingConfigs.size() == 0) {
            return null;
        }
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    public String getConnectWifiSsid() {
        checkWifiManager();

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if(!TextUtils.isEmpty(wifiInfo.getSSID())){
            CurSN = wifiInfo.getSSID().replace("*","").replace(" 途鸽","").replace("\"","");
//            Log.i("current ssid=" + wifiInfo.getSSID());
        }
        return wifiInfo.getSSID();
    }

//    private void Log.i(Object o) {
//        App.Log.i("WiFiUtil:" + o);
//    }
}
