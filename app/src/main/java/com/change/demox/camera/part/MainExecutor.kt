package com.change.demox.camera.part

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * 线程池
 */
class MainExecutor : ThreadExecutor(Handler(Looper.getMainLooper())) {
    override fun execute(runnable: Runnable) {
        handler.post(runnable)
    }
}

open class ThreadExecutor(protected val handler: Handler) : Executor {
    override fun execute(runnable: Runnable) {
        handler.post(runnable)
    }
}