package com.architecture.demo.ui.navi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anylife.keepalive.keepalive.HeartbeatService
import com.anylife.keepalive.utils.BatteryOptimization.isIgnoringBatteryOptimizations
import com.anylife.keepalive.utils.ServiceUtils

/***
 * ComponentActivity ：使用最新的尝试看看
 *
 */
class NaviActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val naviActivityScreenViewModel =
            ViewModelProvider(this)[NaviActivityScreenViewModel::class.java]


        //尝试使用Compose 吧，不需要大规模使用
        setContent {
            MainActivityScreen(naviActivityScreenViewModel)
        }



        //这里启动Notification,用于保活---不一定可用，多少有点用
        if (!ServiceUtils.isServiceRunning(HeartbeatService::class.java.toString(),baseContext)) {
            ContextCompat.startForegroundService(this, Intent(this, HeartbeatService::class.java))
        }



//        checkDebug()

//        startService(Intent(this, KeepAliveService::class.java))
//        KeepAliveService.start(baseContext,KeepAliveService.AliveStrategy.JOB_SERVICE)


    }

}