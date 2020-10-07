package com.change.demox.remote

sealed class ApiConfigRetrofit {

    companion object {
        const val PATH_BOOKS = "getBookList"


        const val CONTENT_TYPE_JSON = "Content-Type: x-www-form-urlencoded"
        // HEADER
        const val HEADER_KEY_AUTHORIZATION = "Authorization"
        const val HEADER_KEY_AUTHORIZATION_BEARER = "Bearer "
        const val HEADER_KEY_ROLE_ID = "KubotaPad-Role-ID"
        const val HEADER_KEY_LANGUAGE_CODE = "KubotaPad-Language-Code"
        const val HEADER_KEY_LAST_MODIFIED = "Last-Modified"
        const val HEADER_KEY_CONTENT_LENGTH = "Content-Length"

        //ERROR CODE
        const val NETWORK_ERROR = "NETWORK_ERROR"
    }
}