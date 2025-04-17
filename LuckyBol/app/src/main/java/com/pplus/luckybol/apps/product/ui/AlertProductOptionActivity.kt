package com.pplus.luckybol.apps.product.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.product.data.ProductOptionDetailAdapter
import com.pplus.luckybol.apps.product.data.ProductOptionItemAdapter
import com.pplus.luckybol.core.network.model.dto.ProductOptionDetail
import com.pplus.luckybol.core.network.model.dto.ProductOptionItem
import com.pplus.luckybol.databinding.ActivityAlertGoodsOptionBinding

class AlertProductOptionActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertGoodsOptionBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertGoodsOptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val name = intent.getStringExtra(Const.NAME)
        val itemList = intent.getParcelableArrayListExtra<ProductOptionItem>(Const.ITEM)

        binding.recyclerAlertGoodsOption.layoutManager = LinearLayoutManager(this)

        binding.textAlertGoodsOptionName.setOnClickListener {
            onBackPressed()
        }


        if(itemList == null){
            val detailList = intent.getParcelableArrayListExtra<ProductOptionDetail>(Const.DETAIL)!!
            val optionType = intent.getStringExtra(Const.OPTION_TYPE)
            val adapter = ProductOptionDetailAdapter(optionType)
            binding.recyclerAlertGoodsOption.adapter = adapter
            adapter.setDataList(detailList)
            adapter.setOnItemClickListener(object : ProductOptionDetailAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    val detail = detailList[position]
                    val data = Intent()
                    data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, 0))
                    data.putExtra(Const.DATA, detail)
                    setResult(Activity.RESULT_OK, data)
                    finish()
                }
            })
        }else{
            val adapter = ProductOptionItemAdapter()
            binding.recyclerAlertGoodsOption.adapter = adapter
            adapter.setDataList(itemList)
            adapter.setOnItemClickListener(object : ProductOptionItemAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    val item = itemList[position]
                    val data = Intent()
                    data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, 0))
                    data.putExtra(Const.DATA, item)
                    setResult(Activity.RESULT_OK, data)
                    finish()
                }
            })
        }

        binding.textAlertGoodsOptionName.text = name

    }

}
