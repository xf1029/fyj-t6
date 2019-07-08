package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.net.TcpUtil;
import com.a51tgt.t4m.utils.CommUtil;
import com.a51tgt.t4m.utils.TipUtil;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.znq.zbarcode.CaptureActivity;

import java.util.List;
import java.util.Map;

public class SplashActivity03 extends BaseActivity implements OnNoticeUI, View.OnClickListener {

    private static final long DELAY_1S = 1000;
    private Context mContext;
    private MyHandler myHandler;
//    private WifiManager wifiManager;
//    private NetworkInfo networkInfo;

    private List<ScanResult> wifiList;
    private RecyclerView rvWifi;
    private View anchor;
    private String deviceMacAddress = "";
    private String deviceSSID = "";
    private String LogTag = "LOGTAG";
    private boolean needCreateBond = true;
    private ProgressBar pb_progress;
    private Button bt_scan;

    Handler getDeviceInfoHandler = new Handler();
    Handler waitWifiEnableHandler = new Handler();
    Runnable getDeviceInfoRunnable = new Runnable() {
        @Override
        public void run() {
            TcpUtil.getInstance().sendMessage(TcpConfig.GET_DEVICE_INFO, new MyHandler());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bt_scan = (Button) findViewById(R.id.bt_scan);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
        anchor = (View) findViewById(R.id.anchor);
        mContext = this;

        bt_scan.setOnClickListener(this);

//        Locale mSystemLanguageList[]= Locale.getAvailableLocales();
//        String lan =Locale.getDefault().getLanguage();
//        String cou = Locale.getDefault().getCountry();

        //40:45:DA:96:3C:B8
        //40:45:DA:A6:B2:56
        deviceMacAddress = UserDataUtils.getInstance(mContext).getMacAddress();
        deviceSSID = UserDataUtils.getInstance(mContext).getSsid();
        if (TextUtils.isEmpty(deviceMacAddress) || TextUtils.isEmpty(deviceSSID)) {
            bt_scan.setVisibility(View.VISIBLE);
//            TipUtil.showAlert(mContext,
//                    mContext.getResources().getText(R.string.tip_title).toString(),
//                    mContext.getResources().getText(R.string.tip_for_new_scan_connected).toString(),
//                    mContext.getResources().getText(R.string.commit_button).toString(),
//                    new TipUtil.OnAlertSelected() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            Intent intent = new Intent(mContext, CaptureActivity.class);
//                            startActivityForResult(intent, APIConstants.SCANNIN_GREQUEST_CODE);
//                            dialog.dismiss();
//                        }
//                    });
        } else {
            anchor.postDelayed(new Runnable() {
            @Override
            public void run() {
                APIConstants.deviceInfo = new DeviceInfo(deviceSSID);
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_1S);

//            connectDeviceForBT(deviceMacAddress);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MZApplication.getInstance().setOnNoticeUiListener(this, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MZApplication.getInstance().setOnNoticeUiListener(null, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MZApplication.getInstance().setOnNoticeUiListener(null, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case APIConstants.SCANNIN_GREQUEST_CODE:
                //从CaptureActivity扫描完成后返回的结果
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra(CaptureActivity.EXTRA_STRING);
                    if (!TextUtils.isEmpty(result)) {
                        //http://aifi.51tgt.com:9988/downloadapp.html?sn=TGT*********&wmac=A4D8CA7B7F88&bmac=4045DA963CB8&imei1=864772030440548&imei2=864772030440549
                        Map<String, String> maps = CommUtil.URLRequest(result);
                        if (maps != null && maps.containsKey("bmac")) {
                            String temp = maps.get("bmac").toUpperCase();
                            if (temp.length() == 12) {
                                deviceMacAddress = temp.substring(0, 2) + ":" + temp.substring(2, 4) + ":" + temp.substring(4, 6) + ":" + temp.substring(6, 8) + ":" + temp.substring(8, 10) + ":" + temp.substring(10, 12);
                                pb_progress.setVisibility(View.VISIBLE);
                                //判断设备是否被激活
//                                OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
//                                params[0] = new OkHttpClientManager.Param("ssid", BuildConfig.DEBUG ? "TGT24170833260" : APIConstants.deviceInfo.getSsid());
//                                new SendRequest(APIConstants.Get_Device_Status, params, new MyHandler(), 1);

                                //UserDataUtils.getInstance(mContext).setMacAddress(deviceMacAddress);
                            }
                        }
                    }
                    if (TextUtils.isEmpty(deviceMacAddress)) {
                        Toast.makeText(mContext, R.string.tip_can_not_find_device_mac_address, Toast.LENGTH_LONG).show();
                        TipUtil.showAlert(mContext,
                                mContext.getResources().getText(R.string.tip_title).toString(),
                                mContext.getResources().getText(R.string.tip_for_new_scan_connected).toString(),
                                mContext.getResources().getText(R.string.commit_button).toString(),
                                new TipUtil.OnAlertSelected() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent intent = new Intent(mContext, CaptureActivity.class);
                                        startActivityForResult(intent, APIConstants.SCANNIN_GREQUEST_CODE);
                                        dialog.dismiss();
                                    }
                                });
                        return;
                    } else {
                        connectDeviceForBT(deviceMacAddress);
                    }
                } else {
                    TipUtil.showAlert(mContext,
                            mContext.getResources().getText(R.string.tip_title).toString(),
                            mContext.getResources().getText(R.string.tip_for_new_scan_connected).toString(),
                            mContext.getResources().getText(R.string.commit_button).toString(),
                            new TipUtil.OnAlertSelected() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(mContext, CaptureActivity.class);
                                    startActivityForResult(intent, APIConstants.SCANNIN_GREQUEST_CODE);
                                    dialog.dismiss();
                                }
                            });
                }
                break;
            case BluetoothUtil.REQUEST_ENABLE_BT:   //打开蓝牙通知
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    //登录
                    getDeviceInfo();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    TipUtil.showAlert(mContext,
                            mContext.getResources().getText(R.string.tip_title).toString(),
                            mContext.getResources().getText(R.string.error_open_bluetooth).toString(),
                            mContext.getResources().getText(R.string.commit_button).toString(),
                            new TipUtil.OnAlertSelected() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(mContext, CaptureActivity.class);
                                    startActivityForResult(intent, APIConstants.SCANNIN_GREQUEST_CODE);
                                    dialog.dismiss();
                                }
                            });
                }
        }
    }

    private void connectDeviceForBT(final String address) {
        if (BluetoothUtil.getInstance().initBluetooth()) {
            BluetoothUtil.getInstance().setBtAddress(address);
            if (!BluetoothUtil.getInstance().isBluetoothEnable()) {
                BluetoothUtil.getInstance().enableBluetooth(this);//发起打开蓝牙

            } else {
                BluetoothUtil.getInstance().setBtName("tuge_debugtool");
                BluetoothUtil.getInstance().startBluetoothService(this);

                try {
                    BluetoothDevice device = BluetoothUtil.getInstance().getBTDevice(address);
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        needCreateBond = false;
                        MZApplication.getInstance().runBackGround(new Runnable() {
                            @Override
                            public void run() {
                                BluetoothUtil.getInstance().connectDevice(address, true);
                            }
                        }, 500);
                        MZApplication.getInstance().runMainThread(hiddenNotice, 10 * 1000);
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
                                MZApplication.getInstance().runMainThread(hiddenNotice, 10 * 1000);
                            }
                        });
                    }
                } catch (Exception e) {
                    TipUtil.showAlert(mContext,
                            mContext.getResources().getText(R.string.tip_title).toString(),
                            e.getMessage(),
                            mContext.getResources().getText(R.string.commit_button).toString(),
                            new TipUtil.OnAlertSelected() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(mContext, CaptureActivity.class);
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

    @Override
    public void onNotice(int NOTICE_TYPE, Object object) {

        switch (NOTICE_TYPE) {
            case OnNoticeUI.NOTICE_TYPE_BLUETOOTH_CONNECTED:
                APIConstants.isBluetoothConnection = true;
                Log.i("xianzouzheli","geniehnoehne");
                getDeviceInfo();
                break;
            case OnNoticeUI.NOTICE_TYPE_BLUETOOTH_CANNOT_CONNECTED:
                if (needCreateBond == true && !TextUtils.isEmpty(deviceMacAddress)) {
                    connectDeviceForBT(deviceMacAddress);
                    needCreateBond = false;
                    APIConstants.isBluetoothConnection =false;

                } else {
                    APIConstants.isBluetoothConnection =false;


                    pb_progress.setVisibility(View.GONE);
                    TipUtil.showAlert(mContext,
                            mContext.getResources().getText(R.string.tip_title).toString(),
                            mContext.getResources().getText(R.string.tip_for_open_scan_connected).toString(),
                            mContext.getResources().getText(R.string.commit_button).toString(),
                            new TipUtil.OnAlertSelected() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(mContext, CaptureActivity.class);
                                    startActivityForResult(intent, APIConstants.SCANNIN_GREQUEST_CODE);
                                    dialog.dismiss();
                                }
                            });
                }
                break;
            case OnNoticeUI.NOTICE_TYPE_BT_DEVICE_INFO:
                if (null == object || object.equals("")) {
                    return;
                }
                try {
                    pb_progress.setVisibility(View.VISIBLE);
                    APIConstants.deviceInfo = new Gson().fromJson(object.toString(), DeviceInfo.class);


                    Log.i("tag","============"+APIConstants.deviceInfo.getSsid());



                    if (APIConstants.deviceInfo != null) {


                        //判断设备是否被激活
                        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
                        params[0] = new OkHttpClientManager.Param("ssid", APIConstants.deviceInfo.getSsid());
                        new SendRequest(APIConstants.Get_Device_Status, params, new MyHandler(), 1);
//                        Intent intent = new Intent(this, MainActivity.class);
//                        startActivity(intent);
//                        finish();

                    } else {
                        Toast.makeText(mContext, R.string.tip_can_not_get_device_info, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case OnNoticeUI.NOTICE_TYPE_BT_PACKAGE_INFO:
                if (null == object || object.equals("")) {
                    return;
                }
                try {
                    APIConstants.devicePackageInfo = new Gson().fromJson(object.toString(), DevicePackageInfo.class);
                    if (APIConstants.devicePackageInfo != null && APIConstants.devicePackageInfo.getStartTime().length() < 13) {
                        APIConstants.devicePackageInfo.setStartTime(APIConstants.devicePackageInfo.getStartTime() + "000");
                    }
                    if (APIConstants.devicePackageInfo != null && APIConstants.devicePackageInfo.getEndTime().length() < 13) {
                        APIConstants.devicePackageInfo.setEndTime(APIConstants.devicePackageInfo.getEndTime() + "000");
                    }

                    if (APIConstants.deviceInfo != null && APIConstants.devicePackageInfo != null) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (Exception e) {
                }
                break;
        }
    }

    public void getDeviceInfo() {
        BluetoothUtil.getInstance().sendMessage(TcpConfig.GET_DEVICE_INFO);
    }

    Runnable hiddenNotice = new Runnable() {
        @Override
        public void run() {
        }
    };

    @Override
    public void onClick(View view) {

    }

    //Http Handler
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.obj == null) {
                return;
            }
            if (msg.what == -10) {
                return;
            }
            HttpResponseData responseData = new HttpResponseData((String) msg.obj);
            if (responseData == null || responseData.status < 0 || responseData.data == null) {
                return;
            }
            pb_progress.setVisibility(View.GONE);
            switch (msg.what) {
                case 1:
                    if (responseData.data.containsKey("device")) {
                        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) responseData.data.get("device");
                        if (data.containsKey("is_active") && data.get("is_active").toString().equals("false")) {
                            final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
                            isExit.setTitle(R.string.tip_title);
                            isExit.setMessage(getResources().getString(R.string.tip_for_active_device));
                            isExit.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isExit.dismiss();
                                    TipUtil.showAlert(mContext,
                                            mContext.getResources().getText(R.string.tip_title).toString(),
                                            mContext.getResources().getText(R.string.tip_for_new_scan_connected).toString(),
                                            mContext.getResources().getText(R.string.commit_button).toString(),
                                            new TipUtil.OnAlertSelected() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    Intent intent = new Intent(mContext, CaptureActivity.class);
                                                    startActivityForResult(intent, APIConstants.SCANNIN_GREQUEST_CODE);
                                                    dialog.dismiss();
                                                }
                                            });
                                }
                            });
                            isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.commit_button), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isExit.dismiss();
                                    OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
                                    params[0] = new OkHttpClientManager.Param("ssid", APIConstants.deviceInfo.getSsid());
                                    new SendRequest(APIConstants.Device_Active, params, new MyHandler(), 2);
                                }
                            });

                            isExit.show();
                        }
                        if (data.containsKey("is_active") && data.get("is_active").toString().equals("true")){
                            UserDataUtils.getInstance(mContext).setMacAddress(APIConstants.deviceInfo.getBluetoothMac());
                            UserDataUtils.getInstance(mContext).setSsid(APIConstants.deviceInfo.getSsid());
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    break;
                case 2:
                    if (responseData.data.containsKey("active") && responseData.data.get("active").toString().equals("true")) {
                        UserDataUtils.getInstance(mContext).setMacAddress(APIConstants.deviceInfo.getBluetoothMac());
                        UserDataUtils.getInstance(mContext).setSsid(APIConstants.deviceInfo.getSsid());

                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        TipUtil.showAlert(mContext,
                                mContext.getResources().getText(R.string.tip_title).toString(),
                                mContext.getResources().getText(R.string.tip_for_active_device_error).toString(),
                                mContext.getResources().getText(R.string.commit_button).toString(),
                                new TipUtil.OnAlertSelected() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
