package com.pplus.luckybol.apps.main.ui


import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.ui.TuneEventGroupAFragment
import com.pplus.luckybol.apps.event.ui.TuneEventGroupBFragment
import com.pplus.luckybol.databinding.FragmentMainTargetEventBinding

class MainTargetEventFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTab = it.getInt(Const.TAB, 0)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainTargetEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainTargetEventBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mAdapter: PagerAdapter? = null

    var mTab = 0

    override fun init() {

        binding.pagerMainTargetEvent.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.layoutMainTargetEventTuneATab.isSelected = true
                        binding.layoutMainTargetEventTuneBTab.isSelected = false
                    }
                    1 -> {
                        binding.layoutMainTargetEventTuneATab.isSelected = false
                        binding.layoutMainTargetEventTuneBTab.isSelected = true
                    }
                }
            }
        })


        binding.layoutMainTargetEventTuneATab.setOnClickListener {
            binding.layoutMainTargetEventTuneATab.isSelected = true
            binding.layoutMainTargetEventTuneBTab.isSelected = false
            binding.pagerMainTargetEvent.currentItem = 0
        }

        binding.layoutMainTargetEventTuneBTab.setOnClickListener {
            binding.layoutMainTargetEventTuneATab.isSelected = false
            binding.layoutMainTargetEventTuneBTab.isSelected = true
            binding.pagerMainTargetEvent.currentItem = 1
        }

        initPager()
    }

    private fun initPager() {

        //        mAdapter!!.size = 2
        //        mAdapter!!.notifyDataSetChanged()

        mAdapter = PagerAdapter(childFragmentManager)
        binding.pagerMainTargetEvent.adapter = mAdapter
        binding.pagerMainTargetEvent.offscreenPageLimit = 2
        binding.pagerMainTargetEvent.currentItem = mTab

        binding.layoutMainTargetEventTuneATab.isSelected = true
        binding.layoutMainTargetEventTuneBTab.isSelected = false
    }

    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        var fragMap: SparseArray<Fragment>
            internal set

        init {
            fragMap = SparseArray()
        }

        override fun getCount(): Int {
            return 2
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
            fragMap.remove(position)
        }

        fun clear() {

            fragMap = SparseArray()
            notifyDataSetChanged()
        }

        fun getFragment(key: Int): Fragment {

            return fragMap.get(key)
        }

        override fun getItem(position: Int): Fragment {


            when (position) {
                0 -> {
                    return TuneEventGroupAFragment.newInstance()
                }
                else -> {
                    return TuneEventGroupBFragment.newInstance()
                }
            }
        }
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance(tab: Int) = MainTargetEventFragment().apply {
            arguments = Bundle().apply {
                putInt(Const.TAB, tab)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
