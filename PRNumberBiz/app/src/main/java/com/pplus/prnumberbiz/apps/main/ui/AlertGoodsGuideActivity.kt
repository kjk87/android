package com.pplus.prnumberbiz.apps.main.ui

import android.app.Activity
import android.os.Bundle
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import kotlinx.android.synthetic.main.activity_goods_guide.*

class AlertGoodsGuideActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_guide
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val page = LoginInfoManager.getInstance().user.page!!

        if (page.type == EnumData.PageTypeCode.store.name) {
            text_goods_guide_title.setText(R.string.word_menu_guide_title)
            text_goods_guide_desc1.setText(R.string.word_menu_guide_desc1)
            text_goods_guide_desc2.setText(R.string.word_menu_guide_desc2)
            text_goods_guide_reg.setText(R.string.msg_reg_menu)
        } else {
            text_goods_guide_title.setText(R.string.word_store_guide_title)
            text_goods_guide_desc1.setText(R.string.word_store_guide_desc1)
            text_goods_guide_desc2.setText(R.string.word_store_guide_desc2)
            text_goods_guide_reg.setText(R.string.msg_reg_goods)
        }

        check_goods_guide.setOnCheckedChangeListener { buttonView, isChecked ->
            PreferenceUtil.getDefaultPreference(this).put(Const.ALERT_GOODS, isChecked)
        }

        text_goods_guide_reg.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
        image_goods_guide_close.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

}
