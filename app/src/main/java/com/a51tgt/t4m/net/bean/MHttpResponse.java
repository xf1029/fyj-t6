package com.a51tgt.t4m.net.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MHttpResponse {

  protected String responseContent;
  public int status;
  public int code;
  public String msg;
  public JSONObject data;

  public MHttpResponse(String responseContent) {
    this.responseContent = responseContent;
    try {
      JSONObject obj = new JSONObject(responseContent);
      status = obj.optInt("status");
      code = obj.optInt("code");
      msg = obj.optString("msg");
      data = obj.optJSONObject("data");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void parseResponseContent(JSONObject data) {
    //Nothing need to do here
  }

  public int getInt(String name, int default_value){
    try {
      return data.getInt(name);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return default_value;
  }

  public String getString(String name, String default_value){
    try {
      return data.getString(name);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return default_value;
  }

  public JSONObject getJsonObject(String name){
    try {
      return data.getJSONObject(name);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  public JSONArray getJsonArray(String name){
    try {
      return data.getJSONArray(name);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getStringFromYouDunResponse(String name, String default_value){
    try {
      JSONObject result = new JSONObject(data.getString("result"));
      if(result != null){
        JSONObject youDunData = result.getJSONObject("data");
        if(youDunData != null){
          return youDunData.getString(name);
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return default_value;
  }

  public boolean isErrorResponse() {
    return status != 1;
  }

  public boolean compareCode(int code){
    return this.code == code;
  }

}
