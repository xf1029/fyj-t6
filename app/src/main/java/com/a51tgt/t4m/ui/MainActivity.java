package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.a51tgt.t4m.bluetooth.BluetoothUtil;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.utils.CommUtil;
import com.a51tgt.t4m.utils.RestartAPPUtil;
import com.a51tgt.t4m.utils.TipUtil;
import com.a51tgt.t4m.utils.WiFiUtil;
import com.znq.zbarcode.CaptureActivity;
import com.a51tgt.t4m.net.TcpUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import  com.a51tgt.t4m.service.MainService;

import org.greenrobot.eventbus.EventBus;


public class MainActivity extends BaseActivity implements OnNoticeUI {

    private Context mContext;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;
    public ViewPager mViewPager;
    private boolean quit = false;
    private int[] pic = new int[]{R.drawable.ic_s_home, R.drawable.ic_s_mall, R.drawable.ic_s_setting};

    private com.a51tgt.t4m.ui.DeviceInfoFragment deviceInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



            Log.i("activityclass",this.toString());
//            MZApplication.getInstance().setOnNoticeUiListener(this, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);



        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
    }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tl_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        if(deviceInfoFragment != null) //针对基本信息修改后的更新
                            deviceInfoFragment.upDateFragment();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        MZApplication.getInstance().addActivity(this);
//        reloadPageAfterChangeLanguage();

    }
    private void reloadPageAfterChangeLanguage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (mContext instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.recreate();
            }

        } else {
//            EventBus.getDefault().post(new AppEvent.ChangeLanguage());
//            ChangeLanguagePage.this.removeAllViews();
//            initView(mContext);
//            checkItem();
        }
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
    @Override
    protected void onResume() {
        super.onResume();

//    reloadPageAfterChangeLanguage();
//        MainActivity AC =  (MainActivity) mContext;
//        AC.recreate();

        MZApplication.getInstance().setOnNoticeUiListener(this, OnNoticeUI.KEY_TYPE_MAIN_ACTIVITY);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MZApplication.getInstance().setOnNoticeUiListener(null, OnNoticeUI.KEY_TYPE_MAIN_ACTIVITY);
        BluetoothUtil.getInstance().stopBluetoothService();
        APIConstants.isBluetoothConnection = false;



        TcpUtil.getInstance().socketTemp=null;
        try {
            if (deviceInfoFragment.timer!=null) {
                deviceInfoFragment.timer.cancel();
            }
        }catch (Exception E){


        }

        BluetoothUtil.getInstance().setBtConnectCallback(null);
    }

    @Override
    public void onBackPressed() {
//        MainService.getInstance().removeHeart();
TcpUtil.getInstance().closeSocket();
        finish();

//        if (!quit) {
//            quit = true;
//            Toast.makeText(this, R.string.toast_back_exit, Toast.LENGTH_SHORT).show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    quit = false;
//                }
//            }, 2000);
//        } else {
//            MZApplication.getInstance().exit();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);





    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
    }
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }


    private void setupTabIcons() {
        View tabHome = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imageviewv= tabHome.findViewById(R.id.iv_icon);
        imageviewv.setImageResource(pic[0]);

        tabHome.setSelected(true);

        TextView textview = tabHome.findViewById(R.id.textTab);
        textview.setText(R.string.tab_home);
        tabLayout.getTabAt(0).setCustomView(tabHome);


        View tabHome1 = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imageviewv1= tabHome1.findViewById(R.id.iv_icon);
//        imageviewv1.setImageResource(R.drawable.ic_s_mall);

        imageviewv1.setImageResource(pic[1]);

        tabHome1.setSelected(true);
        TextView textview1 = tabHome1.findViewById(R.id.textTab);
        textview1.setText(R.string.tab_mall);

        tabLayout.getTabAt(1).setCustomView(tabHome1);

        View tabHome2 = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imageviewv2= tabHome2.findViewById(R.id.iv_icon);
//        imageviewv2.setImageResource(R.drawable.ic_s_setting);
        imageviewv2.setImageResource(pic[2]);
        tabLayout.getTabAt(2).setCustomView(tabHome2);
        TextView textview2 = tabHome2.findViewById(R.id.textTab);
        textview2.setText(R.string.tab_setting);
    }


    @Override
    public void onNotice(int NOTICE_TYPE, Object object) {
        switch (NOTICE_TYPE) {
            case OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD:
                Toast.makeText(MZApplication.getInstance(), object.toString().equals("true") ? R.string.tip_operation_success : R.string.tip_operation_failed, Toast.LENGTH_SHORT).show();

                break;
            case OnNoticeUI.NOTICE_TYPE_SET_APN:
                Toast.makeText(MZApplication.getInstance(), object.toString().equals("true") ? R.string.tip_operation_success : R.string.tip_operation_failed, Toast.LENGTH_SHORT).show();

                break;

        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    deviceInfoFragment = com.a51tgt.t4m.ui.DeviceInfoFragment.newInstance();
                    return deviceInfoFragment;
                case 1:
                    return com.a51tgt.t4m.ui.FlowMallFragment.newInstance();
                case 2:
                    return com.a51tgt.t4m.ui.DeviceSettingFragment.newInstance();
                default:
                    break;
            }
            return com.a51tgt.t4m.ui.DeviceInfoFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
