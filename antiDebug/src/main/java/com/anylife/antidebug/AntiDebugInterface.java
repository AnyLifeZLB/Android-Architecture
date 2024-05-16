package com.anylife.antidebug;

/**
 *
 *
 * Created by zenglb on 2018/7/26.
 */
public class AntiDebugInterface {

    static {
        System.loadLibrary("AntiDebug-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public static native void checkDebug();



}
























//CPP 代码下载链接：https://download.csdn.net/download/weixin_30406497/10565565