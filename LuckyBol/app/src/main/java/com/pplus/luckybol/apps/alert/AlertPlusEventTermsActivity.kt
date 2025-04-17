package com.pplus.luckybol.apps.alert

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.databinding.ActivityPlusEventTermsBinding

class AlertPlusEventTermsActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPlusEventTermsBinding

    override fun getLayoutView(): View {
        binding = ActivityPlusEventTermsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val event = intent.getParcelableExtra<Event>(Const.DATA)


//        check_plus_event_terms.text = PplusCommonUtil.fromHtml(getString(R.string.html_plus_event_terms))


        binding.textPagePlusTermsCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.textPagePlusTermsAgreeJoin.setOnClickListener {
//            val isChecked = check_plus_event_terms.isChecked
//            if(!isChecked){
//                showAlert(R.string.msg_agree_terms)
//                return@setOnClickListener
//            }

            val data = Intent()
            data.putExtra(Const.DATA, event)
            data.putExtra(Const.PROPERTIES, intent.getStringExtra(Const.PROPERTIES))
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
