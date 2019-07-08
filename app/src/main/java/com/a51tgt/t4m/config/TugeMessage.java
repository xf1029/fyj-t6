package com.a51tgt.t4m.config;

/**
 * Created by Administrator on 2017/8/25.
 */

public interface TugeMessage {
    int MSG_CONTEXT_NULL = 0;
    int MSG_SUCCEED = 1;
    int MSG_GET_DEVICE_INFO = 2;
    int MSG_MESSAGE_NULL = 3;

    int EXCEPTION_ERROR = 4;
    int EXCEPTION_JSON_ERROR = 5;
    int EXCEPTION_IO_ERROR = 6;

    int GET_MESSAGE = 7;
    int HTTP_GET_PACKAGE_INFO = 8;
    int HTTP_GET_DEVICE_INFO = 9;
    int TIME_OUT = 10;
    int WIFI_NOT_OPENED = 11;
    int NOT_TGT_WIFI = 12;
    int NOT_SUPPORTED_DEVICE = 13;

    int FILE_DOWNLOAD_SUCCEED = 14;
    int FILE_DOWNLOAD_FAILED = 15;
    int FILE_UPLOAD_SUCCEED = 25;
    int FILE_UPLOAD_FAILED = 26;
    int MSG_CONNECT_OK = 16;
    int MSG_CONNECT_FAILED = 17;
    int MSG_LIST_OK = 18;
    int MSG_LIST_FAILED = 19;
    int MSG_DELE_OK = 20;
    int MSG_DELE_FAILED = 21;
    int MSG_SN_ERROR = 22;
    int MSG_UPLOAD_FAILED = 23;
    int EXCEPTION_MALFORME_URL_ERROR = 24;
}
