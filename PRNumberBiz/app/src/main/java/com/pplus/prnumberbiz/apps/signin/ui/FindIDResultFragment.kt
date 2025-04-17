package com.pplus.prnumberbiz.apps.signin.ui


import android.os.Bundle
import android.view.View
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.network.model.dto.User
import kotlinx.android.synthetic.main.fragment_find_idresult.*


class FindIDResultFragment : BaseFragment<FindIdActivity>() {
    private var mUser: User? = null

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mUser = arguments!!.getParcelable(Const.USER)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_find_idresult
    }

    override fun initializeView(container: View?) {

    }

    override fun init() {

        if (mUser!!.accountType == SnsTypeCode.pplus.name) {
            text_result_id.text = mUser!!.loginId
        } else {
            when (SnsTypeCode.valueOf(mUser!!.accountType!!)) {

                SnsTypeCode.facebook -> text_result_id.text = getString(R.string.msg_account_facebook)
                SnsTypeCode.naver -> text_result_id.text = getString(R.string.msg_account_naver)
                SnsTypeCode.google -> text_result_id.text = getString(R.string.msg_account_google)
                SnsTypeCode.kakao -> text_result_id.text = getString(R.string.msg_account_kakao)
            }
        }

        text_result_signIn.setOnClickListener {
            activity!!.finish()
        }

//        text_result_findPw.setOnClickListener {
//            val data = Intent()
//            data.putExtra(Const.KEY, Const.FIND_PW)
//            activity!!.setResult(Activity.RESULT_OK, data)
//            activity!!.finish()
//        }
//
//        text_find_id_result_confirm.setOnClickListener {
//            text_find_id_result_confirm
//        }

    }


    companion object {

        private val SIGNED_ID = "signed_id"

        fun newInstance(user: User): FindIDResultFragment {

            val fragment = FindIDResultFragment()
            val args = Bundle()
            args.putParcelable(Const.USER, user)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
