package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.model.dto.AdRewardPossible
import com.pplus.luckybol.databinding.ActivityEventAlertBinding
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.util.*

class AdRewardAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityEventAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityEventAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val code = intent.getIntExtra(Const.KEY, 0)
        val adRewardPossible = intent.getParcelableExtra<AdRewardPossible>(Const.DATA)!!

        binding.imageEventAlert.setImageResource(R.drawable.ic_event_popup_character_1)

        when (code) {
            516 -> {

                if (StringUtils.isNotEmpty(adRewardPossible.adRewardDatetime)) {
                    binding.textEventAlert2.visibility = View.GONE
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(adRewardPossible.adRewardDatetime)
                    val calendar = Calendar.getInstance()
                    calendar.time = d
                    calendar.add(Calendar.SECOND, adRewardPossible.joinTerm!!)

                    val remainMillis = calendar.timeInMillis - System.currentTimeMillis()
                    val remainSecond = remainMillis / 1000
                    if (remainSecond < 60) {
                        binding.textEventAlert1.text = getString(R.string.format_joinable_remain_seconds, remainSecond.toString())
                    } else {
                        val remainMinute = remainSecond / 60
                        val second = remainSecond % 60
                        binding.textEventAlert1.text = getString(R.string.format_joinable_remain_minute, remainMinute.toString(), second.toString())
                    }
                }
            }
            662 -> {
                binding.textEventAlert1.text = getString(R.string.msg_limit_ad_reward_count)
                binding.textEventAlert2.visibility = View.GONE
            }
        }

        binding.textEventAlertConfirm.setOnClickListener {
            finish()
        }
    }
}
