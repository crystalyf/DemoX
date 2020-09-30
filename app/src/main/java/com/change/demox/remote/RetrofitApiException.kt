package com.change.demox.remote

import com.change.demox.remote.bean.ApiErrorModel

class RetrofitApiException(
    var errorModel: ApiErrorModel
) : Exception()