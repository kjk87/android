package com.root37.buflexz.apps.product.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.terms.ui.TermsListActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.EmsCountry
import com.root37.buflexz.core.network.model.dto.MemberDelivery
import com.root37.buflexz.core.network.model.dto.Product
import com.root37.buflexz.core.network.model.dto.ProductPurchase
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.core.util.ToastUtil
import com.root37.buflexz.databinding.ActivityProductPurchaseBinding
import retrofit2.Call

class ProductPurchaseActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProductPurchaseBinding

    override fun getLayoutView(): View {
        binding = ActivityProductPurchaseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mEmsCountryList: List<EmsCountry>? = null
    var mMemberDelivery: MemberDelivery? = null
    var mSelectEmsCountry: EmsCountry? = null
    private lateinit var mProduct: Product

    override fun initializeView(savedInstanceState: Bundle?) {
        mProduct = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Product::class.java)!!

        binding.editProductPurchaseReceiver.setSingleLine()
        binding.editProductPurchaseZipCode.setSingleLine()
        binding.editProductPurchaseAddressDetail.setSingleLine()
        binding.editProductPurchaseCity.setSingleLine()
        binding.editProductPurchaseState.setSingleLine()
        binding.editProductPurchaseContact.setSingleLine()

        if (mProduct.imageList != null && mProduct.imageList!!.isNotEmpty()) {
            Glide.with(this).load(mProduct.imageList!![0].image).apply(RequestOptions().centerCrop()).into(binding.imageProductPurchaseProduct)
        }

        binding.textProductPurchaseProductTitle.text = mProduct.title
        binding.textProductPurchaseProductPrice.text = FormatUtil.getMoneyTypeFloat(mProduct.price.toString())

        binding.textProductPurchaseAgree.setOnClickListener {
            val intent = Intent(this, TermsListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutContactUs.textContactUsEmailCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText(getString(R.string.word_contact_us_en), getString(R.string.cs_email))
            clipboard.setPrimaryClip(clip)
            ToastUtil.show(this, R.string.msg_copied_clipboard)
        }

        binding.textProductPurchaseCountry.setOnClickListener {

            if(mEmsCountryList == null){
                return@setOnClickListener
            }
            val contentsList = arrayListOf<String>()
            for(emsCountry in mEmsCountryList!!){
                contentsList.add(emsCountry.country!!)
            }

            val builder = AlertBuilder.Builder()
            builder.setContents(contentsList)
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    mSelectEmsCountry = mEmsCountryList!![event_alert.value - 1]
                    binding.textProductPurchaseCountry.text = mSelectEmsCountry!!.country
                    val nation = NationManager.getInstance().nationMap!![mSelectEmsCountry!!.countryCode]!!
                    binding.textProductPurchaseContactNation.text = nation.nationNo

                }
            }).builder().show(this)
        }

        binding.textProductPurchasePurchase.setOnClickListener {
            val receiver = binding.editProductPurchaseReceiver.text.toString().trim()
            if (StringUtils.isEmpty(receiver)) {
                showAlert(R.string.msg_input_receiver)
                return@setOnClickListener
            }

            if(mSelectEmsCountry == null){
                showAlert(R.string.msg_select_shipping_country)
                return@setOnClickListener
            }

            val zipCode = binding.editProductPurchaseZipCode.text.toString().trim()
            if (StringUtils.isEmpty(zipCode)) {
                showAlert(R.string.msg_input_zip_code)
                return@setOnClickListener
            }

            val addressDetail = binding.editProductPurchaseAddressDetail.text.toString().trim()
            if (StringUtils.isEmpty(addressDetail)) {
                showAlert(R.string.msg_input_detail_address)
                return@setOnClickListener
            }

            val city = binding.editProductPurchaseCity.text.toString().trim()
            if (StringUtils.isEmpty(city)) {
                showAlert(R.string.msg_input_city)
                return@setOnClickListener
            }

            val state = binding.editProductPurchaseState.text.toString().trim()
            if (StringUtils.isEmpty(state)) {
                showAlert(R.string.msg_input_state)
                return@setOnClickListener
            }

            val contact = binding.editProductPurchaseContact.text.toString().trim()
            if (StringUtils.isEmpty(contact)) {
                showAlert(R.string.msg_input_contact)
                return@setOnClickListener
            }

            if(mMemberDelivery == null){
                mMemberDelivery = MemberDelivery()
                mMemberDelivery!!.userKey = LoginInfoManager.getInstance().member!!.userKey
            }
            mMemberDelivery!!.method = "ems"

            val nation = NationManager.getInstance().nationMap!![mSelectEmsCountry!!.countryCode]!!
            mMemberDelivery!!.nation = nation.code
            mMemberDelivery!!.nationKr = nation.name
            mMemberDelivery!!.telNation = nation.nationNo
            mMemberDelivery!!.receiverName = receiver
            mMemberDelivery!!.tel1 = contact
            mMemberDelivery!!.zipcode = zipCode
            mMemberDelivery!!.detail = addressDetail
            mMemberDelivery!!.city = city
            mMemberDelivery!!.state = state

            val params = ProductPurchase()
            params.productSeqNo = mProduct.seqNo
            params.delivery = mMemberDelivery

            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.setTitle(getString(R.string.msg_alert_product_purchase_title))
            builder.addContents(AlertData.MessageData(getString(R.string.html_alert_product_purchase_desc), AlertBuilder.MESSAGE_TYPE.HTML, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {

                            showProgress("")
                            ApiBuilder.create().postProductPurchase(params).setCallback(object : PplusCallback<NewResultResponse<ProductPurchase>>{
                                override fun onResponse(call: Call<NewResultResponse<ProductPurchase>>?, response: NewResultResponse<ProductPurchase>?) {
                                    hideProgress()
                                    showAlert(R.string.msg_complete_req_shipping)
                                    setResult(RESULT_OK)
                                    finish()
                                }

                                override fun onFailure(call: Call<NewResultResponse<ProductPurchase>>?, t: Throwable?, response: NewResultResponse<ProductPurchase>?) {
                                    hideProgress()
                                    if (response?.code == 404) {
                                        showAlert(R.string.msg_not_found_product)
                                    } else if (response?.code == 517) {
                                        showAlert(R.string.msg_lack_point)
                                    }
                                }
                            }).build().call()
                        }

                        else -> {}
                    }
                }
            })
            builder.builder().show(this)


        }

        getEmsCountryList()
    }

    private fun getEmsCountryList(){
        showProgress("")
        ApiBuilder.create().getEmsCountryList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<EmsCountry>>>{
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<EmsCountry>>>?, response: NewResultResponse<ListResultResponse<EmsCountry>>?) {
                hideProgress()
                if(response?.result != null && response.result!!.list != null){
                    mEmsCountryList = response.result!!.list
                }
                getMemberDelivery()
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<EmsCountry>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<EmsCountry>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getMemberDelivery() {
        showProgress("")
        ApiBuilder.create().getMemberDelivery().setCallback(object : PplusCallback<NewResultResponse<MemberDelivery>> {
            override fun onResponse(call: Call<NewResultResponse<MemberDelivery>>?, response: NewResultResponse<MemberDelivery>?) {
                hideProgress()
                if (response?.result != null) {
                    mMemberDelivery = response.result
                    binding.editProductPurchaseReceiver.setText(mMemberDelivery!!.receiverName)

                    for(emsCountry in mEmsCountryList!!){
                        if(emsCountry.countryCode == mMemberDelivery!!.nation){
                            mSelectEmsCountry = emsCountry
                            binding.textProductPurchaseCountry.text = emsCountry.country
                            break
                        }
                    }

                    binding.editProductPurchaseCity.setText(mMemberDelivery!!.city)
                    binding.editProductPurchaseAddressDetail.setText(mMemberDelivery!!.detail)
                    binding.textProductPurchaseContactNation.setText(mMemberDelivery!!.telNation)
                    binding.editProductPurchaseContact.setText(mMemberDelivery!!.tel1)
                } else {
                    mMemberDelivery = null
                }
            }

            override fun onFailure(call: Call<NewResultResponse<MemberDelivery>>?, t: Throwable?, response: NewResultResponse<MemberDelivery>?) {
                hideProgress()
            }
        }).build().call()
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