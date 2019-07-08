package com.a51tgt.t4m.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.bean.AcessPoint;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pc on 2017/10/23.
 */

public class BluetoothUtil {
    private static BluetoothUtil instance;
    private BluetoothUtil(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        registerReceiver();
    }
    public synchronized static BluetoothUtil getInstance(){
        if(instance == null){
            instance = new BluetoothUtil();
        }
        return instance;
    }

    // Debugging
    private static final String TAG = "BluetoothUtil";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private static final String BT_MSG_TYPE_CURRENT = "currentAp";
    private static final String BT_MSG_TYPE_SAVED = "savedAp";
    private static final String BT_MSG_TYPE_SEARCH = "searchedAp";
    private static final String BT_MSG_TYPE_CONNECT_STATE = "connectState";

    // Intent request codes
    public static final int REQUEST_ENABLE_BT = 0;

    // Name of the connected device
    public String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private com.a51tgt.t4m.bluetooth.BluetoothChatService mChatService = null;

    private TextView connectStatus;

    private BondedCallback bondedCallback;
    private BtConnectCallback connectCallback;
    private boolean registerReceiver = false;

    private String bt_address;

    public void setBtAddress(String address){
        bt_address = address;
    }

    public String getBtAddress(){
        return bt_address;
    }

    // false, Bluetooth is not available;
    public boolean initBluetooth() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return mBluetoothAdapter != null;
    }

    public boolean isBluetoothEnable(){
        if(mBluetoothAdapter != null){
            return mBluetoothAdapter.isEnabled();
        }
        return false;
    }

    public String getSelfBtAddress(){
        if(mBluetoothAdapter != null){
            return mBluetoothAdapter.getAddress();
        }
        return null;
    }

    public void setBtName(String name){
        mBluetoothAdapter.setName(name);
    }

    public void enableBluetooth(Activity context){
        // If BT is not on, request that it be enabled.
        if (mBluetoothAdapter==null) {

//            Toast.makeToast(getContext, "你的设备不支持此功能", 1).show;
            return;


        }

    }

    public void startBluetoothService(Context context){
        if(mChatService == null){
            // Initialize the BluetoothChatService to perform bluetooth connections
            mChatService = new com.a51tgt.t4m.bluetooth.BluetoothChatService(context, mHandler);

        }

        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == com.a51tgt.t4m.bluetooth.BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    public void stopBluetoothService(){
        if (mChatService != null) mChatService.stop();
        unregisterReceiver();
    }

    public BluetoothDevice getBTDevice(String address){
        return mBluetoothAdapter.getRemoteDevice(address);
    }

    public void connectDevice(String address, boolean secure) {
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    public void connectDevice(BluetoothDevice device, boolean secure){
        mChatService.connect(device, secure);
    }

    private void ensureDiscoverable(Context context) {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            context.startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != com.a51tgt.t4m.bluetooth.BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(MZApplication.getInstance(), R.string.tip_not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    public void setShowStatusTextView(TextView tv){
        connectStatus = tv;
    }

    private final void setStatus(int resId) {
        if(connectStatus != null){
            connectStatus.setText(getString(resId));
        }
    }

    private final void setStatus(CharSequence string) {
        if(connectStatus != null){
            connectStatus.setText(string);
        }
    }

    private String getString(int resId) {
        return MZApplication.getInstance().getResources().getString(resId);
    }

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case com.a51tgt.t4m.bluetooth.BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.tip_connected_to)+mConnectedDeviceName);
                            if(connectCallback != null){
                                connectCallback.BtConnect(true);
                            }
                            break;
                        case com.a51tgt.t4m.bluetooth.BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.tip_connecting);
                            break;
                        case com.a51tgt.t4m.bluetooth.BluetoothChatService.STATE_LISTEN:
                        case com.a51tgt.t4m.bluetooth.BluetoothChatService.STATE_NONE:
                            setStatus(R.string.tip_not_connecting);
                            if(connectCallback != null){
                                connectCallback.BtConnect(false);
                            }
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case MESSAGE_READ:
                    String readMessage = (String) msg.obj;
                    //Log.i(readMessage.length()+","+readMessage);
                    dealwithReadMessage(readMessage);
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    //Toast.makeText(MZApplication.getInstance(), "Connected to \n" + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BLUETOOTH_CONNECTED, mConnectedDeviceName, OnNoticeUI.KEY_TYPE_MAIN_FRAGMENT);
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BLUETOOTH_CONNECTED, mConnectedDeviceName, OnNoticeUI.KEY_TYPE_BT_LOGIN_ACTIVITY);
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BLUETOOTH_CONNECTED, mConnectedDeviceName, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BLUETOOTH_CONNECTED, mConnectedDeviceName, OnNoticeUI.KEY_TYPE_DEVICE_INFO_FRAGMENT);
                    break;
                case MESSAGE_TOAST:
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BLUETOOTH_CANNOT_CONNECTED, null, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BLUETOOTH_CANNOT_CONNECTED, null, OnNoticeUI.KEY_TYPE_DEVICE_INFO_FRAGMENT);
                    //Toast.makeText(MZApplication.getInstance(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void dealwithReadMessage(String msg){
        if(msg.contains(TcpConfig.BT_MSG_FROM_DEVICE)){
            JSONObject object = null, value = null;
            JSONArray array = null;
            try{
                object = new JSONObject(msg);
                String va = object.getString(TcpConfig.VALUE);
                value = new JSONObject(va);
                String type = value.getString("type");
                Log.i("wifilist",value.toString());
                if (type.equals(BT_MSG_TYPE_CONNECT_STATE)) {
                    String procedure = value.getString("procedure");
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_CONNECT_PROCEDURE, procedure, OnNoticeUI.KEY_TYPE_WIFI_ACTIVITY);
                } else if (type.equals(BT_MSG_TYPE_CURRENT)) {
                    AcessPoint ap = new AcessPoint(value.getString("ssid"), value.getString("pwd"), value.getInt("rssi"), "unkown");
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_MSG_CURRENT_AP, ap, OnNoticeUI.KEY_TYPE_WIFI_ACTIVITY);
                } else if (type.equals(BT_MSG_TYPE_SAVED)) {
                    array = value.getJSONArray("array");
                    ArrayList<AcessPoint> aps = new ArrayList<AcessPoint>();
                    for(int i = 0; i <array.length(); i++){
                        AcessPoint temp = null;
                        try {
                            temp = new Gson().fromJson(array.get(i).toString(), AcessPoint.class);
                        }catch (JSONException e){
                        }
                        if(temp!=null){


                            aps.add(temp);
                        }
                    }
                    if(aps != null){
                        MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_MSG_SAVED_AP, aps, OnNoticeUI.KEY_TYPE_WIFI_ACTIVITY);




                    }
                } else if (type.equals(BT_MSG_TYPE_SEARCH)) {
                    array = value.getJSONArray("array");
                    ArrayList<AcessPoint> aps = new ArrayList<AcessPoint>();
                    for(int i = 0; i <array.length(); i++){
                        AcessPoint temp = null;
                        try {
                            temp = new Gson().fromJson(array.get(i).toString(), AcessPoint.class);
                        }catch (JSONException e){
                            Log.i("e",e.toString());
                        }
                        if(temp!=null){
                            aps.add(temp);
                        }
                    }
                    if(aps != null){
                        MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_MSG_SEARCH_AP, aps, OnNoticeUI.KEY_TYPE_WIFI_ACTIVITY);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
                Log.i("e",e.toString());
            }
        }else {
            JSONObject jsonObject=null;
            String key="";
            String value="";
            try {
                jsonObject=new JSONObject(msg);
                key=jsonObject.getString(TcpConfig.KEY);
                value=jsonObject.getString(TcpConfig.VALUE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (key.equals(TcpConfig.OPEN_ADB)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "打开USB结果 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.CLOSE_ADB)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "关闭USB结果 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.FOTA_UPDATE_CHECK)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "fota升级包检测 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.SWITCH_DATA)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "切换数据开关 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.SWITCH_FLY_MODE)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "关闭飞行模式开关 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.SET_FULL_SCREEN_TRUE)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "全屏开关 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.CONTROL_SPEED_TRUE)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "限速结果 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.CONTROL_SPEED_FALSE)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "不限速结果 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.RESTART)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "重启APP " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.POWER_OFF)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "设备关机 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.REBOOT)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "重启设备 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.RELEASE_CARD_SIMULATION)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "模拟手动释放 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.SET_DEVICE_LANGUAGE)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_DEVICE_LANG, "设置设备语言 " + value, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
                Log.i("shezhiyuyan","valuedezhi"+value);

            } else if (key.equals(TcpConfig.SET_BLACK_LIST)) {
                Log.i("tagggg","valuedezhi"+value);
                APIConstants.deviceInfo.setBlockSwitch(!APIConstants.deviceInfo.isBlockSwitch());
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_BLACK_LIST, value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_BLACK_LIST, value, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_BLACK_LIST, value, OnNoticeUI.KEY_TYPE_SAVE_FLOW);


            } else if (key.equals(TcpConfig.SET_APN)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_APN, value, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
            } else if (key.equals(TcpConfig.SET_WIFIANDPASSWORD)) { //"设置热点和密码 " +
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD, value, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD, value, OnNoticeUI.KEY_TYPE_DEVICE_REDUCTION);
                requery();



            }else if (key.equals(TcpConfig.BT_OPEN_WIFI)) { //"打开wifi " +
                Log.i("openwifi",value);

            }else if (key.equals(TcpConfig.SET_NETWORK_TYPE)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "首选网络模式 " + value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            } else if (key.equals(TcpConfig.CLOSE_WIFIAP)) {//"关闭设备热点 " +
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_CLOSE_WIFIAP, value, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
            } else if (key.equals(TcpConfig.GET_DEVICE_INFO)) {
                Log.i("openwifigegegeg",value);

                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_DEVICE_INFO, value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_DEVICE_INFO, value, OnNoticeUI.KEY_TYPE_DEVICE_INFO_FRAGMENT);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_DEVICE_INFO, value, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);

            } else if (key.equals(TcpConfig.GET_PACKAGE_INFO)) {
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_PACKAGE_INFO, value, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
            }
            else if (key.equals(TcpConfig.BT_REDUCTION)) {
//                Log.i("chaxunapn",value);


                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_DEVICE_REDUTION, value, OnNoticeUI.KEY_TYPE_DEVICE_REDUCTION);
            }
            else if (key.equals(TcpConfig.BT_APN_QUERY)) {

                Log.i("chaxunapn",value);

                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_APN_QUERY, value, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
            }

            else if(key.equals(TcpConfig.GET_DEVICE_ALL_LOGS)){
                if(value.equals("true")){
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_GET_DEVICE_LOGS_SUCCESS, null, OnNoticeUI.KEY_TYPE_DEVICE_CONTROL_FRAGMENT);
                } else {
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_GET_DEVICE_LOGS_FAIL, null, OnNoticeUI.KEY_TYPE_DEVICE_CONTROL_FRAGMENT);
                }
            }
            /*
            else if(key.equals(TcpConfig.BT_SEND_APK)){
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TPYE_BT_SUCCESS, "发送升级包 " + value, OnNoticeUI.KEY_TYPE_MAIN_BT_ACTIVITY);
            } else if(key.equals(TcpConfig.BT_COPY_FILE)){
                File file = new File(Constant.FILE_BT_ROOT, value);
                if(file != null && file.exists()){
                    ShellUtils.execCommand("cp " + file.getAbsolutePath() + " " + Constant.FILE_DOWNLOAD + value, false);
                    file.delete();
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_GET_DEVICE_LOGS_SUCCESS, Constant.FILE_DOWNLOAD + value, OnNoticeUI.KEY_TYPE_DEVICE_CONTROL_FRAGMENT);
                }
            } else if(key.equals(TcpConfig.BT_DELETE_FILE)){
                File file = new File(Constant.FILE_BT_ROOT, value);
                if(file != null && file.exists()){
                    file.delete();
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_GET_DEVICE_LOGS_FAIL, null, OnNoticeUI.KEY_TYPE_DEVICE_CONTROL_FRAGMENT);
                }
            }*/
        }
    }
    private void  requery(){


        sendMessage(TcpConfig.GET_DEVICE_INFO);

    }

    private void registerReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        //filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);//第三方应用不应该注册该广播
        //filter.setPriority(1000);
        registerReceiver = true;
        MZApplication.getInstance().registerReceiver(mReceiver, filter);
    }

    public void unregisterReceiver(){
        if(registerReceiver){
            registerReceiver = false;
            MZApplication.getInstance().unregisterReceiver(mReceiver);
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()){
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch(blueState){
                        case BluetoothAdapter.STATE_TURNING_ON:
                            break;
                        case BluetoothAdapter.STATE_ON:
                            mBluetoothAdapter.setName("tuge_debugtool");
                            startBluetoothService(MZApplication.getInstance());
                            Toast.makeText(context, R.string.tip_bluetooth_open, Toast.LENGTH_LONG).show();
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            break;
                    }
                    break;
                case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                    int blueState2 = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch(blueState2){
                        case BluetoothAdapter.STATE_CONNECTED:
                            break;
                        case BluetoothAdapter.STATE_DISCONNECTED:
                            break;
                        case BluetoothAdapter.STATE_CONNECTING:
                            break;
                        case BluetoothAdapter.STATE_DISCONNECTING:
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String name = device.getName();
                    int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                    switch (state) {
                        case BluetoothDevice.BOND_NONE:
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            if (bondedCallback != null) {
                                bondedCallback.BondBonded();
                            }
                            if(!TextUtils.isEmpty(bt_address)){
                                connectDevice(bt_address, true);
                            } else {
                            }
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    BluetoothDevice device2 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    BluetoothDevice device3 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    BluetoothDevice device4 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    break;
                case BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED:
                    BluetoothDevice device5 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    break;
                case BluetoothDevice.ACTION_PAIRING_REQUEST:
                    BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    break;
            }
        }
    };

    public void setBondedCallback(BondedCallback callback){
        bondedCallback = callback;
    }

    public void setBtConnectCallback(BtConnectCallback callback){
        connectCallback = callback;
    }

    public interface BondedCallback{
        void BondBonded();
    }

    public interface BtConnectCallback{
        void BtConnect(boolean conn);
    }

//    private void Log.i(Object o){}
}
