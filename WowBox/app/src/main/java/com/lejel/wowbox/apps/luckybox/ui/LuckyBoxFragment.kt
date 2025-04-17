package com.lejel.wowbox.apps.luckybox.ui


import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.community.ui.CommunityApplyActivity
import com.lejel.wowbox.apps.faq.ui.FaqActivity
import com.lejel.wowbox.apps.login.LoginActivity2
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxWinAdapter2
import com.lejel.wowbox.apps.main.data.HomeBannerAdapter
import com.lejel.wowbox.apps.main.ui.MainActivity
import com.lejel.wowbox.apps.notice.ui.NoticeActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Banner
import com.lejel.wowbox.core.network.model.dto.Count
import com.lejel.wowbox.core.network.model.dto.LuckyBox
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentLuckyBoxBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call
import java.util.Locale


/**
 * A simple [Fragment] subclass.
 * Use the [LuckyBoxFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LuckyBoxFragment : BaseFragment<MainActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState) //        if (arguments != null) {
        //            mGroupNo = arguments!!.getInt(Const.GROUP)
        //        }
    }

    private var _binding: FragmentLuckyBoxBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentLuckyBoxBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    lateinit var mBannerAdapter: HomeBannerAdapter
    lateinit var mLinearLayoutManager:LinearLayoutManager
    private var mLuckyBoxWinAdapter: LuckyBoxWinAdapter2? = null

    fun ViewPager2.setCurrentItemWithDuration(
        item: Int,
        duration: Long,
        isVertical: Boolean,
        interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
        pagePxWidth: Int = width, // Default value taken from getWidth() from ViewPager2 view
        pagePxHeight: Int = height // Default value taken from getWidth() from ViewPager2 view
    ) {
        var pagePx = 0
        if(isVertical){
            pagePx = pagePxHeight
        }else{
            pagePx = pagePxWidth
        }
        val pxToDrag: Int = pagePx * (item - currentItem)
        val animator = ValueAnimator.ofInt(0, pxToDrag)
        var previousValue = 0
        animator.addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Int
            val currentPxToDrag = (currentValue - previousValue).toFloat()
            fakeDragBy(-currentPxToDrag)
            previousValue = currentValue
        }
        animator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) { beginFakeDrag() }
            override fun onAnimationEnd(animation: Animator) { endFakeDrag() }
            override fun onAnimationCancel(animation: Animator) { /* Ignored */ }
            override fun onAnimationRepeat(animation: Animator) { /* Ignored */ }
        })
        animator.interpolator = interpolator
        animator.duration = duration
        animator.start()
    }

    override fun init() {

        mBannerAdapter = HomeBannerAdapter()
        binding.pagerLuckyBoxBanner.adapter = mBannerAdapter

        mBannerAdapter.listener = object : HomeBannerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val item = mBannerAdapter.getItem(position)
                when (item.moveType) {
                    "inner" -> {
                        when (item.innerType) {
                            "telegram" -> {
                                if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                                    return
                                }

                                val intent = Intent(requireActivity(), CommunityApplyActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }

                            "main" -> {
                            }
                            "notice" -> {
                                val intent = Intent(requireActivity(), NoticeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }
                            "faq" -> {
                                val intent = Intent(requireActivity(), FaqActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }
                            "luckyboxGuide" -> {
                                val intent = Intent(requireActivity(), LuckyBoxGuideActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                        }

                    }

                    "outer" -> {
                        PplusCommonUtil.openChromeWebView(requireActivity(), item.outerUrl!!)
                    }
                }
            }
        }

        binding.appBarLuckyBox.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

            //            if (verticalOffset <= -binding.collapsingLuckyBox.height + binding.toolbarLuckyBox.height) { //toolbar is collapsed here
            //                binding.imageLuckyBoxContainer.setImageResource(R.drawable.ic_lucky_box_b)
            //                binding.textLuckyBoxContainer.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_2e2e2e))
            //                binding.textLuckyBoxContainer.setBackgroundResource(R.drawable.underbar_2e2e2e_transparent)
            //                binding.layoutLuckyBoxContainer.setBackgroundResource(R.drawable.bg_ffeeee_radius_12)
            //            } else {
            //                binding.imageLuckyBoxContainer.setImageResource(R.drawable.ic_lucky_box)
            //                binding.textLuckyBoxContainer.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.white))
            //                binding.textLuckyBoxContainer.setBackgroundResource(R.drawable.underbar_ffffff_transparent)
            //                binding.layoutLuckyBoxContainer.setBackgroundColor(ResourceUtil.getColor(requireActivity(), android.R.color.transparent))
            //            }
        }

        binding.tabLuckyBox.setIsChangeBold(false)
        binding.tabLuckyBox.setSelectedIndicatorColors(ContextCompat.getColor(requireActivity(), R.color.color_ea5506))
        binding.tabLuckyBox.setCustomTabView(R.layout.item_faq_category_tab, R.id.text_faq_category_tab)
        binding.tabLuckyBox.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_3)) //        binding.tabLayoutCategoryPage.setDistributeEvenly(false)
        binding.tabLuckyBox.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)

        binding.textLuckyBoxBuy.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val position = binding.pagerLuckyBox.currentItem
            if (mLuckyBoxList != null) {
                val luckyBox = mLuckyBoxList!![position]

                val intent = Intent(activity, LuckyBoxBuyActivity::class.java)
                intent.putExtra(Const.DATA, luckyBox)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        }

        binding.layoutLuckyBoxContainer.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(activity, LuckyBoxContainerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        val handler = Handler(Looper.myLooper()!!)
        binding.pagerLuckyBoxBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                binding.textLuckyBoxBannerPage.text = "${position+1}/${mBannerAdapter.itemCount}"
                handler.removeMessages(0)
                val runnable = Runnable {
                    if (!isAdded) {
                        return@Runnable
                    }

                    val size = (binding.pagerLuckyBoxBanner.adapter?.itemCount ?: 0)
                    if (binding.pagerLuckyBoxBanner.currentItem < size - 1) {
                        binding.pagerLuckyBoxBanner.setCurrentItemWithDuration(binding.pagerLuckyBoxBanner.currentItem + 1, 1000, false)
                    } else {
                        binding.pagerLuckyBoxBanner.setCurrentItemWithDuration(0, 1000, false)
                    }
                }

                handler.postDelayed(runnable, 2500)
            }

        })

//        mLinearLayoutManager = object : LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false){
//            override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
//                val scroller: LinearSmoothScroller =
//                    object : LinearSmoothScroller(requireActivity()) {
//                        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
//                            return 4000f / displayMetrics.densityDpi;
//                        }
//                    }
//                scroller.targetPosition = position
//                startSmoothScroll(scroller)
//            }
//        }

        mLuckyBoxWinAdapter = LuckyBoxWinAdapter2()
        binding.pagerLuckyBoxWinHistory.adapter = mLuckyBoxWinAdapter
        mLuckyBoxWinAdapter!!.listener = object : LuckyBoxWinAdapter2.OnItemClickListener{
            override fun onClick(position: Int) {
                val intent = Intent(activity, LuckyBoxWinHistoryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        }
        binding.pagerLuckyBoxWinHistory.run { isUserInputEnabled = false }
        val winHistoryHandler = Handler(Looper.myLooper()!!)
        binding.pagerLuckyBoxWinHistory.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                winHistoryHandler.removeMessages(0)
                val runnable = Runnable {
                    if (!isAdded) {
                        return@Runnable
                    }

                    val size = (binding.pagerLuckyBoxWinHistory.adapter?.itemCount ?: 0)
                    if (binding.pagerLuckyBoxWinHistory.currentItem < size - 1) {
                        binding.pagerLuckyBoxWinHistory.setCurrentItemWithDuration(binding.pagerLuckyBoxWinHistory.currentItem + 1, 1500, true)
                    } else {
                        binding.pagerLuckyBoxWinHistory.setCurrentItemWithDuration(0, 1500, true)
                    }
                }

                winHistoryHandler.postDelayed(runnable, 2500)
            }
        })
//        binding.recyclerLuckyBoxWinHistory.layoutManager = mLinearLayoutManager
//        binding.recyclerLuckyBoxWinHistory.adapter = mLuckyBoxWinAdapter
//        binding.recyclerLuckyBoxWinHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if(newState == RecyclerView.SCROLL_STATE_IDLE){
//
//                    visibleItemCount = mLinearLayoutManager.childCount
//                    if(visibleItemCount < mLuckyBoxWinAdapter!!.itemCount){
//                        binding.recyclerLuckyBoxWinHistory.smoothScrollToPosition(mLuckyBoxWinAdapter!!.itemCount)
//                    }
//
//                }
//            }
//        })

        winListCall(1)
        getBannerList()
        getLuckyBoxList()
        loginCheck()
    }

    private fun winListCall(page: Int) {
        val params = java.util.HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "40"
        //        showProgress("")
        ApiBuilder.create().getTotalLuckyBoxPurchaseItemList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>> {

            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>?,
                                    response: NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>?) {
                //                hideProgress()
                if(!isAdded){
                    return
                }
                if (response?.result != null) {
                    if (page == 1) {
                        mLuckyBoxWinAdapter!!.clear()
                    }

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mLuckyBoxWinAdapter!!.setDataList(dataList as MutableList)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>?) {
                //                hideProgress()
                if(!isAdded){
                    return
                }
            }

        }).build().call()
    }
    private fun getBannerList() {
        val params = HashMap<String, String>()
        params["aos"] = "1"
        params["type"] = "luckybox"
        params["nation"] = Locale.getDefault().country
        ApiBuilder.create().getBannerList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Banner>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, response: NewResultResponse<ListResultResponse<Banner>>?) {

                if(!isAdded){
                    return
                }

                if (response?.result != null && response.result!!.list != null && response.result!!.list!!.isNotEmpty()) {
                    binding.layoutLuckyBoxBanner.visibility = View.VISIBLE
                    val bannerList = response.result!!.list!!
                    mBannerAdapter.setDataList(bannerList as MutableList<Banner>)
                    if(mBannerAdapter.itemCount > 1){
                        binding.textLuckyBoxBannerPage.visibility = View.VISIBLE
                        binding.textLuckyBoxBannerPage.text = "1/${mBannerAdapter.itemCount}"
                    }else{
                        binding.textLuckyBoxBannerPage.visibility = View.GONE
                    }
                }else{
                    binding.layoutLuckyBoxBanner.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Banner>>?) {
                if(!isAdded){
                    return
                }
            }
        }).build().call()
    }

    private fun getNotOpenItemCount() {
        ApiBuilder.create().getCountNotOpenLuckyBoxPurchaseItem().setCallback(object : PplusCallback<NewResultResponse<Count>> {
            override fun onResponse(call: Call<NewResultResponse<Count>>?,
                                    response: NewResultResponse<Count>?) {
                if (!isAdded) {
                    return
                }
                if (response?.result != null && response.result!!.count!! > 0) {
                    binding.imageLuckyBoxContainerNew.visibility = View.VISIBLE
                } else {
                    binding.imageLuckyBoxContainerNew.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Count>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Count>?) {
                if(!isAdded){
                    return
                }
            }
        }).build().call()
    }

    var mAdapter: PagerAdapter? = null
    var mLuckyBoxList: List<LuckyBox>? = null

    private fun getLuckyBoxList() {
        ApiBuilder.create().getLuckyBoxList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyBox>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyBox>>>?,
                                    response: NewResultResponse<ListResultResponse<LuckyBox>>?) {
                if (!isAdded) {
                    return
                }

                if (response?.result != null && response.result!!.list != null) {
                    mLuckyBoxList = response.result!!.list
                    if(mLuckyBoxList!!.isEmpty()){
                        return
                    }
                    binding.textLuckyBoxBuy.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mLuckyBoxList!![0].engagePrice.toString()))
                    mAdapter = PagerAdapter(requireActivity())
                    mAdapter!!.mList = mLuckyBoxList as MutableList<LuckyBox>
                    binding.pagerLuckyBox.adapter = mAdapter
                    binding.pagerLuckyBox.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            binding.textLuckyBoxBuy.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mLuckyBoxList!![position].engagePrice.toString()))
                        }
                    })
                    val titleList = arrayListOf<String>()
                    for (category in mLuckyBoxList!!) {
                        titleList.add(category.title!!)
                    }
                    binding.tabLuckyBox.setViewPager(binding.pagerLuckyBox, titleList)
                    binding.pagerLuckyBox.currentItem = 0
                    if (mLuckyBoxList!!.size > 1) {
                        binding.layoutLuckyBoxTab.visibility = View.VISIBLE
                    } else {
                        binding.layoutLuckyBoxTab.visibility = View.GONE
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyBox>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<ListResultResponse<LuckyBox>>?) {
                if(!isAdded){
                    return
                }
            }
        }).build().call()
    }

    inner class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        var mList: MutableList<LuckyBox>
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
            LogUtil.e(LOG_TAG, mLuckyBoxList!![position].title)
            return LuckyBoxItemFragment.newInstance(mLuckyBoxList!![position])
        }
    }

    fun loginCheck(){
        if (LoginInfoManager.getInstance().isMember()) {
            getNotOpenItemCount()
            binding.textLuckyBoxLogin.visibility = View.GONE
        }else{
            binding.textLuckyBoxLogin.visibility = View.VISIBLE
            binding.textLuckyBoxLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity2::class.java)
                defaultLauncher.launch(intent)
            }
        }
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    override fun getPID(): String {
        return "Main_luckybox"
    }

    companion object {

        fun newInstance(): LuckyBoxFragment {

            val fragment = LuckyBoxFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

} // Required empty public constructor
