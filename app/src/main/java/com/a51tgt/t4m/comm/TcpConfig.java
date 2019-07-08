package com.a51tgt.t4m.comm;

public class TcpConfig {

	//public static String host = "192.168.43.1";
	public static String host = "192.168.1.49";
	public static String hostIP = "192.168.1.1";
	public static int port = 51848;

	public static boolean isAllLogs = true;
	public static String KEY = "key";
	public static String VALUE = "value";
	public static String DELETE_MODE_LOG="deleteModemLog";
	
	public static String SET_WIFIAP_CHANNEL = "setWifiApChannel";
	public static String BLOCK_CLIENT = "blockClient";
	public static String UNBLOCK_CLIENT = "unblockClient";
	public static String T3_GET_FIRST_NETWORL_MODE="getNetworkType";
	public static String T3_SET_CARD_STATE="setCardState";
    public static String T3_GET_CARD_STATE="getCardState";
    public static String FOTA_UPDATA_CHECK="fotaUpdateCheck";
    
	public static String _HEAD_ = "b6E6L255EOCdJ9NplKPuUkn87VmwCCcC";
	public static String GET_DEVICE_INFO                  = "CMD_GET_DEVICE_INFO";
	public static String GET_PACKAGE_INFO                 = "CMD_GET_PACKAGE_INFO";
	public static String GET_DEVICE_LOGS                  = "CMD_GET_DEVICE_LOGS";
	public static String GET_DEVICE_ALL_LOGS              = "CMD_GET_DEVICE_ALL_LOGS";
	public static String OPEN_ADB                         = "CMD_OPEN_ADB";
	public static String CLOSE_ADB                        = "CMD_CLOSE_ADB";
	public static String OPEN_MODE_LOG                    = "CMD_OPEN_MODE_LOG";
	public static String CLOSE_MODE_LOG                   = "CMD_CLOSE_MODE_LOG";
	public static String REBOOT                           = "CMD_REBOOT";
	public static String POWER_OFF                        = "CMD_POWER_OFF";
	public static String OPEN_FLY_MODE                    = "CMD_OPEN_FLY_MODE";
	public static String CLOSE_FLY_MODE                   = "CMD_CLOSE_FLY_MODE";
	public static String RESTART                          = "CMD_RESTART";
	public static String OPEN_DATA                        = "CMD_OPEN_DATA";
	public static String CLOSE_DATA                       = "CMD_CLOSE_DATA";
	public static String OPEN_WIFIAP                      = "CMD_OPEN_WIFIAP";
	public static String CLOSE_WIFIAP                     = "CMD_CLOSE_WIFIAP";
	public static String CONTROL_SPEED_TRUE               = "CMD_CONTROL_SPEED_TRUE";
	public static String CONTROL_SPEED_FALSE              = "CMD_CONTROL_SPEED_FALSE";
	public static String SET_FULL_SCREEN_TRUE             = "CMD_SET_FULL_SCREEN_TRUE";
	public static String SET_FULL_SCREEN_FALSE            = "CMD_SET_FULL_SCREEN_FALSE";
	public static String SWITCH_DATA                      = "CMD_SWITCH_DATA";
	public static String SWITCH_FLY_MODE                  = "CMD_SWITCH_FLY_MODE";
	public static String SET_APN                          = "CMD_SET_APN";
	public static String SET_WIFIANDPASSWORD              = "CMD_SET_WIFIANDPASSWORD";
	public static String SET_DATA_ROAMING_TRUE            = "CMD_SET_DATA_ROAMING_TRUE";
	public static String SET_DATA_ROAMING_FALSE           = "CMD_SET_DATA_ROAMING_FALSE";
	public static String SET_BLACK_LIST                   = "CMD_SET_BLACK_LIST";
	public static String GET_HOT_SPOT_CLIENTS             = "CMD_GET_HOT_SPOT_CLIENTS";
	public static String GET_BASIC_CONFIGLIST             = "CMD_GET_BASIC_CONFIGLIST";
	public static String SET_NETWORK_TYPE                 = "CMD_SET_NETWORK_TYPE";
	public static String GET_NETWORK_TYPE                 = "CMD_GET_NETWORK_TYPE";
	public static String SET_CARD_STATE                   = "CMD_SET_CARD_STATE";
	public static String GET_CARD_STATE                   = "CMD_GET_CARD_STATE";
	public static String DELETE_MODEM_LOG                 = "CMD_DELETE_MODEM_LOG";
	public static String SET_DEVICE_LANGUAGE              = "CMD_SET_DEVICE_LANGUAGE";
	public static String ENABLE_SSH_SERVICE               = "CMD_ENABLE_SSH_SERVICE";
	public static String FOTA_UPDATE_CHECK                = "CMD_FOTA_UPDATE_CHECK";
	public static String BLACK_LIST_ENABLE                = "CMD_BLACK_LIST_ENABLE";
	public static String RESTART_TEST_TASK                = "CMD_RESTART_TEST_TASK";
	public static String RESTART_TEST_CANCEL              = "CMD_RESTART_TEST_CANCEL";
	public static String GET_REAL_TIME_LOGS               = "CMD_GET_REAL_TIME_LOGS";
	public static String CANCEL_REAL_TIME_LOGS            = "CMD_CANCEL_REAL_TIME_LOGS";
	public static String COPY_M1_LOGS_TO_M2               = "CMD_COPY_M1_LOGS_TO_M2";
	public static String SET_SSH_DEBUG                    = "CMD_SET_SSH_DEBUG";
	public static String OPEN_REALSIM                     = "CMD_OPEN_REALSIM";
	public static String CLOSE_REALSIM                    = "CMD_CLOSE_REALSIM";
	public static String RESTART_TEST_GET_RES             = "CMD_RESTART_TEST_GET_RES";
	public static String RESTART_TEST_GET_AUTHEN          = "CMD_RESTART_TEST_GET_AUTHEN";
	public static String RELEASE_CARD_SIMULATION          = "CMD_RELEASE_CARD_SIMULATION";
	public static String OPEN_BLUETOOTH_CONNECT           = "CMD_OPEN_BLUETOOTH_CONNECT";
	public static String BT_OPEN_WIFI                     = "CMD_BT_OPEN_WIFI";
	public static String BT_CLOSE_WIFI                    = "CMD_BT_CLOSE_WIFI";
	public static String BT_CONNECT_TO_AP                 = "CMD_BT_CONNECT_TO_AP";
	public static String BT_FORGET_SAVED_AP               = "CMD_BT_FORGET_SAVED_AP";
	public static String BT_DISCONNECT_AP				    = "CMD_BT_DISCONNECT_AP";
	public static String BT_MSG_FROM_DEVICE               = "CMD_BT_MSG_FROM_DEVICE";
	public static String BT_SEND_APK                      = "CMD_BT_SEND_APK";
	public static String BT_REDUCTION                     = "CMD_BLE_RESTORE_FACTORY_SETTING";
//查询apn
	public static String BT_APN_QUERY                    = "CMD_BLE_APN_QUERY";



	public static String[] cmds = new String[]{GET_DEVICE_INFO, GET_PACKAGE_INFO, GET_DEVICE_LOGS,
			GET_DEVICE_ALL_LOGS, OPEN_ADB, CLOSE_ADB, REBOOT, POWER_OFF, OPEN_FLY_MODE, CLOSE_FLY_MODE, RESTART,
			OPEN_DATA, CLOSE_DATA, OPEN_WIFIAP, CLOSE_WIFIAP, CONTROL_SPEED_TRUE, CONTROL_SPEED_FALSE,
			SET_FULL_SCREEN_TRUE, SET_FULL_SCREEN_FALSE, SWITCH_DATA, SWITCH_FLY_MODE, SET_APN, SET_DATA_ROAMING_TRUE,
			SET_DATA_ROAMING_FALSE, SET_WIFIANDPASSWORD, SET_BLACK_LIST, GET_HOT_SPOT_CLIENTS, GET_BASIC_CONFIGLIST,
			OPEN_MODE_LOG, CLOSE_MODE_LOG, SET_NETWORK_TYPE, GET_NETWORK_TYPE, DELETE_MODEM_LOG, GET_CARD_STATE,
			SET_CARD_STATE, SET_DEVICE_LANGUAGE, ENABLE_SSH_SERVICE, FOTA_UPDATE_CHECK, BLACK_LIST_ENABLE,
			RESTART_TEST_TASK, RESTART_TEST_CANCEL,GET_REAL_TIME_LOGS,CANCEL_REAL_TIME_LOGS,
			COPY_M1_LOGS_TO_M2,SET_SSH_DEBUG,OPEN_REALSIM,CLOSE_REALSIM,RESTART_TEST_GET_RES,RESTART_TEST_GET_AUTHEN,
			RELEASE_CARD_SIMULATION,OPEN_BLUETOOTH_CONNECT,BT_OPEN_WIFI,BT_CLOSE_WIFI,BT_CONNECT_TO_AP,BT_FORGET_SAVED_AP,
			BT_MSG_FROM_DEVICE,BT_DISCONNECT_AP};

	public static String DATA_TYPE_RESTART_TASK_ID        = "DATA_TYPE_RESTART_TASK_ID";
	public static String DATA_TYPE_RESTART_TIMES          = "DATA_TYPE_RESTART_TIMES";
	public static String DATA_TYPE_RESTART_CHANGE_CARD    = "DATA_TYPE_RESTART_CHANGE_CARD";
	
	
	public static String getHostIP() {
		return hostIP;
	}
	public static void setHostIP(String hostIP) {
		TcpConfig.hostIP = hostIP;
	}
	
	
}
