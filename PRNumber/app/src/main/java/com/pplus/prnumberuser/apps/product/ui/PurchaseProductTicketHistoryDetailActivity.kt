package com.pplus.prnumberuser.apps.product.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.product.data.PurchaseProductShipAdapter
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.PurchaseProduct
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityPurchaseProductTicketHistoryDetailBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PurchaseProductTicketHistoryDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPurchaseProductTicketHistoryDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityPurchaseProductTicketHistoryDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var mPurchaseProduct: PurchaseProduct? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mPurchaseProduct = intent.getParcelableExtra(Const.DATA)

        getPurchaseProduct()
    }

    private fun getPurchaseProduct() {
        val params = HashMap<String, String>()
        params["seqNo"] = mPurchaseProduct!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getPurchaseProduct(params).setCallback(object : PplusCallback<NewResultResponse<PurchaseProduct>> {
            override fun onResponse(call: Call<NewResultResponse<PurchaseProduct>>?, response: NewResultResponse<PurchaseProduct>?) {
                hideProgress()
                if (response?.data != null) {
                    mPurchaseProduct = response.data
                    setData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<PurchaseProduct>>?, t: Throwable?, response: NewResultResponse<PurchaseProduct>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setData() {

        if(mPurchaseProduct!!.page != null){
            if (StringUtils.isNotEmpty(mPurchaseProduct!!.page!!.thumbnail)) {
                Glide.with(this).load(mPurchaseProduct!!.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_page_profile_circle_default).error(R.drawable.img_page_profile_circle_default)).into(binding.imagePurchaseProductTicketHistoryDetailPageImage)
            } else {
                binding.imagePurchaseProductTicketHistoryDetailPageImage.setImageResource(R.drawable.img_page_profile_circle_default)
            }

            binding.textPurchaseProductTicketHistoryDetailPageName.text = mPurchaseProduct!!.page!!.name
        }


        binding.textPurchaseProductTicketHistoryDetailUse.setOnClickListener {

            if (StringUtils.isNotEmpty(mPurchaseProduct!!.startTime) && StringUtils.isNotEmpty(mPurchaseProduct!!.endTime)) {
                val startMin = (mPurchaseProduct!!.startTime!!.split(":")[0].toInt() * 60) + mPurchaseProduct!!.startTime!!.split(":")[1].toInt()
                val endMin = (mPurchaseProduct!!.endTime!!.split(":")[0].toInt() * 60) + mPurchaseProduct!!.endTime!!.split(":")[1].toInt()
                val cal = Calendar.getInstance()
                val currentMin = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
                if(currentMin < startMin || currentMin > endMin){
                    showAlert(R.string.msg_impossible_use_time)
                    return@setOnClickListener
                }
            }
            val intent = Intent(this, CheckAuthCodeActivity::class.java)
            intent.putExtra(Const.TYPE, Const.PRODUCT_PRICE)
            intent.putExtra(Const.DATA, mPurchaseProduct)
            useLauncher.launch(intent)
        }

        when (mPurchaseProduct!!.reserveStatus) {
            EnumData.ReserveStatus.BOOKING.status -> {
//                text_purchase_product_ticket_history_detail_buy_cancel.visibility = View.GONE

                binding.textPurchaseProductTicketHistoryDetailBuyCancel.visibility = View.VISIBLE
                val endTime = (mPurchaseProduct!!.endTime!!.split(":")[0].toInt() * 60) + mPurchaseProduct!!.endTime!!.split(":")[1].toInt()
                val cal = Calendar.getInstance()
                val currentMin = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
                if (currentMin >= endTime - 60) {
                    binding.textPurchaseProductTicketHistoryDetailBuyCancel.visibility = View.GONE
                }

                binding.textPurchaseProductTicketHistoryDetailUse.visibility = View.VISIBLE
                binding.textPurchaseProductTicketHistoryDetailUseDate.visibility = View.GONE
            }
            EnumData.ReserveStatus.COMPLETE.status -> {
                binding.textPurchaseProductTicketHistoryDetailBuyCancel.visibility = View.GONE
                binding.textPurchaseProductTicketHistoryDetailUse.visibility = View.GONE
                binding.textPurchaseProductTicketHistoryDetailUseDate.visibility = View.VISIBLE

                var date = ""
                val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
                if (StringUtils.isNotEmpty(mPurchaseProduct!!.changeStatusDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.changeStatusDatetime)
                    date = output.format(d)
                    binding.textPurchaseProductTicketHistoryDetailUseDate.text = getString(R.string.format_use_date2, date)
                } else {
                    binding.textPurchaseProductTicketHistoryDetailUseDate.visibility = View.GONE
                }
            }
            EnumData.ReserveStatus.EXPIRED.status -> {
                binding.textPurchaseProductTicketHistoryDetailBuyCancel.visibility = View.GONE
                binding.textPurchaseProductTicketHistoryDetailUse.visibility = View.GONE
                binding.textPurchaseProductTicketHistoryDetailUseDate.visibility = View.VISIBLE

                var date = ""
                val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
                if (StringUtils.isNotEmpty(mPurchaseProduct!!.changeStatusDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.changeStatusDatetime)
                    date = output.format(d)
                    binding.textPurchaseProductTicketHistoryDetailUseDate.text = getString(R.string.format_expire_date4, date)
                } else {
                    binding.textPurchaseProductTicketHistoryDetailUseDate.visibility = View.GONE
                }
            }
            EnumData.ReserveStatus.BOOKING_CANCEL.status -> {
                binding.textPurchaseProductTicketHistoryDetailBuyCancel.visibility = View.GONE
                binding.textPurchaseProductTicketHistoryDetailUse.visibility = View.GONE
                binding.textPurchaseProductTicketHistoryDetailUseDate.visibility = View.VISIBLE

                var date = ""
                val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
                if (StringUtils.isNotEmpty(mPurchaseProduct!!.cancelDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.cancelDatetime)
                    date = output.format(d)
                    binding.textPurchaseProductTicketHistoryDetailUseDate.text = getString(R.string.format_cancel_date2, date)
                } else {
                    binding.textPurchaseProductTicketHistoryDetailUseDate.visibility = View.GONE
                }
            }
        }


        if(mPurchaseProduct!!.savedPoint != null && mPurchaseProduct!!.savedPoint!! > 0 && mPurchaseProduct!!.reserveStatus != EnumData.ReserveStatus.BOOKING_CANCEL.status && mPurchaseProduct!!.reserveStatus != EnumData.ReserveStatus.EXPIRED.status){
            binding.textPurchaseProductTicketHistoryDetailPoint.visibility = View.VISIBLE
            if (mPurchaseProduct!!.isPaymentPoint != null && mPurchaseProduct!!.isPaymentPoint!!) {
                binding.textPurchaseProductTicketHistoryDetailPoint.text = getString(R.string.format_saved, FormatUtil.getMoneyType(mPurchaseProduct!!.savedPoint.toString()))
            } else {
                binding.textPurchaseProductTicketHistoryDetailPoint.text = getString(R.string.format_will_save, FormatUtil.getMoneyType(mPurchaseProduct!!.savedPoint.toString()))
            }
        }else{
            binding.textPurchaseProductTicketHistoryDetailPoint.visibility = View.GONE
        }

        var date = ""
        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
        if (StringUtils.isNotEmpty(mPurchaseProduct!!.payDatetime)) {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.payDatetime)
            date = output.format(d)
        }
        binding.textPurchaseProductTicketHistoryDetailPayDate.text = date

        binding.textPurchaseProductTicketHistoryDetailBuyerName.text = mPurchaseProduct!!.purchase!!.buyerName
        binding.textPurchaseProductTicketHistoryDetailBuyerContact.text = mPurchaseProduct!!.purchase!!.buyerTel

        binding.textPurchaseProductTicketHistoryDetailBuyCancel.setOnClickListener {
            val intent = Intent(this, PurchaseCancelInfoActivity::class.java)
            intent.putExtra(Const.DATA, mPurchaseProduct!!.purchase)
            cancelLauncher.launch(intent)
        }

//        text_purchase_product_ticket_history_detail_count.text = getString(R.string.format_count_unit, FormatUtil.getMoneyType(mBuy!!.buyGoodsList!![0].count.toString()))
        binding.textPurchaseProductTicketHistoryDetailTotalPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mPurchaseProduct!!.price.toString()))

        var payMethod = ""

        when (mPurchaseProduct!!.purchase!!.payMethod) {
            "CARD", "card" -> {
                payMethod = getString(R.string.word_credit_card)
            }
            "BANK", "bank" -> {
                payMethod = getString(R.string.word_real_time_transfer)
            }
            "BOL", "bol"->{
                payMethod = getString(R.string.word_pay_point)
            }
        }

        binding.textPurchaseProductTicketHistoryDetailPayMethod.text = payMethod

        binding.recyclerPurchaseProductTicketHistoryDetail.layoutManager = LinearLayoutManager(this)
        val adapter = PurchaseProductShipAdapter(mPurchaseProduct!!)
        binding.recyclerPurchaseProductTicketHistoryDetail.adapter = adapter

    }

    private fun use() {
        val params = HashMap<String, String>()
        params["seqNo"] = mPurchaseProduct!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().updatePurchaseProductComplete(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_use_completed)
                getPurchaseProduct()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if (response?.resultCode == 516) {
                    showAlert(R.string.msg_impossible_use_time)
                }
            }
        }).build().call()
    }

    private val useLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {
                use()
            }
        }
    }

    private val cancelLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getPurchaseProduct()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buy_history_detail), ToolbarOption.ToolbarMenu.LEFT)
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
