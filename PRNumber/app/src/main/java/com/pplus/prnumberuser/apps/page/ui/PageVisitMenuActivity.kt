package com.pplus.prnumberuser.apps.page.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.menu.ui.MenuCartActivity
import com.pplus.prnumberuser.apps.page.data.PageMenuStickyHeaderAdapter
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Cart
import com.pplus.prnumberuser.core.network.model.dto.OrderMenu
import com.pplus.prnumberuser.core.network.model.dto.OrderMenuGroup
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityPageDeliveryMenuBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call
import java.util.*

class PageVisitMenuActivity : BaseActivity() {

    private lateinit var binding: ActivityPageDeliveryMenuBinding

    override fun getLayoutView(): View {
        binding = ActivityPageDeliveryMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mPage: Page2? = null
    var mAdapter: PageMenuStickyHeaderAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.DATA)

        val layoutManager = LinearLayoutManager(this)

        binding.textPageMenuBack.setOnClickListener {
            onBackPressed()
        }

        binding.textPageMenuBack2.setOnClickListener {
            onBackPressed()
        }

        binding.layoutPageMenuCart.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }
            val intent = Intent(this, MenuCartActivity::class.java)
            intent.putExtra(Const.TYPE, 1)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            menuDetailLauncher.launch(intent)
        }

        binding.layoutPageMenuCart2.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }
            val intent = Intent(this, MenuCartActivity::class.java)
            intent.putExtra(Const.TYPE, 1)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            menuDetailLauncher.launch(intent)
        }
        binding.textPageDeliveryMenuPriceOrder.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }
            val intent = Intent(this, MenuCartActivity::class.java)
            intent.putExtra(Const.TYPE, 1)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            menuDetailLauncher.launch(intent)
        }

        binding.recyclerPageDeliveryMenu.layoutManager = layoutManager
        binding.recyclerPageDeliveryMenu.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_200))
        binding.recyclerPageDeliveryMenu.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val position = layoutManager.findFirstVisibleItemPosition()
                if (position > 0 || recyclerView.computeVerticalScrollOffset() >= resources.getDimensionPixelSize(R.dimen.height_732)) {
                    binding.layoutPageMenuTitle.visibility = View.VISIBLE
                } else {
                    binding.layoutPageMenuTitle.visibility = View.GONE
                }



                if ((mAdapter!!.topHeight > 0 && recyclerView.computeVerticalScrollOffset() >= mAdapter!!.topHeight - resources.getDimensionPixelSize(R.dimen.height_168)) || position > 0) {
                    binding.layoutPageDeliveryMenuGroup.visibility = View.VISIBLE
                } else {
                    binding.layoutPageDeliveryMenuGroup.visibility = View.GONE
                }
            }

        })

        binding.tabLayoutPageDeliveryMenu.setSelectedIndicatorColors(ContextCompat.getColor(this, android.R.color.transparent))
        binding.tabLayoutPageDeliveryMenu.setCustomTabView(R.layout.item_menu_group, R.id.text_menu_group)
        binding.tabLayoutPageDeliveryMenu.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_0)) //        tabLayout_category_page.setDistributeEvenly(false)
        binding.tabLayoutPageDeliveryMenu.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_12), 0)

        getPage()
    }

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
                outRect.bottom = mLastOffset
            }
        }
    }

    val menuDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(mPage!!.businessCategory == "service"){
            binding.textPageDeliveryMenuOff.visibility = View.GONE
            binding.textPageDeliveryMenuPriceOrder.visibility = View.GONE
            binding.layoutPageMenuCart.visibility = View.GONE
            binding.layoutPageMenuCart2.visibility = View.GONE
        }else{
            if ((mPage!!.businessHoursType == 4 || mPage!!.isBusinessHour!!) && !mPage!!.isDayOff!! && !mPage!!.isTimeOff!! && mPage!!.orderable!!) {
                binding.textPageDeliveryMenuOff.visibility = View.GONE
                binding.textPageDeliveryMenuPriceOrder.visibility = View.GONE
                binding.layoutPageMenuCart.visibility = View.VISIBLE
                binding.layoutPageMenuCart2.visibility = View.VISIBLE
                if (LoginInfoManager.getInstance().isMember) {
                    getCartCount()
                }

            } else {
                binding.textPageDeliveryMenuOff.visibility = View.VISIBLE
                binding.textPageDeliveryMenuPriceOrder.visibility = View.GONE
                binding.layoutPageMenuCart.visibility = View.GONE
                binding.layoutPageMenuCart2.visibility = View.GONE
            }
        }

    }

    private fun getPage() {
        val params = HashMap<String, String>()
        params["seqNo"] = mPage!!.seqNo.toString()
        val locationData = LocationUtil.specifyLocationData
        if(locationData != null){
            params["latitude"] = locationData.latitude.toString()
            params["longitude"] = locationData.longitude.toString()
        }

        showProgress("")
        ApiBuilder.create().getPage2(params).setCallback(object : PplusCallback<NewResultResponse<Page2>> {
            override fun onResponse(call: Call<NewResultResponse<Page2>>?, response: NewResultResponse<Page2>?) {
                hideProgress()
                if (response?.data != null) {
                    mPage = response.data

                    binding.textPageMenuTitle.text = mPage!!.name

                    mAdapter = PageMenuStickyHeaderAdapter(mPage!!, binding.recyclerPageDeliveryMenu)
                    mAdapter!!.mIsVisit = true
                    binding.recyclerPageDeliveryMenu.adapter = mAdapter
                    getDelegateOrderMenuList()

                    if(mPage!!.businessCategory == "service"){
                        binding.textPageDeliveryMenuOff.visibility = View.GONE
                        binding.textPageDeliveryMenuPriceOrder.visibility = View.GONE
                        binding.layoutPageMenuCart.visibility = View.GONE
                        binding.layoutPageMenuCart2.visibility = View.GONE
                    }else{
                        if ((mPage!!.businessHoursType == 4 || mPage!!.isBusinessHour!!) && !mPage!!.isDayOff!! && !mPage!!.isTimeOff!! && mPage!!.orderable!!) {
                            binding.textPageDeliveryMenuOff.visibility = View.GONE
                            binding.textPageDeliveryMenuPriceOrder.visibility = View.GONE
                            binding.layoutPageMenuCart.visibility = View.VISIBLE
                            binding.layoutPageMenuCart2.visibility = View.VISIBLE
                            if (LoginInfoManager.getInstance().isMember) {
                                getCartCount()
                            }
                        } else {
                            binding.textPageDeliveryMenuOff.visibility = View.VISIBLE
                            binding.textPageDeliveryMenuPriceOrder.visibility = View.GONE
                            binding.layoutPageMenuCart.visibility = View.GONE
                            binding.layoutPageMenuCart2.visibility = View.GONE
                        }
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page2>>?, t: Throwable?, response: NewResultResponse<Page2>?) {
                hideProgress()
            }
        }).build().call()
    }

    var mDelegateList: MutableList<OrderMenu>? = null

    private fun getDelegateOrderMenuList() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getDelegateOrderMenuList(params).setCallback(object : PplusCallback<NewResultResponse<OrderMenu>> {
            override fun onResponse(call: Call<NewResultResponse<OrderMenu>>?, response: NewResultResponse<OrderMenu>?) {
                hideProgress()
                if (response?.datas != null && response.datas.isNotEmpty()) {
                    mDelegateList = response.datas
                }
                getOrderMenuGroupWithMenuList()
            }

            override fun onFailure(call: Call<NewResultResponse<OrderMenu>>?, t: Throwable?, response: NewResultResponse<OrderMenu>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getOrderMenuGroupWithMenuList() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getOrderMenuGroupWithMenuList(params).setCallback(object : PplusCallback<NewResultResponse<OrderMenuGroup>> {
            override fun onResponse(call: Call<NewResultResponse<OrderMenuGroup>>?, response: NewResultResponse<OrderMenuGroup>?) {
                hideProgress()
                if (response?.datas != null) {
                    val titleList = arrayListOf<String>()
                    if (mDelegateList != null && mDelegateList!!.isNotEmpty()) {
                        titleList.add(getString(R.string.word_main_menu))
                        mAdapter!!.mDelegateList = mDelegateList
                    }

                    for (group in response.datas) {
                        titleList.add(group.name!!)
                    }

                    binding.tabLayoutPageDeliveryMenu.setRecyclerView(binding.recyclerPageDeliveryMenu, titleList)
                    mAdapter!!.mDataList = response.datas
                    mAdapter!!.setListData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<OrderMenuGroup>>?, t: Throwable?, response: NewResultResponse<OrderMenuGroup>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getCartCount() {
        val params = HashMap<String, String>()
        params["salesType"] = "1"
        ApiBuilder.create().getCartList(params).setCallback(object : PplusCallback<NewResultResponse<Cart>> {
            override fun onResponse(call: Call<NewResultResponse<Cart>>?, response: NewResultResponse<Cart>?) {
                hideProgress()
                if (response?.datas != null && response.datas.isNotEmpty()) {

                    binding.textPageMenuCartCount.visibility = View.VISIBLE
                    binding.textPageMenuCartCount2.visibility = View.VISIBLE
                    binding.textPageMenuCartCount.text = response.datas.size.toString()
                    binding.textPageMenuCartCount2.text = response.datas.size.toString()
                    binding.textPageDeliveryMenuPriceOrder.visibility = View.VISIBLE
                    setTotalPrice(response.datas)
                } else {
                    binding.textPageMenuCartCount.visibility = View.GONE
                    binding.textPageMenuCartCount2.visibility = View.GONE
                    binding.textPageDeliveryMenuPriceOrder.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Cart>>?, t: Throwable?, response: NewResultResponse<Cart>?) {
                hideProgress()
            }
        }).build().call()
    }

    fun setTotalPrice(cartList: List<Cart>) {
        var totalPrice = 0
        for (cart in cartList) {
            var optionPrice = 0
            for ((i, cartOption) in cart.cartOptionList!!.withIndex()) {

                if (cartOption.menuOptionDetail!!.price == null) {
                    cartOption.menuOptionDetail!!.price = 0f
                }

                optionPrice += cartOption.menuOptionDetail!!.price!!.toInt()
            }
            totalPrice += (cart.orderMenu!!.price!!.toInt() + optionPrice) * cart.amount!!
        }


        binding.textPageDeliveryMenuPriceOrder.text = getString(R.string.format_price_order, FormatUtil.getMoneyType(totalPrice.toString()))

    }

}