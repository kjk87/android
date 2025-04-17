package com.pplus.prnumberuser.apps.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.NestedScrollView
import androidx.viewpager2.widget.ViewPager2
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.menu.data.MainImageAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.*
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class TicketDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityTicketDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityTicketDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mOrderMenu: OrderMenu
    var mAdapter: MainImageAdapter? = null
    var mPage: Page2? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mOrderMenu = intent.getParcelableExtra(Const.DATA)!!
        mPage = intent.getParcelableExtra(Const.PAGE)

        if (mOrderMenu.imageList != null && mOrderMenu.imageList!!.isNotEmpty()) {
            binding.layoutTicketDetailImage.visibility = View.VISIBLE
            binding.viewTicketDetailNoneImage.visibility = View.GONE
            binding.textTicketDetailBack.setImageResource(R.drawable.ic_navbar_back_light)
        } else {
            binding.layoutTicketDetailImage.visibility = View.GONE
            binding.viewTicketDetailNoneImage.visibility = View.VISIBLE
            binding.textTicketDetailBack.setImageResource(R.drawable.ic_navbar_back)
        }

        binding.textTicketDetailBack.setOnClickListener {
            onBackPressed()
        }

        binding.scrollTicketDetail.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            LogUtil.e(LOG_TAG, "scrollY : {}", scrollY)
            if (scrollY >= resources.getDimensionPixelSize(R.dimen.height_168)) {
                binding.layoutTicketDetailTitle.visibility = View.VISIBLE
            } else {
                binding.layoutTicketDetailTitle.visibility = View.GONE
            }
        })

        mAdapter = MainImageAdapter()
        binding.pagerOrderMenuImage.adapter = mAdapter
        binding.pagerOrderMenuImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.indicatorTicketDetailImage.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        binding.textTicketDetailOrder.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }

            val intent = Intent(this, AlertTicketOptionActivity::class.java)
            intent.putExtra(Const.DATA, mOrderMenu)
            intent.putExtra(Const.PAGE, mPage)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            optionLauncher.launch(intent)
            overridePendingTransition(R.anim.view_up, R.anim.fix)
        }

        getMenu()
    }



    fun createToast(): Toast {
        val binding = CartToastBinding.inflate(layoutInflater)

        binding.textCartToast.text = getString(R.string.msg_added_cart)

        return Toast(this).apply {
            setGravity(Gravity.TOP or Gravity.END, resources.getDimensionPixelSize(R.dimen.width_48), resources.getDimensionPixelSize(R.dimen.height_138))
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }

    private fun getMenu() {
        val params = HashMap<String, String>()
        params["seqNo"] = mOrderMenu.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getMenu(params).setCallback(object : PplusCallback<NewResultResponse<OrderMenu>> {
            override fun onResponse(call: Call<NewResultResponse<OrderMenu>>?, response: NewResultResponse<OrderMenu>?) {
                hideProgress()
                if (response?.data != null) {
                    mOrderMenu = response.data

                    if (mOrderMenu.imageList != null && mOrderMenu.imageList!!.isNotEmpty()) {
                        if (mOrderMenu.imageList!!.size > 1) {
                            binding.indicatorTicketDetailImage.removeAllViews()
                            binding.indicatorTicketDetailImage.visibility = View.VISIBLE
                            binding.indicatorTicketDetailImage.build(LinearLayout.HORIZONTAL, mOrderMenu.imageList!!.size)
                        } else {
                            binding.indicatorTicketDetailImage.visibility = View.GONE
                        }
                        mAdapter!!.setDataList(mOrderMenu.imageList!!.toMutableList())
                    }

                    binding.textTicketDetailTitle.text = mOrderMenu.title
                    binding.textTicketDetailTitle2.text = mOrderMenu.title
                    binding.textTicketDetailTitle2.setSingleLine()

                    if (mOrderMenu.delegate != null && mOrderMenu.delegate!!) {
                        binding.textTicketDetailDelegate.visibility = View.VISIBLE
                    } else {
                        binding.textTicketDetailDelegate.visibility = View.GONE
                    }

                    if (StringUtils.isNotEmpty(mOrderMenu.menuInfo)) {
                        binding.textTicketDetailInfo.visibility = View.VISIBLE
                        binding.textTicketDetailInfo.text = mOrderMenu.menuInfo
                    } else {
                        binding.textTicketDetailInfo.visibility = View.GONE
                    }

                    binding.textTicketDetailPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mOrderMenu.price!!.toInt().toString()))

                    if (mOrderMenu.originPrice != null && mOrderMenu.originPrice!! > 0) {

                        if (mOrderMenu.originPrice!! <= mOrderMenu.price!!) {
                            binding.layoutTicketDetailOriginPrice.visibility = View.GONE
                        } else {
                            binding.textTicketDetailOriginPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mOrderMenu.originPrice!!.toInt().toString()))
                            binding.layoutTicketDetailOriginPrice.visibility = View.VISIBLE
                        }

                        if(mOrderMenu.discount != null && mOrderMenu.discount!! > 0){
                            binding.textTicketDetailDiscount.visibility = View.VISIBLE

                            val discountRatio = mOrderMenu.discount!!/mOrderMenu.originPrice!!*100
                            binding.textTicketDetailDiscount.text = getString(R.string.format_discount, discountRatio.toInt().toString())
                        }else{
                            binding.textTicketDetailDiscount.visibility = View.GONE
                        }

                    } else {
                        binding.layoutTicketDetailOriginPrice.visibility = View.GONE
                    }

                    binding.textTicketDetailPoint.text = getString(R.string.format_will_save, FormatUtil.getMoneyType((mOrderMenu.price!! * 0.05).toInt().toString()))

                    when(mOrderMenu.expireType){
                        "date"->{
                            binding.textPrepaymentDetailExpireDays.text = mOrderMenu.expireDate
                        }
                        "number"->{
                            binding.textPrepaymentDetailExpireDays.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_days, mOrderMenu.remainDate.toString()))
                        }
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<OrderMenu>>?, t: Throwable?, response: NewResultResponse<OrderMenu>?) {
                hideProgress()
            }
        }).build().call()
    }

    val optionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {

            }
        }

    }
}