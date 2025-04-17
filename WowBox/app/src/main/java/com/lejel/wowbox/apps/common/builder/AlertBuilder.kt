package com.lejel.wowbox.apps.common.builder

import android.content.Context
import android.content.Intent
import com.pplus.utils.BusProvider
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.builder.data.AlertData.MessageData
import com.lejel.wowbox.apps.common.ui.AlertActivity
import com.squareup.otto.Subscribe
import java.io.Serializable

/**
 * Created by 김종경 on 2016-07-20.
 */
class AlertBuilder private constructor(title: String?, contents: ArrayList<MessageData>, leftText: String?, rightText: String?, autoCancel: Boolean, backgroundClickable: Boolean, style_alert: STYLE_ALERT, maxLine: Int, onAlertResultListener: OnAlertResultListener?) : Serializable {
    private val LOG_TAG = this.javaClass.simpleName

    enum class MESSAGE_TYPE {
        HTML, TEXT
    }

    enum class STYLE_ALERT(var value: Int) {
        MESSAGE(0), LIST_CENTER(0), LIST_BOTTOM(0), LIST_RADIO(0), LIST_RADIO_BOTTOM(0)

    }

    enum class EVENT_ALERT(var value: Int) {
        LEFT(-1), RIGHT(-2), CANCEL(-3), SINGLE(-4), LIST(0), RADIO(0)

    }

    /**
     * alert data object..
     */
    private val alertData: AlertData

    /**
     * result listener..
     */
    private val onAlertResultListener: OnAlertResultListener?

    init {
        alertData = AlertData(title, contents, leftText, rightText, autoCancel, backgroundClickable, style_alert, maxLine)
        this.onAlertResultListener = onAlertResultListener
    }

    @Subscribe
    fun result(alertResult: AlertResult) {
        BusProvider.getInstance().unregister(this)
        if (alertResult.alertData!!.identityHashCode == alertData.identityHashCode) {

            if (onAlertResultListener != null) {
                when (alertResult.event_alert) {
                    EVENT_ALERT.CANCEL -> onAlertResultListener.onCancel()
                    else -> onAlertResultListener.onResult(alertResult.event_alert)
                }
                return
            }
        } else {
            BusProvider.getInstance().register(this)
        }
    }

    fun show(context: Context) {
        val intent = Intent(context, AlertActivity::class.java)
        intent.putExtra(ALERT_KEYS, alertData)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        BusProvider.getInstance().register(this)
    }

    /**
     * @param context
     * @param isNewTask 외부에서 호출한 경우
     */
    fun show(context: Context, isNewTask: Boolean) {
        val intent = Intent(context, AlertActivity::class.java)
        intent.putExtra(ALERT_KEYS, alertData)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        BusProvider.getInstance().register(this)
    }

    /**
     * alert 빌더
     */
    class Builder {
        private var messageData: ArrayList<MessageData>

        /**
         * main title
         */
        private var title: String? = null

        /**
         * contents array
         */
        private val contents: Array<String>? = null

        /**
         * left button text
         */
        private var leftText: String? = null

        /**
         * right button text
         */
        private var rightText: String? = null

        /**
         * auto cancel
         */
        private var autoCancel = true

        /**
         * backgroundClickable
         */
        private var backgroundClickable = true

        /**
         * result listener..
         */
        private var onAlertResultListener: OnAlertResultListener? = null

        /**
         * alert style..
         * <pre>
         * 기본 스타일은 Message type
        </pre> *
         */
        private var style_alert = STYLE_ALERT.MESSAGE

        /**
         * 기본적으로 표시되는 라인의 수 default 2line..
         */
        private var maxLine = 2

        init {
            messageData = arrayListOf()
        }

        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        fun setContents(vararg contents: String): Builder {
            val result: Array<out String> = contents
            messageData = arrayListOf()
            for (i in contents.indices) {
                val message = MessageData(result[i], MESSAGE_TYPE.TEXT, maxLine)
                messageData.add(message)
            }
            return this
        }

        fun setContents(contentList: List<String>): Builder {
            messageData = arrayListOf()
            for (i in contentList.indices) {
                val message = MessageData(contentList[i], MESSAGE_TYPE.TEXT, maxLine)
                messageData.add(message)
            }
            return this
        }

        fun addContents(contents: MessageData): Builder {
            messageData.add(contents)
            return this
        }

        fun setLeftText(leftText: String?): Builder {
            this.leftText = leftText
            return this
        }

        fun setRightText(rightText: String?): Builder {
            this.rightText = rightText
            return this
        }

        fun setOnAlertResultListener(onAlertResultListener: OnAlertResultListener?): Builder {
            this.onAlertResultListener = onAlertResultListener
            return this
        }

        fun setAutoCancel(autoCancel: Boolean): Builder {
            this.autoCancel = autoCancel
            return this
        }

        fun setBackgroundClickable(backgroundClickable: Boolean): Builder {
            this.backgroundClickable = backgroundClickable
            return this
        }

        fun setStyle_alert(style_alert: STYLE_ALERT): Builder {
            this.style_alert = style_alert
            return this
        }

        fun setDefaultMaxLine(maxLine: Int): Builder {
            this.maxLine = maxLine
            return this
        }

        fun builder(): AlertBuilder {
            return AlertBuilder(title, messageData, leftText, rightText, autoCancel, backgroundClickable, style_alert, maxLine, onAlertResultListener)
        }
    }

    class AlertResult {

        var alertData: AlertData? = null
        var event_alert: EVENT_ALERT? = null
        override fun toString(): String {
            return "AlertResult{" + "alertData=" + alertData + ", event_alert=" + event_alert + '}'
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AlertResult

            if (alertData != other.alertData) return false
            if (event_alert != other.event_alert) return false

            return true
        }

        override fun hashCode(): Int {
            var result = alertData?.hashCode() ?: 0
            result = 31 * result + (event_alert?.hashCode() ?: 0)
            return result
        }

    }

    companion object {
        const val ALERT_KEYS = "alert.builder.keys"
    }
}