package com.pplus.prnumberuser.apps.mobilegift.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityGiftishowDetailBinding
import com.pplus.utils.part.format.FormatUtil

class GiftishowDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_bolpoint_pointshop_detail"
    }

    private lateinit var binding: ActivityGiftishowDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityGiftishowDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mGiftishow: Giftishow? = null
    private var mCount = 0
    private var mGiftishowBuy: GiftishowBuy? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mGiftishow = intent.getParcelableExtra(Const.DATA)

        Glide.with(this).load(mGiftishow!!.goodsImgB).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imageGiftishowDetail)
        binding.textGiftishowDetailName.text = mGiftishow!!.goodsName
        binding.textGiftishowDetailPrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(mGiftishow!!.realPrice)))

        binding.textGiftishowDetailUseNote.text = mGiftishow!!.content

        binding.textGiftishowDetailBuy.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }
            mGiftishowBuy!!.totalCount = mCount
            val targetList = arrayListOf<GiftishowTarget>()
            val target = GiftishowTarget()
            target.mobileNumber = LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE+"##", "")
            target.name = getString(R.string.word_me)
            targetList.add(target)
            mGiftishowBuy!!.targetList = targetList
            val totalPrice = mCount * mGiftishow!!.realPrice!!.toInt()
            mGiftishowBuy!!.price = totalPrice

            val intent = Intent(this, GiftishowBuyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra(Const.DATA, mGiftishowBuy)
            intent.putExtra(Const.IS_ME, true)
            startActivity(intent)
        }

        binding.textGiftishowDetailGift.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }
            val intent = Intent(this, SelectContactActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            selectContactLauncher.launch(intent)
        }


        mCount = 1
        mGiftishowBuy = GiftishowBuy()
        mGiftishowBuy!!.giftishowSeqNo = mGiftishow!!.seqNo
        mGiftishowBuy!!.giftishow = mGiftishow
        mGiftishowBuy!!.totalCount = mCount
        mGiftishowBuy!!.unitPrice = mGiftishow!!.realPrice!!.toInt()
    }

    val selectContactLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val target = data.getParcelableExtra<MsgTarget>(Const.DATA)

                val giftishowTarget = GiftishowTarget()
                giftishowTarget.mobileNumber = target!!.mobile
                giftishowTarget.name = target.name
                mGiftishowBuy!!.totalCount = mCount
                val targetList = arrayListOf<GiftishowTarget>()
                targetList.add(giftishowTarget)
                mGiftishowBuy!!.targetList = targetList

                val totalPrice = mCount * mGiftishow!!.realPrice!!.toInt()
                mGiftishowBuy!!.price = totalPrice

                val intent = Intent(this, GiftishowBuyActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra(Const.DATA, mGiftishowBuy)
                intent.putExtra(Const.IS_ME, false)
                startActivity(intent)
            }

        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_product_detail), ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}