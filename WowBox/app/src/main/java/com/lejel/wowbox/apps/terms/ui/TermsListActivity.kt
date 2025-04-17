package com.lejel.wowbox.apps.terms.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.terms.data.TermsAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Terms
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.ActivityTermsListBinding
import retrofit2.Call
import java.util.Locale

class TermsListActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityTermsListBinding

    override fun getLayoutView(): View {
        binding = ActivityTermsListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: TermsAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mAdapter = TermsAdapter()
        binding.recyclerTermsList.layoutManager = LinearLayoutManager(this)
        binding.recyclerTermsList.adapter = mAdapter
        mAdapter!!.listener = object : TermsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = mAdapter!!.getItem(position)
                val intent = Intent(this@TermsListActivity, TermsActivity::class.java)
                intent.putExtra(Const.DATA, item)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }
        getTerms()
    }

    private fun getTerms() {
        val params = HashMap<String, String>()
        val country = Locale.getDefault().country
        params["nation"] = country.lowercase()
        showProgress("")
        ApiBuilder.create().getTermsList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Terms>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null) {
                    val list = response.result!!.list
                    mAdapter!!.setDataList(list as MutableList<Terms>)

                }

            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
            }

        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_terms), ToolbarOption.ToolbarMenu.LEFT)
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