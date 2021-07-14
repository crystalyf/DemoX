package com.change.demox.extension

import android.content.Context
import android.os.Build
import com.change.demox.camera.part.MainExecutor
import java.util.concurrent.Executor

fun Context.mainExecutor(): Executor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    mainExecutor
} else {
    MainExecutor()
}

