package com.pplus.prnumberbiz.apps.main.ui

import android.content.Intent
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.GoodsSaleApplyPreActivity
import com.pplus.prnumberbiz.apps.post.ui.PostWriteActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import kotlinx.android.synthetic.main.activity_alert_promotion_type.*

class AlertPromotionActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_alert_promotion_type
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        text_alert_promotion_apply_sale_goods.setOnClickListener {
            val page = LoginInfoManager.getInstance().user.page!!
            if (page.isSeller == null || !page.isSeller!!) {
                val intent = Intent(this, GoodsSaleApplyPreActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivityForResult(intent, Const.REQ_APPLY)
            }
            finish()
        }

        text_alert_promotion_post_reg.setOnClickListener {

            val intent = Intent(this, PostWriteActivity::class.java)
            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, Const.REQ_REG)
            finish()
        }

        image_alert_promotion_type_close.setOnClickListener {
            finish()
        }
    }

}
