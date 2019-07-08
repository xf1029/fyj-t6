package com.a51tgt.t4m.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by liu_w on 2017/9/14.
 */

public class FlowProductInfo {
    public String coverImage;
    public String title;
    public String subTitle;
    public String price;
    public String priceType;
    public String moneyType;
    public int id;
    public String url;

    public FlowProductInfo(String info){
        try {
            JSONObject obj = new JSONObject(info);
            coverImage = obj.getString("cover_image");
            title = obj.getString("title");
            subTitle = obj.getString("sub_title");
            price = obj.getString("price");
            priceType = obj.getString("price_type");
            id = obj.getInt("id");
            url = obj.getString("url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public FlowProductInfo(Map<String, Object> data){
        if(data.containsKey("id"))
            id = (int)Float.parseFloat(data.get("id").toString());
        if(data.containsKey("cover_image_url"))
            coverImage = data.get("cover_image_url").toString();
        if(data.containsKey("price")) {
            double d = Double.parseDouble(data.get("price").toString());
            int i = (int) d;
            price = String.valueOf(i);
        }
        if(data.containsKey("price_type"))
            priceType = data.get("price_type").toString();
        if(data.containsKey("url"))
            url = data.get("url").toString();
        if(data.containsKey("money_type"))
            moneyType = data.get("money_type").toString();
        if(data.containsKey("title"))
            title = data.get("title").toString();
        if(data.containsKey("sub_title"))
            subTitle = data.get("sub_title").toString();
    }
}

