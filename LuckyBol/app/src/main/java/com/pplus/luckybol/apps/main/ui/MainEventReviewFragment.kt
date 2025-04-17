package com.pplus.luckybol.apps.main.ui


import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.ui.EventReviewFragment
import com.pplus.luckybol.apps.event.ui.EventWinFragment
import com.pplus.luckybol.apps.event.ui.MyEventReviewActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentMainEventReviewBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

class MainEventReviewFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            mTab = it.getString(Const.TAB)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainEventReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainEventReviewBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mAdapter: PagerAdapter? = null

    override fun init() {

        binding.pagerMainEventReview.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.layoutMainEventReviewImpressionTab.isSelected = true
                        binding.layoutMainEventReviewReviewTab.isSelected = false
                    }
                    1 -> {
                        binding.layoutMainEventReviewImpressionTab.isSelected = false
                        binding.layoutMainEventReviewReviewTab.isSelected = true
                    }
                }
            }
        })

        if (LoginInfoManager.getInstance().isMember) {
            binding.textMainEventReviewEnableReviewCount.setOnClickListener {
                val intent = Intent(activity, MyEventReviewActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivityForResult(intent, Const.REQ_REVIEW)
            }
            getMyWinCount()
        } else {
            binding.textMainEventReviewEnableReviewCount.visibility = View.GONE
        }

        binding.layoutMainEventReviewImpressionTab.setOnClickListener {
            binding.layoutMainEventReviewImpressionTab.isSelected = true
            binding.layoutMainEventReviewReviewTab.isSelected = false
            binding.pagerMainEventReview.currentItem = 0
        }

        binding.layoutMainEventReviewReviewTab.setOnClickListener {
            binding.layoutMainEventReviewImpressionTab.isSelected = false
            binding.layoutMainEventReviewReviewTab.isSelected = true
            binding.pagerMainEventReview.currentItem = 1
        }

        initPager()

    }

    private fun getMyWinCount() {
        ApiBuilder.create().myWinCountOnlyPresent.setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                    response: NewResultResponse<Int>?) {
                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    if (response.data!! > 0) {
                        binding.textMainEventReviewEnableReviewCount.visibility = View.VISIBLE
                        binding.textMainEventReviewEnableReviewCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_enable_reg_review, response.data.toString()))
                    } else {
                        binding.textMainEventReviewEnableReviewCount.visibility = View.GONE
                    }
                } else {
                    binding.textMainEventReviewEnableReviewCount.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>?) {
                binding.textMainEventReviewEnableReviewCount.visibility = View.GONE
            }
        }).build().call()
    }

    private fun initPager() {

        //        mAdapter!!.size = 2
        //        mAdapter!!.notifyDataSetChanged()

        mAdapter = PagerAdapter(childFragmentManager)
        binding.pagerMainEventReview.adapter = mAdapter
        binding.pagerMainEventReview.offscreenPageLimit = 2
        binding.pagerMainEventReview.currentItem = 0

        binding.layoutMainEventReviewImpressionTab.isSelected = true
        binding.layoutMainEventReviewReviewTab.isSelected = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_SIGN_IN, Const.REQ_SET_PROFILE -> {

            }
            Const.REQ_REVIEW -> {
                getMyWinCount()
            }
        }
        if (LoginInfoManager.getInstance().isMember) {
            getMyWinCount()
        } else {
            binding.textMainEventReviewEnableReviewCount.visibility = View.GONE
        }
    }

    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        var fragMap: SparseArray<Fragment>
            internal set

        private val titles = arrayOf(getString(R.string.word_win_impression), getString(R.string.word_win_impression2))

        init {
            fragMap = SparseArray()
        }

        override fun getPageTitle(position: Int): String? {

            return titles[position]
        }

        override fun getCount(): Int {
            return titles.size
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
                    return EventWinFragment.newInstance()
                }
                else -> {
                    return EventReviewFragment.newInstance()
                }
            }
        }
    }

    override fun getPID(): String {
        return "Home_ Winning Review"
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainEventReviewFragment().apply {
            arguments = Bundle().apply {
                //                        putString(Const.TAB, type)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
