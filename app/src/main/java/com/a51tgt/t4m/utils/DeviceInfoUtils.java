package com.a51tgt.t4m.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

/**
 * Created by liu_w on 2017/11/13.
 */

public class DeviceInfoUtils {

    private static final String P_NAME = "device_data";
    private static DeviceInfoUtils instance;
    private static SharedPreferences mPreference;

    private static final String KEY_DEVICE_SSID = "device_ssid";
    private static final String KEY_DEVICE_MACADDRESS = "device_mac_address";

    private DeviceInfoUtils(Context context)
    {
        mPreference = context.getSharedPreferences(P_NAME, Context.MODE_PRIVATE |Context.MODE_MULTI_PROCESS);
    }

    public static final DeviceInfoUtils getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new DeviceInfoUtils(context);
        }
        return instance;
    }

    public final String getSSID()
    {
        return mPreference.getString(KEY_DEVICE_SSID, null);
    }

    public final void setSSID(String ssid)
    {
        mPreference.edit().putString(KEY_DEVICE_SSID, ssid).commit();;
    }

    public final String getMacAddress()
    {
        return mPreference.getString(KEY_DEVICE_MACADDRESS, null);
    }

    public final void setMacAddress(String add)
    {
        mPreference.edit().putString(KEY_DEVICE_MACADDRESS, add).commit();;
    }
}
