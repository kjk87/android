package com.lejel.wowbox.apps.luckydraw.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyDraw
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyDrawCheckPrivateBinding
import retrofit2.Call

class LuckyDrawCheckPrivateActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyDrawCheckPrivateBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawCheckPrivateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mLuckyDraw: LuckyDraw

    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyDraw = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDraw::class.java)!!

        binding.rootLuckyDrawCheckPrivate.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.rootLuckyDrawCheckPrivate.windowToken, 0)
            }
        })

        binding.editLuckyDrawCheckPrivate.setSingleLine()
        binding.editLuckyDrawCheckPrivate.setTextIsSelectable(true)

        val ic: InputConnection = binding.editLuckyDrawCheckPrivate.onCreateInputConnection(EditorInfo())
        binding.padLuckyDrawCheckPrivate.setInputConnection(ic)
        binding.editLuckyDrawCheckPrivate.requestFocus()

        binding.editLuckyDrawCheckPrivate.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){

                v.post {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }

                val ic = binding.editLuckyDrawCheckPrivate.onCreateInputConnection(EditorInfo())
                binding.padLuckyDrawCheckPrivate.setInputConnection(ic)
            }
        }

//        binding.layoutLuckyDrawCheckPrivateTelegram.setOnClickListener {
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/BuffCoin_Official_chat")))
//        }
//
//        binding.layoutLuckyDrawCheckPrivateDiscord.setOnClickListener {
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.com/channels/1194087825486909460/1194543366025781378")))
//        }

        binding.textLuckyDrawCheckPrivateConfirm.setOnClickListener {
            val privateKey = binding.editLuckyDrawCheckPrivate.text.toString().trim()
            if(StringUtils.isEmpty(privateKey)){
                showAlert(R.string.msg_input_private_number)
                return@setOnClickListener
            }
            checkPrivate(privateKey)
        }
    }

    private fun checkPrivate(privateKey:String){
        val params = HashMap<String, String>()
        params["privateKey"] = privateKey
        showProgress("")
        ApiBuilder.create().checkLuckyDrawPrivate(mLuckyDraw.seqNo!!, params).setCallback(object : PplusCallback<NewResultResponse<Boolean>>{
            override fun onResponse(call: Call<NewResultResponse<Boolean>>?, response: NewResultResponse<Boolean>?) {
                hideProgress()
                if(response?.result != null && response.result!!){
                    val data = Intent()
                    data.putExtra(Const.DATA, mLuckyDraw)
                    setResult(RESULT_OK, data)
                    finish()
                }else{
                    showAlert(R.string.msg_invalid_number)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Boolean>>?, t: Throwable?, response: NewResultResponse<Boolean>?) {
                hideProgress()
                showAlert(R.string.msg_invalid_number)
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_input_private_number), ToolbarOption.ToolbarMenu.LEFT)
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