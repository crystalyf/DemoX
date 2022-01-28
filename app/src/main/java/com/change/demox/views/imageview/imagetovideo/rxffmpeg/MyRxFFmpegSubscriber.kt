package com.change.demox.views.imageview.imagetovideo.rxffmpeg

import android.util.Log
import io.microshow.rxffmpeg.RxFFmpegSubscriber

/** Created by Fenrir-xingjunchao on 2022/1/28. **/
class MyRxFFmpegSubscriber() :
    RxFFmpegSubscriber() {
    override fun onFinish() {
        Log.v("ffmpeg", "处理完成")
    }

    override fun onProgress(progress: Int, progressTime: Long) {
        Log.v("ffmpeg", "过程中onProgress")
    }

    override fun onCancel() {
        Log.v("ffmpeg", "已取消")
    }

    override fun onError(message: String) {
        Log.v("ffmpeg", "出错了:"+message)
    }

}