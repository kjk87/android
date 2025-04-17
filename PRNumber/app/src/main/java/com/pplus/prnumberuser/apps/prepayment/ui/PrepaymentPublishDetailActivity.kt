package com.pplus.prnumberuser.apps.prepayment.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.prepayment.data.PrepaymentLogAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.PrepaymentLog
import com.pplus.prnumberuser.core.network.model.dto.PrepaymentPublish
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityPrepaymentPublishDetailBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class PrepaymentPublishDetailActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityPrepaymentPublishDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityPrepaymentPublishDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mPrepaymentPublish:PrepaymentPublish? = null
    var mAdapter: PrepaymentLogAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.editPrepaymentPublishDetailUsePrice.setSingleLine()

        binding.editPrepaymentPublishDetailUsePrice.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if(mPrepaymentPublish != null && p0 != null && p0.isNotEmpty()){
                    val price = p0.toString().toFloat()
                    if(price > mPrepaymentPublish!!.havePrice!!){
                        binding.editPrepaymentPublishDetailUsePrice.setText(mPrepaymentPublish!!.havePrice!!.toInt().toString())
                    }
                    val remainPrice = mPrepaymentPublish!!.havePrice!! - price
                    binding.textPrepaymentPublishDetailRemainPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(remainPrice.toInt().toString()))
                }
            }
        })

        mAdapter = PrepaymentLogAdapter()
        binding.recyclerPrepaymentPublishDetailLog.layoutManager = LinearLayoutManager(this)
        binding.recyclerPrepaymentPublishDetailLog.adapter = mAdapter

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mPrepaymentPublish = intent?.getParcelableExtra(Const.DATA)
        getPrepaymentPublish()
    }

    private fun getPrepaymentPublish(){
        val params = HashMap<String, String>()

        params["seqNo"] = mPrepaymentPublish!!.seqNo.toString()

        showProgress("")
        ApiBuilder.create().getPrepaymentPublish(params).setCallback(object : PplusCallback<NewResultResponse<PrepaymentPublish>> {
            override fun onResponse(call: Call<NewResultResponse<PrepaymentPublish>>?, response: NewResultResponse<PrepaymentPublish>?) {
                hideProgress()
                if (response?.data != null) {
                    mPrepaymentPublish = response.data
                    val page = mPrepaymentPublish!!.page
                    binding.textPrepaymentPublishDetailPageName.text = page!!.name
                    binding.textPrepaymentPublishDetailExpireDate.text = getString(R.string.format_until, mPrepaymentPublish!!.expireDate)
                    binding.textPrepaymentPublishDetailNotice.text = mPrepaymentPublish!!.notice

                    when(mPrepaymentPublish!!.status){
                        "normal"->{
                            binding.layoutPrepaymentPublishDetailUse.visibility = View.VISIBLE
                            binding.layoutPrepaymentPublishDetailComplete.visibility = View.GONE
                            binding.layoutPrepaymentPublishDetailStatus.visibility = View.GONE
                            binding.textPrepaymentPublishDetailRemainPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mPrepaymentPublish!!.havePrice!!.toInt().toString()))
                            binding.textPrepaymentPublishDetailHavePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mPrepaymentPublish!!.havePrice!!.toInt().toString()))

                            binding.textPrepaymentPublishDetailUse.setBackgroundColor(ResourceUtil.getColor(this@PrepaymentPublishDetailActivity, R.color.color_4694fb))
                            binding.textPrepaymentPublishDetailUse.text = getString(R.string.msg_use_prepayment)

                            binding.textPrepaymentPublishDetailUse.setOnClickListener {

                                val usePrice = binding.editPrepaymentPublishDetailUsePrice.text.toString()
                                if(StringUtils.isEmpty(usePrice)){
                                    showAlert(R.string.msg_input_use_price)
                                    return@setOnClickListener
                                }
                                if(usePrice.toInt() > mPrepaymentPublish!!.havePrice!!.toInt()){
                                    showAlert(R.string.msg_exceed_use_price)
                                    return@setOnClickListener
                                }

                                binding.editPrepaymentPublishDetailUsePrice.setText("")

                                val params = HashMap<String, String>()
                                params["prepaymentPublishSeqNo"] = mPrepaymentPublish!!.seqNo.toString()
                                params["usePrice"] = usePrice
                                showProgress("")
                                ApiBuilder.create().prepaymentUse(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                        hideProgress()
                                        val intent = Intent(this@PrepaymentPublishDetailActivity, PrepaymentUseActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        intent.putExtra(Const.DATA, mPrepaymentPublish)
                                        intent.putExtra(Const.PRICE, usePrice.toInt())
                                        launcher.launch(intent)
                                    }

                                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                        hideProgress()
                                        if (response?.resultCode == 662) {
                                            showAlert(R.string.msg_exceed_use_price)
                                        }else if (response?.resultCode == 505) {
                                            showAlert(R.string.msg_not_permission_user)
                                        }else if (response?.resultCode == 661) {
                                            showAlert(R.string.msg_exist_request_log)
                                        }
                                    }
                                }).build().call()


                            }
                        }
                        "completed"->{
                            binding.layoutPrepaymentPublishDetailUse.visibility = View.GONE
                            binding.layoutPrepaymentPublishDetailComplete.visibility = View.VISIBLE
                            binding.layoutPrepaymentPublishDetailStatus.visibility = View.VISIBLE
                            binding.textPrepaymentPublishDetailUsePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mPrepaymentPublish!!.usePrice!!.toInt().toString()))
                            binding.textPrepaymentPublishDetailUnUsePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mPrepaymentPublish!!.havePrice!!.toInt().toString()))
                            binding.textRepaymentPublishDetailStatus.text = getString(R.string.word_use_complete)
                            binding.textRepaymentPublishDetailStatusDate.text = mPrepaymentPublish!!.completedDatetime

                            binding.textPrepaymentPublishDetailUse.setBackgroundColor(ResourceUtil.getColor(this@PrepaymentPublishDetailActivity, R.color.color_a9b0b7))
                            binding.textPrepaymentPublishDetailUse.text = getString(R.string.word_use_complete)

                            binding.textPrepaymentPublishDetailUse.setOnClickListener {

                            }
                        }
                        "expired"->{
                            binding.layoutPrepaymentPublishDetailUse.visibility = View.GONE
                            binding.layoutPrepaymentPublishDetailComplete.visibility = View.VISIBLE
                            binding.layoutPrepaymentPublishDetailStatus.visibility = View.VISIBLE
                            binding.textPrepaymentPublishDetailUsePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mPrepaymentPublish!!.usePrice!!.toInt().toString()))
                            binding.textPrepaymentPublishDetailUnUsePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mPrepaymentPublish!!.havePrice!!.toInt().toString()))
                            binding.textRepaymentPublishDetailStatus.text = getString(R.string.word_expire)
                            binding.textRepaymentPublishDetailStatusDate.text = mPrepaymentPublish!!.expireDate

                            binding.textPrepaymentPublishDetailUse.setBackgroundColor(ResourceUtil.getColor(this@PrepaymentPublishDetailActivity, R.color.color_a9b0b7))
                            binding.textPrepaymentPublishDetailUse.text = getString(R.string.word_expire)

                            binding.textPrepaymentPublishDetailUse.setOnClickListener {

                            }
                        }
                    }

                    setPrepayment(mPrepaymentPublish!!)
                    getPrepaymentLogList()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<PrepaymentPublish>>?, t: Throwable?, response: NewResultResponse<PrepaymentPublish>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setPrepayment(item: PrepaymentPublish){

        binding.itemMainPrepayment.textMainPrepaymentPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price!!.toInt().toString()))
        binding.itemMainPrepayment.textMainPrepaymentTotalPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((item.price!! + item.addPrice!!).toInt().toString()))
        val discount = (item.addPrice!! / item.price!!)*100f
        binding.itemMainPrepayment.textMainPrepaymentDiscount.text = PplusCommonUtil.fromHtml(getString(R.string.format_add_price_discount, discount.toInt().toString()))
    }

    private fun getPrepaymentLogList(){
        val params = HashMap<String, String>()

        params["prepaymentPublishSeqNo"] = mPrepaymentPublish!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getPrepaymentLogList(params).setCallback(object : PplusCallback<NewResultResponse<PrepaymentLog>> {
            override fun onResponse(call: Call<NewResultResponse<PrepaymentLog>>?, response: NewResultResponse<PrepaymentLog>?) {
                hideProgress()
                if (response?.datas != null && response.datas.isNotEmpty()) {
                    binding.layoutPrepaymentPublishDetailLog.visibility = View.VISIBLE
                    val logList = response.datas!!
                    mAdapter!!.setDataList(logList)
                }else{
                    binding.layoutPrepaymentPublishDetailLog.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<PrepaymentLog>>?, t: Throwable?, response: NewResultResponse<PrepaymentLog>?) {
                hideProgress()
            }
        }).build().call()
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getPrepaymentPublish()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_retention_prepayment), ToolbarOption.ToolbarMenu.RIGHT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}