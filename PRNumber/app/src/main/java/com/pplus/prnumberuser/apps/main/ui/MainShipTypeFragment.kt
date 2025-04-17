package com.pplus.prnumberuser.apps.main.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.CategoryFirstManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.product.ui.ProductLikeActivity
import com.pplus.prnumberuser.apps.product.ui.ProductShipSearchResultActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.CategoryFirst
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.FragmentMainShipTypeBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class MainShipTypeFragment : BaseFragment<BaseActivity>() {


    var mAdapter: PagerAdapter? = null

    val productLikeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (childFragmentManager.fragments != null) {
                for (fragment in childFragmentManager.fragments) {
                    if (fragment is ProductShipListFragment) {
                        fragment.setData()
                    }
                }
            }
            getProductLikeCount()
        }
    }

    private var _binding: FragmentMainShipTypeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainShipTypeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        binding.imageMainShipTypeBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tabLayoutShipType.setIsChangeBold(false)
        binding.tabLayoutShipType.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_232323))
        binding.tabLayoutShipType.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
        binding.tabLayoutShipType.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8)) //        tabLayout_category_page.setDistributeEvenly(false)
        binding.tabLayoutShipType.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)

        binding.textMainShipTypeLikeCount.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, ProductLikeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            productLikeLauncher.launch(intent)
        }

        //        text_main_ship_type_login.setOnClickListener {
        //            val intent = Intent(activity, SnsLoginActivity::class.java)
        //            activity?.startActivityForResult(intent, Const.REQ_SIGN_IN)
        //        }

        //        text_ship_type_offline.setOnClickListener {
        //            val intent = Intent(activity, HotDealActivity::class.java)
        //            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //            activity?.startActivityForResult(intent, Const.REQ_SIGN_IN)
        //        }

        binding.imageMainShipTypeSearch2.setOnClickListener {
            search()
        }

        binding.editMainShipTypeSearch.setOnEditorActionListener { textView, i, keyEvent ->

            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }

            true
        }

        binding.editMainShipTypeSearch.setSingleLine()

        checkSignIn()
        setData()
    }

    private fun checkSignIn() {
        if (LoginInfoManager.getInstance().isMember) {
            getProductLikeCount() //            text_main_ship_type_login.visibility = View.GONE
            //            text_main_ship_type_title.visibility = View.VISIBLE
        } else { //            text_main_ship_type_login.visibility = View.VISIBLE
            //            text_main_ship_type_title.visibility = View.GONE
            binding.textMainShipTypeLikeCount.text = "0"
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    private fun search() {
        val search = binding.editMainShipTypeSearch.text.toString().trim()
        if (StringUtils.isEmpty(search)) {
            ToastUtil.show(activity, R.string.msg_input_search_word)
            return
        }
        val intent = Intent(activity, ProductShipSearchResultActivity::class.java)
        intent.putExtra(Const.DATA, search)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    fun getProductLikeCount() {
        ApiBuilder.create().countProductLike.setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {

                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    if (response.data < 100) {
                        binding.textMainShipTypeLikeCount.text = response.data.toString()
                    } else {
                        binding.textMainShipTypeLikeCount.text = "99+"
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {

            }
        }).build().call()
    }

    private fun setData() {

        getCategory()
    }

    private fun getCategory() {
        val categoryList = CategoryFirstManager.getInstance().categoryFirstList

        val list = arrayListOf<CategoryFirst>()
        val titleList = arrayListOf<String>()

        val category = CategoryFirst()
        category.seqNo = -1L
        category.name = getString(R.string.word_md_pick)
        list.add(category)

        if (categoryList != null) {
            list.addAll(categoryList)
        }

        for (category in list) {
            titleList.add(category.name!!)
        }

        mAdapter = PagerAdapter(requireActivity())
        binding.pagerShipType.adapter = mAdapter
        mAdapter!!.setItemList(list)
        binding.tabLayoutShipType.setViewPager(binding.pagerShipType, titleList)
        binding.pagerShipType.currentItem = currentPos
    }

    inner class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        internal var mCategoryList: MutableList<CategoryFirst>
        var fragMap: SparseArray<Fragment>
            internal set

        init {
            fragMap = SparseArray()
            mCategoryList = ArrayList()
        }

        fun setItemList(categoryList: MutableList<CategoryFirst>) {

            this.mCategoryList = categoryList
            notifyDataSetChanged()
        }


        override fun getItemCount(): Int {
            return mCategoryList.size
        }


        override fun createFragment(position: Int): Fragment {
            val fragment = ProductShipListFragment.newInstance(mCategoryList[position])
            fragMap.put(position, fragment)
            return fragment
        }

        fun clear() {

            mCategoryList.clear()
            fragMap = SparseArray()
            notifyDataSetChanged()
        }

        fun getFragment(key: Int): Fragment {

            return fragMap.get(key)
        }
    }

    var currentPos = 0

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainShipTypeFragment().apply {
            arguments = Bundle().apply { //                        putString(ARG_PARAM1, param1)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
