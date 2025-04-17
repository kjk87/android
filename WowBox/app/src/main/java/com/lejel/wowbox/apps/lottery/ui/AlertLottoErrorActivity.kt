package com.lejel.wowbox.apps.lottery.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.pplus.utils.part.resource.ResourceUtil
import com.lejel.wowbox.databinding.ActivityLottoErrorAlertBinding

class AlertLottoErrorActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLottoErrorAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityLottoErrorAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val key = intent.getStringExtra(Const.KEY)

        binding.textLottoErrorAlert2.visibility = View.VISIBLE
        when(key){
            "time"->{
                val remainSecond = intent.getIntExtra(Const.REMAIN_SECOND, 0)
                if (remainSecond < 60) {
                    binding.textLottoErrorAlert1.text = getString(R.string.format_joinable_remain_seconds, remainSecond.toString())
                } else {
                    val remainMinute = remainSecond / 60
                    val second = remainSecond % 60
                    binding.textLottoErrorAlert1.text = getString(R.string.format_joinable_remain_minute, remainMinute.toString(), second.toString())
                }
                binding.textLottoErrorAlert2.text = getString(R.string.msg_minute_join_desc)
                binding.textLottoErrorAlert2.setTextColor(ResourceUtil.getColor(this, R.color.color_ff5e5e))
            }
        }


        binding.textLottoErrorAlertConfirm.setOnClickListener {
            finish()
        }
    }


}
