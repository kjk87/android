package com.pplus.prnumberbiz.apps.signin.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.common.WebViewActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Terms
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_add_terms.*
import kotlinx.android.synthetic.main.item_terms.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.ArrayList
import java.util.HashMap

class AddTermsActivity : BaseActivity(), ImplToolbar, CompoundButton.OnCheckedChangeListener {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_add_terms
    }

    private var mTermsList: ArrayList<Terms>? = null
    private var mTermsAgreeMap: MutableMap<Long, Boolean>? = null
    private var mCheckBoxList: MutableList<CheckBox>? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        ApiBuilder.create().getNotSignedActiveTermsAll(packageName).setCallback(object : PplusCallback<NewResultResponse<Terms>> {

            override fun onResponse(call: Call<NewResultResponse<Terms>>, response: NewResultResponse<Terms>) {

                mTermsList = response.datas as ArrayList<Terms>
                if (mTermsList != null && mTermsList!!.isNotEmpty()) {
                    mTermsAgreeMap = HashMap()
                    layout_add_terms.removeAllViews()
                    mCheckBoxList = ArrayList<CheckBox>()
                    for (i in mTermsList!!.indices) {
                        val terms = mTermsList!![i]
                        mTermsAgreeMap!![terms.no] = false
                        val viewTerms = LayoutInflater.from(this@AddTermsActivity).inflate(R.layout.item_terms, LinearLayout(this@AddTermsActivity))
                        if (i != 0) {
                            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            layoutParams.setMargins(0, resources.getDimensionPixelSize(R.dimen.height_50), 0, 0)
                            viewTerms.layoutParams = layoutParams
                        }
                        val checkTerms = viewTerms.findViewById(R.id.check_terms_agree) as CheckBox
                        checkTerms.text = terms.subject
                        checkTerms.setOnCheckedChangeListener { buttonView, isChecked ->
                            var isAll = true
                            for (checkBox in mCheckBoxList!!) {
                                if (!checkBox.isChecked) {
                                    isAll = false
                                    break
                                }
                            }
                            check_add_terms_total_agree.setOnCheckedChangeListener(null)
                            check_add_terms_total_agree.isChecked = isAll
                            check_add_terms_total_agree.setOnCheckedChangeListener(this@AddTermsActivity)

                            mTermsAgreeMap!![terms.no] = isChecked
                        }
                        mCheckBoxList!!.add(checkTerms)

                        viewTerms.image_terms_story.setOnClickListener{
                            val intent = Intent(this@AddTermsActivity, WebViewActivity::class.java)
                            intent.putExtra(Const.TITLE, terms.subject)
                            intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true)
                            intent.putExtra(Const.WEBVIEW_URL, terms.url)
                            intent.putExtra(Const.TERMS_LIST, mTermsList)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        layout_add_terms.addView(viewTerms)
                    }
                } else {
                    check_add_terms_total_agree.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Terms>>, t: Throwable, response: NewResultResponse<Terms>) {

            }
        }).build().call()

        check_add_terms_total_agree.setOnCheckedChangeListener(this)
    }

    private fun updateTerms() {

        if (mTermsList != null && mTermsList!!.isNotEmpty()) {
            for (i in mTermsList!!.indices) {
                if (mTermsList!![i].isCompulsory && !mTermsAgreeMap!![mTermsList!![i].no]!!) {
                    showAlert(R.string.msg_agree_terms)
                    return
                }
            }

            val builder = StringBuilder()
            for (entry in mTermsAgreeMap!!.entries) {
                if (entry.value) {
                    builder.append(entry.key)
                    builder.append(",")
                }
            }

            if (builder.isNotEmpty()) {
                builder.setLength(builder.length - 1)
                val params = HashMap<String, String>()
                params["termsNo"] = builder.toString()
                showProgress("")
                ApiBuilder.create().agreeTermsList(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                    override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                        hideProgress()
                        setResult(RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                        hideProgress()
                        setResult(RESULT_OK)
                        finish()
                    }
                }).build().call()
            } else {
                setResult(RESULT_OK)
                finish()
            }
        } else {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (mCheckBoxList != null) {
            for (checkBox in mCheckBoxList!!) {
                checkBox.isChecked = isChecked
            }
        } else {
            check_add_terms_total_agree.isChecked = !isChecked
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_confirm))

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    updateTerms()
                }
            }
        }
    }
}
