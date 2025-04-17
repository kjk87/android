package com.pplus.prnumberbiz.apps.signup.ui


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const

import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.*
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.PplusNumberUtil
import kotlinx.android.synthetic.main.fragment_join_number.*
import network.common.PplusCallback
import retrofit2.Call

class JoinNumberFragment : BaseFragment<JoinActivity>() {

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
        return R.layout.fragment_join_number
    }

    override fun initializeView(container: View?) {

    }

    internal enum class NumberType {
        wire, mobile, free
    }

    private var mWireNumber = ""
    private var mFreeNumber = ""
    private var mNumberType: NumberType? = null
    private var mPrNumber = ""
    private var mFreePrefix = ""

    override fun init() {

        mFreePrefix = PreferenceUtil.getDefaultPreference(activity).getString(Const.FREE_NUMBER_PREFIX)
//        text_join_number_free_prefix.text = mFreePrefix

        when (paramsJoin!!.page!!.type) {
            EnumData.PageTypeCode.store.name -> {
                text_join_number_store.visibility = View.VISIBLE
                text_join_number_mobile.visibility = View.VISIBLE
                text_join_number_free.visibility = View.GONE

                mNumberType = NumberType.wire
                text_join_number_store.isSelected = true
                text_join_number_mobile.isSelected = false
                text_join_number_free.isSelected = false

                layout_join_number_store.visibility = View.VISIBLE
                layout_join_number_mobile.visibility = View.GONE
                layout_join_number_free.visibility = View.GONE

                text_join_number_mobile_description.setText(R.string.msg_mobile_prnumber_description1)
            }
            EnumData.PageTypeCode.person.name -> {
                text_join_number_store.visibility = View.GONE
                text_join_number_free.visibility = View.VISIBLE
                text_join_number_mobile.visibility = View.VISIBLE

                mNumberType = NumberType.mobile
                text_join_number_store.isSelected = false
                text_join_number_mobile.isSelected = true
                text_join_number_free.isSelected = false

                layout_join_number_store.visibility = View.GONE
                layout_join_number_mobile.visibility = View.VISIBLE
                layout_join_number_free.visibility = View.GONE
//                text_join_number_mobile_description.setText(R.string.msg_mobile_prnumber_description2)
            }
        }

        text_join_number_mobile_nmber.text = PplusNumberUtil.getOnlyNumber(paramsJoin!!.mobile)

        text_join_number_store.setOnClickListener {
            mNumberType = NumberType.wire
            text_join_number_store.isSelected = true
            text_join_number_mobile.isSelected = false

            layout_join_number_store.visibility = View.VISIBLE
            layout_join_number_mobile.visibility = View.GONE
        }

        text_join_number_mobile.setOnClickListener {
            mNumberType = NumberType.mobile

            when (paramsJoin!!.page!!.type) {
                EnumData.PageTypeCode.store.name -> {
                    text_join_number_store.isSelected = false
                    text_join_number_mobile.isSelected = true

                    layout_join_number_store.visibility = View.GONE
                    layout_join_number_mobile.visibility = View.VISIBLE
                }
                EnumData.PageTypeCode.person.name -> {
                    text_join_number_mobile.isSelected = true
                    text_join_number_free.isSelected = false

                    layout_join_number_mobile.visibility = View.VISIBLE
                    layout_join_number_free.visibility = View.GONE
                }
            }
        }

        text_join_number_free.setOnClickListener {
            mNumberType = NumberType.free
            text_join_number_mobile.isSelected = false
            text_join_number_free.isSelected = true

            layout_join_number_mobile.visibility = View.GONE
            layout_join_number_free.visibility = View.VISIBLE
        }


        text_join_number_store_prefix.setOnClickListener {

            val areaCode = resources.getStringArray(R.array.location_code)
            val areaCodeBuilder = AlertBuilder.Builder()
            areaCodeBuilder.setTitle(null).setContents(*areaCode)
            areaCodeBuilder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
            areaCodeBuilder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> {
                            text_join_number_store_prefix.text = areaCode[event_alert.getValue() - 1]
                        }
                    }
                }
            }).builder().show(activity)
        }

        edit_join_number_store_number.setSingleLine()
        edit_join_number_store_number.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {

                var number = editable.toString()
                number = number.replace("-", "")
                if (mWireNumber != number) {
                    mWireNumber = number
                    edit_join_number_store_number.setText(PplusNumberUtil.getOnlyNumber(number))
                    edit_join_number_store_number.setSelection(edit_join_number_store_number.text.length)
                }
            }
        })

        edit_join_number_free_number.setSingleLine()
        edit_join_number_free_number.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {

                var number = editable.toString()
                number = number.replace("-", "")
                if (mFreeNumber != number) {
                    mFreeNumber = number
                    edit_join_number_free_number.setText(PplusNumberUtil.getOnlyNumber(number))
                    edit_join_number_free_number.setSelection(edit_join_number_store_number.text.length)
                }
            }
        })

        text_join_number_complete.setOnClickListener {

            mPrNumber = ""

            when (mNumberType) {
                NumberType.wire -> {
                    if (StringUtils.isEmpty(mWireNumber)) {
                        showAlert(R.string.msg_input_wireNumber)
                        return@setOnClickListener
                    }

                    if (mWireNumber.length < 7) {
                        showAlert(R.string.msg_input_wireNumber_over)
                        return@setOnClickListener
                    }

                    val prefix = text_join_number_store_prefix.text.toString()
                    mPrNumber = prefix + mWireNumber
                }
                NumberType.mobile -> {
                    mPrNumber = paramsJoin!!.mobile!!
                }
                NumberType.free -> {
                    if (StringUtils.isEmpty(mFreeNumber)) {
                        showAlert(R.string.msg_input_free_number)
                        return@setOnClickListener
                    }

                    if (mFreeNumber.length < 7) {
                        showAlert(R.string.msg_input_free_number_over)
                        return@setOnClickListener
                    }

                    mPrNumber = mFreePrefix + mFreeNumber
                }
            }

            val intent = Intent(activity, JoinConfirmActivity::class.java)
            intent.putExtra(Const.DATA, mPrNumber)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity!!.startActivityForResult(intent, Const.REQ_CONFIRM)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.REQ_JOIN -> {
                    activity?.setResult(RESULT_OK)
                    activity?.finish()
                }
                Const.REQ_CONFIRM -> {
                    val number = PRNumber()
                    number.number = mPrNumber
                    if (key.equals(Const.LEVEL_UP)) {
                        levelUp(number)
                    } else {
                        paramsJoin!!.number = number
                        signUp()
                    }
                }
            }
        }
    }


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
                JoinNumberFragment().apply {
                    arguments = Bundle().apply {
                        putString(Const.KEY, key)
                        putParcelable(Const.USER, param)
                    }
                }
    }
}
