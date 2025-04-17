package com.pplus.prnumberuser.apps.main.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberuser.apps.main.data.MainPageWithSubscriptionProductAdapter
import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
import com.pplus.prnumberuser.apps.subscription.ui.MySubscriptionActivity
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMainPageWithSubscriptionProductBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class MainPageWithSubscriptionProductFragment : BaseFragment<BaseActivity>() {

    private var _binding: FragmentMainPageWithSubscriptionProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val intentFilter = IntentFilter()
        intentFilter.addAction(activity?.packageName + ".sigIn")
        requireActivity().registerReceiver(mSignInReceiver, intentFilter)

        _binding = FragmentMainPageWithSubscriptionProductBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(mSignInReceiver)
        _binding = null
    }

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: MainPageWithSubscriptionProductAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLocationData: LocationData? = null
    private var mIsLast = false

    val mSignInReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            checkSignIn()
        }
    }

    override fun init() {

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerMainPageWithSubscriptionProduct.layoutManager = mLayoutManager!!
        mAdapter = MainPageWithSubscriptionProductAdapter()
        mAdapter!!.mMainPageWithSubscriptionProductFragment = this
        binding.recyclerMainPageWithSubscriptionProduct.adapter = mAdapter
        binding.recyclerMainPageWithSubscriptionProduct.addItemDecoration(BottomItemOffsetDecoration(requireActivity(), R.dimen.height_120))

//        recycler_main_goods_plus.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.layout_main_floating))
        binding.recyclerMainPageWithSubscriptionProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : MainPageWithSubscriptionProductAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {

            }
        })
        binding.layoutMainPageWithSubscriptionProductLoading.visibility  = View.VISIBLE

        binding.textMainPageWithSubscriptionProductRetention.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(),null)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MySubscriptionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.textPageWithSubscriptionProductSearch.setOnClickListener {
            PplusCommonUtil.alertLocation(getParentActivity(), true, object : PplusCommonUtil.Companion.SuccessLocationListener {
                override fun onSuccess() {
                    PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {

                        override fun onResult(address: String) {

                            if (!isAdded) {
                                return
                            }

                            binding.textPageWithSubscriptionProductAddress.text = address
                        }
                    })

                    mLocationData = LocationUtil.specifyLocationData
                    mPaging = 0
                    listCall(mPaging)
                }
            })
        }

        getParentActivity().mLocationListener = object : BaseActivity.LocationListener {
            override fun onLocation(result : ActivityResult) {
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

                                binding.textPageWithSubscriptionProductAddress.text = address
                            }
                        })

                        mLocationData = LocationUtil.specifyLocationData
                        mPaging = 0
                        listCall(mPaging)
                    }
                })
            }
        }

        binding.textPageWithSubscriptionProductAddress.setSingleLine()
        binding.textPageWithSubscriptionProductAddress.setOnClickListener {
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

        binding.textMainPageWithSubscriptionProductLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        binding.textMainPageWithSubscriptionProductPoint.setOnClickListener {
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        checkSignIn()

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

                        binding.textPageWithSubscriptionProductAddress.text = address
                    }
                })
                mLocationData = LocationUtil.specifyLocationData
                mPaging = 0
                listCall(mPaging)
            }
        })
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        checkSignIn()
    }

    private fun checkSignIn(){
        if (LoginInfoManager.getInstance().isMember) {
            binding.textMainPageWithSubscriptionProductPoint.visibility = View.VISIBLE
            binding.textMainPageWithSubscriptionProductLogin.visibility = View.GONE
            binding.textMainPageWithSubscriptionProductRetention.visibility = View.VISIBLE
            setRetentionBol()
        } else {
            binding.textMainPageWithSubscriptionProductPoint.visibility = View.GONE
            binding.textMainPageWithSubscriptionProductLogin.visibility = View.VISIBLE
            binding.textMainPageWithSubscriptionProductRetention.visibility = View.GONE
        }
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                }
                binding.textMainPageWithSubscriptionProductPoint.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())
            }
        })

        getMySubscriptionCount()
    }

    private fun getMySubscriptionCount(){
        ApiBuilder.create().getSubscriptionDownloadCountByMemberSeqNoAndStatus().setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                binding.textMainPageWithSubscriptionProductRetention.text = getString(R.string.format_retention_prepayment_voucher, response?.data.toString())
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        if (mLocationData != null) {
            params["latitude"] = mLocationData!!.latitude.toString()
            params["longitude"] = mLocationData!!.longitude.toString()
        }
        params["page"] = page.toString()
        ApiBuilder.create().getPageListWithSubscription(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Page2>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, response: NewResultResponse<SubResultResponse<Page2>>?) {
                if (!isAdded) {
                    return
                }
                binding.layoutMainPageWithSubscriptionProductLoading.visibility  =View.GONE
                if (response != null) {

                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            binding.layoutMainPageWithSubscriptionProductNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutMainPageWithSubscriptionProductNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Page2>>?) {
//                hideProgress()
                binding.layoutMainPageWithSubscriptionProductLoading.visibility  =View.GONE
                mLockListView = false
            }
        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }

        }
    }

    override fun getPID(): String {
        return "Main_page product"
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
                MainPageWithSubscriptionProductFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
