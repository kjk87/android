package com.pplus.prnumberbiz.apps.menu.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.GoodsCategory
import com.pplus.prnumberbiz.core.network.model.dto.PageGoodsCategory
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_alert_category_reg.*
import network.common.PplusCallback
import retrofit2.Call

class AlertCategoryRegActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_alert_category_reg
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val pageGoodsCategory = intent.getParcelableExtra<PageGoodsCategory>(Const.DATA)

        image_alert_category_reg_close.setOnClickListener {
            finish()
        }

        edit_alert_category_reg.setSingleLine()

        if (pageGoodsCategory == null) {
            text_alert_category_reg_title.setText(R.string.word_category_reg)
            text_alert_category_reg_desc.setText(R.string.msg_category_reg_desc)
            text_alert_category_reg.setText(R.string.word_reg)
        } else {
            text_alert_category_reg_title.setText(R.string.word_category_modify)
            text_alert_category_reg_desc.setText(R.string.msg_category_modify_desc)
            edit_alert_category_reg.setText(pageGoodsCategory.goodsCategory!!.name)
            text_alert_category_reg.setText(R.string.word_modified)
        }

        text_alert_category_reg.setOnClickListener {

            val name = edit_alert_category_reg.text.toString().trim()
            if (StringUtils.isEmpty(name)){
                showAlert(R.string.msg_input_category_name)
                return@setOnClickListener
            }

            if (pageGoodsCategory == null) {
                val goodsCategory = GoodsCategory()
                goodsCategory.name = name
                goodsCategory.depth = 1
                goodsCategory.sortNum = 1

                val params = PageGoodsCategory()
                params.pageSeqNo = LoginInfoManager.getInstance().user.page!!.no
                params.goodsCategory = goodsCategory

                showProgress("")
                ApiBuilder.create().postPageGoodsCategory(params).setCallback(object : PplusCallback<NewResultResponse<PageGoodsCategory>> {
                    override fun onResponse(call: Call<NewResultResponse<PageGoodsCategory>>?, response: NewResultResponse<PageGoodsCategory>?) {
                        hideProgress()
                        if (response != null) {
                            showAlert(R.string.msg_reged_category)
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<PageGoodsCategory>>?, t: Throwable?, response: NewResultResponse<PageGoodsCategory>?) {
                        hideProgress()
                    }
                }).build().call()

            }else{
                showProgress("")
                pageGoodsCategory.goodsCategory!!.name = name
                ApiBuilder.create().putPageGoodsCategory(pageGoodsCategory).setCallback(object : PplusCallback<NewResultResponse<PageGoodsCategory>> {
                    override fun onResponse(call: Call<NewResultResponse<PageGoodsCategory>>?, response: NewResultResponse<PageGoodsCategory>?) {
                        hideProgress()
                        if (response != null) {
                            showAlert(R.string.msg_modified_category)
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<PageGoodsCategory>>?, t: Throwable?, response: NewResultResponse<PageGoodsCategory>?) {
                        hideProgress()
                    }
                }).build().call()
            }
        }
    }

}
