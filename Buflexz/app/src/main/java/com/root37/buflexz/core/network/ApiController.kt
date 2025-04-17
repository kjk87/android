package com.root37.buflexz.core.network

import com.root37.buflexz.core.network.apis.*
import com.root37.buflexz.core.network.service.*
import com.pplus.networks.core.NetworkController

/**
 * Created by J2N on 16. 6. 20..
 */
object ApiController : NetworkController() {
    private var apiController: ApiController? = null

    @Synchronized
    private fun getInstance(): ApiController {
        if (apiController == null) {
            apiController = ApiController
        }
        return apiController!!
    }

    val apiService: ApiService
        get() = getInstance().getService(ApiService::class.java)

    val cdnService: CdnService
        get() = getInstance().getService(CdnService::class.java)
    /**
     * P+ Api를 반환합니다.
     */
    val api: IApi
        get() = apiService.api

    val cdnApi: ICdnApi
        get() = cdnService.api

}