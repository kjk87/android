package com.root37.buflexz.apps.product.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.product.data.ProductPurchaseAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.ProductPurchase
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.ToastUtil
import com.root37.buflexz.databinding.ActivityProductPurchaseHistoryBinding
import retrofit2.Call

class ProductPurchaseHistoryActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProductPurchaseHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityProductPurchaseHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: ProductPurchaseAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1

    override fun initializeView(savedInstanceState: Bundle?) {
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerProductPurchaseHistory.layoutManager = mLayoutManager
        mAdapter = ProductPurchaseAdapter()
        binding.recyclerProductPurchaseHistory.adapter = mAdapter

        binding.recyclerProductPurchaseHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.listener = object : ProductPurchaseAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

            }
        }

        binding.layoutContactUs.textContactUsEmailCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText(getString(R.string.word_contact_us_en), getString(R.string.cs_email))
            clipboard.setPrimaryClip(clip)
            ToastUtil.show(this, R.string.msg_copied_clipboard)
        }

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getProductPurchaseList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<ProductPurchase>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<ProductPurchase>>>?, response: NewResultResponse<ListResultResponse<ProductPurchase>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if(mTotalCount == 0){
                            binding.textProductPurchaseHistoryNotExist.visibility = View.VISIBLE
                        }else{
                            binding.textProductPurchaseHistoryNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<ProductPurchase>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<ProductPurchase>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods_shop_history), ToolbarOption.ToolbarMenu.LEFT)
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