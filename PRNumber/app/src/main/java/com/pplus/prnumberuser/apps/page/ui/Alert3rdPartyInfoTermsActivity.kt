package com.pplus.prnumberuser.apps.page.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventPolicy
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityAlert3rdPartyInfoTermsBinding
import com.pplus.prnumberuser.databinding.ItemEventTermsBinding
import retrofit2.Call
import java.util.*

class Alert3rdPartyInfoTermsActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlert3rdPartyInfoTermsBinding

    override fun getLayoutView(): View {
        binding = ActivityAlert3rdPartyInfoTermsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val event = intent.getParcelableExtra<Event>(Const.EVENT)

        val params = HashMap<String, String>()
        params["no"] = event!!.pageSeqNo.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                hideProgress()

                if (response.data != null) {
                    val page = response.data!!
                    Glide.with(this@Alert3rdPartyInfoTermsActivity).load(page.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_square_profile_default).error(R.drawable.img_square_profile_default)).into(binding.image3rdPartyPage)
                    binding.text3rdPartyPageName.text = page.name

                    binding.text3rdPartyDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_3rd_party_terms_desc, page.name, LoginInfoManager.getInstance().user.loginId))
                    binding.text3rdPartyPolicyTitle.text = getString(R.string.format_page_service_agree, page.name)
                    getEventPolicyList(event)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable?, response: NewResultResponse<Page>) {
                hideProgress()
            }
        }).build().call()

        binding.text3rdPartyCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.text3rdPartyAgreeJoin.setOnClickListener {
            val data = Intent()
            data.putExtra(Const.DATA, event)
            data.putExtra(Const.PROPERTIES, intent.getStringExtra(Const.PROPERTIES))
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun getEventPolicyList(event:Event){
        val params = HashMap<String, String>()
        params["eventSeqNo"] = event.no.toString()
        params["pageSeqNo"] = event.pageSeqNo.toString()
        showProgress("")
        ApiBuilder.create().getEventPolicyList(params).setCallback(object : PplusCallback<NewResultResponse<EventPolicy>> {

            override fun onResponse(call: Call<NewResultResponse<EventPolicy>>, response: NewResultResponse<EventPolicy>) {
                hideProgress()

                binding.layout3rdPartyPolicyList.removeAllViews()
                if (response.datas != null) {

                    for(eventPolicy in response.datas){
                        val eventTermsBinding = ItemEventTermsBinding.inflate(layoutInflater)
                        eventTermsBinding.textEventTermsTitle.text = eventPolicy!!.title
                        eventTermsBinding.textEventTermsView.setOnClickListener {
                            PplusCommonUtil.openChromeWebView(this@Alert3rdPartyInfoTermsActivity, eventPolicy.url!!)
                        }
                        binding.layout3rdPartyPolicyList.addView(eventTermsBinding.root)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventPolicy>>, t: Throwable?, response: NewResultResponse<EventPolicy>) {
                hideProgress()
            }
        }).build().call()
    }
}
