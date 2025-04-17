package com.pplus.luckybol.apps.signin.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.common.WebViewActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Terms
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityAddInfoBinding
import com.pplus.luckybol.databinding.ItemTermsBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class AddInfoActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener, ImplToolbar {

    private var mCheckBoxList: MutableList<CheckBox>? = null
    private var mTermsList: ArrayList<Terms>? = null
    private var mTermsAgreeMap: MutableMap<Long, Boolean>? = null
    private var mIsCheckNickName: Boolean = false
    private var mNickName: String? = null

    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAddInfoBinding

    override fun getLayoutView(): View {
        binding = ActivityAddInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.textAddInfoMobile.text = LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE+"##", "")

        binding.checkAddInfoTotalAgree.setOnCheckedChangeListener(this)

        binding.textAddInfoNickNameDoubleCheck.setOnClickListener {
            val nickname = binding.editAddInfoNickName.text.toString().trim()
            if (StringUtils.isEmpty(nickname)) {
                showAlert(R.string.msg_input_nickName)
                return@setOnClickListener
            }

            if (nickname.length < 2) {
                showAlert(getString(R.string.msg_input_nickname_over2))
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["nickname"] = nickname
            params["appType"] = Const.APP_TYPE
            ApiBuilder.create().existsUser(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                    hideProgress()
                    mIsCheckNickName = false
                    showAlert(getString(R.string.msg_duplicate_nickname))
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                    hideProgress()
                    mIsCheckNickName = true
                    showAlert(getString(R.string.msg_enable_use))
                    mNickName = binding.editAddInfoNickName.text.toString().trim()
                }
            }).build().call()
        }

        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.nickname)) {
            binding.textAddInfoNicknameTitle.visibility = View.GONE
            binding.layoutAddInfoNickname.visibility = View.GONE
        }

        ApiBuilder.create().getNotSignedActiveTermsAll(Const.APP_TYPE).setCallback(object : PplusCallback<NewResultResponse<Terms>> {

            override fun onResponse(call: Call<NewResultResponse<Terms>>, response: NewResultResponse<Terms>) {

                mTermsList = response.datas as ArrayList<Terms>
                if (mTermsList != null && mTermsList!!.size > 0) {
                    mTermsAgreeMap = HashMap()
                    binding.layoutAddInfoTerms.removeAllViews()
                    mCheckBoxList = ArrayList()
                    for (i in mTermsList!!.indices) {
                        val terms = mTermsList!![i]
                        mTermsAgreeMap!![terms.no!!] = false
                        val termsBinding = ItemTermsBinding.inflate(layoutInflater, LinearLayout(this@AddInfoActivity), false)
                        if (i != 0) {
                            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            layoutParams.setMargins(0, resources.getDimensionPixelSize(R.dimen.height_50), 0, 0)
                            termsBinding.root.layoutParams = layoutParams
                        }
                        val checkTerms = termsBinding.checkTermsAgree
                        checkTerms.text = terms.subject
                        checkTerms.setOnCheckedChangeListener { buttonView, isChecked ->
                            var isAll = true
                            for (checkBox in mCheckBoxList!!) {
                                if (!checkBox.isChecked) {
                                    isAll = false
                                    break
                                }
                            }
                            binding.checkAddInfoTotalAgree.setOnCheckedChangeListener(null)
                            binding.checkAddInfoTotalAgree.isChecked = isAll
                            binding.checkAddInfoTotalAgree.setOnCheckedChangeListener(this@AddInfoActivity)

                            mTermsAgreeMap!![terms.no!!] = isChecked
                        }
                        mCheckBoxList!!.add(checkTerms)

                        termsBinding.imageTermsStory.setOnClickListener {
                            val intent = Intent(this@AddInfoActivity, WebViewActivity::class.java)
                            intent.putExtra(Const.TITLE, terms.subject)
                            intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true)
                            intent.putExtra(Const.WEBVIEW_URL, terms.url)
                            intent.putExtra(Const.TERMS_LIST, mTermsList)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        binding.layoutAddInfoTerms.addView(termsBinding.root)
                    }
                } else {
                    binding.checkAddInfoTotalAgree.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Terms>>, t: Throwable, response: NewResultResponse<Terms>) {

            }
        }).build().call()

        binding.textAddInfoComplete.setOnClickListener {
            if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.nickname)) {
                updateTerms()
            } else {
                val nickname = binding.editAddInfoNickName.text.toString().trim()
                if (!mIsCheckNickName || mNickName != nickname) {
                    binding.scrollAddInfo.smoothScrollTo(0, binding.editAddInfoNickName.top)
                    binding.editAddInfoNickName.requestFocus()
                    showAlert(R.string.msg_check_duplication_nickName)
                    return@setOnClickListener
                }

                updateNickname(nickname)
            }
        }
    }

    private fun updateNickname(nickname: String) {

        val params = HashMap<String, String>()
        params["nickname"] = nickname
        showProgress("")
        ApiBuilder.create().updateNickname(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()
                LoginInfoManager.getInstance().user.nickname = nickname
                LoginInfoManager.getInstance().save()
                updateTerms()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

            }
        }).build().call()
    }

    private fun updateTerms() {

        if (mTermsList != null && mTermsList!!.size > 0) {
            for (i in mTermsList!!.indices) {
                if (mTermsList!![i].isCompulsory && !(mTermsAgreeMap!![mTermsList!![i].no])!!) {
                    showAlert(R.string.msg_agree_terms)
                    return
                }
            }

            val builder = StringBuilder()
            for ((key, value) in mTermsAgreeMap!!) {
                if (value) {
                    builder.append(key)
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
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                        hideProgress()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }).build().call()
            } else {
                setResult(Activity.RESULT_OK)
                finish()
            }
        } else {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {

        if (mCheckBoxList != null) {
            for (checkBox in mCheckBoxList!!) {
                checkBox.isChecked = b
            }
        } else {
            binding.checkAddInfoTotalAgree.isChecked = !b
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.RIGHT)

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
