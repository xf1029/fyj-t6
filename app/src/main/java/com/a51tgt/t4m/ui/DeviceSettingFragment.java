package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.adapter.FlowMallAdapter;
import com.a51tgt.t4m.bean.DeviceInfo;
import com.a51tgt.t4m.bean.DevicePackageInfo;
import com.a51tgt.t4m.bean.FlowProductInfo;
import com.a51tgt.t4m.bean.HttpResponseData;
import com.a51tgt.t4m.bluetooth.BluetoothChatService;
import com.a51tgt.t4m.bluetooth.BluetoothUtil;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.service.MainService;
import com.a51tgt.t4m.ui.view.BottomPopupWindow;
import com.a51tgt.t4m.ui.view.OtherWiFiActivity;
import com.a51tgt.t4m.utils.CommUtil;
import com.a51tgt.t4m.utils.TipUtil;
import com.google.gson.Gson;
import com.znq.zbarcode.CaptureActivity;
import com.a51tgt.t4m.net.TcpUtil;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liu_w on 2017/9/13.
 */

public class DeviceSettingFragment extends Fragment implements View.OnClickListener, OnNoticeUI {

    private TextView tv_ssid, tv_translate_language, tv_tip_language,tv_blockSwitch;
    private LinearLayout ll_modify_hotspot_password, ll_setProduction,ll_set_apn, ll_set_blacklist, ll_set_translate_language, ll_set_tip_language, ll_flow_orders, ll_close_hotspot;
    private LinearLayout ll_instruction, ll_software_version, ll_about_us,ll_product;
    private com.a51tgt.t4m.ui.DeviceSetDialog setDialog = new com.a51tgt.t4m.ui.DeviceSetDialog();

    public DeviceSettingFragment(){

    }
    public static DeviceSettingFragment newInstance(){
        DeviceSettingFragment fragment = new DeviceSettingFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        tv_ssid = (TextView)rootView.findViewById(R.id.tv_ssid);
        tv_tip_language = (TextView)rootView.findViewById(R.id.tv_tip_language);
        tv_blockSwitch = (TextView) rootView.findViewById(R.id.blockSwitch);
        ll_modify_hotspot_password = (LinearLayout)rootView.findViewById(R.id.ll_modify_hotspot_password);
        ll_set_apn = (LinearLayout)rootView.findViewById(R.id.ll_set_apn);
        ll_set_blacklist = (LinearLayout)rootView.findViewById(R.id.ll_set_blacklist);
        ll_setProduction = (LinearLayout)rootView.findViewById(R.id.ll_set_production);
        ll_set_tip_language = (LinearLayout)rootView.findViewById(R.id.ll_set_tip_language);
        ll_flow_orders = (LinearLayout)rootView.findViewById(R.id.ll_flow_orders);
//        ll_detection_device = (LinearLayout)rootView.findViewById(R.id.ll_detection_device);
        ll_close_hotspot = (LinearLayout)rootView.findViewById(R.id.ll_close_hotspot);
        ll_software_version  = (LinearLayout)rootView.findViewById(R.id.ll_software_version);
        ll_about_us  = (LinearLayout)rootView.findViewById(R.id.ll_about_us);
        ll_product  = (LinearLayout)rootView.findViewById(R.id.ll_about_product);
        ll_instruction  = (LinearLayout)rootView.findViewById(R.id.ll_instruction);

        if(APIConstants.deviceInfo != null)
            tv_ssid.setText("百度Wifi共享翻译机\n"+APIConstants.deviceInfo.getSsid());
        try {
            if (APIConstants.deviceInfo.isBlockSwitch()==false&&APIConstants.deviceInfo != null){

                tv_blockSwitch.setText(R.string.title_saveflow_close);

            }
            else {

                tv_blockSwitch.setText(R.string.title_saveflow_open);


            }
        } catch (Exception E){



        }



//        if (APIConstants.deviceInfo.)
//tv_blockSwitch.setText();
        ll_modify_hotspot_password.setOnClickListener(this);
        ll_set_apn.setOnClickListener(this);
        ll_set_blacklist.setOnClickListener(this);
        ll_setProduction.setOnClickListener(this);
        ll_set_tip_language.setOnClickListener(this);
        ll_flow_orders.setOnClickListener(this);
        tv_blockSwitch.setOnClickListener(this);
//        ll_detection_device.setOnClickListener(this);
        ll_close_hotspot.setOnClickListener(this);
        ll_software_version.setOnClickListener(this);
        ll_about_us.setOnClickListener(this);
        ll_product.setOnClickListener(this);
        ll_instruction.setOnClickListener(this);


        if(APIConstants.deviceInfo != null && TextUtils.isEmpty(APIConstants.deviceInfo.getAppVersion())){
            MZApplication.getInstance().setOnNoticeUiListener(this, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);

            OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
//
            PackageManager packageManager = getActivity().getPackageManager();
            PackageInfo packInfo = null;
            try {
                String str =   getContext().getPackageName();

                packInfo = packageManager.getPackageInfo(str,0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String version = packInfo.versionName;
            params[0] = new OkHttpClientManager.Param("ver",version);
            Log.i("xiazaidizhi","====="+"fyj_"+version);

            new SendRequest(APIConstants.Get_FYJApp_Info, params, new MyHandler(), 1);
        }

        return rootView;
    }
    //版本号

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            if(APIConstants.deviceInfo != null)
                tv_ssid.setText(APIConstants.deviceInfo.getSsid());
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_modify_hotspot_password:
            case R.id.ll_set_apn:

            case R.id.blockSwitch:
            case R.id.ll_set_blacklist:
            case R.id.ll_connect_wifi:
            case R.id.ll_set_tip_language:
            case R.id.ll_set_production:
//                if (MB.getState() != BluetoothChatService.STATE_CONNECTED) {
                if(TcpUtil.getInstance().socketTemp==null){
                    TipUtil.showAlert(getActivity(),
                            getActivity().getResources().getText(R.string.tip_title).toString(),
                            getActivity().getResources().getText(R.string.error_no_device_connect).toString(),
                            getActivity().getResources().getText(R.string.commit_button).toString(),
                            new TipUtil.OnAlertSelected() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    ((com.a51tgt.t4m.ui.MainActivity)getActivity()).mViewPager.setCurrentItem(0);
                                    dialog.dismiss();
                                }
                            });
                    return;
                }
                break;
        }

        switch (v.getId()){
            case R.id.ll_close_hotspot:

                Intent intentLan = new Intent(getActivity(), OtherWiFiActivity.class);

                getActivity().startActivity(intentLan);

                break;
            case R.id.ll_modify_hotspot_password:
//                if(APIConstants.isBluetoothConnection)
                    setDialog.ModifyDeviceHotSpotPasswordDialog(getActivity());
                break;
            case R.id.ll_set_apn:
//                if(APIConstants.isBluetoothConnection) //未完成蓝牙方式

                    setDialog.SetApnDialog(getActivity());
//                BluetoothUtil.getInstance().sendMessage(TcpConfig.BT_APN_QUERY);

                break;

            case R.id.ll_set_blacklist:
                Intent intent1 = new Intent(getActivity(), com.a51tgt.t4m.ui.SaveFlowActivity.class);

                getActivity().startActivity(intent1);
                break;
//                if(APIConstants.isBluetoothConnection) //未完成蓝牙方式
//                   setDialog.SetBlackListDialog(getActivity());
//               break;
            case R.id.ll_set_production:
                Intent productIntent = new Intent(getActivity(), com.a51tgt.t4m.ui.ReductionActivity.class);
                startActivity(productIntent);
                break;
            case R.id.ll_connect_wifi:
            {
//                String deviceVersion = "V3.1.4";

            String deviceVersion = APIConstants.deviceInfo.getAppVersion();

           String str =  deviceVersion.split("V")[1].replace("","");
                String strNew = str.replaceAll("[a-zA-Z]","");
                Log.i("versionnew",strNew);

           String[] array = strNew.split("\\.");
                try{
                    if (Integer.valueOf(array[0]).intValue() <3) {
                        TipUtil.showAlert(getActivity(),
                                getActivity().getResources().getText(R.string.tip_title).toString(),
                                getActivity().getResources().getText(R.string.error_version_low).toString(),
                                getActivity().getResources().getText(R.string.commit_button).toString(),
                                new TipUtil.OnAlertSelected() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
//                               ((MainActivity)getActivity()).mViewPager.setCurrentItem(0);
                                        dialog.dismiss();
                                    }
                                });
//                    [self showDeviceLow];
                        return;
                    }else if(Integer.valueOf(array[0]).intValue()==3) {

                        if (Integer.valueOf(array[1]).intValue()==0) {
                            if (Integer.valueOf(array[2]).intValue()>27) {
//                            [self connectWifi];
                                Intent intent = new Intent(getActivity(), com.a51tgt.t4m.ui.DeviceConnectWifiActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        else {
                                TipUtil.showAlert(getActivity(),
                                        getActivity().getResources().getText(R.string.tip_title).toString(),
                                        getActivity().getResources().getText(R.string.error_version_low).toString(),
                                        getActivity().getResources().getText(R.string.commit_button).toString(),
                                        new TipUtil.OnAlertSelected() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
//                               ((MainActivity)getActivity()).mViewPager.setCurrentItem(0);
                                                dialog.dismiss();
                                            }
                                        });
//                            [self showDeviceLow];
                                return;
                            }

                        }else {
                            Intent intent = new Intent(getActivity(), com.a51tgt.t4m.ui.DeviceConnectWifiActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
//                        [self connectWifi];

                        }


                    }else if (Integer.valueOf(array[0]).intValue()>3) {
                        Intent intent = new Intent(getActivity(), com.a51tgt.t4m.ui.DeviceConnectWifiActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
//                    [self connectWifi];


                    }


                }catch (Exception exception) {




            }

              Log.i("version", strNew);
                if (Integer.valueOf(str).intValue()<3027){
               TipUtil.showAlert(getActivity(),
                       getActivity().getResources().getText(R.string.tip_title).toString(),
                       getActivity().getResources().getText(R.string.error_version_low).toString(),
                       getActivity().getResources().getText(R.string.commit_button).toString(),
                       new TipUtil.OnAlertSelected() {
                           @Override
                           public void onClick(DialogInterface dialog, int whichButton) {
//                               ((MainActivity)getActivity()).mViewPager.setCurrentItem(0);
                               dialog.dismiss();
                           }
                       });

               return;
           }



            }
                break;

            case R.id.ll_set_tip_language:
                showOptions(v, R.array.tip_language);
                break;
            case R.id.ll_flow_orders:
                Intent intent_device_orders = new Intent(getActivity(), com.a51tgt.t4m.ui.DeviceOrdersActivity.class);
                getActivity().startActivity(intent_device_orders);
                break;

            case R.id.ll_software_version:
//                if(APIConstants.isBluetoothConnection)
                    setDialog.showVersionInfo(getActivity());
                break;
            case R.id.ll_about_us:
                Intent intent_about_us = new Intent(getActivity(), com.a51tgt.t4m.ui.AboutUsActivity.class);
                getActivity().startActivity(intent_about_us);
                break;
            case R.id.ll_about_product:
                Intent intent_product = new Intent(getActivity(), com.a51tgt.t4m.ui.ProductActivity.class);
                getActivity().startActivity(intent_product);
                break;
            case R.id.ll_instruction:
                Intent intent_instruction = new Intent(getActivity(), com.a51tgt.t4m.ui.InstructionCateActivity.class);
                getActivity().startActivity(intent_instruction);
                break;
            default:
                break;
        }
    }

    private void showOptions(View view, int arrayId) {
        BottomPopupWindow optionsWindow = new BottomPopupWindow(getActivity());
        String[] options = getResources().getStringArray(arrayId);
        optionsWindow.showOptions(view, options, createListener(arrayId));
    }

    private BottomPopupWindow.OnOptionsSelectedListener createListener(int arrayId) {
        BottomPopupWindow.OnOptionsSelectedListener listener = null;
        final String[] options = getResources().getStringArray(arrayId);
        switch (arrayId) {
            case R.array.translate_language:
                listener = new BottomPopupWindow.OnOptionsSelectedListener() {
                    @Override
                    public void onOptionSelected(int index) {
                        tv_translate_language.setText(options[index]);
                    }
                };
                break;
            case R.array.tip_language:
                listener = new BottomPopupWindow.OnOptionsSelectedListener() {
                    @Override
                    public void onOptionSelected(int index) {
                        int type=0;
                        if (index==0){
                            type=2;
                        }else if (index==1){
                            type=3;
                        }else if (index==2){
                            type=1;
                        }
                        tv_tip_language.setText(options[index]);
                        JSONObject object = new JSONObject();
                        try {
                            object.put(TcpConfig.SET_DEVICE_LANGUAGE, type);
                            if(APIConstants.isBluetoothConnection)
                            BluetoothUtil.getInstance().sendMessage(object.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                break;
            default:
                break;
        }
        return listener;
    }

    @Override
   public void onNotice(int NOTICE_TYPE, Object object) {
        switch (NOTICE_TYPE){
            case OnNoticeUI.NOTICE_TYPE_NET_CHANGE:
//                if (APIConstants.deviceInfo!=null) {
//                    return;
////                    curSsid = object.toString();
//
//                    String ssid = APIConstants.deviceInfo.getSsid();
//                    String strNew = ssid.replaceAll("[a-zA-Z]","").replace("*","");
//
//                    if (!TextUtils.isEmpty(ssid) && (object.toString().contains(strNew))){
//
//
//
//                    }else {
//
//                        Log.i("SOCKETGEGEG","GEGWEH");
//                        try {
//
//
//
//                            TcpUtil.getInstance().socketTemp=null;
//
//
//                            MainService.getInstance().removeHeart();
////                            showAlert(ssid);
//
//
////            unregisterReceiver(BaseActivity.instance.receiver);
//
//
//                        }
//                        catch (Exception E){
//
//
//                            Log.i("EEEEEEEE",E.toString());
//
//                        }
//                    }
//                }

                    break;
            case 34:
                tv_blockSwitch.setText(APIConstants.deviceInfo.isBlockSwitch()?R.string.title_saveflow_open:R.string.title_saveflow_close);
                break;
            case 44:
                Toast.makeText(MZApplication.getInstance(),object.toString().contains("true")? "设置设备语言成功":"设置设备语言失败",Toast.LENGTH_SHORT).show();


                break;

            case OnNoticeUI.NOTICE_TYPE_CLOSE_WIFIAP:

            case NOTICE_TYPE_SET_BLACK_LIST:

                tv_blockSwitch.setText(APIConstants.deviceInfo.isBlockSwitch()?R.string.title_saveflow_open:R.string.title_saveflow_close);
                break;
            case OnNoticeUI.NOTICE_TYPE_BT_APN_QUERY:
                setDialog.RefreshAPNUi(object);


                break;
            case NOTICE_TYPE_SET_APN:
                String value = object == null ? "" : (String)object;
                if(value.equals("true")) {
                    Toast.makeText(MZApplication.getInstance(), R.string.tip_operation_success, Toast.LENGTH_SHORT).show();
                    //在修改密码成功后，将ApiConstants.deviceInfo中的密码修改
                    if(NOTICE_TYPE == OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD && APIConstants.deviceInfo != null && !TextUtils.isEmpty(APIConstants.newPassword)){
                        APIConstants.deviceInfo.setPassword(APIConstants.newPassword);
                    }
                }
                else
                    Toast.makeText(MZApplication.getInstance(), R.string.tip_operation_failed, Toast.LENGTH_SHORT).show();
                break;

        }
    }
    private void showAlert(String deviceSSID){


        TipUtil.showAlert(getActivity(),
                getActivity().getResources().getText(R.string.tip_title).toString(),
                "Please connect device WiFi",
                getActivity().getResources().getText(R.string.commit_button).toString(),
                new TipUtil.OnAlertSelected() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

//
//        TipUtil.showAlert(getActivity(),
//                getActivity().getResources().getText(R.string.tip_title).toString(),
//                "Please connect WiFi name:"+deviceSSID,
//                getActivity().getResources().getText(R.string.commit_button).toString(),
//                new TipUtil.OnAlertSelected() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                    }
//                });


    }

    int setDeviceLanguageInt;

    private void setDevicelanguage() {
        final String items[] = {"简体中文", "英文","日文"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // 先得到构造器
        builder.setTitle("设置语言"); // 设置标题
        setDeviceLanguageInt = 2;
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            setDeviceLanguageInt = 2;
                        } else if (which == 1) {
                            setDeviceLanguageInt = 3;
                        } else if (which == 2) {
                            setDeviceLanguageInt = 4;
                        }
                    }
                });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject object = new JSONObject();
                try {
                    object.put(TcpConfig.SET_DEVICE_LANGUAGE, setDeviceLanguageInt);
                    if(APIConstants.isBluetoothConnection){

                        BluetoothUtil.getInstance().sendMessage(object.toString());


                    } //未完成蓝牙方式

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Log.i("response",msg.toString());

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
                    Log.i("response",responseData.toString());
//                    APIConstants.deviceVersion = "T4_V2.1.6";//responseData.data.get("new_version").toString();
                    if(responseData.data.containsKey("new_version") && !TextUtils.isEmpty(responseData.data.get("new_version").toString())){
                        APIConstants.deviceVersion = responseData.data.get("new_version").toString();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
