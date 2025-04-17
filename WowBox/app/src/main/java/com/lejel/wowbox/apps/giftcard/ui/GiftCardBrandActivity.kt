package com.lejel.wowbox.apps.giftcard.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.mgmt.NationManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.giftcard.data.GiftCardBrandAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.GiftCardBrand
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityGiftCardBrandBinding
import com.lejel.wowbox.databinding.ItemTopRightBinding
import retrofit2.Call

class GiftCardBrandActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityGiftCardBrandBinding

    override fun getLayoutView(): View {
        binding = ActivityGiftCardBrandBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mAdapter: GiftCardBrandAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mAdapter = GiftCardBrandAdapter()
        binding.recyclerGiftCardBrand.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerGiftCardBrand.adapter = mAdapter
        binding.recyclerGiftCardBrand.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_27))

        mAdapter?.listener = object : GiftCardBrandAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@GiftCardBrandActivity, GiftCardListActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter?.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        }

        loginCheck()
        getBrandList()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

            if (position % 3 != 2) {
                outRect.set(0, 0, mItemOffset, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }
        }
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
            binding.layoutGiftCardBrandMember.visibility = View.VISIBLE
            Glide.with(this).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(binding.imageGiftCardBrandProfile)
            binding.textGiftCardBrandNickname.text = LoginInfoManager.getInstance().member!!.nickname

            reloadSession()
        } else {
            binding.layoutGiftCardBrandMember.visibility = View.GONE
        }
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textGiftCardBrandRetentionPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString()))
            }
        })
    }

    private fun getBrandList() {
        showProgress("")
        ApiBuilder.create().getGiftCardBrandList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<GiftCardBrand>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<GiftCardBrand>>>?, response: NewResultResponse<ListResultResponse<GiftCardBrand>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null) {
                    val list = response.result!!.list!!
                    binding.textGiftCardBrandCont.text = getString(R.string.format_brand_count, FormatUtil.getMoneyType(list.size.toString()))
                    mAdapter!!.setDataList(list as MutableList<GiftCardBrand>)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<GiftCardBrand>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<GiftCardBrand>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_gift_shop), ToolbarOption.ToolbarMenu.LEFT)
        val item = ItemTopRightBinding.inflate(layoutInflater)
        item.textTopRight.setText(R.string.word_purchase_history)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, item.root, 0)
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
                        if (!PplusCommonUtil.loginCheck(this@GiftCardBrandActivity, null)) {
                            return
                        }

                        val intent = Intent(this@GiftCardBrandActivity, GiftCardPurchaseHistoryActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                    }
                    else -> {}
                }
            }
        }
    }
}