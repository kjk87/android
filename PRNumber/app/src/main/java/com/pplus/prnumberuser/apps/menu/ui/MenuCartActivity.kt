package com.pplus.prnumberuser.apps.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.DeliveryAddressManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.menu.data.MenuCartAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.ApiController
import com.pplus.prnumberuser.core.network.model.dto.Cart
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.dto.ResultRiderFee
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityMenuCartBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MenuCartActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityMenuCartBinding

    override fun getLayoutView(): View {
        binding = ActivityMenuCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: MenuCartAdapter? = null
    var mPage: Page2? = null
    var mSalesType = 1
    var mDistance:Double? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mSalesType = intent.getIntExtra(Const.TYPE, 1)

        binding.recyclerMenuCart.layoutManager = LinearLayoutManager(this)
        mAdapter = MenuCartAdapter()
        binding.recyclerMenuCart.adapter = mAdapter

        mAdapter!!.listener = object : MenuCartAdapter.OnItemListener {
            override fun onItemClick(position: Int) {

            }

            override fun changeAmount() {
                setTotalPrice()
            }

            override fun delete(position: Int) {
                delete(mAdapter!!.getItem(position).seqNo!!)
            }
        }

        binding.textMenuCartAddMenu.setOnClickListener {
            val data = Intent()
            data.putExtra(Const.KEY, "add")
            setResult(RESULT_OK, data)
            finish()
        }

        binding.textMenuCartOrder.setOnClickListener {

            if(mPage!!.minOrderPrice != null && mPage!!.minOrderPrice!! > 0 && mPage!!.minOrderPrice!!.toInt() > totalMenuPrice){
                val lackPrice = mPage!!.minOrderPrice!!.toInt() - totalMenuPrice
                val intent = Intent(this, AlertLackOrderPriceActivity::class.java)
                intent.putExtra(Const.PRICE, lackPrice)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                lackPriceLauncher.launch(intent)
                return@setOnClickListener
            }

            when(mSalesType){
                1->{
                    val intent = Intent(this, AlertVisitOrderActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    visitTypeLauncher.launch(intent)
                }
                2->{
                    if (mPage!!.orderType!!.contains("2") && mPage!!.orderType!!.contains("3")) {
                        val intent = Intent(this, AlertSelectOrderTypeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        orderTypeLauncher.launch(intent)
                    } else {
                        val type: Int
                        if (mPage!!.orderType!!.contains("3")) {
                            type = 2
                        } else {
                            type = 5
                        }

                        val intent = Intent(this, OrderPurchasePgActivity::class.java)
                        intent.putExtra(Const.TYPE, type)
                        intent.putParcelableArrayListExtra(Const.DATA, mAdapter!!.mDataList as ArrayList<Cart>)
                        intent.putExtra(Const.PAGE, mPage)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        orderPurchaseLauncher.launch(intent)
                    }
                }
            }
        }


        listCall()
    }

    val orderTypeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                val type = data.getIntExtra(Const.TYPE, -1)
                if (type != -1) {
                    val intent = Intent(this, OrderPurchasePgActivity::class.java)
                    intent.putExtra(Const.TYPE, type)
                    intent.putParcelableArrayListExtra(Const.DATA, mAdapter!!.mDataList as ArrayList<Cart>)
                    intent.putExtra(Const.PAGE, mPage)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    orderPurchaseLauncher.launch(intent)
                }
            }
        }
    }

    val visitTypeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val intent = Intent(this, OrderPurchasePgActivity::class.java)
            intent.putExtra(Const.TYPE, 1)
            intent.putParcelableArrayListExtra(Const.DATA, mAdapter!!.mDataList as ArrayList<Cart>)
            intent.putExtra(Const.PAGE, mPage)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            orderPurchaseLauncher.launch(intent)
        }
    }

    val orderPurchaseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        listCall()
    }

    val lackPriceLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            val data = Intent()
            data.putExtra(Const.KEY, "add")
            setResult(RESULT_OK, data)
            finish()
        }
    }

    private fun listCall() {
        val params = HashMap<String, String>()
        params["salesType"] = mSalesType.toString()
        showProgress("")
        ApiBuilder.create().getCartList(params).setCallback(object : PplusCallback<NewResultResponse<Cart>> {
            override fun onResponse(call: Call<NewResultResponse<Cart>>?, response: NewResultResponse<Cart>?) {
                hideProgress()
                if (response?.datas != null && response.datas.isNotEmpty()) {
                    binding.layoutMenuCartNotExist.visibility = View.GONE
                    binding.textMenuCartPageName.visibility = View.VISIBLE
                    binding.viewMenuCartPageNameBar.visibility = View.VISIBLE
                    binding.layoutMenuCartBottom.visibility = View.VISIBLE

                    mPage = response.datas[0].page

                    binding.textMenuCartPageName.text = mPage!!.name
                    mAdapter!!.setDataList(response.datas)


                    if(mSalesType == 2){
                        if(mPage!!.orderType!!.contains("2") && !mPage!!.orderType!!.contains("3")){
                            getPage()
                            setTotalPrice()
                        }else{
                            val list = DeliveryAddressManager.getInstance().deliveryAddressList!!
                            when(mPage!!.riderType) { //1:배달대행사, 2:자체배달, 3:모두사용
                                2 -> {
                                    mDistance = PplusCommonUtil.calDistance(list[0].latitude!!, list[0].longitude!!, mPage!!.latitude!!, mPage!!.longitude!!)/1000f

                                    getPage()
                                    setTotalPrice()
                                }
                                else->{
                                    val params = HashMap<String, String>()
                                    params["sido"] = list[0].sido!!
                                    params["sigungu"] = list[0].sigungu!!

                                    if(list[0].dongli!!.endsWith("면")){
                                        params["myeon"] = list[0].dongli!!
                                    }else{
                                        params["dongli"] = list[0].dongli!!
                                    }

                                    params["roadAddress"] = list[0].address!!
                                    params["jibunAddress"] = list[0].jibunAddress!!
                                    params["addressDetail"] = list[0].addressDetail!!
                                    params["roadName"] = list[0].roadName!!
                                    params["buildNo"] = list[0].buildNo!!
                                    params["pageSeqNo"] = mPage!!.seqNo.toString()
                                    params["price"] = "10000"
                                    LogUtil.e(LOG_TAG, params.toString())
                                    showProgress("")
                                    ApiController.getCSApi().getRiderFee(params).enqueue(object : Callback<ResultRiderFee> {
                                        override fun onResponse(call: Call<ResultRiderFee>, response: Response<ResultRiderFee>) {
                                            hideProgress()
                                            if(response.body() != null){
                                                if(response.body()!!.result != null){
                                                    val data = response.body()!!.result!!
                                                    LogUtil.e(LOG_TAG, "data.delivery_distance : "+data.delivery_distance)
                                                    if(StringUtils.isNotEmpty(data.delivery_distance)){
                                                        mDistance = data.delivery_distance!!.toDouble()/1000f
                                                    }else{
                                                        mDistance = 1.3
                                                    }
                                                }else{
                                                    mDistance = 1.3
                                                }

                                                getPage()
                                                setTotalPrice()
                                            }
                                        }

                                        override fun onFailure(call: Call<ResultRiderFee>, t: Throwable) {
                                            hideProgress()
                                        }
                                    })
                                }
                            }
                        }


                    }else{
                        getPage()
                        setTotalPrice()
                    }
                } else {
                    binding.layoutMenuCartNotExist.visibility = View.VISIBLE
                    binding.textMenuCartPageName.visibility = View.GONE
                    binding.viewMenuCartPageNameBar.visibility = View.GONE
                    binding.layoutMenuCartBottom.visibility = View.GONE
                    mAdapter!!.clear()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Cart>>?, t: Throwable?, response: NewResultResponse<Cart>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getPage(){
        val params = HashMap<String, String>()
        params["seqNo"] = mPage!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getPage2(params).setCallback(object : PplusCallback<NewResultResponse<Page2>> {
            override fun onResponse(call: Call<NewResultResponse<Page2>>?, response: NewResultResponse<Page2>?) {
                hideProgress()
                if (response?.data != null){
                    mPage = response.data

                    if((mPage!!.businessHoursType == 4 || mPage!!.isBusinessHour!!) && !mPage!!.isDayOff!! && !mPage!!.isTimeOff!! && mPage!!.orderable!!){

                        binding.textMenuCartOff.visibility = View.GONE
                        binding.layoutMenuCartOrder.visibility = View.VISIBLE

                        when(mSalesType){
                            1->{
                                binding.layoutMenuCartOrder.visibility = View.VISIBLE
                                binding.textMenuCartOff.visibility = View.GONE
                                binding.layoutMenuCartDeliveryPrice.visibility = View.GONE
                            }
                            2->{

                                if(mPage!!.orderType!!.contains("2") && !mPage!!.orderType!!.contains("3")){
                                    binding.layoutMenuCartDeliveryPrice.visibility = View.GONE
                                }else{
                                    if(mDistance!! > mPage!!.riderDistance!!*1000f){
                                        binding.layoutMenuCartOrder.visibility = View.GONE
                                        binding.textMenuCartOff.visibility = View.VISIBLE
                                        binding.textMenuCartOff.text = getString(R.string.msg_can_not_delivery)
                                    }else{
                                        binding.layoutMenuCartOrder.visibility = View.VISIBLE
                                        binding.textMenuCartOff.visibility = View.GONE
                                        binding.layoutMenuCartDeliveryPrice.visibility = View.VISIBLE
                                    }
                                }

                            }
                        }

                    }else{
                        binding.textMenuCartOff.visibility = View.VISIBLE
                        binding.layoutMenuCartOrder.visibility = View.GONE
                        binding.textMenuCartOff.text = getString(R.string.msg_not_ready_now)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page2>>?, t: Throwable?, response: NewResultResponse<Page2>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun delete(cartSeqNo: Long) {
        val params = HashMap<String, String>()
        params["cartSeqNo"] = cartSeqNo.toString()
        showProgress("")
        ApiBuilder.create().deleteCart(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                listCall()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    var totalPrice = 0
    var totalMenuPrice = 0

    fun setTotalPrice() {
        totalPrice = 0
        totalMenuPrice = 0
        for (cart in mAdapter!!.mDataList!!) {
            var optionPrice = 0
            for ((i, cartOption) in cart.cartOptionList!!.withIndex()) {

                if (cartOption.menuOptionDetail!!.price == null) {
                    cartOption.menuOptionDetail!!.price = 0f
                }

                optionPrice += cartOption.menuOptionDetail!!.price!!.toInt()
            }
            totalPrice += (cart.orderMenu!!.price!!.toInt() + optionPrice) * cart.amount!!
            totalMenuPrice += (cart.orderMenu!!.price!!.toInt() + optionPrice) * cart.amount!!
        }

        if(mSalesType == 2){
            if(mPage!!.orderType!!.contains("2") && !mPage!!.orderType!!.contains("3")){
                binding.textMenuCartDeliveryPrice.text = getString(R.string.word_free_delivery_price)
            }else{
                if (mPage!!.riderFreePrice != null && mPage!!.riderFreePrice!! > 0) {
                    if (totalPrice >= mPage!!.riderFreePrice!!) {
                        binding.textMenuCartDeliveryPrice.text = getString(R.string.word_free_delivery_price)
                    } else {
                        if(mPage!!.riderFee != null && mPage!!.riderFee!! > 0){

                            var riderFee = mPage!!.riderFee!!

                            var addRiderFeeDistance = mPage!!.addRiderFeeDistance
                            if(addRiderFeeDistance == null){
                                addRiderFeeDistance = 1.3f
                            }

                            var addRiderFee = mPage!!.addRiderFee
                            if(addRiderFee == null){
                                addRiderFee = 1000f
                            }

                            if(mDistance!!.toFloat() > addRiderFeeDistance){
                                LogUtil.e(LOG_TAG, "distance : "+addRiderFeeDistance + " addRiderFeeDistance : "+addRiderFeeDistance)
//                                val totalAddRiderFee = Math.floor(mDistance!!/addRiderFeeDistance).toInt()*addRiderFee
                                riderFee += addRiderFee.toInt()
                            }

                            binding.textMenuCartDeliveryPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(riderFee.toString()))
                            totalPrice += riderFee
                        }else{
                            binding.textMenuCartDeliveryPrice.text = getString(R.string.word_free_delivery_price)
                        }

                    }
                } else {
                    if(mPage!!.riderFee != null && mPage!!.riderFee!! > 0){
                        var riderFee = mPage!!.riderFee!!

                        var addRiderFeeDistance = mPage!!.addRiderFeeDistance
                        if(addRiderFeeDistance == null){
                            addRiderFeeDistance = 1.3f
                        }

                        var addRiderFee = mPage!!.addRiderFee
                        if(addRiderFee == null){
                            addRiderFee = 1000f
                        }

                        if(mDistance!!.toFloat() > addRiderFeeDistance){
                            LogUtil.e(LOG_TAG, "distance : "+addRiderFeeDistance + " addRiderFeeDistance : "+addRiderFeeDistance)
//                            val totalAddRiderFee = Math.floor(mDistance!!/addRiderFeeDistance).toInt()*addRiderFee
                            riderFee += addRiderFee.toInt()
                        }

                        binding.textMenuCartDeliveryPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(riderFee.toString()))
                        totalPrice += riderFee
                    }else{
                        binding.textMenuCartDeliveryPrice.text = getString(R.string.word_free_delivery_price)
                    }
                }
            }

        }


        binding.textMenuCartTotalPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(totalPrice.toString()))

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_cart), ToolbarOption.ToolbarMenu.LEFT)
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