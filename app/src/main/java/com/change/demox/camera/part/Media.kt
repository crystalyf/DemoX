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

    // TODO make data
    fun createData(): MutableList<Media> {
        var list = mutableListOf<Media>()
        val a = Media(photoStatue = PhotoStatue.TAKE_CAMERA.ordinal)
        val b = Media()
        val c = Media()
        list.add(a)
        list.add(b)
        list.add(c)
        return list
    }

    fun createAlbumData(): MutableList<Media> {
        var list = mutableListOf<Media>()
        val a = Media()
        val b = Media()
        val c = Media()
        list.add(a)
        list.add(b)
        list.add(c)
        return list
    }

    fun clearMedia() {
        this.uri = null
    }
}

enum class PhotoStatue(typeId: Int) {
    TAKE_FINISH(0),
    TAKE_CAMERA(1),
    TAKE_EMPTY(2)
}



