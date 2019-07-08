package com.a51tgt.t4m.bean;

import com.google.gson.internal.LinkedTreeMap;

import java.util.LinkedHashMap;

/**
 * Created by liu_w on 2017/12/17.
 */

public class PackageInfo {
    public String product_name;
    public int flow_count;
    public int left_flow_count;
    public String effective_countries;
    public String fy_effective_countries;

    public int effective_countries_id;
    public String start_time;
    public String end_time;
    public int product_type;
    public int status;
    public int flow_status;

    public PackageInfo(LinkedTreeMap<String, Object> data){
        if(data != null){
            if(data.containsKey("product_name"))
                product_name = data.get("product_name").toString();
            if(data.containsKey("flow_count"))
                flow_count = (int)Float.parseFloat(data.get("flow_count").toString());
            if(data.containsKey("left_flow_count"))
                left_flow_count = (int)Float.parseFloat(data.get("left_flow_count").toString());
            if(data.containsKey("effective_countries"))
                effective_countries = data.get("effective_countries").toString();
            if(data.containsKey("fy_effective_countries"))
                fy_effective_countries = data.get("fy_effective_countries").toString();
            if(data.containsKey("effective_countries_id"))
                effective_countries_id = (int)Float.parseFloat(data.get("effective_countries_id").toString());
            if(data.containsKey("start_time"))
                start_time = data.get("start_time").toString();
            if(data.containsKey("end_time"))
                end_time = data.get("end_time").toString();
            if(data.containsKey("product_type"))
                product_type = (int)Float.parseFloat(data.get("product_type").toString());
            if(data.containsKey("status"))
                status = (int)Float.parseFloat(data.get("status").toString());
            if(data.containsKey("flow_status"))
                flow_status = (int)Float.parseFloat(data.get("flow_status").toString());
        }
    }
}
