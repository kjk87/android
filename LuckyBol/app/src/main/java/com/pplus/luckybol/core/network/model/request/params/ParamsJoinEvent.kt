package com.pplus.luckybol.core.network.model.request.params

import com.google.gson.JsonObject
import com.pplus.luckybol.core.network.model.dto.EventBanner
import com.pplus.luckybol.core.network.model.dto.No

/**
 * Created by imac on 2018. 3. 26..
 */
class ParamsJoinEvent(var event: No? = null,
                      var properties: JsonObject? = null,
                      var banner: EventBanner? = null) {

}