//package com.pplus.prnumberuser.apps.mobilegift.ui
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.animation.ObjectAnimator
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.view.animation.AccelerateDecelerateInterpolator
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.model.dto.MobileGift
//import com.pplus.prnumberuser.core.network.model.dto.MobileGiftPurchase
//import com.pplus.prnumberuser.core.network.model.dto.MsgTarget
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_mobile_gift_detail.*
//
//class MobileGiftDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return "Main_bolpoint_pointshop_detail"
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_mobile_gift_detail
//    }
//
//    private var mMobileGift: MobileGift? = null
//    private var mCount = 0
//    private var mPurchase: MobileGiftPurchase? = null
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mMobileGift = intent.getParcelableExtra(Const.DATA)
//
//        Glide.with(this).load(mMobileGift!!.baseImage).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_mobile_gift_detail)
//        text_mobile_gift_detail_name.text = mMobileGift!!.name
//        text_mobile_gift_detail_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(mMobileGift!!.salesPrice.toString())))
//
//        text_mobile_gift_detail_use_note.text = mMobileGift!!.useNote
//
//        image_mobile_gift_detail_option_minus.setOnClickListener {
//            if (mCount > 1) {
//                mCount--
//                setCount()
//            }
//
//        }
//
//        image_mobile_gift_detail_option_plus.setOnClickListener {
//            mCount++
//            setCount()
//        }
//
//        image_mobile_gift_detail_option_close.setOnClickListener {
//            scrollOutAnimation()
//        }
//
//        text_mobile_gift_detail_buy.setOnClickListener {
////            text_mobile_gift_detail_buy.visibility = View.VISIBLE
////            text_mobile_gift_detail_option_gift.visibility = View.GONE
//            scrollInAnimation()
//        }
//
//        text_mobile_gift_detail_gift.setOnClickListener {
////            text_mobile_gift_detail_buy.visibility = View.GONE
////            text_mobile_gift_detail_option_gift.visibility = View.VISIBLE
//            scrollInAnimation()
//        }
//
//        text_mobile_gift_detail_option_gift.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(this)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(this, SelectContactActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SEARCH)
//        }
//
//        text_mobile_gift_detail_option_buy.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(this)) {
//                return@setOnClickListener
//            }
//            mPurchase!!.countPerTarget = mCount
//            val targetList = arrayListOf<MsgTarget>()
//            val target = MsgTarget()
//            target.mobile = LoginInfoManager.getInstance().user.mobile
//            target.name = LoginInfoManager.getInstance().user.nickname
//            targetList.add(target)
//            mPurchase!!.targetList = targetList
//
//            val totalPrice = mCount * mMobileGift!!.salesPrice
//            mPurchase!!.totalCost = totalPrice
//            mPurchase!!.pgCost = 0L
//
//            val intent = Intent(this, MobileGiftBuyActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            intent.putExtra(Const.DATA, mPurchase)
//            intent.putExtra(Const.IS_ME, true)
//            startActivityForResult(intent, Const.REQ_BUY)
//        }
//
//
//
//        mCount = 1
//        setCount()
//        mPurchase = MobileGiftPurchase()
//        mPurchase!!.mobileGift = mMobileGift
//    }
//
//    private fun setCount() {
//        text_mobile_gift_detail_option_count.text = mCount.toString()
//        val totalPrice = mCount * mMobileGift!!.salesPrice
//        text_mobile_gift_detail_option_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_price2, FormatUtil.getMoneyType(totalPrice.toString())))
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_SEARCH -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        val target = data.getParcelableExtra<MsgTarget>(Const.DATA)
//                        mPurchase!!.countPerTarget = mCount
//                        val targetList = arrayListOf<MsgTarget>()
//                        targetList.add(target)
//                        mPurchase!!.targetList = targetList
//
//                        val totalPrice = mCount * mMobileGift!!.salesPrice
//                        mPurchase!!.totalCost = totalPrice
//                        mPurchase!!.pgCost = 0L
//
//                        val intent = Intent(this, MobileGiftBuyActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        intent.putExtra(Const.DATA, mPurchase)
//                        intent.putExtra(Const.IS_ME, false)
//                        startActivityForResult(intent, Const.REQ_BUY)
//                    }
//                }
//            }
//        }
//    }
//
//    var animating = false
//
//    private fun scrollOutAnimation() {
//        val scrollOutAnimator = ObjectAnimator.ofFloat(layout_mobile_gift_detail_option, "translationY", 0f,
//                layout_mobile_gift_detail_option.height.toFloat()).apply {
//            duration = 300
//            interpolator = AccelerateDecelerateInterpolator()
//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationStart(animation: Animator?) {
//                    animating = true
//                }
//
//                override fun onAnimationCancel(animation: Animator?) {
//                    animating = true
//                }
//
//                override fun onAnimationEnd(animation: Animator?) {
//                    animating = false
//                    layout_mobile_gift_detail_option.visibility = View.GONE
//                }
//            })
//        }
//
//        if (!animating) {
//            scrollOutAnimator.start()
//        }
//    }
//
//    private fun scrollInAnimation() {
//        val scrollInAnimator = ObjectAnimator.ofFloat(layout_mobile_gift_detail_option, "translationY",
//                resources.getDimension(R.dimen.height_840), 0f).apply {
//            duration = 300
//            interpolator = AccelerateDecelerateInterpolator()
//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationStart(animation: Animator?) {
//
//                    layout_mobile_gift_detail_option.visibility = View.VISIBLE
//                    animating = true
//                }
//
//                override fun onAnimationCancel(animation: Animator?) {
//                    animating = true
//                }
//
//                override fun onAnimationEnd(animation: Animator?) {
//                    animating = false
//                }
//            })
//        }
//
//        if (!animating) {
//            scrollInAnimator.start()
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_product_detail), ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}