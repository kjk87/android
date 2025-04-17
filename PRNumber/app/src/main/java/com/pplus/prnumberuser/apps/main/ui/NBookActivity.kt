package com.pplus.prnumberuser.apps.main.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.apps.event.ui.EventDetailActivity
import com.pplus.prnumberuser.apps.event.ui.EventMoveDetailActivity
import com.pplus.prnumberuser.apps.event.ui.EventResultActivity
import com.pplus.prnumberuser.apps.main.data.NBookAdapter
import com.pplus.prnumberuser.apps.page.ui.Alert3rdPartyInfoTermsActivity
import com.pplus.prnumberuser.apps.page.ui.NumberGroupPageActivity
import com.pplus.prnumberuser.apps.product.ui.NumberGroupProductActivity
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventResult
import com.pplus.prnumberuser.core.network.model.dto.Plus
import com.pplus.prnumberuser.core.network.model.dto.VirtualNumberManage
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityNBookBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class NBookActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityNBookBinding
    override fun getLayoutView(): View {
        binding = ActivityNBookBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return "nbook"
    }

    var mAdapter:NBookAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mAdapter = NBookAdapter()
        binding.recyclerNBook.layoutManager = GridLayoutManager(this,3)
        binding.recyclerNBook.adapter = mAdapter
        mAdapter!!.listener = object : NBookAdapter.OnItemClickListener{
            override fun onItemClick(item:VirtualNumberManage) {
                when(item.type){
                    EnumData.VirtualNumberManageType.page.name->{
                        if(item.itemList != null && item.itemList!!.isNotEmpty() && item.itemList!![0].page != null){
                            PplusCommonUtil.goPage(this@NBookActivity, item.itemList!![0].page!!, 0, 0)
                        }else{
                            showAlert(R.string.msg_not_exist_pr_number)
                        }

                    }
                    EnumData.VirtualNumberManageType.event.name->{
                        if(StringUtils.isNotEmpty(item.eventCode)){
                            getEvent(item.eventCode!!)
                        }else{
                            showAlert(R.string.msg_not_exist_pr_number)
                        }
                    }

                    EnumData.VirtualNumberManageType.link.name->{
                        if (StringUtils.isNotEmpty(item.url)) {
                            PplusCommonUtil.openChromeWebView(this@NBookActivity, item.url!!)
                        }else{
                            showAlert(R.string.msg_not_exist_pr_number)
                        }
                    }
                    EnumData.VirtualNumberManageType.pages.name->{
                        val intent = Intent(this@NBookActivity, NumberGroupPageActivity::class.java)
                        intent.putExtra(Const.DATA, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                    EnumData.VirtualNumberManageType.products.name->{
                        val intent = Intent(this@NBookActivity, NumberGroupProductActivity::class.java)
                        intent.putExtra(Const.DATA, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                }
            }
        }

        getNbookVirtualNumberManageList()
    }

    private fun getNbookVirtualNumberManageList(){
        val params = HashMap<String, String>()
        showProgress("")
        ApiBuilder.create().getNbookVirtualNumberManageList().setCallback(object :
            PplusCallback<NewResultResponse<VirtualNumberManage>> {
            override fun onResponse(
                call: Call<NewResultResponse<VirtualNumberManage>>?,
                response: NewResultResponse<VirtualNumberManage>?
            ) {
                hideProgress()

                if (response?.datas != null) {
                    mAdapter!!.mDataList = response.datas
                    mAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(
                call: Call<NewResultResponse<VirtualNumberManage>>?,
                t: Throwable?,
                response: NewResultResponse<VirtualNumberManage>?
            ) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getEvent(code:String) {
        val params = HashMap<String, String>()
        params["code"] = code
        showProgress("")
        ApiBuilder.create().getEventByCode(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {

                hideProgress()

                if (response!!.data != null) {
                    val event = response.data

                    if (event.status != "active") {
                        val url = event.moveTargetString
                        if (StringUtils.isNotEmpty(url)) {
                            PplusCommonUtil.openChromeWebView(this@NBookActivity, url!!)
                        }
                        return
                    }

                    if (event!!.primaryType.equals(EventType.PrimaryType.insert.name)) {

                        if(event.isPlus != null && event.isPlus!! && event.pageSeqNo != null && event.agreement2 != null && event.agreement2!! == 1){
                            val params = HashMap<String, String>()
                            params["pageSeqNo"] = event.pageSeqNo.toString()
                            showProgress("")
                            ApiBuilder.create().getOnlyPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
                                override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
                                    hideProgress()
                                    if(response?.data == null || response.data.agreement == null || !response.data.agreement!!){
                                        val intent = Intent(this@NBookActivity, Alert3rdPartyInfoTermsActivity::class.java)
                                        intent.putExtra(Const.EVENT, event)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        startActivityForResult(intent, Const.REQ_EVENT_AGREE)
                                    }else{
                                        joinEvent(event)
                                    }
                                }

                                override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
                                    hideProgress()
                                }
                            }).build().call()
                        }else{
                            joinEvent(event)
                        }

                    } else {
                        if (event.primaryType.equals(EventType.PrimaryType.move.name)) {
                            val intent = Intent(this@NBookActivity, EventMoveDetailActivity::class.java)
                            intent.putExtra(Const.DATA, event)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        } else {
                            if (StringUtils.isNotEmpty(event.eventLink)) {
                                val intent = Intent(this@NBookActivity, EventDetailActivity::class.java)
                                intent.putExtra(Const.DATA, event)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            } else {

                                if(event.isPlus != null && event.isPlus!! && event.pageSeqNo != null && event.agreement2 != null && event.agreement2!! == 1){
                                    val params = HashMap<String, String>()
                                    params["pageSeqNo"] = event.pageSeqNo.toString()
                                    showProgress("")
                                    ApiBuilder.create().getOnlyPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
                                        override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
                                            hideProgress()
                                            if(response?.data == null || response.data.agreement == null || !response.data.agreement!!){
                                                val intent = Intent(this@NBookActivity, Alert3rdPartyInfoTermsActivity::class.java)
                                                intent.putExtra(Const.EVENT, event)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                startActivityForResult(intent, Const.REQ_EVENT_AGREE)
                                            }else{
                                                joinEvent(event)
                                            }
                                        }

                                        override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
                                            hideProgress()
                                        }
                                    }).build().call()
                                }else{
                                    joinEvent(event)
                                }
                            }
                        }
                    }

                } else {
                    showAlert(R.string.msg_not_exist_pr_number)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {

                hideProgress()
                showAlert(R.string.msg_not_exist_pr_number)
            }
        }).build().call()
    }

    fun joinEvent(event: Event) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        showProgress("")

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?, response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (response!!.data != null) {
                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        var delayTime = 2000L
                        mLoading.isCancelable = false
                        try {
                            mLoading.show(supportFragmentManager, "")
                        } catch (e: Exception) {

                        }

                        val handler = Handler()
                        handler.postDelayed(Runnable {

                            try {
                                mLoading.dismiss()
                            } catch (e: Exception) {

                            }

                            val intent = Intent(this@NBookActivity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivityForResult(intent, Const.REQ_RESULT)
                        }, delayTime)

                    } else {
                        val intent = Intent(this@NBookActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivityForResult(intent, Const.REQ_RESULT)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?, t: Throwable?, response: NewResultResponse<EventResult>?) {

                hideProgress()

                if (response != null) {
                    PplusCommonUtil.showEventAlert(this@NBookActivity, response.resultCode, event)
                }
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_EVENT_AGREE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val event = data.getParcelableExtra<Event>(Const.DATA)
                    joinEvent(event!!)
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_n_book), ToolbarOption.ToolbarMenu.LEFT)
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