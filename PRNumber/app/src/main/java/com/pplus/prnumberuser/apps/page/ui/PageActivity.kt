package com.pplus.prnumberuser.apps.page.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityPageBinding
import retrofit2.Call
import java.util.*

class PageActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPageBinding

    override fun getLayoutView(): View {
        binding = ActivityPageBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPage: Page? = null
    var mKey: String? =null
    var x = 0
    var y = 0

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.PAGE)
        mKey = intent.getStringExtra(Const.KEY)
        x = intent.getIntExtra(Const.X, 0)
        y = intent.getIntExtra(Const.Y, 0)

        getPage()
    }

    private fun getPage() {

        val params = HashMap<String, String>()
        params["no"] = mPage!!.no.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                hideProgress()

                if (response.data != null) {
                    mPage = response.data!!
                    val ft = supportFragmentManager.beginTransaction()
                    if(mPage!!.storeType == "offline"){
                        ft.replace(R.id.page_parent_container, OfflinePageFragment.newInstance(mPage!!), PageFragment::class.java.simpleName)
                    }else{
                        ft.replace(R.id.page_parent_container, PageFragment.newInstance(mPage!!), PageFragment::class.java.simpleName)
                    }
                    ft.commit()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable?, response: NewResultResponse<Page>) {
                hideProgress()
            }
        }).build().call()
    }
}
