package com.lejel.wowbox.apps.withdraw.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.withdraw.data.WithdrawPointAdapter
import com.lejel.wowbox.core.network.model.dto.Bank
import com.lejel.wowbox.core.network.model.dto.Withdraw
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityWithdrawPointBinding
import com.lejel.wowbox.databinding.ItemTopRight2Binding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils

class WithdrawPointActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityWithdrawPointBinding

    override fun getLayoutView(): View {
        binding = ActivityWithdrawPointBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mAdapter: WithdrawPointAdapter

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.editWithdrawAccount.setSingleLine()

        mAdapter = WithdrawPointAdapter()
        binding.recyclerWithdrawPoint.layoutManager = LinearLayoutManager(this)
//        binding.recyclerWithdrawPoint.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.width_33)))
        binding.recyclerWithdrawPoint.adapter = mAdapter
        mAdapter.listener = object : WithdrawPointAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                setWithdrawPrice()
            }
        }

        binding.textWithdrawBank.setOnClickListener {
            val intent = Intent(this, BankListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            bankLauncher.launch(intent)
        }

        binding.textWithdraw.setOnClickListener {
            val withdrawPoint = mAdapter.mSelectData
            if (StringUtils.isEmpty(withdrawPoint) || withdrawPoint.toInt() == 0) {
                showAlert(R.string.msg_select_withdraw_point)
                return@setOnClickListener
            }

            if (withdrawPoint.toInt() < 150000) {
                showAlert(R.string.msg_enable_withdraw_150000)
                return@setOnClickListener
            }

            if (mBank == null) {
                showAlert(R.string.msg_select_bank_name)
                return@setOnClickListener
            }

            val account = binding.editWithdrawAccount.text.toString().trim()
            if (StringUtils.isEmpty(account)) {
                showAlert(R.string.msg_input_account_number)
                return@setOnClickListener
            }

            if (!binding.checkWithdrawAgree.isChecked) {
                showAlert(R.string.msg_alert_withdraw_point_agree)
                return@setOnClickListener
            }

            val params = Withdraw()
            params.bank = mBank!!.name
            params.name = LoginInfoManager.getInstance().member!!.nickname
            params.account = account
            params.request = withdrawPoint.toInt()

            val intent = Intent(this, AlertWithdrawActivity::class.java)
            intent.putExtra(Const.DATA, params)
            intent.putExtra(Const.BANK_IMAGE, mBank!!.image)
            defaultLauncher.launch(intent)

        }

        reloadSession()
    }

    private var mBank: Bank? = null


    private fun setWithdrawPrice() {
        val withdrawPoint = mAdapter.mSelectData
        binding.textWithdrawPointUsePoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(withdrawPoint))
        if (StringUtils.isNotEmpty(withdrawPoint)) {
            var withdrawPrice = 0
            if (withdrawPoint.toInt() >= 3000000) {
                withdrawPrice = withdrawPoint.toInt() - (withdrawPoint.toInt() * 0.2).toInt()
                binding.textWithdrawPointFeePercent.text = getString(R.string.format_withdraw_fee_percent, "20")
            } else if (withdrawPoint.toInt() >= 2000000) {
                withdrawPrice = withdrawPoint.toInt() - (withdrawPoint.toInt() * 0.25).toInt()
                binding.textWithdrawPointFeePercent.text = getString(R.string.format_withdraw_fee_percent, "25")
            } else if (withdrawPoint.toInt() >= 1000000) {
                withdrawPrice = withdrawPoint.toInt() - (withdrawPoint.toInt() * 0.3).toInt()
                binding.textWithdrawPointFeePercent.text = getString(R.string.format_withdraw_fee_percent, "30")
            } else if (withdrawPoint.toInt() >= 500000) {
                withdrawPrice = withdrawPoint.toInt() - (withdrawPoint.toInt() * 0.35).toInt()
                binding.textWithdrawPointFeePercent.text = getString(R.string.format_withdraw_fee_percent, "35")
            } else if (withdrawPoint.toInt() >= 300000) {
                withdrawPrice = withdrawPoint.toInt() - (withdrawPoint.toInt() * 0.4).toInt()
                binding.textWithdrawPointFeePercent.text = getString(R.string.format_withdraw_fee_percent, "40")
            } else if (withdrawPoint.toInt() >= 150000) {
                withdrawPrice = withdrawPoint.toInt() - (withdrawPoint.toInt() * 0.5).toInt()
                binding.textWithdrawPointFeePercent.text = getString(R.string.format_withdraw_fee_percent, "50")
            }
            binding.layoutWithdrawPointResult.visibility = View.VISIBLE
            binding.textWithdrawPointExpectRp.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(withdrawPrice.toString()))
            binding.textWithdrawPointExpectRp2.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(withdrawPrice.toString()))
        }else{
            binding.layoutWithdrawPointResult.visibility = View.GONE
        }
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textWithdrawRetentionPoint.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString())
            }
        })
    }

    private val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finish()

            //            reloadSession()
            //            binding.textWithdrawBank.setText("")
            //            binding.editWithdrawAccount.setText("")
            //            mAdapter.mSelectData = ""
            //            mAdapter.notifyDataSetChanged()
        }
    }
    private val bankLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            if (result.data != null) {
                mBank = PplusCommonUtil.getParcelableExtra(result.data!!, Const.DATA, Bank::class.java)
                binding.textWithdrawBank.text = mBank!!.name
            }

        }
    }

    internal inner class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).absoluteAdapterPosition
            if (itemPosition % 3 == 2) {
                outRect.right = 0
            } else {
                outRect.right = space
            }
            outRect.bottom = space // Add top margin only for the first item to avoid double space between items
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
                        val intent = Intent(this@WithdrawPointActivity, WithdrawListActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }

                    else -> {}
                }
            }
        }
    }
}