package com.architecture.demo.ui.navi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.anylife.keepalive.service.KeepAliveService
import com.anylife.keepalive.utils.BatteryUtils
import com.anylife.keepalive.utils.BatteryUtils.addWhiteList
import com.anylife.keepalive.utils.BatteryUtils.isIgnoringBatteryOptimizations
import com.anylife.keepalive.utils.BatteryUtils.requestIgnoreBatteryOptimizations

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


        //KeepAliveService，这里不要设置东西
        startService(Intent(this, KeepAliveService::class.java))

//        KeepAliveService.start(baseContext,KeepAliveService.AliveStrategy.JOB_SERVICE)

        Log.e("Main","------------------------ isIgnoringBatteryOptimizations:"+ isIgnoringBatteryOptimizations(baseContext));

        if(!isIgnoringBatteryOptimizations(baseContext)){
            requestIgnoreBatteryOptimizations(baseContext)
        }

//        addWhiteList(baseContext)

    }


}