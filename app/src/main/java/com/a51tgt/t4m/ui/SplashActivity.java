package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.a51tgt.t4m.BuildConfig;
import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.bean.DeviceInfo;
import com.a51tgt.t4m.bean.DevicePackageInfo;
import com.a51tgt.t4m.bean.HttpResponseData;
import com.a51tgt.t4m.bean.UserDataUtils;
import com.a51tgt.t4m.bluetooth.BluetoothUtil;
import com.a51tgt.t4m.bluetooth.ClsUtils;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.lib.Capture2Activity;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.net.TcpUtil;
import com.a51tgt.t4m.utils.AppLanguageUtils;
import com.a51tgt.t4m.utils.CommUtil;
import com.a51tgt.t4m.utils.DeviceInfoUtils;
import com.a51tgt.t4m.utils.NetWorkUtils;
import com.a51tgt.t4m.utils.TipUtil;
import com.a51tgt.t4m.utils.WiFiUtil;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.paypal.android.sdk.payments.LoginActivity;
import com.znq.zbarcode.CaptureActivity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends BaseActivity {

    private static final long DELAY_1S = 2000;
    private Context mContext;
    private View anchor;
    private WifiManager wifiManager;
    private String deviceMacAddress = "";
    private String deviceSSID = "";
    Handler waitWifiEnableHandler = new Handler();
    Runnable waitWifiEnableRunnable = new Runnable(){
        @Override
        public void run() {
//            SelectWifi();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        anchor = (View) findViewById(R.id.anchor);
        mContext = this;
//        EnableWifiReceiver receiver = new EnableWifiReceiver();

        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        String name = preferences.getString("lang","");//getSt
        Configuration config = getResources().getConfiguration();
        DisplayMetrics dm = getResources().getDisplayMetrics();
//        if (name.equals("zh")) {
//            config.locale = Locale.SIMPLIFIED_CHINESE; // 设置当前语言配置为简体
//            getResources().updateConfiguration(config, dm); // 更新配置文件
//        } else {
//            config.locale = Locale.ENGLISH; // 设置当前语言配置为繁体
//            getResources().updateConfiguration(config, dm); // 更新配置文件
//        }



        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);

//            IntentFilter filter = new IntentFilter(WifiManager.RSSI_CHANGED_ACTION);
//            registerReceiver(receiver, filter);

        }
        else{
//            IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//            registerReceiver(receiver, filter);
//            getDeviceInfoHandler.postDelayed(getDeviceInfoRunnable, DELAY_1S);
        }
//        Integer time = 2000;    //设置等待时间，单位为毫秒
        Handler handler = new Handler();
        //40:45:DA:96:3C:B8
        //40:45:DA:A6:B2:56
//        deviceMacAddress = UserDataUtils.getInstance(mContext).getMacAddress();
//        deviceSSID = UserDataUtils.getInstance(mContext).getSsid();

//        if (!BluetoothUtil.getInstance().isBluetoothEnable()) {
//            BluetoothUtil.getInstance().enableBluetooth(this);//发起打开蓝牙
//        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (TextUtils.isEmpty(deviceMacAddress) || TextUtils.isEmpty(deviceSSID)) {
//                    Intent intent = new Intent(mContext, FirstScanActivity.class);
//                    startActivity(intent);
//                    finish();
//
//                } else {
//                    APIConstants.deviceInfo = new DeviceInfo(deviceSSID, deviceMacAddress);
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
                Intent intent = new Intent(mContext, FirstScanActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_1S);
}

        @Override
    protected void onResume() {
        super.onResume();

//        MZApplication.getInstance().setOnNoticeUiListener(this, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        MZApplication.getInstance().setOnNoticeUiListener(null, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MZApplication.getInstance().setOnNoticeUiListener(null, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
    }
}
