//package com.pplus.prnumberuser.apps.alert
//
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.ui.HotdealBuyActivity
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_alert_goods_select.*
//
//class AlertGoodsSelectActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_alert_goods_select
//    }
//
//    var mGoods:Goods? = null
//    var mCount = 1
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mGoods = intent.getParcelableExtra<Goods>(Const.DATA)
//        text_alert_goods_select_name.text = mGoods!!.name
//
//        image_alert_goods_select_close.setOnClickListener {
//            onBackPressed()
//        }
//
//        image_alert_goods_select_minus.setOnClickListener {
//            if (mCount > 1) {
//                mCount--
//                changeOption()
//            }
//        }
//
//        image_alert_goods_select_plus.setOnClickListener {
//
//            var maxCount = -1
//            if (mGoods!!.count != -1) {
//
//                var soldCount = 0
//                if (mGoods!!.soldCount != null) {
//                    soldCount = mGoods!!.soldCount!!
//                }
//
//                maxCount = mGoods!!.count!! - soldCount
//            }
//
//            if (maxCount != -1) {
//                if (mCount < maxCount) {
//                    mCount++
//                }
//            } else {
//                mCount++
//            }
//
//            changeOption()
//        }
//
//        text_alert_goods_select_cancel.setOnClickListener {
//            onBackPressed()
//        }
//
//        text_alert_goods_select_confirm.setOnClickListener {
//            val intent = Intent(this, HotdealBuyActivity::class.java)
//            intent.putExtra(Const.KEY, Const.GOODS)
//            intent.putExtra(Const.GOODS, mGoods)
//            intent.putExtra(Const.COUNT, mCount)
//            startActivityForResult(intent, Const.REQ_ORDER)
//            finish()
//        }
//
//        mCount = 1
//        changeOption()
//    }
//
//    var mTotalPrice = 0L
//
//    private fun changeOption() {
//        text_alert_goods_select_count.text = mCount.toString()
//        mTotalPrice = mGoods!!.price!! * mCount
//        text_alert_goods_select_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(mTotalPrice.toString())))
//    }
//}
