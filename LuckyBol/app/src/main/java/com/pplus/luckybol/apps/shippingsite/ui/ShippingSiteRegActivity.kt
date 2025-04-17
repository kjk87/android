package com.pplus.luckybol.apps.shippingsite.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.utils.part.utils.StringUtils
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.SearchAddressJuso
import com.pplus.luckybol.core.network.model.dto.ShippingSite
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityShippingSiteRegBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

class ShippingSiteRegActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityShippingSiteRegBinding

    override fun getLayoutView(): View {
        binding = ActivityShippingSiteRegBinding.inflate(layoutInflater)
        return binding.root
    }

    var mShippingSite:ShippingSite? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.editShippingSiteRegAddressDetail.setSingleLine()

        mShippingSite = intent.getParcelableExtra(Const.DATA)
        if(mShippingSite != null){
            binding.textShippingSiteRegPostCode.text = mShippingSite!!.postCode
            binding.textShippingSiteRegAddress.text = mShippingSite!!.address
            binding.editShippingSiteRegAddressDetail.setText(mShippingSite!!.addressDetail)
            binding.editShippingSiteRegReceiverName.setText(mShippingSite!!.receiverName)
            binding.editShippingSiteRegReceiverTel.setText(mShippingSite!!.receiverTel)
        }

        binding.textShippingSiteRegPostCode.setOnClickListener {
            val intent = Intent(this, SearchAddressActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            searchAddressLauncher.launch(intent)
        }

        binding.textShippingSiteReg.setOnClickListener {

            val receiverName = binding.editShippingSiteRegReceiverName.text.toString().trim()
            if(!StringUtils.isNotEmpty(receiverName)){
                showAlert(R.string.msg_input_shipping_site_receiver_name)
                return@setOnClickListener
            }

            val receiverTel = binding.editShippingSiteRegReceiverTel.text.toString().trim()
            if(!StringUtils.isNotEmpty(receiverTel)){
                showAlert(R.string.msg_input_shipping_site_receiver_tel)
                return@setOnClickListener
            }
            val postCode = binding.textShippingSiteRegPostCode.text.toString().trim()

            if(!StringUtils.isNotEmpty(postCode)){
                showAlert(R.string.msg_input_post_code)
                return@setOnClickListener
            }

            val address = binding.textShippingSiteRegAddress.text.toString().trim()
            if(!StringUtils.isNotEmpty(address)){
                showAlert(R.string.msg_input_address)
                return@setOnClickListener
            }

            val addressDetail = binding.editShippingSiteRegAddressDetail.text.toString().trim()
            if(!StringUtils.isNotEmpty(addressDetail)){
                showAlert(R.string.msg_input_detail_address)
                return@setOnClickListener
            }

            val shippingSite = ShippingSite()
            shippingSite.memberSeqNo = LoginInfoManager.getInstance().user.no
            shippingSite.siteName = receiverName
            shippingSite.postCode = postCode
            shippingSite.address = address
            shippingSite.addressDetail = addressDetail
            shippingSite.receiverName = receiverName
            shippingSite.receiverTel = receiverTel

            showProgress("")
            if(mShippingSite != null){
                shippingSite.seqNo = mShippingSite!!.seqNo
                ApiBuilder.create().updateShippingSite(shippingSite).setCallback(object : PplusCallback<NewResultResponse<ShippingSite>>{
                    override fun onResponse(call: Call<NewResultResponse<ShippingSite>>?, response: NewResultResponse<ShippingSite>?) {
                        hideProgress()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<ShippingSite>>?, t: Throwable?, response: NewResultResponse<ShippingSite>?) {
                        hideProgress()
                    }
                }).build().call()
            }else{
                ApiBuilder.create().insertShippingSite(shippingSite).setCallback(object : PplusCallback<NewResultResponse<ShippingSite>>{
                    override fun onResponse(call: Call<NewResultResponse<ShippingSite>>?, response: NewResultResponse<ShippingSite>?) {
                        hideProgress()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<ShippingSite>>?, t: Throwable?, response: NewResultResponse<ShippingSite>?) {
                        hideProgress()
                    }
                }).build().call()
            }

        }
    }

    val searchAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val juso = data.getParcelableExtra<SearchAddressJuso>(Const.ADDRESS)
                binding.textShippingSiteRegAddress.text = juso!!.roadAddrPart1
                binding.textShippingSiteRegPostCode.text = juso.zipNo
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        val shippingSite = intent.getParcelableExtra<ShippingSite>(Const.DATA)
        if(shippingSite == null){
            toolbarOption.initializeDefaultToolbar(getString(R.string.word_add_shipping_site), ToolbarOption.ToolbarMenu.LEFT)
        }else{
            toolbarOption.initializeDefaultToolbar(getString(R.string.word_modify_shipping_site), ToolbarOption.ToolbarMenu.LEFT)
        }

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