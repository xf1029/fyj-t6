package com.a51tgt.t4m.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.net.TcpUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @创建者 Aries_Hoo
 * @作用 常用工具类
 * @创建时间 2014年10月31日下午3:21:41
 */
@SuppressLint({"NewApi", "SimpleDateFormat"})
public class CommUtil {

    /**
     * @param void
     * @param view
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 文本组件添加下划线
     * @创建时间 2014年11月28日上午10:44:50
     */
    public static void addUnderLine(View view) {
        if (view instanceof TextView) {
            ((TextView) view).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        } else if (view instanceof EditText) {
            ((EditText) view).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        } else if (view instanceof Button) {
            ((Button) view).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        }

    }

    /**
     * @param byte[]
     * @param bmp
     * @param needRecycle
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 将Bitmap转换成Byte
     * @创建时间 2014年11月27日上午9:43:33
     */
    public static byte[] bitmapToByteArray(final Bitmap bmp,
                                           final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param context
     * @param num
     * @返回值 void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 直接拨打电话
     * @创建时间 2015年1月6日下午3:27:06
     */
    public static void callPhone(Context context, String num) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + num));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void copyFile(String oldPath, String newPath,
                                boolean isDeleteOldFile) {
        try {
            // int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                // int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    // bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
                if (isDeleteOldFile) {
                    oldfile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param String
     * @param str
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 清空字符串里所有的空格并返回字符
     * @创建时间 2014年11月28日上午9:54:38
     */
    public static String clearBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * @param String
     * @param str
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 清除null或"null"
     * @创建时间 2014年11月26日上午11:16:18
     */
    public static String clearNull(String str) {
        if (str == null || str.toLowerCase(Locale.getDefault()).equals("null")) {
            return "";
        }
        return str;
    }

    /**
     * @param context
     * @return
     * @返回值 boolean
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 检测应用是否前台
     * @创建时间 2015年1月21日上午10:07:41
     */
    public static boolean checkAppOnForeground(Context context) {
        if (context == null) {
            return false;
        }
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return
     * @所属类 BaseActivity.java
     * @创建者 Aries_Hoo
     * @作用 判断网络连接
     * @创建时间 2014年11月23日上午10:23:11
     */
    public static boolean checkNetwork(Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            return (info != null && info.isConnected()) || info.isRoaming();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param context
     * @param permissionName
     * @return
     * @返回值 Boolean
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 检测应用某权限是否开启
     * @创建时间 2015年5月6日下午3:34:04
     */
    public static Boolean checkPermission(Context context, String permissionName) {
        PackageManager pm = context.getPackageManager();
        return (PackageManager.PERMISSION_GRANTED == pm.checkPermission(
                permissionName, context.getPackageName()));

    }

    /**
     * @param Boolean
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用检测是否存在sdcard
     * @创建时间 2014年11月11日上午10:01:28
     */
    public static Boolean checkSdCardExist() {
        return Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * @param void
     * @param context
     * @param num
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 跳转拨号界面
     * @创建时间 2014年11月20日下午12:05:39
     */
    public static void dialPhone(Context context, String num) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + num));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getDateState(int state) {
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

    /**
     * @param formatString
     * @param dates
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取当前时间(按一定格式)
     * @创建时间 2014年12月15日下午3:58:56
     */
    public static String getDateToStringStyle(String formatString, long dates) {
        Date date = new Date(System.currentTimeMillis());
        if (date != null) {
            date = new Date(dates);
        }
        return getDateToStringStyle(formatString, date);
    }

    /**
     * @param formatString
     * @param dates
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取当前时间(按一定格式)
     * @创建时间 2014年12月15日下午3:58:56
     */
    public static String getDateToStringStyle(String formatString, Date dates) {
        if (formatString == null || formatString.isEmpty()) {
            formatString = "MM-dd HH:mm:ss";
        }
        DateFormat dateFormatter = new SimpleDateFormat(formatString);
        Date date = new Date(System.currentTimeMillis());
        if (date != null) {
            date = dates;
        }
        return dateFormatter.format(date);
    }

    /**
     * @param formatString
     * @param dates
     * @return 返回输入格式的系统时间 2014-12-11 17:00:06
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取当前时间(按一定格式)
     * @创建时间 2014年12月11日下午4:59:42
     */
    public static String getDateToStringStyle(String formatString, String dates) {
        long time = 0;
        try {
            time = Long.parseLong(dates);
        } catch (Exception e) {
        }
        return getDateToStringStyle(formatString, new Date(time));
    }

    public static String getDateNow(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    /**
     * @param value 传入double类型数据
     * @param num   保留小数位
     * @return String
     * @返回值 String
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 结果值保留小数位
     * @创建时间 2015年3月27日上午10:14:38
     */
    public static String getDoubleFomat(double value, int num) {
        String pattern = "###";
        if (num > 0) {
            pattern += ".";
            for (int i = 0; i < num; i++) {
                pattern += "0";
            }
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(value).startsWith(".") ? "0" + df.format(value) : df
                .format(value);
    }

    public static String getDoubleFomat(int value, int num) {
        return getDoubleFomat((double) value, num);
    }



    /**
     * @param String
     * @param context
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取系统当前语言
     * @创建时间 2014年11月20日下午12:06:09
     */
    public static String getLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        Log.i("getLanguage", locale.getLanguage());
        return locale.getLanguage();
    }

    /**
     * @param String
     * @param context
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用获 取本机手机号码 (如果硬件提供接口--现在大多不提供)
     * @创建时间 2014年11月20日下午12:47:31
     */
    public static String getNativePhoneNumber(Context context) {
        String NativePhoneNumber = "";
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        NativePhoneNumber = telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * @param str1
     * @param strAll
     * @return
     * @返回值 int
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 计算百分比数值
     * @创建时间 2015年7月1日上午11:22:09
     */
    public static int getPercnet(String str1, String strAll) {
        int number = 100;
        int i1 = 0;
        int i2 = 1;
        if (str1.indexOf(".") != -1) {
            str1 = str1.substring(0, str1.indexOf("."));
        }
        i1 = Integer.parseInt(str1);
        if (strAll.indexOf(".") != -1) {
            strAll = strAll.substring(0, strAll.indexOf("."));
        }
        i2 = Integer.parseInt(strAll);
        if (i1 == 0 || i2 == 0) {
            number = 0;
        }
        number = i1 * 100 / i2 > 100 ? 100 : i1 * 100 / i2;
        Log.i("number", number + "--" + i1 * 100 / i2 + "");
        return number;
    }

    /**
     * @param int
     * @param len 长度(多少位)
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取随机整数
     * @创建时间 2014年11月25日下午5:19:51
     */
    public static int getRandomNumber(int len) {
        return (int) (Math.random() * Math.pow(10, len));
    }

    /**
     * @返回值 int
     * @传入参数 @param max 最大值
     * @传入参数 @param min 最小值
     * @传入参数 @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取min到max之间的随机数
     * @创建时间 下午1:47:35
     */
    public static int getRandomNumber(int max, int min) {
        return (int) (1 + Math.random() * (max - min + 1));
    }

    public static String getSimState(int state) {
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

    /**
     * @param context
     * @return >0 成功,<0 失败
     * @返回值 int
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取系统状态栏高度
     * @创建时间 2015年6月17日上午10:32:24
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     * @返回值 int
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取时间差
     * @创建时间 2015年4月17日下午5:10:50
     */
    public static int getTimeCount(Date startDate, Date endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            Log.i("c1", df.format(startDate) + "");
            Log.i("c2", df.format(endDate) + "");
            c1.setTime(df.parse(df.format(startDate)));
            c2.setTime(df.parse(df.format(endDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int result = c1.compareTo(c2);
        Log.i("result", result + "");
        if (result > 0) {
            return 0;
        }
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        Log.i("getGapCount", (toCalendar.getTime().getTime() - fromCalendar
                .getTime().getTime()) / (1000 * 60 * 60 * 24) + "");
        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime()
                .getTime()) / (1000 * 60 * 60 * 24));
    }

    public static long getTimeInMillis() {
        TimeZone zone = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(zone);
        Log.i("getTimeInMillis",
                "cal.getTimeInMillis()==" + cal.getTimeInMillis()
                        + "System.currentTimeMillis()=="
                        + System.currentTimeMillis());
        return cal.getTimeInMillis();
    }

    /**
     * @param int
     * @param context
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取当前App版本号
     * @创建时间 2014年11月20日上午11:26:28
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            return packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param int
     * @param context
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取缓存中的版本号(用于判断是否出现引导界面)
     * @创建时间 2014年11月20日下午12:48:50
     */
    public static int getVersionCodeShare(Context context) {
        SharedPreferences share = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        return share.getInt("versionCode", 0);
    }

    /**
     * @param String
     * @param context
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取当前App的版本名
     * @创建时间 2014年11月20日上午11:25:57
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return "";
    }

    /**
     * @param context
     * @param view
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 隐藏软键盘
     * @创建时间 2014年10月31日下午3:23:18
     */
    public static void hideInputSoftKey(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * @param context
     * @param path
     * @返回值 void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 安装app
     * @创建时间 2015年6月25日上午9:35:26
     */
    public static void installApp(Context context, String path) {
        installApp(context, new File(path));
    }

    /**
     * @param context
     * @param file
     * @返回值 void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 安装app
     * @创建时间 2015年6月25日上午9:35:15
     */
    public static void installApp(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param title
     * @返回值 void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 跳转Android应用市场(一般用于评价app)
     * @创建时间 2015年1月27日上午8:54:28
     */
    public static void jumpAppMarket(Context context, CharSequence title) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * @param context
     * @param title
     * @返回值 void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 跳转Android应用市场
     * @创建时间 2015年7月1日上午10:34:00
     */
    public static void jumpAppMarket(Context context, int title) {
        jumpAppMarket(context, context.getText(title));
    }

    /**
     * @param boolean
     * @param email
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 验证邮箱格式
     * @创建时间 2014年11月27日上午9:55:21
     */
    public static boolean matchEmail(String email) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * @param boolean
     * @param expression
     * @param text
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 正则表达式
     * @创建时间 2014年11月20日下午12:50:02
     */
    public static boolean matcherText(String expression, String text) {
        Pattern p = Pattern.compile(expression); // 正则表达式
        Matcher m = p.matcher(text); // 操作的字符串
        return m.matches();
    }

    public static String getNumberAndCharacter(String text) {
        Pattern p = Pattern.compile("[A-Za-z0-9]+"); // 正则表达式
        Matcher m = p.matcher(text); // 操作的字符串
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }

    /**
     * @param boolean
     * @param phone
     * @return
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 手机号码验证
     * @创建时间 2014年11月20日下午12:48:15
     */
    public static boolean matchMoblie(String phone) {
        int l = phone.length();
        boolean rs = false;
        switch (l) {
            case 7:
                if (matcherText("^(13[0-9]|15[0-9]|18[7|8|9|6|5])\\d{4}$", phone)) {
                    rs = true;
                }
                break;
            case 11:
                if (matcherText("^(13[0-9]|15[0-9]|18[0-9]|17[0|6|7|8])\\d{4,8}$",
                        phone)) {
                    rs = true;
                }
                break;
            default:
                rs = false;
                break;
        }
        return rs;
    }

    /**
     * @param void
     * @param mContext
     * @param view
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 打开软键盘
     * @创建时间 2014年11月27日下午2:41:39
     */
    public static void openInputSoftKey(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * @param void
     * @param context
     * @param url
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 调用系统浏览器打开url
     * @创建时间 2014年11月26日下午4:43:06
     */
    public static void openWebUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param packageName 为空或者null则打开自己
     * @返回值 void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 根据app包名打开应用
     * @创建时间 2015年7月1日上午10:37:58
     */
    public static boolean runAppByPackageName(Context context,
                                              String packageName) {
        PackageInfo pi;
        if (packageName == null || packageName.isEmpty()) {
            packageName = context.getPackageName();
        }
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = context.getPackageManager();
            @SuppressWarnings("rawtypes")
            List apps = pManager.queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = (ResolveInfo) apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                return true;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param void
     * @param context
     * @param num
     * @param message
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 调用系统发送短信界面(带短信内容)
     * @创建时间 2014年11月20日下午12:06:00
     */
    public static void sendMessage(Context context, String num, String message) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + num));
        sendIntent.putExtra("sms_body", message);
        context.startActivity(sendIntent);
    }

    /**
     * @param destinationAddress
     * @param scAddress
     * @param text
     * @param sentIntent
     * @param deliveryIntent
     * @返回值 void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 直接发送短信 例: sendMessage(address,null,message,null,null);
     * @创建时间 2015年7月1日上午11:00:09
     */
    public static void sendMessage(String destinationAddress, String scAddress,
                                   String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        SmsManager smsMgr = SmsManager.getDefault();
        smsMgr.sendTextMessage(destinationAddress, scAddress, text, sentIntent,
                deliveryIntent);
    }

    /**
     * @param void
     * @param editText
     * @param len
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 设置文本最大输入字符
     * @创建时间 2014年11月28日下午2:19:11
     */
    public static void setEditLength(EditText editText, int len) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                len)});
    }

    /**
     * @param void
     * @param context
     * @param versionCode
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 设置缓存中的版本号(用于判断是否出现引导界面)
     * @创建时间 2014年11月20日下午12:49:39
     */
    public static void setVersionCodeShare(Context context, int versionCode) {
        Editor editor = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE).edit();
        editor.putInt("versionCode", versionCode);
        editor.commit();
    }

    /**
     * @param context
     * @param activity
     * @param bundle
     * @返回值 void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 跳转Activity
     * @创建时间 2015年6月25日上午9:37:55
     */
    public static void startActivity(Context context,
                                     Class<? extends Activity> activity, Bundle bundle) {
        Activity p = ((Activity) context).getParent();
        if (p == null) {
            p = (Activity) context;
        }
        Intent intent = new Intent(p, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        p.startActivity(intent);
    }

    ;

    /**
     * @param context
     * @param activity
     * @param bundle
     * @返回值 void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 跳转Activity并接收返回结果
     * @创建时间 2015年6月25日上午9:37:55
     */
    public static void startActivityForResult(Activity content,
                                              Class<? extends Activity> activity, int requestCode, Bundle bundle) {
        Activity p = content.getParent();
        if (p == null) {
            p = content;
        }
        Intent intent = new Intent(p, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        p.startActivityForResult(intent, requestCode);
    }

    ;

    public static void startWifiSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_WIFI_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean isTGTWiFi(Context context) {
        String ip = getIp(context);
        Log.i("ip", ip);
        String ipString = getIP2(context);
        try {
            if ((!TextUtils.isEmpty(ip) && ip.startsWith("192.168.43"))
                    || (!TextUtils.isEmpty(ip) && ip.startsWith("192.168.8"))// 192.168.8.1 也需要兼容
                    || (!TextUtils.isEmpty(ip) && ip.startsWith("192.168.1"))) {//T3设备
                if (ip.startsWith("192.168.43")) {
                    TcpConfig.hostIP = "192.168.43.1";
                    //TcpConfig.setHostIP("192.168.43.1");
                } else if (ip.startsWith("192.168.8")) {
                    TcpConfig.hostIP = "192.168.8.1";
                } else {
                    TcpConfig.hostIP = "192.168.1.1";
                }
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static String getIp(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int i = wifiInfo.getIpAddress();
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    public static String getIP2(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ipString = "";// 本机在WIFI状态下路由分配给的IP地址
        // 获得IP地址的方法二（反射的方法）：
        try {
            Field field = info.getClass().getDeclaredField("mIpAddress");
            field.setAccessible(true);
            ipString = (String) field.get(info);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ipString;
    }

    public static String getCurTime() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
        return dateFormat.format(now);
    }

    //从信号强度获得对应的图片
    public static int getSignalImage(int rssi) {
        //1.>-85 2.-95<X<-85 3.-105<X<-95  4.-115<X<-105  5. X<-115

        if (rssi <= -115) {
            return R.mipmap.wifi_signal_1;
        } else if (rssi <= -105) {
            return R.mipmap.wifi_signal_2;
        } else if (rssi <= -95) {
            return R.mipmap.wifi_signal_3;
        } else if (rssi <= -85) {
            return R.mipmap.wifi_signal_4;
        } else {
            return R.mipmap.wifi_signal_5;
        }

//        if (rssi <= -100) {
//            return R.mipmap.wifi_signal_1;// 不大于-100
//        } else if (rssi <= -80) {
//            return R.mipmap.wifi_signal_2;// 大于-100，不大于-70
//        } else if (rssi <= -70) {
//            return R.mipmap.wifi_signal_3;// 大于-70，不大于-50
//        } else if (rssi <= -50) {
//            return R.mipmap.wifi_signal_4;// 大于-70，不大于-50
//        } else {
//            return R.mipmap.wifi_signal_5;// 大于-50
//        }
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    public static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim();//.toLowerCase();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public static void setStatusBarBackgroundColor(Activity activity){
        Window window = activity.getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //设置状态栏颜色
//        window.setStatusBarColor(Color.argb(0, 0x10, 0xB5, 0xFF));
        setStatusBarBackgroundColor(activity, "00734f98");
    }

    private static void setStatusBarBackgroundColor(Activity activity, final String colorPref) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (colorPref != null && !colorPref.isEmpty()) {
                final Window window = activity.getWindow();
                // Method and constants not available on all SDKs but we want to be able to compile this code with any SDK
                //SDK19:WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.clearFlags(0x04000000);      //SDK21:WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.addFlags(0x80000000);
                try {
                    // Using reflection makes sure any 5.0+ device will work without having to compile with SDK level 21
                    window.getClass().getDeclaredMethod("setStatusBarColor", int.class).invoke(window, Color.parseColor(colorPref));
                } catch (IllegalArgumentException ignore) {
                } catch (Exception ignore) {
                    // this should not happen, only in case Android removes this method in a version > 21
                }
            }
        }
    }

    /*********************
     * Arabic, Egypt (ar_EG) -----------------------------阿拉伯语，埃及
     Arabic, Israel (ar_IL) -------------------------------阿拉伯语，以色列
     Bulgarian, Bulgaria (bg_BG) ---------------------保加利亚语，保加利亚
     Catalan, Spain (ca_ES) ---------------------------加泰隆语，西班牙
     Czech, Czech Republic (cs_CZ) -----------------捷克语，捷克共和国
     Danish, Denmark(da_DK) ------------------------丹麦语，丹麦
     German, Austria (de_AT) -------------------------德语，奥地利
     German, Switzerland (de_CH) -------------------德语，瑞士
     German, Germany (de_DE) ----------------------德语，德国
     German, Liechtenstein (de_LI) ------------------德语，列支敦士登的
     Greek, Greece (el_GR) ----------------------------希腊语，希腊
     English, Australia (en_AU) -------------------------英语，澳大利亚
     English, Canada (en_CA) --------------------------英语，加拿大
     English, Britain (en_GB) ----------------------------英语，英国
     English, Ireland (en_IE) -----------------------------英语，爱尔兰
     English, India (en_IN) --------------------------------英语，印度
     English, New Zealand (en_NZ) ---------------------英语，新西兰
     English, Singapore(en_SG) --------------------------英语，新加坡
     English, US (en_US) -----------------------------------英语，美国
     English, Zimbabwe (en_ZA) --------------------------英语，津巴布韦
     Spanish (es_ES) ----------------------------------------西班牙
     Spanish, US (es_US) -----------------------------------西班牙语，美国
     Finnish, Finland (fi_FI) ---------------------------------芬兰语，芬兰
     French, Belgium (fr_BE) -------------------------------法语，比利时
     French, Canada (fr_CA) -------------------------------法语，加拿大
     French, Switzerland (fr_CH) --------------------------法语，瑞士
     French, France (fr_FR) --------------------------------法语，法国
     Hebrew, Israel (he_IL) ---------------------------------希伯来语，以色列
     Hindi, India (hi_IN) -------------------------------------印地语，印度
     Croatian, Croatia (hr_HR) ----------------------------克罗地亚语，克罗地亚
     Hungarian, Hungary (hu_HU) ------------------------匈牙利语，匈牙利
     Indonesian, Indonesia (id_ID) ------------------------印尼语，印尼
     Italian, Switzerland (it_CH) ----------------------------意大利语，瑞士
     Italian, Italy (it_IT) ---------------------------------------意大利语，意大利
     Japanese (ja_JP) ----------------------------------------日语
     Korean (ko_KR) ------------------------------------------朝鲜语
     Lithuanian, Lithuania (lt_LT) --------------------------立陶宛语，立陶宛
     Latvian, Latvia (lv_LV) ---------------------------------拉托维亚语，拉托维亚
     Norwegian-Bokmol, Norway(nb_NO) ---------------挪威语，挪威
     Dutch, Belgium (nl_BE) --------------------------------荷兰语，比利时
     Dutch, Netherlands (nl_NL) ---------------------------荷兰语，荷兰
     Polish (pl_PL) -------------------------------------------波兰
     Portuguese, Brazil (pt_BR) ---------------------------葡萄牙语，巴西
     Portuguese, Portugal (pt_PT) ------------------------葡萄牙语，葡萄牙
     Romanian, Romania (ro_RO) ------------------------罗马尼亚语，罗马尼亚
     Russian (ru_RU) ----------------------------------------俄语
     Slovak, Slovakia (sk_SK) ------------------------------斯洛伐克语，斯洛伐克
     Slovenian, Slovenia (sl_SI) ---------------------------斯洛文尼亚语，斯洛文尼亚
     Serbian (sr_RS) ----------------------------------------塞尔维亚语
     Swedish, Sweden (sv_SE) ----------------------------瑞典语，瑞典
     Thai, Thailand (th_TH) --------------------------------泰语，泰国
     Tagalog, Philippines (tl_PH) --------------------------菲律宾语，菲律宾
     Turkish, Turkey (tr_TR) -------------------------------土耳其语，土耳其
     Ukrainian, Ukraine (uk_UA) --------------------------联合王国
     Vietnamese, Vietnam (vi_VN) -----------------------越南语，越南
     Chinese, PRC (zh_CN)--------------------------------中文，中国
     Chinese, Taiwan (zh_TW)-----------------------------中文，台湾

     */
    public static String getCurLanguageForFlowMall() {
        String country = Locale.getDefault().getCountry();
        switch (country) {
            case "TW":
                return "TWD";
            case "CN":
                return "RMB";
            case "JP":
                return "JPY";
            case "US":
                return "USD";
            default:
                return "RMB";

        }
    }
}
