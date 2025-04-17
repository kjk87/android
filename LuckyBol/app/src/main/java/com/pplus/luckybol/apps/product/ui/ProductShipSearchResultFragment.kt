package com.pplus.luckybol.apps.product.ui

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.CategoryFirstManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.signin.ui.SnsLoginActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.CategoryFirst
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentProductShipSearchResultBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call
import java.util.*

class ProductShipSearchResultFragment : BaseFragment<BaseActivity>() {

    private var _binding: FragmentProductShipSearchResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentProductShipSearchResultBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var mAdapter: PagerAdapter? = null
    var mSearch:String? = null

    override fun init() {

        binding.imageProductShipSearchResultBack.setOnClickListener {
            activity?.finish()
        }

        binding.tabLayoutProductShipSearchResult.setIsChangeBold(false)
        binding.tabLayoutProductShipSearchResult.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_232323))
        binding.tabLayoutProductShipSearchResult.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
        binding.tabLayoutProductShipSearchResult.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
        //        tabLayout_category_page.setDistributeEvenly(false)
        binding.tabLayoutProductShipSearchResult.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)

        binding.textProductShipSearchResultLikeCount.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            //            val intent = Intent(activity, GoodsLikeActivity::class.java)
            val intent = Intent(activity, ProductLikeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textProductShipSearchResultLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        loginCheck()
        setData()
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember) {
            getProductLikeCount()
            binding.textProductShipSearchResultLogin.visibility = View.GONE
            binding.textProductShipSearchResultTitle.visibility = View.VISIBLE
        } else {
            binding.textProductShipSearchResultLogin.visibility = View.VISIBLE
            binding.textProductShipSearchResultTitle.visibility = View.GONE
            binding.textProductShipSearchResultLikeCount.text = "0"
        }
    }

    fun getProductLikeCount() {
        ApiBuilder.create().countProductLike.setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                    response: NewResultResponse<Int>?) {

                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    if (response.data!! < 100) {
                        binding.textProductShipSearchResultLikeCount.text = response.data.toString()
                    } else {
                        binding.textProductShipSearchResultLikeCount.text = "99+"
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>?) {

            }
        }).build().call()
    }


    private fun setData() {

        getCategory()
    }

    private fun getCategory() {
        val categoryList = CategoryFirstManager.getInstance().categoryFirstList

        val list = arrayListOf<CategoryFirst>()

        val category = CategoryFirst()
        category.seqNo = -1L
        category.name = getString(R.string.word_md_pick)
        list.add(category)

        if (categoryList != null) {
            list.addAll(categoryList)
        }

        mAdapter = PagerAdapter(childFragmentManager)
        binding.pagerProductShipSearchResult.adapter = mAdapter
        mAdapter!!.setTitle(list)
        binding.tabLayoutProductShipSearchResult.setViewPager(binding.pagerProductShipSearchResult)
        binding.pagerProductShipSearchResult.currentItem = currentPos
    }

    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        internal var mCategoryList: MutableList<CategoryFirst>
        var fragMap: SparseArray<Fragment>
            internal set

        init {
            fragMap = SparseArray()
            mCategoryList = ArrayList()
        }

        fun setTitle(categoryList: MutableList<CategoryFirst>) {

            this.mCategoryList = categoryList
            notifyDataSetChanged()
        }

        override fun getPageTitle(position: Int): String? {
            return mCategoryList[position].name
        }

        override fun getCount(): Int {

            return mCategoryList.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
            fragMap.remove(position)
        }

        fun clear() {

            mCategoryList.clear()
            fragMap = SparseArray()
            notifyDataSetChanged()
        }

        fun getFragment(key: Int): Fragment {

            return fragMap.get(key)
        }

        override fun getItem(position: Int): Fragment {

            val fragment = ProductShipListFragment.newInstance(mCategoryList[position])
            fragMap.put(position, fragment)
            return fragment
        }
    }

    var currentPos = 0

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mSearch = it.getString(Const.DATA)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(search: String) = ProductShipSearchResultFragment().apply {
            arguments = Bundle().apply {
                putString(Const.DATA, search)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
