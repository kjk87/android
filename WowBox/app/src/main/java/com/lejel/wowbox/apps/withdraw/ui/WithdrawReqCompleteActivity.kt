package com.lejel.wowbox.apps.withdraw.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.Bank
import com.lejel.wowbox.core.network.model.dto.Withdraw
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityWithdrawReqCompleteBinding
import com.pplus.utils.part.format.FormatUtil

class WithdrawReqCompleteActivity : BaseActivity() {

    private lateinit var binding: ActivityWithdrawReqCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityWithdrawReqCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val withdraw = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Withdraw::class.java)!!
        val bankImage = intent.getStringExtra(Const.BANK_IMAGE)

        binding.textWithdrawReqCompleteRequest.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(withdraw.request.toString()))
        binding.textWithdrawReqCompleteWithdraw.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(withdraw.withdraw.toString()))

        Glide.with(this).load(bankImage).apply(RequestOptions().centerCrop()).into(binding.imageWithdrawReqCompleteBank)
        binding.textWithdrawReqCompleteBank.text = withdraw.bank
        binding.textWithdrawReqCompleteAccount.text = withdraw.account

        binding.textWithdrawReqCompleteWithdrawHistory.setOnClickListener {
            val intent = Intent(this, WithdrawListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

    }
}