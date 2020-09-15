package com.change.demox.views.recyclerview.bean

import java.io.Serializable

class WirelessTransformModel:  Serializable {
    var TYPE_TITLE = Int.MAX_VALUE - 1
    var TYPE_CONTENT = Int.MAX_VALUE

    var TYPE_HEADER = 1
    var TYPE_FOOTER = 2

    /**
     * The index for item sorting.
     */
    var index: String? = null

    /**
     * The title will be show at section bar for item.
     */
    var indexTitle: String? = null
    var pinyin: String? = null
    var kanaField: String = ""
    var originalPosition = -1
    var itemType = TYPE_CONTENT
    var headerFooterType = 0
    var data: WirelessContactModel? = null


}