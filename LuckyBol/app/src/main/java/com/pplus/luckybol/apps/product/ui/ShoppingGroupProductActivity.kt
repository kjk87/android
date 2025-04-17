package com.pplus.luckybol.apps.product.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.ShoppingGroup
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityReviewBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

class ShoppingGroupProductActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityReviewBinding

    override fun getLayoutView(): View {
        binding = ActivityReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val shoppingGroup = intent.getParcelableExtra<ShoppingGroup>(Const.DATA)

        getShoppingGroup(shoppingGroup!!)
    }

    private fun getShoppingGroup(shoppingGroup: ShoppingGroup){
        val params = HashMap<String, String>()
        params["seqNo"] = shoppingGroup.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getShoppingGroup(params).setCallback(object : PplusCallback<NewResultResponse<ShoppingGroup>>{
            override fun onResponse(call: Call<NewResultResponse<ShoppingGroup>>?,
                                    response: NewResultResponse<ShoppingGroup>?) {
                hideProgress()
                if(response?.data != null){
                    setTitle(response.data!!.title)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.review_container, ShoppingGroupProductFragment.newInstance(response.data!!), ShoppingGroupProductFragment::class.java.simpleName)
                    ft.commit()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ShoppingGroup>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<ShoppingGroup>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
