package com.a51tgt.t4m.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
//import com.google.mgson.Gson;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.bean.DeviceInfo;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.config.TugeMessage;

import com.a51tgt.t4m.net.TcpUtil;
import com.a51tgt.t4m.net.TcpUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.PriorityBlockingQueue;

public class MainService extends Service {

    private final PriorityBlockingQueue<String> mQueue= new PriorityBlockingQueue<String>();
    private static MainService instance;
    private  Thread mThread;
    private boolean mStop = false;
    Handler mTcpHanler;
    private int heartBeatFre = 30;
    private boolean isFtpDownload = true;
    private boolean isNetRecord = false;

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        mTcpHanler = new Handler(mTcpCallback);
    }

    public static MainService getInstance(){
        Log.i("instakce:",instance.toString());
        return instance;
    }

    public void startHeartBeat(){
        startThread();
        mTcpHanler.postDelayed(heartBeat,5000);
    }

    public  void  removeHeart (){

        Log.i("remive", "removeHeart: "+heartBeat);
        mTcpHanler.removeCallbacks(heartBeat);


    }

    public void stopNetRecord(){
        heartBeatFre = 30;
        isNetRecord = false;
    }

    Runnable heartBeat = new Runnable() {
        @Override
        public void run() {
            addRequest(TcpConfig.GET_DEVICE_INFO);
            mTcpHanler.postDelayed(heartBeat, heartBeatFre * 1000);
        }
    };

    private void startThread() {
        if (null == mThread) {
            mThread = new Thread(mAble);
            mThread.start();
        }
    }

    public synchronized void addRequest(String request) {
        if (!mStop) {
            if (!mThread.isAlive()) {
                mThread = null;
                startThread();
            }
            mQueue.add(request);
        }
    }

    /*public void onCmdReceive()
    {
        Thread.State state =  mThread.getState();
        if (!mQueue.isEmpty() && state == Thread.State.WAITING) {
            Log.i("thread is waitting now  notify");
            mThread.notify();
        }
    }*/

    public void setStop() {
        mStop = true;
        mThread = null;
        /*Thread.State state =  mThread.getState();
        if (state == Thread.State.WAITING) {
            Log.i("thread is waitting now  notify");
            mThread.notify();
        }*/
    }

    private void doLoop() {
        String cmd = null;
        while (!mStop) {
            try {
                cmd = mQueue.take();
                if (null != cmd) {
                    TcpUtil.getInstance().sendMessage(cmd, mTcpHanler);

                }
            } catch (Exception e) {
//                e.printStackTrac  e();
            }
        }
    }


    private final Runnable	mAble	= new Runnable() {
        @Override
        public void run() {
            doLoop();
        }
    };


    Handler.Callback mTcpCallback = new Handler.Callback(){
        @Override
        public boolean handleMessage(Message message) {
            if(message == null){
                return true;
            }

            switch (message.what){
                case TugeMessage.GET_MESSAGE:
                    JSONObject resObject = (JSONObject) message.obj;
                    if(resObject!=null){
                        dealwithReceive(resObject);
                    }
                    break;
                default:

                    String send = null;
                    String get = null;
                    JSONObject res = null;
                    try {
                        res = (JSONObject) message.obj;
                        send = res.getString("send");
                        get = res.getString("get");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String msg = "";
                    if (!TextUtils.isEmpty(send)) {
                        msg = send + " ";
                    }
                    msg += "Tcp通讯失败";
                    MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_TCP_FAILED, "通讯失败", OnNoticeUI.KEY_TYPE_DEVICE_INFO_FRAGMENT);
                    TcpUtil.getInstance().socketTemp = null;
                    MainService.getInstance().removeHeart();
                    Log.i("tcptongxun",msg);

                    break;
                /*case TugeMessage.EXCEPTION_ERROR:
                    Log.i("TCP failed error1.",message.obj.toString());
                    break;
                case TugeMessage.EXCEPTION_JSON_ERROR:
                    Log.i("TCP failed error2.",message.obj.toString());
                    break;
                case TugeMessage.EXCEPTION_IO_ERROR:
                    Log.i("TCP failed error3.",message.obj.toString());

                    break;
                case TugeMessage.TIME_OUT:
                    Log.i("TCP failed error4.",message.obj.toString());

                    break;*/
            }
            return true;
        }
    };

    private void dealwithReceive(JSONObject res){
        String send = null;
        String get = null;
        try {
            send = res.getString("send");
            get = res.getString("get");
            Log.i("getcommmandddd",get+send);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(TextUtils.isEmpty(send)||TextUtils.isEmpty(get)){
            return;
        }else{
            if (send.equals(TcpConfig.GET_DEVICE_INFO)) {
                Log.i("deviceinfo1111",get+send);
                DeviceInfo deviceInfo = null;
                try {
                    deviceInfo = new Gson().fromJson(get, DeviceInfo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
//                SettingsData.getInstance().setDeviceInfo(deviceInfo);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_DEVICE_INFO, deviceInfo, OnNoticeUI.KEY_TYPE_DEVICE_INFO_FRAGMENT);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_DEVICE_INFO, deviceInfo, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_APN_QUERY, deviceInfo.getApn2(), OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);

            }    else if(send.contains(TcpConfig.SET_BLACK_LIST)){
                APIConstants.deviceInfo.setBlockSwitch(!APIConstants.deviceInfo.isBlockSwitch());
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_BLACK_LIST, get, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_BLACK_LIST, get, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_BLACK_LIST, get, OnNoticeUI.KEY_TYPE_SAVE_FLOW);

            }
            else if (send.contains(TcpConfig.SET_WIFIANDPASSWORD)){

                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD, get, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD, get, OnNoticeUI.KEY_TYPE_DEVICE_REDUCTION);

            }
            else if (send.contains(TcpConfig.SET_APN)){

                MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_APN, get, OnNoticeUI.KEY_TYPE_MAIN_ACTIVITY);

            }

        }
    }

    private String getRes(boolean res) {
        if (res) {
            return "成功";
        } else {
            return "失败";
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void release(){
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setStop();

    }




}
