//package com.pplus.prnumberuser.apps.use.ui
//
//
//import android.app.Activity.RESULT_OK
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.view.View
//import android.widget.TextView
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Advertise
//import com.pplus.prnumberuser.core.network.model.dto.Coupon
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.fragment_verification_number_confirm.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [UseCouponFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class UseCouponFragment : BaseFragment<BaseActivity>() {
//
//    private var mNumberSb: StringBuilder? = StringBuilder();
//    private val mTextNumbers = arrayOfNulls<TextView>(4)
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_verification_number_confirm;
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//        mTextNumbers[0] = text_verification_number_config_number1;
//        mTextNumbers[1] = text_verification_number_config_number2;
//        mTextNumbers[2] = text_verification_number_config_number3;
//        mTextNumbers[3] = text_verification_number_config_number4;
//
//        view_verification_number_pad?.setOnKeyClickListener { key ->
//            if (key.number == "#") {
//                if (mNumberSb!!.isNotEmpty()) {
//                    mTextNumbers[mNumberSb!!.length - 1]!!.text = "";
//                    mNumberSb!!.setLength(mNumberSb!!.length - 1);
//                }
//            } else if (key.number != "*") {
//                val number: String = key.number
//                if (mNumberSb!!.length < 4) {//4자리까지만 입력
//                    mNumberSb!!.append(number);
//                    mTextNumbers[mNumberSb!!.length - 1]!!.text = "*";
//                }
//
//                if (mNumberSb!!.length == 4) {//입력 완료후 처리
//                    val params = HashMap<String, String>()
//                    params.put("no", mCoupon!!.template?.publisher?.no.toString())
//
//                    params.put("authCode", mNumberSb.toString())
//                    showProgress("")
//                    ApiBuilder.create().checkAuthCodeForUser(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//                        override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
//                            hideProgress()
//                            use()
//                        }
//
//                        override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
//                            hideProgress()
//                            mNumberSb = StringBuilder()
//                            for (textView in mTextNumbers) {
//                                textView!!.text = ""
//                            }
//
//                            var failCount = response!!.data
//                            val builder = AlertBuilder.Builder()
//                            if (failCount < 5) {
//                                builder.setTitle(getString(R.string.format_auth_code_error, failCount))
//                                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_invalid_auth_code1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_invalid_auth_code2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                                builder.setLeftText(getString(R.string.word_confirm))
//                                builder.builder().show(activity)
//                            } else {
//                                builder.setTitle(getString(R.string.word_blocking_use))
//                                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_invalid_auth_code3), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                                builder.setLeftText(getString(R.string.word_confirm))
//                                builder.builder().show(activity)
//                            }
//
//                        }
//                    }).build().call()
//                }
//            }
//        };
//    }
//
//    private fun use() {
//        val params = HashMap<String, String>()
//        if (mCoupon!!.advertise != null) {
//            params.put("no", mCoupon!!.advertise.no.toString())
//        }
//
//        params.put("code", mCoupon!!.code)
//        showProgress("")
//        ApiBuilder.create().useCouponAdvertise(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                hideProgress()
//                var builder = AlertBuilder.Builder()
//                builder.setTitle(getString(R.string.word_use_complete))
//
//                if (mCoupon!!.advertise != null) {
//                    var point = 100L
//                    point += mCoupon!!.advertise!!.reward!!
//
//                    builder.addContents(AlertData.MessageData(getString(R.string.format_msg_reward_point, FormatUtil.getMoneyType(point.toString())), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                } else {
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_used_coupon), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                }
//
//                builder.setLeftText(getString(R.string.word_confirm))
//                builder.setOnAlertResultListener(object : OnAlertResultListener {
//                    override fun onCancel() {
//
//                    }
//
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
//                        when (event_alert) {
//                            AlertBuilder.EVENT_ALERT.SINGLE -> {
//
//                            }
//                        }
//                    }
//                }).builder().show(activity)
//                activity!!.setResult(RESULT_OK)
//                activity!!.finish()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                hideProgress()
////                            showAlert(R.string.msg_invalid_verification_number)
//
//
//                if (response!!.resultCode == 514) {
//
//                }
//            }
//        }).build().call()
//    }
//
//    var mCoupon: Coupon? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            mCoupon = arguments!!.getParcelable<Coupon>(Const.COUPON)
//        }
//    }
//
//
//    companion object {
//
//        // TODO: Rename and change types and number of parameters
//        fun newInstance(coupon: Coupon): UseCouponFragment {
//
//            val fragment = UseCouponFragment()
//            val args = Bundle()
//            args.putParcelable(Const.COUPON, coupon)
////            args.putString(ARG_PARAM2, param2)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
