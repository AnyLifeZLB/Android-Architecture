package com.anylife.keepalive.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 保活设置，取消Service，应该没有毕业，正规app 都可以通知栏长驻通知
 *
 */
public class CancelNotifyService extends Service {
    ForegroundNF _mForegroundNF;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG","onCreate--");
        _mForegroundNF = new ForegroundNF(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG","onStartCommand--");
        _mForegroundNF.startForegroundNotification();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("TAG","onDestroy--");
        super.onDestroy();
        _mForegroundNF.stopForegroundNotification();
    }
}

