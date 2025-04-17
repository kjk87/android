package com.pplus.luckybol.core.network

import com.pplus.luckybol.core.network.apis.*
import com.pplus.luckybol.core.network.service.*
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

    /**
     * PRNumber Service를 반환합니다.
     */
    @JvmStatic
    val pRNumberService: PRNumberService
        get() = getInstance().getService(PRNumberService::class.java)

    /**
     * MapService를 반환합니다.
     */
    val mapService: MapService
        get() = getInstance().getService(MapService::class.java)

    /**
     * 통계용 Service를 호출
     *
     * @return
     */
    val mobonService: MobonService
        get() = getInstance().getService(MobonService::class.java)
    val linkMineService: LinkMineService
        get() = getInstance().getService(LinkMineService::class.java)
    val cdnService: CdnService
        get() = getInstance().getService(CdnService::class.java)
    val gpaService: GpaService
        get() = getInstance().getService(GpaService::class.java)

    /**
     * P+ Api를 반환합니다.
     */
    val pRNumberApi: IPRNumberApi
        get() = pRNumberService.api

    /**
     * P+ 광고 서버 관련 API를 반환합니다.
     */
    val mobonApi: IMobonApi
        get() = mobonService.api
    val linkMineApi: ILinkMineApi
        get() = linkMineService.api
    val mapApi: IMapApi
        get() = mapService.api
    val cdnApi: ICdnApi
        get() = cdnService.api
    val gpaApi: GpaApi
        get() = gpaService.api
}