package com.a51tgt.t4m.config;

/**
 * Created by Administrator on 2017/8/25.
 */

public interface TcpConfig {
    int port = 51848;
    String URL_UPLOAD = "http://120.26.224.101:8080/web/FileUploadServlet";

    // 以下参数为与途鸽WiFi App等工具app交互接口
    String KEY = "key";
    String VALUE = "value";

    String _HEAD_ = "b6E6L255EOCdJ9NplKPuUkn87VmwCCcC";

    String GET_DEVICE_INFO                  = "CMD_GET_DEVICE_INFO";
    String GET_PACKAGE_INFO                 = "CMD_GET_PACKAGE_INFO";
    String GET_DEVICE_LOGS                  = "CMD_GET_DEVICE_LOGS";
    String GET_DEVICE_ALL_LOGS              = "CMD_GET_DEVICE_ALL_LOGS";
    String OPEN_ADB                         = "CMD_OPEN_ADB";
    String CLOSE_ADB                        = "CMD_CLOSE_ADB";
    String OPEN_MODE_LOG                    = "CMD_OPEN_MODE_LOG";
    String CLOSE_MODE_LOG                   = "CMD_CLOSE_MODE_LOG";
    String REBOOT                           = "CMD_REBOOT";
    String POWER_OFF                        = "CMD_POWER_OFF";
    String OPEN_FLY_MODE                    = "CMD_OPEN_FLY_MODE";
    String CLOSE_FLY_MODE                   = "CMD_CLOSE_FLY_MODE";
    String RESTART                          = "CMD_RESTART";
    String OPEN_DATA                        = "CMD_OPEN_DATA";
    String CLOSE_DATA                       = "CMD_CLOSE_DATA";
    String OPEN_WIFIAP                      = "CMD_OPEN_WIFIAP";
    String CLOSE_WIFIAP                     = "CMD_CLOSE_WIFIAP";
    String CONTROL_SPEED_TRUE               = "CMD_CONTROL_SPEED_TRUE";
    String CONTROL_SPEED_FALSE              = "CMD_CONTROL_SPEED_FALSE";
    String SET_FULL_SCREEN_TRUE             = "CMD_SET_FULL_SCREEN_TRUE";
    String SET_FULL_SCREEN_FALSE            = "CMD_SET_FULL_SCREEN_FALSE";
    String SWITCH_DATA                      = "CMD_SWITCH_DATA";
    String SWITCH_FLY_MODE                  = "CMD_SWITCH_FLY_MODE";
    String SET_APN                          = "CMD_SET_APN";
    String SET_WIFIANDPASSWORD              = "CMD_SET_WIFIANDPASSWORD";
    String SET_DATA_ROAMING_TRUE            = "CMD_SET_DATA_ROAMING_TRUE";
    String SET_DATA_ROAMING_FALSE           = "CMD_SET_DATA_ROAMING_FALSE";
    String SET_BLACK_LIST                   = "CMD_SET_BLACK_LIST";
    String GET_HOT_SPOT_CLIENTS             = "CMD_GET_HOT_SPOT_CLIENTS";
    String GET_BASIC_CONFIGLIST             = "CMD_GET_BASIC_CONFIGLIST";
    String SET_NETWORK_TYPE                 = "CMD_SET_NETWORK_TYPE";
    String GET_NETWORK_TYPE                 = "CMD_GET_NETWORK_TYPE";
    String SET_CARD_STATE                   = "CMD_SET_CARD_STATE";
    String GET_CARD_STATE                   = "CMD_GET_CARD_STATE";
    String DELETE_MODEM_LOG                 = "CMD_DELETE_MODEM_LOG";
    String SET_DEVICE_LANGUAGE              = "CMD_SET_DEVICE_LANGUAGE";
    String ENABLE_SSH_SERVICE               = "CMD_ENABLE_SSH_SERVICE";
    String FOTA_UPDATE_CHECK                = "CMD_FOTA_UPDATE_CHECK";
    String BLACK_LIST_ENABLE                = "CMD_BLACK_LIST_ENABLE";
    String RESTART_TEST_TASK                = "CMD_RESTART_TEST_TASK";
    String RESTART_TEST_CANCEL              = "CMD_RESTART_TEST_CANCEL";
    String GET_REAL_TIME_LOGS               = "CMD_GET_REAL_TIME_LOGS";
    String CANCEL_REAL_TIME_LOGS            = "CMD_CANCEL_REAL_TIME_LOGS";
    String COPY_M1_LOGS_TO_M2               = "CMD_COPY_M1_LOGS_TO_M2";
    String SET_SSH_DEBUG                    = "CMD_SET_SSH_DEBUG";
    String OPEN_REALSIM                     = "CMD_OPEN_REALSIM";
    String CLOSE_REALSIM                    = "CMD_CLOSE_REALSIM";
    String RESTART_TEST_GET_RES             = "CMD_RESTART_TEST_GET_RES";
    String RESTART_TEST_GET_AUTHEN          = "CMD_RESTART_TEST_GET_AUTHEN";
    String RELEASE_CARD_SIMULATION          = "CMD_RELEASE_CARD_SIMULATION";
    String OPEN_BLUETOOTH_CONNECT           = "CMD_OPEN_BLUETOOTH_CONNECT";
    String BT_OPEN_WIFI                     = "CMD_BT_OPEN_WIFI";
    String BT_CLOSE_WIFI                    = "CMD_BT_CLOSE_WIFI";
    String BT_CONNECT_TO_AP                 = "CMD_BT_CONNECT_TO_AP";
    String BT_FORGET_SAVED_AP               = "CMD_BT_FORGET_SAVED_AP";
    String BT_DISCONNECT_AP				    = "CMD_BT_DISCONNECT_AP";
    String BT_MSG_FROM_DEVICE               = "CMD_BT_MSG_FROM_DEVICE";
    String BT_SEND_APK                      = "CMD_BT_SEND_APK";
    String BT_COPY_FILE                     = "CMD_BT_COPY_FILE";
    String BT_DELETE_FILE                   = "CMD_BT_DELETE_FILE";
    String BT_GET_MAC						= "CMD_BT_GET_MAC";
    String BLE_START_WIFI_TRACKER           = "CMD_BLE_START_WIFI_TRACKER";
    String BLE_STOP_WIFI_TRACKER            = "CMD_BLE_STOP_WIFI_TRACKER";

    String[] cmds = new String[]{GET_DEVICE_INFO, GET_PACKAGE_INFO, GET_DEVICE_LOGS,
            GET_DEVICE_ALL_LOGS, OPEN_ADB, CLOSE_ADB, REBOOT, POWER_OFF, OPEN_FLY_MODE, CLOSE_FLY_MODE, RESTART,
            OPEN_DATA, CLOSE_DATA, OPEN_WIFIAP, CLOSE_WIFIAP, CONTROL_SPEED_TRUE, CONTROL_SPEED_FALSE,
            SET_FULL_SCREEN_TRUE, SET_FULL_SCREEN_FALSE, SWITCH_DATA, SWITCH_FLY_MODE, SET_APN, SET_DATA_ROAMING_TRUE,
            SET_DATA_ROAMING_FALSE, SET_WIFIANDPASSWORD, SET_BLACK_LIST, GET_HOT_SPOT_CLIENTS, GET_BASIC_CONFIGLIST,
            OPEN_MODE_LOG, CLOSE_MODE_LOG, SET_NETWORK_TYPE, GET_NETWORK_TYPE, DELETE_MODEM_LOG, GET_CARD_STATE,
            SET_CARD_STATE, SET_DEVICE_LANGUAGE, ENABLE_SSH_SERVICE, FOTA_UPDATE_CHECK, BLACK_LIST_ENABLE,
            RESTART_TEST_TASK, RESTART_TEST_CANCEL,GET_REAL_TIME_LOGS,CANCEL_REAL_TIME_LOGS,
            COPY_M1_LOGS_TO_M2,SET_SSH_DEBUG,OPEN_REALSIM,CLOSE_REALSIM,RESTART_TEST_GET_RES,RESTART_TEST_GET_AUTHEN,
            RELEASE_CARD_SIMULATION,OPEN_BLUETOOTH_CONNECT,BT_OPEN_WIFI,BT_CLOSE_WIFI,BT_CONNECT_TO_AP,BT_FORGET_SAVED_AP,
            BT_MSG_FROM_DEVICE,BT_DISCONNECT_AP,BT_SEND_APK,BT_COPY_FILE,BT_DELETE_FILE,BT_GET_MAC,BLE_START_WIFI_TRACKER,
            BLE_STOP_WIFI_TRACKER};

    String DATA_TYPE_RESTART_TASK_ID        = "DATA_TYPE_RESTART_TASK_ID";
    String DATA_TYPE_RESTART_TIMES          = "DATA_TYPE_RESTART_TIMES";
    String DATA_TYPE_RESTART_CHANGE_CARD    = "DATA_TYPE_RESTART_CHANGE_CARD";
}
