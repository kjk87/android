package com.pplus.luckybol.apps.signin.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.core.code.common.SnsTypeCode
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.databinding.FragmentFindIdresultBinding


class FindIDResultFragment : BaseFragment<FindIdActivity>() {
    private var mUser: User? = null

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mUser = requireArguments().getParcelable(Const.USER)
        }
    }

    private var _binding: FragmentFindIdresultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentFindIdresultBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        if (mUser!!.accountType == SnsTypeCode.pplus.name) {
            binding.textResultId.text = mUser!!.loginId?.replace(Const.APP_TYPE+"##", "")
        } else {
            when (mUser!!.accountType) {

                SnsTypeCode.facebook.name -> binding.textResultId.text = getString(R.string.msg_account_facebook)
                SnsTypeCode.naver.name -> binding.textResultId.text = getString(R.string.msg_account_naver)
                SnsTypeCode.google.name -> binding.textResultId.text = getString(R.string.msg_account_google)
                SnsTypeCode.kakao.name -> binding.textResultId.text = getString(R.string.msg_account_kakao)
                SnsTypeCode.apple.name -> binding.textResultId.text = getString(R.string.msg_account_apple)
            }
        }

        binding.textResultSignIn.setOnClickListener {
            requireActivity().finish()
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
