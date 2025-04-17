package com.pplus.prnumberuser.apps.page.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.shippingsite.ui.SearchAddressActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.EventWin
import com.pplus.prnumberuser.core.network.model.dto.SearchAddressJuso
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityPageEventAddressSaveBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class PageEventAddressSaveActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPageEventAddressSaveBinding

    override fun getLayoutView(): View {
        binding = ActivityPageEventAddressSaveBinding.inflate(layoutInflater)
        return binding.root
    }

    var mEventWin:EventWin? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.editPageEventAddressSaveAddressDetail.setSingleLine()

        mEventWin = intent.getParcelableExtra(Const.DATA)

        binding.textPageEventAddressSavePostCode.setOnClickListener {
            val intent = Intent(this, SearchAddressActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            searchAddressLauncher.launch(intent)
        }

        binding.textPageEventAddressSave.setOnClickListener {

            val receiverName = binding.editPageEventAddressSaveReceiverName.text.toString().trim()
            if(!StringUtils.isNotEmpty(receiverName)){
                showAlert(R.string.msg_input_shipping_site_receiver_name)
                return@setOnClickListener
            }

            val receiverTel = binding.editPageEventAddressSaveReceiverTel.text.toString().trim()
            if(!StringUtils.isNotEmpty(receiverTel)){
                showAlert(R.string.msg_input_shipping_site_receiver_tel)
                return@setOnClickListener
            }
            val postCode = binding.textPageEventAddressSavePostCode.text.toString().trim()

            if(!StringUtils.isNotEmpty(postCode)){
                showAlert(R.string.msg_input_post_code)
                return@setOnClickListener
            }

            val address = binding.textPageEventAddressSaveAddress.text.toString().trim()
            if(!StringUtils.isNotEmpty(address)){
                showAlert(R.string.msg_input_address)
                return@setOnClickListener
            }

            val addressDetail = binding.editPageEventAddressSaveAddressDetail.text.toString().trim()
            if(!StringUtils.isNotEmpty(addressDetail)){
                showAlert(R.string.msg_input_detail_address)
                return@setOnClickListener
            }


            mEventWin!!.recipient = receiverName
            mEventWin!!.deliveryPostCode = postCode
            mEventWin!!.deliveryAddress = address + " " + addressDetail
            mEventWin!!.deliveryPhone = receiverTel

            showProgress("")
            ApiBuilder.create().updateEventWinAddress(mEventWin!!).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                    hideProgress()
                    setResult(Activity.RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable?, response: NewResultResponse<Any>) {
                    hideProgress()
                }
            }).build().call()

        }
    }

    val searchAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val juso = data.getParcelableExtra<SearchAddressJuso>(Const.ADDRESS)
                binding.textPageEventAddressSaveAddress.text = juso!!.roadAddrPart1
                binding.textPageEventAddressSavePostCode.text = juso.zipNo
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reg_shipping_address), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}