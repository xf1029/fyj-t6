package com.a51tgt.t4m.abstract_face;

/**
 * Created by pc on 2017/9/15.
 */

public interface OnNoticeUI {
    String KEY_TYPE_SPLASH_ACTIVITY = "splash_activity";
    String KEY_TYPE_DEVICE_INFO_FRAGMENT = "device_info_fragment";
    String KEY_TYPE_MAIN_ACTIVITY = "main_activity";
    String KEY_TYPE_WIFI_ACTIVITY = "device_connect_wifi_activity";
    String KEY_TYPE_BT_LOGIN_ACTIVITY = "bt_login_activity";
    String KEY_TYPE_MAIN_FRAGMENT = "main_fragment";
    String KEY_TYPE_DEVICE_SETTING_FRAGMENT = "device_setting_fragment";
    String KEY_TYPE_DEVICE_REDUCTION = "reduction_activity";
    String KEY_TYPE_SAVE_FLOW = "save_flow_activity";
    String KEY_TYPE_PING_FRAGMENT = "ping_fragment";
    String KEY_TYPE_HTTP_FRAGMENT = "http_fragment";
    String KEY_TYPE_SPEED_FRAGMENT = "speed_fragment";
    String KEY_TYPE_RESTART_FRAGMENT = "restart_fragment";
    String KEY_TYPE_DEVICE_CONTROL_FRAGMENT = "device_control_fragment";
    String KEY_TYPE_NET_RECORD_FRAGMENT = "net_record_fragment";
    String KEY_TYPE_AUTHEN_INFO_FRAGMENT = "authen_info_fragment";
    String KEY_TYPE_CONNECT_RATE_FRAGMENT = "connect_rate_fragment";
    String KEY_TYPE_DEVICE_SET = "device_set_dialog";

    int NOTICE_TPYE_BT_SUCCESS= -3;
    int NOTICE_TYPE_TCP_SUCCESS = -2;
    int NOTICE_TYPE_TCP_FAILED = -1;
    int NOTICE_TYPE_DEVICE_INFO = 0;
    int NOTICE_TYPE_PING_RES = 1;
    int NOTICE_TYPE_PING_SWITCH = 2;
    int NOTICE_TYPE_HTTP_SWITCH = 3;
    int NOTICE_TYPE_HTTP_RES = 4;
    int NOTICE_TYPE_SPEED_RES = 5;
    int NOTICE_TYPE_SPEED_SWITCH = 6;
    int NOTICE_TYPE_SPEED_PROGRESS_UP = 7;
    int NOTICE_TYPE_SPEED_PROGRESS_DOWN = 8;
    int NOTICE_TYPE_RESTART_SWITCH = 9;
    int NOTICE_TYPE_RESTART_RES = 10;
    int NOTICE_TYPE_RESTART_START_SUCCESS = 11;
    int NOTICE_TYPE_RESTART_STOP_SUCCESS = 12;
    int NOTICE_TYPE_RESTART_START_FAIL = 13;
    int NOTICE_TYPE_RESTART_STOP_FAIL = 14;
    int NOTICE_TYPE_SET_LANGUAGE_RES = 15;
    int NOTICE_TYPE_GET_DEVICE_LOGS_SUCCESS = 16;
    int NOTICE_TYPE_GET_DEVICE_LOGS_FAIL = 17;
    int NOTICE_TYPE_SEND_APK_SUCCESS = 18;
    int NOTICE_TYPE_SEND_APK_FAIL = 19;
    int NOTICE_TYPE_SET_CARD_STATE = 20;
    int NOTICE_TYPE_SET_NETWIRK_TYPE = 21;
    int NOTICE_TYPE_NET_RECORD_SWITCH = 22;
    int NOTICE_TYPE_NET_RECORD_RES = 23;
    int NOTICE_TYPE_AUTHEN_START_BUTTON = 24;
    int NOTICE_TYPE_AUTHEN_RES = 25;
    int NOTICE_TYPE_CONNECT_RATE_START_BUTTON = 26;
    int NOTICE_TYPE_CONNECT_RATE_RES = 27;
    int NOTICE_TYPE_OPEN_BLUETOOTH = 28;
    int NOTICE_TYPE_BLUETOOTH_CONNECTED = 29;
    int NOTICE_TYPE_BT_MSG_CURRENT_AP = 30;
    int NOTICE_TYPE_BT_MSG_SAVED_AP = 31;
    int NOTICE_TYPE_BT_MSG_SEARCH_AP = 32;
    int NOTICE_TYPE_CONNECT_PROCEDURE = 33;
    int NOTICE_TYPE_BT_DEVICE_INFO=34;
    int NOTICE_TYPE_BT_DEVICE_LANG=44;

    int NOTICE_TYPE_BT_PACKAGE_INFO=35;
    int NOTICE_TYPE_BLUETOOTH_CANNOT_CONNECTED = 36;
    int NOTICE_TYPE_CLOSE_WIFIAP = 37;
    int NOTICE_TYPE_SET_WIFIANDPASSWORD = 38;
    int NOTICE_TYPE_SET_APN = 39;
    int NOTICE_TYPE_SET_BLACK_LIST = 40;
    int NOTICE_TYPE_BT_DEVICE_INFO2=41;
    int NOTICE_TYPE_BT_DEVICE_REDUTION=42;
    int NOTICE_TYPE_BT_APN_QUERY=43;
    int NOTICE_TYPE_NET_CHANGE=45;



    void onNotice(int NOTICE_TYPE, Object object);
}
