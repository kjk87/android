package com.pplus.luckybol.apps.shippingsite.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.shippingsite.data.SearchAddressAdapter
import com.pplus.luckybol.core.network.ApiController
import com.pplus.luckybol.core.network.model.dto.ResultAddress
import com.pplus.luckybol.databinding.ActivitySearchAddressBinding
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SearchAddressActivity : BaseActivity(), ImplToolbar {

    private var mLockListView = true
    private var mTotalCount = 0
    private var mPage = 0
    private var mSearch: String? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: SearchAddressAdapter? = null
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivitySearchAddressBinding

    override fun getLayoutView(): View {
        binding = ActivitySearchAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.editSearchAddress.setSingleLine()
        binding.editSearchAddress.imeOptions = EditorInfo.IME_ACTION_SEARCH
        binding.editSearchAddress.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    mSearch = binding.editSearchAddress.text.toString().trim()
                    if (StringUtils.isNotEmpty(mSearch)) {
                        mPage = 1
                        search(mPage)
                    } else {
                        showAlert(R.string.msg_input_searchWord)
                    }
                }
            }

            true
        }
        binding.imageSearchAddress.setOnClickListener {
            mSearch = binding.editSearchAddress.text.toString().trim()
            if (StringUtils.isEmpty(mSearch)) {
                showAlert(R.string.msg_input_searchWord)
                return@setOnClickListener
            }
            mPage = 1
            search(mPage)
        }

        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerSearchAddress.layoutManager = mLayoutManager
        mAdapter = SearchAddressAdapter()
        binding.recyclerSearchAddress.adapter = mAdapter

        binding.recyclerSearchAddress.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                        search(mPage)
                    }
                }
            }
        })

        mAdapter!!.listener = object : SearchAddressAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val juso = mAdapter!!.getItem(position)

                val data = Intent()
                data.putExtra(Const.ADDRESS, juso)
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }

    private fun search(page: Int) {

        if (StringUtils.isEmpty(mSearch)) {
            return
        }

        val params = HashMap<String, String>()
        params["pg"] = "" + page
        params["search"] = mSearch!!
        showProgress("")
        mLockListView = true
        ApiController.pRNumberApi.requestSearchAddress(params).enqueue(object : Callback<ResultAddress> {

            override fun onResponse(call: Call<ResultAddress>, response: Response<ResultAddress>) {
                mLockListView = false
                hideProgress()
                LogUtil.e(LOG_TAG, "result : {}", response.body()!!.toString())
                val address = response.body()!!.results
                mTotalCount = address.common.totalCount!!
                if (mTotalCount == 0) {
                    binding.textSearchNotExist.visibility = View.VISIBLE
                } else {
                    binding.textSearchNotExist.visibility = View.GONE
                }
                if (mPage == 1) {
                    mAdapter!!.clear()
                }

                mAdapter!!.addAll(address.juso)
            }

            override fun onFailure(call: Call<ResultAddress>, t: Throwable) {
                mLockListView = false
                hideProgress()
            }
        })
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_find_address), ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }

    }
}
