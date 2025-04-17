package com.pplus.prnumberuser.apps.setting.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.setting.data.FaqAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Faq
import com.pplus.prnumberuser.core.network.model.dto.FaqGroup
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityFaqBinding
import com.pplus.prnumberuser.databinding.ItemToolbarListRowBinding
import retrofit2.Call
import java.util.*

class FAQActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityFaqBinding

    override fun getLayoutView(): View {
        binding = ActivityFaqBinding.inflate(layoutInflater)
        return binding.root
    }

    private var groupList: MutableList<FaqGroup>? = null
    private var mIsGroupClick = false
    private var mSelectGroup: FaqGroup? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: FaqAdapter? = null
    private var mLockListView = true
    private var mTotalCount = 0
    private var mPage = 0

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.fvFaq.setOnClickListener(categoryLayoutClickListener)
        binding.fvFaq.isEnabled = false
        binding.layoutFaqGroup.visibility = View.GONE
        binding.scrollFaqGroup.visibility = View.GONE
        mAdapter = FaqAdapter(this)
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerFaq.layoutManager = mLayoutManager
        binding.recyclerFaq.adapter = mAdapter
        mAdapter!!.setOnItemClickListener { position ->
            if (mAdapter!!.getItem(position) != null) {
                // Webview 상세로 이동
                val intent = Intent(this@FAQActivity, NoticeDetailActivity::class.java)
                intent.putExtra(Const.FAQ, mAdapter!!.getItem(position))
                startActivity(intent)
            }
        }
        binding.recyclerFaq.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var pastVisibleItems = 0
            var visibleItemCount = 0
            var totalItemCount = 0
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
        groupList = ArrayList()
        val totalGroup = FaqGroup()
        totalGroup.no = 0L
        totalGroup.name = getString(R.string.word_total)
        mSelectGroup = totalGroup
        groupList!!.add(totalGroup)
        getCount()
        callGroup()
    }

    var categoryLayoutClickListener = View.OnClickListener {
        mIsGroupClick = !mIsGroupClick
        if (mIsGroupClick) {
            binding.imageFaqDown.setImageResource(R.drawable.ic_setting_up)
            binding.layoutFaqGroup.removeAllViews()
            binding.layoutFaqGroup.visibility = View.VISIBLE
            binding.scrollFaqGroup.visibility = View.VISIBLE
            for (i in groupList!!.indices) {
                if (groupList!![i].no != mSelectGroup!!.no) {
                    val toolbarBinding = ItemToolbarListRowBinding.inflate(layoutInflater)
                    toolbarBinding.tvItemList.text = groupList!![i].name
                    toolbarBinding.tvItemList.setTag(toolbarBinding.tvItemList.id, groupList!![i])
                    toolbarBinding.tvItemList.setOnClickListener(categoryClickListener)
                    binding.layoutFaqGroup.addView(toolbarBinding.root)
                }
            }
        } else {
            binding.imageFaqDown.setImageResource(R.drawable.ic_setting_down)
            binding.layoutFaqGroup.visibility = View.GONE
            binding.scrollFaqGroup.visibility = View.GONE
        }
    }
    var categoryClickListener = View.OnClickListener { view ->
        mIsGroupClick = false
        binding.imageFaqDown.setImageResource(R.drawable.ic_setting_down)
        binding.layoutFaqGroup.visibility = View.GONE
        binding.scrollFaqGroup.visibility = View.GONE
        mSelectGroup = view.getTag(view.id) as FaqGroup
        binding.textFaqGroup.text = mSelectGroup!!.name
        getCount()
    }

    private fun callGroup() {
        val params = HashMap<String, String>()
        params["appKey"] = packageName
        showProgress("")
        ApiBuilder.create().getFaqGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<FaqGroup>> {
            override fun onResponse(call: Call<NewResultResponse<FaqGroup>>?,
                                    response: NewResultResponse<FaqGroup>?) {
                hideProgress()
                if (response?.datas != null) {
                    groupList!!.addAll(response.datas as MutableList)
                }
                binding.fvFaq.isEnabled = true
            }

            override fun onFailure(call: Call<NewResultResponse<FaqGroup>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<FaqGroup>?) {
                hideProgress()
            }

        }).build().call()
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["appType"] = Const.APP_TYPE
        if (mSelectGroup!!.no != 0L) {
            params["no"] = "" + mSelectGroup!!.no
        }
        params["platform"] = "aos"
        ApiBuilder.create().getFaqCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>,
                                    response: NewResultResponse<Int>) {
                if (response.data != null) {
                    mTotalCount = response.data!!
                    mPage = 1
                    mAdapter!!.clear()
                    listCall(mPage)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["appType"] = Const.APP_TYPE
        params["pg"] = "" + page
        if (mSelectGroup!!.no != 0L) {
            params["no"] = "" + mSelectGroup!!.no
        }
        params["platform"] = "aos"
        mLockListView = true
        showProgress("")
        ApiBuilder.create().getFaqList(params).setCallback(object : PplusCallback<NewResultResponse<Faq>> {
            override fun onResponse(call: Call<NewResultResponse<Faq>>, response: NewResultResponse<Faq>) {
                hideProgress()
                if(response.datas != null){
                    mLockListView = false
                    mAdapter!!.addAll(response.datas)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Faq>>, t: Throwable, response: NewResultResponse<Faq>) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_faq_en), ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}