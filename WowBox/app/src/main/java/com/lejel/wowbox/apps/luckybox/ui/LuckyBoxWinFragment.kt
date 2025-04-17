package com.lejel.wowbox.apps.luckybox.ui


import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxWinAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentLuckyBoxWinBinding
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [LuckyBoxWinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LuckyBoxWinFragment : BaseFragment<BaseActivity>() {

    private var mPage: Int = 1
    private var mLayoutManager: LinearLayoutManager? = null
    private var mTotalCount = 0
    private var mLockListView = true
    private var mAdapter: LuckyBoxWinAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) { //            type = arguments.getSerializable(Const.TYPE) as EnumData.AdsType
        }
    }

    private var _binding: FragmentLuckyBoxWinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentLuckyBoxWinBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        mLayoutManager = LinearLayoutManager(requireActivity())

        binding.recyclerLuckyBoxWinHistory.layoutManager = mLayoutManager
        mAdapter = LuckyBoxWinAdapter()
        mAdapter!!.replyLauncher = replyLauncher
//        mAdapter!!.replyLuckyPickLauncher = replyLuckyPickLauncher
        binding.recyclerLuckyBoxWinHistory.adapter = mAdapter
        binding.recyclerLuckyBoxWinHistory.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_96))

        binding.recyclerLuckyBoxWinHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
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

        mAdapter!!.listener = object : LuckyBoxWinAdapter.OnItemClickListener{
            override fun onClick(position: Int) {
                val luckyBoxPurchaseItem = mAdapter!!.getItem(position)

                if(LoginInfoManager.getInstance().isMember() && luckyBoxPurchaseItem.userKey == LoginInfoManager.getInstance().member!!.userKey){
                    val builder = AlertBuilder.Builder()
                    builder.setRightText(getString(R.string.word_cancel))

                    builder.setContents(getString(R.string.word_modified))

                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert.value) {
                                1 -> {
                                    val intent = Intent(requireActivity(), AlertLuckyBoxImpressionActivity::class.java)
                                    intent.putExtra(Const.DATA, luckyBoxPurchaseItem)
                                    intent.putExtra(Const.POSITION, position)
                                    replyLauncher.launch(intent)
                                }
                            }
                        }
                    }).builder().show(requireActivity())
                }
            }
        }

        binding.layoutLuckyBoxWinLoading.visibility = View.VISIBLE

        mPage = 1
        listCall(mPage)

    }

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
                outRect.bottom = mLastOffset
            }
        }
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = java.util.HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
//        showProgress("")
        ApiBuilder.create().getTotalLuckyBoxPurchaseItemList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>> {

            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>?,
                                    response: NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>?) {
//                hideProgress()
                if(!isAdded){
                    return
                }

                binding.layoutLuckyBoxWinLoading.visibility = View.GONE

                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if(mTotalCount == 0){
                            binding.layoutLuckyBoxWinHistoryNotExist.visibility = View.VISIBLE
                        }else{
                            binding.layoutLuckyBoxWinHistoryNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>?) {
//                hideProgress()
                mLockListView = false
                if(!isAdded){
                    return
                }

                binding.layoutLuckyBoxWinLoading.visibility = View.GONE
            }

        }).build().call()
    }

    val replyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.data != null) {
            val data = result.data!!
            val item = PplusCommonUtil.getParcelableExtra(data, Const.DATA, LuckyBoxPurchaseItem::class.java)
            val position = data.getIntExtra(Const.POSITION, -1)
            if (item != null && position != -1) {
                getLuckyBoxPurchaseItem(item, position)
            }
        }
    }

//    val replyLuckyPickLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.data != null) {
//            val data = result.data!!
//            val item = PplusCommonUtil.getParcelableExtra(data, Const.DATA, LuckyPickPurchaseItem::class.java)
//            val position = data.getIntExtra(Const.POSITION, -1)
//            if (item != null && position != -1) {
//                getLuckyPickPurchaseItem(item, position)
//            }
//
//        }
//    }

    private fun getLuckyBoxPurchaseItem(item: LuckyBoxPurchaseItem, position: Int) {
//        showProgress("")
        ApiBuilder.create().getLuckyBoxPurchaseItem(item.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<LuckyBoxPurchaseItem>> {
            override fun onResponse(call: Call<NewResultResponse<LuckyBoxPurchaseItem>>?,
                                    response: NewResultResponse<LuckyBoxPurchaseItem>?) {
//                hideProgress()
                if (response?.result != null) {
                    mAdapter!!.mDataList!![position] = response.result!!
                    mAdapter!!.notifyItemChanged(position)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<LuckyBoxPurchaseItem>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<LuckyBoxPurchaseItem>?) {
                hideProgress()
            }
        }).build().call()
    }

//    private fun getLuckyPickPurchaseItem(item: LuckyPickPurchaseItem, position: Int) {
//        val params = HashMap<String, String>()
//        params["seqNo"] = item.seqNo.toString()
//        showProgress("")
//        ApiBuilder.create().getLuckyPickPurchaseItem(params).setCallback(object : PplusCallback<NewResultResponse<LuckyPickPurchaseItem>> {
//            override fun onResponse(call: Call<NewResultResponse<LuckyPickPurchaseItem>>?,
//                                    response: NewResultResponse<LuckyPickPurchaseItem>?) {
//                hideProgress()
//                if (response?.data != null) {
//                    val luckyPickPurchaseItem = response.data!!
//                    val luckyBoxPurchaseItem = luckyPickPurchaseItem.convertLuckyBox()
//                    mAdapter!!.mDataList!![position] = luckyBoxPurchaseItem
//                    mAdapter!!.notifyItemChanged(position)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<LuckyPickPurchaseItem>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<LuckyPickPurchaseItem>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }

    override fun getPID(): String {
        return "Main_luckyboxwin"
    }

    companion object {

        fun newInstance(): LuckyBoxWinFragment {

            val fragment = LuckyBoxWinFragment()
            val args = Bundle() //            args.putSerializable(Const.TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

} // Required empty public constructor
