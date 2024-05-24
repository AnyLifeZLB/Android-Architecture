package com.anylife.keepalive.keepalive


/**
 * 抽象任务
 *
 */
abstract class AbstractTask {
    companion object {
        const val TAG_EXECUTE = "action_execute"
        const val INTERVAL_NEVER = 0L
    }


    /**
     * 开始定时任务
     *
     * @param task 异步任务
     * @param intervalMillis 间隔时间
     * @param delayMillis 延迟时间
     */
    abstract fun scheduleTask(task: Runnable, intervalMillis: Long, delayMillis: Long = 0L)




    /**
     * 开始延迟任务
     *
     * @param task 任务
     * @param delayMillis 延迟时间
     */
    abstract fun startTaskDelay(task: Runnable, delayMillis: Long = 0L)




    /**
     * 停止任务
     */
    abstract fun stopTask()


}