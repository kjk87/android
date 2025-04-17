package com.pplus.luckybol.apps.main.ui

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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.CategoryFirstManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.ui.EventActivity
import com.pplus.luckybol.apps.event.ui.EventDetailActivity
import com.pplus.luckybol.apps.event.ui.LuckyLottoDetailActivity
import com.pplus.luckybol.apps.main.data.HomeBannerAdapter
import com.pplus.luckybol.apps.product.ui.*
import com.pplus.luckybol.apps.setting.ui.NoticeDetailActivity
import com.pplus.luckybol.core.code.common.MoveType1Code
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.FragmentMainShipTypeBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class MainShipTypeFragment : BaseFragment<BaseActivity>() {

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

    var mAdapter: PagerAdapter? = null

    override fun init() {

        binding.tabLayoutShipType.setIsChangeBold(false)
        binding.tabLayoutShipType.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_232323))
        binding.tabLayoutShipType.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
        binding.tabLayoutShipType.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
//        binding.tabLayoutCategoryPage.setDistributeEvenly(false)
        binding.tabLayoutShipType.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)

        binding.textMainShipTypeLikeCount.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), null)) {
                return@setOnClickListener
            }

//            val intent = Intent(activity, GoodsLikeActivity::class.java)
            val intent = Intent(activity, ProductLikeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
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

//        image_main_ship_type_search.setOnClickListener {
//            layout_main_ship_type_search.visibility = View.VISIBLE
//            text_main_ship_type_title.visibility = View.GONE
//            image_main_ship_type_search.visibility = View.GONE
//        }

        binding.layoutMainShipTypeSearch.visibility = View.VISIBLE
//        text_main_ship_type_title.visibility = View.GONE
//        image_main_ship_type_search.visibility = View.GONE

        binding.imageMainShipTypeBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.imageMainShipTypeSearch2.setOnClickListener {
            search()
        }

        binding.editMainShipTypeSearch.setOnEditorActionListener { textView, i, keyEvent ->

            if(i == EditorInfo.IME_ACTION_SEARCH){
                search()
            }

            true
        }

        binding.editMainShipTypeSearch.setSingleLine()



        initBanner()

        loginCheck()
        setData()
    }


    lateinit var mBannerAdapter: HomeBannerAdapter

    private fun initBanner(){
        mBannerAdapter = HomeBannerAdapter()

        binding.pagerMainShipTypeBanner.adapter = mBannerAdapter

        mBannerAdapter.listener = object : HomeBannerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val item = mBannerAdapter.getItem(position)

                when (item.moveType) {
                    MoveType1Code.inner.name -> {
                        when (item.innerType) {
                            "online" -> {
                                val productPrice = ProductPrice()
                                productPrice.seqNo = item.moveTarget!!.toLong()

                                val intent = Intent(requireActivity(), ProductShipDetailActivity::class.java)
                                intent.putExtra(Const.DATA, productPrice)
                                signInLauncher.launch(intent)
                            }
                            "eventA" -> {
                                val intent = Intent(requireActivity(), EventActivity::class.java)
                                intent.putExtra(Const.GROUP, 1)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                signInLauncher.launch(intent)
                            }
                            "eventB" -> {
                                val intent = Intent(requireActivity(), EventActivity::class.java)
                                intent.putExtra(Const.GROUP, 2)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                signInLauncher.launch(intent)
                            }
                            "event" -> {
                                val event = Event()
                                event.no = item.moveTarget!!.toLong()
                                val intent = Intent(requireActivity(), EventDetailActivity::class.java)
                                intent.putExtra(Const.DATA, event)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                signInLauncher.launch(intent)
                            }
                            "lotto" -> {
                                val event = Event()
                                event.no = item.moveTarget!!.toLong()
                                val intent = Intent(requireActivity(), LuckyLottoDetailActivity::class.java)
                                intent.putExtra(Const.DATA, event)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                signInLauncher.launch(intent)
                            }
                            "notice" -> {
                                val notice = Notice()
                                notice.no = item.moveTarget!!.toLong()
                                val intent = Intent(requireActivity(), NoticeDetailActivity::class.java)
                                intent.putExtra(Const.NOTICE, notice)
                                signInLauncher.launch(intent)
                            }
                            "shoppingGroup"->{
                                val shoppingGroup = ShoppingGroup()
                                shoppingGroup.seqNo = item.moveTarget!!.toLong()
                                val intent = Intent(requireActivity(), ShoppingGroupProductActivity::class.java)
                                intent.putExtra(Const.DATA, shoppingGroup)
                                signInLauncher.launch(intent)
                            }
                        }

                    }
                    MoveType1Code.outer.name -> {
                        PplusCommonUtil.openChromeWebView(requireActivity(), item.moveTarget!!)
                    }
                }
            }
        }

        getShoppingBanner()
    }

    private fun getShoppingBanner() {
        val params = HashMap<String, String>()
        params["platform"] = "aos"
        params["type"] = "shopping"
        ApiBuilder.create().getBannerList(params).setCallback(object : PplusCallback<NewResultResponse<Banner>> {

            override fun onResponse(call: Call<NewResultResponse<Banner>>?,
                                    response: NewResultResponse<Banner>?) {
                if (response?.datas != null) {
                    if (!isAdded) {
                        return
                    }

                    val bannerList = response.datas!!

                    if (bannerList.isEmpty()) {
                        binding.layoutMainShipTypeBanner.visibility = View.GONE
                    } else {
                        binding.layoutMainShipTypeBanner.visibility = View.VISIBLE
                        binding.textMainShipTypeBannerPage.text = "1/${bannerList.size}"
                        binding.pagerMainShipTypeBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                binding.textMainShipTypeBannerPage.text = "${position + 1}/${bannerList.size}"
                            }
                        })
                        mBannerAdapter.setDataList(bannerList as MutableList<Banner>)
                        binding.textMainShipTypeBannerPage.text = "${binding.pagerMainShipTypeBanner.currentItem + 1}/${bannerList.size}"
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Banner>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Banner>?) {

            }
        }).build().call()
    }

    private fun search(){
        val search = binding.editMainShipTypeSearch.text.toString().trim()
        if(StringUtils.isEmpty(search)){
            ToastUtil.show(activity, R.string.msg_input_search_word)
            return
        }
        val intent = Intent(activity, ProductShipSearchResultActivity::class.java)
        intent.putExtra(Const.DATA, search)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    private fun loginCheck(){
        if (LoginInfoManager.getInstance().isMember) {
            getProductLikeCount()
        }else{
            binding.textMainShipTypeLikeCount.text = "0"
        }
    }

    fun getProductLikeCount() {
        ApiBuilder.create().countProductLike.setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {

                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    if(response.data!! < 100){
                        binding.textMainShipTypeLikeCount.text = response.data.toString()
                    }else{
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

        val category = CategoryFirst()
        category.seqNo = -1L
        category.name = getString(R.string.word_md_pick)
        list.add(category)

        if (categoryList != null) {
            list.addAll(categoryList)
        }

        mAdapter = PagerAdapter(childFragmentManager)
        binding.pagerShipType.adapter = mAdapter
        mAdapter!!.setTitle(list)
        binding.tabLayoutShipType.setViewPager(binding.pagerShipType)
        binding.pagerShipType.currentItem = currentPos
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
        return "Home_shopping"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                MainShipTypeFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
