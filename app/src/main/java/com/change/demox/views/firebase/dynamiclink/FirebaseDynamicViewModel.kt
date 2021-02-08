package com.change.demox.views.firebase.dynamiclink

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.change.base.BaseViewModel
import com.change.demox.BuildConfig
import com.change.demox.utils.Utils
import com.change.demox.utils.Utils.buildDynamicShortLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FirebaseDynamicViewModel : BaseViewModel() {

    private var dynamicLongLink = ""
    private var dynamicShortLink = ""
    //DYNAMIC_LINK_DEEP_FRONT是自定义的深链接网址domain,深链接必须是合法网址形势
    val DEEP_LINK_URL = BuildConfig.DYNAMIC_LINK_DEEP_FRONT+ "?invitationKey="

    /**
     * 创建动态链接
     *
     */
    fun createDynamicLink() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // create dynamic link
                buildDynamicLongLink(Uri.parse(DEEP_LINK_URL + "tagetValue"))
                buildDynamicShortLink(Uri.parse(DEEP_LINK_URL + "tagetValue"))
            }
        }
        Log.v("Link:",dynamicLongLink+",,"+dynamicShortLink)
    }

    private fun buildDynamicLongLink(deepLink: Uri) {
        dynamicLongLink = Utils.buildDynamicLongLink(deepLink)
    }

    private suspend fun buildDynamicShortLink(deepLink: Uri) {
        val result = Utils.buildDynamicShortLink(deepLink)!!
        if (result != null) {
            dynamicShortLink = result
        }
    }
}