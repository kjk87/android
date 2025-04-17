package com.root37.buflexz.apps.my.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.common.ui.custom.BottomItemOffsetDecoration
import com.root37.buflexz.apps.community.ui.CommunityApplyActivity
import com.root37.buflexz.apps.faq.ui.FaqActivity
import com.root37.buflexz.apps.main.ui.MainActivity
import com.root37.buflexz.apps.my.data.BannerAdapter
import com.root37.buflexz.apps.notice.ui.NoticeActivity
import com.root37.buflexz.apps.partner.ui.PartnerReqActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Banner
import com.root37.buflexz.core.network.model.dto.Partner
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityBannerListBinding
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