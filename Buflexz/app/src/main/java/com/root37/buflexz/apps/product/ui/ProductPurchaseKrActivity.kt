package com.root37.buflexz.apps.product.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
import com.root37.buflexz.apps.common.ui.SearchAddressActivity
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.terms.ui.TermsListActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Address
import com.root37.buflexz.core.network.model.dto.MemberDelivery
import com.root37.buflexz.core.network.model.dto.Product
import com.root37.buflexz.core.network.model.dto.ProductPurchase
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.core.util.ToastUtil
import com.root37.buflexz.databinding.ActivityProductPurchaseKrBinding
import retrofit2.Call

class ProductPurchaseKrActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProductPurchaseKrBinding

    override fun getLayoutView(): View {
        binding = ActivityProductPurchaseKrBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mAddress: Address? = null
    var mMemberDelivery: MemberDelivery? = null
    lateinit var mProduct: Product
    override fun initializeView(savedInstanceState: Bundle?) {

        mProduct = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Product::class.java)!!


        binding.editProductPurchaseKrReceiver.setSingleLine()
        binding.editProductPurchaseKrContact.setSingleLine()
        binding.editProductPurchaseKrAddressDetail.setSingleLine()

        binding.textProductPurchaseKrAddressSearch.setOnClickListener {
            val intent = Intent(this, SearchAddressActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            searchAddressLauncher.launch(intent)
        }

        if (mProduct.imageList != null && mProduct.imageList!!.isNotEmpty()) {
            Glide.with(this).load(mProduct.imageList!![0].image).apply(RequestOptions().centerCrop()).into(binding.imageProductPurchaseKrProduct)
        }

        binding.textProductPurchaseKrProductTitle.text = mProduct.title
        binding.textProductPurchaseKrProductPrice.text = FormatUtil.getMoneyTypeFloat(mProduct.price.toString())

        binding.textProductPurchaseKrAgree.setOnClickListener {
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

        binding.textProductPurchaseKrPurchase.setOnClickListener {
            val receiver = binding.editProductPurchaseKrReceiver.text.toString().trim()
            if (StringUtils.isEmpty(receiver)) {
                showAlert(R.string.msg_input_receiver)
                return@setOnClickListener
            }

            val contact = binding.editProductPurchaseKrContact.text.toString().trim()
            if (StringUtils.isEmpty(contact)) {
                showAlert(R.string.msg_input_contact)
                return@setOnClickListener
            }

            if (!FormatUtil.isPhoneNumber(contact)) {
                showAlert(R.string.msg_invalid_phone_number)
                return@setOnClickListener
            }

            if (mAddress == null && mMemberDelivery == null) {
                showAlert(R.string.msg_input_address)
                return@setOnClickListener
            }

            val address = binding.textProductPurchaseKrAddress.text.toString().trim()
            if (StringUtils.isEmpty(address)) {
                showAlert(R.string.msg_input_address)
                return@setOnClickListener
            }

            val addressDetail = binding.editProductPurchaseKrAddressDetail.text.toString().trim()
            if (StringUtils.isEmpty(addressDetail)) {
                showAlert(R.string.msg_input_detail_address)
                return@setOnClickListener
            }

            if(mMemberDelivery == null){
                mMemberDelivery = MemberDelivery()
                mMemberDelivery!!.userKey = LoginInfoManager.getInstance().member!!.userKey
            }
            mMemberDelivery!!.method = "general"

            val nation = NationManager.getInstance().nationMap!![LoginInfoManager.getInstance().member!!.nation]!!
            mMemberDelivery!!.nation = nation.code
            mMemberDelivery!!.nationKr = nation.name
            mMemberDelivery!!.telNation = nation.nationNo
            mMemberDelivery!!.receiverName = receiver
            mMemberDelivery!!.tel1 = contact

            if(mAddress != null){
                mMemberDelivery!!.zipcode = mAddress!!.postno
            }

            mMemberDelivery!!.addr1 = address
            mMemberDelivery!!.addr2 = addressDetail

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

        getMemberDelivery()
    }

    private fun getMemberDelivery() {
        showProgress("")
        ApiBuilder.create().getMemberDelivery().setCallback(object : PplusCallback<NewResultResponse<MemberDelivery>> {
            override fun onResponse(call: Call<NewResultResponse<MemberDelivery>>?, response: NewResultResponse<MemberDelivery>?) {
                hideProgress()
                if (response?.result != null) {
                    mMemberDelivery = response.result
                    binding.editProductPurchaseKrReceiver.setText(mMemberDelivery!!.receiverName)
                    binding.editProductPurchaseKrContact.setText(mMemberDelivery!!.tel1)
                    binding.textProductPurchaseKrAddress.text = mMemberDelivery!!.addr1
                    binding.editProductPurchaseKrAddressDetail.setText(mMemberDelivery!!.addr2)
                } else {
                    mMemberDelivery = null
                }
            }

            override fun onFailure(call: Call<NewResultResponse<MemberDelivery>>?, t: Throwable?, response: NewResultResponse<MemberDelivery>?) {
                hideProgress()
            }
        }).build().call()
    }

    private val searchAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            mAddress = PplusCommonUtil.getParcelableExtra(result.data!!, Const.DATA, Address::class.java)
            binding.textProductPurchaseKrAddress.text = mAddress!!.roadAddress

        }
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