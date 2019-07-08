package com.a51tgt.t4m.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.a51tgt.t4m.bean.DeviceErrorInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by liu_w on 2017/9/18.
 */

public class DeviceErrorUtil {

    private Context mContext;

    public DeviceErrorUtil(Context context){
        mContext = context;
    }

    public static DeviceErrorUtil getInstance(Context context){
        return new DeviceErrorUtil(context);
    }

    public void SaveErrorInfo(DeviceErrorInfo deviceErrorInfo){
        try {
            JSONObject jObj = new JSONObject();
            jObj.put("SSID", deviceErrorInfo.SSID);
            jObj.put("startTime", deviceErrorInfo.startTime);
            jObj.put("errorInfo", deviceErrorInfo.errorInfo);

            JSONArray errorJsonArray = null;

            SharedPreferences pref = mContext.getSharedPreferences("device_error_data", mContext.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            String error_data = pref.getString("data", "");
            if(!TextUtils.isEmpty(error_data)){
                errorJsonArray = (new JSONObject()).getJSONArray(error_data);
            }
            else {
                errorJsonArray = new JSONArray();
            }
            errorJsonArray.put(jObj);
            editor.putString("data",errorJsonArray.toString());
            editor.commit();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getErrorInfo(){
        SharedPreferences pref = mContext.getSharedPreferences("device_error_data", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String error_data = pref.getString("data", "");
        editor.putString("data","");
        editor.commit();
        return error_data;
    }

}
