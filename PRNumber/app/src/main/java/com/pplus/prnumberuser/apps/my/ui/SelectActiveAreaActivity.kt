package com.pplus.prnumberuser.apps.my.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.my.data.CityAdapter
import com.pplus.prnumberuser.apps.my.data.GuAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Juso
import com.pplus.prnumberuser.core.network.model.dto.Province
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivitySelectActiveAreaBinding
import retrofit2.Call

class SelectActiveAreaActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivitySelectActiveAreaBinding

    override fun getLayoutView(): View {
        binding = ActivitySelectActiveAreaBinding.inflate(layoutInflater)
        return binding.root
    }

    var mCityAdapter:CityAdapter? = null
    var mGuAdapter: GuAdapter? = null
    var mSelectGuCode = ""
    var mSelectData: Juso? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.recyclerSelectActiveAreaCity.layoutManager = LinearLayoutManager(this)
        mCityAdapter = CityAdapter()
        binding.recyclerSelectActiveAreaCity.adapter = mCityAdapter

        mCityAdapter!!.setOnItemClickListener(object : CityAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val city = mCityAdapter!!.getItem(position)
                mGuAdapter!!.mSelectData = null
                mSelectGuCode = ""
                mSelectData = null
                if(city.docode == "36"){
                    val juso = Juso()
                    juso.name = city.doname
                    juso.value = "110"
                    mGuAdapter!!.mDoCode = "36"
                    mGuAdapter!!.mSelectData = juso
                    val list = arrayListOf<Juso>()
                    list.add(juso)
                    mGuAdapter!!.setDataList(list)

                }else{
                    getGuList(city.docode!!)
                }

            }
        })

        binding.recyclerSelectActiveAreaGu.layoutManager = LinearLayoutManager(this)
        mGuAdapter = GuAdapter()
        binding.recyclerSelectActiveAreaGu.adapter = mGuAdapter

        getDoList()
    }

    private fun getDoList(){
        showProgress("")
        ApiBuilder.create().doList.setCallback(object : PplusCallback<NewResultResponse<Province>>{
            override fun onResponse(call: Call<NewResultResponse<Province>>?, response: NewResultResponse<Province>?) {
                hideProgress()
                if(response?.datas != null){

                    if(response.datas.isNotEmpty()){
                        mCityAdapter!!.mSelectData = response.datas[0]
                        getGuList(response.datas[0].docode!!)
                    }
                    mCityAdapter!!.setDataList(response.datas)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Province>>?, t: Throwable?, response: NewResultResponse<Province>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getGuList(value:String){
        val params = HashMap<String, String>()
        params["type"] = "gu"
        params["value"] = value
        showProgress("")
        ApiBuilder.create().getJusoList(params).setCallback(object : PplusCallback<NewResultResponse<Juso>>{
            override fun onResponse(call: Call<NewResultResponse<Juso>>?, response: NewResultResponse<Juso>?) {
                hideProgress()
                if(response?.datas != null){
//                    if(response.datas.isNotEmpty()){
//                        mGuAdapter!!.mSelectData = response.datas[0]
//                    }
                    mGuAdapter!!.mDoCode = value
                    mGuAdapter!!.setDataList(response.datas)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Juso>>?, t: Throwable?, response: NewResultResponse<Juso>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_setting_active_area), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete))
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
                        if (mSelectData == null) {
                            showAlert(R.string.msg_select_active_area)
                            return
                        }
                        val data = Intent()
                        mSelectData!!.value = mSelectGuCode + mSelectData!!.value
                        data.putExtra(Const.DATA, mSelectData)
                        setResult(RESULT_OK, data)
                        finish()
                    }
                }
            }
        }
    }
}