package com.pplus.prnumberuser.apps.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.Cart
import com.pplus.prnumberuser.core.network.model.dto.CartOption
import com.pplus.prnumberuser.core.network.model.dto.OrderMenu
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityAlertTicketOptionBinding
import com.pplus.prnumberuser.databinding.ItemMenuOptionBinding
import com.pplus.prnumberuser.databinding.ItemMenuOptionDetailBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil

class AlertTicketOptionActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertTicketOptionBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertTicketOptionBinding.inflate(layoutInflater)
        return binding.root
    }

    private lateinit var mOrderMenu: OrderMenu
    var mPage: Page2? = null
    var mCount = 1

    override fun initializeView(savedInstanceState: Bundle?) {
        mOrderMenu = intent.getParcelableExtra(Const.DATA)!!
        mPage = intent.getParcelableExtra(Const.PAGE)

        binding.layoutAlertTicketOptionDown.setOnClickListener {
            val intent = Intent(this, TicketDetailActivity::class.java)
            intent.putExtra(Const.DATA, mOrderMenu)
            intent.putExtra(Const.PAGE, intent.getParcelableExtra<Page2>(Const.PAGE))
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            overridePendingTransition(R.anim.fix, R.anim.view_down)
        }

        binding.imageAlertTicketOptionMinus.setOnClickListener {

            if (mCount > 1) {
                mCount--
            }
            binding.textMenuDetailCount.text = mCount.toString()

        }

        binding.imageAlertTicketOptionPlus.setOnClickListener {

            mCount++
            binding.textMenuDetailCount.text = mCount.toString()
        }

        mCount = 1
        binding.textMenuDetailCount.text = mCount.toString()

        binding.textAlertTicketOptionBuy.setOnClickListener {
            for (option in mOrderMenu.optionList!!) {
                var existSelect = false
                for (optionDetail in option.menuOption!!.detailList!!) {
                    if (optionDetail.isSelect != null && optionDetail.isSelect!!) {
                        existSelect = true
                    }
                }

                if (option.menuOption!!.type == 1 && !existSelect) {
                    showAlert(getString(R.string.format_select_essential_option, option.menuOption!!.title))
                    return@setOnClickListener
                }
            }

            val cart = Cart()
            cart.amount = mCount
            cart.memberSeqNo = LoginInfoManager.getInstance().user.no
            cart.pageSeqNo = mOrderMenu.pageSeqNo
            cart.orderMenuSeqNo = mOrderMenu.seqNo
            cart.salesType = 2
            cart.orderMenu = mOrderMenu
            val list = arrayListOf<CartOption>()
            for (option in mOrderMenu.optionList!!) {

                var existSelect = false

                for (optionDetail in option.menuOption!!.detailList!!) {

                    if (optionDetail.isSelect != null && optionDetail.isSelect!!) {
                        LogUtil.e(LOG_TAG, optionDetail.title)
                        val cartOption = CartOption()
                        cartOption.menuOptionDetailSeqNo = optionDetail.seqNo
                        cartOption.menuOptionDetail = optionDetail
                        cartOption.type = option.menuOption!!.type
                        list.add(cartOption)
                        existSelect = true
                    }
                }

                if (option.menuOption!!.type == 1 && !existSelect) {
                    showAlert(getString(R.string.format_select_essential_option, option.menuOption!!.title))
                    return@setOnClickListener
                }
            }

            cart.cartOptionList = list

            val cartList = arrayListOf<Cart>()
            cartList.add(cart)

            val intent = Intent(this, TicketPurchasePgActivity::class.java)
            intent.putParcelableArrayListExtra(Const.DATA, cartList)
            intent.putExtra(Const.PAGE, mPage)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        setOption()
    }

    private fun setOption() {
        if (mOrderMenu.optionList != null) {
            for (option in mOrderMenu.optionList!!) {
                val optionBinding = ItemMenuOptionBinding.inflate(layoutInflater)

                var type = ""
                when (option.menuOption!!.type) {
                    1 -> {
                        optionBinding.textMenuOptionTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_option_title, option.menuOption!!.title, getString(R.string.word_essential)))

                        val optionTextViewList = arrayListOf<TextView>()
                        for ((i, optionDetail) in option.menuOption!!.detailList!!.withIndex()) {
                            val optionDetailBinding = ItemMenuOptionDetailBinding.inflate(layoutInflater)
                            optionDetailBinding.textMenuOptionDetailTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_menu_radio, 0, 0, 0)
                            optionDetailBinding.textMenuOptionDetailTitle.text = optionDetail.title
                            optionTextViewList.add(optionDetailBinding.textMenuOptionDetailTitle)
                            optionDetailBinding.textMenuOptionDetailTitle.setOnClickListener {
                                for (textView in optionTextViewList) {
                                    textView.isSelected = false
                                }

                                for (optionDetail in option.menuOption!!.detailList!!) {
                                    optionDetail.isSelect = false
                                }

                                optionDetailBinding.textMenuOptionDetailTitle.isSelected = true
                                optionDetail.isSelect = true
                            }

                            optionDetailBinding.textMenuOptionDetailPrice.text = "+${getString(R.string.format_money_unit, FormatUtil.getMoneyType(optionDetail.price!!.toInt().toString()))}"
                            optionBinding.layoutMenuOptionDetail.addView(optionDetailBinding.root)

                            if(i == 0){
                                for (textView in optionTextViewList) {
                                    textView.isSelected = false
                                }

                                for (optionDetail in option.menuOption!!.detailList!!) {
                                    optionDetail.isSelect = false
                                }

                                optionDetailBinding.textMenuOptionDetailTitle.isSelected = true
                                optionDetail.isSelect = true
                            }
                        }
                    }
                    2 -> {
                        optionBinding.textMenuOptionTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_option_title, option.menuOption!!.title, getString(R.string.word_add)))
                        for (optionDetail in option.menuOption!!.detailList!!) {
                            val optionDetailBinding = ItemMenuOptionDetailBinding.inflate(layoutInflater)
                            optionDetailBinding.textMenuOptionDetailTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_menu_check, 0, 0, 0)
                            optionDetailBinding.textMenuOptionDetailTitle.text = optionDetail.title
                            optionDetailBinding.textMenuOptionDetailTitle.setOnClickListener {

                                optionDetailBinding.textMenuOptionDetailTitle.isSelected = !optionDetailBinding.textMenuOptionDetailTitle.isSelected
                                optionDetail.isSelect = optionDetailBinding.textMenuOptionDetailTitle.isSelected
                            }

                            optionDetailBinding.textMenuOptionDetailPrice.text = "+${getString(R.string.format_money_unit, FormatUtil.getMoneyType(optionDetail.price!!.toInt().toString()))}"
                            optionBinding.layoutMenuOptionDetail.addView(optionDetailBinding.root)
                        }
                    }
                }

                binding.layoutAlertTicketOption.addView(optionBinding.root)
            }
        }
    }
}
