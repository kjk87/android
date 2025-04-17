package com.lejel.wowbox.apps.product.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.product.data.ProductOptionDetailAdapter
import com.lejel.wowbox.apps.product.data.ProductOptionItemAdapter
import com.lejel.wowbox.core.network.model.dto.ProductOptionDetail
import com.lejel.wowbox.core.network.model.dto.ProductOptionItem
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertProductOptionBinding

class AlertProductOptionActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertProductOptionBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertProductOptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val name = intent.getStringExtra(Const.NAME)
        val itemList = PplusCommonUtil.getParcelableArrayListExtra(intent, Const.ITEM, ProductOptionItem::class.java)

        binding.recyclerAlertProductOption.layoutManager = LinearLayoutManager(this)

        binding.textAlertProductOptionName.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        if(itemList == null){
            val detailList = PplusCommonUtil.getParcelableArrayListExtra(intent, Const.DETAIL,  ProductOptionDetail::class.java)!!
            val optionType = intent.getStringExtra(Const.OPTION_TYPE)
            val adapter = ProductOptionDetailAdapter(optionType)
            binding.recyclerAlertProductOption.adapter = adapter
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
            binding.recyclerAlertProductOption.adapter = adapter
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

        binding.textAlertProductOptionName.text = name

    }

}
