package com.root37.buflexz.apps.luckydraw.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.LuckyDrawWin
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityLuckyDrawWinImpressionBinding
import retrofit2.Call

class LuckyDrawWinImpressionActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyDrawWinImpressionBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawWinImpressionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mLuckyDrawWin: LuckyDrawWin

    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyDrawWin = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDrawWin::class.java)!!

        if(StringUtils.isNotEmpty(mLuckyDrawWin.impression)){
            setTitle(getString(R.string.word_modify_win_impression))
            binding.editLuckyDrawWinImpression.setText(mLuckyDrawWin.impression)
        }else{
            setTitle(getString(R.string.word_reg_win_impression))
        }

        binding.editLuckyDrawWinImpression.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.toString().trim().length < 5){
                    binding.textLuckyDrawWinImpressionComplete.setBackgroundResource(R.drawable.bg_595959_radius_30)
                    binding.textLuckyDrawWinImpressionComplete.isClickable = false
                    binding.textLuckyDrawWinImpressionComplete.isEnabled = false
                }else{
                    binding.textLuckyDrawWinImpressionComplete.setBackgroundResource(R.drawable.bg_48b778_radius_30)
                    binding.textLuckyDrawWinImpressionComplete.isClickable = true
                    binding.textLuckyDrawWinImpressionComplete.isEnabled = true
                }

                binding.textLuckyDrawWinImpressionCount.text = getString(R.string.format_text_count, s.toString().trim().length, 50)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.textLuckyDrawWinImpressionComplete.setOnClickListener {
            val impression = binding.editLuckyDrawWinImpression.text.toString().trim()
            if(StringUtils.isEmpty(impression)){
                return@setOnClickListener
            }

            if(impression.length < 5){
                return@setOnClickListener
            }
            val params = HashMap<String, String>()
            params["seqNo"] = mLuckyDrawWin.seqNo.toString()
            params["impression"] = impression

            showProgress("")
            ApiBuilder.create().updateLuckyDrawWinImpression(params).setCallback(object : PplusCallback<NewResultResponse<LuckyDrawWin>>{
                override fun onResponse(call: Call<NewResultResponse<LuckyDrawWin>>?, response: NewResultResponse<LuckyDrawWin>?) {
                    hideProgress()
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<LuckyDrawWin>>?, t: Throwable?, response: NewResultResponse<LuckyDrawWin>?) {
                    hideProgress()
                }
            }).build().call()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reg_win_impression), ToolbarOption.ToolbarMenu.LEFT)
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