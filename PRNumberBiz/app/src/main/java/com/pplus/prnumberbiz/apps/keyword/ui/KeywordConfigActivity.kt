package com.pplus.prnumberbiz.apps.keyword.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberbiz.apps.main.data.HashTagAdapter
import com.pplus.prnumberbiz.apps.main.data.HashTagValueAdapter
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.HashTag
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_keyword_config.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class KeywordConfigActivity : BaseActivity(), ImplToolbar {

    private var mPage: Page? = null

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_keyword_config
    }

    private var mAdapter: HashTagAdapter? = null
    private var mMyAdapter: HashTagValueAdapter? = null
    var mMyTagList: MutableList<String>? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = LoginInfoManager.getInstance().user.page

        recycler_keyword_config_my_keyword.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        recycler_keyword_config.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mMyAdapter = HashTagValueAdapter(this, true)
        recycler_keyword_config_my_keyword.adapter = mMyAdapter
        mAdapter = HashTagAdapter(this)
        recycler_keyword_config.adapter = mAdapter
        mAdapter!!.setOnItemListener(object : HashTagAdapter.OnItemListener {
            override fun onItemSelect(tag: String) {
                if (mMyTagList == null) {
                    mMyTagList = arrayListOf()
                }

                if (mMyTagList!!.contains(tag)) {
                    mMyTagList!!.remove(tag)
                } else {

                    if (mMyTagList!!.size < 5) {
                        mMyTagList!!.add(tag)
                    } else {
                        ToastUtil.show(this@KeywordConfigActivity, R.string.msg_select_keyword_until_5)
                        return
                    }
                }

                postMyHashTag()
            }
        })

        recycler_keyword_config.addItemDecoration(BottomItemOffsetDecoration(this, R.dimen.height_120))

        mMyAdapter!!.setOnItemClickListener(object : HashTagValueAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                mMyTagList!!.removeAt(position)
                postMyHashTag()
            }
        })

        layout_keyword_config_search.setOnClickListener {
            val intent = Intent(this, HashTagSearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SEARCH)
        }

        mMyTagList = ArrayList<String>()
        if (StringUtils.isNotEmpty(mPage!!.hashtag)) {

            text_keyword_config_my_keyword_not_exist.visibility = View.GONE
            layout_keyword_config_my_keyword.visibility = View.VISIBLE

            mMyTagList!!.addAll(mPage!!.hashtag!!.split(",").toMutableList())

            text_keyword_config_my_keyword_title.text = PplusCommonUtil.fromHtml(getString(R.string.html_my_keyword_title, mMyTagList!!.size.toString()))
        } else {
            text_keyword_config_my_keyword_not_exist.visibility = View.VISIBLE
            layout_keyword_config_my_keyword.visibility = View.GONE
        }

        mMyAdapter!!.setDataList(mMyTagList!!)
        mAdapter!!.mMyTagList = mMyTagList
        getHashTagList()
    }

    private fun getHashTagList() {

        showProgress("")
        ApiBuilder.create().hashTagList.setCallback(object : PplusCallback<NewResultResponse<HashTag>> {
            override fun onResponse(call: Call<NewResultResponse<HashTag>>?, response: NewResultResponse<HashTag>?) {
                hideProgress()
                if (response != null) {
                    mAdapter!!.setDataList(response.datas)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<HashTag>>?, t: Throwable?, response: NewResultResponse<HashTag>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun postMyHashTag() {
        // 검색키워드
        var tag = ""
        for (i in 0 until mMyTagList!!.size) {
            if (i != 0) {
                tag += ","
            }
            tag += mMyTagList!![i]
        }
        mPage!!.hashtag = tag

        showProgress("")
        ApiBuilder.create().updatePage(mPage).setCallback(object : PplusCallback<NewResultResponse<Page>> {
            override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                LoginInfoManager.getInstance().user.page = response!!.data
                LoginInfoManager.getInstance().save()
                hideProgress()

                if (mMyTagList!!.isNotEmpty()) {
                    text_keyword_config_my_keyword_not_exist.visibility = View.GONE
                    layout_keyword_config_my_keyword.visibility = View.VISIBLE
                } else {
                    text_keyword_config_my_keyword_not_exist.visibility = View.VISIBLE
                    layout_keyword_config_my_keyword.visibility = View.GONE
                }

                text_keyword_config_my_keyword_title.text = PplusCommonUtil.fromHtml(getString(R.string.html_my_keyword_title, mMyTagList!!.size.toString()))
                mMyAdapter!!.setDataList(mMyTagList!!)
                mAdapter!!.mMyTagList = mMyTagList
                mAdapter!!.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_SEARCH -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val tag = data.getStringExtra(Const.DATA)
                        if (mMyTagList!!.size < 5) {
                            mMyTagList!!.add(tag)
                        } else {
                            ToastUtil.show(this, R.string.msg_select_keyword_until_5)
                            return
                        }
                        postMyHashTag()

                    }
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_keyword_setting), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
