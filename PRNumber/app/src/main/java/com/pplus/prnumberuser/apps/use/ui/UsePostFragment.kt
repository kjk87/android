//package com.pplus.prnumberuser.apps.use.ui
//
//
//import android.os.Bundle
//import android.view.View
//import android.widget.TextView
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Page2
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.fragment_verification_number_confirm.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [UsePostFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class UsePostFragment : BaseFragment<BaseActivity>() {
//
//    private var mNumberSb: StringBuilder? = StringBuilder();
//    private val mTextNumbers = arrayOfNulls<TextView>(4)
//    private var mPage:Page2? = null
//
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
//                    mNumberSb!!.setLength(mNumberSb!!.length - 1)
//                }
//            } else if (key.number != "*") {
//                val number: String = key.number
//                if (mNumberSb!!.length < 4) {//4자리까지만 입력
//                    mNumberSb!!.append(number)
//                    mTextNumbers[mNumberSb!!.length - 1]!!.text = "*";
//                }
//
//                if (mNumberSb!!.length == 4) {//입력 완료후 처리
//                    val params = HashMap<String, String>()
//
//                    params.put("no", mPage?.seqNo.toString())
//
//                    params.put("authCode", mNumberSb.toString())
//                    showProgress("")
//                    ApiBuilder.create().checkAuthCodeForUser(params).setCallback(object : PplusCallback<NewResultResponse<Int>>{
//                        override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
//                            hideProgress()
//
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
//                            if(failCount < 5){
//                                builder.setTitle(getString(R.string.format_auth_code_error, failCount))
//                                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_invalid_auth_code1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_invalid_auth_code2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                                builder.setLeftText(getString(R.string.word_confirm))
//                                builder.builder().show(activity)
//                            }else{
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
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
////            mAdvertise = arguments!!.getParcelable<Advertise>(Const.ADVERTISE)
//        }
//    }
//
//
//    companion object {
//
//        // TODO: Rename and change types and number of parameters
//        fun newInstance(page:Page2): UsePostFragment {
//
//            val fragment = UsePostFragment()
//            val args = Bundle()
////            args.putParcelable(Const.ADVERTISE, advertise)
////            args.putString(ARG_PARAM2, param2)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
