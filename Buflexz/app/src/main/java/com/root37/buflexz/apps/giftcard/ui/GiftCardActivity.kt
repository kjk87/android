package com.root37.buflexz.apps.giftcard.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.info.DeviceUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.GiftCard
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityGiftCardBinding
import retrofit2.Call

class GiftCardActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityGiftCardBinding

    override fun getLayoutView(): View {
        binding = ActivityGiftCardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mGiftCard: GiftCard
    override fun initializeView(savedInstanceState: Bundle?) {
        mGiftCard = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, GiftCard::class.java)!!

        binding.imageGiftCard.layoutParams.height = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS
        getData()
    }

    private fun getData() {
        showProgress("")
        ApiBuilder.create().getGiftCard(mGiftCard.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<GiftCard>> {
            override fun onResponse(call: Call<NewResultResponse<GiftCard>>?, response: NewResultResponse<GiftCard>?) {
                hideProgress()
                if (response?.result != null) {
                    mGiftCard = response.result!!
                    Glide.with(this@GiftCardActivity).load(mGiftCard.image).apply(RequestOptions().centerCrop()).into(binding.imageGiftCard)
                    binding.textGiftCardTitle.text = mGiftCard.title
                    binding.textGiftCardComment.text = mGiftCard.comment
                    binding.textGiftCardPurchase.setOnClickListener {
                        if (!PplusCommonUtil.loginCheck(this@GiftCardActivity, null)) {
                            return@setOnClickListener
                        }
                        val intent = Intent(this@GiftCardActivity, GiftCardPurchaseActivity::class.java)
                        intent.putExtra(Const.DATA, mGiftCard)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<GiftCard>>?, t: Throwable?, response: NewResultResponse<GiftCard>?) {

            }
        }).build().call()
    }

    private val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_product_detail), ToolbarOption.ToolbarMenu.LEFT)
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