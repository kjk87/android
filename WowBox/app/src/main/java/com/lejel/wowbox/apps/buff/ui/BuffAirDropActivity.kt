package com.lejel.wowbox.apps.buff.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.AirDrop
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ActivityBuffAirDropBinding
import retrofit2.Call

class BuffAirDropActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityBuffAirDropBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffAirDropBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }


    override fun initializeView(savedInstanceState: Bundle?) {

        val tokenAddress = "0xe9C1B765c3b69Ff6178c7310FE3eb106421870a5"

        binding.textBuffAirDropTokenAddress.text = tokenAddress

        binding.textBuffAirDropTokenAddressCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("token address", tokenAddress)
            clipboard.setPrimaryClip(clip)
            ToastUtil.show(this, R.string.msg_copied_clipboard)
        }

        binding.textBuffAirDropMetaMaskJoinMethodGuide.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/A7sbpFvkEe0?si=glXvabeA6Sjjl4gY")))
        }

        binding.textBuffAirDropMetaMaskCopyAddressMethodGuide.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/shorts/VXz_CyjFNvk?feature=share")))
        }

        binding.textBuffAirDropApply.setOnClickListener {
            val wallet = binding.editBuffAirDropMetaMaskAddress.text.toString().trim()
            if (StringUtils.isEmpty(wallet)) {
                showAlert(R.string.msg_input_meta_mask_address)
                return@setOnClickListener
            }

            val builder = AlertBuilder.Builder()
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_apply_air_drop), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val params = HashMap<String, String>()
                            params["wallet"] = wallet
                            showProgress("")
                            ApiBuilder.create().airDropApply(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    getAirDrop()
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    if (response?.code == 504) {
                                        showAlert(R.string.msg_dupliate_meta_mask_address)
                                    }
                                }
                            }).build().call()
                        }

                        else -> {

                        }
                    }
                }
            }).builder().show(this)
        }

        getAirDrop()
    }

    private fun getAirDrop() {
        showProgress("")
        ApiBuilder.create().getAirDrop().setCallback(object : PplusCallback<NewResultResponse<AirDrop>> {
            override fun onResponse(call: Call<NewResultResponse<AirDrop>>?, response: NewResultResponse<AirDrop>?) {
                hideProgress()
                if (response?.result != null) {
                    val airDrop = response.result!!
                    when (airDrop.status) {
                        "pending", "redemand" -> {
                            binding.layoutBuffAirDropMetaMaskAddress.visibility = View.GONE
                            binding.layoutBuffAirDropApply.visibility = View.GONE
                            binding.layoutBuffAirDropPending.visibility = View.VISIBLE
                            binding.layoutBuffAirDropReturn.visibility = View.GONE
                            binding.layoutBuffAirDropNormal.visibility = View.GONE
                        }

                        "return" -> {
                            binding.layoutBuffAirDropMetaMaskAddress.visibility = View.VISIBLE
                            binding.editBuffAirDropMetaMaskAddress.setText(airDrop.wallet)
                            binding.layoutBuffAirDropApply.visibility = View.VISIBLE
                            binding.layoutBuffAirDropPending.visibility = View.GONE
                            binding.layoutBuffAirDropReturn.visibility = View.VISIBLE
                            binding.layoutBuffAirDropNormal.visibility = View.GONE
                            binding.textBuffAirDropReturnViewReason.setOnClickListener {
                                val intent = Intent(this@BuffAirDropActivity, AlertBuffAirDropReturnReasonActivity::class.java)
                                intent.putExtra(Const.DATA, airDrop)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                        }

                        "normal" -> {
                            binding.layoutBuffAirDropMetaMaskAddress.visibility = View.GONE
                            binding.layoutBuffAirDropApply.visibility = View.GONE
                            binding.layoutBuffAirDropPending.visibility = View.GONE
                            binding.layoutBuffAirDropReturn.visibility = View.GONE
                            binding.layoutBuffAirDropNormal.visibility = View.VISIBLE
                            binding.textBuffAirDropAmount.text = FormatUtil.getMoneyTypeFloat(airDrop.amount.toString())
                        }
                    }
                } else {
                    binding.layoutBuffAirDropMetaMaskAddress.visibility = View.VISIBLE
                    binding.layoutBuffAirDropApply.visibility = View.VISIBLE
                    binding.layoutBuffAirDropPending.visibility = View.GONE
                    binding.layoutBuffAirDropReturn.visibility = View.GONE
                    binding.layoutBuffAirDropNormal.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<AirDrop>>?, t: Throwable?, response: NewResultResponse<AirDrop>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_mining1_desc2), ToolbarOption.ToolbarMenu.LEFT)
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