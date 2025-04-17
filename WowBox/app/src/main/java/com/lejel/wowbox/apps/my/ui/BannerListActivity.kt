package com.lejel.wowbox.apps.my.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.custom.BottomItemOffsetDecoration
import com.lejel.wowbox.apps.community.ui.CommunityApplyActivity
import com.lejel.wowbox.apps.faq.ui.FaqActivity
import com.lejel.wowbox.apps.main.ui.MainActivity
import com.lejel.wowbox.apps.my.data.BannerAdapter
import com.lejel.wowbox.apps.notice.ui.NoticeActivity
import com.lejel.wowbox.apps.partner.ui.PartnerReqActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Banner
import com.lejel.wowbox.core.network.model.dto.Partner
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityBannerListBinding
import retrofit2.Call
import java.util.Locale

class BannerListActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBannerListBinding

    override fun getLayoutView(): View {
        binding = ActivityBannerListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mBannerAdapter: BannerAdapter

    var mPartner: Partner? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mPartner = PplusCommonUtil.getParcelableExtra(intent, Const.PARTNER, Partner::class.java)
        mBannerAdapter = BannerAdapter()
        binding.recyclerBannerList.layoutManager = LinearLayoutManager(this)
        binding.recyclerBannerList.adapter = mBannerAdapter
        binding.recyclerBannerList.addItemDecoration(BottomItemOffsetDecoration(this, R.dimen.height_44))

        mBannerAdapter.listener = object : BannerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val item = mBannerAdapter.getItem(position)
                when (item.moveType) {
                    "inner" -> {
                        when (item.innerType) {
                            "partner" -> {

                                if (!PplusCommonUtil.loginCheck(this@BannerListActivity, null)) {
                                    return
                                }

                                if (mPartner == null || mPartner!!.status != "active") {
                                    val intent = Intent(this@BannerListActivity, PartnerReqActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(intent)
                                }else{
                                    showAlert(R.string.msg_already_active_partner)
                                }
                            }
                            "telegram"->{
                                if (!PplusCommonUtil.loginCheck(this@BannerListActivity, null)) {
                                    return
                                }

                                val intent = Intent(this@BannerListActivity, CommunityApplyActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                            "main" -> {
                                val intent = Intent(this@BannerListActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }

                            "notice" -> {
                                val intent = Intent(this@BannerListActivity, NoticeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }

                            "faq" -> {
                                val intent = Intent(this@BannerListActivity, FaqActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                        }

                    }

                    "outer" -> {
                        PplusCommonUtil.openChromeWebView(this@BannerListActivity, item.outerUrl!!)
                    }
                }
            }
        }

        getBannerList()
    }

    private fun getBannerList() {
        val params = HashMap<String, String>()
        params["aos"] = "1"
        params["type"] = "home"
        params["nation"] = Locale.getDefault().country
        ApiBuilder.create().getBannerList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Banner>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, response: NewResultResponse<ListResultResponse<Banner>>?) {

                if (response?.result != null && response.result!!.list != null) {
                    val bannerList = response.result!!.list!!
                    mBannerAdapter.setDataList(bannerList as MutableList<Banner>)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Banner>>?) {

            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_total_view_banner), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}