package com.pplus.prnumberuser.apps.main.ui

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.category.data.CategoryAdapter
import com.pplus.prnumberuser.apps.common.mgmt.DeliveryAddressManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.delivery.ui.DeliveryAddressSetActivity
import com.pplus.prnumberuser.apps.page.ui.CategoryMinorDeliveryPageFragment
import com.pplus.prnumberuser.apps.search.ui.SearchDeliveryActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.CategoryMinor
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.FragmentMainDeliveryBinding
import retrofit2.Call
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainDeliveryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainDeliveryFragment : BaseFragment<BaseActivity>() {

    override fun getPID(): String? {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainDeliveryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainDeliveryBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var mCategoryAdapter:CategoryAdapter? = null
    var mAdapter: PagerAdapter? = null

    override fun init() {

        binding.recyclerMainDeliveryCategory.layoutManager = GridLayoutManager(requireActivity(), 4)
        mCategoryAdapter = CategoryAdapter()
        binding.recyclerMainDeliveryCategory.adapter = mCategoryAdapter

        binding.tabLayoutMainDeliveryCategory.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_579ffb))
        binding.tabLayoutMainDeliveryCategory.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
        binding.tabLayoutMainDeliveryCategory.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
        //        tabLayout_category_page.setDistributeEvenly(false)
        binding.tabLayoutMainDeliveryCategory.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_80), 0)

        binding.textMainDeliveryAddress.setOnClickListener {
            val intent = Intent(activity, DeliveryAddressSetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            locationSelectLauncher.launch(intent)
        }

        binding.imageMainDeliverySearch.setOnClickListener {
            val intent = Intent(activity, SearchDeliveryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        mCategoryAdapter!!.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                binding.appBarPage.setExpanded(false, true)
                currentPos = position
                binding.pagerMainDeliveryPage.currentItem = currentPos
            }
        })

        checkAddress()
    }

    private fun checkAddress(){
        val deliveryAddressList = DeliveryAddressManager.getInstance().deliveryAddressList
        if(deliveryAddressList != null && deliveryAddressList.isNotEmpty()){
            val pre = deliveryAddressList[0].address!!.split(" ")[0]
            binding.textMainDeliveryAddress.text = deliveryAddressList[0].address!!.replace("$pre ", "")
            getCategory()
        }else{
            val intent = Intent(activity, DeliveryAddressSetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            locationSelectLauncher.launch(intent)
        }
    }

    val locationSelectLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        currentPos = binding.pagerMainDeliveryPage.currentItem
        checkAddress()
    }

    var currentPos = 0

    private fun getCategory() {

        val params = HashMap<String, String>()
        params["major"] = "8"
        showProgress("")
        ApiBuilder.create().getCategoryMinorList(params).setCallback(object : PplusCallback<NewResultResponse<CategoryMinor>> {
            override fun onResponse(call: Call<NewResultResponse<CategoryMinor>>?, response: NewResultResponse<CategoryMinor>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
                val categoryList = response!!.datas
                val list = arrayListOf<CategoryMinor>()
                val titleList = arrayListOf<String>()

                list.add(CategoryMinor(-1L, 8, getString(R.string.word_total)))

                if (categoryList != null) {
                    list.addAll(categoryList)
                }

                for(category in list){
                    titleList.add(category.name!!)
                }

                mCategoryAdapter!!.setDataList(list)
                mAdapter = PagerAdapter(requireActivity())
                binding.pagerMainDeliveryPage.adapter = mAdapter
                mAdapter!!.setItemList(list)
                binding.tabLayoutMainDeliveryCategory.setViewPager(binding.pagerMainDeliveryPage, titleList)
                binding.pagerMainDeliveryPage.currentItem = currentPos

            }

            override fun onFailure(call: Call<NewResultResponse<CategoryMinor>>?, t: Throwable?, response: NewResultResponse<CategoryMinor>?) {
                hideProgress()
            }
        }).build().call()
    }

    inner class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        internal var mCategoryList: MutableList<CategoryMinor>
        var fragMap: SparseArray<Fragment>

        init {
            fragMap = SparseArray()
            mCategoryList = ArrayList<CategoryMinor>()
        }

        override fun getItemCount(): Int {
            return mCategoryList.size
        }

        fun setItemList(categoryList: MutableList<CategoryMinor>) {

            this.mCategoryList = categoryList
            notifyDataSetChanged()
        }

        override fun createFragment(position: Int): Fragment {
            val fragment = CategoryMinorDeliveryPageFragment.newInstance(mCategoryList[position])
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

    companion object {
        @JvmStatic
        fun newInstance() = MainDeliveryFragment().apply {
            arguments = Bundle().apply {
//                putString(ARG_PARAM1, param1)
//                putString(ARG_PARAM2, param2)
            }
        }
    }
}