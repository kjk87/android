package com.pplus.luckybol.apps.main.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.ui.EventByGroupFragment
import com.pplus.luckybol.apps.event.ui.EventReviewActivity
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentMainEventBinding

class MainEventFragment : BaseFragment<AppMainActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            mTab = it.getString(Const.TAB)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    var _binding: FragmentMainEventBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainEventBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mAdapter: PagerAdapter? = null

    override fun init() {

        binding.tabLayoutMainEvent.setIsChangeBold(false)
        binding.tabLayoutMainEvent.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_fc5c57))
        binding.tabLayoutMainEvent.setCustomTabView(R.layout.item_event_tab, R.id.text_event_tab)
        binding.tabLayoutMainEvent.setBottomBorder(resources.getDimensionPixelSize(R.dimen.width_6))
        binding.tabLayoutMainEvent.setDistributeEvenly(true)
        binding.tabLayoutMainEvent.setDividerWidthHeight(0, 0)


        binding.pagerMainEvent.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                }
            }
        })

        binding.layoutMainEventFloating.setOnClickListener {
            val intent = Intent(activity, EventReviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        initPager()
    }

    fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                }
//                binding.textMainEventRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_bol_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
    }

    fun floating() : View{
        return binding.layoutMainEventFloating
    }

    private fun initPager() {

//        mAdapter!!.size = 2
//        mAdapter!!.notifyDataSetChanged()

        mAdapter = PagerAdapter(requireActivity())
        binding.pagerMainEvent.adapter = mAdapter
        binding.pagerMainEvent.offscreenPageLimit = 2

        val titleList = arrayListOf<String>()
        titleList.add(getString(R.string.word_daily_event))
        titleList.add(getString(R.string.word_sponsor_event))

        binding.tabLayoutMainEvent.setViewPager(binding.pagerMainEvent, titleList)
        binding.pagerMainEvent.currentItem = 0
    }


    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setRetentionBol()
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
//            if (LoginInfoManager.getInstance().isMember) {
//                setRetentionBol()
//                binding.textMainEventRetentionBol.visibility = View.VISIBLE
//                binding.textMainEventLogin.visibility = View.GONE
//            }else{
//                binding.textMainEventRetentionBol.visibility = View.GONE
//                binding.textMainEventLogin.visibility = View.VISIBLE
//            }
        }
    }

    inner class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        var fragMap: SparseArray<Fragment>


        init {
            fragMap = SparseArray()
        }

        override fun getItemCount(): Int {
            return 2
        }

        fun clear() {

            fragMap = SparseArray()
            notifyDataSetChanged()
        }

        fun getFragment(key: Int): Fragment {

            return fragMap.get(key)
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return EventByGroupFragment.newInstance(1)
                }
                else -> {
                    //                    return EventByGroupFragment.newInstance(1)
                    return EventByGroupFragment.newInstance(2)
                }
            }
        }

    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                MainEventFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(Const.TAB, type)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
