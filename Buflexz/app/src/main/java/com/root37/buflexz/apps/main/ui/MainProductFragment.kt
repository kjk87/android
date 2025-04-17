package com.root37.buflexz.apps.main.ui

import android.content.Intent
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.apps.common.ui.base.BaseFragment
import com.root37.buflexz.apps.login.LoginActivity
import com.root37.buflexz.apps.product.ui.ProductListFragment
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.ProductCategory
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.FragmentMainProductBinding
import retrofit2.Call

/**
 * A simple [Fragment] subclass.
 * Use the [MainProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainProductFragment : BaseFragment<MainActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var _binding: FragmentMainProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainProductBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: PagerAdapter? = null

    override fun init() {
        binding.tabMainProduct.setIsChangeBold(true)
        binding.tabMainProduct.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_77f5ae))
        binding.tabMainProduct.setCustomTabView(R.layout.item_faq_category_tab, R.id.text_faq_category_tab)
        binding.tabMainProduct.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_3)) //        binding.tabLayoutCategoryPage.setDistributeEvenly(false)
        binding.tabMainProduct.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)

        loginCheck()

        mAdapter = PagerAdapter(requireActivity())
        getCategory()
    }

    fun loginCheck(){
        if (LoginInfoManager.getInstance().isMember()) {
            binding.layoutMainProductMember.visibility = View.VISIBLE
            binding.layoutMainProductNoneMember.visibility = View.GONE
            Glide.with(this).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(binding.imageMainProductProfile)
            binding.textMainProductNickname.text = LoginInfoManager.getInstance().member!!.nickname
            Glide.with(this).load(Uri.parse("file:///android_asset/flags/${LoginInfoManager.getInstance().member!!.nation!!.uppercase()}.png")).into(binding.imageMainProductFlag)

            val nation = NationManager.getInstance().nationMap!![LoginInfoManager.getInstance().member!!.nation]
            if (nation!!.code == "KR") {
                binding.textMainProductNation.text = nation.name
            } else {
                binding.textMainProductNation.text = nation.nameEn
            }
            reloadSession()
        } else {
            binding.layoutMainProductMember.visibility = View.GONE
            binding.layoutMainProductNoneMember.visibility = View.VISIBLE
            binding.textMainProductLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                loginLauncher.launch(intent)
            }
        }
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textMainProductRetentionPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString()))
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
                    val total = ProductCategory()
                    total.title = getString(R.string.word_total)
                    list.add(0, total)

                    mAdapter!!.mList = list
                    binding.pagerProduct.adapter = mAdapter

                    val titleList = arrayListOf<String>()
                    for (category in list) {
                        titleList.add(category.title!!)
                    }

                    binding.tabMainProduct.setViewPager(binding.pagerProduct, titleList)
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

    val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainProductFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}