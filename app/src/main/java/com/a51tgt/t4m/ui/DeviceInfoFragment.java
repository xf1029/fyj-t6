package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ActivityManager.RunningServiceInfo;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t4m.BuildConfig;
import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.bean.DeviceInfoForUserData;
import com.a51tgt.t4m.net.TcpUtil;
import com.a51tgt.t4m.service.MainService;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.adapter.ScanWifiAdapter;
import com.a51tgt.t4m.bean.DeviceInfo;
import com.a51tgt.t4m.bean.DevicePackageInfo;
import com.a51tgt.t4m.bean.HttpResponseData;
import com.a51tgt.t4m.bean.PackageInfo;
import com.a51tgt.t4m.bean.UserDataUtils;
import com.a51tgt.t4m.bean.WxHttpResponseData;
import com.a51tgt.t4m.bluetooth.BluetoothUtil;
import com.a51tgt.t4m.bluetooth.ClsUtils;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.service.MainService;
import com.a51tgt.t4m.ui.view.DeviceInfoView;
import com.a51tgt.t4m.ui.view.PackageInfoView;
import com.a51tgt.t4m.utils.CommUtil;
import com.a51tgt.t4m.utils.RestartAPPUtil;
import com.a51tgt.t4m.utils.TipUtil;
import com.a51tgt.t4m.utils.Util;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.znq.zbarcode.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.a51tgt.t4m.utils.WiFiUtil;

import org.json.JSONException;

/**
 * Created by liu_w on 2017/9/13.
 */

public class DeviceInfoFragment extends Fragment implements View.OnClickListener, OnNoticeUI{

    private TextView tv_ssid, tv_language, tv_state_of_charge, tv_connect_device, tv_password, tv_app_version, tv_device_ssid, tv_connect_tip, tv_scan;
    private ImageView iv_modify_transalte_language, iv_modify_password, iv_modify_ssid, iv_signal;
    private ProgressBar pb_progress;
    private LinearLayout ll_package_info, ll_device_connect, ll_device_connecting, ll_device_info;
    private Button bt_scan, bt_flowmall;
    private boolean needCreateBond = true;
    private String deviceMacAddress = "";
    private String deviceSSID = "";
    public static final int RESULT_OK = -1;
    public  Timer timer;
    private String curSsid = "";
    boolean isNewConnect = false;
    boolean isReStartService = false;
    public    EnableWifiReceiver receiver;

    Handler handler=new Handler();


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            boolean order_status = intent.getBooleanExtra("order_status", false);
            if(order_status == true && APIConstants.deviceInfo != null){
                getFlowPackage(APIConstants.deviceInfo.getSsid());
            }
            else{

            }
        }
    };
    /**无线是否连接的监听
     * */
    private void registerNetworkConnectChangeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        receiver = new EnableWifiReceiver();
       getActivity().registerReceiver(receiver, filter);
    }
    public class EnableWifiReceiver extends BroadcastReceiver {


        private String getConnectionType(int type) {
            String connType = "";
            if (type == ConnectivityManager.TYPE_MOBILE) {
                connType = "3G网络数据";
            } else if (type == ConnectivityManager.TYPE_WIFI) {
                connType = "WIFI网络";
            }
            return connType;
        }
        @SuppressLint("LongLogTag")

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
//            Log.i("actioin:"+action+"==>"+printBundle(extras));

            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {//这个监听wifi的打开与关闭，与wifi的连接无关
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        Log.i("wifiState:WIFI_STATE_DISABLED，so quit.", "geg");
//                        showToast("WiFi已关闭，退出登录...");
//                        Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
//                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent2);
//                        finish();
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        Log.i("wifiState:WIFI_STATE_DISABLING", "222");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.i("wifiState:WIFI_STATE_ENABLED", "3333");
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        Log.i("wifiState:WIFI_STATE_ENABLING", "4444");
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        Log.i("wifiState:WIFI_STATE_UNKNOWN", "55555");
                        break;
                }
            }
            // 这个监听wifi的连接状态即是否连上了一个有效无线路由，
            // 当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
            // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，
            // 当然刚打开wifi肯定还没有连接到有效的无线
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                String bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID);

                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    NetworkInfo.State state = networkInfo.getState();
                    Log.i("", "NetWork Sate Change:" + state + " connectedBssid:" + bssid);
                    String curSsid = WiFiUtil.getInstance().getConnectWifiSsid();
                    if (state == NetworkInfo.State.DISCONNECTED) {
                        Log.i("", "DISCONNECTED from " + bssid);
                    } else if (state == NetworkInfo.State.CONNECTED) {


                        Log.i("neognowenon", "CONNECTED to " + curSsid);
                        MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_NET_CHANGE, curSsid, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
                        MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_NET_CHANGE, curSsid, OnNoticeUI.KEY_TYPE_DEVICE_INFO_FRAGMENT);


                    }
//                    if(GlobalUtil.latestRecord != null
//                            && (!TextUtils.isEmpty(curSsid) && !curSsid.contains(GlobalUtil.latestRecord.getSsid().substring(3)))){
//                        Log.i("","ssid already changed! so quit." + GlobalUtil.latestRecord.getSsid());
////                        showToast("网络已变更，退出登录...");
//                        Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
//                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent2);
//                        finish();
//                    }

                } else {
                    Log.i("", "parcelableExtra is null.");
                }
            }

            // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                //获取联网状态的NetworkInfo对象
                NetworkInfo info = intent
                        .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    //如果当前的网络连接成功并且网络连接可用
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI){

                            Log.e("TAG", getConnectionType(info.getType()) + "连上（wifi）");

                        }else if (info.getType() == ConnectivityManager.TYPE_MOBILE){

                            Log.e("TAG", getConnectionType(info.getType()) + "连上（3g）");
                        }
                    } else {
                        Log.e("TAG", getConnectionType(info.getType()) + "断开");
                        if (getConnectionType(info.getType()).equals("WIFI网络"))
                            TcpUtil.getInstance().socketTemp=null;
                        //                        MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_NET_CHANGE, curSsid, OnNoticeUI.KEY_TYPE_DEVICE_INFO_FRAGMENT);

                    }
                }
            }



        } }
    public DeviceInfoFragment(){

    }
    public static DeviceInfoFragment newInstance(){
        DeviceInfoFragment fragment = new DeviceInfoFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_info, container, false);

        ll_package_info = (LinearLayout) rootView.findViewById(R.id.ll_package_info);
        ll_device_connect = (LinearLayout) rootView.findViewById(R.id.ll_device_connect);
        ll_device_connecting = (LinearLayout) rootView.findViewById(R.id.ll_device_connecting);
        ll_device_info = (LinearLayout) rootView.findViewById(R.id.ll_device_info);

        tv_ssid = (TextView) rootView.findViewById(R.id.tv_ssid);
        tv_language = (TextView) rootView.findViewById(R.id.tv_language);
        tv_state_of_charge = (TextView) rootView.findViewById(R.id.tv_state_of_charge);
        tv_connect_device = (TextView) rootView.findViewById(R.id.tv_connect_device);
        tv_password = (TextView) rootView.findViewById(R.id.tv_password);
        tv_app_version = (TextView) rootView.findViewById(R.id.tv_app_version);
        tv_device_ssid = (TextView) rootView.findViewById(R.id.tv_device_ssid);
        tv_connect_tip = (TextView) rootView.findViewById(R.id.tv_connect_tip);
        tv_scan = (TextView) rootView.findViewById(R.id.tv_scan);

        iv_modify_transalte_language = (ImageView) rootView.findViewById(R.id.iv_modify_transalte_language);
        iv_modify_password = (ImageView) rootView.findViewById(R.id.iv_modify_password);
        iv_modify_ssid = (ImageView) rootView.findViewById(R.id.iv_modify_ssid);
        iv_signal  = (ImageView) rootView.findViewById(R.id.iv_signal);

        bt_scan = (Button) rootView.findViewById(R.id.bt_scan);
        bt_flowmall = (Button) rootView.findViewById(R.id.bt_flowmall);
        pb_progress = (ProgressBar) rootView.findViewById(R.id.pb_progress);

        ll_device_connect.setVisibility(View.VISIBLE);
        ll_device_info.setVisibility(View.GONE);
        if(APIConstants.deviceInfo != null) {
            tv_device_ssid.setText(APIConstants.deviceInfo.getSsid());
            tv_connect_tip.setText(getResources().getString(R.string.tip_connect) + APIConstants.deviceInfo.getSsid() + "...");
        }
        if (APIConstants.deviceInfo!=null) {

            deviceMacAddress = APIConstants.deviceInfo.getBluetoothMac();
            deviceSSID = APIConstants.deviceInfo.getSsid();
        }
        ll_device_connect.setOnClickListener(this);
        bt_scan.setOnClickListener(this);
        bt_flowmall.setOnClickListener(this);
        tv_scan.setOnClickListener(this);

        upDateFragment();
        curSsid =  WiFiUtil.getInstance().getConnectWifiSsid();


        iv_modify_transalte_language.setOnClickListener(this);
        if(APIConstants.deviceInfo != null) {
            String ssid;
            if (APIConstants.deviceInfo.getSsid().contains("GTWiFi")){

              ssid = APIConstants.deviceInfo.getSsid().replace("GTWiFi","TGT");

            }else {

                ssid = APIConstants.deviceInfo.getSsid();

            }


            getFlowPackage(ssid);
        }

        //注册接受的广播
        IntentFilter trans = new IntentFilter(APIConstants.BR_LAN_STATUS);

        getActivity().registerReceiver(broadcastReceiver, trans);


        //注册接受的广播
        IntentFilter filter = new IntentFilter(APIConstants.BR_ORDER_STATUS);
        getActivity().registerReceiver(broadcastReceiver, filter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        registerNetworkConnectChangeReceiver();

        MZApplication.getInstance().setOnNoticeUiListener(this, OnNoticeUI.KEY_TYPE_DEVICE_INFO_FRAGMENT);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Log.i("zoubuzouzheli", "onDestroyView: ");
        super.onDestroyView();
        isReStartService = false;
    }

    //销毁Fragment时调用
    public void onDestroy() {

        try {

//            MainService.getInstance().removeHeart();
//            unregisterReceiver(BaseActivity.instance.receiver);
            getActivity().unregisterReceiver(receiver);
            getActivity().unregisterReceiver(broadcastReceiver);


        }
        catch (Exception E){


            Log.i("EEEEEEEE",E.toString());

        }        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_modify_transalte_language:
                break;
            case R.id.iv_modify_password:
                break;
            case R.id.iv_modify_ssid:
                break;
            case R.id.ll_device_connect:
                Log.i("CURRENTWIFI:",curSsid+deviceSSID);
//                Intent intent = new Intent(getActivity(), MainService.class);
                    isReStartService = true;
                String strNew = deviceSSID.replaceAll("[a-zA-Z]","").replace("*","");
//                startMainservice();
//                ll_device_connect.setVisibility(View.GONE);
//                ll_device_connecting.setVisibility(View.VISIBLE);
//                break;

                if (!TextUtils.isEmpty(strNew) && (curSsid.contains(strNew))){

                    Log.i("strnew3333",strNew+curSsid);


//                if (!TextUtils.isEmpty(strNew) && (curSsid.contains(deviceSSID) || curSsid.contains(deviceSSID.replace("TGT","TUGE"))||curSsid.contains(deviceSSID.replace("TGT","GTWiFi")))){
                    startMainservice();


                ll_device_connect.setVisibility(View.GONE);
                ll_device_connecting.setVisibility(View.VISIBLE);}
                else {
                    showAlert();

                }
//                connectDeviceForBT(deviceMacAddress, deviceSSID);
                break;
            case R.id.tv_scan:



                Log.i("GETACTIVITY:",getActivity().toString());
                try {
                    MainService.getInstance().removeHeart();

                }catch (Exception e){
                    e.printStackTrace();
                }

           getActivity().finish();

//                Intent first = new Intent(getActivity(),FirstScanActivity.class);
//                startActivity(first);
//               getActivity().overridePendingTransition(0, 0);

//                getActivity().finish();
                break;
            case R.id.bt_scan:
                operation();

                break;
            case R.id.bt_flowmall:
                ((com.a51tgt.t4m.ui.MainActivity)getActivity()).mViewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }
    private void showAlert(){


        TipUtil.showAlert(getActivity(),
                getActivity().getResources().getText(R.string.tip_title).toString(),
               getString(R.string.tip_connect_wifi) ,
                getActivity().getResources().getText(R.string.commit_button).toString(),
                new TipUtil.OnAlertSelected() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });


    }
//    public static boolean isServiceRunning(Context context, String ServiceName) {
//        if (("").equals(ServiceName) || ServiceName == null)
//            return false;
//        ActivityManager myManager = (ActivityManager) context
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
//                .getRunningServices(30);
//        for (int i = 0; i < runningService.size(); i++) {
//            if (runningService.get(i).service.getClassName().toString()
//                    .equals(ServiceName)) {
//                return true;
//            }
//        }
//        return false;
//    }

    private void operation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("shoudong")
                .setPositiveButton(R.string.tip_connect2, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //集合里的删除点击的条目
                        Intent intent = new Intent(getActivity(), CaptureActivity.class);
                        startActivityForResult(intent, APIConstants.SCANNIN_GREQUEST_CODE);

                    }
                })
                .setNegativeButton(R.string.tip_device_info_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        final AlertDialog isExit = new AlertDialog.Builder(getContext()).create();
                        isExit.setTitle(R.string.tip_title);
                        isExit.setMessage(getResources().getString(R.string.tip_unbind_the_device));


                        }}).show();


    }

    public void startMainservice(){
        try {
            Intent intent = new Intent(getActivity(), MainService.class);
            getActivity().startService(intent);

        }
        catch (Exception E){



        }

      handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("00000000-------","=====");
//                MainService.getInstance().startHeartBeat();


                try {
					MainService.getInstance().startHeartBeat();

                }
                catch (Exception e){

                    Log.i("exception",e.toString());
                }

            }
        },500);
    }



    public void upDateFragment(){
        if(APIConstants.deviceInfo != null && !TextUtils.isEmpty(APIConstants.deviceInfo.getPassword())){

            Log.i("1111", "upDateFragment: "+APIConstants.deviceInfo.getOrginalSsid());
//            if (APIConstants.deviceInfo.getOrginalSsid().contains("GTWiFi")){
//
//
//            }
            tv_ssid.setText(APIConstants.deviceInfo.getOrginalSsid());
            tv_state_of_charge.setText(APIConstants.deviceInfo.getPower());
            tv_connect_device.setText(APIConstants.deviceInfo.getCurrConnections());
            tv_password.setText(APIConstants.deviceInfo.getPassword());
            tv_app_version.setText(APIConstants.deviceInfo.getAppVersion());
            iv_signal.setImageResource(CommUtil.getSignalImage(APIConstants.deviceInfo.getSignal1()));
            ll_device_info.setVisibility(View.VISIBLE);
            ll_device_connect.setVisibility(View.GONE);
            ll_device_connecting.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case APIConstants.SCANNIN_GREQUEST_CODE:
                //从CaptureActivity扫描完成后返回的结果
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra(CaptureActivity.EXTRA_STRING);
                    if (!TextUtils.isEmpty(result)) {
                        //http://aifi.51tgt.com:9988/downloadapp.html?sn=TGT*********&wmac=A4D8CA7B7F88&bmac=4045DA963CB8&imei1=864772030440548&imei2=864772030440549
                        Map<String, String> maps = CommUtil.URLRequest(result);
                        if (maps != null && maps.containsKey("bmac") && maps.containsKey("sn")) {
                            String temp = maps.get("bmac").toUpperCase();
                            if (temp.length() == 12) {
                                deviceMacAddress = temp.substring(0, 2) + ":" + temp.substring(2, 4) + ":" + temp.substring(4, 6) + ":" + temp.substring(6, 8) + ":" + temp.substring(8, 10) + ":" + temp.substring(10, 12);
                            }
                            deviceSSID = maps.get("sn").toString();
                            APIConstants.isScanNewDevice = true;
                        }
                    }
                    if (TextUtils.isEmpty(deviceMacAddress) || TextUtils.isEmpty(deviceSSID)) {
                        Toast.makeText(getActivity(), R.string.tip_can_not_find_device_mac_address, Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        ll_device_info.setVisibility(View.GONE);
                        ll_device_connect.setVisibility(View.GONE);
                        ll_device_connecting.setVisibility(View.VISIBLE);
                        isNewConnect = true;
//                        connectDeviceForBT(deviceMacAddress, deviceSSID);
                    }
                } else {
                }
            break;
        }
    }

    @Override
    public void onNotice(int NOTICE_TYPE, Object object) {
        switch (NOTICE_TYPE){
            case OnNoticeUI.NOTICE_TYPE_BLUETOOTH_CONNECTED:
                APIConstants.isBluetoothConnection = true;

                getDeviceInfo();

                 timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        try {

                            getDeviceInfo();

                        } catch (Exception e) {


                        }
                    }
                };

                timer.schedule(task,10000,10000*3);
                break;
            case OnNoticeUI.NOTICE_TYPE_NET_CHANGE:
                curSsid = object.toString();
                String strNew = deviceSSID.replaceAll("[a-zA-Z]","").replace("*","");


                Log.i("SGEGEWGWEGEHGWE:", curSsid+deviceSSID);
//|| object.toString().contains(deviceSSID.replace("TGT","TUGE"))
                if (!TextUtils.isEmpty(deviceSSID) && (object.toString().contains(strNew) )&&isReStartService) {

                    startMainservice();
                    int status = ll_device_connecting.getVisibility();
                }
                break;
            case OnNoticeUI.NOTICE_TYPE_TCP_FAILED:
                Toast.makeText(MZApplication.getInstance(), R.string.tip_not_connected, Toast.LENGTH_SHORT).show();
                ll_device_connecting.setVisibility(View.GONE);
                if (ll_device_info.getVisibility()==View.GONE) {
                    ll_device_connect.setVisibility(View.VISIBLE);
                }
//                ll_device_info.setVisibility(View.GONE);

                break;
            case OnNoticeUI.NOTICE_TYPE_BLUETOOTH_CANNOT_CONNECTED:
                if(isNewConnect == false && getActivity() != null){

                    TipUtil.showAlert(getActivity(),
                            getActivity().getResources().getText(R.string.tip_title).toString(),
                            getActivity().getResources().getText(R.string.tip_not_connected).toString(),
                            getActivity().getResources().getText(R.string.commit_button).toString(),
                            new TipUtil.OnAlertSelected() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            });
                    APIConstants.isBluetoothConnection = false;
                    ll_device_connect.setVisibility(View.VISIBLE);
                    ll_device_info.setVisibility(View.GONE);
                    ll_device_connecting.setVisibility(View.GONE);
                    isNewConnect = true;
                }
                break;
            case OnNoticeUI.NOTICE_TPYE_BT_SUCCESS:
                Toast.makeText(getContext(),object.toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), object, Toast.LENGTH_SHORT).show();


                break;
            case OnNoticeUI.NOTICE_TYPE_BT_DEVICE_INFO:
                if (null==object || object.equals("")){
                    return;
                }
                try {
                    DeviceInfo info = (DeviceInfo) object;

                    Log.i("sdevevaewhgweah",info.getSsid()+info.getOrginalSsid());

                    APIConstants.deviceInfo = (DeviceInfo) object;
//                            new Gson().fromJson(object.toString(),DeviceInfo.class);
                    if(APIConstants.deviceInfo != null)
                    {
                        if (tv_ssid.getText().equals("")) {
                            upDateFragment();
                            //判断设备是否被激活
//                            OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
//                            params[0] = new OkHttpClientManager.Param("ssid", APIConstants.deviceInfo.getSsid());
//                            new SendRequest(APIConstants.Get_Device_Status, params, new MyHandler(), 2);
                        }else {

                            upDateFragment();
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), R.string.tip_can_not_get_device_info, Toast.LENGTH_LONG ).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void getDeviceInfo(){
        Log.i("gegehehe","zouzheli");

        BluetoothUtil.getInstance().sendMessage(TcpConfig.GET_DEVICE_INFO);
    }

    private void connectDeviceForBT(final String address, String ssid){
        if(TextUtils.isEmpty(ssid))
            ssid = APIConstants.deviceInfo.getSsid();
        tv_connect_tip.setText(getResources().getString(R.string.tip_connect) + ssid + "...");
        if(BluetoothUtil.getInstance().initBluetooth()) {
            BluetoothUtil.getInstance().setBtAddress(address);
            if (!BluetoothUtil.getInstance().isBluetoothEnable()) {
                BluetoothUtil.getInstance().enableBluetooth(getActivity());//发起打开蓝牙

            } else {
                BluetoothUtil.getInstance().stopBluetoothService();
                BluetoothUtil.getInstance().setBtName("tuge_debugtool");
                BluetoothUtil.getInstance().startBluetoothService(getActivity());

                try {
                    BluetoothDevice device = BluetoothUtil.getInstance().getBTDevice(address);
                    if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                        needCreateBond = false;
                        MZApplication.getInstance().runBackGround(new Runnable() {
                            @Override
                            public void run() {
                                BluetoothUtil.getInstance().connectDevice(address, true);
                            }
                        }, 500);
                        MZApplication.getInstance().runMainThread(hiddenNotice, 10*1000);
                    } else {
                        try {
                            //通过工具类ClsUtils,调用createBond方法
                            ClsUtils.createBond(device.getClass(), device);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        BluetoothUtil.getInstance().setBondedCallback(new BluetoothUtil.BondedCallback() {
                            @Override
                            public void BondBonded() {
                                MZApplication.getInstance().runMainThread(hiddenNotice, 10*1000);
                            }
                        });
                        Toast.makeText(getActivity(), R.string.tip_match_bluetooth, Toast.LENGTH_LONG).show();
                        ll_device_info.setVisibility(View.GONE);
                        ll_device_connect.setVisibility(View.VISIBLE);
                        ll_device_connecting.setVisibility(View.GONE);

                        tv_device_ssid.setText(deviceSSID);
                        tv_connect_tip.setText(getResources().getString(R.string.tip_connect) + deviceSSID + "...");
                    }
                }
                catch (Exception e){
                    TipUtil.showAlert(getActivity(),
                            getActivity().getResources().getText(R.string.tip_title).toString(),
                            e.getMessage(),
                            getActivity().getResources().getText(R.string.commit_button).toString(),
                            new TipUtil.OnAlertSelected(){
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                                    startActivityForResult(intent, APIConstants.SCANNIN_GREQUEST_CODE);
                                    dialog.dismiss();
                                }
                            });
                }

            }
        } else {
            String msg = getResources().getString(R.string.error_mobile_phone_not_support_bluetooth);
        }
    }

    Runnable hiddenNotice = new Runnable() {
        @Override
        public void run() {
        }
    };

    private void getFlowPackage(String ssid){
        ll_package_info.removeAllViews();
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[3];
        params[0] = new OkHttpClientManager.Param("device_no",  ssid);
        params[1] = new OkHttpClientManager.Param("useing","1");
        String lan;
        if (APIConstants.currentLan.contains("zh")){

            lan = "zh";
        }else{

            lan = "en";
        }
        params[2] = new OkHttpClientManager.Param("lang", lan);

        new SendRequest(APIConstants.Get_Flow_Package, params, new MyHandler(), 1);
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

            switch (msg.what){
                case 1: {
                    WxHttpResponseData responseData = new WxHttpResponseData((String) msg.obj);
                    if (responseData == null || responseData.status < 0 || responseData.data == null) {
                        return;
                    }
                    if (responseData.data.containsKey("device_order")) {

                        Log.i("deviceorder",responseData.data.get("device_order").toString());

                        ll_package_info.removeAllViews();
                        ArrayList<LinkedTreeMap<String, Object>> products = (ArrayList<LinkedTreeMap<String, Object>>) responseData.data.get("device_order");
                        if (products == null || products.size() == 0) {
                            ll_package_info.setVisibility(View.GONE);
                            return;
                        }
                        try {
                            for (int i = 0; i < products.size(); i++) {


                                    LinkedTreeMap<String, Object> product = products.get(i);
//                                ll_package_info.addView(new PackageInfoView(getContext(), new PackageInfo(product)));
                                ll_package_info.addView(new PackageInfoView(MZApplication.getContext(), new PackageInfo(product)));
                            }
                        }catch (Exception exception ){

                        }
                        List<Double> flowPackage = new ArrayList<Double>();

                        for (int i=0;i<products.size();i++){

                            if (products.get(i).get("product_type").equals(104.0)){

                                continue;
                            }else {
                                flowPackage.add((Double) products.get(i).get("product_type"));


                            }
                            if (flowPackage.size()>0) {
                                APIConstants.deviceInfo.setisFlow(true);

                                Log.i("producttype",flowPackage.toString()+APIConstants.deviceInfo.isFlow());


                            }


                        }

                        //ll_package_info.addView();
                    }
                }
                    break;
                case 2:{
                    HttpResponseData responseData = new HttpResponseData((String) msg.obj);
                    if (responseData == null || responseData.status < 0 || responseData.data == null) {
                        return;
                    }
                    boolean is_active = false;
                    if (responseData.data.containsKey("device")) {
                        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) responseData.data.get("device");
                        if (data.containsKey("is_active") && data.get("is_active").toString().equals("true")) {
                            is_active = true;
                            upDateFragment();
                            if(APIConstants.deviceInfo != null)
                                getFlowPackage(APIConstants.deviceInfo.getSsid());
                        }
                        if(is_active == false){
                            final AlertDialog isExit = new AlertDialog.Builder(getActivity()).create();
                            isExit.setTitle(R.string.tip_title);
                            isExit.setMessage(getResources().getString(R.string.tip_for_active_device));
                            isExit.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ll_device_connect.setVisibility(View.VISIBLE);
                                    ll_device_info.setVisibility(View.GONE);
                                    ll_device_connecting.setVisibility(View.GONE);
                                    //BluetoothUtil.getInstance().stopBluetoothService();
                                    isExit.dismiss();
                                }
                            });
                            isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.commit_button), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isExit.dismiss();
                                    OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
                                    params[0] = new OkHttpClientManager.Param("ssid", APIConstants.deviceInfo.getSsid());
                                    new SendRequest(APIConstants.Device_Active, params, new MyHandler(), 3);
                                }
                            });

                            isExit.show();
                        }
                    }
                }
                    break;
                case 3:{
                    HttpResponseData responseData = new HttpResponseData((String) msg.obj);
                    if (responseData == null || responseData.status < 0 || responseData.data == null) {
                        return;
                    }
                    if (responseData.data.containsKey("active") && responseData.data.get("active").toString().equals("true")) {
                        UserDataUtils.getInstance(getActivity()).setMacAddress(APIConstants.deviceInfo.getBluetoothMac());
                        UserDataUtils.getInstance(getActivity()).setSsid(APIConstants.deviceInfo.getSsid());
                        if(APIConstants.deviceInfo != null)
                            getFlowPackage(APIConstants.deviceInfo.getSsid());
                        upDateFragment();
                    }
                    else {
                        TipUtil.showAlert(getActivity(),
                                getActivity().getResources().getText(R.string.tip_title).toString(),
                                getActivity().getResources().getText(R.string.tip_for_active_device_error).toString(),
                                getActivity().getResources().getText(R.string.commit_button).toString(),
                                new TipUtil.OnAlertSelected() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                }
                    break;
                default:
                    break;
            }
        }
    }
}
