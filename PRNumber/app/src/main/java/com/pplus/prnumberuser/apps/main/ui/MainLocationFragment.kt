package com.pplus.prnumberuser.apps.main.ui


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.appbar.AppBarLayout
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.page.ui.CategoryMinorVisitPageFragment
import com.pplus.prnumberuser.apps.page.ui.ServicePageFragment
import com.pplus.prnumberuser.apps.search.ui.LocationAroundPageActivity
import com.pplus.prnumberuser.apps.search.ui.SearchActivity
import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.CategoryMinor
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMainLocationBinding
import com.pplus.utils.BusProvider
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap

class MainLocationFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BusProvider.getInstance().register(this)
        arguments?.let { //            mTab = it.getString(Const.TAB)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BusProvider.getInstance().unregister(this)
    }

    private var _binding: FragmentMainLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainLocationBinding.inflate(inflater, container, false)
        val view = binding.root

        val intentFilter = IntentFilter()
        intentFilter.addAction(requireActivity().packageName + ".sigIn")
        requireActivity().registerReceiver(mSignInReceiver, intentFilter)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(mSignInReceiver)
        _binding = null
    }

    val mSignInReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            LogUtil.e(LOG_TAG, "receive : " + requireActivity().packageName + ".sigIn")
            checkSignIn()
        }
    }

    private var mTotalCount: Int = 0
    private var mLockListView = false
//    private var mPaging = 1
    private var mAdapter: PagerAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
    private var mLocationData: LocationData? = null


    override fun init() {
        binding.appBarMainLocation.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (!isAdded) {
                    return
                }

                if (verticalOffset <= -binding.collapsingMainLocation.height + binding.toolbarMainLocation.height) {
                    binding.toolbarMainLocation.visibility = View.VISIBLE
                    binding.layoutMainLocationCash.visibility = View.GONE
                } else {
                    binding.toolbarMainLocation.visibility = View.GONE
                    binding.layoutMainLocationCash.visibility = View.VISIBLE
                }
            }
        })

        getParentActivity().mLocationListener = object : BaseActivity.LocationListener {
            override fun onLocation(result: ActivityResult) {
                PplusCommonUtil.alertLocation(getParentActivity(), false, object : PplusCommonUtil.Companion.SuccessLocationListener {
                    override fun onSuccess() {
                        if (!isAdded) {
                            return
                        }
                        PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {

                            override fun onResult(address: String) {

                                if (!isAdded) {
                                    return
                                }

                                binding.textMainLocationAddress.text = address
                            }
                        })

                        mLocationData = LocationUtil.specifyLocationData
                        currentPos = binding.pagerMainLocationPage.currentItem
                        getCategory()
                    }
                })
            }
        }

        binding.imageMainLocationSearch.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMainLocationSearch2.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textMainLocationSearch.setOnClickListener {
            PplusCommonUtil.alertLocation(getParentActivity(), true, object : PplusCommonUtil.Companion.SuccessLocationListener {
                override fun onSuccess() {
                    PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {

                        override fun onResult(address: String) {

                            if (!isAdded) {
                                return
                            }

                            binding.textMainLocationAddress.text = address
                        }
                    })

                    mLocationData = LocationUtil.specifyLocationData
                    getCategory()
                }
            }) //            val intent = Intent(activity, LocationAroundPageActivity::class.java)
            //            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            //            startActivity(intent)
        }

        binding.imageMainLocationViewMap.setOnClickListener {
            val intent = Intent(activity, LocationAroundPageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.imageMainLocationViewMap2.setOnClickListener {
            val intent = Intent(activity, LocationAroundPageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textMainLocationAddress.setSingleLine()
        binding.textMainLocationAddress.setOnClickListener {
            val intent = Intent(activity, LocationSelectActivity::class.java)
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            val x = location[0] + it.width / 2
            val y = location[1] + it.height / 2
            intent.putExtra(Const.X, x)
            intent.putExtra(Const.Y, y)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            getParentActivity().locationLauncher.launch(intent)
        }

        binding.tabLayoutMainLocationCategory.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_4694fb))
        binding.tabLayoutMainLocationCategory.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
        binding.tabLayoutMainLocationCategory.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
        //        tabLayout_category_page.setDistributeEvenly(false)
        binding.tabLayoutMainLocationCategory.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_80), 0)

        binding.tabLayoutMainLocationCategory.setNormalListener {
            binding.tabLayoutMainLocationCategory.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_4694fb))
            binding.layoutMainLocationServiceTab.isSelected = false
            binding.textMainLocationServiceTab.typeface = Typeface.create(binding.textMainLocationServiceTab.getTypeface(), Typeface.NORMAL)
            binding.pagerMainLocationPage.visibility = View.VISIBLE
            binding.servicePageContainer.visibility = View.GONE
        }

        checkSignIn()

        binding.textMainLocationPoint.setOnClickListener {
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textMainLocationPoint2.setOnClickListener {
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textMainLocationLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        binding.textMainLocationLogin2.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        //        val pagerAdapter = MainLocationPagerAdapter(requireActivity())
        //        pager_main_location.adapter = pagerAdapter
        //        indicator_main_location.removeAllViews()
        //        indicator_main_location.build(LinearLayout.HORIZONTAL, pagerAdapter.count)
        //        pager_main_location.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
        //
        //            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //
        //            }
        //
        //            override fun onPageSelected(position: Int) {
        //
        //                indicator_main_location.setCurrentItem(position)
        //            }
        //
        //            override fun onPageScrollStateChanged(state: Int) {
        //
        //            }
        //        })

        //        image_main_location_banner.setOnClickListener {
        //            val intent = Intent(activity, ThemeActivity::class.java)
        //            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //            startActivity(intent)
        //        }



        PplusCommonUtil.alertLocation(getParentActivity(), false, object : PplusCommonUtil.Companion.SuccessLocationListener {
            override fun onSuccess() {
                if (!isAdded) {
                    return
                }

                PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {

                    override fun onResult(address: String) {

                        if (!isAdded) {
                            return
                        }

                        binding.textMainLocationAddress.text = address
                    }
                })
                mLocationData = LocationUtil.specifyLocationData
                getCategory()
            }
        })

    }

    var currentPos = 0

    private fun locationLog(){
        val params = HashMap<String, String>()
        params["deviceId"] = PplusCommonUtil.getDeviceID()
        params["platform"] = "aos"
        params["serviceLog"] = "내주변 매장방문 업체 목록제공을 위한 현재위치조회"
        ApiBuilder.create().locationServiceLogSave(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {

            }
        }).build().call()
    }

    private fun getCategory() {

        locationLog()

        val params = HashMap<String, String>()
        params["major"] = "8"
        showProgress("")
        ApiBuilder.create().getCategoryMinorList(params).setCallback(object : PplusCallback<NewResultResponse<CategoryMinor>> {
            override fun onResponse(call: Call<NewResultResponse<CategoryMinor>>?, response: NewResultResponse<CategoryMinor>?) {
                hideProgress()
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

                mAdapter = PagerAdapter(requireActivity())
                binding.pagerMainLocationPage.adapter = mAdapter
                mAdapter!!.setItemList(list)
                binding.tabLayoutMainLocationCategory.setViewPager(binding.pagerMainLocationPage, titleList)
                binding.pagerMainLocationPage.currentItem = currentPos

                binding.layoutMainLocationServiceTab.setOnClickListener {
                    binding.tabLayoutMainLocationCategory.setUnselected()
                    binding.tabLayoutMainLocationCategory.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), android.R.color.transparent))
                    binding.layoutMainLocationServiceTab.isSelected = true
                    binding.textMainLocationServiceTab.setTypeface(binding.textMainLocationServiceTab.typeface, Typeface.BOLD)
                    binding.pagerMainLocationPage.visibility = View.GONE
                    binding.servicePageContainer.visibility = View.VISIBLE

                    val ft = childFragmentManager.beginTransaction()
                    ft.replace(R.id.service_page_container, ServicePageFragment.newInstance(), ServicePageFragment::class.java.simpleName)
                    ft.commit()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<CategoryMinor>>?, t: Throwable?, response: NewResultResponse<CategoryMinor>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                }
                binding.textMainLocationPoint.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())
                binding.textMainLocationPoint2.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())
            }
        })
    }


    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        checkSignIn()
    }

    private fun checkSignIn() {
        if (LoginInfoManager.getInstance().isMember) {
            binding.textMainLocationPoint.visibility = View.VISIBLE
            binding.textMainLocationPoint2.visibility = View.VISIBLE
            binding.textMainLocationPoint2Title.visibility = View.VISIBLE
            binding.textMainLocationLogin.visibility = View.GONE
            binding.textMainLocationLogin2.visibility = View.GONE

            setRetentionBol()
        } else {
            binding.textMainLocationPoint.visibility = View.GONE
            binding.textMainLocationPoint2.visibility = View.GONE
            binding.textMainLocationPoint2Title.visibility = View.GONE
            binding.textMainLocationLogin.visibility = View.VISIBLE
            binding.textMainLocationLogin2.visibility = View.VISIBLE
        }
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
            val fragment = CategoryMinorVisitPageFragment.newInstance(mCategoryList[position])
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

    override fun getPID(): String {
        return "Main page list"

    }

    companion object {

        @JvmStatic
        fun newInstance() = MainLocationFragment().apply {
            arguments = Bundle().apply { //                        putString(Const.TAB, type)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
