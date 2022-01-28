package com.change.demox.views.imageview.imagetovideo.rxffmpeg

import android.util.Log
import io.microshow.rxffmpeg.RxFFmpegSubscriber

/**
 *  RxFFmpeg 的观察者
 */
class MyRxFFmpegSubscriber( private var viewModel: ImageToVideoViewModel) :
    RxFFmpegSubscriber() {

    override fun onFinish() {
        Log.v("ffmpegLog", "处理完成")
        viewModel.afterTempVideo()
    }

    override fun onProgress(progress: Int, progressTime: Long) {
        Log.v("ffmpegLog", "过程中onProgress")
    }

    override fun onCancel() {
        Log.v("ffmpegLog", "已取消")
    }

    override fun onError(message: String) {
        Log.v("ffmpegLog", "出错了:"+message)
    }

}