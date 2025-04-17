package com.lejel.wowbox.core.network.upload

import com.google.gson.reflect.TypeToken
import com.lejel.wowbox.Const
import com.lejel.wowbox.core.network.model.response.NewResultResponse

/**
 * Created by j2n on 2016. 9. 30..
 */
class S3Upload(callback: PplusUploadListener<String>) : AbstractUpload<String>(callback) {


    override fun getUploadUrl(): String {
        return Const.API_URL + "file/s3Upload"
    }

    override fun isMultiThreadEnable(): Boolean {
        return true
    }

    override fun getResultType(): TypeToken<NewResultResponse<String>> {
        return object : TypeToken<NewResultResponse<String>>() {}
    }
}