package com.root37.buflexz.core.network.service

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.pplus.utils.part.logs.LogUtil
import com.root37.buflexz.Const
import java.io.IOException

/**
 * Created by J2N on 16. 6. 20..
 */
class GsonTypeAdapterFactory(serviceClz: Class<*>) : TypeAdapterFactory {
    private val LOG_TAG: String

    init {
        LOG_TAG = serviceClz.simpleName
    }

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)
        val elementAdapter = gson.getAdapter(JsonElement::class.java)
        return object : TypeAdapter<T>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T) {
                delegate.write(out, value)
            }

            @Throws(IOException::class)
            override fun read(jsonReader: JsonReader): T {
                val jsonElement = elementAdapter.read(jsonReader)

                if (Const.DEBUG_MODE) {
                    if (jsonElement.isJsonObject) {
                        val jsonObject = jsonElement.asJsonObject
                        if (jsonObject.isJsonObject) {
                            if (jsonObject.has("result")) {
                                val result = jsonObject.asJsonObject
                                if (!result.isJsonNull) { // job.. logic
                                    LogUtil.e(LOG_TAG, "response result = {}", result)
                                }
                            } else if (jsonObject.has("code")) {
                                LogUtil.e(LOG_TAG, "response result = {}", jsonObject.asJsonObject)
                            }
                        }
                    }
                }
                return delegate.fromJsonTree(jsonElement)
            }
        }.nullSafe()
    }
}