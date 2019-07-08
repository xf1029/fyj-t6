package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.a51tgt.t4m.BuildConfig;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.adapter.DeviceOrdersAdapter;
import com.a51tgt.t4m.adapter.FlowMallAdapter;
import com.a51tgt.t4m.bean.FlowProductInfo;
import com.a51tgt.t4m.bean.HttpResponseData;
import com.a51tgt.t4m.bean.WxHttpResponseData;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.ui.view.PackageInfoView;
import com.a51tgt.t4m.utils.CommUtil;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DeviceOrdersActivity extends BaseActivity implements View.OnClickListener{

	private ImageView iv_back;
	private RecyclerView rv_device_orders;
	private ArrayList<LinkedTreeMap<String, Object>> deviceOrders;
	private DeviceOrdersAdapter deviceOrderAdapter;



	private Context mContext;
	@SuppressLint("InflateParams") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_orders);

		//CommUtil.setStatusBarBackgroundColor(DeviceOrdersActivity.this);

		mContext = DeviceOrdersActivity.this;
		iv_back = (ImageView) findViewById(R.id.iv_back);
		rv_device_orders = (RecyclerView) findViewById(R.id.rv_device_orders);
		rv_device_orders.setHasFixedSize(true);
		rv_device_orders.setLayoutManager(new LinearLayoutManager(mContext));

        iv_back.setOnClickListener(this);
        String ssid;
        if (APIConstants.deviceInfo.getSsid().contains("GTWiFi")){

        ssid = APIConstants.deviceInfo.getSsid().replace("GTWiFi","TGT");


		}else {

			ssid = APIConstants.deviceInfo.getSsid();

		}
		String lan;
		if (APIConstants.currentLan.contains("zh")){

			lan = "zh";
		}else{

			lan = "en";
		}
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[2];
		params[0] = new OkHttpClientManager.Param("device_no",  ssid);
//		params[1] = new OkHttpClientManager.Param("useing","0");
		params[1] = new OkHttpClientManager.Param("lang", lan);

		Log.i("pachgeurl",APIConstants.currentLan);
		new SendRequest(APIConstants.Get_Flow_Package, params, new MyHandler(), 1);

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
    { 
        super.onSaveInstanceState(outState);
    } 

	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    { 
        super.onRestoreInstanceState(savedInstanceState); 
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.iv_back:
				finish();
				break;
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (msg == null || msg.obj == null) {
				return;
			}
			if(msg.what == -10){
				return;
			}

			WxHttpResponseData responseData = new WxHttpResponseData((String) msg.obj);
			if (responseData == null || responseData.status < 0 || responseData.data == null) {
				return;
			}
			switch (msg.what){
				case 1:
					if(responseData.data.containsKey("device_order")){
						deviceOrders = (ArrayList<LinkedTreeMap<String, Object>>)responseData.data.get("device_order");
						if(deviceOrders != null && deviceOrders.size() > 0){
							deviceOrderAdapter = new DeviceOrdersAdapter(mContext, deviceOrders);
							rv_device_orders.setAdapter(deviceOrderAdapter);
						}
					}
					break;
				default:
					break;
			}
		}
	}
}

