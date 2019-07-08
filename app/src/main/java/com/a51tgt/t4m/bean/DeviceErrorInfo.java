package com.a51tgt.t4m.bean;

import android.content.Context;

import com.a51tgt.t4m.utils.CommUtil;

import java.util.List;

/**
 * Created by liu_w on 2017/9/18.
 */

public class DeviceErrorInfo {
    public String SSID;
    public String startTime;
    public String errorInfo;

    public DeviceErrorInfo(String ssid, String error_info){
        SSID = ssid;
        startTime = CommUtil.getCurTime();
        errorInfo = error_info;
    }

}
