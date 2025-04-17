//package com.pplus.prnumberuser.apps.main.ui
//
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.View
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.databinding.ActivityChangeSnsAccountBinding
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.utils.StringUtils
//import retrofit2.Call
//import java.util.*
//
//class ChangeSnsAccountActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    private lateinit var binding: ActivityChangeSnsAccountBinding
//
//    override fun getLayoutView(): View {
//        binding = ActivityChangeSnsAccountBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    var mIsCheckId = false
//    var mSignedId = ""
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
////        text_change_account_close.setOnClickListener {
////            finish()
////        }
//
////        text_change_account_mobile_number.text = FormatUtil.getPhoneNumber(LoginInfoManager.getInstance().user.mobile)
//        binding.editChangeAccountId.setSingleLine()
//        binding.editChangeAccountId.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                val id = s.toString().trim()
//                if (id.length > 3) {
//
//                    val regex = Regex(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")
//                    if(id.matches(regex)) {
//                        binding.textChangeAccountIdState.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_reject, 0, 0, 0)
//                        binding.textChangeAccountIdState.setTextColor(ResourceUtil.getColor(this@ChangeSnsAccountActivity, R.color.color_ff4646))
//                        binding.textChangeAccountIdState.setText(R.string.msg_input_valid_id)
//                        binding.textChangeAccountIdState.visibility = View.VISIBLE
//                        return
//                    }
//
//                    val params = HashMap<String, String>()
//                    params["loginId"] = id
//                    ApiBuilder.create().userCheck(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
//                        override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
//                            if (response != null) {
//                                val result = response.data
//                                when (result) {
//                                    "success" -> {
//                                        mIsCheckId = true
//                                        mSignedId = id
//
//                                        binding.textChangeAccountIdState.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_ok, 0, 0, 0)
//                                        binding.textChangeAccountIdState.setTextColor(ResourceUtil.getColor(this@ChangeSnsAccountActivity, R.color.color_579ffb))
//                                        binding.textChangeAccountIdState.setText(R.string.msg_usable_id)
//                                        binding.textChangeAccountIdState.visibility = View.VISIBLE
//                                    }
//                                    "fail" -> {
//                                        mIsCheckId = false
//                                        binding.textChangeAccountIdState.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_reject, 0, 0, 0)
//                                        binding.textChangeAccountIdState.setTextColor(ResourceUtil.getColor(this@ChangeSnsAccountActivity, R.color.color_ff4646))
//                                        binding.textChangeAccountIdState.setText(R.string.msg_duplicate_id)
//                                        binding.textChangeAccountIdState.visibility = View.VISIBLE
//                                    }
//                                }
//                            }
//                        }
//
//                        override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
//
//                        }
//                    }).build().call()
//                } else {
//                    binding.textChangeAccountIdState.visibility = View.INVISIBLE
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//
//        binding.textChangeAccountComplete.setOnClickListener {
//            val loginId = binding.editChangeAccountId.text.toString().trim()
//
//            if (StringUtils.isEmpty(loginId)) {
//                showAlert(R.string.msg_input_id)
//                return@setOnClickListener
//            }
//
//            if (loginId.length < 4) {
//                showAlert(getString(R.string.msg_input_id_over4))
//                return@setOnClickListener
//            }
//
//            if (!mIsCheckId || mSignedId != loginId) {
//                showAlert(R.string.msg_duplicate_id)
//                return@setOnClickListener
//            }
//
//            val regex = Regex(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")
//
//            if(loginId.matches(regex)) {
//                showAlert(R.string.msg_input_valid_id);
//                return@setOnClickListener
//            }
//
//            val password = binding.editChangeAccountPassword.text.toString().trim()
//            if (StringUtils.isEmpty(password)) {
//                showAlert(R.string.msg_input_password)
//                return@setOnClickListener
//            }
//
//            if (password.length < 4) {
//                showAlert(getString(R.string.to_password) + " " + getString(R.string.format_msg_input_over_number, 4))
//                return@setOnClickListener
//            }
//
//            val params = HashMap<String, String>()
////            params["snsId"] = LoginInfoManager.getInstance().user.loginId!!
//            params["loginId"] = loginId
//            params["password"] = password
//            showProgress("")
//            ApiBuilder.create().putSnsAccount(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                    hideProgress()
//                    LoginInfoManager.getInstance().user.loginId = loginId
//                    try {
//                        LoginInfoManager.getInstance().user.password = PplusCommonUtil.encryption(password)
//                    } catch (e: Exception) {
//
//                    }
//                    LoginInfoManager.getInstance().save()
//                    showAlert(R.string.msg_changed_account)
//                    finish()
//
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                    hideProgress()
//                }
//            }).build().call()
//        }
//    }
//
//    override fun onBackPressed() {
//
//    }
//}
