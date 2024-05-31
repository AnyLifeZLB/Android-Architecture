package com.architecture.demo.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import com.architecture.demo.http.HttpBaseInfo;
import com.architecture.httplib.base.BaseHttpApiService;

/**
 * MainApplication
 *
 */
public class MainApplication extends Application {
    public static boolean isTerminate=false;


    public void onCreate(){
        super.onCreate();


        //初始化的时候要传入的参数
        BaseHttpApiService.Companion.init(new HttpBaseInfo(this));
    }

    /**
     * 要注意仅仅是主进程结束才是结束，这里没有多进程，简单处理
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        isTerminate=true;

    }



}
