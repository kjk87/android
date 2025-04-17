package com.lejel.wowbox.apps.withdraw.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.withdraw.data.BankAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Bank
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.ActivityBankListBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

class BankListActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBankListBinding

    override fun getLayoutView(): View {
        binding = ActivityBankListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }
    private var mBankList: List<Bank>? = null
    private var mAdapter: BankAdapter? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        binding.editBankListSearch.setSingleLine()
        binding.recyclerBankList.layoutManager = LinearLayoutManager(this)
        mAdapter = BankAdapter()
        binding.recyclerBankList.adapter = mAdapter
        binding.editBankListSearch.addTextChangedListener {
            if(it.toString().trim().isNotEmpty()){
                val search = it.toString().replace(" ", "")
                val list = mBankList!!.filter {
                    it.name!!.replace(" ", "").contains(search.uppercase())
                }

                mAdapter!!.setDataList(list as MutableList<Bank>)
            }else{
                mAdapter!!.setDataList(mBankList as MutableList<Bank>)
            }
        }

        mAdapter!!.listener = object : BankAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val bank = mAdapter!!.getItem(position)
                val data = Intent()
                data.putExtra(Const.DATA, bank)
                setResult(RESULT_OK, data)
                finish()
            }
        }

        getBankList()
    }

    private fun getBankList(){
        showProgress("")
        ApiBuilder.create().getBankList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Bank>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Bank>>>?, response: NewResultResponse<ListResultResponse<Bank>>?) {
                hideProgress()
                if(response?.result != null){
                    mBankList = response.result!!.list!!
                    mAdapter!!.setDataList(mBankList as MutableList<Bank>)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Bank>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Bank>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_bank_select_list), ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}