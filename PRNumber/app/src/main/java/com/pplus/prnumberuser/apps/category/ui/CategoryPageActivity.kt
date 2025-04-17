//package com.pplus.prnumberuser.apps.category.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.util.SparseArray
//import android.view.View
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.BusProviderData
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.ui.LocationSelectActivity
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.CategoryMajor
//import com.pplus.prnumberuser.core.network.model.dto.CategoryMinor
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.databinding.ActivityCategoryPageBinding
//import com.pplus.utils.BusProvider
//import com.pplus.utils.part.utils.StringUtils
//import retrofit2.Call
//import java.util.*
//
//class CategoryPageActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        val category = intent.getParcelableExtra<CategoryMajor>(Const.DATA)
//        return "Main_category_${category!!.seqNo}"
//    }
//
//    private lateinit var binding : ActivityCategoryPageBinding
//
//    override fun getLayoutView(): View {
//        binding = ActivityCategoryPageBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    var categoryMajor: CategoryMajor? = null
//    var mAdapter: PagerAdapter? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        categoryMajor = intent.getParcelableExtra(Const.DATA)
//
//        setTitle(categoryMajor!!.name)
//
//        binding.tabLayoutCategoryPage.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_232323))
//        binding.tabLayoutCategoryPage.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
//        binding.tabLayoutCategoryPage.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
////        tabLayout_category_page.setDistributeEvenly(false)
//        binding.tabLayoutCategoryPage.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_80), 0)
//
//
//        binding.textCategoryPageAddress.setSingleLine()
//        binding.textCategoryPageAddress.setOnClickListener {
//            val intent = Intent(this, LocationSelectActivity::class.java)
//            val location = IntArray(2)
//            it.getLocationOnScreen(location)
//            val x = location[0] + it.width / 2
//            val y = location[1] + it.height / 2
//            intent.putExtra(Const.X, x)
//            intent.putExtra(Const.Y, y)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            locationSelectLauncher.launch(intent)
//        }
//
//        setData()
//
//    }
//
//    private fun setData() {
//        var locationData = LocationUtil.specifyLocationData
//        if (locationData != null) {
//            if (StringUtils.isEmpty(locationData.address)) {
//                PplusCommonUtil.callAddress(locationData, object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                    override fun onResult(address: String) {
//
//                        locationData = LocationUtil.specifyLocationData
//                        binding.textCategoryPageAddress.text = locationData!!.address
//
//                    }
//                })
//            } else {
//                binding.textCategoryPageAddress.text = locationData!!.address
//            }
//
//            getCategory()
//        }
//    }
//
//    private fun getCategory() {
//
//        val params = HashMap<String, String>()
//        params["major"] = categoryMajor!!.seqNo.toString()
//        showProgress("")
//        ApiBuilder.create().getCategoryMinorList(params).setCallback(object : PplusCallback<NewResultResponse<CategoryMinor>> {
//            override fun onResponse(call: Call<NewResultResponse<CategoryMinor>>?, response: NewResultResponse<CategoryMinor>?) {
//                hideProgress()
//                val categoryList = response!!.datas
//
//                val list = arrayListOf<CategoryMinor>()
//                val titleList = arrayListOf<String>()
//
//                list.add(CategoryMinor(-1L, categoryMajor!!.seqNo, getString(R.string.word_total)))
//
//                if (categoryList != null) {
//                    list.addAll(categoryList)
//                }
//
//                for(category in list){
//                    titleList.add(category.name!!)
//                }
//
//                mAdapter = PagerAdapter(this@CategoryPageActivity)
//                binding.pagerCategoryPage.adapter = mAdapter
//                mAdapter!!.setItemList(list)
//                binding.tabLayoutCategoryPage.setViewPager(binding.pagerCategoryPage, titleList)
//                binding.pagerCategoryPage.currentItem = currentPos
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<CategoryMinor>>?, t: Throwable?, response: NewResultResponse<CategoryMinor>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    inner class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
//
//        internal var mCategoryList: MutableList<CategoryMinor>
//        var fragMap: SparseArray<Fragment>
//            internal set
//
//        init {
//            fragMap = SparseArray()
//            mCategoryList = ArrayList<CategoryMinor>()
//        }
//
//        override fun getItemCount(): Int {
//            return mCategoryList.size
//        }
//
//        fun setItemList(categoryList: MutableList<CategoryMinor>) {
//
//            this.mCategoryList = categoryList
//            notifyDataSetChanged()
//        }
//
//        override fun createFragment(position: Int): Fragment {
//            val fragment = CategoryMinorPageFragment.newInstance(mCategoryList[position])
//            fragMap.put(position, fragment)
//            return fragment
//        }
//
//        fun clear() {
//
//            mCategoryList.clear()
//            fragMap = SparseArray()
//            notifyDataSetChanged()
//        }
//
//        fun getFragment(key: Int): Fragment {
//
//            return fragMap.get(key)
//        }
//    }
//
//    val locationSelectLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data = result.data
//            currentPos = binding.pagerCategoryPage.currentItem
//            setData()
//        }
//    }
//
//    var currentPos = 0
//    var mPage: Page? = null
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_CHROME -> {
//                if (mPage != null) {
//                    val no = mPage!!.no
//                    mPage = null
//                    val params = HashMap<String, String>()
//                    params["no"] = no!!.toString()
//                    showProgress("")
//                    ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
//                        override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
//                            hideProgress()
//                            val bus = BusProviderData()
//                            bus.subData = response.data
//                            bus.type = BusProviderData.BUS_MAIN
//                            BusProvider.getInstance().post(bus)
//                        }
//
//                        override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
//                            hideProgress()
//                        }
//                    }).build().call()
//                }
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_category), ToolbarOption.ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}
