package com.pplus.prnumberbiz.apps.ads.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.GoodsRegActivity2
import com.pplus.prnumberbiz.apps.push.ui.PushSendActivity
import com.pplus.prnumberbiz.apps.sms.SmsSendActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Group
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_advertise.*
import network.common.PplusCallback
import retrofit2.Call

class AdvertiseActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_advertise
    }

    override fun initializeView(savedInstanceState: Bundle?) {
//        layout_advertise_share.setOnClickListener {
//            ToastUtil.show(this, R.string.msg_preparing_service)
////            share()
//        }

        appbar_advertise.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (verticalOffset <= -collapsing_advertise.height + toolbar_advertise.height) {
                    //toolbar is collapsed here
                    //write your code here
                    image_advertise_back.setImageResource(R.drawable.ic_top_prev)
//                    text_biz_main_user_mode.visibility = View.GONE
                } else {
                    image_advertise_back.setImageResource(R.drawable.ic_top_prev_white)
//                    text_biz_main_user_mode.visibility = View.VISIBLE
                }
            }
        })

        image_advertise_back.setOnClickListener {
            onBackPressed()
        }

//        layout_advertise_sms.setOnClickListener {
//            val intent = Intent(this, SmsSendActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }

//        layout_advertise_reg_plus_goods.setOnClickListener {
//            val intent = Intent(this, GoodsRegActivity2::class.java)
//            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//            intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_REG)
//        }

        layout_advertise_send_event.setOnClickListener {
            val intent = Intent(this, SendEventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_advertise_send_push.setOnClickListener {
            val intent = Intent(this, PushSendActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
        }
        getPlusCount()
    }

    private fun getPlusCount() {
        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!

        ApiBuilder.create().getFanGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {

            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {
                for (group in response.datas) {
                    if (group.isDefaultGroup) {
                        text_advertise_plus_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_person_count, FormatUtil.getMoneyType(group.count.toString())))
                        break
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {

            }
        }).build().call()
    }

    private fun share() {

        val shareText = LoginInfoManager.getInstance().user.page!!.catchphrase + "\n" + getString(R.string.format_msg_page_url, "index.php?no=" + LoginInfoManager.getInstance().user.page!!.no!!)

        val intent = Intent(Intent.ACTION_SEND)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareText)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share_page))
        startActivity(chooserIntent)
    }
}
