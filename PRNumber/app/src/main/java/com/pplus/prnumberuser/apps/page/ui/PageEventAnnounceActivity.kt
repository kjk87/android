package com.pplus.prnumberuser.apps.page.ui

import android.content.Intent
import android.net.Uri
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
import com.pplus.prnumberuser.apps.page.data.PageEventAnnounceGiftAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityPageEventAnnounceBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class PageEventAnnounceActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String? {
        return ""
    }

    lateinit var mEvent: Event
    var mPage:Page? = null

    private lateinit var binding: ActivityPageEventAnnounceBinding

    override fun getLayoutView(): View {
        binding = ActivityPageEventAnnounceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        mEvent = intent.getParcelableExtra(Const.DATA)!!

        binding.recyclerPageEventAnnounce.layoutManager = LinearLayoutManager(this)

        getEvent()
    }

    private fun getEvent() {
        val params = HashMap<String, String>()
        params["no"] = mEvent.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                hideProgress()
                if(response?.data != null){
                    mEvent = response.data
                    setTitle(mEvent.title)
                    getPage()
                    existResult()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getPage() {

        val params = HashMap<String, String>()
        params["no"] = mEvent.pageSeqNo.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                hideProgress()
                if (response.data != null) {
                    mPage = response.data!!
                    setData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable?, response: NewResultResponse<Page>) {
            }
        }).build().call()
    }

    private fun setData(){
        val params = HashMap<String, String>()
        params["no"] = mEvent.no.toString()
        ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
            override fun onResponse(call: Call<NewResultResponse<EventGift>>?, response: NewResultResponse<EventGift>?) {
                if(response?.datas != null && response.datas.isNotEmpty()){
                    val giftList = response.datas.filter {
                        it.totalCount != it.remainCount
                    }
                    val adapter = PageEventAnnounceGiftAdapter()
                    binding.recyclerPageEventAnnounce.adapter = adapter
                    adapter.setDataList(giftList as MutableList<EventGift>)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventGift>>?, t: Throwable?, response: NewResultResponse<EventGift>?) {

            }
        }).build().call()

    }

    private fun existResult() {
        val params = HashMap<String, String>()
        params["no"] = mEvent.no.toString()
        ApiBuilder.create().existsEventResult(params).setCallback(object : PplusCallback<NewResultResponse<EventExist>> {
            override fun onResponse(call: Call<NewResultResponse<EventExist>>?, response: NewResultResponse<EventExist>?) {
                val eventExist = response!!.data
                if (eventExist.join!!) {
                    if (eventExist.win!!) {
                        getWinAll()
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventExist>>?, t: Throwable?, response: NewResultResponse<EventExist>?) {
            }
        }).build().call()
    }

    private fun getWinAll() {
        val params = HashMap<String, String>()
        params["no"] = mEvent.no.toString()
        showProgress("")
        ApiBuilder.create().getWinAll(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {

            override fun onResponse(call: Call<NewResultResponse<EventWin>>, response: NewResultResponse<EventWin>) {
                hideProgress()
                if (response.datas != null && response.datas.size > 0) {

                    var eventWin: EventWin? = null
                    for(data in response.datas){
                        eventWin = data
                        break
                    }

                    if(eventWin == null || (eventWin.gift!!.delivery != null && !eventWin.gift!!.delivery!!)){
                        binding.textPageEventAnnounceAddress.visibility = View.GONE
                    }else{
                        binding.textPageEventAnnounceAddress.visibility = View.VISIBLE
                        eventWin.event = mEvent
                        if(StringUtils.isEmpty(eventWin.deliveryAddress)){
                            binding.textPageEventAnnounceAddress.setBackgroundColor(ResourceUtil.getColor(this@PageEventAnnounceActivity, R.color.color_579ffb))
                            binding.textPageEventAnnounceAddress.setText(R.string.word_reg_shipping_address)
                            binding.textPageEventAnnounceAddress.setOnClickListener {
                                val intent = Intent(this@PageEventAnnounceActivity, PageEventAddressSaveActivity::class.java)
                                intent.putExtra(Const.DATA, eventWin)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                setAddressLauncher.launch(intent)
                            }
                        }else{
                            binding.textPageEventAnnounceAddress.setBackgroundColor(ResourceUtil.getColor(this@PageEventAnnounceActivity, R.color.color_575757))
                            binding.textPageEventAnnounceAddress.setText(R.string.msg_gift_address_inquiry)
                            binding.textPageEventAnnounceAddress.setOnClickListener {
                                if(mPage != null){
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage?.phone!!))
                                    startActivity(intent)
                                }

                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventWin>>, t: Throwable, response: NewResultResponse<EventWin>) {
                hideProgress()
            }
        }).build().call()
    }

    val setAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getWinAll()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
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