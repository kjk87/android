package com.pplus.prnumberuser.apps.main.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.friend.ui.PlusFragment
import com.pplus.prnumberuser.apps.search.ui.SearchFragment
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMainNumberBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MainNumberFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainNumberFragment : BaseFragment<AppMainActivity>() {

    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            type = arguments.getSerializable(Const.TYPE) as EnumData.AdsType
        }
    }

    private var _binding: FragmentMainNumberBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainNumberBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {
        binding.layoutMainNumberTab1.setOnClickListener {
            setPadFragment()
        }

        binding.layoutMainNumberTab2.setOnClickListener {
            setSearchFragment()
        }

        binding.layoutMainNumberTab3.setOnClickListener {
            setPlusFragment()
        }


        setPadFragment()
    }

    fun setPadFragment() {
        binding.layoutMainNumberTab1.isSelected = true
        binding.layoutMainNumberTab2.isSelected = false
        binding.layoutMainNumberTab3.isSelected = false
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.main_number_container, MainPadFragment.newInstance(null, null), MainPadFragment::class.java.simpleName)
        ft.commit()
    }

    fun setSearchFragment() {
        binding.layoutMainNumberTab1.isSelected = false
        binding.layoutMainNumberTab2.isSelected = true
        binding.layoutMainNumberTab3.isSelected = false
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.main_number_container, SearchFragment.newInstance(), SearchFragment::class.java.simpleName)
        ft.commit()
    }

    fun setPlusFragment() {
        if (!PplusCommonUtil.loginCheck(requireActivity(), null)) {
            return
        }

        binding.layoutMainNumberTab1.isSelected = false
        binding.layoutMainNumberTab2.isSelected = false
        binding.layoutMainNumberTab3.isSelected = true
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.main_number_container, PlusFragment.newInstance(), PlusFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getPID(): String {
        return "Main_number"
    }

    companion object {

        fun newInstance(): MainNumberFragment {

            val fragment = MainNumberFragment()
            val args = Bundle()
//            args.putSerializable(Const.TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
