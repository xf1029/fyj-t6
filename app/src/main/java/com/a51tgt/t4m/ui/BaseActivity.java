package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.bean.DeviceInfo;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.net.TcpUtil;
import com.a51tgt.t4m.utils.AppLanguageUtils;
import com.a51tgt.t4m.utils.CommUtil;
import com.a51tgt.t4m.utils.RestartAPPUtil;
import com.a51tgt.t4m.utils.TipUtil;
import com.a51tgt.t4m.utils.WiFiUtil;

import java.util.Locale;

//import com.umeng.message.PushAgent;


public class BaseActivity extends AppCompatActivity {

  private WifiManager wifiManager;
  private NetworkInfo networkInfo;
  private Context mContext;
  public static BaseActivity instance = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
//    if (savedInstanceState != null) {
//      savedInstanceState.putParcelable("android:support:fragments", null);
//    }
    super.onCreate(savedInstanceState);
//    PushAgent.getInstance(this).onAppStart();

    mContext = this;
    instance = this;
    wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

    SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
    String name = preferences.getString("lang","");//getSt
   APIConstants.currentLan =  getAppLanguage(this);
    Log.i("language55555", "onCreate: "+APIConstants.currentLan);
    onLanguageChange();
//    changeAppLanguage(name);

  }
  private void onLanguageChange() {
    AppLanguageUtils.changeAppLanguage(this, AppLanguageUtils.getSupportLanguage(getAppLanguage(this)));
  }
  private String getAppLanguage(Context context) {

    SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
    String name = preferences.getString("lang","");//getSt
    APIConstants.currentLan = name;

    Log.i("namenamemem", name);
    if (!TextUtils.isEmpty(name)) {
      Intent intentLan = new Intent();
      intentLan.setAction(APIConstants.BR_LAN_STATUS);
//        intent.putExtra("order_status", true);
      sendBroadcast(intentLan);
      if (name.equals("zh")) {
        return APIConstants.SIMPLIFIED_CHINESE;
//            return APIConstants.ENGLISH;


      } else {

        return APIConstants.ENGLISH;


      }
    }else{


    return   Locale.getDefault().getLanguage();
    }
//    return null;

  }
  public void changeAppLanguage(String name) {
             // 本地语言设置
            Locale myLocale = new Locale(name);
             Resources res = getResources();
    Log.i("tetetet", "changeAppLanguage: "+res);
            DisplayMetrics dm = res.getDisplayMetrics();
          Configuration conf = res.getConfiguration();
             conf.locale = myLocale;
//        conf.setLocales(new LocaleList(locale));
           res.updateConfiguration(conf, dm);
        }
  @Override
  protected void onSaveInstanceState(Bundle outState)
  {
//    super.onSaveInstanceState(outState);
  }
//  public void changeAppLanguage() {
//      // 本地语言设置
//      Locale myLocale = new Locale("en");
//      Resources res = getResources();
//      DisplayMetrics dm = res.getDisplayMetrics();
//      Configuration conf = res.getConfiguration();
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//      conf.setLocale(myLocale);
//    } else {
//      conf.locale = myLocale;
//    }      res.updateConfiguration(conf, dm);
//
//
//  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState)
  {
    super.onRestoreInstanceState(savedInstanceState);
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(AppLanguageUtils.attachBaseContext(newBase, APIConstants.currentLan));
  }

  @Override
  protected void onResume() {
    super.onResume();

    // 监控wifi是否连接
//    IntentFilter ins = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//    registerReceiver(netConnReceiver, ins);
//    if (!wifiManager.isWifiEnabled() || !CommUtil.isTGTWiFi(getApplicationContext())) {
//      TipUtil.showAlert(mContext,
//              mContext.getResources().getText(R.string.tip_title).toString(),
//              mContext.getResources().getText(R.string.cannot_find_device).toString(),
//              mContext.getResources().getText(R.string.connect_wifi_button).toString(),
//              new TipUtil.OnAlertSelected(){
//                @Override
//                public void onClick(DialogInterface dialog, int whichButton) {
//                  CommUtil.startWifiSettings(mContext);
//                  dialog.dismiss();
//                }
//              });
//    }
//    else {
//      TcpUtil.getInstance().sendMessage(TcpConfig.GET_DEVICE_INFO, new MyHandler(), 1);
//    }
  }


  private boolean checknet() {
    ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
    // 获取代表联网状态的NetWorkInfo对象
    networkInfo = connManager.getActiveNetworkInfo();
    if (null != networkInfo) {
      return networkInfo.isAvailable();
    }
    return false;
  }

  @SuppressLint("HandlerLeak")
  class MyHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
      if(msg.what == -10){

        return;
      }
      String response = (String) msg.obj;
      if (TextUtils.isEmpty(response)) {
        return;
      }
      switch (msg.what){
        case 1:

          break;
        default:
          break;
      }
    }
  }

}
