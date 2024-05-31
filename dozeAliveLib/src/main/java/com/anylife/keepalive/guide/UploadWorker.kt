package com.anylife.keepalive.guide

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * 具体的工作使用worker 类定义
 *
 * @param appContext
 * @param workerParams
 */
class UploadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    //doWork() 方法在 WorkManager 提供的后台线程上异步运行
    override fun doWork(): Result {

        // Do the work here--in this case, upload the images.
        // uploadImages()

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}
