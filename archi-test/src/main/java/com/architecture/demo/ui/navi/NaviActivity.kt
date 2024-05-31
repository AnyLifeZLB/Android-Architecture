package com.architecture.demo.ui.navi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anylife.keepalive.keepalive.DozeAliveService
import com.anylife.keepalive.utils.DozeServiceUtils
import com.architecture.demo.application.MainApplication
import com.architecture.demo.ui.keepalive.BackGroundWork


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


//        //定义一个run
//        val run= Runnable {
//            //模拟后台持续采集定位
//            BackGroundWork.postJsonData(application)
//        }


        Thread {
            try {
                //1、创建客户端Socket，指定服务器地址和端口 10.39.170.156

                while (!MainApplication.isTerminate) {
                    //2、获取输出流，向服务端发送信息
                    BackGroundWork.postJsonData(application)

                    Thread.sleep(10000)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                println("-- finally error -----------")
            }
        }.start()


        //这里启动Notification,用于保活---不一定可用，多少有点用
        if (!DozeServiceUtils.isServiceRunning(DozeAliveService::class.java.toString(),baseContext)) {
            val intent=Intent(this, DozeAliveService::class.java)

            //封装一下，支持传参数
            ContextCompat.startForegroundService(this, intent)
        }

//        DozeAliveService.s



    }




}