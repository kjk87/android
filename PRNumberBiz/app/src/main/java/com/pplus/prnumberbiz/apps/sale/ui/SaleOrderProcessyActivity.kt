package com.pplus.prnumberbiz.apps.sale.ui

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.OrderCount
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_sale_order_process.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class SaleOrderProcessyActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sale_order_process
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        layout_sale_order_process_tab1.setOnClickListener {
            layout_sale_order_process_tab1.isSelected = true
            layout_sale_order_process_tab2.isSelected = false
            layout_sale_order_process_tab3.isSelected = false
            setSaleOrderFragment(0)
        }

        layout_sale_order_process_tab2.setOnClickListener {
            layout_sale_order_process_tab1.isSelected = false
            layout_sale_order_process_tab2.isSelected = true
            layout_sale_order_process_tab3.isSelected = false
            setSaleOrderFragment(1)
        }

        layout_sale_order_process_tab3.setOnClickListener {
            layout_sale_order_process_tab1.isSelected = false
            layout_sale_order_process_tab2.isSelected = false
            layout_sale_order_process_tab3.isSelected = true

            if(LoginInfoManager.getInstance().user.page!!.isDelivery!!){
                setSaleHistoryFragment()
            }else{
                setSaleOrderHistoryFragment()
            }

        }

        layout_sale_order_process_tab1.isSelected = true
        layout_sale_order_process_tab2.isSelected = false
        layout_sale_order_process_tab3.isSelected = false
        setSaleOrderFragment(0)

        getOrderCount()
    }

    private fun setSaleOrderFragment(tab: Int) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.replace(R.id.sale_order_process_container, SaleOrderProcessFragment.newInstance(tab), SaleOrderProcessFragment::class.java.simpleName)
        ft.commitNow()
    }

    private fun setSaleHistoryFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.replace(R.id.sale_order_process_container, SaleHistoryFragment.newInstance(), SaleHistoryFragment::class.java.simpleName)
        ft.commitNow()
    }

    private fun setSaleOrderHistoryFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.replace(R.id.sale_order_process_container, SaleOrderHistoryFragment.newInstance(), SaleOrderHistoryFragment::class.java.simpleName)
        ft.commitNow()
    }

    private fun getOrderCount() {
        val today = Date(System.currentTimeMillis())
        val params = HashMap<String, String>()
        val output = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["startDuration"] = output.format(today) + " 00:00:00"
        params["endDuration"] = output.format(today) + " 23:59:59"
        params["type"] = "0"

        showProgress("")
        ApiBuilder.create().getCountOrderProcess(params).setCallback(object : PplusCallback<NewResultResponse<OrderCount>> {
            override fun onResponse(call: Call<NewResultResponse<OrderCount>>?, response: NewResultResponse<OrderCount>?) {
                hideProgress()
                if (response != null) {
                    val readyCount = response.data.readyCount
                    val ingCount = response.data.ingCount
                    text_sale_order_process_tab1.text = getString(R.string.word_order_ready) + "(" + readyCount + ")"
                    text_sale_order_process_tab2.text = getString(R.string.word_order_ing) + "(" + ingCount + ")"

                }

            }

            override fun onFailure(call: Call<NewResultResponse<OrderCount>>?, t: Throwable?, response: NewResultResponse<OrderCount>?) {
                hideProgress()
            }
        }).build().call()
    }


    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_sale_history), ToolbarOption.ToolbarMenu.LEFT)
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
