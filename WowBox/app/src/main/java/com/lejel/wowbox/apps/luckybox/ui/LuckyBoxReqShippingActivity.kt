package com.lejel.wowbox.apps.luckybox.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.product.ui.AlertProductOptionActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.*
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ActivityLuckyBoxReqShippingBinding
import com.lejel.wowbox.databinding.ItemLuckyBoxOptionBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class LuckyBoxReqShippingActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityLuckyBoxReqShippingBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxReqShippingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mLuckyBoxPurchaseItem: LuckyBoxPurchaseItem? = null
    private var mProductOptionTotal: ProductOptionTotal? = null
    private var mDeliveryFee: Float? = null
    private var mDeliveryAddFee1: Float? = null
    private var mDeliveryAddFee2: Float? = null
    private var mProduct: Product? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mDeliveryFee = 15000f
        mDeliveryAddFee1 = 0f
        mDeliveryAddFee2 = 0f
        mLuckyBoxPurchaseItem = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxPurchaseItem::class.java)

        binding.textLuckyBoxReqShipping.setOnClickListener {
            pay()
        }

        binding.editLuckyBoxReqShippingName.setSingleLine()
        binding.editLuckyBoxReqShippingFamilyName.setSingleLine()
        binding.editLuckyBoxReqShippingAddress1.setSingleLine()
        binding.editLuckyBoxReqShippingAddress2.setSingleLine()
        binding.editLuckyBoxReqShippingTel.setSingleLine()


        Glide.with(this).load(mLuckyBoxPurchaseItem!!.productImage).apply(RequestOptions().fitCenter()).into(binding.imageLuckyBoxReqShippingProductImage)
        binding.imageLuckyBoxReqShippingProductImage.setOnClickListener {
            if(!packageManager.hasSystemFeature(PackageManager.FEATURE_WEBVIEW)){
                return@setOnClickListener
            }
            val intent = Intent(this, LuckyBoxProductInfoActivity::class.java)
            val product = Product()
            product.seqNo = mLuckyBoxPurchaseItem!!.productSeqNo
            intent.putExtra(Const.DATA, product)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        binding.textLuckyBoxReqShippingProductName.text = mLuckyBoxPurchaseItem!!.productName
        binding.textLuckyBoxReqShippingProductPrice.text = getString(R.string.format_origin_price, FormatUtil.getMoneyType(mLuckyBoxPurchaseItem!!.productPrice.toString()))

        binding.editLuckyBoxReqShippingUsePoint.setSingleLine()
        binding.editLuckyBoxReqShippingUsePoint.addTextChangedListener {
            if (it.toString().isNotEmpty()) {

                val totalPrice = totalPrice + totalDeliveryPrice

                if (LoginInfoManager.getInstance().member!!.point!!.toInt() > totalPrice) {
                    if (it.toString().toInt() > totalPrice) {
                        binding.editLuckyBoxReqShippingUsePoint.setText(totalPrice.toString())
                    }
                } else {
                    if (it.toString().toInt() > LoginInfoManager.getInstance().member!!.point!!.toInt()) {
                        binding.editLuckyBoxReqShippingUsePoint.setText(LoginInfoManager.getInstance().member!!.point!!.toInt().toString())
                    }
                }
            }
            setTotalPrice()
        }

        binding.textLuckyBoxReqShippingUsePointAll.setOnClickListener {
            binding.editLuckyBoxReqShippingUsePoint.setText(LoginInfoManager.getInstance().member!!.point!!.toInt().toString())
        }

        binding.textLuckyBoxReqShippingProvinsi.setOnClickListener {

            if(mProvinsiList == null){
                return@setOnClickListener
            }

            val contentList = arrayListOf<String>()
            for (provinsi in mProvinsiList!!) {
                contentList.add(provinsi.provinsi!!)
            }
            val builder = AlertBuilder.Builder()
            builder.setContents(contentList)
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    val provinsi = mProvinsiList!![event_alert.value - 1]
                    binding.textLuckyBoxReqShippingProvinsi.text = provinsi.provinsi!!

                    mKabkotaList = null
                    mKabkota = null
                    binding.textLuckyBoxReqShippingKota.text = ""

                    mKecamatanList = null
                    mKecamatan = null
                    binding.textLuckyBoxReqShippingKecamatan.text = ""

                    binding.textLuckyBoxReqShippingKodePos.text = ""
                    mKodePosList = null
                    mKodePos = null

                    mProvinsi = provinsi
                    getKabkotaList()

                }
            }).builder().show(this@LuckyBoxReqShippingActivity)
        }

        binding.textLuckyBoxReqShippingKota.setOnClickListener {
            if(mKabkotaList == null){
                showAlert(R.string.msg_select_provinsi)
                return@setOnClickListener
            }

            val contentList = arrayListOf<String>()
            for (kabkota in mKabkotaList!!) {
                contentList.add(kabkota.kabkota!!)
            }
            val builder = AlertBuilder.Builder()
            builder.setContents(contentList)
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    val kabkota = mKabkotaList!![event_alert.value - 1]
                    binding.textLuckyBoxReqShippingKota.text = kabkota.kabkota!!

                    mKecamatanList = null
                    mKecamatan = null
                    binding.textLuckyBoxReqShippingKecamatan.text = ""

                    mKodePosList = null
                    mKodePos = null
                    binding.textLuckyBoxReqShippingKodePos.text = ""

                    mKabkota = kabkota
                    getKecamatanList()

                }
            }).builder().show(this@LuckyBoxReqShippingActivity)
        }

        binding.textLuckyBoxReqShippingKecamatan.setOnClickListener {
            if(mKecamatanList == null){
                showAlert(R.string.msg_select_kabkota)
                return@setOnClickListener
            }

            val contentList = arrayListOf<String>()
            for (kecamatan in mKecamatanList!!) {
                contentList.add(kecamatan.kecamatan!!)
            }
            val builder = AlertBuilder.Builder()
            builder.setContents(contentList)
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    val kecamatan = mKecamatanList!![event_alert.value - 1]
                    binding.textLuckyBoxReqShippingKecamatan.text = kecamatan.kecamatan!!

                    mKodePosList = null
                    mKodePos = null
                    binding.textLuckyBoxReqShippingKodePos.text = ""

                    mKecamatan = kecamatan
                    getKodePoList()

                }
            }).builder().show(this@LuckyBoxReqShippingActivity)
        }

        binding.textLuckyBoxReqShippingKodePos.setOnClickListener {
            if(mKodePosList == null){
                showAlert(R.string.msg_select_kabkota)
                return@setOnClickListener
            }

            val contentList = arrayListOf<String>()
            for (kodepos in mKodePosList!!) {
                contentList.add("${kodepos.kelurahan!!}(${kodepos.kodePos!!})")
            }
            val builder = AlertBuilder.Builder()
            builder.setContents(contentList)
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    val kodePos = mKodePosList!![event_alert.value - 1]
                    binding.textLuckyBoxReqShippingKodePos.text = kodePos.kodePos!!
                    mKodePos = kodePos

                }
            }).builder().show(this@LuckyBoxReqShippingActivity)
        }

        getDelivery()
        getData()
        getPoint()
    }

    private fun getDelivery(){
        ApiBuilder.create().getMemberDelivery().setCallback(object : PplusCallback<NewResultResponse<MemberDelivery>>{
            override fun onResponse(call: Call<NewResultResponse<MemberDelivery>>?, response: NewResultResponse<MemberDelivery>?) {
                if(response?.result != null){
                    val memberDelivery = response.result!!
                    binding.editLuckyBoxReqShippingName.setText(memberDelivery.receiverName)
                    binding.editLuckyBoxReqShippingFamilyName.setText(memberDelivery.receiverFamilyName)
                    binding.editLuckyBoxReqShippingAddress1.setText(memberDelivery.receiverAddress)
                    binding.editLuckyBoxReqShippingAddress2.setText(memberDelivery.receiverAddress2)
                    binding.textLuckyBoxReqShippingProvinsi.setText(memberDelivery.receiverProvinsi)
                    binding.textLuckyBoxReqShippingKota.setText(memberDelivery.receiverKabkota)
                    binding.textLuckyBoxReqShippingKecamatan.setText(memberDelivery.receiverKecamatan)
                    binding.textLuckyBoxReqShippingKodePos.setText(memberDelivery.receiverPostCode)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<MemberDelivery>>?, t: Throwable?, response: NewResultResponse<MemberDelivery>?) {

            }
        }).build().call()
    }

    private fun pay() {
        if (mProduct!!.useOption != null && mProduct!!.useOption!! && mSelectLuckyBoxPurchaseItemOption == null) {
            showAlert(R.string.msg_select_option)
            return
        }

        val name = binding.editLuckyBoxReqShippingName.text.toString().trim()
        if (StringUtils.isEmpty(name)) {
            showAlert(R.string.msg_input_name)
            return
        }

        val familyName = binding.editLuckyBoxReqShippingFamilyName.text.toString().trim()
        if (StringUtils.isEmpty(familyName)) {
            showAlert(R.string.msg_input_family_name)
            return
        }

        val address1 = binding.editLuckyBoxReqShippingAddress1.text.toString().trim()
        if (StringUtils.isEmpty(address1)) {
            showAlert(R.string.msg_input_address1)
            return
        }

        val address2 = binding.editLuckyBoxReqShippingAddress2.text.toString().trim()
        if (StringUtils.isEmpty(address2)) {
            showAlert(R.string.msg_input_address2)
            return
        }

        val provinsi = binding.textLuckyBoxReqShippingProvinsi.text.toString().trim()
        if (StringUtils.isEmpty(provinsi)) {
            showAlert(R.string.msg_select_provinsi)
            return
        }

        val kabkota = binding.textLuckyBoxReqShippingKota.text.toString().trim()
        if (StringUtils.isEmpty(kabkota)) {
            showAlert(R.string.msg_select_kabkota)
            return
        }

        val kecamatan = binding.textLuckyBoxReqShippingKecamatan.text.toString().trim()
        if (StringUtils.isEmpty(kecamatan)) {
            showAlert(R.string.msg_select_kecamatan)
            return
        }

        val kodePos = binding.textLuckyBoxReqShippingKodePos.text.toString().trim()

        if (StringUtils.isEmpty(kodePos)) {
            showAlert(R.string.msg_select_kode_pos)
            return
        }

        val receiverTel = binding.editLuckyBoxReqShippingTel.text.toString().trim()
        if (StringUtils.isEmpty(receiverTel)) {
            showAlert(R.string.msg_input_phone_number)
            return
        }

        val deliveryPurchase = LuckyBoxDeliveryPurchase()
        deliveryPurchase.price = (totalPrice + totalDeliveryPrice).toFloat()
        deliveryPurchase.pgPrice = pgPrice
        deliveryPurchase.usePoint = usePoint

        deliveryPurchase.luckyboxPurchaseItemSeqNo = mLuckyBoxPurchaseItem!!.seqNo
        deliveryPurchase.userKey = LoginInfoManager.getInstance().member!!.userKey
        deliveryPurchase.status = 1

        if (mSelectLuckyBoxPurchaseItemOption != null) {
            mSelectLuckyBoxPurchaseItemOption!!.luckyboxPurchaseItemSeqNo = mLuckyBoxPurchaseItem!!.seqNo
            deliveryPurchase.selectOption = mSelectLuckyBoxPurchaseItemOption
        }

        val luckyBoxDelivery = LuckyBoxDelivery()
        luckyBoxDelivery.luckyboxPurchaseItemSeqNo = mLuckyBoxPurchaseItem!!.seqNo
        luckyBoxDelivery.type = mProduct!!.deliveryType
        luckyBoxDelivery.method = 3
        luckyBoxDelivery.paymentMethod = "before"

        luckyBoxDelivery.deliveryAddFee1 = mDeliveryAddFee1
        luckyBoxDelivery.deliveryAddFee2 = mDeliveryAddFee2
        luckyBoxDelivery.deliveryFee = mDeliveryFee

        luckyBoxDelivery.receiverName = name
        luckyBoxDelivery.receiverFamilyName = familyName
        luckyBoxDelivery.receiverAddress = address1
        luckyBoxDelivery.receiverAddress2 = address2
        luckyBoxDelivery.receiverProvinsi = provinsi
        luckyBoxDelivery.receiverKabkota = kabkota
        luckyBoxDelivery.receiverKecamatan = kecamatan
        luckyBoxDelivery.receiverPostCode = kodePos
        luckyBoxDelivery.receiverTel = receiverTel

        deliveryPurchase.selectDelivery = luckyBoxDelivery

        showProgress("")
        ApiBuilder.create().saveLuckyBoxDeliveryPurchase(deliveryPurchase).setCallback(object : PplusCallback<NewResultResponse<LuckyBoxDeliveryPurchase>> {
            override fun onResponse(call: Call<NewResultResponse<LuckyBoxDeliveryPurchase>>?, response: NewResultResponse<LuckyBoxDeliveryPurchase>?) {
                hideProgress()
                setEvent("wowbox_requestPayShipping")
                if (response?.result != null) {
                    val luckyBoxDeliveryPurchase = response.result!!
                    if (luckyBoxDeliveryPurchase.paymentMethod == "point") {
                        val intent = Intent(this@LuckyBoxReqShippingActivity, LuckyBoxReqShippingCompleteActivity::class.java)
                        intent.putExtra(Const.DATA, luckyBoxDeliveryPurchase)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        resultLauncher.launch(intent)
                    } else {
                        val intent = Intent(this@LuckyBoxReqShippingActivity, LuckyBoxPgActivity::class.java)
                        intent.putExtra(Const.KEY, "delivery")
                        intent.putExtra(Const.DATA, luckyBoxDeliveryPurchase)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        resultLauncher.launch(intent)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<LuckyBoxDeliveryPurchase>>?, t: Throwable?, response: NewResultResponse<LuckyBoxDeliveryPurchase>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getData() {
        val params = HashMap<String, String>()
        showProgress("")
        ApiBuilder.create().getProduct(mLuckyBoxPurchaseItem!!.productSeqNo!!).setCallback(object : PplusCallback<NewResultResponse<Product>> {
            override fun onResponse(call: Call<NewResultResponse<Product>>?, response: NewResultResponse<Product>?) {
                hideProgress()
                if (response?.result != null) {
                    mProduct = response.result

                    when (mProduct!!.deliveryType) { // 1:무료, 2:유료, 3:조건부 무료
                        1 -> {
                            mDeliveryFee = 15000f
                        }
                        else -> {
                            mDeliveryFee = mProduct!!.deliveryFee
                            if(mDeliveryFee!! < 15000f){
                                mDeliveryFee = 15000f
                            }
                        }
                    }

                    if (mProduct!!.useOption != null && mProduct!!.useOption!!) {
                        binding.layoutLuckyBoxReqShippingOption.visibility = View.VISIBLE
                        binding.textLuckyBoxReqShippingProductOption.visibility = View.VISIBLE
                        getOption()
                    } else {
                        binding.layoutLuckyBoxReqShippingOption.visibility = View.GONE
                        binding.textLuckyBoxReqShippingProductOption.visibility = View.GONE
                        setTotalPrice()
                    }

                    getProvinsiList()
                } else {
                    showAlert(R.string.msg_not_exist_goods)
                    finish()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Product>>?, t: Throwable?, response: NewResultResponse<Product>?) {
                hideProgress()
            }
        }).build().call()
    }

    private var mProvinsiList: List<Provinsi>? = null
    private var mProvinsi: Provinsi? = null
    private fun getProvinsiList(){
        showProgress("")
        ApiBuilder.create().getProvinsiList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Provinsi>>>{
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Provinsi>>>?, response: NewResultResponse<ListResultResponse<Provinsi>>?) {
                hideProgress()
                if(response?.result != null){
                    mProvinsiList = response.result!!.list!!
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Provinsi>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Provinsi>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private var mKabkotaList: List<Kabkota>? = null
    private var mKabkota: Kabkota? = null
    private fun getKabkotaList(){
        val params = HashMap<String, String>()
        params["parentId"] = mProvinsi!!.id.toString()
        showProgress("")
        ApiBuilder.create().getKabkotaList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Kabkota>>>{
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Kabkota>>>?, response: NewResultResponse<ListResultResponse<Kabkota>>?) {
                hideProgress()
                if(response?.result != null){
                    mKabkotaList = response.result!!.list!!
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Kabkota>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Kabkota>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private var mKecamatanList: List<Kecamatan>? = null
    private var mKecamatan: Kecamatan? = null
    private fun getKecamatanList(){
        val params = HashMap<String, String>()
        params["parentId"] = mKabkota!!.id.toString()
        showProgress("")
        ApiBuilder.create().getKecamatanList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Kecamatan>>>{
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Kecamatan>>>?, response: NewResultResponse<ListResultResponse<Kecamatan>>?) {
                hideProgress()
                if(response?.result != null){
                    mKecamatanList = response.result!!.list!!
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Kecamatan>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Kecamatan>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private var mKodePosList: List<KodePos>? = null
    private var mKodePos: KodePos? = null
    private fun getKodePoList(){
        val params = HashMap<String, String>()
        params["parentId"] = mKecamatan!!.id.toString()
        showProgress("")
        ApiBuilder.create().getKodePosList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<KodePos>>>{
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<KodePos>>>?, response: NewResultResponse<ListResultResponse<KodePos>>?) {
                hideProgress()
                if(response?.result != null){
                    mKodePosList = response.result!!.list!!
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<KodePos>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<KodePos>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getPoint() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                binding.textLuckyBoxReqShippingRetentionPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_point, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point.toString())))
            }
        })
    }

    private fun getOption() {
        val params = HashMap<String, String>()

        params["productSeqNo"] = mProduct!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getProductOption(params).setCallback(object : PplusCallback<NewResultResponse<ProductOptionTotal>> {
            override fun onResponse(call: Call<NewResultResponse<ProductOptionTotal>>?, response: NewResultResponse<ProductOptionTotal>?) {
                hideProgress()
                if (response?.result != null) {
                    mProductOptionTotal = response.result
                    initOption()

                } else {
                    binding.layoutLuckyBoxReqShippingOption.visibility = View.GONE
                    binding.textLuckyBoxReqShippingProductOption.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ProductOptionTotal>>?, t: Throwable?, response: NewResultResponse<ProductOptionTotal>?) {
                hideProgress()
            }
        }).build().call()
    }

    var mItemList = arrayListOf<ProductOptionItem>()
    var mOptionTextList = arrayListOf<TextView>()
    var mSelectLuckyBoxPurchaseItemOption: LuckyBoxPurchaseItemOption? = null

    private fun initOption() {
        val productOptionList = mProductOptionTotal!!.option
        val productOptionDetailList = mProductOptionTotal!!.detail

        binding.layoutLuckyBoxReqShippingOption.removeAllViews()
        if (!productOptionList.isNullOrEmpty()) {
            mItemList = arrayListOf()
            mOptionTextList = arrayListOf<TextView>()
            for (i in productOptionList.indices) {
                mItemList.add(ProductOptionItem())
                val productOption = productOptionList[i]
                val optionBinding = ItemLuckyBoxOptionBinding.inflate(layoutInflater)
                optionBinding.textOptionName.setTextColor(ResourceUtil.getColor(this, R.color.color_737373))
                optionBinding.textOptionName.text = productOption.name
                mOptionTextList.add(optionBinding.textOptionName)
                optionBinding.root.setOnClickListener {

                    when (mProduct!!.optionType) {
                        "single" -> {
                            val selectedDetailList = productOptionDetailList!!.filter {
                                it.optionSeqNo == productOption.seqNo
                            }

                            LogUtil.e(LOG_TAG, "size : {}", selectedDetailList.size)
                            val intent = Intent(this, AlertProductOptionActivity::class.java)
                            intent.putParcelableArrayListExtra(Const.DETAIL, selectedDetailList as ArrayList)
                            intent.putExtra(Const.POSITION, i)
                            intent.putExtra(Const.NAME, productOption.name)
                            intent.putExtra(Const.OPTION_TYPE, mProduct!!.optionType)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            optionDetailLauncher.launch(intent)
                        }

                        "union" -> {
                            if (i != 0 && mItemList[i - 1].seqNo == null) {
                                ToastUtil.show(this, R.string.msg_select_before_option)
                                return@setOnClickListener
                            }
                            if (i < productOptionList.size - 1) {
                                val intent = Intent(this, AlertProductOptionActivity::class.java)
                                intent.putParcelableArrayListExtra(Const.ITEM, productOption.items as ArrayList)
                                intent.putExtra(Const.POSITION, i)
                                intent.putExtra(Const.NAME, productOption.name)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                optionLauncher.launch(intent)
                            } else {
                                val selectedDetailList = productOptionDetailList!!.filter {
                                    when (productOptionList.size) {
                                        1 -> {
                                            true
                                        }

                                        2 -> {
                                            it.depth1ItemSeqNo == mItemList[0].seqNo
                                        }

                                        else -> {
                                            it.depth1ItemSeqNo == mItemList[0].seqNo && it.depth2ItemSeqNo == mItemList[1].seqNo
                                        }
                                    }
                                }

                                val intent = Intent(this, AlertProductOptionActivity::class.java)
                                intent.putParcelableArrayListExtra(Const.DETAIL, selectedDetailList as ArrayList)
                                intent.putExtra(Const.POSITION, i)
                                intent.putExtra(Const.NAME, productOption.name)
                                intent.putExtra(Const.OPTION_TYPE, mProduct!!.optionType)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                optionDetailLauncher.launch(intent)
                            }
                        }
                    }
                }
                binding.layoutLuckyBoxReqShippingOption.addView(optionBinding.root)
            }
        } else {
            binding.layoutLuckyBoxReqShippingOption.visibility = View.GONE
            binding.textLuckyBoxReqShippingProductOption.visibility = View.GONE
        }
    }

    var totalPrice = 0
    var totalDeliveryPrice = 0
    var usePoint = 0
    var pgPrice = 0
    private fun setTotalPrice() {
        totalPrice = 0
        totalDeliveryPrice = 0
        if (mSelectLuckyBoxPurchaseItemOption != null) {
            totalPrice += mSelectLuckyBoxPurchaseItemOption!!.price!!.toInt()
        }

        binding.textLuckyBoxReqOptionPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((totalPrice).toString()))

        totalDeliveryPrice = (mDeliveryFee!! + mDeliveryAddFee1!! + mDeliveryAddFee2!!).toInt()

        if (totalDeliveryPrice > 0) {
            binding.textLuckyBoxReqDeliveryFee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(totalDeliveryPrice.toString()))
        } else {
            binding.textLuckyBoxReqDeliveryFee.text = getString(R.string.word_free_ship)
        }

        val usePointStr = binding.editLuckyBoxReqShippingUsePoint.text.toString().trim()
        if (StringUtils.isNotEmpty(usePointStr)) {
            usePoint = usePointStr.toInt()
        } else {
            usePoint = 0
        }

        if (usePoint > 0) {
            binding.textLuckyBoxReqUsePoint.text = "- ${getString(R.string.format_point_unit, FormatUtil.getMoneyType(usePoint.toString()))}"
        } else {
            binding.textLuckyBoxReqUsePoint.text = getString(R.string.format_point_unit, "0")
        }


        pgPrice = totalPrice + totalDeliveryPrice - usePoint

        binding.textLuckyBoxReqPgPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((pgPrice).toString()))
    }

    val optionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {

                val item = PplusCommonUtil.getParcelableExtra(data, Const.DATA, ProductOptionItem::class.java)

                val position = data.getIntExtra(Const.POSITION, 0)
                mItemList[position] = item!!
                mOptionTextList[position].text = item.item
                mOptionTextList[position].setTextColor(ResourceUtil.getColor(this, R.color.color_232323))
            }
        }
    }

    val optionDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {

                val detail = PplusCommonUtil.getParcelableExtra(data, Const.DATA, ProductOptionDetail::class.java)!!

                var isContain = false

                if (!isContain) {
                    mSelectLuckyBoxPurchaseItemOption = LuckyBoxPurchaseItemOption()
                    mSelectLuckyBoxPurchaseItemOption!!.quantity = 1
                    mSelectLuckyBoxPurchaseItemOption!!.productSeqNo = mProduct!!.seqNo
                    mSelectLuckyBoxPurchaseItemOption!!.productOptionDetailSeqNo = detail.seqNo
                    mSelectLuckyBoxPurchaseItemOption!!.price = detail.price!!
                    if (detail.item1 != null) {
                        mSelectLuckyBoxPurchaseItemOption!!.depth1 = detail.item1!!.item
                    }
                    if (detail.item2 != null) {
                        mSelectLuckyBoxPurchaseItemOption!!.depth2 = detail.item2!!.item
                    }

                    var option = ""
                    when (mProduct!!.optionType) {
                        "single" -> {
                            if (mSelectLuckyBoxPurchaseItemOption!!.price != null && mSelectLuckyBoxPurchaseItemOption!!.price!! > 0) {
                                option = "${mSelectLuckyBoxPurchaseItemOption!!.depth1}(+${getString(R.string.format_money_unit, FormatUtil.getMoneyType(mSelectLuckyBoxPurchaseItemOption!!.price.toString()))})"
                            } else {
                                option = "${mSelectLuckyBoxPurchaseItemOption!!.depth1}"
                            }

                        }

                        "union" -> {
                            when (mItemList.size) {
                                1 -> {
                                    if (mSelectLuckyBoxPurchaseItemOption!!.price != null && mSelectLuckyBoxPurchaseItemOption!!.price!! > 0) {
                                        option = "${mSelectLuckyBoxPurchaseItemOption!!.depth1}(+${getString(R.string.format_money_unit, FormatUtil.getMoneyType(mSelectLuckyBoxPurchaseItemOption!!.price.toString()))})"
                                    } else {
                                        option = "${mSelectLuckyBoxPurchaseItemOption!!.depth1}"
                                    }
                                }

                                else -> {
                                    if (mSelectLuckyBoxPurchaseItemOption!!.price != null && mSelectLuckyBoxPurchaseItemOption!!.price!! > 0) {
                                        option = "${mSelectLuckyBoxPurchaseItemOption!!.depth1} / ${mSelectLuckyBoxPurchaseItemOption!!.depth2}(+${getString(R.string.format_money_unit, FormatUtil.getMoneyType(mSelectLuckyBoxPurchaseItemOption!!.price.toString()))})"
                                    } else {
                                        option = "${mSelectLuckyBoxPurchaseItemOption!!.depth1} / ${mSelectLuckyBoxPurchaseItemOption!!.depth2}"
                                    }
                                }
                            }
                        }
                    }

                    binding.textLuckyBoxReqShippingProductOption.text = option
                }

                setTotalPrice()

                binding.layoutLuckyBoxReqShippingOption.visibility = View.VISIBLE
                binding.textLuckyBoxReqShippingProductOption.visibility = View.VISIBLE
                initOption()
            }
        }
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(RESULT_OK)
        finish()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_req_shipping), ToolbarOption.ToolbarMenu.LEFT)
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