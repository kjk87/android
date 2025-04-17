package com.lejel.wowbox.apps.luckybox.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyBox
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchase
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBoxBuyBinding
import retrofit2.Call

class LuckyBoxBuyActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityLuckyBoxBuyBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxBuyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mCount = 0
    var mLuckyBox: LuckyBox? = null
    var mLuckyBoxPurchase: LuckyBoxPurchase? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mLuckyBox = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBox::class.java)

        setTitle(mLuckyBox!!.title)

        binding.textLuckyBoxBuyCount1Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_wow_box_count, "1"))
        binding.textLuckyBoxBuyCount3Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_wow_box_count, "3"))
        binding.textLuckyBoxBuyCount5Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_wow_box_count, "5"))
        binding.textLuckyBoxBuyCount10Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_wow_box_count, "10"))
        binding.textLuckyBoxBuyCount50Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_wow_box_count, "50"))

        binding.textLuckyBoxBuyCount1Price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mLuckyBox!!.engagePrice.toString()))
        binding.textLuckyBoxBuyCount3Price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((mLuckyBox!!.engagePrice!!*3).toString()))
        binding.textLuckyBoxBuyCount5Price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((mLuckyBox!!.engagePrice!!*5).toString()))
        binding.textLuckyBoxBuyCount10Price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((mLuckyBox!!.engagePrice!!*10).toString()))
        binding.textLuckyBoxBuyCount50Price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((mLuckyBox!!.engagePrice!!*50).toString()))

        binding.textLuckyBoxBuyCount5Bonus.text = getString(R.string.format_bonus_point, FormatUtil.getMoneyType("1000"))
        binding.textLuckyBoxBuyCount10Bonus.text = getString(R.string.format_bonus_point, FormatUtil.getMoneyType("2000"))
        binding.textLuckyBoxBuyCount50Bonus.text = getString(R.string.format_bonus_point, FormatUtil.getMoneyType("12000"))

        binding.textLuckyBoxBuyDesc1.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_buy_lucky_box_desc1))


        binding.layoutLuckyBoxBuyCount1.setOnClickListener {
            binding.layoutLuckyBoxBuyCount1.isSelected = true
            binding.layoutLuckyBoxBuyCount3.isSelected = false
            binding.layoutLuckyBoxBuyCount5.isSelected = false
            binding.layoutLuckyBoxBuyCount10.isSelected = false
            binding.layoutLuckyBoxBuyCount50.isSelected = false
            mCount = 1
            setTotalPrice()
        }

        binding.layoutLuckyBoxBuyCount3.setOnClickListener {
            binding.layoutLuckyBoxBuyCount1.isSelected = false
            binding.layoutLuckyBoxBuyCount3.isSelected = true
            binding.layoutLuckyBoxBuyCount5.isSelected = false
            binding.layoutLuckyBoxBuyCount10.isSelected = false
            binding.layoutLuckyBoxBuyCount50.isSelected = false
            mCount = 3
            setTotalPrice()
        }

        binding.layoutLuckyBoxBuyCount5.setOnClickListener {
            binding.layoutLuckyBoxBuyCount1.isSelected = false
            binding.layoutLuckyBoxBuyCount3.isSelected = false
            binding.layoutLuckyBoxBuyCount5.isSelected = true
            binding.layoutLuckyBoxBuyCount10.isSelected = false
            binding.layoutLuckyBoxBuyCount50.isSelected = false
            mCount = 5
            setTotalPrice()
        }

        binding.layoutLuckyBoxBuyCount10.setOnClickListener {
            binding.layoutLuckyBoxBuyCount1.isSelected = false
            binding.layoutLuckyBoxBuyCount3.isSelected = false
            binding.layoutLuckyBoxBuyCount5.isSelected = false
            binding.layoutLuckyBoxBuyCount10.isSelected = true
            binding.layoutLuckyBoxBuyCount50.isSelected = false
            mCount = 10
            setTotalPrice()
        }

        binding.layoutLuckyBoxBuyCount50.setOnClickListener {
            binding.layoutLuckyBoxBuyCount1.isSelected = false
            binding.layoutLuckyBoxBuyCount3.isSelected = false
            binding.layoutLuckyBoxBuyCount5.isSelected = false
            binding.layoutLuckyBoxBuyCount10.isSelected = false
            binding.layoutLuckyBoxBuyCount50.isSelected = true
            mCount = 50
            setTotalPrice()
        }

        binding.editLuckyBoxBuyUseCash.setSingleLine()
        binding.editLuckyBoxBuyUseCash.addTextChangedListener {
            if(it.toString().isNotEmpty() && mCount == 0){
                showAlert(R.string.msg_select_wow_box)
                binding.editLuckyBoxBuyUseCash.setText("")
                return@addTextChangedListener
            }

            if (it.toString().isNotEmpty()) {

                val totalPrice = totalPrice

                if (LoginInfoManager.getInstance().member!!.cash!!.toInt() > totalPrice) {
                    if (it.toString().toInt() > totalPrice) {
                        binding.editLuckyBoxBuyUseCash.setText(totalPrice.toString())
                    }
                } else {
                    if (it.toString().toInt() > LoginInfoManager.getInstance().member!!.cash!!.toInt()) {
                        binding.editLuckyBoxBuyUseCash.setText(LoginInfoManager.getInstance().member!!.cash!!.toInt().toString())
                    }
                }
            }
            setTotalPrice()
        }

        binding.textLuckyBoxBuyUseCashAll.setOnClickListener {
            binding.editLuckyBoxBuyUseCash.setText(LoginInfoManager.getInstance().member!!.cash!!.toInt().toString())
        }

        val hander = Handler(Looper.myLooper()!!)

        binding.layoutLuckyBoxBuyGuideTitle.setOnClickListener {
            if (binding.textLuckyBoxBuyGuideDesc.visibility == View.VISIBLE) {
                binding.imageLuckyBoxBuyGuideArrow.setImageResource(R.drawable.ic_arrow_down)
                binding.textLuckyBoxBuyGuideDesc.visibility = View.GONE
            } else {
                binding.imageLuckyBoxBuyGuideArrow.setImageResource(R.drawable.ic_arrow_up)
                binding.textLuckyBoxBuyGuideDesc.visibility = View.VISIBLE
                hander.postDelayed({
                    binding.scrollLuckyBoxBuy.isSmoothScrollingEnabled = true
                    binding.scrollLuckyBoxBuy.scrollY = binding.viewLuckyBoxBuyLastBar.bottom
                }, 100)
            }
        }

        binding.layoutLuckyBoxBuyRefundTitle.setOnClickListener {
            if (binding.textLuckyBoxBuyRefundDesc.visibility == View.VISIBLE) {
                binding.imageLuckyBoxBuyRefundArrow.setImageResource(R.drawable.ic_arrow_down)
                binding.textLuckyBoxBuyRefundDesc.visibility = View.GONE
            } else {
                binding.imageLuckyBoxBuyRefundArrow.setImageResource(R.drawable.ic_arrow_up)
                binding.textLuckyBoxBuyRefundDesc.visibility = View.VISIBLE
                hander.postDelayed({
                    binding.scrollLuckyBoxBuy.isSmoothScrollingEnabled = true
                    binding.scrollLuckyBoxBuy.scrollY = binding.viewLuckyBoxBuyLastBar.bottom
                }, 100)

            }
        }

        binding.layoutLuckyBoxBuyDeliveryTitle.setOnClickListener {
            if (binding.textLuckyBoxBuyDeliveryDesc.visibility == View.VISIBLE) {
                binding.imageLuckyBoxBuyDeliveryArrow.setImageResource(R.drawable.ic_arrow_down)
                binding.textLuckyBoxBuyDeliveryDesc.visibility = View.GONE
            } else {
                binding.imageLuckyBoxBuyDeliveryArrow.setImageResource(R.drawable.ic_arrow_up)
                binding.textLuckyBoxBuyDeliveryDesc.visibility = View.VISIBLE
                hander.postDelayed({
                    binding.scrollLuckyBoxBuy.isSmoothScrollingEnabled = true
                    binding.scrollLuckyBoxBuy.scrollY = binding.viewLuckyBoxBuyLastBar.bottom
                }, 100)
            }
        }

        binding.textLuckyBoxBuyPay.setOnClickListener {

            if(mCount == 0){
                showAlert(R.string.msg_select_wow_box)
                return@setOnClickListener
            }

//            if (!binding.checkLuckyBoxBuyAgree.isChecked) {
//                showAlert(R.string.msg_agree_lucky_box_buy_terms)
//                return@setOnClickListener
//            }

            mLuckyBoxPurchase = LuckyBoxPurchase()
            mLuckyBoxPurchase!!.luckyboxSeqNo = mLuckyBox!!.seqNo
            mLuckyBoxPurchase!!.userKey = LoginInfoManager.getInstance().member!!.userKey
            mLuckyBoxPurchase!!.salesType = "delivery"
            mLuckyBoxPurchase!!.title = mLuckyBox!!.title
            mLuckyBoxPurchase!!.quantity = mCount
            mLuckyBoxPurchase!!.price = totalPrice.toFloat()
            mLuckyBoxPurchase!!.pgPrice = pgPrice
            mLuckyBoxPurchase!!.useCash = useCash
            mLuckyBoxPurchase!!.unitPrice = mLuckyBox!!.engagePrice!!.toFloat()

            showProgress("")
            ApiBuilder.create().saveLuckyBoxPurchase(mLuckyBoxPurchase!!).setCallback(object : PplusCallback<NewResultResponse<LuckyBoxPurchase>> {
                override fun onResponse(call: Call<NewResultResponse<LuckyBoxPurchase>>?,
                                        response: NewResultResponse<LuckyBoxPurchase>?) {
                    hideProgress()
                    if (response?.result != null) {
                        setEvent("wowbox_buyRequest")
                        val luckyBoxPurchase = response.result!!

                        if(luckyBoxPurchase.paymentMethod == "cash"){
                            val intent = Intent(this@LuckyBoxBuyActivity, LuckyBoxPayCompleteActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra(Const.DATA, luckyBoxPurchase)
                            resultLauncher.launch(intent)
                        }else{
                            val intent = Intent(this@LuckyBoxBuyActivity, LuckyBoxPgActivity::class.java)
                            intent.putExtra(Const.KEY, "luckybox")
                            intent.putExtra(Const.DATA, luckyBoxPurchase)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                        }

                    }
                }

                override fun onFailure(call: Call<NewResultResponse<LuckyBoxPurchase>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<LuckyBoxPurchase>?) {
                    hideProgress()
                    showAlert(R.string.msg_fail_pay)
                }
            }).build().call()
        }
//        binding.checkLuckyBoxBuyAgree.isChecked = true
        reloadSession()
    }

    var totalPrice = 0
    var pgPrice = 0
    var useCash = 0
    private fun setTotalPrice() {

        if(mCount == 0){
            binding.layoutLuckyBoxBuyPrice.visibility = View.GONE
        }else{
            binding.layoutLuckyBoxBuyPrice.visibility = View.VISIBLE
        }

        totalPrice = mLuckyBox!!.engagePrice!! * mCount
        if(mCount == 1){
            totalPrice += 3000
            binding.layoutLuckyBoxBuyServiceCharge.visibility = View.VISIBLE
            binding.textLuckyBoxBuyServiceCharge.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType("3000"))
        }else{
            binding.layoutLuckyBoxBuyServiceCharge.visibility = View.GONE
        }

        binding.textLuckyBoxBuyCount.text = getString(R.string.format_wow_box_count, mCount.toString())

        binding.textLuckyBoxBuyTotalPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(totalPrice.toString()))

        val useCashStr = binding.editLuckyBoxBuyUseCash.text.toString().trim()
        if(StringUtils.isNotEmpty(useCashStr) && useCashStr.toInt() > 0){
            binding.layoutLuckyBoxBuyUseCash.visibility = View.VISIBLE
            binding.textLuckyBoxBuyUseCash.text = "-${getString(R.string.format_money_unit, FormatUtil.getMoneyType(useCashStr))}"
            useCash = useCashStr.toInt()
        }else{
            binding.layoutLuckyBoxBuyUseCash.visibility = View.GONE
        }

        pgPrice = totalPrice - useCash

        binding.textLuckyBoxBuyPayPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(pgPrice.toString()))
    }
    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(RESULT_OK)
        finish()
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textLuckyBoxBuyRetentionCash.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.cash!!.toInt().toString())))
            }
        })
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_wow_box), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}