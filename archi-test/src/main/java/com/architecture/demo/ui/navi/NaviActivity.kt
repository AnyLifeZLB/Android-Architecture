package com.architecture.demo.ui.navi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anylife.keepalive.keepalive.DozeAliveService
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


        val run= Runnable {

        }

        //这里启动Notification,用于保活---不一定可用，多少有点用
        if (!ServiceUtils.isServiceRunning(DozeAliveService::class.java.toString(),baseContext)) {

            //封装一下，支持传参数
            ContextCompat.startForegroundService(this, Intent(this, DozeAliveService::class.java))
        }



    }

}