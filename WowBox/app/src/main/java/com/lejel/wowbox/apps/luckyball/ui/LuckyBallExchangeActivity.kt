package com.lejel.wowbox.apps.luckyball.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBallExchangeBinding
import retrofit2.Call

class LuckyBallExchangeActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityLuckyBallExchangeBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBallExchangeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.textLuckyBallExchangePointUnit.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType("1000"))
        binding.textLuckyBallExchangeLuckyBallUnit.text = getString(R.string.format_ball_unit, FormatUtil.getMoneyType("1"))

        binding.editLuckyBallExchange.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(it: Editable?) {
                if (it.toString().isNotEmpty()) {
                    val maximumBall = LoginInfoManager.getInstance().member!!.point!!.toInt() / 1000
                    if (it.toString().toInt() > maximumBall) {
                        binding.editLuckyBallExchange.setText(maximumBall.toString())
                    }
                }
            }
        })

        binding.textLuckyBallExchangeAll.setOnClickListener {
            val maximumBall = LoginInfoManager.getInstance().member!!.point!!.toInt() / 1000
            binding.editLuckyBallExchange.setText(maximumBall.toString())
        }

        binding.textLuckyBallExchange.setOnClickListener {
            val maximumBall = LoginInfoManager.getInstance().member!!.point!!.toInt() / 1000
            val exchangeBall = binding.editLuckyBallExchange.text.toString().trim()

            if(StringUtils.isEmpty(exchangeBall)){
                showAlert(R.string.msg_input_exchange_luckyball_amount)
                return@setOnClickListener
            }

            if(exchangeBall.toInt() > maximumBall){
                showAlert(R.string.msg_over_enable_exchange_ball)
                return@setOnClickListener
            }

            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.msg_alert_exchange_luckyball_title))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_exchange_luckyball_desc), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_exchange))
            builder.setBackgroundClickable(false).setAutoCancel(false)
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {}

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val params = HashMap<String, String>()
                            params["ball"] = exchangeBall
                            showProgress("")
                            ApiBuilder.create().exchangePointToBall(params).setCallback(object :PplusCallback<NewResultResponse<Any>>{
                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    val intent = Intent(this@LuckyBallExchangeActivity, LuckyBallExchangeCompleteActivity::class.java)
                                    intent.putExtra(Const.BALL, exchangeBall.toInt())
                                    defaultLauncher.launch(intent)
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    if(response?.code == 517){
                                        showAlert(R.string.msg_lack_point)
                                    }
                                }
                            }).build().call()
                        }
                        else -> {}
                    }
                }
            }).builder().show(this)


        }
        initPoint()
    }
    private fun initPoint(){
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textLuckyBallExchangeRetentionPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point.toString()))
                val maximumBall = LoginInfoManager.getInstance().member!!.point!!.toInt() / 1000
                binding.textLuckyBallExchangeMaximumBall.text = PplusCommonUtil.fromHtml(getString(R.string.html_exchange_luckyball_maximum, FormatUtil.getMoneyType(maximumBall.toString())))
                binding.editLuckyBallExchange.setText("")
            }
        })
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        initPoint()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_exchange_luckyball), ToolbarOption.ToolbarMenu.LEFT)
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