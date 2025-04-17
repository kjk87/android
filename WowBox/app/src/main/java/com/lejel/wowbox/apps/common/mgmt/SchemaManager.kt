package com.lejel.wowbox.apps.common.mgmt

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.luckybox.ui.LuckyBoxContainerActivity
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.push.PushReceiveData
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * Created by ksh on 2016-10-18.
 */
class SchemaManager {
    fun moveToSchema(type1: String?, type2: String?, moveTarget: String?, moveTargetString: String?) {

        var intent: Intent? = null // 내부로 연결시
        if (type1 == "inner") {
            if (type2 != null) {
                when (type2) {
                    "main" -> {}
                    "luckyboxPurchase" -> {
                        if (!PplusCommonUtil.loginCheck((mContext as FragmentActivity), null)) {
                            return
                        }

                        intent = Intent(mContext, LuckyBoxContainerActivity::class.java)
                        intent.putExtra(Const.TAB, 0)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    }
                    "luckyboxDeliveryPurchase"->{
                        if (!PplusCommonUtil.loginCheck((mContext as FragmentActivity), null)) {
                            return
                        }

                        intent = Intent(mContext, LuckyBoxContainerActivity::class.java)
                        intent.putExtra(Const.TAB, 1)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    }
                }
            }
        } else {
            if (StringUtils.isNotEmpty(moveTargetString)) {
                PplusCommonUtil.openChromeWebView(mContext!!, moveTargetString!!)
            }
        }
    }

    fun moveToSchema(url: String?, isPush: Boolean) {
        if (url == null){
            return
        }
        LogUtil.d(TAG, "url = $url")
        var resultData: Map<String, MutableList<String>>? = null
        if (url.contains(SCHEMA)) {
            try {
                resultData = splitQuery(url)
            } catch (e: UnsupportedEncodingException) {
                LogUtil.e(TAG, e.toString())
            }
            var type1: String? = null
            var type2: String? = null
            var moveTarget: String? = null
            var moveTargetString: String? = null
            if (resultData != null && resultData[SCHEMA_MOVE_TYPE1] != null && resultData[SCHEMA_MOVE_TYPE1]!!.size > 0) {
                type1 = resultData[SCHEMA_MOVE_TYPE1]!![0]
            }
            if (resultData != null && resultData[SCHEMA_MOVE_TYPE2] != null && resultData[SCHEMA_MOVE_TYPE2]!!.size > 0) {
                type2 = resultData[SCHEMA_MOVE_TYPE2]!![0]
            }
            if (resultData != null && resultData[SCHEMA_MOVE_TARGET] != null && resultData[SCHEMA_MOVE_TARGET]!!.size > 0) {
                moveTarget = resultData[SCHEMA_MOVE_TARGET]!![0]
            }
            if (resultData != null && resultData[SCHEMA_MOVE_TARGET_STRING] != null && resultData[SCHEMA_MOVE_TARGET_STRING]!!.size > 0) {
                moveTargetString = resultData[SCHEMA_MOVE_TARGET_STRING]!![0]
            }
            if (StringUtils.isNotEmpty(type1) && StringUtils.isNotEmpty(type2)) {
                moveToSchema(type1, type2, moveTarget, moveTargetString)
            }
        }
    }

    fun setSchemaData(data: PushReceiveData?): String? {
        if (data != null) {
            val builder = StringBuilder()
            builder.append(SCHEMA)
            if (data.moveType1 != null) {
                builder.append(SCHEMA_MOVE_TYPE1 + "=" + data.moveType1)
            }
            if (data.moveType2 != null) {
                builder.append("&" + SCHEMA_MOVE_TYPE2 + "=" + data.moveType2)
            }
            if (data.moveTarget != null) {
                builder.append("&" + SCHEMA_MOVE_TARGET + "=" + data.moveTarget)
            }
            if (data.move_target_string != null) {
                builder.append("&" + SCHEMA_MOVE_TARGET_STRING + "=" + data.move_target_string)
            }
            if (data.msgNo != null) {
                builder.append("&" + MSG_NO + "=" + data.msgNo)
            }
            return builder.toString()
        }
        return null
    }

    companion object {
        private val TAG = SchemaManager::class.java.simpleName
        private var mSchemaManager: SchemaManager? = null
        private var mContext: Context? = null
        const val SCHEMA = "cashpick://"
        const val SCHEMA_PPLUS = "pplus://qr"
        const val SCHEMA_MOVE_TYPE1 = "moveType1"
        const val SCHEMA_MOVE_TYPE2 = "moveType2"
        const val SCHEMA_MOVE_TARGET = "moveTarget"
        const val SCHEMA_MOVE_TARGET_STRING = "moveTargetString"
        const val MSG_NO = "msgNo"
        fun getInstance(context: Context): SchemaManager {
            if (mSchemaManager == null) {
                mSchemaManager = SchemaManager()
            }
            mContext = context
            return mSchemaManager!!
        }

        @Throws(UnsupportedEncodingException::class)
        fun splitQuery(url: String): MutableMap<String, MutableList<String>> {
            val params: MutableMap<String, MutableList<String>> = HashMap()
            val urlParts = url.split("\\?").toTypedArray()
            var query: String? = null
            if (urlParts.size > 1) {
                query = urlParts[1]
                for (param in query.split("&").toTypedArray()) {
                    val pair = param.split("=").toTypedArray()
                    val key = URLDecoder.decode(pair[0], "UTF-8")
                    var value = ""
                    if (pair.size > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8")
                    }
                    var values = params[key]
                    if (values == null) {
                        values = ArrayList()
                        params[key] = values
                    }
                    values.add(value)
                }
            } else {
                query = if (url.contains(SCHEMA)) {
                    url.replace(SCHEMA.toRegex(), "")
                } else {
                    url
                }
            }
            for (param in query.split("&").toTypedArray()) {
                val pair = param.split("=").toTypedArray()
                val key = URLDecoder.decode(pair[0], "UTF-8")
                var value = ""
                if (pair.size > 1) {
                    value = URLDecoder.decode(pair[1], "UTF-8")
                }
                var values = params[key]
                if (values == null) {
                    values = ArrayList()
                    params[key] = values
                }
                values.add(value)
            }
            return params
        }
    }
}