package com.pplus.prnumberuser.apps.page.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.page.data.PageEventAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.FragmentPageEventBinding
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PageEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PageEventFragment : BaseFragment<BaseActivity>() {

    // TODO: Rename and change types of parameters


    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: PageEventAdapter? = null
    private lateinit var mPage: Page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPage = it.getParcelable(Const.PAGE)!!
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentPageEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPageEventBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {


        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerPageEvent.layoutManager = mLayoutManager
        mAdapter = PageEventAdapter()
        binding.recyclerPageEvent.adapter = mAdapter
        binding.recyclerPageEvent.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : PageEventAdapter.OnItemClickListener {

            override fun onItemClick(position: Int, isWinAnnounce: Boolean) {

                val item = mAdapter!!.getItem(position)

                if(isWinAnnounce){
                    val intent = Intent(activity, PageEventAnnounceActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.putExtra(Const.PAGE, mPage)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    activity?.startActivityForResult(intent, Const.REQ_DETAIL)
                }else{
                    val intent = Intent(activity, PageEventDetailActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.putExtra(Const.PAGE, mPage)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    activity?.startActivityForResult(intent, Const.REQ_DETAIL)
                }

            }
        })

        getCount()
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().getEventCountByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                mTotalCount = response.data

                if (mTotalCount == 0) {
                    binding.layoutPageEventNotExist.visibility = View.VISIBLE
                } else {
                    binding.layoutPageEventNotExist.visibility = View.GONE
                }

                mPaging = 1
                mAdapter?.clear()
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

                hideProgress()
                if (!isAdded) {
                    return
                }
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        params["pg"] = page.toString()
        params["appType"] = Const.APP_TYPE
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getEventListByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                mLockListView = false
                if(response?.datas != null){
                    mAdapter?.addAll(response.datas)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
            }
        }).build().call()
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance(page: Page) =
                PageEventFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Const.PAGE, page)
                    }
                }
    }

}// Required empty public constructor
