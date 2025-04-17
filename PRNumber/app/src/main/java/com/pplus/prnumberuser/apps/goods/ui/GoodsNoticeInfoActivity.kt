//package com.pplus.prnumberuser.apps.goods.ui
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import android.view.View
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsNoticeInfoAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsNoticeInfo
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_goods_notice_info.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.HashMap
//
//class GoodsNoticeInfoActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_goods_notice_info
//    }
//
//    var mGoods: Goods? = null
//    var mAdapter: GoodsNoticeInfoAdapter? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mGoods = intent.getParcelableExtra(Const.DATA)
//
//        when(mGoods!!.page!!.goodsNotiType){
//            EnumData.GoodsNoticeType.text.name->{
//                recycler_goods_notice_info.visibility = View.GONE
//                val goodsNotification = mGoods!!.page!!.goodsNotification
//                if (StringUtils.isNotEmpty(goodsNotification)){
//                    text_goods_notice_info_text.visibility = View.VISIBLE
//                    text_goods_notice_info_not_exist.visibility = View.GONE
//                    text_goods_notice_info_text.text = goodsNotification
//                }else{
//                    text_goods_notice_info_text.visibility = View.GONE
//                    text_goods_notice_info_not_exist.visibility = View.VISIBLE
//                }
//            }
//            EnumData.GoodsNoticeType.select.name->{
//                recycler_goods_notice_info.visibility = View.VISIBLE
//                text_goods_notice_info_text.visibility = View.GONE
//                recycler_goods_notice_info.layoutManager = LinearLayoutManager(this)
//                mAdapter = GoodsNoticeInfoAdapter(this)
//                recycler_goods_notice_info.adapter = mAdapter
//                getGoodsNoticeInfo()
//            }
//        }
//
//    }
//
//    private fun getGoodsNoticeInfo() {
//        val params = HashMap<String, String>()
//        params["pageSeqNo"] = mGoods!!.page!!.seqNo.toString()
//        params["goodsSeqNo"] = "0"
//        showProgress("")
//        ApiBuilder.create().getPageGoodsInfo(params).setCallback(object : PplusCallback<NewResultResponse<GoodsNoticeInfo>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsNoticeInfo>>?, response: NewResultResponse<GoodsNoticeInfo>?) {
//                hideProgress()
//                if (response != null) {
//                    if (response.data != null) {
//
//                        if(response.data.infoPropList != null && response.data.infoPropList!!.isNotEmpty()){
//                            text_goods_notice_info_not_exist.visibility = View.GONE
//                            mAdapter!!.setDataList(response.data.infoPropList!!.toMutableList())
//                        }else{
//                            text_goods_notice_info_not_exist.visibility = View.VISIBLE
//                        }
//
//                    }else{
//                        text_goods_notice_info_not_exist.visibility = View.VISIBLE
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsNoticeInfo>>?, t: Throwable?, response: NewResultResponse<GoodsNoticeInfo>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods_notice_info), ToolbarOption.ToolbarMenu.LEFT)
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
//}
