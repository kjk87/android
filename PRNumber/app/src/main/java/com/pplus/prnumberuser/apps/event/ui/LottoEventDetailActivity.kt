package com.pplus.prnumberuser.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.alert.LottoTicketChargeAlertActivity
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.BolChargeAlertActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventGift
import com.pplus.prnumberuser.core.network.model.dto.EventResult
import com.pplus.prnumberuser.core.network.model.dto.User
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityLottoEventDetailBinding
import com.pplus.prnumberuser.databinding.ItemLottoBinding
import com.pplus.prnumberuser.databinding.ItemSelectedLottoBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class LottoEventDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLottoEventDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityLottoEventDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var selectList = ArrayList<Int>()
    val numberList = ArrayList<View>()
    var winCode = ""
    var user: User? = null
    override fun initializeView(savedInstanceState: Bundle?) {

        val event = intent.getParcelableExtra<Event>(Const.DATA)

        setTitle(getString(R.string.format_lotto_event_title, event!!.lottoTimes.toString()))

        user = LoginInfoManager.getInstance().user

        var horizontalLinearLayout = LinearLayout(this)
        horizontalLinearLayout.orientation = LinearLayout.HORIZONTAL
        for (j in 1..45) {

            if (j % 6 == 1) {
                horizontalLinearLayout = LinearLayout(this)
                horizontalLinearLayout.orientation = LinearLayout.HORIZONTAL
                binding.layoutLottoEventDetailNumber.addView(horizontalLinearLayout)
                if (j != 1) {
                    (horizontalLinearLayout.layoutParams as LinearLayout.LayoutParams).topMargin = resources.getDimensionPixelSize(R.dimen.height_40)
                }
            }
            val lottoBinding = ItemLottoBinding.inflate(layoutInflater, LinearLayout(this), false)
            lottoBinding.root.tag = j
            lottoBinding.textLottoNumber.text = j.toString()
            if (j in 1..10) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_1)
            } else if (j in 11..20) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_2)
            } else if (j in 21..30) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_3)
            } else if (j in 31..40) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_4)
            } else {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_5)
            }

            lottoBinding.textLottoNumber.setOnClickListener {
                if (selectList.contains((lottoBinding.root.tag as Int))) {
                    lottoBinding.root.isSelected = false
                    selectList.remove((lottoBinding.root.tag as Int))
                } else {
                    if (selectList.size < 6) {
                        lottoBinding.root.isSelected = true
                        selectList.add((lottoBinding.root.tag as Int))
                    } else {
                        ToastUtil.show(this, R.string.msg_enable_select_until_6)
                    }
                }
                setSelectNumber()
            }

            numberList.add(lottoBinding.root)
            horizontalLinearLayout.addView(lottoBinding.root)
            if (j % 6 != 6) {
                (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.height_44)
            }

            if (j == 45) {
                val randomView = layoutInflater.inflate(R.layout.item_lotto_random, null)
                randomView.setOnClickListener {
                    selectList = (1..45).shuffled().take(6) as ArrayList<Int>
                    for (k in 0 until numberList.size) {
                        numberList[k].isSelected = false
                        if (selectList.contains((numberList[k].tag as Int))) {
                            numberList[k].isSelected = true
                        }
                    }
                    setSelectNumber()
                }
                horizontalLinearLayout.addView(randomView)
            }

        }

        binding.textLottoEventDetailJoin.setOnClickListener {
            winCode = ""
            for (i in 0 until selectList.size) {
                winCode += selectList[i].toString()
                if (i < selectList.size - 1) {
                    winCode += ","
                }
            }
            LogUtil.e(LOG_TAG, winCode)

            if (event.primaryType == EventType.PrimaryType.lotto.name) {
                if (user!!.lottoDefaultTicketCount == 0 && user!!.lottoTicketCount == 0 && user!!.totalBol < Math.abs(event.reward!!)) {
                    val intent = Intent(this, LottoTicketChargeAlertActivity::class.java)
//                    intent.putExtra(Const.EVENT, event)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                } else {
                    val intent = Intent(this, PlayAlertActivity::class.java)
                    intent.putExtra(Const.DATA, event)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(intent, Const.REQ_JOIN_ALERT)
                }

            } else if (event.primaryType == EventType.PrimaryType.lottoPlaybol.name) {
                if (user!!.lottoTicketCount == 0 && user!!.totalBol < Math.abs(event.reward!!)) {
                    val intent = Intent(this, LottoTicketChargeAlertActivity::class.java)
//                    intent.putExtra(Const.EVENT, event)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }else{
                    if (event.reward != null && event.reward!! < 0) {
                        val intent = Intent(this, PlayAlertActivity::class.java)
                        intent.putExtra(Const.DATA, event)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivityForResult(intent, Const.REQ_JOIN_ALERT)
                    }else{
                        joinEvent(event, winCode)
                    }

                }
//                if (event.reward != null && event.reward!! < 0) {
//                    if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(event.reward!!)) {
//                        val intent = Intent(this, PlayAlertActivity::class.java)
//                        intent.putExtra(Const.DATA, event)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivityForResult(intent, Const.REQ_JOIN_ALERT)
//                    } else {
//                        val intent = Intent(this, BolChargeAlertActivity::class.java)
//                        intent.putExtra(Const.EVENT, event)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivity(intent)
//                    }
//                } else {
//
//                }
            }
        }

        if (event.primaryType == EventType.PrimaryType.lotto.name) {
            binding.layoutLottoEventDetailGift.visibility = View.VISIBLE
            binding.layoutLottoEventDetailPlayGift.visibility = View.GONE

            binding.textLottoDetailLottotimes.text = getString(R.string.format_lotto_times_price, event.lottoTimes)
            binding.textLottoDetailPrice.text = FormatUtil.getMoneyType(event.lottoPrice.toString())
        } else if (event.primaryType == EventType.PrimaryType.lottoPlaybol.name) {
            binding.layoutLottoEventDetailGift.visibility = View.GONE
            binding.layoutLottoEventDetailPlayGift.visibility = View.VISIBLE

            val params = HashMap<String, String>()
            params["no"] = event.no.toString()
            showProgress("")
            ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
                override fun onResponse(call: Call<NewResultResponse<EventGift>>?, response: NewResultResponse<EventGift>?) {
                    hideProgress()
                    if (response!!.datas != null && response.datas.isNotEmpty()) {

                        val gift = response.datas[0]
                        Glide.with(this@LottoEventDetailActivity).load(gift.giftImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imageLottoDetailGift)
                        binding.textLottoDetailGiftName.text = gift.title
                        binding.textLottoDetailCondition.text = getString(R.string.format_match_lotto_number, event.lottoMatchNum.toString())
                    }

                }

                override fun onFailure(call: Call<NewResultResponse<EventGift>>?, t: Throwable?, response: NewResultResponse<EventGift>?) {
                    hideProgress()
                }
            }).build().call()
        }

        binding.textLottoEventDetailJoinHistory.setOnClickListener {
            val intent = Intent(this, MyLottoJoinListActivity::class.java)
            intent.putExtra(Const.LOTTO_TIMES, event.lottoTimes)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        setSelectNumber()
        getJoinCount(event)
    }

    fun getJoinCount(event:Event) {
        val params = HashMap<String, String>()
        params["lottoTimes"] = event.lottoTimes!!
        ApiBuilder.create().getLottoJoinCont(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                if (response != null) {
                    if(response.data > 0){
                        binding.textLottoEventDetailJoinHistory.visibility = View.VISIBLE
                    }else{
                        binding.textLottoEventDetailJoinHistory.visibility = View.GONE
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {

            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_JOIN_ALERT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val event = data.getParcelableExtra<Event>(Const.DATA)
                        if (event!!.primaryType == EventType.PrimaryType.lotto.name) {
                            if (user!!.lottoDefaultTicketCount == 0 && user!!.lottoTicketCount == 0 && user!!.totalBol < Math.abs(event.reward!!)) {
                                val intent = Intent(this@LottoEventDetailActivity, BolChargeAlertActivity::class.java)
                                intent.putExtra(Const.EVENT, event)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }else{
                                joinEvent(event, winCode)
                            }
                        }else if (event.primaryType == EventType.PrimaryType.lottoPlaybol.name) {
                            if (user!!.lottoTicketCount == 0 && user!!.totalBol < Math.abs(event.reward!!)) {
                                val intent = Intent(this, BolChargeAlertActivity::class.java)
                                intent.putExtra(Const.EVENT, event)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }else{
                                joinEvent(event, winCode)
                            }
                        }
                    }
                }
            }
            Const.REQ_RESULT -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun getGift(event: Event) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        showProgress("")
        ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
            override fun onResponse(call: Call<NewResultResponse<EventGift>>?, response: NewResultResponse<EventGift>?) {
                if (response!!.datas != null && response.datas.isNotEmpty()) {

//                    mAdapter!!.mGiftList = response.datas!!
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventGift>>?, t: Throwable?, response: NewResultResponse<EventGift>?) {

            }
        }).build().call()
    }

    fun joinEvent(event: Event, winCode: String) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        params["winCode"] = winCode
        showProgress("")

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?, response: NewResultResponse<EventResult>?) {

                hideProgress()

                if (response!!.data != null) {
                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        val delayTime = 2000L
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

                            val intent = Intent(this@LottoEventDetailActivity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivityForResult(intent, Const.REQ_RESULT)
                        }, delayTime)

                    } else {
                        val intent = Intent(this@LottoEventDetailActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivityForResult(intent, Const.REQ_RESULT)
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?, t: Throwable?, response: NewResultResponse<EventResult>?) {

                hideProgress()

                if (response != null) {
                    PplusCommonUtil.showEventAlert(this@LottoEventDetailActivity, response.resultCode, event)
                }

            }
        }).build().call()
    }

    private fun setSelectNumber() {

        if (selectList.size == 0) {
            binding.textLottoEventDetailSelectNumber.visibility = View.VISIBLE
            binding.layoutLottoEventDetailSelectedNumber.visibility = View.GONE
        } else {
            binding.textLottoEventDetailSelectNumber.visibility = View.GONE
            binding.layoutLottoEventDetailSelectedNumber.visibility = View.VISIBLE
            selectList.sort()

            binding.layoutLottoEventDetailSelectedNumber.removeAllViews()
            for (i in 0 until selectList.size) {
                val selectedLottoBinding = ItemSelectedLottoBinding.inflate(layoutInflater, LinearLayout(this), false)
                selectedLottoBinding.textSelectedLottoNumber.text = selectList[i].toString()

                if (selectList[i] in 1..10) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_1)
                } else if (selectList[i] in 11..20) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_2)
                } else if (selectList[i] in 21..30) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_3)
                } else if (selectList[i] in 31..40) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_4)
                } else {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_5)
                }

                binding.layoutLottoEventDetailSelectedNumber.addView(selectedLottoBinding.root)
                if (i < selectList.size - 1) {
                    (selectedLottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.height_26)
                }
            }
        }

        if (selectList.size < 6) {
            binding.textLottoEventDetailJoin.setBackgroundResource(R.drawable.btn_gray)
            binding.textLottoEventDetailJoin.isEnabled = false
        } else {
            binding.textLottoEventDetailJoin.setBackgroundResource(R.drawable.btn_blue)
            binding.textLottoEventDetailJoin.isEnabled = true
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_match_lotto_number_event), ToolbarOption.ToolbarMenu.LEFT)
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
