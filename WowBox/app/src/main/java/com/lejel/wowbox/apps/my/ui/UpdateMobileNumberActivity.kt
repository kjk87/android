package com.lejel.wowbox.apps.my.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.verify.ui.SelectVerifyTypeActivity
import com.lejel.wowbox.apps.verify.ui.VerifySmsActivity
import com.lejel.wowbox.apps.verify.ui.VerifyWhatsAppActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityUpdateMobileNumberBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call


class UpdateMobileNumberActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityUpdateMobileNumberBinding

    override fun getLayoutView(): View {
        binding = ActivityUpdateMobileNumberBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }


    override fun initializeView(savedInstanceState: Bundle?) {

        binding.editUpdateMobileNumber.setSingleLine()

        binding.textUpdateMobileNumberSave.setOnClickListener {
            val mobileNumber = binding.editUpdateMobileNumber.text.toString().trim()
            if(StringUtils.isEmpty(mobileNumber)){
                showAlert(R.string.hint_input_mobile_number)
                return@setOnClickListener
            }

            if(!FormatUtil.isPhoneNumber(mobileNumber)){
                showAlert(R.string.msg_invalid_phone_number)
                return@setOnClickListener
            }

            val intent = Intent(this@UpdateMobileNumberActivity, SelectVerifyTypeActivity::class.java)
            intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
            selectVerifyTypeLauncher.launch(intent)

        }
    }

    val selectVerifyTypeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mobileNumber = result.data!!.getStringExtra(Const.MOOBILE_NUMBER)!!
            val verifyType = result.data!!.getStringExtra(Const.VERIFY_TYPE)!!
            when (verifyType) {
                "whatsapp" -> {
                    sendWhatsApp(mobileNumber)
                }

                else -> {
                    sendSms(mobileNumber)
                }
            }
        }
    }

    private fun sendWhatsApp(mobileNumber: String) {
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        params["type"] = "update_mobile"
        showProgress("")
        ApiBuilder.create().sendWhatsapp(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@UpdateMobileNumberActivity, VerifyWhatsAppActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
                intent.putExtra(Const.TYPE, "update_mobile")
                defaultLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if(response?.code == 504){
                    showAlert(R.string.msg_duplicate_phone_number)
                }
            }
        }).build().call()
    }

    private fun sendSms(mobileNumber: String) {
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        params["type"] = "update_mobile"
        showProgress("")
        ApiBuilder.create().sendSms(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@UpdateMobileNumberActivity, VerifySmsActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
                intent.putExtra(Const.TYPE, "update_mobile")
                defaultLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if(response?.code == 504){
                    showAlert(R.string.msg_duplicate_phone_number)
                }
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            showProgress("")
            PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                override fun reload() {
                    hideProgress()
                    showAlert(R.string.msg_completed)
                    setResult(RESULT_OK)
                    finish()
                }
            })

        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_verify_mobile_number), ToolbarOption.ToolbarMenu.LEFT)
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