package com.pplus.prnumberbiz.apps.signup.ui


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const

import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.common.ui.common.WebViewActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.*
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.prnumber.IPRNumberRequest
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.fragment_join.*
import kotlinx.android.synthetic.main.item_terms.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.ArrayList
import java.util.HashMap

class JoinFragment : BaseFragment<JoinActivity>(), CompoundButton.OnCheckedChangeListener {

    private var paramsJoin: User? = null
    private var key: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramsJoin = it.getParcelable<User>(Const.USER)
            key = it.getString(Const.KEY)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_join
    }

    override fun initializeView(container: View?) {

    }

    internal enum class NumberType {
        wire, mobile, free
    }

    private var mIsSns = false
    private var mIsCheckId = false
    private var mSignedId = ""
    private var mTermsList: ArrayList<Terms>? = null
    private var mTermsAgreeMap: MutableMap<Long, Boolean>? = null
    private var mCheckBoxList: MutableList<CheckBox>? = null
    private var mPageType = ""

    override fun init() {

        val api: IPRNumberRequest<Terms>

        if (key.equals(Const.LEVEL_UP)) {
            layout_join_id.visibility = View.GONE
            layout_join_recommend.visibility = View.GONE

            paramsJoin!!.mobile = LoginInfoManager.getInstance().user.mobile

            api = ApiBuilder.create().getNotSignedActiveTermsAll(activity!!.packageName)
        } else {
            if (StringUtils.isNotEmpty(paramsJoin!!.accountType) && paramsJoin!!.accountType != SnsTypeCode.pplus.name) {
                layout_join_id.visibility = View.GONE
                mIsSns = true
            } else {
                layout_join_id.visibility = View.VISIBLE
                mIsSns = false
            }

            val recommend = PreferenceUtil.getDefaultPreference(activity).getString(Const.RECOMMEND)
            if (StringUtils.isNotEmpty(recommend)) {
                edit_join_recommend_code.setText(recommend)
            }

            api = ApiBuilder.create().getActiveTermsAll(activity!!.packageName)
        }

        text_join_id_title.text = PplusCommonUtil.fromHtml(getString(R.string.html_id))
        text_join_password_title.text = PplusCommonUtil.fromHtml(getString(R.string.html_password))

        edit_join_id.setSingleLine()
        edit_join_id.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val id = s.toString().trim()
                if (id.length > 3) {

                    val regex = Regex(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")
                    if (id.matches(regex)) {
                        text_join_id_state.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_reject, 0, 0, 0)
                        text_join_id_state.setTextColor(ResourceUtil.getColor(activity, R.color.color_ff4646))
                        text_join_id_state.setText(R.string.msg_input_valid_id)
                        text_join_id_state.visibility = View.VISIBLE
                        return
                    }
                    val params = HashMap<String, String>()
                    params["loginId"] = id
                    ApiBuilder.create().userCheck(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
                        override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                            if (response != null) {
                                val result = response.data
                                when (result) {
                                    "success" -> {
                                        mIsCheckId = true
                                        mSignedId = id

                                        text_join_id_state.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_ok, 0, 0, 0)
                                        text_join_id_state.setTextColor(ResourceUtil.getColor(activity, R.color.color_579ffb))
                                        text_join_id_state.setText(R.string.msg_usable_id)
                                        text_join_id_state.visibility = View.VISIBLE
                                    }
                                    "fail" -> {
                                        mIsCheckId = false
                                        text_join_id_state.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_reject, 0, 0, 0)
                                        text_join_id_state.setTextColor(ResourceUtil.getColor(activity, R.color.color_ff4646))
                                        text_join_id_state.setText(R.string.msg_duplicate_id)
                                        text_join_id_state.visibility = View.VISIBLE
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {

                        }
                    }).build().call()
                } else {
                    text_join_id_state.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

//        text_join_verification.visibility = View.VISIBLE
//        layout_join_complete_verification.visibility = View.GONE
//        text_join_verification.setOnClickListener {
//            val intent = Intent(activity, VerificationMeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            intent.putExtra(Const.KEY, Const.JOIN)
//            activity!!.startActivityForResult(intent, Const.REQ_VERIFICATION)
//        }

        api.setCallback(object : PplusCallback<NewResultResponse<Terms>> {
            override fun onResponse(call: Call<NewResultResponse<Terms>>?, response: NewResultResponse<Terms>?) {

                if (!isAdded) {
                    return
                }

                mTermsList = response!!.datas as ArrayList<Terms>
                mTermsAgreeMap = HashMap()
                layout_join_terms.removeAllViews()
                mCheckBoxList = ArrayList<CheckBox>()
                for (i in mTermsList!!.indices) {
                    val terms = mTermsList!![i]
                    mTermsAgreeMap!![terms.no] = false
                    val viewTerms = LayoutInflater.from(activity).inflate(R.layout.item_terms, LinearLayout(activity))
                    if (i != 0) {
                        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        layoutParams.setMargins(0, resources.getDimensionPixelSize(R.dimen.height_50), 0, 0)
                        viewTerms.layoutParams = layoutParams
                    }
                    val checkTerms = viewTerms.findViewById(R.id.check_terms_agree) as CheckBox
                    checkTerms.text = terms.subject
                    checkTerms.setOnCheckedChangeListener { buttonView, isChecked ->
                        var isAll = true
                        for (checkBox in mCheckBoxList!!) {
                            if (!checkBox.isChecked) {
                                isAll = false
                                break
                            }
                        }
                        check_join_total_agree.setOnCheckedChangeListener(null)
                        check_join_total_agree.isChecked = isAll
                        check_join_total_agree.setOnCheckedChangeListener(this@JoinFragment)

                        mTermsAgreeMap!![terms.no] = isChecked
                    }
                    mCheckBoxList!!.add(checkTerms)

                    viewTerms.image_terms_story.setOnClickListener {
                        val intent = Intent(activity, WebViewActivity::class.java)
                        intent.putExtra(Const.TITLE, terms.subject)
                        intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true)
                        intent.putExtra(Const.WEBVIEW_URL, terms.url)
                        intent.putExtra(Const.TERMS_LIST, mTermsList)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }

                    layout_join_terms.addView(viewTerms)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Terms>>?, t: Throwable?, response: NewResultResponse<Terms>?) {

            }
        }).build().call()


        layout_join_page_type_person.setOnClickListener {
            mPageType = EnumData.PageTypeCode.person.name
            layout_join_page_type_person.isSelected = true
            layout_join_page_type_shop.isSelected = false
        }

        layout_join_page_type_shop.setOnClickListener {
            mPageType = EnumData.PageTypeCode.shop.name
            layout_join_page_type_person.isSelected = false
            layout_join_page_type_shop.isSelected = true
        }

        mPageType = EnumData.PageTypeCode.shop.name
        layout_join_page_type_person.isSelected = false
        layout_join_page_type_shop.isSelected = true
        edit_join_page_name.setSingleLine()

        check_join_total_agree.setOnCheckedChangeListener(this)

        text_join_next.setOnClickListener {


            if (!key.equals(Const.LEVEL_UP)) {

                var password = ""
                if (!mIsSns) {
                    val loginId = edit_join_id.text.toString().trim()


                    if (StringUtils.isEmpty(loginId)) {
                        scroll_join.smoothScrollTo(0, edit_join_id.top)
                        edit_join_id.requestFocus()
                        showAlert(R.string.msg_input_id)
                        return@setOnClickListener
                    }

                    if (loginId.length < 4) {
                        scroll_join.smoothScrollTo(0, edit_join_id.top)
                        edit_join_id.requestFocus()
                        showAlert(R.string.msg_input_over4)
                        return@setOnClickListener
                    }

                    val regex = Regex(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")

                    if (loginId.matches(regex)) {
                        showAlert(R.string.msg_input_valid_id);
                        return@setOnClickListener
                    }

                    if (!mIsCheckId || mSignedId != loginId) {
                        scroll_join.smoothScrollTo(0, edit_join_id.top)
                        edit_join_id.requestFocus()
                        showAlert(R.string.msg_duplicate_id)
                        return@setOnClickListener
                    }

                    password = edit_join_password.text.toString().trim()
                    if (StringUtils.isEmpty(password)) {
                        scroll_join.smoothScrollTo(0, edit_join_password.getTop())
                        edit_join_password.requestFocus()
                        showAlert(R.string.msg_input_password)
                        return@setOnClickListener
                    }

                    if (password.length < 4) {
                        scroll_join.smoothScrollTo(0, edit_join_password.getTop())
                        edit_join_password.requestFocus()
                        showAlert(getString(R.string.to_password) + " " + getString(R.string.format_msg_input_over, 4))
                        return@setOnClickListener
                    }
                }

                if (!mIsSns) {
                    paramsJoin!!.loginId = mSignedId
                    paramsJoin!!.password = password
                    paramsJoin!!.accountType = SnsTypeCode.pplus.name
                }
            }

//            if (paramsJoin!!.verification == null) {
//                showAlert(R.string.msg_request_mobile_verification)
//                return@setOnClickListener
//            }

            val page = Page()


            val pageName = edit_join_page_name.text.toString().trim()

            if (StringUtils.isEmpty(pageName)) {
                scroll_join.smoothScrollTo(0, edit_join_page_name.top)
                edit_join_password.requestFocus()
                showAlert(R.string.msg_input_page_name)
                return@setOnClickListener
            }

            val catchphrase = edit_join_page_catchphrase.text.toString().trim()

            if (StringUtils.isEmpty(catchphrase)) {
                scroll_join.smoothScrollTo(0, edit_join_page_catchphrase.top)
                edit_join_password.requestFocus()
                showAlert(R.string.msg_input_catchphrase)
                return@setOnClickListener
            }

            page.name = pageName
            page.catchphrase = catchphrase
            page.status = EnumData.PageStatus.ready.name
            page.type = mPageType
            paramsJoin!!.page = page

            for (i in mTermsList!!.indices) {
                if (mTermsList!![i].isCompulsory && !mTermsAgreeMap!![mTermsList!![i].no]!!) {
                    showAlert(R.string.msg_agree_terms)
                    return@setOnClickListener
                }
            }

            val termsList = ArrayList<No>()
            for ((key1, value) in mTermsAgreeMap!!) {
                if (value) {
                    termsList.add(No(key1))
                }
            }

            paramsJoin!!.termsList = termsList

            val number = PRNumber()
            number.number = paramsJoin!!.mobile
            if (key.equals(Const.LEVEL_UP)) {
                levelUp(number)
            } else {
                paramsJoin!!.number = number
                signUp()
            }

//            val intent = Intent(activity, SelectPageTypeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SET_PAGE_TYPE)

        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (mCheckBoxList != null) {
            for (checkBox in mCheckBoxList!!) {
                checkBox.isChecked = isChecked
            }
        } else {
            check_join_total_agree.isChecked = !isChecked
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.REQ_SET_PAGE_TYPE -> {
                    if (resultCode == RESULT_OK) {
                        if (data != null) {
                            val page = Page()
                            page.type = data.getStringExtra(Const.TYPE)
                            paramsJoin!!.page = page

                            val number = PRNumber()
                            number.number = paramsJoin!!.mobile
                            if (key.equals(Const.LEVEL_UP)) {
                                levelUp(number)
                            } else {
                                paramsJoin!!.number = number
                                signUp()
                            }

//                            parentActivity.joinNumber(paramsJoin!!)
                        }

                    }
                }
                Const.REQ_JOIN -> {
                    activity?.setResult(RESULT_OK)
                    activity?.finish()
                }
//                Const.REQ_VERIFICATION -> {
//                    if (data != null) {
//
//                        val user = data.getParcelableExtra<User>(Const.USER)
//                        if (key.equals(Const.LEVEL_UP)) {
//                            text_join_verification.visibility = View.GONE
//                            layout_join_complete_verification.visibility = View.VISIBLE
//                            paramsJoin!!.verification = user.verification
//                            paramsJoin!!.mobile = user.mobile
//                            paramsJoin!!.name = user.name
//                            paramsJoin!!.birthday = user.birthday
//                            paramsJoin!!.gender = user.gender
//                        } else {
//                            checkExistUser(user)
//                        }
//
//                    }
//                }
            }
        }
    }

    private fun signUp() {

        showProgress("")

        ApiBuilder.create().join(paramsJoin).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {

                hideProgress()
                LoginInfoManager.getInstance().user = response.data
                LoginInfoManager.getInstance().user.password = PplusCommonUtil.encryption(paramsJoin!!.password!!)
                LoginInfoManager.getInstance().save()

                if (!isAdded) {
                    return
                }
                complete()

            }

            override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {

                hideProgress()
                if (!isAdded) {
                    return
                }
                when (response.resultCode) {
                    504 -> showAlert(getString(R.string.msg_using_number))
                    587 -> showAlert(getString(R.string.msg_need_agree_terms))
                    583 -> showAlert(getString(R.string.msg_duplicate_id))
                    610, 611 -> showAlert(getString(R.string.msg_can_not_use_number))
                    503 -> showAlert(getString(R.string.msg_not_found_recommend))
                    else -> showAlert(getString(R.string.msg_failed_join))
                }
            }
        }).build().call()
    }

    fun levelUp(number: PRNumber) {
        paramsJoin!!.number = number
        paramsJoin!!.no = LoginInfoManager.getInstance().user.no
        showProgress("")

        ApiBuilder.create().levelup(paramsJoin).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                complete()
            }

            override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {

                hideProgress()
                when (response.resultCode) {
                    504 -> showAlert(getString(R.string.msg_using_number))
                    587 -> showAlert(getString(R.string.msg_need_agree_terms))
                    610, 611 -> showAlert(getString(R.string.msg_can_not_use_number))
                    else -> showAlert(getString(R.string.msg_failed_levelup))
                }
            }
        }).build().call()
    }

//    private fun checkExistUser(user: User) {
//        val params = HashMap<String, String>()
//        params["mobile"] = user.mobile!!
//        ApiBuilder.create().existsUser(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                val builder = AlertBuilder.Builder()
//                builder.setTitle(getString(R.string.word_notice_alert))
//                builder.addContents(AlertData.MessageData(getString(R.string.msg_exist_user_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                builder.addContents(AlertData.MessageData(getString(R.string.msg_exist_user_alert2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//                builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                    override fun onCancel() {
//
//                    }
//
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                        when (event_alert) {
//                            AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                val intent = Intent(activity, FindIdActivity::class.java)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                startActivity(intent)
//                                activity?.finish()
//                            }
//                        }
//                    }
//                }).builder().show(activity)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                text_join_verification.visibility = View.GONE
//                layout_join_complete_verification.visibility = View.VISIBLE
//                paramsJoin!!.verification = user.verification
//                paramsJoin!!.mobile = user.mobile
//                paramsJoin!!.name = user.name
//                paramsJoin!!.birthday = user.birthday
//                paramsJoin!!.gender = user.gender
//
//            }
//        }).build().call()
//    }

    private fun complete() {
        val intent = Intent(activity, JoinCompleteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity?.startActivityForResult(intent, Const.REQ_JOIN)
    }

    override fun getPID(): String {
        return ""
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this mapFragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of mapFragment JoinFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(key: String, param: User) =
                JoinFragment().apply {
                    arguments = Bundle().apply {
                        putString(Const.KEY, key)
                        putParcelable(Const.USER, param)
                    }
                }
    }
}
