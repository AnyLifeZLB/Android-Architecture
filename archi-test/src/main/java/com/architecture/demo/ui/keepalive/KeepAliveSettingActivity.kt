package com.architecture.demo.ui.keepalive

import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.anylife.keepalive.h5.H5KeepAliveGuideActivity
import com.anylife.keepalive.h5.H5KeepAliveGuideActivity.KeepTypeMenu
import com.anylife.keepalive.service.KeepAliveService
import com.anylife.keepalive.service.KeepAliveService.AliveStrategy
import com.anylife.keepalive.utils.BatteryOptimization
import com.anylife.keepalive.utils.KeepCompactUtil.deviceEnum
import com.architecture.demo.R
import com.architecture.demo.ui.archi.fragment.home.HomeViewModel
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * 保活设置，这里处理的数据移动到其他地方去，不要和Activity 绑定
 * 单独使用一个Service 来验证吧
 *
 * https://tuisemo.github.io/2018/09/18/json-server%E2%80%94%E2%80%9430%E7%A7%92%E6%90%AD%E5%BB%BA%E4%B8%80%E4%B8%AA%E6%9C%AC%E5%9C%B0%E7%9A%84REST-API%E6%9C%8D/
 *
 * @author zenglb@vanke.com
 */
class KeepAliveSettingActivity : AppCompatActivity() {
    private var socket: Socket? = null
    private lateinit var  ignoringBatteryOptiTextView: TextView
    private var isDestroy = false
    private var isIgnoringBatteryOptimizations = false

    private lateinit var deviceName: String

    private lateinit var keepAliveViewModel:KeepAliveViewModel


    override fun onResume() {
        super.onResume()
        isIgnoringBatteryOptimizations = BatteryOptimization.isIgnoringBatteryOptimizations(this)
        if (isIgnoringBatteryOptimizations) {
            ignoringBatteryOptiTextView.text = "已优化"
            ignoringBatteryOptiTextView.setBackgroundResource(R.drawable.shape_green_btn_bg)
        } else {
            ignoringBatteryOptiTextView.text = "去设置"
            ignoringBatteryOptiTextView.setBackgroundResource(R.drawable.shape_set_btn_bg)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keep_alive_setting)
        title = "App运行设置"


        //判断出是什么品牌的手机
        deviceName = deviceEnum.toString()


        ignoringBatteryOptiTextView = findViewById(R.id.keep_alive_battery_set)
        ignoringBatteryOptiTextView.setOnClickListener(View.OnClickListener { // 已经设置的不需要再去设置
            if (!isIgnoringBatteryOptimizations) {
                BatteryOptimization.requestIgnoreBatteryOptimizations(this@KeepAliveSettingActivity)
            }
        })

        findViewById<View>(R.id.keep_alive_background_set).setOnClickListener {
            H5KeepAliveGuideActivity.Companion.startWebView(baseContext,getURLPath(
                KeepTypeMenu.daemon,deviceName),KeepTypeMenu.daemon)
        }


        findViewById<View>(R.id.battery_set_background_set).setOnClickListener {
            H5KeepAliveGuideActivity.Companion.startWebView(baseContext,getURLPath(
                KeepTypeMenu.battery,deviceName),KeepTypeMenu.battery)
        }




        keepAliveViewModel =
            ViewModelProvider(this)[KeepAliveViewModel::class.java]

        createSocket()

        postData()



        //约束条件
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)  //???
            .setRequiresCharging(false)
            .build()


        //Worker 定义工作单元，WorkRequest（及其子类）则定义工作运行方式和时间
        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<UploadWorker>()
                .build()


        val request = OneTimeWorkRequestBuilder<UploadWorker>().setInitialDelay(0L, TimeUnit.MILLISECONDS)
            // 周期性任务不能设置这个，有延迟的任务也不能设置这个
            //.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(constraints)
            .build()



        //将 WorkRequest 提交给系统
//        WorkManager
//            .getInstance(baseContext)
//            .enqueue(uploadWorkRequest)


        WorkManager.getInstance(baseContext).enqueueUniqueWork(
            "workName????",
            ExistingWorkPolicy.REPLACE,
            request
        )



    }


    /**
     * 不用判断一堆的手机型号，按照一定的规则来处理拼接处理URL
     *
     * @param keepType    保活类型，省电优化还是后台启动
     * @param deviceName  设备厂商类型
     */
    private fun getURLPath(keepType: KeepTypeMenu, deviceName: String): String {
        return "file:///android_asset/keepalive_guide/html/$keepType/$deviceName/$deviceName.html"
    }




    /**
     * 创建Socket 链接
     */
    private fun postData() {

        Thread {
            try {
                //1、创建客户端Socket，指定服务器地址和端口 10.39.170.156

                while (!isDestroy) {
                    //2、获取输出流，向服务端发送信息
                    keepAliveViewModel.postJsonData(application)

                    Thread.sleep(5000)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                println("-- finally error -----------")
            }
        }.start()
    }




    // 下面是测试验证代码

    /**
     * 创建Socket 链接
     */
    private fun createSocket() {

        // 获取Wi-Fi 信息
        val wifiService = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        Thread {
            try {
                //1、创建客户端Socket，指定服务器地址和端口 10.39.170.156
                socket = Socket("10.39.179.70", 8894)

                while (!isDestroy) {
                    //2、获取输出流，向服务端发送信息
                    val os = socket!!.getOutputStream() //字节输出流
                    val pw = PrintWriter(os) //将输出流包装为打印流

                    val ssid= wifiService.connectionInfo.ssid
                    //3，收集手机型号和时间。加个时间戳网络活动可能受限制，看看本地Log 有没有问题和间隔时间统计
                    pw.println(Build.BRAND + "      $ssid      " + stampToDate(System.currentTimeMillis()))
                    Log.e("TAG", "发送 hello Server a")
                    pw.flush()


                    //4.获取输入流，并读取服务器端的响应信息
                    val inputStream = socket!!.getInputStream()
                    val br = BufferedReader(InputStreamReader(inputStream))
                    val info = br.readLine()
                    if (info != null && info.isNotEmpty()) {
                        // 循环读取客户端的信息
                        Log.e("Socket", "收到服务器消息：$info")
                    }

                    Thread.sleep(5000)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            } finally {
                println("-- finally error -----------")
            }
        }.start()
    }


    /*
     * 将时间戳转换为时间
     *
     */
    fun stampToDate(s: Long): String {
        val res: String
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(s)
        res = simpleDateFormat.format(date)
        return res
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroy = true
    }
}