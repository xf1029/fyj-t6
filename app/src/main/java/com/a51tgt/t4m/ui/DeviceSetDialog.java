package com.a51tgt.t4m.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.bluetooth.BluetoothChatService;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.service.MainService;
import com.google.gson.Gson;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.bean.ApnNode;
import com.a51tgt.t4m.bean.DeviceInfo;
import com.a51tgt.t4m.bluetooth.BluetoothUtil;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.net.TcpUtil;
import com.a51tgt.t4m.utils.CommUtil;
import com.a51tgt.t4m.utils.PwdCheckUtil;
import com.a51tgt.t4m.utils.TipUtil;

import org.json.JSONException;
import org.json.JSONObject;
import android.support.design.widget.TextInputEditText;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.security.acl.LastOwnerException;
import java.sql.Ref;
import java.util.ArrayList;
import com.a51tgt.t4m.BuildConfig;


/**
 * Created by liu_w on 2017/9/15.
 */

public class DeviceSetDialog implements OnNoticeUI {


    public TextInputEditText et_apn;
    public TextInputEditText et_apn_name;
    public TextInputEditText et_apn_mcc;
    public TextInputEditText et_apn_mnc;
    public TextInputEditText et_apn_numermic;


    final private int CloseDeviceHotSpotDialog_What = 1;
    final private int ModifyDeviceHotSpotPasswordDialog_What = 2;
    final private int SetApnDialog_What = 3;
    final private int SetBlacklistDialog_What = 4;

    private Context mContext;
    private int whichFile;

    public void ModifyDeviceHotSpotPasswordDialog(final Context context){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_modify_device_hotspot, null));
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_modify_device_hotspot);
        final TextInputEditText et_old_password = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_old_password);

        final TextInputEditText et_new_password = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_new_password);
        final TextView tv_error_tip = (TextView) dialog.getWindow().findViewById(R.id.tv_error_tip);
        tv_error_tip.setVisibility(View.GONE);

        et_old_password.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager)et_old_password.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_old_password, 0);
            }
        }, 1000);

        dialog.getWindow().findViewById(R.id.ok)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_error_tip.setVisibility(View.GONE);
                        if(TextUtils.isEmpty(et_old_password.getText()) || !et_old_password.getText().toString().equals(APIConstants.deviceInfo.getPassword())){
                            tv_error_tip.setVisibility(View.VISIBLE);
                            tv_error_tip.setText(context.getResources().getText(R.string.error_check_old_password).toString());
                            return;
                        }
                        //if(et_new_password.getText().length() < 8 || !PwdCheckUtil.isLetterDigit(et_new_password.getText().toString())){
                        if(et_new_password.getText().length() < 8){
                            tv_error_tip.setVisibility(View.VISIBLE);
                            tv_error_tip.setText(context.getResources().getText(R.string.error_set_new_password2).toString());
                            return;
                        }
                        JSONObject object = new JSONObject();
                        JSONObject json = new JSONObject();
                        try {
                            json.put("SSID", APIConstants.deviceInfo.getOrginalSsid());
                            json.put("PWD", et_new_password.getText().toString());
                            object.put(TcpConfig.KEY, TcpConfig.SET_WIFIANDPASSWORD);
                            object.put(TcpConfig.VALUE, json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            tv_error_tip.setVisibility(View.VISIBLE);
                            tv_error_tip.setText(context.getResources().getText(R.string.error_set_device).toString());
                            return;
                        }
                        APIConstants.newPassword = et_new_password.getText().toString();
                        MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD, "true", OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);

                        Log.i("NEWPAAAA",object.toString());
                        MainService.getInstance().addRequest(object.toString());
                        //TcpUtil.getInstance().sendMessage(object.toString(), new DialogHandler(context, tv_error_tip, dialog, ModifyDeviceHotSpotPasswordDialog_What), ModifyDeviceHotSpotPasswordDialog_What);
//                        BluetoothUtil.getInstance().sendMessage(object.toString());
                        dialog.dismiss();

                    }
                });
        dialog.getWindow().findViewById(R.id.bt_default)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainService.getInstance().addRequest(TcpConfig.SET_WIFIANDPASSWORD);

//                        BluetoothUtil.getInstance().sendMessage(TcpConfig.SET_WIFIANDPASSWORD);
                        dialog.dismiss();
                        //
                        MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD, "true", OnNoticeUI.KEY_TYPE_MAIN_ACTIVITY);


                    }
                });
        dialog.getWindow().findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
    }

    public void CloseDeviceHotSpotDialog(final Context context){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_close_device_hotspot, null));
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_close_device_hotspot);
        final TextView tv_error_tip = (TextView) dialog.getWindow().findViewById(R.id.tv_error_tip);


        dialog.getWindow().findViewById(R.id.ok)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TcpUtil.getInstance().sendMessage(TcpConfig.CLOSE_WIFIAP, new DialogHandler(context, tv_error_tip, dialog, CloseDeviceHotSpotDialog_What), CloseDeviceHotSpotDialog_What);
                        BluetoothUtil.getInstance().sendMessage(TcpConfig.CLOSE_WIFIAP);
                        dialog.dismiss();

                    }
                });
        dialog.getWindow().findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
    }


//    }





    public void SetApnDialog(final Context context){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_set_apn, null));
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_set_apn);
        final TextView tv_error_tip = (TextView) dialog.getWindow().findViewById(R.id.tv_error_tip);
        et_apn_name = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn_name);

        et_apn = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn);
        et_apn_mcc = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn_mcc);
        et_apn_mnc = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn_mnc);
        et_apn_numermic = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn_numermic);
        final TextInputEditText et_apn_type = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn_type);
        final TextInputEditText et_apn_user = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn_user);
        final TextInputEditText et_apn_password = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn_password);
        final TextInputEditText et_apn_port = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn_port);
        final TextInputEditText et_apn_proxy = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn_proxy);
        final TextInputEditText et_apn_mmsc = (TextInputEditText) dialog.getWindow().findViewById(R.id.et_apn_mmsc);

        RefreshAPNUi(APIConstants.deviceInfo.getApn2());

        et_apn_name.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager)et_apn_name.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_apn_name, 0);
            }
        }, 1000);

        dialog.getWindow().findViewById(R.id.ok)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_error_tip.setVisibility(View.GONE);
                        if(TextUtils.isEmpty(et_apn_name.getText())){
                            tv_error_tip.setVisibility(View.VISIBLE);
                            tv_error_tip.setText(context.getResources().getText(R.string.error_apn_name).toString());
                            return;
                        }
                        else if(TextUtils.isEmpty(et_apn.getText())){
                            tv_error_tip.setVisibility(View.VISIBLE);
                            tv_error_tip.setText(context.getResources().getText(R.string.error_apn).toString());
                            return;
                        }
                        else if(TextUtils.isEmpty(et_apn_mcc.getText())){
                            tv_error_tip.setVisibility(View.VISIBLE);
                            tv_error_tip.setText(context.getResources().getText(R.string.error_apn_mcc).toString());
                            return;
                        }else if(TextUtils.isEmpty(et_apn_mnc.getText())){
                            tv_error_tip.setVisibility(View.VISIBLE);
                            tv_error_tip.setText(context.getResources().getText(R.string.error_apn_mnc).toString());
                            return;
                        }
                        else if(TextUtils.isEmpty(et_apn_numermic.getText())){
                            tv_error_tip.setVisibility(View.VISIBLE);
                            tv_error_tip.setText(context.getResources().getText(R.string.error_apn_numermic).toString());
                            return;
                        }
                        JSONObject object = new JSONObject();
                        ApnNode apn=new ApnNode();
                        apn.setApn(et_apn.getText().toString());
                        apn.setName(et_apn_name.getText().toString());
                        apn.setMcc(et_apn_mcc.getText().toString());
                        apn.setMnc(et_apn_mnc.getText().toString());
                        apn.setNumeric(et_apn_numermic.getText().toString());
                        if (!TextUtils.isEmpty(et_apn_type.getText())) {
                            apn.setType(et_apn_type.getText().toString());
                        }
                        apn.setUser(et_apn_user.getText().toString());
                        apn.setPassword(et_apn_password.getText().toString());
                        apn.setMmsc(et_apn_mmsc.getText().toString());
                        apn.setPort(et_apn_port.getText().toString());
                        apn.setProxy(et_apn_proxy.getText().toString());
                        String apnStr = new Gson().toJson(apn);
                        try {
                            object.put(TcpConfig.KEY, TcpConfig.SET_APN);
                            object.put(TcpConfig.VALUE, apnStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            e.printStackTrace();
                            tv_error_tip.setVisibility(View.VISIBLE);
                            tv_error_tip.setText(context.getResources().getText(R.string.error_set_device).toString());
                            return;
                        }
//                        if(APIConstants.isBluetoothConnection) {
//                            BluetoothUtil.getInstance().sendMessage(object.toString());
                            MainService.getInstance().addRequest(object.toString());
//                        }
                        dialog.dismiss();
                    }
                });
        dialog.getWindow().findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
    }
    public void RefreshAPNUi (Object object) {
        if (et_apn_name==null)return;

        Log.i("OBJECT:",object.toString());
        String[] str1 = null;
        try {
            str1 = object.toString().split(",");
            for (int i=0;i<str1.length;i++) {
                Log.i("OBJECT333:", str1[i]);

                if (str1[i].contains("name")){

                    et_apn_name.setText(str1[i].split(":")[1]
                    );

                }
                else if (str1[i].contains("apn")){

                    et_apn.setText(str1[i].split(":")[1]);

                }
                else if (str1[i].contains("mcc")){

                    et_apn_mcc.setText(str1[i].split(":")[1]);

                }
                else if (str1[i].contains("mnc")) {

                    et_apn_mnc.setText(str1[i].split(":")[1]);

                    et_apn_numermic.setText(et_apn_mcc.getText().toString()+et_apn_mnc.getText().toString());

                }

            }

        }catch ( Exception E){


            Log.i("OBJECT:",E.toString());

        }
        Log.i("OBJECT333:", String.valueOf(str1.length));


//        String apnInfo = object.toString();
//
//        String name = apnInfo.substring(apnInfo.indexOf("["));
//        String newString = name.replace("[","");
//        String newString1 = newString.replace("]","");
//        String[] arr = newString1.split(",");//分割字符串得到数组
//        ArrayList<TextInputEditText>  editTexts =new ArrayList<>() ;
//
//        editTexts.add(et_apn_name);
//        editTexts.add(et_apn);
//
//        editTexts.add(et_apn_mcc);
//        editTexts.add(et_apn_mnc);
//        editTexts.add(et_apn_numermic);

        //[]{et_apn,et_apn_name,et_apn_mcc,et_apn_mnc,et_apn_numermic};
//        for (int i = 0; i < arr.length; i++) {
//
//            String str = String.valueOf(arr[i]);
////            String []  str1 = str.split("=");
////            Log.i("apn", str1[1]);
//           TextInputEditText text =  editTexts.get(i);
//           text.setHint("");
//           text.setText(str);
//            et_apn.setHint(String.valueOf(arr[0]).split("=")[1]);
//            et_apn_name.setHint(String.valueOf(arr[1]).split("=")[1]);
//            et_apn_mcc.setHint(String.valueOf(arr[2]).split("=")[1]);
//            et_apn_mnc.setHint(String.valueOf(arr[3]).split("=")[1]);
//            et_apn_numermic.setHint(String.valueOf(arr[4]).split("=")[1]);

    }

    //省流量模式
    public void setSaveFlow (boolean open){


        JSONObject object = new JSONObject();
        try {
            object.put(TcpConfig.KEY, TcpConfig.SET_BLACK_LIST);
//                            if (!TextUtils.isEmpty(et_blacklist.getText().toString())) {
//                                object.put(TcpConfig.VALUE, et_blacklist.getText().toString());
//                            }
            object.put(TcpConfig.BLACK_LIST_ENABLE, open);
            Log.i("tag","gegegeg"+object.toString());
//            BluetoothUtil.getInstance().sendMessage(object.toString());
            MainService.getInstance().addRequest(object.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }



    }
//恢复设备默认热点密码
public void setDefaultPwd (String cmd){


//    try {

        String s1 = new String(cmd);

//        String s2 = s1+ BluetoothChatService.END_FLAG;
//        object.put(TcpConfig.KEY, TcpConfig.SET_BLACK_LIST);
////                            if (!TextUtils.isEmpty(et_blacklist.getText().toString())) {
////                                object.put(TcpConfig.VALUE, et_blacklist.getText().toString());
////                            }
//        object.put(TcpConfig.VALUE, open);
//       Log.i("tag","gegegeg"+s2);
//       BluetoothUtil.getInstance().sendMessage(s1);


    MainService.getInstance().addRequest(s1);
//    Toast.makeText(this, R.string.tip_operation_success,Toast.LENGTH_LONG).show();


//    } catch (JSONException e) {
//        e.printStackTrace();
//        return;
//    }



}




    public void SetBlackListDialog(final Context context){final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_set_blacklist, null));
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_set_blacklist);
        final TextView tv_error_tip = (TextView) dialog.getWindow().findViewById(R.id.tv_error_tip);
        final EditText et_blacklist = (EditText) dialog.getWindow().findViewById(R.id.et_blacklist);

        et_blacklist.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager)et_blacklist.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_blacklist, 0);
            }
        }, 1000);

        dialog.getWindow().findViewById(R.id.ok)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_error_tip.setVisibility(View.GONE);
//                        if(TextUtils.isEmpty(et_blacklist.getText())){
//                            tv_error_tip.setVisibility(View.VISIBLE);
//                            tv_error_tip.setText(context.getResources().getText(R.string.error_set_blacklist).toString());
//                            return;
//                        }
                        JSONObject object = new JSONObject();
                        try {
                            object.put(TcpConfig.KEY, TcpConfig.SET_BLACK_LIST);
//                            if (!TextUtils.isEmpty(et_blacklist.getText().toString())) {
//                                object.put(TcpConfig.VALUE, et_blacklist.getText().toString());
//                            }
                            object.put(TcpConfig.VALUE, true);
                            Log.i("tag","gegegeg"+object.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            tv_error_tip.setVisibility(View.VISIBLE);
                            tv_error_tip.setText(context.getResources().getText(R.string.error_set_device).toString());
                            return;
                        }
                        if(APIConstants.isBluetoothConnection)
//                            BluetoothUtil.getInstance().sendMessage(object.toString());
                        dialog.dismiss();
                    }
                });
        dialog.getWindow().findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
    }

    public void deviceStatusCheck(final Context context) {
        mContext = context;
        String message = "";
        try {
            DeviceInfo deviceInfo = APIConstants.deviceInfo;
            message += "设备电量:" + deviceInfo.getPower();
            message += "\n设备连接数:" + deviceInfo.getCurrConnections();
            message += "\n设备M2 Firmware版本:" + deviceInfo.getVersion();
            message += "\n设备M2 App版本:" + deviceInfo.getAppVersion();
            if (!deviceInfo.getVersionM1().isEmpty()) {
                message += "\n设备M1 Firmware版本:" + deviceInfo.getVersionM1();
            }
            if (!deviceInfo.getAppM1Version().isEmpty()) {
                message += "\n设备M1 App版本:" + deviceInfo.getAppM1Version();
            }
            message += "\n设备SSID:" + deviceInfo.getSsid();
            message += "\n设备密码:" + deviceInfo.getPassword();
            message += "\n当前SIM卡:" + deviceInfo.getCurrSim();
            if (deviceInfo.getCurrSim().equals("2")) {
                message += "\n信号强度:" + deviceInfo.getSignal2();
                try {
                    message += "\nSIM状态:"
                            + getSimState(Integer.parseInt(deviceInfo
                            .getSimState1()));
                    message += "\n数据连接状态:"
                            + getDateState(Integer.parseInt(deviceInfo
                            .getDataState1()));

                } catch (Exception e) {
                }
                message += "\nIMSI:" + deviceInfo.getImsi1();
                message += "\nICCID:" + deviceInfo.getIccid1();
                message += "\nAPN:" + deviceInfo.getApn2();
                message += "\n数据网络类型:" + deviceInfo.getNetworkTypeName1();
                message += "\n国家代码:" + deviceInfo.getSimCountryIso1();
                message += "\n基站信息:" + deviceInfo.getCellLocationGemini1();
            } else {
                message += "\n信号强度:" + deviceInfo.getSignal1();
                try {
                    message += "\nSIM状态:"
                            + getSimState(Integer.parseInt(deviceInfo
                            .getSimState0()));
                    message += "\n数据连接状态:"
                            + getDateState(Integer.parseInt(deviceInfo
                            .getDataState0()));

                } catch (Exception e) {
                }
                message += "\nIMSI:" + deviceInfo.getImsi0();
                message += "\nICCID:" + deviceInfo.getIccid0();
                message += "\nAPN:" + deviceInfo.getApn1();
                message += "\n数据网络类型:" + deviceInfo.getNetworkTypeName0();
                message += "\n国家代码:" + deviceInfo.getSimCountryIso0();
                message += "\n基站信息:" + deviceInfo.getCellLocationGemini0();
            }
            message += "\nIMEI1:" + deviceInfo.getImei0();
            message += "\nIMEI2:" + deviceInfo.getImei1();
            message+="\n蓝牙mac地址:"+deviceInfo.getBluetoothMac();
            message+="\n设备类型:"+(deviceInfo.getDeviceType().equals("5")?"5:租赁(仅流量)":(deviceInfo.getDeviceType().equals("1")?"1:售卖(翻译+流量)"
                    :(deviceInfo.getDeviceType().equals("2")?"2:租赁(仅翻译)":(deviceInfo.getDeviceType().equals("3")?"3:租赁(翻译+流量)"
                    :(deviceInfo.getDeviceType().equals("4:售卖(仅流量)")?"":"0:售卖(仅翻译)")))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        showAlert("设备详情 ",message);
    }

    private void showAlert(String title, String message) {
        if (message.contains("\n")) {
            message = message.replace("\n", "<br />");
        }
        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mContext).create();
        dialog.setTitle(title);
        dialog.setMessage(Html.fromHtml(message));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "获取设备日志", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!APIConstants.isBluetoothConnection) {

                } else {
                    JSONObject res = new JSONObject();
                    try{
                        res.put(TcpConfig.KEY, TcpConfig.GET_DEVICE_ALL_LOGS);
                        res.put(TcpConfig.VALUE, BluetoothUtil.getInstance().getSelfBtAddress());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if(res != null){
                        BluetoothUtil.getInstance().sendMessage(res.toString());
                    }
                }
            }
        });
        dialog.show();
    }

    private String getSimState(int state) {
        String simState = "";
        switch (state) {
            case TelephonyManager.SIM_STATE_READY:// 良好
                simState = "READY-良好";
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:// 未知
                simState = "UNKNOWN-未知";
                break;
            case TelephonyManager.SIM_STATE_ABSENT:// 无卡
                simState = "ABSENT-无卡";
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:// 需要PIN解锁
                simState = "PIN_REQUIRED-需要PIN解锁";
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:// 需要PUK解锁
                simState = "PUK_REQUIRED-需要PUK解锁";
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:// 需要NetworkPIN解锁
                simState = "NETWORK_LOCKED-需要NetworkPIN解锁";
                break;
            default:
                simState = "未做处理,状态值为:" + state;
                break;
        }
        return simState;
    }

    private String getDateState(int state) {
        String dateState = "数据未知";
        switch (state) {
            case TelephonyManager.DATA_CONNECTED:
                dateState = "数据已连接";
                break;

            case TelephonyManager.DATA_CONNECTING:
                dateState = "数据连接中";
                break;
            case TelephonyManager.DATA_DISCONNECTED:
                dateState = "数据断连";
                break;
            case TelephonyManager.DATA_SUSPENDED:
                dateState = "数据暂停";
                break;
        }
        return dateState;
    }

    public void showVersionInfo(final Context context) {
        mContext = context;
        final AlertDialog isExit = new AlertDialog.Builder(context).create();
        isExit.setTitle(R.string.tip_newest_version_check);
        isExit.setMessage(TextUtils.isEmpty(APIConstants.deviceVersion)?context.getResources().getString(R.string.tip_no_new_version) : APIConstants.deviceVersion);
        isExit.setButton(AlertDialog.BUTTON_NEGATIVE, TextUtils.isEmpty(APIConstants.deviceVersion)?context.getResources().getString(R.string.button_cancel):context.getResources().getString(R.string.button_no_update), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        if(!TextUtils.isEmpty(APIConstants.deviceVersion)){
            isExit.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.button_update), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                isExit.dismiss();
                showNewVersionFoundDialog(context, APIConstants.Get_Download_App + "/" + APIConstants.deviceVersion + ".apk", "fyj_"+APIConstants.deviceVersion + ".apk");
                Log.i("downloadurl","fyj_"+APIConstants.deviceVersion + ".apk");
                }
            });
        }

        isExit.show();
    }

    private void showNewVersionFoundDialog(final Context context, String url, String file_name)
    {
        final AlertDialog myDialog = new AlertDialog.Builder(context).create();
        myDialog.show();
        myDialog.getWindow().setContentView(R.layout.view_download_dialog);
        final ProgressBar pb_progress = (ProgressBar) myDialog.getWindow().findViewById(R.id.pb_progress);
        final TextView tv_percent = (TextView) myDialog.getWindow().findViewById(R.id.tv_progress);
        final  Handler handler = new Handler();
        //下载易借款安装包
        SendRequest.downLoadFile(url, APIConstants.FILE_DOWNLOAD, file_name, new SendRequest.ProgressCallBack()
        {
            @Override
            public void onProgress(long total, long current)
            {
                final int percent = (int) (((double) current / total) * 100);
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        pb_progress.setProgress(percent);
                        tv_percent.setText(percent + " %");
                    }
                });
            }

            @Override
            public void onComplete()
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                       myDialog.dismiss();
                        updateDeviceSoftware();
                    }
                });
            }

            @Override
            public void onError()
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
//                        myDialog.dismiss();
                        tv_percent.setText(context.getResources().getString(R.string.tip_download_error));
                    }
                });
            }
        });
    }

    private void updateDeviceSoftware(){
        ArrayList<File> targetFiles = new ArrayList<>();
        File dir = new File(APIConstants.FILE_DOWNLOAD);
        File[] childrenFiles = dir.listFiles();
        Log.i("filfilefilfeifi",APIConstants.FILE_DOWNLOAD+childrenFiles.toString());
        if(childrenFiles != null){
            for (int j = 0; j < childrenFiles.length; j++) {
                if (childrenFiles[j].getName().endsWith(".apk")) {
                    targetFiles.add(childrenFiles[j]);
                }
            }
        }


        if (targetFiles == null || targetFiles.size() <= 0) {//请将apk文件放至Download目录
            android.app.AlertDialog isExit2 = new android.app.AlertDialog.Builder(mContext).create();
            isExit2.setTitle(R.string.tip_can_not_find_file);
            isExit2.setButton(mContext.getResources().getString(R.string.button_commit), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case android.app.AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                            break;
                        case android.app.AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                            break;
                        default:
                            break;
                    }
                }
            });
            isExit2.show();
        } else {
            whichFile = 0;
            showAPKUpdateDialog(targetFiles);
        }
    }

    private void showAPKUpdateDialog(final ArrayList<File> files) {
        final String items[] = new String[files.size()];
        for (int i = 0; i < files.size(); i++) {
            items[i] = files.get(i).getName();
        }

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext); // 先得到构造器
        builder.setTitle(R.string.tip_select_file); // 设置标题
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        whichFile = which;
                    }
                });

        builder.setPositiveButton(R.string.button_install, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (!APIConstants.isBluetoothConnection) {

//                } else {
                    JSONObject res = new JSONObject();
                    try {
                        res.put(TcpConfig.KEY, TcpConfig.BT_SEND_APK);
                        res.put(TcpConfig.VALUE, files.get(whichFile).getName());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if(res != null){



//                        BluetoothUtil.getInstance().sendMessage(res.toString());

                        String uri = "file://" + files.get(whichFile).getAbsolutePath();
                        File FILE = new File( Environment.getExternalStorageDirectory(),"/Tuge/Download/fyj_1.0.5.apk");

                        File eee = new File(files.get(whichFile).getAbsolutePath());
                        Log.i("test:",eee.toString());
                            if(Build.VERSION.SDK_INT>=24) {//判读版本是否在7.0以上
                                File file= new File(uri);
                                Log.i("provider",BuildConfig.APPLICATION_ID+".provider"+eee);
                                Uri apkUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID+".provider", FILE);//在AndroidManifest中的android:authorities值
                                Intent install = new Intent(Intent.ACTION_VIEW);
                                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                mContext.startActivity(install);
                            }else {
                                Log.i("anzhuang", FILE.toString());
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(FILE), "application/vnd.android.package-archive");
                                mContext.startActivity(intent);
                            }
//                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                        //sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        sharingIntent.setType("*/*");
//                        //sharingIntent.setComponent(new ComponentName("com.android.bluetooth", "com.android.bluetooth.opp.BluetoothOppLauncherActivity"));
//                        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
//                        mContext.startActivity(sharingIntent);

                    }
                }
//            }
        });
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onNotice(int NOTICE_TYPE, Object object) {
        switch (NOTICE_TYPE){


            case OnNoticeUI.NOTICE_TYPE_BT_APN_QUERY:
//                DeviceInfo deviceInfo = APIConstants.deviceInfo;
                Log.i("DGEGWEGWEG",object.toString());

//                et_apn.setText("gege");
//
//                String apnInfo = object.toString();
//                String name = apnInfo.substring(apnInfo.indexOf("["));
//                Log.i("apn",name);
            break;

        }
    }


    public class DialogHandler extends Handler {

        private Context mContext;
        private TextView tv_tipView;
        private AlertDialog mDialog;

        private int cur_msg_what;
        public DialogHandler(Context context, TextView tipView, AlertDialog dialog, int msgWhat){
            mContext = context;
            tv_tipView = tipView;
            cur_msg_what = msgWhat;
            mDialog = dialog;
        }
        public DialogHandler(){

        }

        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.obj == null) {
                return;
            }
            if(msg.what == -10){
                switch (cur_msg_what){
                    case CloseDeviceHotSpotDialog_What:
                        tv_tipView.setText(mContext.getResources().getText(R.string.error_close_device_hotspot).toString());
                        break;
                    default:
                        break;
                }
                return;
            }
            String response = (String) msg.obj;
            if (TextUtils.isEmpty(response)) {
                return;
            }
            switch (msg.what){
                case CloseDeviceHotSpotDialog_What:
                case ModifyDeviceHotSpotPasswordDialog_What:
                case SetApnDialog_What:
                case SetBlacklistDialog_What:
                    mDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    }
}
