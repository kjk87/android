package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventBuy
import com.pplus.luckybol.databinding.ActivityEventLoseAlertBinding
import com.pplus.utils.part.format.FormatUtil

class EventLoseAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityEventLoseAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityEventLoseAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val eventBuy = intent.getParcelableExtra<EventBuy>(Const.EVENT_BUY)!!
        val event = intent.getParcelableExtra<Event>(Const.EVENT)!!

        if(event.primaryType == EventType.PrimaryType.randomluck.name){
            binding.textEventLoseAlertDesc.text =  getString(R.string.msg_event_lose_desc)
        }else if(event.primaryType == EventType.PrimaryType.goodluck.name){
            binding.textEventLoseAlertDesc.text =  getString(R.string.msg_event_lose_desc2)
        }

        if(event.earnedPoint != null){
            binding.textEventLoseAlertRewardPoint.text = getString(R.string.format_bol_unit, FormatUtil.getMoneyTypeFloat((event.earnedPoint!!*eventBuy.count!!).toString()))
        }

        binding.textEventLoseAlertClose.setOnClickListener {
            finish()
        }

//        text_event_lose_alert_giftishow.setOnClickListener {
//            val intent = Intent(this, MobileGiftActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//            finish()
//        }
    }
}
