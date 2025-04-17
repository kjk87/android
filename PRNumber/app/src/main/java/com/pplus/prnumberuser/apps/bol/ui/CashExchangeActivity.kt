package com.pplus.prnumberuser.apps.bol.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.Exchange
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityCashExchangeBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils

class CashExchangeActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding : ActivityCashExchangeBinding

    override fun getLayoutView(): View {
        binding = ActivityCashExchangeBinding.inflate(layoutInflater)
        return binding.root
    }

    private var exchange: Exchange? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textCashExchangeRetentionBol.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString()))
            }
        })

        binding.editCashExchangeAmount.setSingleLine()
        binding.editCashExchangeAccountName.setSingleLine()
        binding.editCashExchangeAccountNumber.setSingleLine()

        binding.editCashExchangeAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if(s.toString().isNotEmpty()){
                    val amount = s.toString().toInt()

                    if(amount > LoginInfoManager.getInstance().user.totalBol){
                        binding.editCashExchangeAmount.setText(LoginInfoManager.getInstance().user.totalBol.toString())
                        binding.editCashExchangeAmount.setSelection(binding.editCashExchangeAmount.text.length)
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.textCashExchangeBank.setOnClickListener {

            val banks = resources.getStringArray(R.array.bank)

            val builder = AlertBuilder.Builder()
            builder.setContents(*banks)
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    binding.textCashExchangeBank.text = banks[event_alert.getValue()-1]
                }
            }).builder().show(this)
        }

        binding.textCashExchangeRequest.setOnClickListener {

//            if(LoginInfoManager.getInstance().user.totalBol < 50000){
//                val intent = Intent(this@CashExchangeActivity, CashChangeLackAlertActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//                return@setOnClickListener
//            }

            val amount = binding.editCashExchangeAmount.text.toString().trim()
            if(StringUtils.isEmpty(amount)){
                ToastUtil.showAlert(this, R.string.msg_input_cash_exchange_amount)
                return@setOnClickListener
            }

            if(amount.toInt() < 50000){
                ToastUtil.showAlert(this, R.string.msg_enable_cash_exchange_amount_over_50000)
                return@setOnClickListener
            }

            val accountName = binding.editCashExchangeAccountName.text.toString().trim()
            if(StringUtils.isEmpty(accountName)){
                ToastUtil.showAlert(this, R.string.msg_input_account_name)
                return@setOnClickListener
            }

            val bank = binding.textCashExchangeBank.text.toString().trim()
            if(StringUtils.isEmpty(bank)){
                ToastUtil.showAlert(this, R.string.msg_select_bank_name)
                return@setOnClickListener
            }

            val accountNumber = binding.editCashExchangeAccountNumber.text.toString().trim()
            if(StringUtils.isEmpty(accountNumber)){
                ToastUtil.showAlert(this, R.string.msg_input_account_number)
                return@setOnClickListener
            }

            exchange = Exchange()
            exchange!!.bol = amount.toLong()
            exchange!!.bankAccountHolderName = accountName
            exchange!!.bankAccountId = accountNumber
            exchange!!.bankName = bank

            val intent = Intent(this, CashExchangeAlertActivity::class.java)
            intent.putExtra(Const.EXCHANGE, exchange)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashExchangeLauncher.launch(intent)

        }

    }

    val cashExchangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if(result.resultCode == Activity.RESULT_OK){
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_cash_exchange), ToolbarOption.ToolbarMenu.LEFT)

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
