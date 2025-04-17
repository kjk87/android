package com.pplus.prnumberuser.apps.product.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.menu.data.OrderMenuReviewAdapter
import com.pplus.prnumberuser.apps.menu.ui.OrderMenuReviewRegActivity
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.OrderMenuReview
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMyReviewBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class MyReviewFragment : BaseFragment<BaseActivity>() {


    private var _binding: FragmentMyReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMyReviewBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var mAdapter: OrderMenuReviewAdapter? = null
    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true

    override fun init() {

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerMyReview.layoutManager = mLayoutManager!!
        mAdapter = OrderMenuReviewAdapter()
        binding.recyclerMyReview.adapter = mAdapter

        binding.recyclerMyReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : OrderMenuReviewAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

                val orderMenuReview = mAdapter!!.getItem(position)

                if(orderMenuReview.member!!.seqNo == LoginInfoManager.getInstance().user.no){
                    val contents = arrayOf(getString(R.string.word_modified), getString(R.string.word_delete))
                    val builder = AlertBuilder.Builder()
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
                    builder.setContents(*contents)
                    builder.setLeftText(getString(R.string.word_cancel))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                            when(event_alert) {
                                AlertBuilder.EVENT_ALERT.LIST -> {
                                    when (event_alert.getValue()) {
                                        1 -> {
                                            val intent = Intent(activity, OrderMenuReviewRegActivity::class.java)
                                            intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                            intent.putExtra(Const.DATA, orderMenuReview)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            launcher.launch(intent)
                                        }

                                        2 -> {
                                            val params = HashMap<String, String>()
                                            params["seqNo"] = orderMenuReview.seqNo.toString()

                                            showProgress("")
                                            ApiBuilder.create().deleteProductReview(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                                    hideProgress()
                                                    showAlert(R.string.msg_deleted)
                                                    mPaging = 0
                                                    listCall(mPaging)
                                                }

                                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                                    hideProgress()
                                                }
                                            }).build().call()
                                        }
                                    }
                                }
                            }
                        }
                    }).builder().show(activity)
                }


            }
        })

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getReviewByMemberSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<OrderMenuReview>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<OrderMenuReview>>>?, response: NewResultResponse<SubResultResponse<OrderMenuReview>>?) {
                hideProgress()
                if(!isAdded){
                    return
                }
                if (response != null) {

                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        binding.textMyReviewTotalCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_my_review, FormatUtil.getMoneyType(mTotalCount.toString())))
                        if (mTotalCount == 0) {
                            binding.layoutMyReviewNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutMyReviewNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<OrderMenuReview>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<OrderMenuReview>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            mPaging = 0
            listCall(mPaging)
        }

    }


    override fun getPID(): String {
        return ""
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
                MyReviewFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
