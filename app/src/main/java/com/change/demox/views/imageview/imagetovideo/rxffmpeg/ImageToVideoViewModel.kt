package com.change.demox.views.imageview.imagetovideo.rxffmpeg


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.change.demox.utils.Event


class ImageToVideoViewModel(
) : ViewModel() {

    private val _afterTempVideo = MutableLiveData<Event<Unit>>()
    val afterTempVideo: LiveData<Event<Unit>> = _afterTempVideo

    /**
     * 当图片先合成mp4之后(没有声音的mp4 : tempMp4), 接下来通知去执行第二步: 音频+mp4去合成带声音的mp4
     */
    fun afterTempVideo() {
        _afterTempVideo.value = Event(Unit)
    }
}