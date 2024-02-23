package com.architecture.demo.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.anna.lib_keepalive.service.KeepAliveService;
import com.architecture.demo.http.HttpBaseInfo;
import com.architecture.httplib.base.BaseHttpApiService;

/**
 * MainApplication
 *
 */
public class MainApplication extends Application {
    public void onCreate(){
        super.onCreate();

        //初始化的时候要传入的参数
        BaseHttpApiService.Companion.init(new HttpBaseInfo(this));

        //KeepAliveService
//        startService(new Intent(this, KeepAliveService.class ));
        Log.e("Main","------------------------ isIgnoringBatteryOptimizations:"+isIgnoringBatteryOptimizations());

        if(!isIgnoringBatteryOptimizations()){
            requestIgnoreBatteryOptimizations();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
