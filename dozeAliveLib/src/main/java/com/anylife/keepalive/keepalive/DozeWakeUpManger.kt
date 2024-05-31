package com.anylife.keepalive.keepalive

import android.content.Context
import android.os.PowerManager
import android.util.Log
import java.util.concurrent.ConcurrentHashMap


/**
 * 定时唤醒CPU 和 刷新通知栏
 *
 */
internal object DozeWakeUpManger {
    val taskMap = ConcurrentHashMap<String, WorkTaskImpl>()  //只是为了做拓展
    private var wakeLock: PowerManager.WakeLock? = null

    private var lastFlashMillis = System.currentTimeMillis()
    const val KEY_WORK_NAME = "key_work_name"
    const val TAG = "KeepAlive"

    /**
     * 唤醒CPU
     */
    @Synchronized
    fun dozeWakePMUp(context: Context) {
        lastFlashMillis = System.currentTimeMillis()

        if (wakeLock == null) {
            try {
                val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager?
                // PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP

                wakeLock = pm?.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK,
                    DozeAliveService.TAG_WAKE_LOCK
                )

                wakeLock?.run {
                    try {
                        setReferenceCounted(false)
                        acquire()
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
                    Log.i(TAG, "「wakeupCpu」 --- 唤醒CPU-- wake up cpu")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @Synchronized
    fun stopWakeUp() {
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
}