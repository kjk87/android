package com.root37.buflexz.apps.luckydraw.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.root37.buflexz.Const
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.model.dto.LuckyDrawWin
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityAlertMyLuckyDrawWinBinding

class AlertMyLuckyDrawWinActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertMyLuckyDrawWinBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertMyLuckyDrawWinBinding.inflate(layoutInflater)
        return binding.root
    }

    lateinit var mLuckyDrawWin: LuckyDrawWin
    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyDrawWin = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDrawWin::class.java)!!

        binding.textMyLuckyDrawWinGiftNotice.text = mLuckyDrawWin.luckyDrawGift!!.notice
        Glide.with(this).load(mLuckyDrawWin.giftImage).apply(RequestOptions().centerCrop()).into(binding.imageMyLuckyDrawWinGift)

        binding.textAlertMyLuckyDrawWinImpression.setOnClickListener {
            val intent = Intent(this, LuckyDrawWinImpressionActivity::class.java)
            intent.putExtra(Const.DATA, mLuckyDrawWin)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            setResult(RESULT_OK)
            finish()
        }
    }
}
