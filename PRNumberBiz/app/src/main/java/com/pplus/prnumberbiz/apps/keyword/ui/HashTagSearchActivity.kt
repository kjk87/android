package com.pplus.prnumberbiz.apps.keyword.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.main.data.HashTagSearchAdapter
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_hash_tag_search.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.HashMap

class HashTagSearchActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_hash_tag_search
    }

    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    private var mAdapter: HashTagSearchAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler_hash_tag_search.layoutManager = mLayoutManager
        mAdapter = HashTagSearchAdapter(this)
        recycler_hash_tag_search.adapter = mAdapter

        edit_hash_tag_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if(s.toString().length >= 2){
                    listCall(s.toString())
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        mAdapter!!.setOnItemClickListener(object : HashTagSearchAdapter.OnItemClickListener{
            override fun onItemClick(word: String) {
                edit_hash_tag_search.setText(word)
            }
        })

        text_hash_tag_reg.setOnClickListener {
            val tag = edit_hash_tag_search.text.toString().trim()

            if(StringUtils.isEmpty(tag)){
                showAlert(R.string.msg_input_keyword)
                return@setOnClickListener
            }

            if(tag.length < 2){
                showAlert(R.string.msg_input_keyword_over_2)
                return@setOnClickListener
            }

            val page = LoginInfoManager.getInstance().user.page!!

            if (StringUtils.isNotEmpty(page.hashtag) && page.hashtag!!.split(",").contains(tag)) {
                showAlert(R.string.msg_already_registed_keyword)
                return@setOnClickListener
            }

            val data = Intent()
            data.putExtra(Const.DATA, tag)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        listCall("")
    }

    private fun listCall(search: String) {

        val params = HashMap<String, String>()
        params["search"] = search
        params["sz"] = "100"
        ApiBuilder.create().getHashTagSearch(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                if (response != null) {
                    mAdapter!!.setDataList(response.datas)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {

            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_keyword_setting), ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }

}
