package com.anylife.keepalive.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

/**
 * 后台运行权限，也就是自启动
 * 暂时先不要这些了，不维护了；
 *
 */
@Deprecated
public class RestartSettingUtils {

    /**
     * 跳转到指定应用的首页
     */
    private static void showActivity(Context context,@NonNull String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     *
     */
    private static void showActivity(Context context,@NonNull String packageName, @NonNull String activityDir) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 针对不同的手机匹配不同的设置，主要是兼容性很难处理
     *
     * @param context
     */
    public static void setReStartAction(Context context){
        if (isHuawei()){
            goHuaweiSetting(context);
        }else if(isMeizu()){
            goMeizuSetting(context);
        }else if(isOPPO()){
            goOPPOSetting(context);
        }else if(isSamsung()){
            goSamsungSetting(context);
        }else if(isXiaomi()){
            goXiaomiSetting(context);
        }else if(isVIVO()){
            goVIVOSetting(context);
        }
    }


    //华为：可能要分化为华为和荣耀了
    public static boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }

    //小米
    public static boolean isXiaomi() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi");
    }

    // OPPO
    public static boolean isOPPO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("oppo");
    }

    //VIVO
    public static boolean isVIVO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("vivo");
    }

    //魅族
    public static boolean isMeizu() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("meizu");
    }

    // 三星
    public static boolean isSamsung() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("samsung");
    }

   // 手机管家或者自启动界面启动方式：
   // 华为：
    private static void goHuaweiSetting(Context context) {
        try {
            showActivity(context,"com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } catch (Exception e) {
            showActivity(context,"com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
        }
    }


   // 小米：
    private static void goXiaomiSetting(Context context) {
        showActivity(context,"com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }


    //OPPO：
    private  static void goOPPOSetting(Context context) {
        try {
            showActivity(context,"com.coloros.phonemanager");
        } catch (Exception e1) {
            try {
                showActivity(context,"com.oppo.safe");
            } catch (Exception e2) {
                try {
                    showActivity(context,"com.coloros.oppoguardelf");
                } catch (Exception e3) {
                    showActivity(context,"com.coloros.safecenter");
                }
            }
        }
    }


    //VIVO
    public static void goVIVOSetting(Context context) {
        showActivity(context,"com.iqoo.secure");
    }


    //魅族
    private static void goMeizuSetting(Context context) {
        showActivity(context,"com.meizu.safe");
    }


    //三星
    private static void goSamsungSetting(Context context) {
        try {
            showActivity(context,"com.samsung.android.sm_cn");
        } catch (Exception e) {
            showActivity(context,"com.samsung.android.sm");
        }
    }

}
