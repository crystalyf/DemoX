package com.change.demox.camera.cameraandimageview

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.change.demox.camera.part.Media
import com.change.demox.camera.part.PhotoStatue

class CameraAndShowImageViewModel() : ViewModel() {


    //照片数据源LiveData
    var photo = MutableLiveData<Bitmap>()

    //更新photo
    fun updatePhoto(bitmap: Bitmap) {
        photo.value = bitmap
    }

}