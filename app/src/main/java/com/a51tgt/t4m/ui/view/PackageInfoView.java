package com.a51tgt.t4m.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.bean.PackageInfo;
import com.a51tgt.t4m.utils.TipUtil;

import java.util.Map;

/**
 * Created by liu_w on 2017/12/17.
 */

public class PackageInfoView extends LinearLayout implements View.OnClickListener {
    private TextView tv_package_name, tv_flow_count, tv_flow_left, tv_flow_countries, tv_package_starttime, tv_package_endtime, tv_in_using, tv_flow_left_title, tv_flow_count_title, tv_tip;
    PackageInfo myPackageInfo;

    public PackageInfoView(final Context context, final PackageInfo packageInfo){
        super(context);
        Log.i("contenxt",context.toString());
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.view_package_info, this);
        tv_package_name = (TextView) convertView.findViewById(R.id.tv_package_name);
        tv_flow_count = (TextView) convertView.findViewById(R.id.tv_flow_count);
        tv_flow_left = (TextView) convertView.findViewById(R.id.tv_flow_left);
        tv_flow_countries = (TextView) convertView.findViewById(R.id.tv_flow_countries);
        tv_package_starttime = (TextView) convertView.findViewById(R.id.tv_package_starttime);
        tv_package_endtime = (TextView) convertView.findViewById(R.id.tv_package_endtime);
        tv_in_using = (TextView) convertView.findViewById(R.id.tv_in_using);
        tv_flow_left_title = (TextView) convertView.findViewById(R.id.tv_flow_left_title);
        tv_flow_count_title = (TextView) convertView.findViewById(R.id.tv_flow_count_title);
        tv_tip = (TextView) convertView.findViewById(R.id.tv_tip);

        if(packageInfo != null){
            tv_package_name.setText(packageInfo.product_name);
            if (packageInfo.product_name.equals("翻译订单"))

                tv_package_name.setText(getResources().getString(R.string.title_trasn_order));

            Log.i("contenxt",packageInfo.product_name);

            tv_flow_count.setText(packageInfo.flow_count + "MB");
            if (packageInfo.left_flow_count<0){

                packageInfo.left_flow_count = 0;
            }
            tv_flow_left.setText(packageInfo.left_flow_count + "MB");
//            tv_flow_countries.setText(packageInfo.effective_countries.substring(0, 40) + "...");

            if(packageInfo.fy_effective_countries.length() > 40)
                tv_flow_countries.setText(packageInfo.fy_effective_countries.substring(0, 40) + "...");
            else
                tv_flow_countries.setText(packageInfo.fy_effective_countries);

            tv_package_starttime.setText(packageInfo.start_time);
            tv_package_endtime.setText(packageInfo.end_time);
            if(packageInfo.flow_status == 2)
                tv_in_using.setVisibility(View.VISIBLE);

            if(packageInfo.product_type == 104){
                tv_flow_count.setVisibility(View.GONE);
                tv_flow_left.setVisibility(View.GONE);
                tv_flow_left_title.setVisibility(View.GONE);
                tv_flow_count_title.setVisibility(View.GONE);
                tv_tip.setVisibility(View.VISIBLE);
            }
        }
        tv_flow_countries.setOnClickListener(this);
        myPackageInfo = packageInfo;
    }

    public PackageInfoView(Context context){
        super(context);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.view_package_info, this);
        tv_package_name = (TextView) convertView.findViewById(R.id.tv_package_name);
        tv_flow_count = (TextView) convertView.findViewById(R.id.tv_flow_count);
        tv_flow_left = (TextView) convertView.findViewById(R.id.tv_flow_left);
        tv_flow_countries = (TextView) convertView.findViewById(R.id.tv_flow_countries);
        tv_package_starttime = (TextView) convertView.findViewById(R.id.tv_package_starttime);
        tv_package_endtime = (TextView) convertView.findViewById(R.id.tv_package_endtime);
        tv_in_using = (TextView) convertView.findViewById(R.id.tv_in_using);
        tv_flow_left_title = (TextView) convertView.findViewById(R.id.tv_flow_left_title);
        tv_flow_count_title = (TextView) convertView.findViewById(R.id.tv_flow_count_title);
    }

    public void setData(final PackageInfo packageInfo){

        if(packageInfo != null){
            tv_package_name.setText(packageInfo.product_name);

            tv_flow_count.setText(packageInfo.flow_count + "MB");
            if (packageInfo.left_flow_count<0){

                packageInfo.left_flow_count = 0;
            }
            tv_flow_left.setText(packageInfo.left_flow_count + "MB");
            if(packageInfo.fy_effective_countries.length() > 40)
                tv_flow_countries.setText(packageInfo.fy_effective_countries.substring(0, 40) + "...");
            else
                tv_flow_countries.setText(packageInfo.fy_effective_countries);
            tv_package_starttime.setText(packageInfo.start_time);
            tv_package_endtime.setText(packageInfo.end_time);
            if(packageInfo.flow_status == 2)
                tv_in_using.setVisibility(View.VISIBLE);
            if(packageInfo.product_type == 104){
//                tv_package_name.setText(R.string.title_trasn_order);

                tv_flow_count.setVisibility(View.GONE);
                tv_flow_left.setVisibility(View.GONE);
                tv_flow_left_title.setVisibility(View.GONE);
                tv_flow_count_title.setVisibility(View.GONE);
            }

            tv_flow_countries.setOnClickListener(this);

            myPackageInfo = packageInfo;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_flow_countries:
                if(myPackageInfo != null){
                    if(tv_flow_countries.getText().length() <= 45)
                        tv_flow_countries.setText(myPackageInfo.fy_effective_countries);
                    else
                        tv_flow_countries.setText(myPackageInfo.fy_effective_countries.substring(0, 40) + "...");
                }
                break;
        }
    }
}
