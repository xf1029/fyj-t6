package com.a51tgt.t4m.comm;


import android.os.Debug;
import android.os.Environment;

import com.a51tgt.t4m.BuildConfig;
import com.a51tgt.t4m.bean.DeviceInfo;
import com.a51tgt.t4m.bean.DevicePackageInfo;

/**
 * Created by liuke on 11/10/2016.
 */

public final class APIConstants {

    public final static String mac_address = BuildConfig.DEBUG ? "40:45:DA:96:3C:B8" : "";
//  public final static String mac_address = BuildConfig.DEBUG ? "" : "";
    public final static String server_domain = BuildConfig.DEBUG ? /*"192.168.0.168/TGT" : "as2.51tgt.com";//*/"mall.51tgt.com":"mall.51tgt.com";
//   public final static String server_domain = BuildConfig.DEBUG ? "as2.51tgt.com":"mall.51tgt.com";
    public final static String server_host = BuildConfig.DEBUG ? "http://" + server_domain : "http://"+server_domain;
    public final static String Get_Flow_Mall = server_host + "/FyjApp/GetFlowProducts";
    public final static String Get_Flow_Package = server_host + "/WxApp/GetDeviceInfoByQrCode";
    public final static String Get_FYJApp_Info = server_host + "/FyjApp/GetFYJAppInfo";
    public final static String Get_Download_App = server_host + "/download";
    public final static String Get_Device_Status = server_host + "/FyjApp/GetDeviceStatus";
    public final static String Device_Active = server_host + "/FyjApp/DeviceActive";
    public final static String Purchase_Product = server_host + "/FyjApp/WxPurchaseProduct";
    public final static String Purchase_Product_StripePay = server_host + "/FyjApp/StripePayProduct";

    public final static String Query_WxOrder = server_host + "/FyjApp/QueryWxOrder";
    public final static String Purchase_Product_credit = server_host + "/FyjApp/APPayProduct";
    public final static String Query_CreditOrder = server_host + "/FyjApp/QueryAPPayOrder";
 public final static String Get_Instruc = server_host + "/download/instructions/instructions.json";

    public static DeviceInfo deviceInfo = null;
    public static String deviceVersion = null;
    public static DevicePackageInfo devicePackageInfo = null;
    public static boolean isBluetoothConnection = false;
    public static String newPassword = null;
    public static String currentLan = "";

    public static boolean isScanNewDevice = false;

    public final static String FILE_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tuge/";
    public final static String FILE_DOWNLOAD = FILE_ROOT + "Download/";

    public final static String WX_APP_ID = "wxfa9d6ecb81f41530";
    public static String OUT_TRADE_NO = "";

    public final static int SCANNIN_GREQUEST_CODE = 0x1001;

    public static final String BR_ORDER_STATUS = "BR_ORDER_STATUS";
    public static final String BR_LAN_STATUS = "BR_LAN_STATUS";

    // 简体中文
    public static final String SIMPLIFIED_CHINESE = "zh";
    // 英文
    public static final String ENGLISH = "en";
    // 繁体中文


}
