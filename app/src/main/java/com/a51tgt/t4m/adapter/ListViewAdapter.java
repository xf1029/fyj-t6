package com.a51tgt.t4m.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.bean.PackageInfo;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.ui.view.PackageInfoView;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;




public class ListViewAdapter extends BaseAdapter {
    LayoutInflater inflater;
    JSONArray jsonArray;

    public ListViewAdapter(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonArray.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.list, null);
        TextView idTextView = (TextView) v.findViewById(R.id.text1);
        String name;


        try {
            // 得到JSONObject
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String lan;
            if (APIConstants.currentLan.contains("zh")){

                lan = "zh";
            }else{

                lan = "en";
            }
            if (lan.contains("zh")){

                name = jsonObject.getString("name").split("-")[0];
            }else{

                name = jsonObject.getString("name").split("-")[1];

            }
            // 得到映射的数据

//            String name = jsonObject.getString("name");
            // 赋值
           idTextView.setText(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }

}