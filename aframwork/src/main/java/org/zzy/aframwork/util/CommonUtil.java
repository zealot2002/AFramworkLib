package org.zzy.aframwork.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.validator.routines.UrlValidator;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class CommonUtil {
    public static final Pattern sDateTimePattern = Pattern.compile("(\\d4)(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})");
    public static long timeInterval = 0;

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 把密度转换为像素
     */
    public static int dip2px(Context context, float dp) {
        final float scale = getScreenDensity(context);
        return (int) (dp * scale + 0.5);
    }

    public static float dip2pxF(Context context, float dp) {
        final float scale = getScreenDensity(context);
        return (dp * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 全屏
     *
     * @param mActivity
     */
    public static void requestFullscreen(final Activity mActivity) {
        final Window window = mActivity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /** 获取手机的IMEI串号 */
    public static String getPhoneImei(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)
            return runningTaskInfos.get(0).topActivity.getClassName();
        else
            return "";
    }

    /**
     * 验证邮箱地址是否正确
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 验证url
     *
     */
    public static boolean isValidUrl(String url) {
        return new UrlValidator().isValid(url);
    }
    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return [0-9]{5,9}
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    public static boolean isNum(String number) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[0-9]{5}$");
            Matcher m = p.matcher(number);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 将String 类型的double数据转换成格式化后的百分比数据
     *
     * @param doublevalue
     * @return
     */
    public static String formatDoublePer(String doublevalue) {
        String format = "";
        if (!TextUtils.isEmpty(doublevalue)) {
            try {
                format = new DecimalFormat("#.##%").format(Double.parseDouble(doublevalue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return format;
    }

    /**
     * 将String 类型的double数据转换成格式化后的百分比数据
     *
     * @param doublevalue
     * @return
     */
    public static String formatDoublePerZero(String doublevalue) {
        String format = "";
        if (!TextUtils.isEmpty(doublevalue)) {
            try {
                double s = Double.parseDouble(doublevalue) / 100;
                format = new DecimalFormat("#.##%").format(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return format;
    }

    public static String formatDoublePerZeroSame(String doublevalue) {
        String format = "";
        if (!TextUtils.isEmpty(doublevalue)) {
            try {
                double s = Double.parseDouble(doublevalue);
                format = new DecimalFormat("#.##%").format(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return format;
    }

    /**
     * 将String 类型的double数据转换成格式化后的百分比数据
     *
     *            将0.0256转化成float类型的2.6
     * @return
     */
    public static float formatFloat(String floatValue) {
        float data = 0f;
        if (!TextUtils.isEmpty(floatValue)) {
            try {
                DecimalFormat df = new DecimalFormat("0.00");
                float data1 = Float.parseFloat(floatValue);
                data = Float.parseFloat(df.format(data1 * 100));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /** 将String类型的数据格式化 0.00补上 */
    public static String formatStringData(String str) {
        String format = "";
        if (!TextUtils.isEmpty(str)) {
            try {
                float f = Float.parseFloat(str);
                DecimalFormat df = new DecimalFormat("0.00");
                format = df.format(f);
                if (format.contains(".")) {
                    int ind = format.indexOf(".");
                    if (format.substring(ind).length() < 2) {
                        format += "0";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return format;
    }

    /** 将String类型的数据格式化 0.00补上 */
    public static String formatStringPercentData100(String str) {
        String format = "";
        if (!TextUtils.isEmpty(str)) {
            try {
                double f = Double.parseDouble(str) * 100;
                DecimalFormat df = new DecimalFormat("0.00");
                format = df.format(f);
                if (format.contains(".")) {
                    int ind = format.indexOf(".");
                    if (format.substring(ind).length() < 2) {
                        format += "0";
                    }
                }
                format += "%";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return format;
    }

    /** 将String类型的数据格式化 0.00补上 */
    public static String formatStringPercentData(String str) {
        String format = "";
        if (!TextUtils.isEmpty(str)) {
            try {
                float f = Float.parseFloat(str);
                DecimalFormat df = new DecimalFormat("0.00");
                format = df.format(f);
                if (format.contains(".")) {
                    int ind = format.indexOf(".");
                    if (format.substring(ind).length() < 2) {
                        format += "0";
                    }
                }
                format += "%";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return format;
    }

    /** 将String类型的数据格式化 *100 0.00补上 */
    public static String formatStringPercentData1(String str) {
        String format = "";
        if (!TextUtils.isEmpty(str)) {
            try {
                float f = Float.parseFloat(str) * 100;
                DecimalFormat df = new DecimalFormat("0.00");
                format = df.format(f);
                if (format.contains(".")) {
                    int ind = format.indexOf(".");
                    if (format.substring(ind).length() < 2) {
                        format += "0";
                    }
                }
                format += "%";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return format;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getAndroidDisplayVersion(Context context) {
        String androidDisplay = null;
        androidDisplay = android.os.Build.DISPLAY;
        return androidDisplay;
    }

    /**
     * 文本复制
     *
     * @param content
     */
    public static void copyText(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 粘贴
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    /**
     * 设置 属性 防止异常 UnsupportedOperationException
     *
     * @param view
     */
    public static void setButtons(View view) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    // SDK1.5以上支持
    /** 获取手机型号 */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /** 获取手机生厂商 */
    public static String getMANUFACTURER() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 打开软键盘
     *
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static String generateTag() {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = "%s";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName);
        return tag;
    }

    public static void addShortCut(Context context, String name, int resourceId) {
        // 安装的Intent
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        // 快捷名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // 快捷图标是允许重复
        shortcut.putExtra("duplicate", false);

        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.putExtra("name", name);
        shortcutIntent.setClassName("com.mdj", "com.mdj.ui.WelcomeActivity");
        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        // 快捷图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, resourceId);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        // 发送广播
        context.sendBroadcast(shortcut);
    }

    /**
     * 方法名称:transStringToMap
     * 传入参数:mapString 形如 username'chenziwen^password'1234
     * 返回值:Map
     */
    public static Map transStringToMap(String mapString){
        Map map = new HashMap();
        StringTokenizer items;
        for(StringTokenizer entrys = new StringTokenizer(mapString, "^"); entrys.hasMoreTokens();
            map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), "'");
        return map;
    }

    /**
     * 方法名称:transMapToString
     * 传入参数:map
     * 返回值:String 形如 username'chenziwen^password'1234
     */
    public static String transMapToString(Map map){
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for(Iterator iterator = map.entrySet().iterator(); iterator.hasNext();)
        {
            entry = (Map.Entry)iterator.next();
            sb.append(entry.getKey().toString()).append( "'" ).append(null==entry.getValue()?"":
                    entry.getValue().toString()).append (iterator.hasNext() ? "^" : "");
        }
        return sb.toString();
    }
    public static String jointUrl(LinkedHashMap<String, String> params, String url) {
        try {
            // 添加url参数
            if (params != null) {
                Iterator<String> it = params.keySet().iterator();
                StringBuffer sb = new StringBuffer();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = params.get(key);
                    if(TextUtils.isEmpty(sb.toString()))
                    {
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
                url += sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
    /*
	 * 启动一个app
	 */
    public static void startApp(Context context, String appPackageName){
        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        }catch(Exception e){
            Toast.makeText(context, "您没有安装该应用："+appPackageName, Toast.LENGTH_LONG).show();
        }
    }
}
