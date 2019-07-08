package com.a51tgt.t4m.bean;

import android.content.Context;
import android.content.SharedPreferences;

import com.a51tgt.t4m.comm.APIConstants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by liu_w on 2017/12/20.
 */

public class UserDataUtils {
    private static final String P_NAME = "user_data";
    private static final String KEY_DEVICE_MAC_ADDRESS = "mac_address";
    private static final String KEY_DEVICE_SSID = "ssid";
    private static final String KEY_PACKAGE_INFO = "package_info";
    private static final String KEY_DEVICE_LIST = "device_list";
    private static final String KEY_FIRST_ENTER = "first_enter";

    private static UserDataUtils instance;
    private static SharedPreferences mPreference;

    private UserDataUtils(Context context)
    {
        mPreference = context.getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
    }

    public static final UserDataUtils getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new UserDataUtils(context);
        }
        return instance;
    }

    public final boolean getFirstEnter(){
        return mPreference.getBoolean(KEY_FIRST_ENTER, true);
    }

    public void setFirstEnter(boolean isFirstEnter){
        mPreference.edit().putBoolean(KEY_FIRST_ENTER, isFirstEnter).apply();
    }

    public final String getMacAddress(){
        return mPreference.getString(KEY_DEVICE_MAC_ADDRESS, "");
    }

    public void setMacAddress(String mac_address){
        mPreference.edit().putString(KEY_DEVICE_MAC_ADDRESS, mac_address).apply();
    }

    public final String getSsid(){
        return mPreference.getString(KEY_DEVICE_SSID, "");
    }

    public void setSsid(String ssid){
        mPreference.edit().putString(KEY_DEVICE_SSID, ssid).apply();
    }

    public ArrayList<DeviceInfoForUserData> getDeviceList(){
        String json = mPreference.getString(KEY_DEVICE_LIST, "");
        try{
            Gson gson = new Gson();
            ArrayList<DeviceInfoForUserData> devices = gson.fromJson(json, new TypeToken<ArrayList<DeviceInfoForUserData>>() {}.getType());
            return devices;
        }
        catch (JsonSyntaxException ex){

        }
        return new ArrayList<DeviceInfoForUserData>();
    }

    public void setDeviceList(String ssid, String mac_address){
        String json = mPreference.getString(KEY_DEVICE_LIST, "");
        try{
            Gson gson = new Gson();
            ArrayList<DeviceInfoForUserData> devices = gson.fromJson(json, new TypeToken<ArrayList<DeviceInfoForUserData>>() {}.getType());
            if(devices == null) devices = new ArrayList<DeviceInfoForUserData>();
            for(int i = 0; i < devices.size(); i++){
                DeviceInfoForUserData device = devices.get(i);
                if(device.Ssid.equals(ssid))
                    return;
            }
            devices.add(new DeviceInfoForUserData(ssid, mac_address));
            mPreference.edit().putString(KEY_DEVICE_LIST, gson.toJson(devices)).apply();
        }
        catch (JsonSyntaxException ex){

        }
    }

    public void removeFromDeviceList(String ssid, String mac_address){
        String json = mPreference.getString(KEY_DEVICE_LIST, "");
        try{
            Gson gson = new Gson();
            ArrayList<DeviceInfoForUserData> devices = gson.fromJson(json, new TypeToken<ArrayList<DeviceInfoForUserData>>() {}.getType());
            if(devices == null) devices = new ArrayList<DeviceInfoForUserData>();
            for(int i = 0; i < devices.size(); i++){
                DeviceInfoForUserData device = devices.get(i);
                if(device.Ssid.equals(ssid) && device.MacAddress.equals(mac_address)) {
                    devices.remove(i);
                    break;
                }
            }
            mPreference.edit().putString(KEY_DEVICE_LIST, gson.toJson(devices)).apply();
        }
        catch (JsonSyntaxException ex){

        }
    }

    public void setDeviceList(ArrayList<DeviceInfoForUserData> devices){
        Gson gson = new Gson();
        mPreference.edit().putString(KEY_DEVICE_LIST, gson.toJson(devices)).apply();
    }

    public final String getPackageInfo(){
        return mPreference.getString(KEY_PACKAGE_INFO, null);
    }

    public void setPackageInfo(String json){
        mPreference.edit().putString(KEY_PACKAGE_INFO, json).apply();
    }
}
