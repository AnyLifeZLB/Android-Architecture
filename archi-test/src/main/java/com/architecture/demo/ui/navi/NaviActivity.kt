package com.architecture.demo.ui.navi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.anna.lib_keepalive.service.KeepAliveService
import com.anna.lib_keepalive.utils.BatteryUtils

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


        //KeepAliveService
//        startService(Intent(this, KeepAliveService::class.java))

        KeepAliveService.start(baseContext,KeepAliveService.AliveStrategy.JOB_SERVICE)

        Log.e("Main","------------------------ isIgnoringBatteryOptimizations:"+ BatteryUtils.isIgnoringBatteryOptimizations(baseContext));

//        if(!isIgnoringBatteryOptimizations(baseContext)){
//            requestIgnoreBatteryOptimizations(baseContext)
//        }



    }




}