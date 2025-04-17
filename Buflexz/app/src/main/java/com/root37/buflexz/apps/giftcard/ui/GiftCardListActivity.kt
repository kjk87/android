package com.root37.buflexz.apps.giftcard.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.giftcard.data.GiftCardAdapter
import com.root37.buflexz.apps.giftcard.data.GiftCardBrandAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.GiftCard
import com.root37.buflexz.core.network.model.dto.GiftCardBrand
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityGiftCardListBinding
import retrofit2.Call

class GiftCardListActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityGiftCardListBinding

    override fun getLayoutView(): View {
        binding = ActivityGiftCardListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: GiftCardAdapter? = null
    private lateinit var mLayoutManager: GridLayoutManager
    private var mLockListView = false
    private var mPaging = 1
    private var mDir = "ASC"

    private lateinit var mGiftCardBrand: GiftCardBrand
    override fun initializeView(savedInstanceState: Bundle?) {
        mGiftCardBrand = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, GiftCardBrand::class.java)!!
        Glide.with(this).load(mGiftCardBrand.backgroundImage).apply(RequestOptions().centerCrop()).into(binding.imageGiftCardListBrand)
        binding.textGiftCardListBrandTitle.text = mGiftCardBrand.title
        binding.textGiftCardListBrandComment.text = mGiftCardBrand.comment

        mLayoutManager = GridLayoutManager(this, 2)
        binding.recyclerGiftCardList.layoutManager = mLayoutManager
        mAdapter = GiftCardAdapter()
        binding.recyclerGiftCardList.adapter = mAdapter
        binding.recyclerGiftCardList.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.width_33, R.dimen.height_69))

        binding.recyclerGiftCardList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter?.listener = object : GiftCardAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@GiftCardListActivity, GiftCardActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter?.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        }

        binding.textGiftCardSort.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_sort_low_price), getString(R.string.word_sort_high_price))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.value) {
                        1 -> {
                            mDir = "ASC"
                            binding.textGiftCardSort.setText(R.string.word_sort_low_price)
                        }
                        2 -> {
                            binding.textGiftCardSort.setText(R.string.word_sort_high_price)
                            mDir = "DESC"
                        }
                    }
                    mPaging = 1
                    getList(mPaging)

                }
            }).builder().show(this)
        }

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["order[][column]"] = "price"
        params["order[][dir]"] = mDir
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getGiftCardList(mGiftCardBrand.seqNo!!, params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<GiftCard>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<GiftCard>>>?, response: NewResultResponse<ListResultResponse<GiftCard>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<GiftCard>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<GiftCard>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mRightOffset: Int, private val mBottomOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes rightOffsetId: Int, @DimenRes bottomOffsetId: Int) : this(context.resources.getDimensionPixelSize(rightOffsetId), context.resources.getDimensionPixelSize(bottomOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

            if (position % 2 != 1) {
                outRect.set(0, 0, mRightOffset, mBottomOffset)
            } else {
                outRect.set(0, 0, 0, mBottomOffset)
            }
        }
    }

    private val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    }


    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_gift_shop), ToolbarOption.ToolbarMenu.LEFT)
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