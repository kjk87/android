package com.pplus.prnumberbiz.apps.main.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import com.igaworks.adbrix.IgawAdbrix
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.coupon.ui.CouponConfigActivity
import com.pplus.prnumberbiz.apps.customer.ui.PlusActivity
import com.pplus.prnumberbiz.apps.goods.ui.GoodsReviewActivity
import com.pplus.prnumberbiz.apps.goods.ui.PlusGoodsActivity
import com.pplus.prnumberbiz.apps.menu.ui.MenuConfigActivity
import com.pplus.prnumberbiz.apps.nfc.ui.NFCPayActivity
import com.pplus.prnumberbiz.apps.number.ui.MakePrnumberPreActivity
import com.pplus.prnumberbiz.apps.pages.ui.AlertPageSetGuideActivity
import com.pplus.prnumberbiz.apps.pages.ui.PageEditActivity
import com.pplus.prnumberbiz.apps.push.ui.PushSendActivity
import com.pplus.prnumberbiz.apps.sale.ui.SaleHistoryActivity
import com.pplus.prnumberbiz.apps.sale.ui.SaleOrderProcessyActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Count
import com.pplus.prnumberbiz.core.network.model.dto.Group
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_biz_main.*
import kotlinx.android.synthetic.main.fragment_biz_main_fragment2.*
import kotlinx.android.synthetic.main.item_biz_main_footer.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class BizMainFragment2 : BaseFragment<BizMainActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_biz_main_fragment2
    }

    override fun initializeView(container: View?) {}

    override fun init() {

        layout_biz_main2_nfc.setOnClickListener {
            val intent = Intent(activity, NFCPayActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SALE_HISTORY)
        }

        image_biz_main2_menu.setOnClickListener {
            if (parentActivity is BizMainActivity) {
                IgawAdbrix.retention("Main_menu")
                parentActivity.drawer_layout.openDrawer(GravityCompat.START)
            }
        }

        layout_biz_main_footer_company_info_btn.setOnClickListener {
            if (layout_biz_main_footer_company_info.visibility == View.GONE) {
                text_biz_main_footer_company_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_up, 0)
                layout_biz_main_footer_company_info.visibility = View.VISIBLE
            } else if (layout_biz_main_footer_company_info.visibility == View.VISIBLE) {
                text_biz_main_footer_company_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_down, 0)
                layout_biz_main_footer_company_info.visibility = View.GONE
            }
        }

        layout_biz_main2_sale_history.setOnClickListener {
            val page = LoginInfoManager.getInstance().user.page!!
            if (page.type == EnumData.PageTypeCode.store.name) {
                val intent = Intent(activity, SaleOrderProcessyActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                activity?.startActivityForResult(intent, Const.REQ_SALE_HISTORY)
            } else {
                val intent = Intent(activity, SaleHistoryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                activity?.startActivityForResult(intent, Const.REQ_SALE_HISTORY)
            }
        }

        layout_biz_main2_plus.setOnClickListener {
            val intent = Intent(activity, PlusActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SALE_HISTORY)
        }

        layout_biz_main2_goods_config.setOnClickListener {
            val intent = Intent(activity, MenuConfigActivity::class.java)
            intent.putExtra(Const.KEY, Const.REG)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_biz_main2_review.setOnClickListener {
            val intent = Intent(activity, GoodsReviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_biz_main2_coupon.setOnClickListener {
            val intent = Intent(activity, CouponConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

//        layout_biz_main2_plus_goods.setOnClickListener {
//            val intent = Intent(activity, PlusGoodsActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

//        layout_biz_main2_hot_deal.setOnClickListener {
//            val intent = Intent(activity, HotDealActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_DETAIL)
//
//        }

        layout_biz_main2_push.setOnClickListener {
            val intent = Intent(activity, PushSendActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
//
//        layout_biz_main2_review.setOnClickListener {
//            val intent = Intent(activity, GoodsReviewActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        layout_biz_main2_store_config.setOnClickListener {
            val intent = Intent(activity, PageEditActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
        }

        text_biz_main2_page_name.text = LoginInfoManager.getInstance().user.page!!.name
//        layout_biz_main2_seller.visibility = View.GONE
//        layout_biz_main2_footer.visibility = View.GONE

        layout_biz_main2_nfc.visibility = View.GONE

        getPage(true)
    }

//    private fun checkPageSet(): Boolean {
//        val page = LoginInfoManager.getInstance().user.page!!
//        if (page.address != null && StringUtils.isEmpty(page.address!!.roadBase)) {
////            if (StringUtils.isEmpty(page.catchphrase)) {
//            val intent = Intent(activity, AlertPageSetGuideActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
//            return false
//        }
//        return true
//    }

    private fun getPage(isFirst: Boolean) {
        val params = HashMap<String, String>()
        params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page?>> {

            override fun onResponse(call: Call<NewResultResponse<Page?>>?, response: NewResultResponse<Page?>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response!!.data != null) {

                    LoginInfoManager.getInstance().user.page = response.data!!
                    LoginInfoManager.getInstance().save()

                    if (isFirst) {
//                        if (!LoginInfoManager.getInstance().user.page!!.usePrnumber!!) {
//                            val intent = Intent(activity, MakePrnumberPreActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            activity?.startActivityForResult(intent, Const.REQ_MAKE_PRNUMBER)
//                        } else {
//                            val page = LoginInfoManager.getInstance().user.page!!
//                            if (page.address == null || StringUtils.isEmpty(page.address!!.roadBase) || StringUtils.isEmpty(page.phone)) {
//                                val intent = Intent(activity, AlertPageSetGuideActivity::class.java)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
//                            }
//                        }

                        val page = LoginInfoManager.getInstance().user.page!!
                        if (page.address == null || StringUtils.isEmpty(page.address!!.roadBase) || StringUtils.isEmpty(page.phone)) {
                            val intent = Intent(activity, AlertPageSetGuideActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
                        }
                    }

                    parentActivity.setPageData()

                    if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
                        text_biz_main2_goods_config.setText(R.string.word_menu_config)
                    } else {
                        text_biz_main2_goods_config.setText(R.string.word_goods_config)
                    }

                    getPlusCount()
//                    if (LoginInfoManager.getInstance().user.page!!.isSeller!!) {
//                        layout_biz_main2_seller.visibility = View.VISIBLE
//                        layout_biz_main2_footer.visibility = View.VISIBLE
////                        getSaleCount()
//                    } else {
//                        layout_biz_main2_seller.visibility = View.GONE
//                        layout_biz_main2_footer.visibility = View.GONE
//                    }

                    if(LoginInfoManager.getInstance().user.page!!.ableNfc!!){
                        layout_biz_main2_nfc.visibility = View.VISIBLE
                    }else{
                        layout_biz_main2_nfc.visibility = View.GONE
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page?>>?, t: Throwable?, response: NewResultResponse<Page?>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getPlusCount() {
        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!

        ApiBuilder.create().getFanGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {

            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {

                if (response.datas.isEmpty()) {
                    text_biz_main2_plus_count.text = "0"
                } else {
                    for (group in response.datas) {
                        if (group.isDefaultGroup) {
//                        text_biz_main2_plus_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_person_count, FormatUtil.getMoneyType(group.count.toString())))
                            text_biz_main2_plus_count.text = FormatUtil.getMoneyType(group.count.toString())
                            break
                        }
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {

            }
        }).build().call()
    }

//    private fun getSaleCount() {
//        val params = HashMap<String, String>()
//
//        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
//        if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
//            params["type"] = "0"
//        } else {
//            params["type"] = "1"
//        }
//
//        ApiBuilder.create().getBuyCount(params).setCallback(object : PplusCallback<NewResultResponse<Count>> {
//            override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {
//                if (response != null) {
//                    val count = response.data.count
//                    text_biz_main2_sale_count?.text = FormatUtil.getMoneyType(count.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {
//            }
//        }).build().call()
//    }

//    private fun getHotdeal(){
//        val params = HashMap<String, String>()
//        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
//        params["isHotdeal"] = "true"
//        params["page"] = "0"
//        params["sort"] = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"
//        layout_biz_main2_hot_deal.isEnabled = false
//        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                if (response != null && response.data.content!!.isNotEmpty()) {
//                    mHotDealGoods = response.data.content!![0]
//                }else{
//                    mHotDealGoods = null
//                }
//                layout_biz_main2_hot_deal.isEnabled = true
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                layout_biz_main2_hot_deal.isEnabled = true
//            }
//        }).build().call()
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_SET_PAGE -> {
                getPage(false)
            }
            Const.REQ_MAKE_PRNUMBER -> {
                if (resultCode == RESULT_OK) {
                    getPage(false)
                }
            }
            Const.REQ_INTRODUCE_IMAGE -> {
                getPage(false)
            }
            Const.REQ_APPLY -> {
                getPage(false)
            }
            Const.REQ_SALE_HISTORY -> {
//                getSaleCount()
            }
        }
    }

    override fun getPID(): String {
        return ""
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                BizMainFragment2().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
