package com.anylife.keepalive.utils;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;


/**
 * 移动到服务内部去吧
 *
 *
 */
public final class DozeServiceUtils {

    private DozeServiceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return all of the services are running.
     *
     * @return all of the services are running
     */
    public static Set<String> getAllRunningServices(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> info = am.getRunningServices(0x7FFFFFFF);
        Set<String> names = new HashSet<>();
        if (info == null || info.size() == 0) return null;
        for (RunningServiceInfo aInfo : info) {
            names.add(aInfo.service.getClassName());
        }
        return names;
    }



    /**
     * Return whether service is running.
     *
     * @param className The name of class.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isServiceRunning(@NonNull final String className,Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningServiceInfo> info = am.getRunningServices(0x7FFFFFFF);
            if (info == null || info.isEmpty()) return false;
            for (RunningServiceInfo aInfo : info) {
                if (className.equals(aInfo.service.getClassName())) return true;
            }
            return false;
        } catch (Exception ignore) {
            return false;
        }
    }
}
