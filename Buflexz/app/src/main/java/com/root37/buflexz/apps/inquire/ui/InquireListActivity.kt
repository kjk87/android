package com.root37.buflexz.apps.inquire.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.inquire.data.InquireAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Inquire
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.databinding.ActivityInquireListBinding
import retrofit2.Call

class InquireListActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityInquireListBinding

    override fun getLayoutView(): View {
        binding = ActivityInquireListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: InquireAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1

    override fun initializeView(savedInstanceState: Bundle?) {
        mAdapter = InquireAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerInquireList.adapter = mAdapter
        binding.recyclerInquireList.layoutManager = mLayoutManager

        binding.recyclerInquireList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        getList(mPaging)
                    }
                }
            }
        })

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"

        showProgress("")
        mLockListView = true
        ApiBuilder.create().getInquireList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Inquire>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Inquire>>>?, response: NewResultResponse<ListResultResponse<Inquire>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        binding.textInquireListCount.text = getString(R.string.format_inquire_count, FormatUtil.getMoneyType(mTotalCount.toString()))
                        if(mTotalCount == 0){
                            binding.layoutInquireListNotExist.visibility = View.VISIBLE
                        }else{
                            binding.layoutInquireListNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Inquire>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Inquire>>?) {
                hideProgress()
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 1
        getList(mPaging)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_inquiry_history), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.msg_inquiry))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        val intent = Intent(this@InquireListActivity, InquireActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                    }

                    else -> {}
                }
            }
        }
    }
}