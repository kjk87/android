package com.pplus.luckybol.apps.product.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonParser
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.card.data.CardSelectAdapter
import com.pplus.luckybol.apps.card.ui.CardRegActivity
import com.pplus.luckybol.apps.card.ui.PayPasswordCheckActivity
import com.pplus.luckybol.apps.card.ui.PayPasswordSetActivity
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.goods.ui.ShipTypePayCompleteActivity
import com.pplus.luckybol.apps.product.data.PurchaseProductShipAdapter
import com.pplus.luckybol.apps.shippingsite.ui.ShippingSiteConfigActivity
import com.pplus.luckybol.apps.signup.ui.VerificationMeActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityPurchaseProductShipPgBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import kr.co.bootpay.Bootpay
import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.listener.*
import kr.co.bootpay.model.BootExtra
import kr.co.bootpay.model.BootUser
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap


class PurchaseProductShipPgActivity : BaseActivity(), ImplToolbar, CompoundButton.OnCheckedChangeListener {
    override fun getPID(): String {
        return "Main_surrounding sale_product detail_buy"
    }

    private lateinit var binding: ActivityPurchaseProductShipPgBinding

    override fun getLayoutView(): View {
        binding = ActivityPurchaseProductShipPgBinding.inflate(layoutInflater)
        return binding.root
    }

    var mPayMethod = "card"
    var mInstallment = "00"

    //    var mKey = ""
    var mPurchaseProductList: ArrayList<PurchaseProduct>? = null
    var mPurchase = Purchase()
    private var mSelectShippingSite: ShippingSite? = null
    private var mPurchaseProductShipAdapter: PurchaseProductShipAdapter? = null
    private var mIsMemoInput = false
    private var mCardAdapter: CardSelectAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mPurchaseProductList = intent.getParcelableArrayListExtra(Const.PURCHASE_PRODUCT)

        //        text_purchase_product_ship_view_terms1.setOnClickListener {
        //            val intent = Intent(this, GoodsOrderTermsActivity::class.java)
        //            intent.putExtra(Const.KEY, Const.TERMS1)
        //            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        //            startActivity(intent)
        //        }
        //
        //        text_purchase_product_ship_view_terms2.setOnClickListener {
        //            val intent = Intent(this, GoodsOrderTermsActivity::class.java)
        //            intent.putExtra(Const.KEY, Const.TERMS2)
        //            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        //            startActivity(intent)
        //        }

        binding.textPurchaseProductShipSiteConfig.setOnClickListener {
            val intent = Intent(this, ShippingSiteConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            searchAddressLauncher.launch(intent)
        }

        binding.textPurchaseProductShipNotExistSite.setOnClickListener {
            val intent = Intent(this, ShippingSiteConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            searchAddressLauncher.launch(intent)
        }
        binding.editPurchaseProductShipName.setSingleLine()
        binding.editPurchaseProductShipPhone.setSingleLine()

        if (LoginInfoManager.getInstance().user.verification!!.media == "external" && StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.name)) {
            binding.editPurchaseProductShipName.setText(LoginInfoManager.getInstance().user.name)
        }

        binding.editPurchaseProductShipPhone.setText(LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", ""))

        binding.textPurchaseProductShipPay.setOnClickListener {

            //            if (!check_purchase_product_ship_terms1.isChecked || !check_purchase_product_ship_terms2.isChecked) {
            //
            //                ToastUtil.show(this, getString(R.string.msg_agree_terms))
            //                return@setOnClickListener
            //            }

            mPurchase.payMethod = mPayMethod

            val name = binding.editPurchaseProductShipName.text.toString().trim()
            if (StringUtils.isEmpty(name)) {
                showAlert(R.string.hint_input_name)
                return@setOnClickListener
            }

            val phone = binding.editPurchaseProductShipPhone.text.toString().trim()
            if (StringUtils.isEmpty(phone)) {
                showAlert(R.string.hint_input_contact)
                return@setOnClickListener
            }

            if (!FormatUtil.isPhoneNumber(phone)) {
                showAlert(R.string.msg_invalid_phone_number)
                return@setOnClickListener
            }

            //            val memo = text_purchase_product_ship_memo.text.toString().trim()

            mPurchase.buyerName = name
            mPurchase.buyerTel = phone

            if (mSelectShippingSite == null) {
                showAlert(R.string.msg_select_shipping_site)
                return@setOnClickListener
            }

            val purchaseProductList = ArrayList<PurchaseProduct>()
            for (item in mPurchaseProductList!!) {

                val purchaseProduct = PurchaseProduct()
                purchaseProduct.productSeqNo = item.product!!.seqNo
                purchaseProduct.productPriceCode = item.productPriceData!!.code
                purchaseProduct.count = item.count

                purchaseProduct.purchaseDeliverySelect = item.purchaseDeliverySelect

                if (purchaseProduct.purchaseDeliverySelect!!.deliveryAddFee1 == null) {
                    purchaseProduct.purchaseDeliverySelect!!.deliveryAddFee1 = 0f
                }

                if (purchaseProduct.purchaseDeliverySelect!!.deliveryAddFee2 == null) {
                    purchaseProduct.purchaseDeliverySelect!!.deliveryAddFee2 = 0f
                }

                if (purchaseProduct.purchaseDeliverySelect!!.deliveryFee == null) {
                    purchaseProduct.purchaseDeliverySelect!!.deliveryFee = 0f
                }

                if (StringUtils.isEmpty(mSelectShippingSite!!.receiverName)) {
                    showAlert(R.string.msg_not_exist_receiver_name)
                    return@setOnClickListener
                }

                if (StringUtils.isEmpty(mSelectShippingSite!!.receiverTel)) {
                    showAlert(R.string.msg_not_exist_receiver_tel)
                    return@setOnClickListener
                }

                var deliveryMemo = ""

                if (mIsMemoInput) {
                    deliveryMemo = binding.editPurchaseProductShipDeliveryMemo.text.toString().trim()
                } else {
                    deliveryMemo = binding.textPurchaseProductShipDeliveryMemo.text.toString().trim()
                }

                purchaseProduct.purchaseDeliverySelect!!.deliveryMemo = deliveryMemo
                purchaseProduct.purchaseDeliverySelect!!.receiverName = mSelectShippingSite!!.receiverName
                purchaseProduct.purchaseDeliverySelect!!.receiverTel = mSelectShippingSite!!.receiverTel
                purchaseProduct.purchaseDeliverySelect!!.receiverAddress = mSelectShippingSite!!.address
                purchaseProduct.purchaseDeliverySelect!!.receiverAddressDetail = mSelectShippingSite!!.addressDetail
                purchaseProduct.purchaseDeliverySelect!!.receiverPostCode = mSelectShippingSite!!.postCode
                purchaseProduct.purchaseProductOptionSelectList = item.purchaseProductOptionSelectList
                purchaseProductList.add(purchaseProduct)
            }

            mPurchase.purchaseProductSelectList = purchaseProductList
            if (mPurchaseProductList!!.size > 1) {
                mPurchase.title = getString(R.string.format_other2, mPurchaseProductList!![0].product!!.name, mPurchaseProductList!!.size - 1)
            } else {
                mPurchase.title = mPurchaseProductList!![0].product!!.name
            }
            mPurchase.appType = Const.APP_TYPE


            if (LoginInfoManager.getInstance().user.verification!!.media != "external") {
                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_notice_alert))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_me_for_service), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {
                    override fun onCancel() {}
                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                val intent = Intent(this@PurchaseProductShipPgActivity, VerificationMeActivity::class.java)
                                intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                verificationLauncher.launch(intent)
                            }
                            else -> {}
                        }
                    }
                }).builder().show(this)
            } else {

                when (mPayMethod) {
                    "card" -> {
                        orderId()
                    }
                    "ftlink" -> {
                        if (mCardAdapter!!.mSelectData == null) {
                            val builder = AlertBuilder.Builder()
                            builder.setTitle(getString(R.string.word_notice_alert))
                            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_not_exist_card), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
                            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                            builder.setOnAlertResultListener(object : OnAlertResultListener {

                                override fun onCancel() {
                                }

                                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                    when (event_alert) {
                                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                                            val intent = Intent(this@PurchaseProductShipPgActivity, CardRegActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            cardRegLauncher.launch(intent)
                                        }
                                        else -> {}
                                    }

                                }
                            }).builder().show(this)
                        } else {
                            val intent = Intent(this, PayPasswordCheckActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            checkPasswordLauncher.launch(intent)
                        }
                    }
                    "point"->{
                        val price = totalPrice + totalDeliveryPrice + totalDeliveryAddPrice
                        if(price > LoginInfoManager.getInstance().user.point!!){
                            showAlert(R.string.msg_lack_retention_point, 3)
                            return@setOnClickListener
                        }
                        orderId()
                    }
                }
            }
        }

        mPurchase.memberSeqNo = LoginInfoManager.getInstance().user.no
        mPurchase.salesType = 3

        mPurchaseProductShipAdapter = PurchaseProductShipAdapter(mPurchaseProductList!![0])
        binding.recyclerPurchaseProductShipProduct.adapter = mPurchaseProductShipAdapter
        //        adapter.setDataList(mBuyGoodsList!!)
        binding.recyclerPurchaseProductShipProduct.layoutManager = LinearLayoutManager(this)


        binding.textPurchaseProductShipDeliveryMemo.setOnClickListener {

            val deliveryMemos = resources.getStringArray(R.array.delivery_memo)

            val builder = AlertBuilder.Builder()
            builder.setContents(*deliveryMemos)
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    binding.textPurchaseProductShipDeliveryMemo.text = deliveryMemos[event_alert.getValue() - 1]
                    if (event_alert.getValue() == deliveryMemos.size) {
                        mIsMemoInput = true
                        binding.editPurchaseProductShipDeliveryMemo.visibility = View.VISIBLE
                    } else {
                        mIsMemoInput = false
                        binding.editPurchaseProductShipDeliveryMemo.visibility = View.GONE
                    }
                }
            }).builder().show(this)
        }

        binding.textPurchaseProductShipInstallment.setOnClickListener {
            val contents = resources.getStringArray(R.array.report_installment_period)
            val builder = AlertBuilder.Builder()
            builder.setContents(*contents)
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_RADIO)
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RADIO -> {

                            binding.textPurchaseProductShipInstallment.text = contents[event_alert.value - 1]
                            if (event_alert.value == 1) {
                                mInstallment = "00"
                            } else if (event_alert.value < 10) {
                                mInstallment = "0${event_alert.value}"
                            } else {
                                mInstallment = event_alert.value.toString()
                            }
                        }
                        else -> {}
                    }
                }
            }).builder().show(this)
        }

        binding.textPurchaseProductShipTerms.text = getString(R.string.msg_product_purchase_agree_desc)

        val purchaseDelivery = PurchaseDelivery()
        purchaseDelivery.type = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.type
        purchaseDelivery.method = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.method
        purchaseDelivery.paymentMethod = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.paymentMethod
        purchaseDelivery.deliveryFee = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryFee
        mPurchaseProductList!![0].purchaseDeliverySelect = purchaseDelivery
        getShippingSiteList()

        mCardAdapter = CardSelectAdapter()
        binding.recyclerPurchaseProductShipCard.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerPurchaseProductShipCard.adapter = mCardAdapter

        binding.textPurchaseProductShipCard.setOnClickListener {
            binding.textPurchaseProductShipCard.isSelected = true
            binding.textPurchaseProductShipEasyPay.isSelected = false
            binding.textPurchaseProductShipPoint.isSelected = false
            mPayMethod = "card"
            binding.layoutPurchaseProductShipEasyPay.visibility = View.GONE
            binding.layoutPurchaseProductShipRetentionPoint.visibility  =View.GONE
            setTotalPrice()
        }

        binding.textPurchaseProductShipEasyPay.setOnClickListener {
            binding.textPurchaseProductShipCard.isSelected = false
            binding.textPurchaseProductShipEasyPay.isSelected = true
            binding.textPurchaseProductShipPoint.isSelected = false
            mPayMethod = "ftlink"
            binding.layoutPurchaseProductShipEasyPay.visibility = View.VISIBLE
            binding.layoutPurchaseProductShipRetentionPoint.visibility  =View.GONE
            setTotalPrice()
        }

        binding.textPurchaseProductShipPoint.setOnClickListener {

            val price = totalPrice + totalDeliveryPrice + totalDeliveryAddPrice
            if(price > LoginInfoManager.getInstance().user.point!!){
                showAlert(R.string.msg_lack_retention_point, 2)
                return@setOnClickListener
            }

            binding.textPurchaseProductShipCard.isSelected = false
            binding.textPurchaseProductShipEasyPay.isSelected = false
            binding.textPurchaseProductShipPoint.isSelected = true
            mPayMethod = "point"
            binding.layoutPurchaseProductShipEasyPay.visibility = View.GONE
            binding.layoutPurchaseProductShipRetentionPoint.visibility  = View.VISIBLE
            setTotalPrice()
        }

        binding.textPurchaseProductShipCard.isSelected = true
        binding.textPurchaseProductShipEasyPay.isSelected = false
        binding.textPurchaseProductShipPoint.isSelected = false
        mPayMethod = "card"
        binding.layoutPurchaseProductShipEasyPay.visibility = View.GONE
        binding.layoutPurchaseProductShipRetentionPoint.visibility  = View.GONE
        cardListCall()
        getPoint()
    }

    private fun getPoint(){
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                binding.textPurchaseProductShipRetentionPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.point.toString())))
            }
        })
    }

    private fun cardListCall() {
        showProgress("")
        ApiBuilder.create().cardList.setCallback(object : PplusCallback<NewResultResponse<Card>> {
            override fun onResponse(call: Call<NewResultResponse<Card>>?,
                                    response: NewResultResponse<Card>?) {
                hideProgress()
                if (response?.datas != null) {
                    if (response.datas!!.isNotEmpty()) {
                        mCardAdapter!!.mSelectData = response.datas!![0]
                    }
                    mCardAdapter!!.setDataList(response.datas!! as MutableList<Card>)
                    mCardAdapter!!.add(Card())
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Card>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Card>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getShippingSiteList() {
        ApiBuilder.create().shippingSiteList.setCallback(object : PplusCallback<NewResultResponse<ShippingSite>> {
            override fun onResponse(call: Call<NewResultResponse<ShippingSite>>?,
                                    response: NewResultResponse<ShippingSite>?) {
                if (response?.datas != null) {
                    if (response.datas!!.isNotEmpty()) {
                        binding.textPurchaseProductShipNotExistSite.visibility = View.GONE
                        binding.layoutPurchaseProductShipExistSite.visibility = View.VISIBLE
                        mSelectShippingSite = response.datas!![0]
                        binding.textPurchaseProductShipSiteName.text = mSelectShippingSite!!.siteName
                        binding.textPurchaseProductShipSiteAddress.text = mSelectShippingSite!!.address + " " + mSelectShippingSite!!.addressDetail
                        binding.textPurchaseProductShipDeliveryMemo.visibility = View.VISIBLE
                        checkIslandsRegion()
                    } else {
                        binding.textPurchaseProductShipNotExistSite.visibility = View.VISIBLE
                        binding.layoutPurchaseProductShipExistSite.visibility = View.GONE
                        binding.textPurchaseProductShipDeliveryMemo.visibility = View.GONE
                        setTotalPrice()
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ShippingSite>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<ShippingSite>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun checkIslandsRegion() {
        if (mSelectShippingSite != null && mPurchaseProductList!![0].productPriceData!!.productDelivery != null && mPurchaseProductList!![0].productPriceData!!.productDelivery!!.isAddFee!!) {
            val params = HashMap<String, String>()
            params["postCode"] = mSelectShippingSite!!.postCode!!
            showProgress("")
            ApiBuilder.create().getIsLandsRegion(params).setCallback(object : PplusCallback<NewResultResponse<IslandsRegion>> {
                override fun onResponse(call: Call<NewResultResponse<IslandsRegion>>?,
                                        response: NewResultResponse<IslandsRegion>?) {
                    hideProgress()
                    if (response?.data != null) {
                        val isLandRegion = response.data!!

                        if (isLandRegion.isJeju!!) {
                            if (mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee1 != null && mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee1!! > 0) {
                                mPurchaseProductList!![0].purchaseDeliverySelect!!.deliveryAddFee1 = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee1
                            } else {
                                mPurchaseProductList!![0].purchaseDeliverySelect!!.deliveryAddFee1 = 0f
                            }
                        } else {
                            if (mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee2 != null && mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee2!! > 0) {
                                mPurchaseProductList!![0].purchaseDeliverySelect!!.deliveryAddFee2 = mPurchaseProductList!![0].productPriceData!!.productDelivery!!.deliveryAddFee2
                            } else {
                                mPurchaseProductList!![0].purchaseDeliverySelect!!.deliveryAddFee2 = 0f
                            }
                        }
                        setTotalPrice()
                    } else {
                        setTotalPrice()
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<IslandsRegion>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<IslandsRegion>?) {
                    hideProgress()
                    setTotalPrice()
                }
            }).build().call()
        } else {
            setTotalPrice()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        //        check_purchase_product_ship_terms1.isChecked = isChecked
        //        check_purchase_product_ship_terms2.isChecked = isChecked
    }

    var totalPrice = 0
    var totalDeliveryPrice = 0
    var totalDeliveryAddPrice = 0
    var totalPriceDeliveryPrice = 0
    var totalPriceDeliveryAddPrice = 0
    private fun setTotalPrice() {
        totalPrice = 0
        totalDeliveryPrice = 0
        totalDeliveryAddPrice = 0
        totalPriceDeliveryPrice = 0
        totalPriceDeliveryAddPrice = 0
        for (i in 0 until mPurchaseProductList!!.size) {
            var price = 0
            if (mPurchaseProductList!![i].purchaseProductOptionSelectList != null && mPurchaseProductList!![i].purchaseProductOptionSelectList!!.isNotEmpty()) {
                for (purchaseProductOption in mPurchaseProductList!![i].purchaseProductOptionSelectList!!) {
                    price = (mPurchaseProductList!![i].productPriceData!!.price!!.toInt() + purchaseProductOption.price!!) * purchaseProductOption.amount!!
                    totalPrice += price
                }
            } else {
                price = mPurchaseProductList!![i].productPriceData!!.price!!.toInt() * mPurchaseProductList!![i].count!!
                totalPrice += price
            }

            when (mPurchaseProductList!![i].purchaseDeliverySelect!!.type) { // 1:무료, 2:유료, 3:조건부 무료
                EnumData.DeliveryType.free.type -> {
                    mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee = 0f
                }
                EnumData.DeliveryType.pay.type -> {
                    if (mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee!! > 0) {
                        totalDeliveryPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee!!.toInt()
                        if (mPurchaseProductList!![i].purchaseDeliverySelect!!.paymentMethod == "before") {
                            totalPriceDeliveryPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee!!.toInt()
                        }

                    } else {
                        mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee = 0f
                    }
                }
                EnumData.DeliveryType.conditionPay.type -> {
                    if (mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee!! > 0) {
                        if (mPurchaseProductList!![i].productPriceData!!.productDelivery!!.deliveryMinPrice != null && mPurchaseProductList!![i].productPriceData!!.productDelivery!!.deliveryMinPrice!! > 0 && mPurchaseProductList!![i].productPriceData!!.productDelivery!!.deliveryMinPrice!! > price) {
                            totalDeliveryPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee!!.toInt()
                        } else {
                            mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryFee = 0f
                        }
                    }
                }
            }
            if (mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee1 != null && mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee1!! > 0) {
                totalDeliveryAddPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee1!!.toInt()
                if (mPurchaseProductList!![i].purchaseDeliverySelect!!.paymentMethod == "before") {
                    totalPriceDeliveryAddPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee1!!.toInt()
                }
            } else {
                mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee1 = 0f
            }

            if (mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee2 != null && mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee2!! > 0) {
                totalDeliveryAddPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee2!!.toInt()
                if (mPurchaseProductList!![i].purchaseDeliverySelect!!.paymentMethod == "before") {
                    totalPriceDeliveryAddPrice += mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee2!!.toInt()
                }
            } else {
                mPurchaseProductList!![i].purchaseDeliverySelect!!.deliveryAddFee2 = 0f
            }
        }

        if (totalDeliveryPrice + totalDeliveryAddPrice > 0) {
            binding.textPurchaseProductShipDeliveryFee.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType((totalDeliveryPrice + totalDeliveryAddPrice).toString())))
        } else {
            binding.textPurchaseProductShipDeliveryFee.text = getString(R.string.word_free_ship)
        }

        binding.textPurchaseProductShipTotalPrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType((totalPrice + totalPriceDeliveryPrice + totalPriceDeliveryAddPrice).toString())))
        
        
        if (mPayMethod != "point" && mPurchaseProductList!![0].productPriceData!!.isPoint != null && mPurchaseProductList!![0].productPriceData!!.isPoint!!) {
            binding.layoutPurchaseProductShipSavePoint.visibility = View.VISIBLE
            binding.textPurchaseProductShipPointPurchaseDesc.visibility = View.GONE
            val point = mPurchaseProductList!![0].productPriceData!!.point
            binding.textPurchaseProductShipSavePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit, FormatUtil.getMoneyType((point!! * mPurchaseProductList!![0].count!!).toInt().toString())))
        } else {
            binding.layoutPurchaseProductShipSavePoint.visibility = View.GONE
            binding.textPurchaseProductShipPointPurchaseDesc.visibility = View.VISIBLE
        }


        checkInstallment()
    }

    private fun checkInstallment() {

        when (mPayMethod) {
            "card" -> {
                binding.layoutPurchaseProductShipInstallment.visibility = View.GONE
            }
            "ftlink" -> {
                val price = totalPrice + totalDeliveryPrice + totalDeliveryAddPrice
                if (price >= 50000) {
                    binding.layoutPurchaseProductShipInstallment.visibility = View.VISIBLE
                } else {
                    binding.layoutPurchaseProductShipInstallment.visibility = View.GONE
                }
            }
            "point" -> {
                binding.layoutPurchaseProductShipInstallment.visibility = View.GONE
            }
        }

    }

    private fun orderId() {
        showProgress("")
        ApiBuilder.create().postOrderId().setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?,
                                    response: NewResultResponse<String>?) {

                hideProgress()
                if (response != null) {
                    //                    val regex = Regex("^[0-9]{22}\\z")
                    val orderId = response.data!!

                    when (mPayMethod) {
                        "card" -> {
                            openPg(orderId)
                        }
                        "ftlink", "point" -> {
                            buy(orderId, null)
                        }
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun openPg(orderId: String) {
        val builder = Bootpay.init(this)
        if (Const.API_URL.startsWith("https://api")) {
            builder.setApplicationId(getString(R.string.boot_pay_cash_id)) // 해당 프로젝트(안드로이드)의 application id 값
        } else {
            builder.setApplicationId(getString(R.string.boot_pay_id_stage)) // 해당 프로젝트(안드로이드)의 application id 값
        }

        val bootUser = BootUser()
        bootUser.username = LoginInfoManager.getInstance().user.name
        bootUser.phone = LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", "")
        val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))

        builder.setBootUser(bootUser).setMethod(Method.CARD) // 결제수단
            .setPG(PG.DANAL) // 결제수단
            .setContext(this).setName(mPurchase.title) // 결제할 상품명
            .setOrderId(orderId) //고유 주문번호로, 생성하신 값을 보내주셔야 합니다.
        //.setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
        builder.addItem(mPurchase.title, mPurchaseProductList!![0].count!!, mPurchaseProductList!![0].productPriceData!!.code, (totalPrice + totalDeliveryPrice + totalDeliveryAddPrice).toInt()) // 주문정보에 담길 상품정보, 통계를 위해 사용
        builder.setPrice((totalPrice + totalDeliveryPrice + totalDeliveryAddPrice))

        builder.onConfirm(object : ConfirmListener { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
            override fun onConfirm(message: String?) {
                buy(orderId, message)
                LogUtil.e(LOG_TAG, "confirm : {}", message)
            }
        }).onDone(object : DoneListener { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
            override fun onDone(message: String?) {
                LogUtil.e(LOG_TAG, "done : {}", message)

                val receiptId = JsonParser.parseString(message).asJsonObject.get("receipt_id").asString

                verify(mPurchase.orderId!!, receiptId)

            }
        }).onReady(object : ReadyListener {
            override fun onReady(message: String?) {
                LogUtil.e(LOG_TAG, "ready : {}", message)
            }
        }).onCancel(object : CancelListener {
            override fun onCancel(message: String?) {
                LogUtil.e(LOG_TAG, "cancel : {}", message)
                showAlert(R.string.msg_cancel_pg)
                //                deleteBuy(buy.seqNo.toString())
            }
        }).onError(object : ErrorListener {
            override fun onError(message: String?) {
                LogUtil.e(LOG_TAG, "error : {}", message)
                showAlert(R.string.msg_error_pg)
                //                deleteBuy(buy.seqNo.toString())
            }
        }).onClose(object : CloseListener {
            override fun onClose(message: String?) {
                LogUtil.e(LOG_TAG, "close : {}", message)
            }
        }).request()
    }

    private fun buy(orderId: String, message: String?) {
        mPurchase.orderId = orderId
        when (mPayMethod) {
            "card" -> {
                mPurchase.pg = "DANAL"
            }
            "ftlink" -> {
                mPurchase.pg = "FTLINK"
            }
            "point" -> {
                mPurchase.pg = "POINT"
            }
        }

        mPurchase.installment = mInstallment
        showProgress("")
        ApiBuilder.create().postPurchaseShip(mPurchase).setCallback(object : PplusCallback<NewResultResponse<Purchase>> {
            override fun onResponse(call: Call<NewResultResponse<Purchase>>?,
                                    response: NewResultResponse<Purchase>?) {
                hideProgress()
                if (response?.data != null && response.data!!.seqNo != null) {
                    when (mPayMethod) {
                        "card" -> {
                            Bootpay.confirm(message)
                        }
                        "ftlink" -> {
                            ftLinkPay()
                        }
                        "point" -> {
                            PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                                override fun reload() {
                                }
                            })

                            orderComplete()
                        }
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Purchase>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Purchase>?) {
                hideProgress()
                showAlert(R.string.msg_cancel_pg)
            }
        }).build().call()
    }

    private fun verify(orderId: String, receiptId: String) {
        val params = HashMap<String, String>()
        params["orderId"] = orderId
        params["receiptId"] = receiptId

        showProgress("")
        ApiBuilder.create().postPurchaseBootPayVerify(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {
                hideProgress()
                orderComplete()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_error_pg)
            }
        }).build().call()
    }

    private fun ftLinkPay() {
        val params = FTLink()
        params.shopcode = mPurchaseProductList!![0].productPriceData!!.page!!.shopCode
        //        params.loginId = LoginInfoManager.getInstance().user.loginId
        params.order_req_amt = (totalPrice + totalDeliveryPrice + totalDeliveryAddPrice).toString()
        params.order_hp = mPurchase.buyerTel
        params.order_name = mPurchase.buyerName
        params.order_goodsname = mPurchase.title
        params.req_installment = mInstallment
        params.comp_memno = LoginInfoManager.getInstance().user.no.toString()
        params.autokey = mCardAdapter!!.mSelectData!!.autoKey
        params.req_cardcode = mCardAdapter!!.mSelectData!!.cardCode
        params.comp_orderno = mPurchase.orderId
        showProgress("")
        ApiBuilder.create().postPurchaseFTLinkPay(params).setCallback(object : PplusCallback<NewResultResponse<FTLink>> {
            override fun onResponse(call: Call<NewResultResponse<FTLink>>?,
                                    response: NewResultResponse<FTLink>?) {
                hideProgress()
                orderComplete()
            }

            override fun onFailure(call: Call<NewResultResponse<FTLink>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<FTLink>?) {
                hideProgress()
                if (response?.data != null && StringUtils.isNotEmpty(response.data!!.errMessage)) {
                    showAlert(response.data!!.errMessage)
                }
            }
        }).build().call()
    }

    private fun orderComplete() {
        setEvent("productPurchase")
        val intent = Intent(this, ShipTypePayCompleteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        orderFinishLauncher.launch(intent)
    }

    val cardRegLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if(LoginInfoManager.getInstance().user.setPayPassword == null || !LoginInfoManager.getInstance().user.setPayPassword!!){
                val intent = Intent(this, PayPasswordSetActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                changePasswordLauncher.launch(intent)
            }
            cardListCall()

        }
    }

    val checkPasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            orderId()

        }
    }

    val searchAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                mSelectShippingSite = data.getParcelableExtra(Const.SHIPPING_SITE)
                if (mSelectShippingSite != null) {
                    binding.textPurchaseProductShipNotExistSite.visibility = View.GONE
                    binding.layoutPurchaseProductShipExistSite.visibility = View.VISIBLE
                    binding.textPurchaseProductShipSiteName.text = mSelectShippingSite!!.siteName
                    binding.textPurchaseProductShipSiteAddress.text = mSelectShippingSite!!.address + " " + mSelectShippingSite!!.addressDetail
                    binding.textPurchaseProductShipDeliveryMemo.visibility = View.VISIBLE
                    checkIslandsRegion()
                } else {
                    binding.textPurchaseProductShipNotExistSite.visibility = View.VISIBLE
                    binding.layoutPurchaseProductShipExistSite.visibility = View.GONE
                }
            }

        }
    }

    val orderFinishLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(result.resultCode)
        finish()
    }

    val changePasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
            }
        })
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_buy), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
