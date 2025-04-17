package com.pplus.prnumberuser.apps.shippingsite.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.pplus.prnumberuser.apps.shippingsite.data.ShippingSiteAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.ShippingSite
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityShippingSiteConfigBinding
import retrofit2.Call

class ShippingSiteConfigActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityShippingSiteConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityShippingSiteConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    var mAdapter: ShippingSiteAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.recyclerShippingSiteConfig.layoutManager = LinearLayoutManager(this)
        mAdapter = ShippingSiteAdapter()
        binding.recyclerShippingSiteConfig.adapter = mAdapter

        binding.textShippingSiteConfigSelect.setOnClickListener {
            if(mAdapter!!.mSelectData == null){
                showAlert(R.string.msg_select_shipping_site)
                return@setOnClickListener
            }
            val data = Intent()
            data.putExtra(Const.SHIPPING_SITE, mAdapter!!.mSelectData)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        listCall()
    }

    private fun listCall(){
        showProgress("")
        ApiBuilder.create().shippingSiteList.setCallback(object : PplusCallback<NewResultResponse<ShippingSite>>{
            override fun onResponse(call: Call<NewResultResponse<ShippingSite>>?, response: NewResultResponse<ShippingSite>?) {
                hideProgress()
                if(response?.datas != null){
                    if(response.datas.isNotEmpty()){
                        mAdapter!!.mSelectData = response.datas[0]
                    }

                    mAdapter!!.setDataList(response.datas)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ShippingSite>>?, t: Throwable?, response: NewResultResponse<ShippingSite>?) {
                hideProgress()
            }
        }).build().call()
    }

    val regLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            listCall()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_shipping_site_config), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_plus_add))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        val intent = Intent(this@ShippingSiteConfigActivity, ShippingSiteRegActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        regLauncher.launch(intent)
                    }
                }
            }
        }
    }

}