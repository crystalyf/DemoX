package com.change.demox.camera.albumandcamera

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.change.demox.camera.part.Media
import com.change.demox.camera.part.PhotoStatue

class CameraAndShowViewModel() : ViewModel() {

    enum class TAKEPHOTOGRAPH {
        HEAD_PORTRAIT,
        PRODUCT_INPUT
    }

    //照片数据源LiveData
    var photos = MutableLiveData<MutableList<Media>>()


    /**
     * 初始化制造数据源
     */
    fun makeData(): MutableList<Media>?{
       photos.value = getPhotoImageList()
        return photos.value
    }

    /**
     * 构造照片数据的数据源
     */
    private fun getPhotoImageList(): MutableList<Media> {
        return Media().createData()
    }

    /**
     * 更新数据源
     */
    fun updatePhotoList(uri: String?) {
        if (photos.value == null) return
        for (index in 0 until  photos.value?.size!!) {
            if ( photos.value!![index].uri == null || photos.value!![index].uri.toString().isEmpty()){
                photos.value!![index].uri = uri
                photos.value!![index].photoStatue = PhotoStatue.TAKE_FINISH.ordinal
                if (index < photos.value!!.size -1) {
                    photos.value!![index + 1].photoStatue = PhotoStatue.TAKE_CAMERA.ordinal
                }
                break
            }
        }
        photos.postValue(photos.value)
    }

}