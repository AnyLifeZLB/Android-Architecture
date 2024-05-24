package com.anylife.keepalive.keepalive

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * 不需要更新吧，更新个啥
 *
 *
 * @param workerParams
 */
class NotifyWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.e(WorkTaskHelper.TAG, "Keep alive 更新通知栏")
        context.applicationContext.sendBroadcast(Intent(HeartbeatService.ACTION_NOTIFY))
        WorkTaskHelper.startNotify(context)
        return Result.success()
    }
}

