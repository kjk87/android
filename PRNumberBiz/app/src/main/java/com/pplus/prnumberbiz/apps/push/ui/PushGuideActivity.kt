package com.pplus.prnumberbiz.apps.push.ui

import android.os.Bundle
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_push_guide.*

class PushGuideActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }


    override fun getLayoutRes(): Int {
        return R.layout.activity_push_guide
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        var key = intent.getStringExtra(Const.KEY)
        if(key == Const.GUIDE_PUSH) {
            image_push_guide.setImageResource(R.drawable.ic_guide_push)
            text_push_guide1.setText(R.string.msg_push_send_guide1)
            text_push_guide2.setText(R.string.msg_push_send_guide2)
            text_push_guide3.setText(R.string.msg_push_send_guide3)
        } else if (key == Const.GUIDE_SMS) {
            image_push_guide.setImageResource(R.drawable.ic_guide_message)
            text_push_guide1.setText(R.string.msg_sms_send_guide1)
            text_push_guide2.setText(R.string.msg_sms_send_guide2)
            text_push_guide3.setText(R.string.msg_sms_send_guide3)
        }else if(key ==  Const.GUIDE_BOL){
            image_push_guide.setImageResource(R.drawable.ic_guide_point)
            text_push_guide1.setText(R.string.msg_point_guide1)
            text_push_guide2.setText(R.string.msg_point_guide2)
            text_push_guide3.setText(R.string.msg_point_guide3)
        }

        check_push_guide.setOnCheckedChangeListener { buttonView, isChecked ->
            PreferenceUtil.getDefaultPreference(this).put(key, isChecked)
        }

        text_push_guide_confirm.setOnClickListener {
            finish()
        }
    }
}
