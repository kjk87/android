package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityMyEventReviewBinding

class MyEventReviewActivity : BaseActivity() {

    private lateinit var binding: ActivityMyEventReviewBinding

    override fun getLayoutView(): View {
        binding = ActivityMyEventReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mAdapter: PagerAdapter? = null

    fun getBinding() : ActivityMyEventReviewBinding{
        return binding
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.imageMyEventReviewBack.setOnClickListener {
            onBackPressed()
        }

        mAdapter = PagerAdapter(supportFragmentManager)
        binding.pagerMyEventReview.adapter = mAdapter
        binding.pagerMyEventReview.offscreenPageLimit = 2
        binding.pagerMyEventReview.currentItem = 0

        binding.pagerMyEventReview.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.textMyEventReviewWinTab.isSelected = true
                        binding.textMyEventReviewReviewTab.isSelected = false
                        binding.pagerMyEventReview.currentItem = 0
                    }
                    1 -> {
                        binding.textMyEventReviewWinTab.isSelected = false
                        binding.textMyEventReviewReviewTab.isSelected = true
                        binding.pagerMyEventReview.currentItem = 1
                    }
                }
            }
        })



        binding.textMyEventReviewWinTab.setOnClickListener {
            binding.textMyEventReviewWinTab.isSelected = true
            binding.textMyEventReviewReviewTab.isSelected = false
            binding.pagerMyEventReview.currentItem = 0
        }

        binding.textMyEventReviewReviewTab.setOnClickListener {
            binding.textMyEventReviewWinTab.isSelected = false
            binding.textMyEventReviewReviewTab.isSelected = true
            binding.pagerMyEventReview.currentItem = 1
        }

        binding.textMyEventReviewWinTab.isSelected = true
        binding.textMyEventReviewReviewTab.isSelected = false
        binding.pagerMyEventReview.currentItem = 0
    }

    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        var fragMap: SparseArray<Fragment>
            internal set

        private val titles = arrayOf(getString(R.string.word_enable_review), getString(R.string.word_registed_review))

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
                    return MyEventWinFragment.newInstance()
                }
                else -> {
                    return MyEventReviewFragment.newInstance()
                }
            }
        }
    }
}