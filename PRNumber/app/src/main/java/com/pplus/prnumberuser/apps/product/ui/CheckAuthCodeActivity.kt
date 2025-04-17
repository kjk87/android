package com.pplus.prnumberuser.apps.product.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.EventGift
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.PurchaseProduct
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityCheckAuthCodeBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class CheckAuthCodeActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityCheckAuthCodeBinding

    override fun getLayoutView(): View {
        binding = ActivityCheckAuthCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val type = intent.getStringExtra(Const.TYPE)
        val purchaseProduct = intent.getParcelableExtra<PurchaseProduct>(Const.DATA)
        val page = intent.getParcelableExtra<Page>(Const.PAGE)
        val gift = intent.getParcelableExtra<EventGift>(Const.EVENT_GIFT)

        binding.editCheckAuthCode1.setSingleLine()
        binding.editCheckAuthCode1.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckAuthCode2.setSingleLine()
        binding.editCheckAuthCode2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckAuthCode3.setSingleLine()
        binding.editCheckAuthCode3.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckAuthCode4.setSingleLine()
        binding.editCheckAuthCode4.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.editCheckAuthCode1.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editCheckAuthCode2.requestFocus()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editCheckAuthCode2.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editCheckAuthCode3.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editCheckAuthCode3.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editCheckAuthCode4.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.layoutPlusInfoRef.visibility = View.GONE
        when(type){
            Const.PRODUCT_PRICE->{
                binding.textCheckAuthCodeDesc.setText(R.string.msg_check_auth_code_desc1)
                if(purchaseProduct != null){
                    if(purchaseProduct.page != null){
                        Glide.with(this).load(purchaseProduct.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(binding.imageCheckAuthCodePageImage)
                        binding.textCheckAuthCodePageName.text = purchaseProduct.page!!.name
                    }
                    binding.textCheckAuthCodeBuyTitle.text = purchaseProduct.title
                }
            }
            Const.EVENT_GIFT->{
                Glide.with(this).load(page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(binding.imageCheckAuthCodePageImage)
                binding.textCheckAuthCodePageName.text = page.name
                binding.textCheckAuthCodeBuyTitle.text = gift!!.title
            }
            Const.PLUS_GIFT->{
                Glide.with(this).load(page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(binding.imageCheckAuthCodePageImage)
                binding.textCheckAuthCodePageName.text = page.name
                binding.textCheckAuthCodeBuyTitle.text = page.plusInfo
                binding.textCheckAuthCodeDesc.setText(R.string.msg_check_auth_code_desc2)
                binding.layoutPlusInfoRef.visibility = View.VISIBLE
            }
        }

        binding.textCheckAuthCodeConfirm.setOnClickListener {

            val code1 = binding.editCheckAuthCode1.text.toString().trim()
            val code2 = binding.editCheckAuthCode2.text.toString().trim()
            val code3 = binding.editCheckAuthCode3.text.toString().trim()
            val code4 = binding.editCheckAuthCode4.text.toString().trim()

            if(StringUtils.isEmpty(code1) || StringUtils.isEmpty(code2) || StringUtils.isEmpty(code3) || StringUtils.isEmpty(code4)){
                showAlert(R.string.msg_input_use_auth_code)
                return@setOnClickListener
            }

            val params = HashMap<String, String>()

            if(purchaseProduct != null){
                params["no"] = purchaseProduct.page!!.seqNo.toString()
            }else{
                params["no"] = page!!.no.toString()
            }

            params["authCode"] = code1+code2+code3+code4
            showProgress("")
            ApiBuilder.create().checkAuthCodeForUser(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
                override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                    hideProgress()
                    val data = Intent()
                    if(purchaseProduct != null){
                        data.putExtra(Const.DATA, purchaseProduct)
                        data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, 0))
                    }

                    setResult(Activity.RESULT_OK, data)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                    hideProgress()
                    binding.editCheckAuthCode1.setText("")
                    binding.editCheckAuthCode2.setText("")
                    binding.editCheckAuthCode3.setText("")
                    binding.editCheckAuthCode4.setText("")

                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_alert_invalid_auth_code_title))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_invalid_auth_code1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.builder().show(this@CheckAuthCodeActivity)

//                    showAlert(getString(R.string.msg_alert_invalid_auth_code1))
                }
            }).build().call()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_use_goods), ToolbarOption.ToolbarMenu.LEFT)
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
