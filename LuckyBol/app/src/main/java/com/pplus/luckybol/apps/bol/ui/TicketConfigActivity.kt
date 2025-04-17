//package com.pplus.luckybol.apps.bol.ui
//
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.bol.data.TicketAdapter
//import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
//import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
//import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
//import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.core.network.ApiBuilder
//import com.pplus.luckybol.core.network.model.dto.Bol
//import com.pplus.luckybol.core.network.model.response.NewResultResponse
//import com.pplus.luckybol.core.util.PplusCommonUtil
//import com.pplus.luckybol.databinding.ActivityTicketConfigBinding
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.format.FormatUtil
//import retrofit2.Call
//
//class TicketConfigActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return "Main_mypage_lotto_ticket"
//    }
//
//    private lateinit var binding: ActivityTicketConfigBinding
//
//    override fun getLayoutView(): View {
//        binding = ActivityTicketConfigBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    private var mPage = 1
//    private var mTotalCount = 0
//    private var mLockListView = true
//    private var mSort = "new"
//    private var mAdapter: TicketAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        binding.textTicketConfigSortPast.setOnClickListener {
//            mSort = "old"
//            binding.textTicketConfigSortPast.isSelected = true
//            binding.textTicketConfigSortRecent.isSelected = false
//
//            getCount()
//        }
//
//        binding.textTicketConfigSortRecent.setOnClickListener {
//            mSort = "new"
//
//            binding.textTicketConfigSortPast.isSelected = false
//            binding.textTicketConfigSortRecent.isSelected = true
//            getCount()
//        }
//
//        mLayoutManager = LinearLayoutManager(this)
//        binding.recyclerTicketConfig.layoutManager = mLayoutManager
//        mAdapter = TicketAdapter(this)
//        binding.recyclerTicketConfig.adapter = mAdapter
////        recycler_point_config.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_20))
//
//        binding.recyclerTicketConfig.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPage++
//                        listCall(mPage)
//                    }
//                }
//            }
//        })
//
//        mAdapter!!.setOnItemClickListener(object : TicketAdapter.OnItemClickListener{
//            override fun onItemClick(position: Int) {
////                val intent = Intent(this@TicketConfigActivity, PointHistoryDetailActivity::class.java)
////                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                startActivity(intent)
//            }
//        })
//
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//            override fun reload() {
//                binding.textPointConfigRetentionTicket.text = getString(R.string.format_ticket_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.lottoTicketCount.toString()))
//            }
//        })
//
//        binding.textTicketConfigSortPast.isSelected = false
//        binding.textTicketConfigSortRecent.isSelected = true
//
//        val bol = intent.getParcelableExtra<Bol>(Const.DATA)
//        if(bol != null){
////            val intent = Intent(this@TicketConfigActivity, PointHistoryDetailActivity::class.java)
////            intent.putExtra(Const.DATA, bol)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
//        }
//
//        if(LoginInfoManager.getInstance().user.lottoTicketCount!! > 0){
//            binding.layoutTicketConfigRetentionTicket.visibility = View.VISIBLE
//            binding.layoutTicketConfigNotRetentionTicket.visibility = View.GONE
//        }else{
//            binding.layoutTicketConfigRetentionTicket.visibility = View.GONE
//            binding.layoutTicketConfigNotRetentionTicket.visibility = View.VISIBLE
//
//            binding.textTicketConfigInvite.setOnClickListener {
//                PplusCommonUtil.share(this)
//            }
//        }
//
//        getCount()
//    }
//
//    private fun getCount() {
//        val params = HashMap<String, String>()
//
//        ApiBuilder.create().getLottoTicketHistoryCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//
//                if (isFinishing) {
//                    return
//                }
//
//                mTotalCount = response.data!!
//                if(mTotalCount > 0){
//                    binding.textTicketConfigNotExist.visibility = View.GONE
//                }else{
//                    binding.textTicketConfigNotExist.visibility = View.VISIBLE
//                }
//                mPage = 1
//                mAdapter!!.clear()
//                listCall(mPage)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
//
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//
//        val params = HashMap<String, String>()
//        params["align"] = mSort
//        params["pg"] = "" + page
//        mLockListView = true
//        showProgress("")
//        ApiBuilder.create().getLottoTicketHistory(params).setCallback(object : PplusCallback<NewResultResponse<Bol>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Bol>>, response: NewResultResponse<Bol>) {
//
//                hideProgress()
//                if (isFinishing) {
//                    return
//                }
//
//                mLockListView = false
//
//                mAdapter!!.addAll(response.datas!!)
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Bol>>, t: Throwable, response: NewResultResponse<Bol>) {
//
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_lotto_ticket_config), ToolbarOption.ToolbarMenu.LEFT)
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
//                else -> {}
//            }
//        }
//    }
//}
