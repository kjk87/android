package com.lejel.wowbox.apps.point.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityPointExchangeBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils

class PointExchangeActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityPointExchangeBinding

    override fun getLayoutView(): View {
        binding = ActivityPointExchangeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.editPointExchangePoint.setSingleLine()
        binding.editPointExchangePoint.addTextChangedListener {
            if (it.toString().isNotEmpty()) {

                if (it.toString().toInt() > LoginInfoManager.getInstance().member!!.point!!.toInt()) {
                    binding.editPointExchangePoint.setText(LoginInfoManager.getInstance().member!!.point!!.toInt().toString())
                }
            }
            setExchangePoint()
        }

        binding.textPointExchangePointAll.setOnClickListener {
            binding.editPointExchangePoint.setText(LoginInfoManager.getInstance().member!!.point!!.toInt().toString())
        }
        binding.textPointExchangeExpectPoint.text = getString(R.string.format_point_unit, "0")

        binding.textPointExchange.setOnClickListener {
            if(!binding.checkPointExchangeAgree.isChecked){
                showAlert(R.string.msg_agree_caution)
                return@setOnClickListener
            }
            val pointStr = binding.editPointExchangePoint.text.toString().trim()
            if(StringUtils.isEmpty(pointStr) || pointStr.toInt() <= 0){
                showAlert(R.string.msg_input_exchange_point)
                return@setOnClickListener
            }

            val intent = Intent(this, AlertPointExchangeActivity::class.java)
            intent.putExtra(Const.POINT, pointStr.toInt())
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        reloadSession()
    }

    private fun setExchangePoint(){
        val pointStr = binding.editPointExchangePoint.text.toString().trim()
        binding.textPointExchangeExpectPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(pointStr))
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textPointExchangeRetentionPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString()))
            }
        })
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_wow_mall_point_exchange), ToolbarOption.ToolbarMenu.LEFT)
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