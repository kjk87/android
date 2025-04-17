package com.pplus.luckybol.apps.buff.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityAlertBuffTimeLimitBinding

class AlertBuffTimeLimitActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertBuffTimeLimitBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertBuffTimeLimitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val remainSecond = intent.getIntExtra(Const.REMAIN_SECOND, 0)

        binding.textAlertBuffTimeLimitConfirm.setOnClickListener {
            finish()
        }
        if(remainSecond == 0){
            binding.textAlertBuffTimeLimitRemainTime.visibility = View.GONE
        }else{
            binding.textAlertBuffTimeLimitRemainTime.visibility = View.VISIBLE

            if (remainSecond < 60) {
                binding.textAlertBuffTimeLimitRemainTime.text = getString(R.string.format_enable_buff_join_remain_time, "${remainSecond}${getString(R.string.word_second)}")
            } else if (remainSecond < 3600) {
                val remainMinute = remainSecond / 60
                binding.textAlertBuffTimeLimitRemainTime.text = getString(R.string.format_enable_buff_join_remain_time, "${remainMinute}${getString(R.string.word_minute)}")
            }else{
                val remainHour = remainSecond / 60 / 60
                binding.textAlertBuffTimeLimitRemainTime.text = getString(R.string.format_enable_buff_join_remain_time, "${remainHour}${getString(R.string.word_hour)}")
            }
        }
    }


}
