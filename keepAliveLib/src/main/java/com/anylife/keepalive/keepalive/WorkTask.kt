package com.anylife.keepalive.keepalive

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.anylife.keepalive.keepalive.WorkTaskHelper.KEY_WORK_NAME
import java.util.concurrent.TimeUnit

/**
 * 周期性后台任务的新实现
 *
 */
class WorkTask(private val context: Context, private val workName: String = TAG_EXECUTE): AbstractTask() {

    /**
     * 0表示不循环
     */
    @Volatile
    var mIntervalMillis = INTERVAL_NEVER

    @Volatile
    var mTask: Runnable? = null


    /**
     * 重复的调度任务
     *
     * @param task
     * @param intervalMillis
     * @param delayMillis
     */
    override fun scheduleTask(task: Runnable, intervalMillis: Long, delayMillis: Long) {
        if (intervalMillis < INTERVAL_NEVER) {
            throw IllegalArgumentException("intervalMillis is less than 0")
        }


        mTask = task
        WorkTaskHelper.taskMap[workName] = this
        mIntervalMillis = intervalMillis

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .build()

        // 周期性任务最短周期为15分钟，如果想实现小于15分钟的效果，则使用一次性任务+重复执行曲线救国，
        // 需要注意，这个时间间隔并不是精确的，会有10毫秒左右延迟
        //val request = PeriodicWorkRequestBuilder<ScanWorker>(mScanSetting.intervalSeconds, TimeUnit.SECONDS)
        //val request = OneTimeWorkRequestBuilder<W>()
        val data = Data.Builder().putString(KEY_WORK_NAME, workName).build()
        val request = OneTimeWorkRequestBuilder<WrapWorker>().setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            // 周期性任务不能设置这个，有延迟的任务也不能设置这个
            //.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }


    /**
     * 没有用
     *
     * @param task
     * @param delayMillis
     */
    @Deprecated("No used")
    override fun startTaskDelay(task: Runnable, delayMillis: Long) {
        mTask = task
        WorkTaskHelper.taskMap[workName] = this
        mIntervalMillis = INTERVAL_NEVER
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .build()

        val data = Data.Builder().putString(KEY_WORK_NAME, workName).build()
        val request = OneTimeWorkRequestBuilder<WrapWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun stopTask() {
        mTask = null
        WorkTaskHelper.taskMap.remove(workName)
        WorkManager.getInstance(context).cancelUniqueWork(workName)
    }
}