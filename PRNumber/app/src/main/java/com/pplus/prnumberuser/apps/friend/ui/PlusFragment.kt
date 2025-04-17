package com.pplus.prnumberuser.apps.friend.ui


import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.common.ui.custom.SafeSwitchCompat
import com.pplus.prnumberuser.apps.plus.data.PlusAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.Plus
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMyPlusBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class PlusFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMyPlusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMyPlusBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: PlusAdapter? = null

    override fun init() {

        if(LoginInfoManager.getInstance().user.plusPush == null){
            LoginInfoManager.getInstance().user.plusPush = true
        }

        binding.switchMyPlusTotalAlarm.setSafeCheck(LoginInfoManager.getInstance().user.plusPush!!, SafeSwitchCompat.IGNORE);

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerMyPlus.layoutManager = mLayoutManager!!
        mAdapter = PlusAdapter()
        mAdapter!!.mIsAlarm = true
        binding.layoutMyPlusTotalAlarm.visibility = View.VISIBLE
        binding.recyclerMyPlus.adapter = mAdapter
//        recycler_my_plus.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_60, R.dimen.height_60))

        mAdapter!!.setOnItemClickListener(object : PlusAdapter.OnItemClickListener {

            override fun onItemClick(position: Int, view: View) {

                val location = IntArray(2)
                view.getLocationOnScreen(location)
                val x = location[0] + view.width / 2
                val y = location[1] + view.height / 2

                val params = HashMap<String, String>()
                params["no"] = mAdapter!!.getItem(position).no!!.toString()
                showProgress("")
                ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
                    override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                        hideProgress()
                        PplusCommonUtil.goPage(activity!!, response.data, x, y)
                    }

                    override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
                        hideProgress()
                    }
                }).build().call()
            }

            override fun onRefresh() {
                getCount()
            }
        })

        binding.recyclerMyPlus.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        binding.imageMyPlusBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.textMyPlusAlarm.setOnClickListener {
            if (mAdapter!!.mIsAlarm) {
                mAdapter!!.mIsAlarm = false
                binding.textMyPlusAlarm.setText(R.string.word_alarm_setting)
                binding.layoutMyPlusTotalAlarm.visibility = View.GONE
            } else {
                mAdapter!!.mIsAlarm = true
                binding.textMyPlusAlarm.setText(R.string.word_complete)
                binding.layoutMyPlusTotalAlarm.visibility = View.VISIBLE
            }
            mAdapter!!.notifyDataSetChanged()
        }

        binding.switchMyPlusTotalAlarm.setOnCheckedChangeListener { compoundButton, b ->
            val params = HashMap<String, String>()
            params["plusPush"] = b.toString()
            showProgress("")
            ApiBuilder.create().updatePlusPush(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    LoginInfoManager.getInstance().user.plusPush = b
                    LoginInfoManager.getInstance().save()
                    mAdapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    mAdapter!!.notifyDataSetChanged()
                }
            }).build().call()

        }

        binding.layoutPlusLoading.visibility = View.VISIBLE
        getCount()
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

    fun getCount() {

        val params = HashMap<String, String>()

        ApiBuilder.create().getPlusCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                if (!isAdded) {
                    return
                }
                mTotalCount = response.data
                binding.textPlusTotalCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count, FormatUtil.getMoneyType(mTotalCount.toString())))

                if (mTotalCount > 0) {
                    binding.layoutMyPlusNotExist.visibility = View.GONE
                } else {
                    binding.layoutPlusLoading.visibility = View.GONE
                    binding.layoutMyPlusNotExist.visibility = View.VISIBLE
                }

                mAdapter!!.clear()
                mPaging = 1
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()
        params["pg"] = "" + page
        mLockListView = true
        ApiBuilder.create().getPlusList(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {

            override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
                mLockListView = false
                if (!isAdded) {
                    return
                }
                binding.layoutPlusLoading.visibility = View.GONE
                if (response != null && response.datas != null) {
                    mAdapter!!.addAll(response.datas)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
                hideProgress()
                mLockListView = false

            }
        }).build().call()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_PLUS -> {
                getCount()
            }
        }
    }

    override fun getPID(): String {
        return "Main_mypage_plus list"
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                PlusFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
