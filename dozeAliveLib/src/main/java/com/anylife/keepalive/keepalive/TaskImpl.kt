package com.anylife.keepalive.keepalive


/**
 * 抽象任务
 *
 */
abstract class TaskImpl {
    companion object {
        const val TAG_EXECUTE = "action_execute"
        const val INTERVAL_NEVER = 0L
    }

    /**
     * 任务调度
     *
     * @param runAble        异步任务
     * @param intervalMillis 间隔时间
     * @param delayMillis    延迟时间  默认都不延迟
     */
    abstract fun scheduleTask(runAble: Runnable, intervalMillis: Long, delayMillis: Long = 0L)



    /**
     * 停止任务
     */
    abstract fun stopTask()


}