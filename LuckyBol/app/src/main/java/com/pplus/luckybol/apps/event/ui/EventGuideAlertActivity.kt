package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityEventGuideAlertBinding
import com.pplus.utils.part.pref.PreferenceUtil

class EventGuideAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityEventGuideAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityEventGuideAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val key = intent.getStringExtra(Const.KEY)

        when (key) {
            Const.EVENT_GUIDE -> {
                binding.textEventGuideAlertTitle.setText(R.string.msg_question_time_event)
                binding.textEventGuideAlertContents.setText(R.string.msg_time_event_description)
                binding.imageEventGuideAlert.setImageResource(R.drawable.img_popup_timeevent_guide)
            }
            Const.PLAY_GUIDE -> {
                binding.textEventGuideAlertTitle.setText(R.string.word_play_event)
                binding.textEventGuideAlertContents.setText(R.string.msg_play_event_description)
                binding.imageEventGuideAlert.setImageResource(R.drawable.img_popup_play_guide)
            }
            Const.NUMBER_EVENT_GUIDE -> {
                binding.textEventGuideAlertTitle.setText(R.string.msg_title_number_event)
                binding.textEventGuideAlertContents.setText(R.string.msg_number_event_description)
                binding.imageEventGuideAlert.setImageResource(R.drawable.img_popup_numberevent_guide)
            }
            Const.LOTTO_GUIDE -> {
                binding.textEventGuideAlertTitle.setText(R.string.word_title_lotto_event_guide)
                binding.textEventGuideAlertContents.setText(R.string.msg_lotto_event_guide_description)
                binding.imageEventGuideAlert.setImageResource(R.drawable.img_popup_lotto_guide)
            }
        }

        binding.textEventGuideAlertConfirm.setOnClickListener {
            finish()
        }

        binding.textEventGuideAlertDoNotAgain.setOnClickListener {
            PreferenceUtil.getDefaultPreference(this).put(key, false)
            finish()
        }
    }
}
