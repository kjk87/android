package com.lejel.wowbox.apps.withdraw.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Bank
import com.lejel.wowbox.core.network.model.dto.Provinsi
import com.lejel.wowbox.core.network.model.dto.Withdraw
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityWithdrawBinding
import com.lejel.wowbox.databinding.ItemTopRight2Binding
import com.lejel.wowbox.databinding.ItemTopRightBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

class WithdrawActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityWithdrawBinding

    override fun getLayoutView(): View {
        binding = ActivityWithdrawBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.editWithdrawCash.setSingleLine()
        binding.editWithdrawName.setSingleLine()
        binding.editWithdrawAccount.setSingleLine()

        binding.editWithdrawCash.addTextChangedListener {
            if (it.toString().isNotEmpty()) {

                if (it.toString().toInt() > LoginInfoManager.getInstance().member!!.cash!!.toInt()) {
                    binding.editWithdrawCash.setText(LoginInfoManager.getInstance().member!!.cash!!.toInt().toString())
                }
            }
            setWithdrawPrice()
        }
        binding.textWithdrawCashAll.setOnClickListener {
            binding.editWithdrawCash.setText(LoginInfoManager.getInstance().member!!.cash!!.toInt().toString())
        }

        binding.textWithdrawBank.setOnClickListener {
            if(mBankList == null){
                return@setOnClickListener
            }

            val contentList = arrayListOf<String>()
            for (bank in mBankList!!) {
                contentList.add(bank.name!!)
            }
            val builder = AlertBuilder.Builder()
            builder.setContents(contentList)
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    val bank = mBankList!![event_alert.value - 1]
                    binding.textWithdrawBank.text = bank.name
                    mBank = bank

                }
            }).builder().show(this@WithdrawActivity)
        }

        binding.textWithdraw.setOnClickListener {
            val withdrawPrice = binding.editWithdrawCash.text.toString().trim()
            if (StringUtils.isEmpty(withdrawPrice) || withdrawPrice.toInt() == 0) {
                showAlert(R.string.msg_input_withdraw_point)
                return@setOnClickListener
            }

            if (withdrawPrice.toInt() < 150000) {
                showAlert(R.string.msg_enable_withdraw_150000)
                return@setOnClickListener
            }

            val bank = binding.textWithdrawBank.text.toString().trim()
            if (StringUtils.isEmpty(bank)) {
                showAlert(R.string.msg_input_bank_name)
                return@setOnClickListener
            }

            val name = binding.editWithdrawName.text.toString().trim()
            if (StringUtils.isEmpty(name)) {
                showAlert(R.string.msg_input_account_name)
                return@setOnClickListener
            }

            val account = binding.editWithdrawAccount.text.toString().trim()
            if (StringUtils.isEmpty(account)) {
                showAlert(R.string.msg_input_account_number)
                return@setOnClickListener
            }

            val params = Withdraw()
            params.bank = bank
            params.name = name
            params.account = account
            params.request = withdrawPrice.toInt()

            val intent = Intent(this, AlertWithdrawActivity::class.java)
            intent.putExtra(Const.DATA, params)
            defaultLauncher.launch(intent)

        }

        binding.textWithdrawCash.text = "0"
        reloadSession()
        getBankList()
    }

    private var mBankList: List<Bank>? = null
    private var mBank: Bank? = null

    private fun getBankList(){
        showProgress("")
        ApiBuilder.create().getBankList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Bank>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Bank>>>?, response: NewResultResponse<ListResultResponse<Bank>>?) {
                hideProgress()
                if(response?.result != null){
                    mBankList = response.result!!.list!!
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Bank>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Bank>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setWithdrawPrice() {
        val withdrawPrice = binding.editWithdrawCash.text.toString().trim()
        if (StringUtils.isNotEmpty(withdrawPrice)) {
            binding.textWithdrawCash.text = FormatUtil.getMoneyType(withdrawPrice)
        }
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textWithdrawRetentionCash.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.cash!!.toInt().toString())
            }
        })
    }

    private val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            reloadSession()
            binding.editWithdrawCash.setText("")
            binding.editWithdrawName.setText("")
            binding.editWithdrawAccount.setText("")
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_cash_exchange), ToolbarOption.ToolbarMenu.LEFT)
        val item = ItemTopRight2Binding.inflate(layoutInflater)
        item.textTopRight.setText(R.string.word_withdraw_history)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, item.root, 0)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        val intent = Intent(this@WithdrawActivity, WithdrawListActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }

                    else -> {}
                }
            }
        }
    }
}