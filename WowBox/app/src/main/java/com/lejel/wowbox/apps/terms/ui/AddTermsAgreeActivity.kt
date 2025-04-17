package com.lejel.wowbox.apps.terms.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Terms
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.ActivityAddTermsAgreeBinding
import com.lejel.wowbox.databinding.ItemTermsBinding
import retrofit2.Call

class AddTermsAgreeActivity : BaseActivity() {

    private lateinit var binding: ActivityAddTermsAgreeBinding

    override fun getLayoutView(): View {
        binding = ActivityAddTermsAgreeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTermsList: MutableList<Terms>? = null
    private var mTermsAgreeMap: MutableMap<Long, Boolean>? = null
    private var mCheckBoxList: MutableList<CheckBox>? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.checkAddTermsAgreeTotalAgree.setOnCheckedChangeListener(checkListener)

        getNotSignedTermsList()

        binding.textAddTermsAgree.setOnClickListener {
            for (i in mTermsList!!.indices) {
                if (mTermsList!![i].compulsory!! && (!mTermsAgreeMap!![mTermsList!![i].seqNo]!!)) {
                    showAlert(R.string.msg_agree_terms)
                    return@setOnClickListener
                }
            }

            val termsList = arrayListOf<Long>()
            for ((key, value) in mTermsAgreeMap!!) {
                if (value) {
                    termsList.add(key)
                }
            }

            var termsNo = ""

            for (i in 0 until termsList.size) {
                termsNo += termsList[i]
                if(i < termsList.size -1){
                    termsNo += ","
                }
            }

            val params = HashMap<String, String>()
            params["termsNo"] = termsNo
            showProgress("")
            ApiBuilder.create().agreeAddTerms(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    if(response?.code == 587){
                        showAlert(R.string.msg_agree_terms)
                    }
                }
            }).build().call()
        }
    }

    private fun getNotSignedTermsList(){
        showProgress("")
        ApiBuilder.create().getNotSignedList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Terms>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null) {
                    mTermsList = response.result!!.list as MutableList<Terms>
                    mTermsAgreeMap = HashMap()
                    binding.layoutAddTermsAgree.removeAllViews()

                    mCheckBoxList = ArrayList()
                    for (i in mTermsList!!.indices) {
                        val terms = mTermsList!![i]
                        mTermsAgreeMap!![terms.seqNo!!] = false
                        val termsBinding = ItemTermsBinding.inflate(layoutInflater, LinearLayout(this@AddTermsAgreeActivity), false)
                        if (i != 0) {
                            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            layoutParams.setMargins(0, resources.getDimensionPixelSize(R.dimen.height_50), 0, 0)
                            termsBinding.root.layoutParams = layoutParams
                        }
                        val checkTerms = termsBinding.checkTermsAgree
                        checkTerms.text = terms.title
                        checkTerms.setOnCheckedChangeListener { buttonView, isChecked ->
                            var isAll = true
                            for (checkBox in mCheckBoxList!!) {
                                if (!checkBox.isChecked) {
                                    isAll = false
                                    break
                                }
                            }

                            binding.checkAddTermsAgreeTotalAgree.setOnCheckedChangeListener(null)
                            binding.checkAddTermsAgreeTotalAgree.isChecked = isAll
                            binding.checkAddTermsAgreeTotalAgree.setOnCheckedChangeListener(checkListener)

                            mTermsAgreeMap!![terms.seqNo!!] = isChecked
                        }
                        mCheckBoxList!!.add(checkTerms)

                        termsBinding.textTermsContents.setOnClickListener {
                            val intent = Intent(this@AddTermsAgreeActivity, TermsActivity::class.java)
                            intent.putExtra(Const.DATA, terms)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        binding.layoutAddTermsAgree.addView(termsBinding.root)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
            }
        }).build().call()
    }

    val checkListener = CompoundButton.OnCheckedChangeListener { compoundButton, b ->
        if (mCheckBoxList != null) {
            for (checkBox in mCheckBoxList!!) {
                checkBox.isChecked = b
            }
        } else {
            binding.checkAddTermsAgreeTotalAgree.isChecked = !b
        }
    }
}