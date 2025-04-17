package com.pplus.prnumberuser.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityEventDetailPersonalTermsBinding
import retrofit2.Call
import java.util.*

class EventDetailPersonalTermsActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityEventDetailPersonalTermsBinding

    override fun getLayoutView(): View {
        binding = ActivityEventDetailPersonalTermsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val event = intent.getParcelableExtra<Event>(Const.DATA)

        val params = HashMap<String, String>()
        params["no"] = event!!.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params)
            .setCallback(object : PplusCallback<NewResultResponse<Event>> {
                override fun onResponse(
                    call: Call<NewResultResponse<Event>>?,
                    response: NewResultResponse<Event>?
                ) {
                    hideProgress()
                    val event = response!!.data
                    setTitle(event.personalTitle)
                    binding.textEventDetailPersonalTerms.text = event.personalContents
                }

                override fun onFailure(
                    call: Call<NewResultResponse<Event>>?,
                    t: Throwable?,
                    response: NewResultResponse<Event>?
                ) {
                    hideProgress()
                }
            }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}