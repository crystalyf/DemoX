package com.change.demox.camera.part

/**
 * 拍照完照片的Model
 */
data class Media(
        var uri: String? = null,
        val isVideo: Boolean = false,
        val date: Long? = 11212,
        var photoStatue: Int = PhotoStatue.TAKE_EMPTY.ordinal
) {
    //for save image id
    var id: Long? = null

    // TODO 构造数据源，限制了造5个
    fun createData(): MutableList<Media> {
        var list = mutableListOf<Media>()
        val a = Media(photoStatue = PhotoStatue.TAKE_CAMERA.ordinal)
        val b = Media()
        val c = Media()
        val d = Media()
        val e = Media()
        list.add(a)
        list.add(b)
        list.add(c)
        list.add(d)
        list.add(e)
        return list
    }
}

enum class PhotoStatue(typeId: Int) {
    TAKE_FINISH(0),
    TAKE_CAMERA(1),
    TAKE_EMPTY(2)
}



