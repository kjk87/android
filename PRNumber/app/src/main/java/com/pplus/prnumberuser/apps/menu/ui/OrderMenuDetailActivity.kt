package com.pplus.prnumberuser.apps.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.NestedScrollView
import androidx.viewpager2.widget.ViewPager2
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.DeliveryAddressManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.menu.data.MainImageAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.ApiController
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityOrderMenuDetailBinding
import com.pplus.prnumberuser.databinding.CartToastBinding
import com.pplus.prnumberuser.databinding.ItemMenuOptionBinding
import com.pplus.prnumberuser.databinding.ItemMenuOptionDetailBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OrderMenuDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityOrderMenuDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityOrderMenuDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mOrderMenu: OrderMenu
    var mAdapter: MainImageAdapter? = null
    var mCount = 1
    var mPage: Page2? = null
    var mSalesType = 1

    override fun initializeView(savedInstanceState: Bundle?) {
        mOrderMenu = intent.getParcelableExtra(Const.DATA)!!
        mPage = intent.getParcelableExtra(Const.PAGE)
        mSalesType = intent.getIntExtra(Const.TYPE, 1)

        binding.textMenuDetailMinOrderPrice.text = getString(R.string.format_min_order_price, FormatUtil.getMoneyType(mPage!!.minOrderPrice!!.toInt().toString()))

        if (mOrderMenu.imageList != null && mOrderMenu.imageList!!.isNotEmpty()) {
            binding.layoutMenuDetailImage.visibility = View.VISIBLE
            binding.viewMenuDetailNoneImage.visibility = View.GONE
            binding.textMenuDetailBack.setImageResource(R.drawable.ic_navbar_back_light)
            binding.imageMenuDetailCart.setImageResource(R.drawable.ic_cart_white)
        } else {
            binding.layoutMenuDetailImage.visibility = View.GONE
            binding.viewMenuDetailNoneImage.visibility = View.VISIBLE
            binding.textMenuDetailBack.setImageResource(R.drawable.ic_navbar_back)
            binding.imageMenuDetailCart.setImageResource(R.drawable.ic_cart)
        }

        binding.textMenuDetailBack.setOnClickListener {
            onBackPressed()
        }

        binding.layoutMenuDetailCart.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }
            val intent = Intent(this, MenuCartActivity::class.java)
            intent.putExtra(Const.TYPE, mSalesType)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cartLauncher.launch(intent)
        }

        binding.layoutMenuDetailCart2.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }
            val intent = Intent(this, MenuCartActivity::class.java)
            intent.putExtra(Const.TYPE, mSalesType)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cartLauncher.launch(intent)
        }

        binding.scrollMenuDetail.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                LogUtil.e(LOG_TAG, "scrollY : {}", scrollY)
                if (scrollY >= resources.getDimensionPixelSize(R.dimen.height_168)) {
                    binding.layoutMenuDetailTitle.visibility = View.VISIBLE
                } else {
                    binding.layoutMenuDetailTitle.visibility = View.GONE
                }
            }
        })

        mAdapter = MainImageAdapter()
        binding.pagerOrderMenuImage.adapter = mAdapter
        binding.pagerOrderMenuImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.indicatorMenuDetailImage.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        binding.imageMenuDetailMinus.setOnClickListener {

            if (mCount > 1) {
                mCount--
            }
            binding.textMenuDetailCount.text = mCount.toString()

            setTotalPrice()
        }

        binding.imageMenuDetailPlus.setOnClickListener {

            mCount++
            binding.textMenuDetailCount.text = mCount.toString()
            setTotalPrice()
        }

        mCount = 1
        binding.textMenuDetailCount.text = mCount.toString()

        binding.textMenuDetailOrder.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }

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

            if(mPage!!.minOrderPrice != null && mPage!!.minOrderPrice!! > 0 && mPage!!.minOrderPrice!!.toInt() > totalMenuPrice){
                val lackPrice = mPage!!.minOrderPrice!!.toInt() - totalMenuPrice
                val intent = Intent(this, AlertLackOrderPriceActivity::class.java)
                intent.putExtra(Const.PRICE, lackPrice.toInt())
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                return@setOnClickListener
            }

            when(mSalesType){
                1->{
                    val intent = Intent(this, AlertVisitOrderActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    visitTypeLauncher.launch(intent)
                }
                2->{
                    if (mPage!!.orderType!!.contains("2") && mPage!!.orderType!!.contains("3")) {
                        val intent = Intent(this, AlertSelectOrderTypeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        orderTypeLauncher.launch(intent)
                    } else {
                        val type: Int
                        if (mPage!!.orderType!!.contains("3")) {
                            type = 2
                        } else {
                            type = 5
                        }

                        purchase(type)
                    }
                }
            }


        }

        binding.textMenuDetailAddCart.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }
            val params = HashMap<String, String>()
            params["pageSeqNo"] = mOrderMenu.pageSeqNo.toString()
            params["salesType"] = mSalesType.toString()
            showProgress("")
            ApiBuilder.create().checkCartPage(params).setCallback(object : PplusCallback<NewResultResponse<Boolean>> {
                override fun onResponse(call: Call<NewResultResponse<Boolean>>?, response: NewResultResponse<Boolean>?) {
                    hideProgress()
                    if (response?.data != null && response.data == false) {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_clear_and_add_cart), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {
                            override fun onCancel() {}
                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        saveCart()
                                    }
                                }
                            }
                        }).builder().show(this@OrderMenuDetailActivity)
                    } else {
                        saveCart()
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<Boolean>>?, t: Throwable?, response: NewResultResponse<Boolean>?) {
                    hideProgress()
                    saveCart()
                }
            }).build().call()

        }

        if ((mPage!!.businessHoursType == 4 || mPage!!.isBusinessHour!!) && !mPage!!.isDayOff!! && !mPage!!.isTimeOff!! && mPage!!.orderable!!) {
            binding.textMenuDetailOff.visibility = View.GONE
            binding.layoutMenuDetailOrder.visibility = View.VISIBLE

            when(mSalesType){
                1->{
                    binding.textMenuDetailOff.visibility = View.GONE
                    binding.layoutMenuDetailOrder.visibility = View.VISIBLE
                }
                2->{
                    val list = DeliveryAddressManager.getInstance().deliveryAddressList!!

                    when(mPage!!.riderType) { //1:배달대행사, 2:자체배달, 3:모두사용
                        2->{
                            val distance = PplusCommonUtil.calDistance(list[0].latitude!!, list[0].longitude!!, mPage!!.latitude!!, mPage!!.longitude!!)

                            if(distance > mPage!!.riderDistance!!*1000f){
                                binding.textMenuDetailOff.visibility = View.VISIBLE
                                binding.layoutMenuDetailOrder.visibility = View.GONE
                                binding.textMenuDetailOff.text = getString(R.string.msg_can_not_delivery)
                            }else{
                                binding.layoutMenuDetailOrder.visibility = View.VISIBLE
                                binding.textMenuDetailOff.visibility = View.GONE
                            }
                        }
                        else->{
                            val params = HashMap<String, String>()
                            params["sido"] = list[0].sido!!
                            params["sigungu"] = list[0].sigungu!!

                            if(list[0].dongli!!.endsWith("면")){
                                params["myeon"] = list[0].dongli!!
                            }else{
                                params["dongli"] = list[0].dongli!!
                            }

                            params["roadAddress"] = list[0].address!!
                            params["jibunAddress"] = list[0].jibunAddress!!
                            params["addressDetail"] = list[0].addressDetail!!
                            params["roadName"] = list[0].roadName!!
                            params["buildNo"] = list[0].buildNo!!
                            params["pageSeqNo"] = mPage!!.seqNo.toString()
                            params["price"] = "10000"
                            LogUtil.e(LOG_TAG, params.toString())
                            showProgress("")
                            ApiController.getCSApi().getRiderFee(params).enqueue(object : Callback<ResultRiderFee> {
                                override fun onResponse(call: Call<ResultRiderFee>, response: Response<ResultRiderFee>) {
                                    hideProgress()
                                    if(response.body() != null){
                                        if(response.body()!!.result != null){
                                            val data = response.body()!!.result!!
                                            if(StringUtils.isNotEmpty(data.delivery_distance)){
                                                LogUtil.e(LOG_TAG, "data.delivery_distance : {}", data.delivery_distance)
                                                val distance = data.delivery_distance!!.toDouble()

                                                if(distance > mPage!!.riderDistance!!*1000f || data.is_run == "N"){
                                                    binding.textMenuDetailOff.visibility = View.VISIBLE
                                                    binding.layoutMenuDetailOrder.visibility = View.GONE
                                                    binding.textMenuDetailOff.text = getString(R.string.msg_can_not_delivery)
                                                }else{
                                                    binding.layoutMenuDetailOrder.visibility = View.VISIBLE
                                                    binding.textMenuDetailOff.visibility = View.GONE
                                                }
                                            }
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<ResultRiderFee>, t: Throwable) {
                                    hideProgress()
                                }
                            })
                        }
                    }

                }
            }
        } else {
            binding.textMenuDetailOff.visibility = View.VISIBLE
            binding.layoutMenuDetailOrder.visibility = View.GONE
            binding.textMenuDetailOff.text = getString(R.string.msg_not_ready_now)
        }

        if (LoginInfoManager.getInstance().isMember) {
            getCartCount()
        }

        getMenu()
    }

    val orderTypeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                val type = data.getIntExtra(Const.TYPE, -1)
                if (type != -1) {
                    purchase(type)
                }
            }
        }
    }

    val visitTypeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            purchase(1)
        }
    }

    private fun purchase(type: Int) {
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
                return
            }
        }

        cart.cartOptionList = list

        val cartList = arrayListOf<Cart>()
        cartList.add(cart)

        val intent = Intent(this, OrderPurchasePgActivity::class.java)
        intent.putExtra(Const.TYPE, type)
        intent.putParcelableArrayListExtra(Const.DATA, cartList)
        intent.putExtra(Const.PAGE, mPage)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    private fun saveCart() {
        val cart = Cart()
        cart.amount = mCount
        cart.memberSeqNo = LoginInfoManager.getInstance().user.no
        cart.pageSeqNo = mOrderMenu.pageSeqNo
        cart.orderMenuSeqNo = mOrderMenu.seqNo
        cart.salesType = mSalesType
        val list = arrayListOf<CartOption>()
        for (option in mOrderMenu.optionList!!) {

            var existSelect = false

            for (optionDetail in option.menuOption!!.detailList!!) {

                if (optionDetail.isSelect != null && optionDetail.isSelect!!) {
                    LogUtil.e(LOG_TAG, optionDetail.title)
                    val cartOption = CartOption()
                    cartOption.menuOptionDetailSeqNo = optionDetail.seqNo
                    cartOption.type = option.menuOption!!.type
                    list.add(cartOption)
                    existSelect = true
                }

            }

            if (option.menuOption!!.type == 1 && !existSelect) {
                showAlert(getString(R.string.format_select_essential_option, option.menuOption!!.title))
                return
            }
        }

        cart.cartOptionList = list
        showProgress("")
        ApiBuilder.create().saveCart(cart).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                getCartCount()
                createToast().show()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getCartCount() {
        val params = HashMap<String, String>()
        if(mSalesType == 1){
            params["salesType"] = "1"
        }else{
            params["salesType"] = "2"
        }

        ApiBuilder.create().getCartCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                if (response?.data != null && response.data > 0) {
                    binding.textMenuDetailCartCount.visibility = View.VISIBLE
                    binding.textMenuDetailCartCount2.visibility = View.VISIBLE
                    binding.textMenuDetailCartCount.text = response.data.toString()
                    binding.textMenuDetailCartCount2.text = response.data.toString()
                } else {
                    binding.textMenuDetailCartCount.visibility = View.GONE
                    binding.textMenuDetailCartCount2.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {

            }
        }).build().call()
    }

    val cartLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                val key = data.getStringExtra(Const.KEY)
                if (key.equals("add")) {
                    setResult(RESULT_OK)
                    finish()
                    return@registerForActivityResult
                }
            }
        }

        if (LoginInfoManager.getInstance().isMember) {
            getCartCount()
        }
    }

    fun createToast(): Toast {
        val binding = CartToastBinding.inflate(layoutInflater)

        binding.textCartToast.text = getString(R.string.msg_added_cart)

        return Toast(this).apply {
            setGravity(Gravity.TOP or Gravity.END, resources.getDimensionPixelSize(R.dimen.width_48), resources.getDimensionPixelSize(R.dimen.height_138))
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }

    private fun getMenu() {
        val params = HashMap<String, String>()
        params["seqNo"] = mOrderMenu.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getMenu(params).setCallback(object : PplusCallback<NewResultResponse<OrderMenu>> {
            override fun onResponse(call: Call<NewResultResponse<OrderMenu>>?, response: NewResultResponse<OrderMenu>?) {
                hideProgress()
                if (response?.data != null) {
                    mOrderMenu = response.data

                    if (mOrderMenu.imageList != null && mOrderMenu.imageList!!.isNotEmpty()) {
                        if (mOrderMenu.imageList!!.size > 1) {
                            binding.indicatorMenuDetailImage.removeAllViews()
                            binding.indicatorMenuDetailImage.visibility = View.VISIBLE
                            binding.indicatorMenuDetailImage.build(LinearLayout.HORIZONTAL, mOrderMenu.imageList!!.size)
                        } else {
                            binding.indicatorMenuDetailImage.visibility = View.GONE
                        }
                        mAdapter!!.setDataList(mOrderMenu.imageList!!.toMutableList())
                    }

                    binding.textMenuDetailTitle.text = mOrderMenu.title
                    binding.textMenuDetailTitle2.text = mOrderMenu.title
                    binding.textMenuDetailTitle2.setSingleLine()

                    if (mOrderMenu.delegate != null && mOrderMenu.delegate!!) {
                        binding.textMenuDetailDelegate.visibility = View.VISIBLE
                    } else {
                        binding.textMenuDetailDelegate.visibility = View.GONE
                    }

                    if (StringUtils.isNotEmpty(mOrderMenu.menuInfo)) {
                        binding.textMenuDetailInfo.visibility = View.VISIBLE
                        binding.textMenuDetailInfo.text = mOrderMenu.menuInfo
                    } else {
                        binding.textMenuDetailInfo.visibility = View.GONE
                    }

                    binding.textMenuDetailPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mOrderMenu.price!!.toInt().toString()))

                    setOption()
                    setTotalPrice()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<OrderMenu>>?, t: Throwable?, response: NewResultResponse<OrderMenu>?) {
                hideProgress()
            }
        }).build().call()
    }

    var totalMenuPrice = 0f

    private fun setTotalPrice() {
        totalMenuPrice = mOrderMenu.price!!
        for (option in mOrderMenu.optionList!!) {
            for (optionDetail in option.menuOption!!.detailList!!) {
                if (optionDetail.isSelect != null && optionDetail.isSelect!!) {
                    totalMenuPrice += optionDetail.price!!
                }
            }
        }

        totalMenuPrice *= mCount

        binding.textMenuDetailTotalPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(totalMenuPrice.toInt().toString()))

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
                                setTotalPrice()
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
                                setTotalPrice()
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
                                setTotalPrice()
                            }

                            optionDetailBinding.textMenuOptionDetailPrice.text = "+${getString(R.string.format_money_unit, FormatUtil.getMoneyType(optionDetail.price!!.toInt().toString()))}"
                            optionBinding.layoutMenuOptionDetail.addView(optionDetailBinding.root)
                        }
                    }
                }

                binding.layoutMenuDetailOption.addView(optionBinding.root)
            }
        }
    }
}