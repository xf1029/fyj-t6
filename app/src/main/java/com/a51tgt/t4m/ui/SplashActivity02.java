package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.net.TcpUtil;

import java.util.List;

public class SplashActivity02 extends BaseActivity {

    private static final long DELAY_1S = 1000;
    private Context mContext;
    private MyHandler myHandler;
    private WifiManager wifiManager;
    private List<ScanResult> wifiList;

    private SelectWifiFragment selectWifiFragment;
    private RecyclerView rvWifi;
    private View anchor;
    private LinearLayout ll_connect_wifi;
    EnableWifiReceiver receiver = new EnableWifiReceiver();

    Handler getDeviceInfoHandler = new Handler();
    Handler waitWifiEnableHandler = new Handler();
    Runnable getDeviceInfoRunnable = new Runnable(){
        @Override
        public void run() {
//            TcpUtil.getInstance().sendMessage(TcpConfig.GET_DEVICE_INFO, new MyHandler(1), 1);
            TcpUtil.getInstance().sendMessage(TcpConfig.GET_DEVICE_INFO,new MyHandler(1));
        }
    };

    Runnable waitWifiEnableRunnable = new Runnable(){
        @Override
        public void run() {
            SelectWifi();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ll_connect_wifi = (LinearLayout) findViewById(R.id.ll_connect_wifi);
        anchor = (View) findViewById(R.id.anchor);

        mContext = this;
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);

            IntentFilter filter = new IntentFilter(WifiManager.RSSI_CHANGED_ACTION);
            registerReceiver(receiver, filter);

        }
        else{
            getDeviceInfoHandler.postDelayed(getDeviceInfoRunnable, DELAY_1S);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void SelectWifi(){
//        waitWifiEnableHandler.removeCallbacks(waitWifiEnableRunnable);
//        ll_connect_wifi.setVisibility(View.VISIBLE);
//        wifiList = wifiManager.getScanResults();
//        selectWifiFragment = SelectWifiFragment.newInstance(wifiList);
//        selectWifiFragment.setOnItemClickListener(new SelectWifiFragment.OnWifiConnectListener() {
//            @Override
//            public void onWifiConnect(boolean res) {
//                if(res == true){
//                    unregisterReceiver(receiver);
//                    ll_connect_wifi.setVisibility(View.GONE);
//                }
//            }
//        });
//        getSupportFragmentManager().beginTransaction().replace(R.id.cover_container, selectWifiFragment).commit();
    }

    public class EnableWifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

            if (info.getState().equals(NetworkInfo.State.CONNECTED)) {

                                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                            }
        else    if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                    Log.i("WIFIIEIIFIEI",wifiInfo.getBSSID());
//                    // 当前WIFI名称
                }
               else if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
                waitWifiEnableHandler.postDelayed(waitWifiEnableRunnable, DELAY_1S * 3);
            }
        }

    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        public int hander_type = 1;
        public MyHandler(int type){
            hander_type = type;
        }

        @Override
        public void handleMessage(Message msg) {
            if(hander_type == 1) getDeviceInfoHandler.removeCallbacks(getDeviceInfoRunnable);
            if (msg == null || msg.obj == null) {
                return;
            }
            if(msg.what == -10){
                SelectWifi();
            }
            String response = (String) msg.obj;
            if (TextUtils.isEmpty(response)) {
                return;
            }
        }
    }

}
