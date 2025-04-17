package com.pplus.prnumberbiz.apps.menu.ui

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.menu.data.CategoryMenuAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.dto.PageGoodsCategory
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import kotlinx.android.synthetic.main.fragment_category_menu.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class CategoryMenuFragment : BaseFragment<MenuConfigActivity>() {

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_category_menu
    }

    override fun initializeView(container: View?) {

    }

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: CategoryMenuAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var category: PageGoodsCategory? = null


    override fun init() {

        mLayoutManager = LinearLayoutManager(activity)
        recycler_category_menu.layoutManager = mLayoutManager
        mAdapter = CategoryMenuAdapter(activity!!)
        recycler_category_menu.adapter = mAdapter
        recycler_category_menu.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_180))

        recycler_category_menu.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : CategoryMenuAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (parentActivity.mKey == Const.SELECT) {
                    val goods = mAdapter!!.getItem(position)
                    val data = Intent()
                    data.putExtra(Const.GOODS, goods)
                    parentActivity.setResult(RESULT_OK, data)
                    parentActivity.finish()
                }
            }

            override fun onItemChanged() {
                parentActivity.refreshChildFragment()
//                mPaging = 0
//                listCall(mPaging)
            }
        })

        if(StringUtils.isNotEmpty(parentActivity.mKey)){
            mAdapter!!.mKey = parentActivity.mKey
        }

        if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
            text_category_menu_not_exist.setText(R.string.msg_not_exist_category_menu)
        } else {
            text_category_menu_not_exist.setText(R.string.msg_not_exist_category_menu)
        }

        setData()
    }

    fun setData() {
        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
        params["status"] = EnumData.GoodsStatus.ing.status.toString()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["isHotdeal"] = "false"
        params["isPlus"] = "false"
        if (category!!.goodsCategory != null) {
            params["categorySeqNo"] = category!!.goodsCategory!!.seqNo.toString()
        }

        if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
            params["type"] = "0"
        } else {
            params["type"] = "1"
        }

        params["page"] = page.toString()
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        showProgress("")
        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response != null) {

                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()

                        if (category!!.goodsCategory != null) {
                            if (mTotalCount == 0) {
                                layout_category_menu_not_exist.visibility = View.VISIBLE
                            } else {
                                layout_category_menu_not_exist.visibility = View.GONE
                            }
                        } else {
                            parentActivity.setNotExist(mTotalCount == 0)
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
                outRect.bottom = mItemOffset
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_REG -> {
                if (resultCode == RESULT_OK) {
                    setData()
                }
            }
        }
    }

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(Const.DATA)
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(category: PageGoodsCategory) =
                CategoryMenuFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Const.DATA, category)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
