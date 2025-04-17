package com.pplus.prnumberbiz.apps.main.ui

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_background_image_detail.*

class BackgroundImageDetailActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_background_image_detail
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val url = intent.getStringExtra(Const.URL)

        image_background_image_close.setOnClickListener {
            finish()
        }

        Glide.with(this).load(url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_background_image_detail)
    }

}
