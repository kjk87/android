package com.lejel.wowbox.apps.giftcard.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.giftcard.data.GiftCardPurchaseAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.GiftCardPurchase
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ActivityGiftCardPurchaseHistoryBinding
import retrofit2.Call

class GiftCardPurchaseHistoryActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityGiftCardPurchaseHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityGiftCardPurchaseHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: GiftCardPurchaseAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1
    private var mDir = "ASC"

    override fun initializeView(savedInstanceState: Bundle?) {
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerGiftCardPurchaseHistory.layoutManager = mLayoutManager
        mAdapter = GiftCardPurchaseAdapter()
        binding.recyclerGiftCardPurchaseHistory.adapter = mAdapter

        binding.recyclerGiftCardPurchaseHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.listener = object : GiftCardPurchaseAdapter.OnItemClickListener {
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
        ApiBuilder.create().getGiftCardPurchaseList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<GiftCardPurchase>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<GiftCardPurchase>>>?, response: NewResultResponse<ListResultResponse<GiftCardPurchase>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if(mTotalCount == 0){
                            binding.textGiftCardPurchaseHistoryNotExist.visibility = View.VISIBLE
                        }else{
                            binding.textGiftCardPurchaseHistoryNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<GiftCardPurchase>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<GiftCardPurchase>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_gift_shop_history), ToolbarOption.ToolbarMenu.LEFT)
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