package com.root37.buflexz.apps.product.ui

import android.net.Uri
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.ProductCategory
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityProductCategoryBinding
import retrofit2.Call

class ProductCategoryActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProductCategoryBinding

    override fun getLayoutView(): View {
        binding = ActivityProductCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: PagerAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.tabProductCategory.setIsChangeBold(true)
        binding.tabProductCategory.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_77f5ae))
        binding.tabProductCategory.setCustomTabView(R.layout.item_faq_category_tab, R.id.text_faq_category_tab)
        binding.tabProductCategory.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_3)) //        binding.tabLayoutCategoryPage.setDistributeEvenly(false)
        binding.tabProductCategory.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)

        loginCheck()

        mAdapter = PagerAdapter(this)
        getCategory()
    }

    fun loginCheck(){
        if (LoginInfoManager.getInstance().isMember()) {
            binding.layoutProductCategoryMember.visibility = View.VISIBLE
            Glide.with(this).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(binding.imageProductCategoryProfile)
            binding.textProductCategoryNickname.text = LoginInfoManager.getInstance().member!!.nickname
            Glide.with(this).load(Uri.parse("file:///android_asset/flags/${LoginInfoManager.getInstance().member!!.nation!!.uppercase()}.png")).into(binding.imageProductCategoryFlag)

            val nation = NationManager.getInstance().nationMap!![LoginInfoManager.getInstance().member!!.nation]
            if (nation!!.code == "KR") {
                binding.textProductCategoryNation.text = nation.name
            } else {
                binding.textProductCategoryNation.text = nation.nameEn
            }
            reloadSession()
        } else {
            binding.layoutProductCategoryMember.visibility = View.GONE
        }
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textProductCategoryRetentionPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString()))
            }
        })
    }

    private fun getCategory() {
        showProgress("")
        ApiBuilder.create().getProductCategoryList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<ProductCategory>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<ProductCategory>>>?, response: NewResultResponse<ListResultResponse<ProductCategory>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null) {
                    val list = response.result!!.list!! as MutableList
//                    val total = ProductCategory()
//                    total.title = getString(R.string.word_total)
//                    list.add(0, total)

                    mAdapter!!.mList = list
                    binding.pagerProduct.adapter = mAdapter

                    val titleList = arrayListOf<String>()
                    for (category in list) {
                        titleList.add(category.title!!)
                    }

                    binding.tabProductCategory.setViewPager(binding.pagerProduct, titleList)
                    binding.pagerProduct.currentItem = 0
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<ProductCategory>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<ProductCategory>>?) {
                hideProgress()
            }
        }).build().call()
    }

    inner class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        var mList: MutableList<ProductCategory>
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
            return ProductListFragment.newInstance(mList[position])
        }

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_luxury_shop), ToolbarOption.ToolbarMenu.LEFT)
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