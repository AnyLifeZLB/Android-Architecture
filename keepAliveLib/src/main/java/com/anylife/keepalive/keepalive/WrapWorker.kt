package com.anylife.keepalive.keepalive

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.anylife.keepalive.keepalive.WorkTaskHelper.KEY_WORK_NAME
import com.anylife.keepalive.keepalive.verify.TestBackgroundWork
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.Date


/**
 * 具体的后台任务的包装类，实际的工作在Worker 中执行
 *
 * @property context
 * @property workerParams
 */
class WrapWorker(private val context: Context, private val workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val workName = workerParams.inputData.getString(KEY_WORK_NAME)
        val workTask = if (workName == null) null else WorkTaskHelper.taskMap[workName]

        Log.e(WorkTaskHelper.TAG, "Keep alive 「class WrapWorker」  -!- work ${workName}:${workTask}")

        //验证doze 模式的下收集数据，以及网络活动
        TestBackgroundWork(applicationContext).testDozeWork()


        workTask?.run {
            mTask?.let {
                it.run()
                // 继续开始下一次“扫描”
                if (mIntervalMillis > AbstractTask.INTERVAL_NEVER) {
                    scheduleTask(it, mIntervalMillis, mIntervalMillis)
                    Log.d(WorkTaskHelper.TAG, "继续开始下一次“扫描 \n")

                }
            }
        }


        return Result.success()
    }


















    // 下面是测试验证代码

    /**
     * 创建Socket 链接
     */

    private var socket: Socket? = null

    private fun createSocket() {


        // 获取Wi-Fi 信息
        val wifiService = applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager


        Thread {
            try {
                //1、创建客户端Socket，指定服务器地址和端口 10.39.170.156
                socket = Socket("10.39.179.70", 8894)

                //2、获取输出流，向服务端发送信息
                val os = socket!!.getOutputStream() //字节输出流
                val pw = PrintWriter(os) //将输出流包装为打印流

                val ssid= wifiService.connectionInfo.ssid
                //3，收集手机型号和时间。加个时间戳网络活动可能受限制，看看本地Log 有没有问题和间隔时间统计
                pw.println(Build.BRAND + "SPACE    $ssid   " + stampToDate(System.currentTimeMillis()))
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
    private fun stampToDate(s: Long): String {
        val res: String
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(s)
        res = simpleDateFormat.format(date)
        return res
    }





}