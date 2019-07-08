package com.a51tgt.t4m.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liu_w on 2017/12/14.
 */

public class Util {
    /**
     * @param void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 文本组件添加下划线
     * @创建时间 2014年11月28日上午10:44:50
     * @param view
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 将Bitmap转换成Byte
     * @创建时间 2014年11月27日上午9:43:33
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bitmapToByteArray(final Bitmap bmp,
                                           final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 清空字符串里所有的空格并返回字符
     * @创建时间 2014年11月28日上午9:54:38
     * @param str
     * @return
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 清除null或"null"
     * @创建时间 2014年11月26日上午11:16:18
     * @param str
     * @return
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
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用检测是否存在sdcard
     * @创建时间 2014年11月11日上午10:01:28
     * @return
     */
    public static Boolean checkSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * @param void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 跳转拨号界面
     * @创建时间 2014年11月20日下午12:05:39
     * @param context
     * @param num
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取当前时间(按一定格式)
     * @创建时间 2014年12月15日下午3:58:56
     * @param formatString
     * @param dates
     * @return
     */
    public static String getDateToStringStyle(String formatString, long dates) {
        Date date = new Date(System.currentTimeMillis());
        if (date != null) {
            date = new Date(dates);
        }
        return getDateToStringStyle(formatString, date);
    }

    /**
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取当前时间(按一定格式)
     * @创建时间 2014年12月15日下午3:58:56
     * @param formatString
     * @param dates
     * @return
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取当前时间(按一定格式)
     * @创建时间 2014年12月11日下午4:59:42
     * @param formatString
     * @param dates
     * @return 返回输入格式的系统时间 2014-12-11 17:00:06
     */
    public static String getDateToStringStyle(String formatString, String dates) {
        long time = 0;
        try {
            time = Long.parseLong(dates);
        } catch (Exception e) {
        }
        return getDateToStringStyle(formatString, new Date(time));
    }

    /**
     * @param value
     *            传入double类型数据
     * @param num
     *            保留小数位
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取系统当前语言
     * @创建时间 2014年11月20日下午12:06:09
     * @param context
     * @return
     */
    public static String getLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        Log.i("getLanguage", locale.getLanguage());
        return locale.getLanguage();
    }

    /**
     * @param String
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用获 取本机手机号码 (如果硬件提供接口--现在大多不提供)
     * @创建时间 2014年11月20日下午12:47:31
     * @param context
     * @return
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取随机整数
     * @创建时间 2014年11月25日下午5:19:51
     * @param len
     *            长度(多少位)
     * @return
     */
    public static int getRandomNumber(int len) {
        return (int) (Math.random() * Math.pow(10, len));
    }

    /**
     *
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取当前App版本号
     * @创建时间 2014年11月20日上午11:26:28
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param int
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取缓存中的版本号(用于判断是否出现引导界面)
     * @创建时间 2014年11月20日下午12:48:50
     * @param context
     * @return
     */
    public static int getVersionCodeShare(Context context) {
        SharedPreferences share = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        return share.getInt("versionCode", 0);
    }

    /**
     * @param String
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 获取当前App的版本名
     * @创建时间 2014年11月20日上午11:25:57
     * @param context
     * @return
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 验证邮箱格式
     * @创建时间 2014年11月27日上午9:55:21
     * @param email
     * @return
     */
    public static boolean matchEmail(String email) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * @param boolean
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 正则表达式
     * @创建时间 2014年11月20日下午12:50:02
     * @param expression
     * @param text
     * @return
     */
    public static boolean matcherText(String expression, String text) {
        Pattern p = Pattern.compile(expression); // 正则表达式
        Matcher m = p.matcher(text); // 操作的字符串
        return m.matches();
    }

    /**
     * @param boolean
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 手机号码验证
     * @创建时间 2014年11月20日下午12:48:15
     * @param phone
     * @return
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 打开软键盘
     * @创建时间 2014年11月27日下午2:41:39
     * @param mContext
     * @param view
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 调用系统浏览器打开url
     * @创建时间 2014年11月26日下午4:43:06
     * @param context
     * @param url
     */
    public static void openWebUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param packageName
     *            为空或者null则打开自己
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
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 调用系统发送短信界面(带短信内容)
     * @创建时间 2014年11月20日下午12:06:00
     * @param context
     * @param num
     * @param message
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
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 设置文本最大输入字符
     * @创建时间 2014年11月28日下午2:19:11
     * @param editText
     * @param len
     */
    public static void setEditLength(EditText editText, int len) {
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
                len) });
    }

    /**
     * @param void
     * @所属类 Util.java
     * @创建者 Aries_Hoo
     * @作用 设置缓存中的版本号(用于判断是否出现引导界面)
     * @创建时间 2014年11月20日下午12:49:39
     * @param context
     * @param versionCode
     */
    public static void setVersionCodeShare(Context context, int versionCode) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(),
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
    };

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
    };

    public static void startWifiSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_WIFI_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
