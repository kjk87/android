package com.pplus.prnumberbiz.apps.number.ui


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
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusNumberUtil
import kotlinx.android.synthetic.main.fragment_join_number.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class MakeNumberFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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
    private var mFreePrefix = ""
    private var mPrNumber = ""

    private var mPage: Page? = null

    override fun init() {
        mPage = LoginInfoManager.getInstance().user.page
        mFreePrefix = PreferenceUtil.getDefaultPreference(activity).getString(Const.FREE_NUMBER_PREFIX)
        text_join_number_free_prefix.text = mFreePrefix

        when (mPage!!.type) {
            EnumData.PageTypeCode.store.name, EnumData.PageTypeCode.shop.name -> {

                mNumberType = NumberType.wire
                text_join_number_store.isSelected = true
                text_join_number_mobile.isSelected = false
                text_join_number_free.isSelected = false

                layout_join_number_store.visibility = View.VISIBLE
                layout_join_number_mobile.visibility = View.GONE
                layout_join_number_free.visibility = View.GONE
            }
            EnumData.PageTypeCode.person.name -> {

                mNumberType = NumberType.mobile
                text_join_number_store.isSelected = false
                text_join_number_mobile.isSelected = true
                text_join_number_free.isSelected = false

                layout_join_number_store.visibility = View.GONE
                layout_join_number_mobile.visibility = View.VISIBLE
                layout_join_number_free.visibility = View.GONE
            }
        }

        text_join_number_mobile_nmber.text = PplusNumberUtil.getOnlyNumber(LoginInfoManager.getInstance().user.mobile)

        text_join_number_store.setOnClickListener {
            mNumberType = NumberType.wire
            text_join_number_store.isSelected = true
            text_join_number_mobile.isSelected = false
            text_join_number_free.isSelected = false

            layout_join_number_store.visibility = View.VISIBLE
            layout_join_number_mobile.visibility = View.GONE
            layout_join_number_free.visibility = View.GONE
        }

        text_join_number_mobile.setOnClickListener {
            mNumberType = NumberType.mobile

            text_join_number_store.isSelected = false
            text_join_number_mobile.isSelected = true
            text_join_number_free.isSelected = false

            layout_join_number_store.visibility = View.GONE
            layout_join_number_mobile.visibility = View.VISIBLE
            layout_join_number_free.visibility = View.GONE
        }

        text_join_number_free.setOnClickListener {
            mNumberType = NumberType.free
            text_join_number_store.isSelected = false
            text_join_number_mobile.isSelected = false
            text_join_number_free.isSelected = true

            layout_join_number_store.visibility = View.GONE
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
                    edit_join_number_free_number.setSelection(edit_join_number_free_number.text.length)
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
                    mPrNumber = LoginInfoManager.getInstance().user.mobile!!
                }
                NumberType.free -> {
                    if (StringUtils.isEmpty(mFreeNumber)) {
                        showAlert(R.string.msg_input_free_number)
                        return@setOnClickListener
                    }

                    if (mFreeNumber.length < 6) {
                        showAlert(R.string.msg_input_free_number_over)
                        return@setOnClickListener
                    }

                    mPrNumber = mFreePrefix + mFreeNumber
                }
            }

            val intent = Intent(activity, MakePrnumberConfirmActivity::class.java)
            intent.putExtra(Const.DATA, mPrNumber)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity!!.startActivityForResult(intent, Const.REQ_CONFIRM)
        }
    }

    private fun updatePrnumber(prnumber: String) {
        val params = HashMap<String, String>()
        params["number"] = prnumber
        params["pageNo"] = mPage!!.no.toString()
        showProgress("")
        ApiBuilder.create().allocateVirtualNumberToPage(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                if (!isAdded) {
                    return
                }
                hideProgress()
                showAlert(R.string.msg_saved)
                activity?.setResult(RESULT_OK)
                activity?.finish()


            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                when (response!!.resultCode) {
                    504 -> showAlert(getString(R.string.msg_using_number))
                    610, 611 -> showAlert(getString(R.string.msg_can_not_use_number))
                }
            }
        }).build().call()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.REQ_CONFIRM -> {
                    updatePrnumber(mPrNumber)
                }
            }
        }
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
        fun newInstance() =
                MakeNumberFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(Const.KEY, key)
//                        putParcelable(Const.USER, param)
                    }
                }
    }
}
