package com.pplus.prnumberuser.apps.page.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.dto.VisitLog
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityPageFirstBenefitBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class PageFirstBenefitActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String? {
        return ""
    }

    var mPage: Page2? = null

    private lateinit var binding: ActivityPageFirstBenefitBinding

    override fun getLayoutView(): View {
        binding = ActivityPageFirstBenefitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.DATA)

        getPage()
    }

    private fun getPage() {
        val params = HashMap<String, String>()
        params["seqNo"] = mPage!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getPage2(params).setCallback(object : PplusCallback<NewResultResponse<Page2>> {
            override fun onResponse(call: Call<NewResultResponse<Page2>>?, response: NewResultResponse<Page2>?) {
                hideProgress()
                if (response?.data != null) {
                    mPage = response.data
                    Glide.with(this@PageFirstBenefitActivity).load(mPage!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imagePageFirstBenefitThumbnail)
                    binding.textPageFirstBenefitPageName.text = mPage!!.name

                    if (mPage!!.visitBenefitType == "none" || StringUtils.isEmpty(mPage!!.visitBenefit)) {
                        binding.layoutPageFirstBenefit.visibility = View.GONE
                        binding.textPageFirstBenefitStop.visibility = View.VISIBLE
                        binding.textPageFirstBenefitDate.visibility = View.GONE

                        binding.textPageFirstBenefitReceive.text = getString(R.string.word_confirm)
                        binding.textPageFirstBenefitReceive.setOnClickListener {
                            setResult(RESULT_OK)
                            finish()
                        }
                    } else {
                        binding.layoutPageFirstBenefit.visibility = View.VISIBLE
                        binding.textPageFirstBenefitStop.visibility = View.GONE
                        binding.textPageFirstBenefitDate.visibility = View.VISIBLE
                        when (mPage!!.visitBenefitType) {
                            "discount" -> {
                                binding.textPageFirstBenefit.text = getString(R.string.format_discount_money_unit, FormatUtil.getMoneyType(mPage!!.visitBenefit))
                            }
                            "free" -> {
                                binding.textPageFirstBenefit.text = mPage!!.visitBenefit
                            }
                        }

                        getVisitLog()
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page2>>?, t: Throwable?, response: NewResultResponse<Page2>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getVisitLog() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getVisitLog(params).setCallback(object : PplusCallback<NewResultResponse<VisitLog>> {
            override fun onResponse(call: Call<NewResultResponse<VisitLog>>?, response: NewResultResponse<VisitLog>?) {
                hideProgress()
                if (response?.data != null) {
                    binding.textPageFirstBenefitDate.text = getString(R.string.format_first_benefit_date, response.data!!.regDatetime)
                    binding.textPageFirstBenefitDate.setTextColor(ResourceUtil.getColor(this@PageFirstBenefitActivity, R.color.color_4694fb))
                    binding.textPageFirstBenefitReceive.text = getString(R.string.word_confirm)
                    binding.textPageFirstBenefitReceive.setOnClickListener {
                        setResult(RESULT_OK)
                        finish()
                    }
                } else {
                    binding.textPageFirstBenefitDate.setTextColor(ResourceUtil.getColor(this@PageFirstBenefitActivity, R.color.color_8c969f))
                    binding.textPageFirstBenefitDate.text = getString(R.string.msg_first_benefit_desc)
                    binding.textPageFirstBenefitReceive.text = getString(R.string.msg_receive_first_benefit)
                    binding.textPageFirstBenefitReceive.setOnClickListener {

                        val params = HashMap<String, String>()
                        params["pageSeqNo"] = mPage!!.seqNo.toString()
                        showProgress("")
                        ApiBuilder.create().visitLogReceive(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                hideProgress()
                                val intent = Intent(this@PageFirstBenefitActivity, PageFirstBenefitReceiveActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                intent.putExtra(Const.DATA, mPage)
                                useLauncher.launch(intent)
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                hideProgress()
                                if (response?.resultCode == 662) {
                                    showAlert(R.string.msg_already_receive_first_benefit)
                                }else if (response?.resultCode == 661) {
                                    showAlert(R.string.msg_exist_request_log)
                                }
                            }
                        }).build().call()
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<VisitLog>>?, t: Throwable?, response: NewResultResponse<VisitLog>?) {
                hideProgress()
            }
        }).build().call()
    }

    val useLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getPage()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_first_benefit), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}