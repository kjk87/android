package com.pplus.prnumberuser.apps.card.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.alert.AlertInputPriceActivity
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.databinding.ActivityInputPriceBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.NumberUtils


class InputPriceActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityInputPriceBinding

    override fun getLayoutView(): View {
        binding = ActivityInputPriceBinding.inflate(layoutInflater)
        return binding.root
    }

    var mPrice = 0
    var mInstallment = ""
    var mPage:Page? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.PAGE)
        binding.textInputPricePageName.text = mPage!!.name
        binding.editInputPrice.setTextIsSelectable(true)
        binding.editInputPrice.setSingleLine()

        val ic: InputConnection = binding.editInputPrice.onCreateInputConnection(EditorInfo())
        binding.dialNfcpay.setInputConnection(ic)

        binding.editInputPrice.addTextChangedListener(textWatcher)

        binding.textInputPricePay.setOnClickListener {
            mPrice = binding.editInputPrice.text.toString().replace(",", "").toInt()
            if (mPrice > 0) {

                if (mPrice < 1000) {
                    showAlert(R.string.msg_enable_pg_over_1000)
                    return@setOnClickListener
                }

                if (mPrice >= 50000) {
                    val intent = Intent(this, InstallmentActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    installmentLauncher.launch(intent)
                } else {
                    //pplus://qr?title={title}&price={price}&pageSeqNo={pageSeqNo}&shopCode={shopCode}&installment={installment}

                    mInstallment = "00"
                    val intent = Intent(this, AlertInputPriceActivity::class.java)
                    intent.putExtra(Const.PRICE, mPrice)
                    intent.putExtra(Const.INSTALLMENT, mInstallment)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    alertLauncher.launch(intent)

                }

            }
        }

    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            if (s!!.isNotEmpty()) {
                binding.textInputPriceHint.visibility = View.GONE
                binding.textInputPriceMoneyUnit.visibility = View.VISIBLE
                binding.textInputPricePay.setBackgroundColor(ResourceUtil.getColor(this@InputPriceActivity, R.color.color_579ffb))
            } else {
                binding.textInputPriceHint.visibility = View.VISIBLE
                binding.textInputPriceMoneyUnit.visibility = View.GONE
                binding.textInputPricePay.setBackgroundColor(ResourceUtil.getColor(this@InputPriceActivity, R.color.color_b7b7b7))
            }

            binding.editInputPrice.removeTextChangedListener(this)
            val cursorPos =  binding.editInputPrice.selectionStart
            val beforeLength =  binding.editInputPrice.length()
            binding.editInputPrice.setText(FormatUtil.getMoneyType(s.toString().replace(",", "")))
            val afterLength =  binding.editInputPrice.length()
            LogUtil.e(LOG_TAG, "afterLength : {} beforeLength : {}", afterLength, beforeLength)

            if (NumberUtils.isNumber( binding.editInputPrice.text.toString()) &&  binding.editInputPrice.text.toString().toInt() == 0) {
                binding.editInputPrice.setSelection(1)
            } else {
                if (afterLength > beforeLength) {
                    binding.editInputPrice.setSelection(cursorPos + 1)
                } else if (afterLength < beforeLength) {
                    binding.editInputPrice.setSelection(cursorPos - 1)
                } else {
                    binding.editInputPrice.setSelection(cursorPos)
                }
            }

            binding.editInputPrice.addTextChangedListener(this)

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    val installmentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                mInstallment = data.getStringExtra(Const.INSTALLMENT)!!

                val intent = Intent(this, AlertInputPriceActivity::class.java)
                intent.putExtra(Const.PRICE, mPrice)
                intent.putExtra(Const.INSTALLMENT, mInstallment)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                alertLauncher.launch(intent)
            }
        }
    }

    val alertLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            val resultData = Intent()
            resultData.putExtra(Const.PAGE, mPage)
            resultData.putExtra(Const.PRICE, mPrice)
            resultData.putExtra(Const.INSTALLMENT, mInstallment)
            setResult(Activity.RESULT_OK, resultData)
            finish()
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_input_pay_price), ToolbarOption.ToolbarMenu.RIGHT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
