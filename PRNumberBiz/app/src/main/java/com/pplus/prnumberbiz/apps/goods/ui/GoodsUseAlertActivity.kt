package com.pplus.prnumberbiz.apps.goods.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_goods_use_alert.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.HashMap

class GoodsUseAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_use_alert
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val buyGoods = intent.getParcelableExtra<BuyGoods>(Const.DATA)

        layout_goods_use_alert_confirm.setOnClickListener {
            val intent = Intent(this, GoodsSaleHistoryDetailActivity::class.java)
            intent.putExtra(Const.DATA, buyGoods)
            startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
            finish()
        }

        val params = HashMap<String, String>()
        params["seqNo"] = buyGoods.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getOneBuyGoods(params).setCallback(object : PplusCallback<NewResultResponse<BuyGoods>> {

            override fun onResponse(call: Call<NewResultResponse<BuyGoods>>?, response: NewResultResponse<BuyGoods>?) {
                hideProgress()
                if (response != null) {
                    val data = response.data

                    if (data.goods != null) {
                        text_goods_use_alert_name.text = data.goods!!.name
                        if (data.goods!!.attachments != null && data.goods!!.attachments!!.images != null && data.goods!!.attachments!!.images!!.isNotEmpty()) {
                            val id = data.goods!!.attachments!!.images!![0]

                            val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
                            Glide.with(this@GoodsUseAlertActivity).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_goods_use_alert_image)
                        } else {
                            image_goods_use_alert_image.setImageResource(R.drawable.prnumber_default_img)
                        }
                    }

                    val count = FormatUtil.getMoneyType(data.count.toString())

                    var payMethod = ""

                    if (data.buy != null) {
                        text_goods_use_alert_description.text = getString(R.string.format_goods_use_alert, data.buy!!.buyerName)
                        text_goods_use_alert_info.text = getString(R.string.format_goods_use_alert_detail, data.buy!!.buyerName, count, FormatUtil.getMoneyType(data.price.toString()))
                    }

//                    when (data.process) {
//                        EnumData.BuyGoodsProcess.PAY.process -> {
//
//                        }
//                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BuyGoods>>?, t: Throwable?, response: NewResultResponse<BuyGoods>?) {
                hideProgress()
            }
        }).build().call()
    }

}
