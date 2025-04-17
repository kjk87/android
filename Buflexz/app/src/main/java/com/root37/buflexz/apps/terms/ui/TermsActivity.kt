package com.root37.buflexz.apps.terms.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.Const
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Terms
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityTermsBinding
import retrofit2.Call

class TermsActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityTermsBinding

    override fun getLayoutView(): View {
        binding = ActivityTermsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        var terms = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Terms::class.java)!!

        showProgress("")
        ApiBuilder.create().getTerms(terms.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<Terms>>{
            override fun onResponse(call: Call<NewResultResponse<Terms>>?, response: NewResultResponse<Terms>?) {
                hideProgress()
                if(response?.result != null){
                    terms = response.result!!
                    setTitle(terms.title)
                    binding.webviewTerms.loadUrl(terms.url!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Terms>>?, t: Throwable?, response: NewResultResponse<Terms>?) {
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
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}