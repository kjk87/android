package com.lejel.wowbox.apps.luckybox.ui


import android.app.Activity.RESULT_OK
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
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxOpenAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.FragmentLuckyBoxOpenBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [LuckyBoxOpenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LuckyBoxOpenFragment : BaseFragment<BaseActivity>() {

    var mAdapter: LuckyBoxOpenAdapter? = null
    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

    private var _binding: FragmentLuckyBoxOpenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentLuckyBoxOpenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        mAdapter = LuckyBoxOpenAdapter()
        mLayoutManager = LinearLayoutManager(requireActivity())

        binding.recyclerLuckyNotOpen.layoutManager = mLayoutManager //        binding.recyclerLuckyBoxNotOpen.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_200))
        binding.recyclerLuckyNotOpen.adapter = mAdapter!!

        mAdapter!!.listener = object : LuckyBoxOpenAdapter.OnItemClickListener {

            override fun onRegReview(position: Int) { //                val item = mAdapter!!.getItem(position)
                //                val intent = Intent(requireActivity(), LuckyBoxReviewRegActivity::class.java)
                //                intent.putExtra(Const.MODE, EnumData.MODE.WRITE.name)
                //                intent.putExtra(Const.LUCKY_BOX_PURCHASE_ITEM, item)
                //                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                //                defaultLauncher.launch(intent)
            }

            override fun onClick(position: Int) {
                val item = mAdapter!!.getItem(position)
                if (item.status == 2 && item.deliveryStatus != null) {
                    val intent = Intent(requireActivity(), LuckyBoxPurchaseItemDetailActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    defaultLauncher.launch(intent)
                }
            }

            override fun reqDelivery(position: Int) {
                val item = mAdapter!!.getItem(position)
                if (StringUtils.isNotEmpty(item.impression)) {
                    val intent = Intent(requireActivity(), LuckyBoxReqShippingActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    defaultLauncher.launch(intent)
                } else {
                    val intent = Intent(requireActivity(), AlertLuckyBoxImpressionActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    impressionLauncher.launch(intent)
                }

            }

            override fun onCashBack(position: Int, type: String) {

                val item = mAdapter!!.getItem(position)

                when (type) {
                    "point" -> {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_lucky_box_cash_back_title))
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_lucky_box_cash_back_desc1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_lucky_box_cash_back_desc2, FormatUtil.getMoneyTypeFloat(item.price.toString())), AlertBuilder.MESSAGE_TYPE.HTML, 1))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val params = HashMap<String, String>()
                                        params["type"] = type
                                        showProgress("")
                                        ApiBuilder.create().cashBackLuckyBoxPurchaseItem(item.seqNo!!, params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                                setEvent(requireActivity(), "wowbox_cashBack")
                                                hideProgress()
                                                val intent = Intent(requireActivity(), AlertLuckyBoxPointExchangeCompleteActivity::class.java)
                                                intent.putExtra(Const.POINT, item.price!!.toInt())
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                startActivity(intent)
                                                mPaging = 1
                                                listCall(mPaging)
                                            }

                                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                                hideProgress()
                                            }
                                        }).build().call()
                                    }

                                    else -> {}
                                }
                            }
                        }).builder().show(requireActivity())
                    }

                    "ball" -> {
                        val intent = Intent(requireActivity(), AlertLuckyBoxExchangeBolActivity::class.java)
                        intent.putExtra(Const.DATA, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        bolExchangeLauncher.launch(intent)
                    }
                }


            }
        }

        binding.recyclerLuckyNotOpen.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })
        mPaging = 1
        listCall(mPaging)
    }

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
                outRect.bottom = mLastOffset
            }
        }
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        showProgress("")
        ApiBuilder.create().getOpenLuckyBoxPurchaseItemList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>?, response: NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount == 0) {
                            binding.luckyBoxOpenNotExist.visibility = View.VISIBLE
                        } else {
                            binding.luckyBoxOpenNotExist.visibility = View.GONE
                        }
                        binding.textLuckyBoxOpenTotalCount.text = getString(R.string.format_total_count, mTotalCount.toString())
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>?) {
                hideProgress()
                mLockListView = false
                if (!isAdded) {
                    return
                }
            }
        }).build().call()
    }

    val bolExchangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {

            val luckyBoxPurchaseItem = PplusCommonUtil.getParcelableExtra(result.data!!, Const.DATA, LuckyBoxPurchaseItem::class.java)!!

            val params = HashMap<String, String>()
            params["type"] = "ball"
            showProgress("")
            ApiBuilder.create().cashBackLuckyBoxPurchaseItem(luckyBoxPurchaseItem.seqNo!!, params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    ToastUtil.show(requireActivity(), getString(R.string.msg_cash_back_complete))
                    mPaging = 1
                    listCall(mPaging)
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()
        }
    }

    val impressionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (!isAdded) {
            return@registerForActivityResult
        }

        val luckyBoxPurchaseItem = PplusCommonUtil.getParcelableExtra(result.data!!, Const.DATA, LuckyBoxPurchaseItem::class.java)!!

        val intent = Intent(requireActivity(), LuckyBoxReqShippingActivity::class.java)
        intent.putExtra(Const.DATA, luckyBoxPurchaseItem)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        defaultLauncher.launch(intent)
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (!isAdded) {
            return@registerForActivityResult
        }
        mPaging = 1
        listCall(mPaging)
    }

    override fun getPID(): String {
        return ""
    }

    companion object {
        @JvmStatic
        fun newInstance() = LuckyBoxOpenFragment().apply {
            arguments = Bundle().apply { //                    putParcelable(Const.DATA, luckyBox)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }

} // Required empty public constructor
