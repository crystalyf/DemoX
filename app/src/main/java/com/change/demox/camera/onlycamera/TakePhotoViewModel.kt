package com.change.demox.camera.onlycamera

import androidx.lifecycle.ViewModel


open class TakePhotoViewModel() : ViewModel() {

    enum class TAKEPHOTOGRAPH {
        HEAD_PORTRAIT,
        PRODUCT_INPUT
    }
}