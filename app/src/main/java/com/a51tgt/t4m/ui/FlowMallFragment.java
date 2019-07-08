package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.adapter.FlowMallAdapter;
import com.a51tgt.t4m.bean.DeviceInfo;
import com.a51tgt.t4m.bean.FlowProductInfo;
import com.a51tgt.t4m.bean.HttpResponseData;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.utils.CommUtil;
import com.a51tgt.t4m.utils.TipUtil;
import com.google.gson.Gson;
import com.znq.zbarcode.CaptureActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liu_w on 2017/9/13.
 */

public class FlowMallFragment extends Fragment {

    private RecyclerView rv_flow_package;
    private List<FlowProductInfo> flowProductInfoList;
    private FlowMallAdapter flowMallAdapter;
    private List<Map<String, Object>> mData;
    private String ssid;

    public FlowMallFragment(){

    }
    public static FlowMallFragment newInstance(){
        FlowMallFragment fragment = new FlowMallFragment();
        return fragment;

    }
    //接收广播的处理
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {


//            FlowMallFragment.this.recreate();

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //注册接受的广播
        IntentFilter filter = new IntentFilter(APIConstants.BR_LAN_STATUS);
//        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flow_mall, container, false);
        rv_flow_package = (RecyclerView) rootView.findViewById(R.id.rv_flow_package);
        rv_flow_package.setHasFixedSize(true);
        rv_flow_package.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(APIConstants.deviceInfo != null && APIConstants.deviceInfo.getSsid() != null){
            ssid = CommUtil.getNumberAndCharacter(APIConstants.deviceInfo.getSsid());
            if(TextUtils.isEmpty(ssid))
                ssid = "";
        }
        PackageManager packageManager = getActivity().getPackageManager();

        PackageInfo packInfo = null;
        try {
            String str = getContext().getPackageName();

            packInfo = packageManager.getPackageInfo(str,0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        if (ssid.contains("GTWiFi")) ssid = ssid.replace("GTWiFi","TGT");

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[4];
        params[0] = new OkHttpClientManager.Param("ssid",  ssid);
        params[1] = new OkHttpClientManager.Param("type", "USD");
        params[2] = new OkHttpClientManager.Param("app_version",  version);
        params[3] = new OkHttpClientManager.Param("ret_all",  "1");


        new SendRequest(APIConstants.Get_Flow_Mall, params, new MyHandler(), 1);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            PackageManager packageManager = getActivity().getPackageManager();

            PackageInfo packInfo = null;
            try {
                String str = getContext().getPackageName();

                packInfo = packageManager.getPackageInfo(str,0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String version = packInfo.versionName;

            if(APIConstants.isScanNewDevice == true){
                if (ssid.contains("GTWiFi")) ssid = ssid.replace("GTWiFi","TGT");
                OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[4];
                params[0] = new OkHttpClientManager.Param("ssid",  ssid);
                params[1] = new OkHttpClientManager.Param("type", "USD");
                params[2] = new OkHttpClientManager.Param("app_version",  version);
                params[3] = new OkHttpClientManager.Param("ret_all",  "1");

                new SendRequest(APIConstants.Get_Flow_Mall, params, new MyHandler(), 1);
                APIConstants.isScanNewDevice = false;
            }
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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

            HttpResponseData responseData = new HttpResponseData((String) msg.obj);
            if (responseData == null || responseData.status < 0 || responseData.data == null) {
                return;
            }
            switch (msg.what){
                case 1:
                    if(responseData.data.containsKey("products")){
                        Log.i("prodictss:",responseData.data.toString());
                        mData = (List<Map<String, Object>>)responseData.data.get("products");
                        if(mData != null){
                            flowProductInfoList = new ArrayList<FlowProductInfo>();
                            for(int i = 0; i < mData.size(); i++){
                                FlowProductInfo flowProduct = new FlowProductInfo(mData.get(i));
                                flowProductInfoList.add(flowProduct);
                            }
                            flowMallAdapter = new FlowMallAdapter(getActivity(), flowProductInfoList);
                            flowMallAdapter.setOnItemClickListener(new FlowMallAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position, FlowProductInfo flowProductInfo) {
                                    Intent intent = new Intent(getActivity(), FlowProductDetailActivity.class);
                                    intent.putExtra("product_id", flowProductInfo.id);
                                    intent.putExtra("url", APIConstants.server_host + flowProductInfo.url);
                                    Log.i("urlurlulr:",APIConstants.server_host + flowProductInfo.url+flowProductInfo.price);
                                    intent.putExtra("price", flowProductInfo.price);
                                    intent.putExtra("price_type", flowProductInfo.priceType);
                                    intent.putExtra("title", flowProductInfo.title);
                                    intent.putExtra("money_type", flowProductInfo.moneyType);

                                    getActivity().startActivity(intent);
                                }
                            });
                            rv_flow_package.setAdapter(flowMallAdapter);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
