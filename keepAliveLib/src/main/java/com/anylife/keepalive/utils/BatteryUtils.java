package com.anylife.keepalive.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

/**
 * 判断应用是否在白名单中，并且能调整到系统基本的设置里面去
 */
public class BatteryUtils {

    /**
     * 判断是否在电池优化的白名单中
     *
     * @param context
     * @return
     */
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnore = false;
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isIgnore = pm.isIgnoringBatteryOptimizations(context.getPackageName());
            }
        }
        return isIgnore;
    }

    /**
     * 满足可接受的用例的应用可以改为调用包含 ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS intent
     * 操作的 intent，让用户无需转到系统设置即可直接将应用添加到豁免列表
     *
     * 基本上在国内可以随便弄，但是在国外就麻烦了，Google Play 政策不允许使用，否则不能上架G Play
     *
     * @param context
     */
    public static void requestIgnoreBatteryOptimizations(Context context) {
        if(isIgnoringBatteryOptimizations(context)){
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:"+context.getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * 大多数应用可以调用包含 ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS 的 intent。
     *
     * @param context
     */
    public static void addWhiteList(Context context){
        // 获取电量管理器
        PowerManager powerManager = (PowerManager) context.
                getSystemService(Context.POWER_SERVICE);
        // Android 6.0 以上才能使用该功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 查看当前应用是否则电量白名单中
            boolean isInWhiteList = powerManager.
                    isIgnoringBatteryOptimizations(context.getPackageName());
            // 如果没有在白名单中 , 弹出对话框 , 引导用户设置白名单
            if(!isInWhiteList){
                // 弹出白名单设置对话框
                Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                context.startActivity(intent);
            }
        }
    }

}
