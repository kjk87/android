package com.pplus.prnumberuser.apps.goods.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.product.ui.ProductLikeActivity
import com.pplus.prnumberuser.databinding.ActivityAlertGoodsLikeDownloadCompleteBinding

class AlertGoodsLikeCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertGoodsLikeDownloadCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertGoodsLikeDownloadCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.imageAlertGoodsLikeCompleteConfirm.setOnClickListener {
            finish()
        }

        binding.imageAlertGoodsLikeCompleteGo.setOnClickListener {
            val intent = Intent(this, ProductLikeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

    }

}
