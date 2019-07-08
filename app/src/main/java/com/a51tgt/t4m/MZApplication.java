package com.a51tgt.t4m;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.utils.AppLanguageUtils;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MZApplication extends Application {

    private Handler mBackThreadHandler, mFrontHandler;
    private OnNoticeUI proxy;
    private static Logger logger = null;

    private Map mOnticeUI = new HashMap();
    private static MZApplication instance;
    private List<Activity> activityList = new LinkedList<Activity>();
    private IWXAPI iwxApi;

    public static MZApplication getInstance() {
        if (null == instance) {
            instance = new MZApplication();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : activityList) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void exit() {
        finishActivity();
        System.exit(0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
     mContext = getApplicationContext();
        ;
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        String name = preferences.getString("lang","");//getSt
        APIConstants.currentLan = name;
        check2StartThread();
//    CrashReport.initCrashReport(getApplicationContext(), APIConstants.TENCENT_APPID, BuildConfig.DEBUG);
        initPicasso();
//    initPushNotification();
        iwxApi = WXAPIFactory.createWXAPI(MZApplication.getInstance(), null);
        boolean res = iwxApi.registerApp("wxfa9d6ecb81f41530");
      AppLanguageUtils.changeAppLanguage(mContext, APIConstants.currentLan);
        AppLanguageUtils.changeAppLanguage(this, APIConstants.currentLan);

//onLanguageChange();

//            onLanguageChange();
//        AppLanguageUtils.changeAppLanguage(MZApplication.getContext(), "en");

//       mContext =  AppLanguageUtils.attachBaseContext(MZApplication.getContext(),name);



//        String languageToLoad  = name;
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = getResources().getConfiguration();
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        if (name.equals("en")) {
//            config.locale = Locale.ENGLISH;
//        }else{
//
//            config.setLocale(Locale.SIMPLIFIED_CHINESE); ;
//
//        }
//        getResources().updateConfiguration(config, metrics);
//mContext.createConfigurationContext(config);

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
    private void initPicasso() {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(BuildConfig.DEBUG);
        built.setLoggingEnabled(BuildConfig.DEBUG);
        Picasso.setSingletonInstance(built);
    }

    public IWXAPI getWxApi(){
        return iwxApi;
    }

    /*******************************提供利用后台线程执行任务或方法的全局统一接口**************************************/
    public void runMainThread(Runnable r, int delay) {
        if (mFrontHandler == null) {
            mFrontHandler = new Handler();
        }
        mFrontHandler.postDelayed(r, (long) delay);
    }
    public static Context mContext;

    public static Context getContext(){
        return mContext;
    }
    public void removeMainThreadRunnable(Runnable r) {
        if (mFrontHandler != null) {
            mFrontHandler.removeCallbacks(r);
        }
    }

    public void setOnNoticeUiListener(OnNoticeUI o, String type) {
        mOnticeUI.put(type, o);
    }

    public void sendUiNotice(int what, Object object, String type) {
        proxy = (OnNoticeUI) mOnticeUI.get(type);
        if (proxy != null) {
            proxy.onNotice(what, object);
        }
        proxy = null;
    }

    public void runBackGround(Runnable r, int delay) {
        if (null != this.mBackThreadHandler) {
            this.check2StartThread();
            this.mBackThreadHandler.postDelayed(r, (long) delay);
        }
    }
    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(AppLanguageUtils.attachBaseContext(base, getAppLanguage(base)));

       super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void check2StartThread() {
        if(null == this.mBackThreadHandler || !this.mBackThreadHandler.getLooper().getThread().isAlive()) {
            if(null != this.mBackThreadHandler) {
                this.mBackThreadHandler.removeCallbacksAndMessages((Object)null);
                this.mBackThreadHandler.getLooper().quit();
                this.mBackThreadHandler = null;
            }

            HandlerThread thread = new HandlerThread("Global-Background-Handler");
            thread.setDaemon(true);
            thread.start();
            this.mBackThreadHandler = new Handler(thread.getLooper());
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        onLanguageChange();

    }
    private void onLanguageChange() {
        AppLanguageUtils.changeAppLanguage(this, AppLanguageUtils.getSupportLanguage(getAppLanguage(this)));
    }
    private String getAppLanguage(Context context) {


        String name = APIConstants.currentLan;
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
        }
        return null;

    }
    public void removeRunnable(Runnable r) {
        if(null != this.mBackThreadHandler) {
            this.mBackThreadHandler.removeCallbacks(r);
        }
    }
//    public static void Log.i(final Object msg) {
//        if (msg == null || msg.toString().isEmpty()) {
//            return;
//        }
//        String appMsg = getDateToStringStyle("MM-dd HH:mm:ss,SSS", new Date()) + ":" + msg.toString();
//        if (logger == null) {
//            logger = Logger.getLogger(TAG);
//        }
//        logger.warn(appMsg);
//    }
//    public void warn(Object message) {
//        if(!this.repository.isDisabled(30000)) {
//            if(Level.WARN.isGreaterOrEqual(this.getEffectiveLevel())) {
//                this.forcedLog(FQCN, Level.WARN, message, (Throwable)null);
//            }
//
//        }
//    }

    protected void quit() {
        if(null != this.mBackThreadHandler) {
            this.mBackThreadHandler.getLooper().quit();
            this.mBackThreadHandler = null;
        }
    }

    public void executors(Handler handler, final Object instance, final String name, int delayMillis) {
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    Method e = instance.getClass().getDeclaredMethod(name, new Class[0]);
                    e.invoke(instance, new Object[0]);
                } catch (Exception var3) {
                }
            }
        }, (long) delayMillis);
    }

}
