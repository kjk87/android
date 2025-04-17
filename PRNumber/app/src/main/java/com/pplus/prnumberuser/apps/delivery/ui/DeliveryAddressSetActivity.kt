package com.pplus.prnumberuser.apps.delivery.ui

import android.app.Activity
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
import com.pplus.prnumberuser.apps.delivery.data.DeliveryAddressAdapter
import com.pplus.prnumberuser.apps.shippingsite.ui.SearchAddressActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Coord
import com.pplus.prnumberuser.core.network.model.dto.DeliveryAddress
import com.pplus.prnumberuser.core.network.model.dto.SearchAddressJuso
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityDeliveryAddressSetBinding
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap

class DeliveryAddressSetActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityDeliveryAddressSetBinding

    override fun getLayoutView(): View {
        binding = ActivityDeliveryAddressSetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mAdapter: DeliveryAddressAdapter

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.layoutDeliveryAddressSetSearch.setOnClickListener {
            val intent = Intent(this, SearchAddressActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            searchAddressLauncher.launch(intent)
        }

        binding.textDeliveryAddressSetFindCurrentLocation.setOnClickListener {
            val intent = Intent(this, DeliveryAddressFindMapActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            detailAddressLauncher.launch(intent)
        }

        binding.imageDeliveryAddressSetFindCurrentLocation.setOnClickListener {
            val intent = Intent(this, DeliveryAddressFindMapActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            detailAddressLauncher.launch(intent)
        }

        binding.recyclerDeliveryAddressSet.layoutManager = LinearLayoutManager(this)
        mAdapter = DeliveryAddressAdapter()
        binding.recyclerDeliveryAddressSet.adapter = mAdapter

        mAdapter.listener = object : DeliveryAddressAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val list = mAdapter.mDataList!!
                val item = list[position]
                list.remove(item)
                list.add(0, item)

                DeliveryAddressManager.getInstance().deliveryAddressList = list
                DeliveryAddressManager.getInstance().save()
                setResult(RESULT_OK)
                finish()
            }

            override fun onItemDelete(position: Int) {
                mAdapter.mDataList!!.removeAt(position)
                DeliveryAddressManager.getInstance().deliveryAddressList = mAdapter.mDataList!!
                DeliveryAddressManager.getInstance().save()
                mAdapter.notifyDataSetChanged()
            }
        }

        if (DeliveryAddressManager.getInstance().deliveryAddressList != null) {
            mAdapter.setDataList(DeliveryAddressManager.getInstance().deliveryAddressList!!)
        }
    }

    val detailAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if (data != null) {
            val deliveryAddress = data.getParcelableExtra<DeliveryAddress>(Const.DATA)
            if (DeliveryAddressManager.getInstance().deliveryAddressList == null) {
                DeliveryAddressManager.getInstance().deliveryAddressList = mutableListOf()
            }
            DeliveryAddressManager.getInstance().deliveryAddressList!!.add(0, deliveryAddress!!)
            DeliveryAddressManager.getInstance().save()
//            mAdapter.setDataList(DeliveryAddressManager.getInstance().deliveryAddressList!!)
//            mAdapter.notifyDataSetChanged()

            setResult(RESULT_OK)
            finish()
        }
    }

    val findMapLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {
                val deliveryAddress = data.getParcelableExtra<DeliveryAddress>(Const.DATA)
                val intent = Intent(this@DeliveryAddressSetActivity, DeliveryAddressDetailSetActivity::class.java)
                intent.putExtra(Const.DATA, deliveryAddress)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                detailAddressLauncher.launch(intent)
            }
        }
    }

    val searchAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {
                val juso = data.getParcelableExtra<SearchAddressJuso>(Const.ADDRESS)
                val deliveryAddress = DeliveryAddress()
//                deliveryAddress.address = juso!!.roadAddr
                deliveryAddress.address = juso!!.roadAddrPart1
                deliveryAddress.zipNo = juso.zipNo
                callLatLon(deliveryAddress)
            }
        }
    }

    private fun callLatLon(deliveryAddress: DeliveryAddress) {

        val params = HashMap<String, String>()
        params["address"] = deliveryAddress.address!!
        showProgress("")
        ApiBuilder.create().getCoordByAddress(params).setCallback(object : PplusCallback<NewResultResponse<Coord>> {

            override fun onResponse(call: Call<NewResultResponse<Coord>>, response: NewResultResponse<Coord>) {

                hideProgress()
                if (response.data != null) {
                    deliveryAddress.latitude = response.data!!.y
                    deliveryAddress.longitude = response.data!!.x

                    val intent = Intent(this@DeliveryAddressSetActivity, DeliveryAddressFindMapActivity::class.java)
                    intent.putExtra(Const.X, response.data!!.x)
                    intent.putExtra(Const.Y, response.data!!.y)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    detailAddressLauncher.launch(intent)

                    //                    val intent = Intent(this@DeliveryAddressSetActivity, DeliveryAddressDetailSetActivity::class.java)
                    //                    intent.putExtra(Const.DATA, deliveryAddress)
                    //                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    //                    detailAddressLauncher.launch(intent)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Coord>>, t: Throwable, response: NewResultResponse<Coord>) {

                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_set_delivery_address), ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if(existDeliveryAddress()){
            super.onBackPressed()
        }else{
            showAlert(R.string.msg_set_delivery_address)
        }

    }

    private fun existDeliveryAddress():Boolean{
        if(DeliveryAddressManager.getInstance().deliveryAddressList != null && DeliveryAddressManager.getInstance().deliveryAddressList!!.isNotEmpty()){
            return true
        }else{
            return false
        }
    }
}