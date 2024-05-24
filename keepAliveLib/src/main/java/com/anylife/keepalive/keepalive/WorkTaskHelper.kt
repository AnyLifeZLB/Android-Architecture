package com.anylife.keepalive.keepalive

import android.content.Context
import android.os.PowerManager
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit


/**
 * 定时唤醒CPU 和 刷新通知栏
 *
 */
internal object WorkTaskHelper {
    const val KEY_WORK_NAME = "key_work_name"
    const val TAG = "KeepAlive"

    val taskMap = ConcurrentHashMap<String, WorkTask>()

    private var wakeLock: PowerManager.WakeLock? = null

    private var lastFlashMillis = System.currentTimeMillis()

    private const val WORK_NAME_NOTIFY = "work_notify"

    /**
     * 唤醒CPU
     */
    @Synchronized
    fun wakeupCpu(context: Context) {
        lastFlashMillis = System.currentTimeMillis()

        if (wakeLock == null) {
            try {
                val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager?
                // PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP
                wakeLock = pm?.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK,
                    HeartbeatService.TAG_WAKE_LOCK
                )

                wakeLock?.run {
                    try {
                        setReferenceCounted(false)
                        acquire()
                        Log.e(TAG, "keep alive --- 唤醒CPU wake up cpu")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        } else {
            wakeLock?.run {
                try {
                    setReferenceCounted(false)
                    release()
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }
                try {
                    setReferenceCounted(false)
                    acquire()
                    Log.e(TAG, "keep alive --- 唤醒CPU wake up cpu")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @Synchronized
    fun stopWake() {
        wakeLock?.run {
            try {
                setReferenceCounted(false)
                release()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            } finally {
                wakeLock = null
            }
        }
    }


    /**
     * 不需要这样刷新，后台刷新UI 毫无意义
     *
     */
    fun startNotify(context:Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .build()

        val request = OneTimeWorkRequestBuilder<NotifyWorker>()
            .setInitialDelay(5_000L, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            WORK_NAME_NOTIFY,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}