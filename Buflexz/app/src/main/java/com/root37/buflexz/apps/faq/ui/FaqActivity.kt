package com.root37.buflexz.apps.faq.ui

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.FaqCategory
import com.root37.buflexz.core.network.model.dto.ProductCategory
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.databinding.ActivityFaqBinding
import retrofit2.Call

class FaqActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityFaqBinding

    override fun getLayoutView(): View {
        binding = ActivityFaqBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: PagerAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.tabFaqCategory.setIsChangeBold(true)
        binding.tabFaqCategory.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_77f5ae))
        binding.tabFaqCategory.setCustomTabView(R.layout.item_faq_category_tab, R.id.text_faq_category_tab)
        binding.tabFaqCategory.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_3)) //        binding.tabLayoutCategoryPage.setDistributeEvenly(false)
        binding.tabFaqCategory.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)

        mAdapter = PagerAdapter(this)
        getFaqCategory()
    }

    private fun getFaqCategory() {
        showProgress("")
        ApiBuilder.create().getFaqCategoryList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<FaqCategory>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<FaqCategory>>>?, response: NewResultResponse<ListResultResponse<FaqCategory>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null) {
                    val list = response.result!!.list!! as MutableList
                    val total = FaqCategory()
                    total.name = getString(R.string.word_total)
                    list.add(0, total)

                    mAdapter!!.mList = list
                    binding.pagerFaq.adapter = mAdapter

                    val titleList = arrayListOf<String>()
                    for (category in list) {
                        titleList.add(category.name!!)
                    }

                    binding.tabFaqCategory.setViewPager(binding.pagerFaq, titleList)
                    binding.pagerFaq.currentItem = 0
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<FaqCategory>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<FaqCategory>>?) {
                hideProgress()
            }
        }).build().call()
    }

    inner class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        var mList: MutableList<FaqCategory>
        var fragMap: SparseArray<Fragment>


        init {
            fragMap = SparseArray()
            mList = arrayListOf()
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        fun clear() {

            fragMap = SparseArray()
            notifyDataSetChanged()
        }

        fun getFragment(key: Int): Fragment {

            return fragMap.get(key)
        }

        override fun createFragment(position: Int): Fragment {
            return FaqFragment.newInstance(mList[position])
        }

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_faq_en), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}