package com.root37.buflexz.apps.common.builder.data

import android.os.Parcelable
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.apps.common.builder.AlertBuilder.MESSAGE_TYPE
import com.root37.buflexz.apps.common.builder.AlertBuilder.STYLE_ALERT
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.Arrays

/**
 * Created by j2n on 2016. 8. 22..
 */

@Parcelize
class AlertData(var title: String?,
                var contents: ArrayList<MessageData>,
                var leftText: String?,
                var rightText: String?,
                var autoCancel: Boolean?,
                var backgroundClickable: Boolean?,
                var style_alert: STYLE_ALERT?,
                var maxLine: Int,
                var identityHashCode: Int = System.identityHashCode(this)) : Parcelable {
    private val LOG_TAG = this.javaClass.simpleName

    /**
     * auto cancel
     */
    var isAutoCancel = false

    /**
     * auto cancel
     */
    var isBackgroundClickable = false


    init {

        isAutoCancel = (autoCancel != null && autoCancel!!)
        isBackgroundClickable = (backgroundClickable != null && backgroundClickable!!)
    }


    fun isSingle(): Boolean {
        return StringUtils.isEmpty(leftText) || StringUtils.isEmpty(rightText)
    }



    class MessageData : Serializable {
        private val LOG_TAG = this.javaClass.simpleName

        constructor()

        var contents: String? = null
        var message_type = MESSAGE_TYPE.TEXT
        var maxLine = 3

        constructor(contents: String?) {
            this.contents = contents
        }

        constructor(contents: String?, message_type: MESSAGE_TYPE) {
            this.contents = contents
            this.message_type = message_type
        }

        constructor(contents: String?, message_type: MESSAGE_TYPE, maxLine: Int) {
            this.contents = contents
            this.message_type = message_type
            this.maxLine = maxLine
        }

        override fun toString(): String {
            return "MessageData{" + "contents='" + contents + '\'' + ", message_type=" + message_type + ", maxLine=" + maxLine + '}'
        }

        companion object {
            private const val serialVersionUID = 6943399599090986268L
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val alertData = o as AlertData
        if (isAutoCancel != alertData.isAutoCancel) return false
        if (maxLine != alertData.maxLine) return false
        if (if (title != null) title != alertData.title else alertData.title != null) return false // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return if (contents != alertData.contents) false else style_alert == alertData.style_alert
    }

    override fun hashCode(): Int {
        var result = if (title != null) title.hashCode() else 0
        result = 31 * result + contents.hashCode()
        result = 31 * result + if (isAutoCancel) 1 else 0
        result = 31 * result + maxLine
        result = 31 * result + if (style_alert != null) style_alert.hashCode() else 0
        return result
    }

    override fun toString(): String {
        return "AlertData(title=$title, contents=$contents, leftText=$leftText, rightText=$rightText, autoCancel=$autoCancel, backgroundClickable=$backgroundClickable, style_alert=$style_alert, maxLine=$maxLine, LOG_TAG='$LOG_TAG', identityHashCode=$identityHashCode, isAutoCancel=$isAutoCancel, isBackgroundClickable=$isBackgroundClickable)"
    }

    companion object {
        private const val serialVersionUID = 5076033928181479319L
    }
}