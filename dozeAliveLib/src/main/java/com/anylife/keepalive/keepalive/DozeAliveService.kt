package com.anylife.keepalive.keepalive

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.anylife.keepalive.R
import com.anylife.keepalive.utils.AppUtils


/**
 * 要提交的任务，runAble 和 间隔时间
 *
 */
class DozeAliveService : Service() {
    private val TAG = "DozeAliveService"

    companion object {
        const val TAG_WAKE_LOCK = "DozeAlive:WakeUp"
        const val ACTION_NOTIFY = "action_notify"
    }

    //服务开启时间，用于计算定时器执行的时间
    private var startServiceTime = 0L


    private lateinit var taskParamObject: TaskParamObject


    //获取定位工牌配置定时器
    private val customTask: TaskImpl by lazy {
        WorkTaskImpl(this, "custom_task")
    }

    //唤醒CPU定时器
    private val wakeupCpuWorkTask: TaskImpl by lazy {
        WorkTaskImpl(this, "wakeupCpu")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        showForegroundNotification()

        AppUtils.getAppName(this)

        return super.onStartCommand(intent, flags, startId)
    }


    override fun onCreate() {
        super.onCreate()

//        showForegroundNotification()

//        WorkTaskHelper.wakeupCpu(this)  //先屏蔽，没必要吧
        startServiceTime = System.currentTimeMillis()


        //要提交的周期任务，10秒的整数倍
        wakeupCpuWorkTask.scheduleTask(kotlinx.coroutines.Runnable {

            DozeWakeUpManger.dozeWakePMUp(this)

//            BackGroundWork.postJsonData(application)

            val time = (System.currentTimeMillis() - startServiceTime) / 1000L
            Log.e(
                DozeWakeUpManger.TAG,
                "唤醒CPU定时任务执行时间：${time}"
            )
        }, 1 * 10000L)


//        //刷新定位配置，30分钟更新一次 ？
//        customTask.scheduleTask({
//
//            Log.e(
//                WorkTaskHelper.TAG,
//                "另外一个周期任务 -----------------------------------------------"
//            )
//
//        }, 1 * 10_000L)

    }

    /**
     * 停止保活服务
     *
     */
    fun onStop(){

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.w(DozeWakeUpManger.TAG, "DozeAliveService  destroyed")
        customTask.stopTask()
        wakeupCpuWorkTask.stopTask()
        DozeWakeUpManger.stopWakeUp()
    }
}








/**
 * 显示前台通知,当是否存活的标识
 *
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Context.showForegroundNotification(forUpdate: Boolean = false) {
    val notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

    val clickIntent = Intent("com.common.dozealive.ACTION_START")
    clickIntent.addCategory("com.common.dozealive.MY_CATEGORY")

    clickIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        clickIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val channel = NotificationChannel(packageName, packageName, NotificationManager.IMPORTANCE_LOW)
    channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
    // 设置通知出现时不震动
    channel.enableVibration(false)
    channel.setVibrationPattern(longArrayOf(0L))
    // 不要声音
    channel.setSound(null, null)
    notificationManager.createNotificationChannel(channel)

    val builder = NotificationCompat.Builder(this, packageName)
    val appName = AppUtils.getAppName(applicationContext )

    builder.setContentTitle(appName)
        .setContentText(appName+"运行中...")
        .setChannelId(packageName)
        .setWhen(System.currentTimeMillis())
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setContentIntent(pendingIntent)   //先不加点击
        .setAutoCancel(false)
        .setOngoing(true)
        .setWhen(System.currentTimeMillis())
        .setShowWhen(true)
        .setOngoing(true)
        .setVibrate(longArrayOf(0L))
        .setSound(null)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setSmallIcon(R.mipmap.ic_launcher_round)

    val notification: Notification = builder.build()


    val notifyID = (System.currentTimeMillis() / 1000).toInt()

    if (forUpdate) {
        notificationManager.notify(notifyID, notification)
    } else {
        (this as? Service)?.startForeground(notifyID, notification)
    }
}

