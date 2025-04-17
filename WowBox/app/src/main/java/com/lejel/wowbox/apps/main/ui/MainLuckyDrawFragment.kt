package com.lejel.wowbox.apps.main.ui

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.faq.ui.FaqActivity
import com.lejel.wowbox.apps.login.LoginActivity2
import com.lejel.wowbox.apps.luckyball.ui.BallConfigActivity
import com.lejel.wowbox.apps.luckybox.ui.LuckyBoxGuideActivity
import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawCompleteListActivity
import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawDetailActivity
import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawGuideActivity
import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawJoinActivity
import com.lejel.wowbox.apps.luckydraw.ui.MyLuckyDrawHistoryActivity
import com.lejel.wowbox.apps.main.data.HomeBannerAdapter
import com.lejel.wowbox.apps.main.data.LuckyDrawAdapter
import com.lejel.wowbox.apps.notice.ui.NoticeActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Banner
import com.lejel.wowbox.core.network.model.dto.LuckyDraw
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentMainLuckyDrawBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.Locale


/**
 * A simple [Fragment] subclass.
 * Use the [MainLuckyDrawFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainLuckyDrawFragment : BaseFragment<MainActivity>() { //    private var param1: String? = null
    //    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //        arguments?.let {
        //            param1 = it.getString(ARG_PARAM1)
        //            param2 = it.getString(ARG_PARAM2)
        //        }
    }

    private var _binding: FragmentMainLuckyDrawBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainLuckyDrawBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: LuckyDrawAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1

    lateinit var mBannerAdapter: HomeBannerAdapter

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
        binding.pagerMainLuckyDrawBanner.adapter = mBannerAdapter

        val handler = Handler(Looper.myLooper()!!)
        binding.pagerMainLuckyDrawBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                binding.textMainLuckyDrawBannerPage.text = "${position+1}/${mBannerAdapter.itemCount}"
                handler.removeMessages(0)
                val runnable = Runnable {
                    if (!isAdded) {
                        return@Runnable
                    }

                    val size = (binding.pagerMainLuckyDrawBanner.adapter?.itemCount ?: 0)
                    if (binding.pagerMainLuckyDrawBanner.currentItem < size - 1) {
                        binding.pagerMainLuckyDrawBanner.setCurrentItemWithDuration(binding.pagerMainLuckyDrawBanner.currentItem + 1, 1000, false)
                    } else {
                        binding.pagerMainLuckyDrawBanner.setCurrentItemWithDuration(0, 1000, false)
                    }
                }

                handler.postDelayed(runnable, 2500)
            }

        })

        mBannerAdapter.listener = object : HomeBannerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val item = mBannerAdapter.getItem(position)
                when (item.moveType) {
                    "inner" -> {
                        when (item.innerType) {

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
                        }
                    }

                    "outer" -> {
                        PplusCommonUtil.openChromeWebView(requireActivity(), item.outerUrl!!)
                    }
                }
            }
        }

        mAdapter = LuckyDrawAdapter()
        mAdapter!!.launcher = defaultLauncher
        mAdapter!!.checkPrivateLauncher = checkPrivateLauncher
        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerMainLuckyDraw.adapter = mAdapter
        binding.recyclerMainLuckyDraw.layoutManager = mLayoutManager

        binding.recyclerMainLuckyDraw.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        getList(mPaging)
                    }
                }
            }
        })

        mAdapter!!.listener = object : LuckyDrawAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val item = mAdapter!!.getItem(position)
                if(StringUtils.isNotEmpty(item.contents)){
                    if(requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_WEBVIEW)){
                        val intent = Intent(requireActivity(), LuckyDrawDetailActivity::class.java)
                        intent.putExtra(Const.DATA, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                        return
                    }
                }
            }
        }

        binding.textMainLuckyDrawComplete.setOnClickListener {
            val intent = Intent(requireActivity(), LuckyDrawCompleteListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMainLuckyDrawMyWinHistory.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), MyLuckyDrawHistoryActivity::class.java)
            intent.putExtra(Const.TAB, 1)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMainLuckyDrawMyPurchaseHistory.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), MyLuckyDrawHistoryActivity::class.java)
            intent.putExtra(Const.TAB, 0)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.swipeRefreshLuckyDraw.setOnRefreshListener(object : androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                if (!isAdded) {
                    return
                }
                mPaging = 1
                getList(mPaging)
                binding.swipeRefreshLuckyDraw.isRefreshing = false
            }
        })

        binding.layoutMainLuckyDrawRetentionBall.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), BallConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainLuckyDrawLoading.visibility = View.VISIBLE

        loginCheck()
        getBannerList()
        mPaging = 1
        getList(mPaging)
    }
    private fun getBannerList() {
        val params = HashMap<String, String>()
        params["aos"] = "1"
        params["type"] = "luckyDraw"
        params["nation"] = Locale.getDefault().country
        ApiBuilder.create().getBannerList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Banner>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, response: NewResultResponse<ListResultResponse<Banner>>?) {

                if(!isAdded){
                    return
                }

                if (response?.result != null && response.result!!.list != null && response.result!!.list!!.isNotEmpty()) {
                    binding.layoutMainLuckyDrawBanner.visibility = View.VISIBLE
                    val bannerList = response.result!!.list!!
                    mBannerAdapter.setDataList(bannerList as MutableList<Banner>)
                    if(mBannerAdapter.itemCount > 1){
                        binding.textMainLuckyDrawBannerPage.visibility = View.VISIBLE
                        binding.textMainLuckyDrawBannerPage.text = "1/${mBannerAdapter.itemCount}"
                    }else{
                        binding.textMainLuckyDrawBannerPage.visibility = View.GONE
                    }
                }else{
                    binding.layoutMainLuckyDrawBanner.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Banner>>?) {
                if(!isAdded){
                    return
                }
            }
        }).build().call()
    }


    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["aos"] = "1"

        mLockListView = true
        ApiBuilder.create().getLuckyDrawList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyDraw>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyDraw>>>?, response: NewResultResponse<ListResultResponse<LuckyDraw>>?) {
                if (!isAdded) {
                    return
                }
                binding.layoutMainLuckyDrawLoading.visibility = View.GONE

                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount == 0) {
                            binding.layoutMainLuckyDrawNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutMainLuckyDrawNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyDraw>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyDraw>>?) {
                if (!isAdded) {
                    return
                }
                binding.layoutMainLuckyDrawLoading.visibility = View.GONE
            }
        }).build().call()
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
            binding.textMainLuckyDrawLogin.visibility = View.GONE
            binding.layoutMainLuckyDrawRetentionBall.visibility = View.VISIBLE
            reloadSession()
        } else {
            binding.textMainLuckyDrawLogin.visibility = View.VISIBLE
            binding.layoutMainLuckyDrawRetentionBall.visibility = View.GONE
            binding.textMainLuckyDrawLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity2::class.java)
                defaultLauncher.launch(intent)
            }
        }
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                }
                binding.textMainLuckyDrawRetentionBall.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.ball!!.toInt().toString())
            }
        })
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
        mPaging = 1
        getList(mPaging)
    }

    private val checkPrivateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val item = PplusCommonUtil.getParcelableExtra(result.data!!, Const.DATA, LuckyDraw::class.java)

            val intent = Intent(requireActivity(), LuckyDrawJoinActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainLuckyDrawFragment().apply {
            arguments = Bundle().apply { //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}