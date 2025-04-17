package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.BolChargeAlertActivity
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.product.ui.ProductShipDetailActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ActivityLottoJoinBinding
import com.pplus.luckybol.databinding.ItemLottoBinding
import com.pplus.luckybol.databinding.ItemLottoRandomBinding
import com.pplus.luckybol.databinding.ItemSelectedLottoBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import kotlin.collections.set

class LottoJoinActivity : BaseActivity() {
    override fun getPID(): String {
        return "Main_mypage_lotto_play"
    }

    private lateinit var binding: ActivityLottoJoinBinding

    override fun getLayoutView(): View {
        binding = ActivityLottoJoinBinding.inflate(layoutInflater)
        return binding.root
    }

    var selectList = ArrayList<Int>()
    val numberList = ArrayList<View>()
    var joinNumber = ""
    var mUser: User? = null
    lateinit var mEvent:Event
    override fun initializeView(savedInstanceState: Bundle?) {

        mEvent = intent.getParcelableExtra<Event>(Const.DATA)!!
        Glide.with(this@LottoJoinActivity).load(mEvent.bannerImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imageLottoJoinGift)

        binding.appBarLottoJoin.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (verticalOffset <= -binding.collapsingLottoJoin.height + binding.toolbarLottoJoin.height) { //toolbar is collapsed here
                binding.imageLottoJoinBack.setImageResource(R.drawable.ic_navbar_back)
            } else {
                binding.imageLottoJoinBack.setImageResource(R.drawable.ic_navbar_back_light)
            }
        }

        binding.imageLottoJoinBack.setOnClickListener {
            onBackPressed()
        }

        setRetentionBol()

        var horizontalLinearLayout = LinearLayout(this)
        horizontalLinearLayout.orientation = LinearLayout.HORIZONTAL
        for (j in 1..45) {

            if (j % 6 == 1) {
                horizontalLinearLayout = LinearLayout(this)
                horizontalLinearLayout.orientation = LinearLayout.HORIZONTAL
                binding.layoutLottoJoinNumber.addView(horizontalLinearLayout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
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
                    if (selectList.size < 5) {
                        lottoBinding.root.isSelected = true
                        selectList.add((lottoBinding.root.tag as Int))
                    } else {
                        ToastUtil.show(this, R.string.msg_enable_select_until_5)
                    }
                }
                setSelectNumber()
            }

            numberList.add(lottoBinding.root)
            horizontalLinearLayout.addView(lottoBinding.root)
            if (j % 6 != 0) {
                (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.height_48)
            }

            if (j == 45) {
                val lottoRandomBinding = ItemLottoRandomBinding.inflate(layoutInflater, LinearLayout(this), false)
                lottoRandomBinding.textLottoRandom.setOnClickListener {
                    selectList = (1..45).shuffled().take(5) as ArrayList<Int>
                    for (k in 0 until numberList.size) {
                        numberList[k].isSelected = false
                        if (selectList.contains((numberList[k].tag as Int))) {
                            numberList[k].isSelected = true
                        }
                    }
                    setSelectNumber()
                }
                horizontalLinearLayout.addView(lottoRandomBinding.root)
            }

        }

        binding.textLottoJoin.setOnClickListener {

            if (mUser == null) {
                return@setOnClickListener
            }

            joinNumber = ""
            for (i in 0 until selectList.size) {
                joinNumber += selectList[i].toString()
                if (i < selectList.size - 1) {
                    joinNumber += ","
                }
            }
            LogUtil.e(LOG_TAG, joinNumber)

            if (mUser!!.totalBol < Math.abs(mEvent.reward!!)) {
                val intent = Intent(this, BolChargeAlertActivity::class.java)
                intent.putExtra(Const.EVENT, mEvent)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                cashChangeLauncher.launch(intent)
            } else {
                if (mEvent.reward != null && mEvent.reward!! < 0) {
                    val intent = Intent(this, PlayAlertActivity::class.java)
                    intent.putExtra(Const.DATA, mEvent)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    joinAlertLauncher.launch(intent)
                } else {
                    joinEvent(mEvent, joinNumber)
                }
            }
        }

        val params = HashMap<String, String>()
        params["no"] = mEvent.no.toString()
        showProgress("")
        ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
            override fun onResponse(call: Call<NewResultResponse<EventGift>>?,
                                    response: NewResultResponse<EventGift>?) {
                hideProgress()
                if (response?.datas != null && response.datas!!.isNotEmpty()) {
                    val gift = response.datas!![0]
                    binding.textLottoJoinGiftName.text = gift.title
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventGift>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventGift>?) {
                hideProgress()
            }
        }).build().call()

        setSelectNumber()
    }

    val joinAlertLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val event = data.getParcelableExtra<Event>(Const.DATA)!!
                if (mUser!!.totalBol < Math.abs(event.reward!!)) {
                    val intent = Intent(this, BolChargeAlertActivity::class.java)
                    intent.putExtra(Const.EVENT, event)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    cashChangeLauncher.launch(intent)
                } else {
                    joinEvent(event, joinNumber)
                }
            }
        }
    }
    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setRetentionBol()
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if(StringUtils.isNotEmpty(mEvent.moveTargetString)){
            val moveTargetNumber = mEvent.moveTargetNumber
            if (mEvent.moveType == "inner" && moveTargetNumber != null) {
                when (moveTargetNumber) {
                    5 -> { //배송상품
                        val intent = Intent(this, ProductShipDetailActivity::class.java)
                        val productPrice = ProductPrice()
                        productPrice.seqNo = mEvent.moveTargetString!!.toLong()
                        intent.putExtra(Const.DATA, productPrice)
                        startActivity(intent)
                    }
                }
            } else {
                PplusCommonUtil.openChromeWebView(this, mEvent.moveTargetString!!)
            }
        }

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                mUser = LoginInfoManager.getInstance().user
            }
        })
    }

    fun joinEvent(event: Event, joinNumber: String) {
        val params = HashMap<String, String>()
        params["eventSeqNo"] = event.no.toString()
        params["joinNumber"] = joinNumber
        showProgress("")

        ApiBuilder.create().lottoJoin(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?,
                                    response: NewResultResponse<EventResult>?) {

                hideProgress()

                if (response?.data != null) {
                    val intent = Intent(this@LottoJoinActivity, AlertLottoJoinResultActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    resultLauncher.launch(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventResult>?) {

                hideProgress()

                if (response != null) {
                    if (response.resultCode == 517) {
                        val intent = Intent(this@LottoJoinActivity, BolChargeAlertActivity::class.java)
                        intent.putExtra(Const.EVENT, event)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        cashChangeLauncher.launch(intent)
                    } else {
                        PplusCommonUtil.showEventAlert(this@LottoJoinActivity, response.resultCode, event, null)
                    }
                }
            }
        }).build().call()
    }

    private fun setSelectNumber() {

        if (selectList.size == 0) {
            binding.textLottoJoinSelectNumber.visibility = View.VISIBLE
            binding.layoutLottoJoinSelectedNumber.visibility = View.GONE
        } else {
            binding.textLottoJoinSelectNumber.visibility = View.GONE
            binding.layoutLottoJoinSelectedNumber.visibility = View.VISIBLE
            selectList.sort()

            binding.layoutLottoJoinSelectedNumber.removeAllViews()
            for (i in 0 until selectList.size) {
                val selectedLottoBinding = ItemSelectedLottoBinding.inflate(layoutInflater, LinearLayout(this), false)
                selectedLottoBinding.textSelectedLottoNumber.text = selectList[i].toString()

                if (selectList[i] in 1..10) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_f2c443)
                } else if (selectList[i] in 11..20) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_85c5f1)
                } else if (selectList[i] in 21..30) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_e4807b)
                } else if (selectList[i] in 31..40) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_a689ee)
                } else {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_57d281)
                }

                binding.layoutLottoJoinSelectedNumber.addView(selectedLottoBinding.root)
                if (i < selectList.size - 1) {
                    (selectedLottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.width_36)
                }
            }
        }

        if (selectList.size < 5) {
            binding.textLottoJoin.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_c0c6cc))
            binding.textLottoJoin.isEnabled = false
        } else {
            binding.textLottoJoin.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_fc5c57))
            binding.textLottoJoin.isEnabled = true
        }
    }

    //    override fun getToolbarOption(): ToolbarOption {
    //
    //        val toolbarOption = ToolbarOption(this)
    //        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_match_lotto_number_event), ToolbarOption.ToolbarMenu.LEFT)
    //        return toolbarOption
    //    }
    //
    //    override fun getOnToolbarClickListener(): OnToolbarListener {
    //
    //        return OnToolbarListener { v, toolbarMenu, tag ->
    //            when (toolbarMenu) {
    //                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
    //                    onBackPressed()
    //                }
    //            }
    //        }
    //    }
}
