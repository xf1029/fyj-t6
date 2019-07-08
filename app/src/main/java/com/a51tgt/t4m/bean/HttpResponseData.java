package com.a51tgt.t4m.bean;

import android.content.DialogInterface;
import android.content.Intent;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.utils.TipUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.znq.zbarcode.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Created by liu_w on 2017/10/22.
 */

public class HttpResponseData {
    public int status;
    public int code;
    public String msg;

    public LinkedHashMap<String, Object> data;
    //public JSONObject data;

    public HttpResponseData(String json){
        try{
            Gson gson = new Gson();
            HttpResponseData obj = gson.fromJson(json, HttpResponseData.class);
            if(obj != null) {
                status = obj.status;
                code = obj.code;
                msg = obj.msg;
                data = obj.data;
            }
        }
        catch (Exception ex){
        }
    }
}
