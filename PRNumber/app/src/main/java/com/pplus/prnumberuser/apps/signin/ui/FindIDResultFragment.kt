package com.pplus.prnumberuser.apps.signin.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.core.code.common.SnsTypeCode
import com.pplus.prnumberuser.core.network.model.dto.User
import com.pplus.prnumberuser.databinding.FragmentFindIdresultBinding


class FindIDResultFragment : BaseFragment<FindIdActivity>() {
    private var mUser: User? = null

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            mUser = it.getParcelable(Const.USER)
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
            binding.textResultId.text = mUser!!.loginId
        } else {
            when (SnsTypeCode.valueOf(mUser!!.accountType!!)) {

                SnsTypeCode.facebook -> binding.textResultId.text = getString(R.string.msg_account_facebook)
                SnsTypeCode.naver -> binding.textResultId.text = getString(R.string.msg_account_naver)
                SnsTypeCode.google -> binding.textResultId.text = getString(R.string.msg_account_google)
                SnsTypeCode.kakao -> binding.textResultId.text = getString(R.string.msg_account_kakao)
            }
        }

        binding.textResultSignIn.setOnClickListener {
            requireActivity().finish()
        }

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
