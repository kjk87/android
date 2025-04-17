package com.pplus.prnumberuser.core.network.service

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.pplus.prnumberuser.core.util.DebugConfig
import com.pplus.utils.part.logs.LogUtil
import java.io.IOException

/**
 * Created by J2N on 16. 6. 20..
 */
class GsonTypeAdapterFactory(serviceClz: Class<*>) : TypeAdapterFactory {
    private val LOG_TAG = serviceClz.simpleName

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)
        val elementAdapter = gson.getAdapter(JsonElement::class.java)
        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter?, value: T) {
                delegate.write(out, value)
            }

            override fun read(`in`: JsonReader?): T {
                val jsonElement = elementAdapter.read(`in`)

                if (DebugConfig.isDebugMode()) {
                    if (jsonElement.isJsonObject) {
                        val jsonObject = jsonElement.asJsonObject
                        if (jsonObject.isJsonObject) {
                            if (jsonObject.has("row") || jsonObject.has("rows")) {
                                if (!jsonObject.asJsonObject.isJsonNull) { // job.. logic
                                    LogUtil.e(LOG_TAG, "response result = {}", jsonObject.asJsonObject)
                                }
                            } else if (jsonObject.has("resultCode")) {
                                LogUtil.e(LOG_TAG, "response error result = {}", jsonObject.asJsonObject)
                            } else if (jsonObject.has("result")) {
                                LogUtil.e(LOG_TAG, "cs result = {}", jsonObject.asJsonObject)
                            }
                        }
                    }
                }
                return delegate.fromJsonTree(jsonElement)
            }
        }.nullSafe()
    }

}