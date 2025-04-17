package com.pplus.luckybol.apps.main.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.data.LottoBannerAdapter
import com.pplus.luckybol.apps.event.ui.LottoHistoryActivity
import com.pplus.luckybol.apps.event.ui.LuckyLottoDetailActivity
import com.pplus.luckybol.apps.event.ui.LuckyLottoGuideActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentMainLottoBinding
import com.pplus.luckybol.databinding.FragmentPlayABinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [MainLottoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainLottoFragment : BaseFragment<BaseActivity>() {

    private var mPage: Int = 0
    private var mTotalCount = 0

    //    private var mLayoutManager: LinearLayoutManager? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: LottoBannerAdapter? = null
    private var mSelectPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

    private var _binding: FragmentMainLottoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentMainLottoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        mLayoutManager = LinearLayoutManager(requireActivity()) //        mLayoutManager = Lin(activity, 2)

        //        mGridLayoutManager!!.setSpanSizeLookup(object : SpanSizeLookup() {
        //            override fun getSpanSize(position: Int): Int {
        //                return when (mAdapter!!.getItemViewType(position)) {
        //                    mAdapter!!.TYPE_HEADER -> 2
        //                    mAdapter!!.TYPE_ITEM -> 1
        //                    else -> 1
        //                }
        //            }
        //        })

        binding.recyclerMainLotto.layoutManager = mLayoutManager
        mAdapter = LottoBannerAdapter()
        mAdapter!!.launcher = defaultLauncher
        binding.recyclerMainLotto.adapter = mAdapter

        binding.recyclerMainLotto.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy) //                visibleItemCount = mLayoutManager!!.childCount
                //                totalItemCount = mLayoutManager!!.itemCount
                //                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : LottoBannerAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {
                if (!PplusCommonUtil.loginCheck(activity!!, null)) {
                    return
                }

                val event = mAdapter!!.getItem(position)
                LogUtil.e(LOG_TAG, "position : "+position)
                LogUtil.e(LOG_TAG, "seqNo : "+event.no)
                val intent = Intent(requireActivity(), LuckyLottoDetailActivity::class.java)
                intent.putExtra(Const.DATA, event)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        })

        binding.swipeRefreshMainLotto.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                getCount()
                binding.swipeRefreshMainLotto.isRefreshing = false
            }
        })

        binding.layoutMainLottoWinHistory.setOnClickListener {
            val intent = Intent(activity, LottoHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMainLottoViewDetail.setOnClickListener {
            val intent = Intent(activity, LuckyLottoGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMainLottoLoading.visibility = View.VISIBLE

        getCount()

    }


    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                } //                binding.textPlayRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_bol_unit, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["filter"] = EventType.PrimaryType.goodluck.name
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        params["isLotto"] = "true"
        params["groupSeqNo"] = "2"
        ApiBuilder.create().getEventCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>,
                                    response: NewResultResponse<Int>) {
                if (!isAdded) {
                    return
                }
                mTotalCount = response.data!!

                if (mTotalCount == 0) {
                    binding.layoutMainLottoNotExist.visibility = View.VISIBLE
                } else {
                    binding.layoutMainLottoNotExist.visibility = View.GONE
                }

                mPage = 1
                mAdapter?.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>,
                                   t: Throwable,
                                   response: NewResultResponse<Int>) { //                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["filter"] = EventType.PrimaryType.goodluck.name
        params["pg"] = page.toString()
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        params["isLotto"] = "true"
        params["groupSeqNo"] = "2"
        mLockListView = true
        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>,
                                    response: NewResultResponse<Event>) {

                if (!isAdded) {
                    return
                }
                binding.layoutMainLottoLoading.visibility = View.GONE
                mLockListView = false

                mAdapter?.addAll(response.datas!!)
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>,
                                   t: Throwable,
                                   response: NewResultResponse<Event>) { //                hideProgress()
            }
        }).build().call()
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (LoginInfoManager.getInstance().isMember) {
                setRetentionBol() //                binding.textPlayRetentionBol.visibility = View.VISIBLE
                //                binding.textPlayLogin.visibility = View.GONE
            } else { //                binding.textPlayRetentionBol.visibility = View.GONE
                //                binding.textPlayLogin.visibility = View.VISIBLE
            }
        }
    }


    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getCount()
    }

    override fun getPID(): String {
        return "Main_playeventlist"
    }

    companion object {

        fun newInstance(): MainLottoFragment {

            val fragment = MainLottoFragment() //            val args = Bundle() //            args.putSerializable(Const.GROUP, groupSeqNo) //            fragment.arguments = args
            return fragment
        }
    }

} // Required empty public constructor
