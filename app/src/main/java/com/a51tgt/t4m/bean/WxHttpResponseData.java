package com.a51tgt.t4m.bean;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.LinkedHashMap;

/**
 * Created by liu_w on 2017/12/17.
 */

public class WxHttpResponseData {
    public int status;
    public int code;
    public String msg;

    public LinkedHashMap<String, Object> data;
    //public JSONObject data;

    public WxHttpResponseData(String json){
        try{
            if(json.contains("errorcode")){
                status = -1;
                msg = "";
            }
            else {
                status = 1;
                Gson gson = new Gson();
                data = gson.fromJson(json, LinkedHashMap.class);
            }
        }
        catch (JsonSyntaxException ex){

        }
    }
}
