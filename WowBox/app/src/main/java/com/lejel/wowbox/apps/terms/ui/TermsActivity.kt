package com.lejel.wowbox.apps.terms.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Terms
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityTermsBinding
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