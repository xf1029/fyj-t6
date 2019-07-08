package com.a51tgt.t4m.utils;

import android.net.wifi.WifiManager;

import android.net.wifi.WifiManager;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.bean.AcessPoint;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建热点
 */
public class WifiApUtil {

    public static final String TAG = "WifiApUtil";

    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    public static final int WIFI_AP_STATE_DISABLING = 10;
    public static final int WIFI_AP_STATE_DISABLED = 11;
    public static final int WIFI_AP_STATE_ENABLING = 12;
    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_FAILED = 14;

    private WifiManager mWifiManager = null;
    private Context mContext = null;
    private static WifiApUtil instance = null;

    private String mSSID = "";
    private String mPasswd = "";

    public static void closeWifiAp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        closeWifiAp(wifiManager);
    }

    public static boolean openWifiAp(Context context, String ssid, String passwd) {
        WifiApUtil admin = getInstance();
        return admin.startWifiAp(ssid, passwd);
    }

    private WifiApUtil(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        //closeWifiAp(mWifiManager);
    }

    public static synchronized WifiApUtil getInstance() {
        if (instance == null) {
            instance = new WifiApUtil(MZApplication.getInstance());
        }
        return instance;
    }

    public boolean startWifiAp(String ssid, String passwd) {
        mSSID = ssid;
        mPasswd = passwd;
        boolean r = false;
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
        WifiConfiguration config = getApConfig();
        if (null == config || !mSSID.equals(config.SSID) || !compare(mPasswd, config.preSharedKey)) {
            closeWifiAp();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    stratWifiAp();
                }
            }, 800);
        } else {
            r = true;
        }
        return r;
    }

    public AcessPoint getWifiAp(){
        AcessPoint ap = new AcessPoint();
        WifiConfiguration configuration = getApConfig();
        if(configuration != null){
            ap.setSsid(configuration.SSID);
            ap.setPwd(configuration.preSharedKey);
        }
        return ap;
    }

    private boolean compare(String str, String str2) {
        boolean r = TextUtils.isEmpty(str2);
        if (!TextUtils.isEmpty(str)) {
            r = str.equals(str2);
        }
        return r;
    }

    public int getApState() {
        int r = WIFI_AP_STATE_FAILED;
        try {
            Method method = WifiManager.class.getMethod("getWifiApState");
            method.setAccessible(true);
            r = (Integer) method.invoke(mWifiManager);
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
        }
        return r;
    }

    public WifiConfiguration getApConfig() {
        if (isWifiApEnabled(mWifiManager)) {
            try {
                Method method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration) method.invoke(mWifiManager);
                return config;
            } catch (Exception e) {
            }
        }
        return null;
    }

    public boolean stratWifiAp() {
        Method method1 = null;
        try {
            method1 = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;

            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            if (!TextUtils.isEmpty(mPasswd)) {
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            } else {
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            }
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            method1.invoke(mWifiManager, netConfig, true);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public void closeWifiAp() {
        closeWifiAp(mWifiManager);
    }

    private static void closeWifiAp(WifiManager wifiManager) {
        if (isWifiApEnabled(wifiManager)) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);

                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class,
                        boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (Exception e) {
            }
        }
    }

    public boolean ApIsEnabled() {
        return isWifiApEnabled(mWifiManager);
    }

    private static boolean isWifiApEnabled(WifiManager wifiManager) {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);

        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
        }

        return false;
    }

    public static String getApStateString(int state) {
        String res = "";
        switch (state) {
            case WIFI_AP_STATE_DISABLING:
                res = "DISABLING";
                break;
            case WIFI_AP_STATE_DISABLED:
                res = "DISABLED";
                break;
            case WIFI_AP_STATE_ENABLING:
                res = "ENABLING";
                break;
            case WIFI_AP_STATE_ENABLED:
                res = "ENABLED";
                break;
            case WIFI_AP_STATE_FAILED:
                res = "FAILED";
                break;
            default:
                res = "UNKOWN";
                break;
        }
        return res;
    }


//    private static void Log.i(Object o) {
//        MZApplication.Log.i("WifiApUtil:" + o);
//    }

}
