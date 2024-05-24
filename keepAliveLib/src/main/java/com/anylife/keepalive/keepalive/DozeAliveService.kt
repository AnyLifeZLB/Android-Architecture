package com.anylife.keepalive.keepalive

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.anylife.keepalive.R
import com.anylife.keepalive.h5.H5KeepAliveGuideActivity
import com.anylife.keepalive.keepalive.DozeAliveService.Companion.SPACE_NOTIFY_ID

/**
 * 要提交的任务，runAble 和 间隔时间
 *
 */
class DozeAliveService : Service() {
    private val TAG = "DozeAliveService"

    companion object {
        const val TAG_WAKE_LOCK = "space:wake_screen"
        const val ACTION_NOTIFY = "action_notify"
        const val SPACE_NOTIFY_ID = 73986
    }

    //服务开启时间，用于计算定时器执行的时间
    private var startServiceTime = 0L


    //获取定位工牌配置定时器
    private val customTask: AbstractTask by lazy {
        WorkTask(this, "custom_task")
    }

    //唤醒CPU定时器
    private val wakeupCpuWorkTask: AbstractTask by lazy {
        WorkTask(this, "wakeupCpu")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showForegroundNotification()
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onCreate() {
        super.onCreate()
        showForegroundNotification()
//        WorkTaskHelper.wakeupCpu(this)  //先屏蔽，没必要吧
        startServiceTime = System.currentTimeMillis()


        //要提交的周期任务，10秒的整数倍
        wakeupCpuWorkTask.scheduleTask(kotlinx.coroutines.Runnable {
            WorkTaskHelper.wakeupCpu(this)
            val time = (System.currentTimeMillis() - startServiceTime) / 1000L
            Log.e(
                WorkTaskHelper.TAG,
                "唤醒CPU定时任务执行时间：${time}"
            )
        },1*10000L)





        //刷新定位配置，30分钟更新一次 ？
        customTask.scheduleTask({

            Log.e(
                WorkTaskHelper.TAG,
                "另外一个周期任务 -----------------------------------------------"
            )

        }, 1 * 10_000L)




    }



    override fun onDestroy() {
        super.onDestroy()
        Log.w(WorkTaskHelper.TAG, "DozeAliveService  destroyed")
        customTask.stopTask()
        wakeupCpuWorkTask.stopTask()
        WorkTaskHelper.stopWake()
    }
}




/**
 * 显示前台通知
 *
 */
fun Context.showForegroundNotification(forUpdate: Boolean = false) {
    val notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

    val openIntent = Intent(this, H5KeepAliveGuideActivity::class.java)
    openIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        openIntent,
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
    var appName = "Android 框架搭建"

    builder.setContentTitle(appName)
        .setContentText(appName + "运行中")
        .setChannelId(packageName)
        .setWhen(System.currentTimeMillis())
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setContentIntent(pendingIntent)//先不加点击
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
    if (forUpdate) {
        notificationManager.notify(SPACE_NOTIFY_ID, notification)
    } else {
        (this as? Service)?.startForeground(SPACE_NOTIFY_ID, notification)
    }
}

