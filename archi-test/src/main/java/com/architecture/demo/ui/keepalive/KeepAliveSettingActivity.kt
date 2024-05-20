package com.architecture.demo.ui.keepalive

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anylife.keepalive.h5.H5KeepAliveGuideActivity
import com.anylife.keepalive.utils.BatteryOptimization
import com.anylife.keepalive.utils.KeepCompactUtil.daemonSet
import com.anylife.keepalive.utils.KeepCompactUtil.deviceEnum
import com.anylife.keepalive.h5.H5KeepAliveGuideActivity.KeepTypeMenu
import com.architecture.demo.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.Date

/**
 * 保活设置，这里处理的数据移动到其他地方去，不要和Activity 绑定
 * 单独使用一个Service 来验证吧，
 */
class KeepAliveSettingActivity : AppCompatActivity() {
    private var socket: Socket? = null
    private lateinit var  ignoringBatteryOptiTextView: TextView
    private var isDestroy = false
    private var isIgnoringBatteryOptimizations = false


    private lateinit var deviceName: String


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

        findViewById<View>(R.id.keep_alive_tips_background_set).setOnClickListener {
//            daemonSet(this@KeepAliveSettingActivity)
            H5KeepAliveGuideActivity.Companion.startWebView(baseContext,getURLPath(
                KeepTypeMenu.battery,deviceName),KeepTypeMenu.battery)
        }


        findViewById<View>(R.id.battery_set_background_set).setOnClickListener {
//            noSleepSet(
//                this@KeepAliveSettingActivity
//            )

            H5KeepAliveGuideActivity.Companion.startWebView(baseContext,getURLPath(
                KeepTypeMenu.daemon,deviceName),KeepTypeMenu.battery);
        }

        createSocket()
    }


    /**
     * 获取拼接后的URL
     * H5KeepAliveGuideActivity.Companion.startWebView(getBaseContext(),"file:///android_asset/keepalive_guide/html/mi_cn.html");
     *
     * @param keepType
     * @param deviceName
     */
    private fun getURLPath(keepType: H5KeepAliveGuideActivity.KeepTypeMenu, deviceName: String): String {
        return "file:///android_asset/keepalive_guide/html/$keepType/$deviceName/$deviceName.html"
    }





    // 下面是测试验证代码

    /**
     * 创建Socket 链接
     */
    private fun createSocket() {
        Thread {
            try {
                //1、创建客户端Socket，指定服务器地址和端口
                socket = Socket("10.39.169.59", 8886)

                while (!isDestroy) {
                    //2、获取输出流，向服务端发送信息
                    val os = socket!!.getOutputStream() //字节输出流
                    val pw = PrintWriter(os) //将输出流包装为打印流

                    //3，收集手机型号和时间。加个时间戳网络活动可能受限制，看看本地Log 有没有问题和间隔时间统计
                    pw.println(Build.BRAND + ": Hello Server " + stampToDate(System.currentTimeMillis()))
                    Log.e("TAG", "发送 hello Server a")
                    pw.flush()


                    //4.获取输入流，并读取服务器端的响应信息
                    val `is` = socket!!.getInputStream()
                    val br = BufferedReader(InputStreamReader(`is`))
                    val info = br.readLine()
                    if (info != null && info.length != 0) {
                        // 循环读取客户端的信息
                        Log.e("Socket", "收到服务器消息：$info")
                    }

                    Thread.sleep(2000)
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