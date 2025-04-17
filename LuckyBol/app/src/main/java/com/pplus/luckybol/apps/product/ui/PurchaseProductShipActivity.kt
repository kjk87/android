//package com.pplus.luckybol.apps.product.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.text.SpannableString
//import android.view.View
//import android.widget.CompoundButton
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.common.builder.AlertBuilder
//import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
//import com.pplus.luckybol.apps.common.builder.data.AlertData
//import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
//import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
//import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
//import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.apps.goods.ui.ShipTypePayCompleteActivity
//import com.pplus.luckybol.apps.product.data.PurchaseProductShipAdapter
//import com.pplus.luckybol.apps.shippingsite.ui.ShippingSiteConfigActivity
//import com.pplus.luckybol.apps.signup.ui.VerificationMeActivity
//import com.pplus.luckybol.core.code.common.EnumData
//import com.pplus.luckybol.core.network.ApiBuilder
//import com.pplus.luckybol.core.network.model.dto.*
//import com.pplus.luckybol.core.network.model.response.NewResultResponse
//import com.pplus.luckybol.core.util.PplusCommonUtil
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.activity_cash_exchange.*
//import kotlinx.android.synthetic.main.activity_purchase_product_ship.*
//import retrofit2.Call
//import java.util.*
//import kotlin.collections.HashMap
//
//
//class PurchaseProductShipActivity : BaseActivity(), ImplToolbar, CompoundButton.OnCheckedChangeListener {
//    override fun getPID(): String {
//        return "Main_surrounding sale_product detail_buy"
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_purchase_product_ship
//    }
//
//    var mPayMethod = "point"
//
//    //    var mKey = ""
//    var mPurchaseProductList: ArrayList<PurchaseProduct>? = null
//    var mPurchase = Purchase()
//    private var mSelectShippingSite:ShippingSite? = null
//    private var mPurchaseProductShipAdapter: PurchaseProductShipAdapter? = null
//    private var mIsMemoInput = false
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mPurchaseProductList = intent.getParcelableArrayListExtra(Const.PURCHASE_PRODUCT)
//
////        text_purchase_product_ship_view_terms1.setOnClickListener {
////            val intent = Intent(this, GoodsOrderTermsActivity::class.java)
////            intent.putExtra(Const.KEY, Const.TERMS1)
////            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////            startActivity(intent)
////        }
////
////        text_purchase_product_ship_view_terms2.setOnClickListener {
////            val intent = Intent(this, GoodsOrderTermsActivity::class.java)
////            intent.putExtra(Const.KEY, Const.TERMS2)
////            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////            startActivity(intent)
////        }
//
//        text_purchase_product_ship_site_config.setOnClickListener {
//            val intent = Intent(this, ShippingSiteConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_SEARCH_ADDRESS)
//        }
//
//        text_purchase_product_ship_not_exist_site.setOnClickListener {
//            val intent = Intent(this, ShippingSiteConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_SEARCH_ADDRESS)
//        }
//        edit_purchase_product_ship_name.setSingleLine()
//        edit_purchase_product_ship_phone.setSingleLine()
//
//        if (LoginInfoManager.getInstance().user.verification!!.media == "external" && StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.name)) {
//            edit_purchase_product_ship_name.setText(LoginInfoManager.getInstance().user.name)
//        }
//
//        edit_purchase_product_ship_phone.setText(LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", ""))
//
//        text_purchase_product_ship_pay.setOnClickListener {
//
////            if (!check_purchase_product_ship_terms1.isChecked || !check_purchase_product_ship_terms2.isChecked) {
////
////                ToastUtil.show(this, getString(R.string.msg_agree_terms))
////                return@setOnClickListener
////            }
//
//            mPurchase.payMethod = "point"
//
//            val name = edit_purchase_product_ship_name.text.toString().trim()
//            if (StringUtils.isEmpty(name)) {
//                showAlert(R.string.hint_input_name)
//                return@setOnClickListener
//            }
//
//            val phone = edit_purchase_product_ship_phone.text.toString().trim()
//            if (StringUtils.isEmpty(phone)) {
//                showAlert(R.string.hint_input_contact)
//                return@setOnClickListener
//            }
//
//            if (!FormatUtil.isPhoneNumber(phone)) {
//                showAlert(R.string.msg_invalid_phone_number)
//                return@setOnClickListener
//            }
//
////            val memo = text_purchase_product_ship_memo.text.toString().trim()
//
//            mPurchase.buyerName = name
//            mPurchase.buyerTel = phone
//
//            if(mSelectShippingSite == null){
//                showAlert(R.string.msg_select_shipping_site)
//                return@setOnClickListener
//            }
//
//            val purchaseProductList = ArrayList<PurchaseProduct>()
//            for (item in mPurchaseProductList!!) {
//
//                val purchaseProduct = PurchaseProduct()
//                purchaseProduct.productSeqNo = item.product!!.seqNo
//                purchaseProduct.productPriceCode = item.productPriceData!!.code
//                purchaseProduct.count = item.count
//
//                purchaseProduct.purchaseDeliverySelect = item.purchaseDeliverySelect
//
//                if(purchaseProduct.purchaseDeliverySelect!!.deliveryAddFee1 == null){
//                    purchaseProduct.purchaseDeliverySelect!!.deliveryAddFee1 = 0f
//                }
//
//                if(purchaseProduct.purchaseDeliverySelect!!.deliveryAddFee2 == null){
//                    purchaseProduct.purchaseDeliverySelect!!.deliveryAddFee2 = 0f
//                }
//
//                if(purchaseProduct.purchaseDeliverySelect!!.deliveryFee == null){
//                    purchaseProduct.purchaseDeliverySelect!!.deliveryFee = 0f
//                }
//
//                if(StringUtils.isEmpty(mSelectShippingSite!!.receiverName)){
//                    showAlert(R.string.msg_not_exist_receiver_name)
//                    return@setOnClickListener
//                }
//
//                if(StringUtils.isEmpty(mSelectShippingSite!!.receiverTel)){
//                    showAlert(R.string.msg_not_exist_receiver_tel)
//                    return@setOnClickListener
//                }
//
//                var deliveryMemo = ""
//
//                if(mIsMemoInput){
//                    deliveryMemo = edit_purchase_product_ship_delivery_memo.text.toString().trim()
//                }else{
//                    deliveryMemo = text_purchase_product_ship_delivery_memo.text.toString().trim()
//                }
//
//                purchaseProduct.purchaseDeliverySelect!!.deliveryMemo = deliveryMemo
//                purchaseProduct.purchaseDeliverySelect!!.receiverName = mSelectShippingSite!!.receiverName
//                purchaseProduct.purchaseDeliverySelect!!.receiverTel = mSelectShippingSite!!.receiverTel
//                purchaseProduct.purchaseDeliverySelect!!.receiverAddress= mSelectShippingSite!!.address
//                purchaseProduct.purchaseDeliverySelect!!.receiverAddressDetail= mSelectShippingSite!!.addressDetail
//                purchaseProduct.purchaseDeliverySelect!!.receiverPostCode = mSelectShippingSite!!.postCode
//                purchaseProduct.purchaseProductOptionSelectList = item.purchaseProductOptionSelectList
//                purchaseProductList.add(purchaseProduct)
//            }
//
//            mPurchase.purchaseProductSelectList = purchaseProductList
//            if (mPurchaseProductList!!.size > 1) {
//                mPurchase.title = getString(R.string.format_other2, mPurchaseProductList!![0].product!!.name, mPurchaseProductList!!.size - 1)
//            } else {
//                mPurchase.title = mPurchaseProductList!![0].product!!.name
//            }
//            mPurchase.appType = Const.APP_TYPE
//
//            val price = totalPrice + totalDeliveryPrice + totalDeliveryAddPrice
//            if(price > LoginInfoManager.getInstance().user.point!!){
//                showAlert(R.string.msg_lack_retention_point, 3)
//                return@setOnClickListener
//            }
//
//            if (LoginInfoManager.getInstance().user.verification!!.media != "external") {
//                val builder = AlertBuilder.Builder()
//                builder.setTitle(getString(R.string.word_notice_alert))
//                builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_me_for_service), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
//                builder.addContents(AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//                builder.setOnAlertResultListener(object : OnAlertResultListener {
//                    override fun onCancel() {}
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                        when (event_alert) {
//                            AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                val intent = Intent(this@PurchaseProductShipActivity, VerificationMeActivity::class.java)
//                                intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                startActivityForResult(intent, Const.REQ_VERIFICATION)
//                            }
//                        }
//                    }
//                }).builder().show(this)
//            } else {
////                val intent = Intent(this, PayPasswordCheckActivity::class.java)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                startActivityForResult(intent, Const.REQ_CHECK_PASSWORD)
//
//                orderId()
//            }
//        }
//
//        mPayMethod = "point"
//        mPurchase.memberSeqNo = LoginInfoManager.getInstance().user.no
//        mPurchase.salesType = 3
//
//        mPurchaseProductShipAdapter = PurchaseProductShipAdapter(mPurchaseProductList!![0])
//        recycler_purchase_product_ship_buy_goods.adapter = mPurchaseProductShipAdapter
////        adapter.setDataList(mBuyGoodsList!!)
//        recycler_purchase_product_ship_buy_goods.layoutManager = LinearLayoutManager(this)
//
//
//        text_purchase_product_ship_delivery_memo.setOnClickListener {
//
//            val deliveryMemos = resources.getStringArray(R.array.delivery_memo)
//
//            val builder = AlertBuilder.Builder()
//            builder.setContents(*deliveryMemos)
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                    text_purchase_product_ship_delivery_memo.text = deliveryMemos[event_alert.getValue()-1]
//                    if(event_alert.getValue() == deliveryMemos.size){
//                        mIsMemoInput = true
//                        edit_purchase_product_ship_delivery_memo.visibility = View.VISIBLE
//                    }else{
//                        mIsMemoInput = false
//                        edit_purchase_product_ship_delivery_memo.visibility = View.GONE
//                    }
//                }
//            }).builder().show(this)
//        }
//
//        val spannable = SpannableString(getString(R.string.msg_product_purchase_agree_desc))
//
////        val clickableSpan1 = object : ClickableSpan() {
////            override fun onClick(p0: View) {
////                val intent = Intent(this@PurchaseProductShipActivity, GoodsOrderTermsActivity::class.java)
////                intent.putExtra(Const.KEY, Const.TERMS1)
////                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////                startActivity(intent)
////            }
////
////            override fun updateDrawState(ds: TextPaint) {
////                ds.color = ResourceUtil.getColor(this@PurchaseProductShipActivity, R.color.color_fc5c57)
////            }
////        }
////
////        val clickableSpan2 = object : ClickableSpan() {
////            override fun onClick(p0: View) {
////                val intent = Intent(this@PurchaseProductShipActivity, GoodsOrderTermsActivity::class.java)
////                intent.putExtra(Const.KEY, Const.TERMS2)
////                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////                startActivity(intent)
////            }
////
////            override fun updateDrawState(ds: TextPaint) {
////                ds.color = ResourceUtil.getColor(this@PurchaseProductShipActivity, R.color.color_fc5c57)
////            }
////        }
//
////        spannable.setSpan(clickableSpan1, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
////        spannable.setSpan(clickableSpan2, 6, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
////        text_purchase_product_ship_terms.movementMethod = LinkMovementMethod.getInstance()
//        text_purchase_product_ship_terms.text = getString(R.string.msg_product_purchase_agree_desc)
//
////        check_purchase_product_ship_totalAgree.isChecked = true
////        check_purchase_product_ship_terms1.isChecked = true
////        check_purchase_product_ship_terms2.isChecked = true
//
////        check_purchase_product_ship_totalAgree.setOnCheckedChangeListener(this)
////        check_purchase_product_ship_terms1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
////            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
////
////
////                var isAll = false
////                if (isChecked && check_purchase_product_ship_terms2.isChecked) {
////                    isAll = true
////                }
////                check_purchase_product_ship_totalAgree.setOnCheckedChangeListener(null)
////                check_purchase_product_ship_totalAgree.isChecked = isAll
////                check_purchase_product_ship_totalAgree.setOnCheckedChangeListener(this@PurchaseProductShipActivity)
////            }
////        })
//
////        check_purchase_product_ship_terms2.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
////            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
////
////
////                var isAll = false
////                if (isChecked && check_purchase_product_ship_terms1.isChecked) {
////                    isAll = true
////                }
////                check_purchase_product_ship_totalAgree.setOnCheckedChangeListener(null)
////                check_purchase_product_ship_totalAgree.isChecked = isAll
////                check_purchase_product_ship_totalAgree.setOnCheckedChangeListener(this@PurchaseProductShipActivity)
////            }
////        })
//
//        text_purchase_product_ship_desc.text = PplusCommonUtil.fromHtml(getString(R.string.html_purchase_point_desc))
//
//        val purchaseDelivery = PurchaseDelivery()
//        purchaseDelivery.type = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.type
//        purchaseDelivery.method = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.method
//        purchaseDelivery.paymentMethod = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.paymentMethod
//        purchaseDelivery.deliveryFee = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryFee
//        mPurchaseProductList!![0].purchaseDeliverySelect = purchaseDelivery
//        getShippingSiteList()
//    }
//
//    private fun getShippingSiteList(){
//        ApiBuilder.create().shippingSiteList.setCallback(object : PplusCallback<NewResultResponse<ShippingSite>> {
//            override fun onResponse(call: Call<NewResultResponse<ShippingSite>>?,
//                                    response: NewResultResponse<ShippingSite>?) {
//                if (response?.datas != null) {
//                    if (response.datas.isNotEmpty()) {
//                        text_purchase_product_ship_not_exist_site.visibility = View.GONE
//                        layout_purchase_product_ship_exist_site.visibility = View.VISIBLE
//                        mSelectShippingSite = response.datas[0]
//                        text_purchase_product_ship_site_name.text = mSelectShippingSite!!.siteName
//                        text_purchase_product_ship_site_address.text = mSelectShippingSite!!.address + " " + mSelectShippingSite!!.addressDetail
//                        text_purchase_product_ship_delivery_memo.visibility = View.VISIBLE
//                        checkIslandsRegion()
//                    } else {
//                        text_purchase_product_ship_not_exist_site.visibility = View.VISIBLE
//                        layout_purchase_product_ship_exist_site.visibility = View.GONE
//                        text_purchase_product_ship_delivery_memo.visibility = View.GONE
//                        setTotalPrice()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<ShippingSite>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<ShippingSite>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun checkIslandsRegion(){
//        if(mSelectShippingSite != null && mPurchaseProductList!![0].productPriceData!!.productDelivery != null && mPurchaseProductList!![0].productPriceData!!.productDelivery!!.isAddFee!!){
//            val params = HashMap<String, String>()
//            params["postCode"] = mSelectShippingSite!!.postCode!!
//            showProgress("")
//            ApiBuilder.create().getIsLandsRegion(params).setCallback(object : PplusCallback<NewResultResponse<IslandsRegion>> {
//                override fun onResponse(call: Call<NewResultResponse<IslandsRegion>>?,
//                                        response: NewResultResponse<IslandsRegion>?) {
//                    hideProgress()
//                    if (response?.data != null) {
//                        val isLandRegion = response.data
//
//                        if (isLandRegion.isJeju!!) {
//                            if (mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee1 != null && mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee1!! > 0) {
//                                mPurchaseProductList!![0].purchaseDeliverySelect!!.deliveryAddFee1 = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee1
//                            } else {
//                                mPurchaseProductList!![0].purchaseDeliverySelect!!.deliveryAddFee1 = 0f
//                            }
//                        } else {
//                            if (mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee2 != null && mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee2!! > 0) {
//                                mPurchaseProductList!![0].purchaseDeliverySelect!!.deliveryAddFee2 = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee2
//                            } else {
//                                mPurchaseProductList!![0].purchaseDeliverySelect!!.deliveryAddFee2 = 0f
//                            }
//                        }
//                        setTotalPrice()
//                    } else {
//                        setTotalPrice()
//                    }
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<IslandsRegion>>?,
//                                       t: Throwable?,
//                                       response: NewResultResponse<IslandsRegion>?) {
//                    hideProgress()
//                    setTotalPrice()
//                }
//            }).build().call()
//        }else{
//            setTotalPrice()
//        }
//    }
//
//    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
////        check_purchase_product_ship_terms1.isChecked = isChecked
////        check_purchase_product_ship_terms2.isChecked = isChecked
//    }
//
//    var totalPrice = 0
//    var totalDeliveryPrice = 0
//    var totalDeliveryAddPrice = 0
//    var totalPriceDeliveryPrice = 0
//    var totalPriceDeliveryAddPrice = 0
//    private fun setTotalPrice() {
//        totalPrice = 0
//        totalDeliveryPrice = 0
//        totalDeliveryAddPrice = 0
//        totalPriceDeliveryPrice = 0
//        totalPriceDeliveryAddPrice = 0
//        for (i in 0 until mPurchaseProductList!!.size) {
//            var price = 0
//            if(mPurchaseProductList!![i].purchaseProductOptionSelectList != null && mPurchaseProductList!![i].purchaseProductOptionSelectList!!.isNotEmpty()){
//                for(purchaseProductOption in mPurchaseProductList!![i].purchaseProductOptionSelectList!!){
//                    price = (mPurchaseProductList!![i].productPriceData!!.price!!.toInt() + purchaseProductOption.price!!)*purchaseProductOption.amount!!
//                    totalPrice += price
//                }
//            }else{
//                price = mPurchaseProductList!![i].productPriceData!!.price!!.toInt() * mPurchaseProductList!![i].count!!
//                totalPrice += price
//            }
//
//            when(mPurchaseProductList!![i].purchaseDeliverySelect!!.type){// 1:무료, 2:유료, 3:조건부 무료
//                EnumData.DeliveryType.free.type -> {
//                    mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee = 0f
//                }
//                EnumData.DeliveryType.pay.type -> {
//                    if (mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee!! > 0) {
//                        totalDeliveryPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee!!.toInt()
//                        if (mPurchaseProductList!![i].purchaseDeliverySelect!!.paymentMethod == "before") {
//                            totalPriceDeliveryPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee!!.toInt()
//                        }
//
//                    } else {
//                        mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee = 0f
//                    }
//                }
//                EnumData.DeliveryType.conditionPay.type -> {
//                    if (mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee!! > 0) {
//                        if (mPurchaseProductList!![i].productPriceData!!.productDelivery!!.deliveryMinPrice != null && mPurchaseProductList!![i].productPriceData!!.productDelivery!!.deliveryMinPrice!! > 0 && mPurchaseProductList!![i].productPriceData!!.productDelivery!!.deliveryMinPrice!! > price) {
//                            totalDeliveryPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee!!.toInt()
//                        } else {
//                            mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee = 0f
//                        }
//                    }
//                }
//            }
//            if(mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee1 != null && mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee1!! > 0){
//                totalDeliveryAddPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee1!!.toInt()
//                if(mPurchaseProductList!![i].purchaseDeliverySelect!!.paymentMethod == "before"){
//                    totalPriceDeliveryAddPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee1!!.toInt()
//                }
//            }else{
//                mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee1 = 0f
//            }
//
//            if(mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee2 != null && mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee2!! > 0){
//                totalDeliveryAddPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee2!!.toInt()
//                if(mPurchaseProductList!![i].purchaseDeliverySelect!!.paymentMethod == "before"){
//                    totalPriceDeliveryAddPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee2!!.toInt()
//                }
//            }else{
//                mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee2 = 0f
//            }
//        }
//
//        if(totalDeliveryPrice + totalDeliveryAddPrice > 0){
//            text_purchase_product_ship_delivery_fee.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit, FormatUtil.getMoneyType((totalDeliveryPrice + totalDeliveryAddPrice).toString())))
//        }else{
//            text_purchase_product_ship_delivery_fee.text = getString(R.string.word_free_ship)
//        }
//
//        text_purchase_product_ship_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit, FormatUtil.getMoneyType((totalPrice + totalPriceDeliveryPrice + totalPriceDeliveryAddPrice).toString())))
//        getPoint()
//    }
//
//    private fun getPoint(){
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//            override fun reload() {
//                text_purchase_product_ship_retention_point.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.point.toString())))
//            }
//        })
//    }
//
//    private fun orderId() {
//        showProgress("")
//        ApiBuilder.create().postOrderId().setCallback(object : PplusCallback<NewResultResponse<String>> {
//            override fun onResponse(call: Call<NewResultResponse<String>>?,
//                                    response: NewResultResponse<String>?) {
//
//                hideProgress()
//                if (response != null) {
//                    //                    val regex = Regex("^[0-9]{22}\\z")
//                    val orderId = response.data
//                    buy(orderId)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<String>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<String>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun buy(orderId: String) {
//        mPurchase.orderId = orderId
//        mPurchase.pg = "POINT"
//        showProgress("")
//        ApiBuilder.create().postPurchaseShip(mPurchase).setCallback(object : PplusCallback<NewResultResponse<Purchase>> {
//            override fun onResponse(call: Call<NewResultResponse<Purchase>>?,
//                                    response: NewResultResponse<Purchase>?) {
//                hideProgress()
//                if (response?.data != null) {
//                    PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                        override fun reload() {
//                        }
//                    })
//
//                    orderComplete()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Purchase>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<Purchase>?) {
//                hideProgress()
//                showAlert(R.string.msg_cancel_pg)
//            }
//        }).build().call()
//    }
//
//    private fun orderComplete() {
//        val intent = Intent(this, ShipTypePayCompleteActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivityForResult(intent, Const.REQ_ORDER_FINISH)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_CHECK_PASSWORD -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    orderId()
//                }
//            }
//            Const.REQ_SEARCH_ADDRESS -> {
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    mSelectShippingSite = data.getParcelableExtra(Const.SHIPPING_SITE)
//                    if (mSelectShippingSite != null) {
//                        text_purchase_product_ship_not_exist_site.visibility = View.GONE
//                        layout_purchase_product_ship_exist_site.visibility = View.VISIBLE
//                        text_purchase_product_ship_site_name.text = mSelectShippingSite!!.siteName
//                        text_purchase_product_ship_site_address.text = mSelectShippingSite!!.address + " " + mSelectShippingSite!!.addressDetail
//                        text_purchase_product_ship_delivery_memo.visibility = View.VISIBLE
//                        checkIslandsRegion()
//                    } else {
//                        text_purchase_product_ship_not_exist_site.visibility = View.VISIBLE
//                        layout_purchase_product_ship_exist_site.visibility = View.GONE
//                    }
//                }
//            }
//            Const.REQ_ORDER_FINISH -> {
//                setResult(resultCode)
//                finish()
//            }
//            Const.REQ_CHANGE_PASSWORD -> {
//                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                    override fun reload() {
//                    }
//                })
//            }
//            Const.REQ_VERIFICATION -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        val verifiedData = data.getParcelableExtra<User>(Const.DATA)
//                        if (LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", "") != verifiedData.mobile) {
//                            showAlert(R.string.msg_incorrect_joined_mobile_number)
//                        } else {
//                            val user = LoginInfoManager.getInstance().user
//                            user.name = verifiedData.name
//                            user.birthday = verifiedData.birthday
//                            user.mobile = verifiedData.mobile
//                            user.gender = verifiedData.gender
//                            user.verification = verifiedData.verification
//                            showProgress("")
//                            ApiBuilder.create().updateUser(user).setCallback(object : PplusCallback<NewResultResponse<User>> {
//
//                                override fun onResponse(call: Call<NewResultResponse<User>>,
//                                                        response: NewResultResponse<User>) {
//                                    hideProgress()
//                                    showProgress("")
//                                    PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                                        override fun reload() {
//                                            hideProgress()
//                                            if (LoginInfoManager.getInstance().user.verification!!.media == "external" && StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.name)) {
//                                                edit_purchase_product_ship_name.setText(LoginInfoManager.getInstance().user.name)
//                                            }
//                                            showAlert(R.string.msg_verified)
//                                        }
//                                    })
//                                }
//
//                                override fun onFailure(call: Call<NewResultResponse<User>>,
//                                                       t: Throwable,
//                                                       response: NewResultResponse<User>) {
//                                    hideProgress()
//                                }
//                            }).build().call()
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_buy), ToolbarOption.ToolbarMenu.LEFT)
//
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}
