//package com.pplus.prnumberbiz.apps.signup.ui
//
//
//import android.app.Activity
//import android.app.Activity.RESULT_OK
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CheckBox
//import android.widget.CompoundButton
//import android.widget.LinearLayout
//import com.pple.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberbiz.Const
//import com.pplus.prnumberbiz.R
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberbiz.apps.common.ui.common.WebViewActivity
//import com.pplus.prnumberbiz.core.network.ApiBuilder
//import com.pplus.prnumberbiz.core.network.model.dto.No
//import com.pplus.prnumberbiz.core.network.model.dto.Terms
//import com.pplus.prnumberbiz.core.network.model.dto.User
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_join.*
//import kotlinx.android.synthetic.main.item_terms.view.*
//import network.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class UpdatePendingUserFragment : BaseFragment<JoinActivity>(), CompoundButton.OnCheckedChangeListener {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_join
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//
//    private var mIsCheckId = false
//    private var mSignedId = ""
//    private var mTermsList: ArrayList<Terms>? = null
//    private var mTermsAgreeMap: MutableMap<Long, Boolean>? = null
//    private var mCheckBoxList: MutableList<CheckBox>? = null
//
//    override fun init() {
//
//
//        layout_join_verification.visibility = View.GONE
//        val intent = Intent(activity, VerificationMeActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        intent.putExtra(Const.KEY, Const.JOIN)
//        activity!!.startActivityForResult(intent, Const.REQ_VERIFICATION)
//
//        text_join_check_dup_id.setOnClickListener {
//            val id = edit_join_id.text.toString().trim()
//            if (id.isEmpty()) {
//                showAlert(R.string.msg_input_id)
//                return@setOnClickListener
//            }
//
//            if (id.length < 4) {
//                showAlert(R.string.msg_input_over4)
//                return@setOnClickListener
//            }
//
//            if (id.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*".toRegex())) {
//                showAlert(R.string.msg_input_valid_id)
//                return@setOnClickListener
//            }
//
//            val params = HashMap<String, String>()
//            params["loginId"] = id
//            ApiBuilder.create().existsUser(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//
//                override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
//
//                    hideProgress()
//                    if (!isAdded) {
//                        return
//                    }
//                    mIsCheckId = false
//                    showAlert(getString(R.string.msg_duplicate_id))
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
//
//                    hideProgress()
//                    if (!isAdded) {
//                        return
//                    }
//                    mIsCheckId = true
//                    showAlert(getString(R.string.msg_enable_use))
//                    mSignedId = id
//                }
//            }).build().call()
//        }
//
//        text_join_verification.visibility = View.VISIBLE
//        layout_join_complete_verification.visibility = View.GONE
//
//        val api = ApiBuilder.create().getActiveTermsAll(activity!!.packageName)
//        api.setCallback(object : PplusCallback<NewResultResponse<Terms>> {
//            override fun onResponse(call: Call<NewResultResponse<Terms>>?, response: NewResultResponse<Terms>?) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                mTermsList = response!!.datas as ArrayList<Terms>
//                mTermsAgreeMap = HashMap()
//                layout_join_terms.removeAllViews()
//                mCheckBoxList = ArrayList<CheckBox>()
//                for (i in mTermsList!!.indices) {
//                    val terms = mTermsList!![i]
//                    mTermsAgreeMap!![terms.no] = false
//                    val viewTerms = LayoutInflater.from(activity).inflate(R.layout.item_terms, LinearLayout(activity))
//                    if (i != 0) {
//                        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                        layoutParams.setMargins(0, resources.getDimensionPixelSize(R.dimen.height_50), 0, 0)
//                        viewTerms.layoutParams = layoutParams
//                    }
//                    val checkTerms = viewTerms.findViewById(R.id.check_terms_agree) as CheckBox
//                    checkTerms.text = terms.subject
//                    checkTerms.setOnCheckedChangeListener { buttonView, isChecked ->
//                        var isAll = true
//                        for (checkBox in mCheckBoxList!!) {
//                            if (!checkBox.isChecked) {
//                                isAll = false
//                                break
//                            }
//                        }
//                        check_join_total_agree.setOnCheckedChangeListener(null)
//                        check_join_total_agree.isChecked = isAll
//                        check_join_total_agree.setOnCheckedChangeListener(this@UpdatePendingUserFragment)
//
//                        mTermsAgreeMap!![terms.no] = isChecked
//                    }
//                    mCheckBoxList!!.add(checkTerms)
//
//                    viewTerms.image_terms_story.setOnClickListener {
//                        val intent = Intent(activity, WebViewActivity::class.java)
//                        intent.putExtra(Const.TITLE, terms.subject)
//                        intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true)
//                        intent.putExtra(Const.WEBVIEW_URL, terms.url)
//                        intent.putExtra(Const.TERMS_LIST, mTermsList)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivity(intent)
//                    }
//
//                    layout_join_terms.addView(viewTerms)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Terms>>?, t: Throwable?, response: NewResultResponse<Terms>?) {
//
//            }
//        }).build().call()
//
//        check_join_total_agree.setOnCheckedChangeListener(this)
//
//
//        text_join_next.setOnClickListener {
//
//
//            val loginId = edit_join_id.text.toString().trim()
//
//            if (StringUtils.isEmpty(loginId)) {
//                scroll_join.smoothScrollTo(0, edit_join_id.top)
//                edit_join_id.requestFocus()
//                showAlert(R.string.msg_input_id)
//                return@setOnClickListener
//            }
//
//            if (loginId.length < 4) {
//                scroll_join.smoothScrollTo(0, edit_join_id.top)
//                edit_join_id.requestFocus()
//                showAlert(R.string.msg_input_over4)
//                return@setOnClickListener
//            }
//
//            if (!mIsCheckId || mSignedId != loginId) {
//                scroll_join.smoothScrollTo(0, edit_join_id.top)
//                edit_join_id.requestFocus()
//                showAlert(R.string.msg_check_duplication_id)
//                return@setOnClickListener
//            }
//
//            val password = edit_join_password.text.toString().trim()
//            if (password.isEmpty()) {
//                scroll_join.smoothScrollTo(0, edit_join_password.getTop())
//                edit_join_password.requestFocus()
//                showAlert(R.string.msg_input_password)
//                return@setOnClickListener
//            }
//
//            if (password.length < 4) {
//                scroll_join.smoothScrollTo(0, edit_join_password.getTop())
//                edit_join_password.requestFocus()
//                showAlert(getString(R.string.to_password) + " " + getString(R.string.format_msg_input_over, 4))
//                return@setOnClickListener
//            }
//
//            LoginInfoManager.getInstance().user.loginId = mSignedId
//            LoginInfoManager.getInstance().user.password = password
//
//            if (LoginInfoManager.getInstance().user!!.verification == null) {
//                showAlert(R.string.msg_request_mobile_verification)
//                return@setOnClickListener
//            }
//
//            for (i in mTermsList!!.indices) {
//                if (mTermsList!![i].isCompulsory && !mTermsAgreeMap!![mTermsList!![i].no]!!) {
//                    showAlert(R.string.msg_agree_terms)
//                    return@setOnClickListener
//                }
//            }
//
//            val termsList = ArrayList<No>()
//            for ((key1, value) in mTermsAgreeMap!!) {
//                if (value) {
//                    termsList.add(No(key1))
//                }
//            }
//
//            LoginInfoManager.getInstance().user.termsList = termsList
//
//            putUserStatus()
//        }
//    }
//
//    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//        if (mCheckBoxList != null) {
//            for (checkBox in mCheckBoxList!!) {
//                checkBox.isChecked = isChecked
//            }
//        } else {
//            check_join_total_agree.isChecked = !isChecked
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            Const.REQ_JOIN -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    activity?.setResult(RESULT_OK)
//                    activity?.finish()
//                }
//
//            }
//            Const.REQ_VERIFICATION -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//
//                        val user = data.getParcelableExtra<User>(Const.USER)
//
//                        if(user.mobile != LoginInfoManager.getInstance().user.mobile){
//                            showAlert(R.string.msg_not_matched_user)
//                            activity?.finish()
//                        }else{
//                            LoginInfoManager.getInstance().user.verification = user.verification
//                            LoginInfoManager.getInstance().user.mobile = user.mobile
//                            LoginInfoManager.getInstance().user.name = user.name
//                            LoginInfoManager.getInstance().user.birthday = user.birthday
//                            LoginInfoManager.getInstance().user.gender = user.gender
//                        }
//
//
//                    }
//                } else {
//                    activity?.finish()
//                }
//
//            }
//        }
//    }
//
//    private fun putUserStatus() {
//
//        showProgress("")
//
//        ApiBuilder.create().putUserStatus(LoginInfoManager.getInstance().user).setCallback(object : PplusCallback<NewResultResponse<String>> {
//
//            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
//                hideProgress()
//                LoginInfoManager.getInstance().user.password = PplusCommonUtil.encryption(LoginInfoManager.getInstance().user.password!!)
//                LoginInfoManager.getInstance().save()
//
//                if (!isAdded) {
//                    return
//                }
//                complete()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
////                when (response.resultCode) {
////                    504 -> showAlert(getString(R.string.msg_using_number))
////                    587 -> showAlert(getString(R.string.msg_need_agree_terms))
////                    583 -> showAlert(getString(R.string.msg_duplicate_id))
////                    610, 611 -> showAlert(getString(R.string.msg_can_not_use_number))
////                    503 -> showAlert(getString(R.string.msg_not_found_recommend))
////                    else -> showAlert(getString(R.string.msg_failed_join))
////                }
//            }
//        }).build().call()
//    }
//
//    private fun complete() {
//        val intent = Intent(activity, JoinCompleteActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        activity?.startActivityForResult(intent, Const.REQ_JOIN)
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this mapFragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of mapFragment JoinFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance() =
//                UpdatePendingUserFragment().apply {
//                    arguments = Bundle().apply {
//                    }
//                }
//    }
//}
