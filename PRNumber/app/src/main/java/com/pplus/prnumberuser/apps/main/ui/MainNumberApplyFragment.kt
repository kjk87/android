package com.pplus.prnumberuser.apps.main.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityBizIntroduceBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MainNumberApplyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainNumberApplyFragment : BaseFragment<AppMainActivity>() {

    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            type = arguments.getSerializable(Const.TYPE) as EnumData.AdsType
        }
    }

    private var _binding: ActivityBizIntroduceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = ActivityBizIntroduceBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {
        binding.layoutBizIntroduceGoBiz.setOnClickListener {
            PplusCommonUtil.openChromeWebView(requireActivity(), "https://docs.google.com/forms/d/e/1FAIpQLSeRx79ivsfNT-ZkxfgHpXg72bMZaQByrA4GILO6n1zaUTrALA/viewform")
        }
    }

    override fun getPID(): String {
        return "Main_pr_eventlist"
    }

    companion object {

        fun newInstance(): MainNumberApplyFragment {

            val fragment = MainNumberApplyFragment()
            val args = Bundle()
//            args.putSerializable(Const.TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
