package com.pplus.prnumberbiz.apps.marketing.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_target_marketing.*

class TargetMarketingActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_target_marketing
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        appbar_target_marketing.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (verticalOffset <= -collapsing_target_marketing.height + toolbar_target_marketing.height) {
                    //toolbar is collapsed here
                    //write your code here
                    image_target_marketing_back.setImageResource(R.drawable.ic_top_prev)
//                    text_biz_main_user_mode.visibility = View.GONE
                } else {
                    image_target_marketing_back.setImageResource(R.drawable.ic_top_prev_white)
//                    text_biz_main_user_mode.visibility = View.VISIBLE
                }
            }
        })

        image_target_marketing_back.setOnClickListener {
            onBackPressed()
        }
    }

}
