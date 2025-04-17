package com.pplus.prnumberuser.apps.recommend.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.CountryConfigManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityRecommendBinding
import com.pplus.utils.part.format.FormatUtil

class RecommendActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_number_invite"
    }

    private lateinit var binding: ActivityRecommendBinding

    override fun getLayoutView(): View {
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val configProperties = CountryConfigManager.getInstance().config.properties!!

        val joinBol = FormatUtil.getMoneyType(configProperties.joinBol.toString())
        val recommendKey = LoginInfoManager.getInstance().user.recommendKey

        binding.textRecommendKey.text = recommendKey
//        text_recommend_desc1.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_recommend_description1))
//        text_recommend_desc2.text = getString(R.string.format_msg_join_bol, joinBol)

        binding.textRecommendInvite.setOnClickListener {

            share()
        }

//        image_recommend_ranking.setOnClickListener {
//            val intent = Intent(this, RankingActivity::class.java)
//            intent.putExtra(Const.TYPE, EnumData.RankType.recommend.name)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        binding.imageRecommendCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText(getString(R.string.word_recommend_code), recommendKey)
            clipboard.setPrimaryClip(clip)
            ToastUtil.show(this, R.string.msg_copied_clipboard)
        }
//
//        text_recommend_history.setOnClickListener {
//            val intent = Intent(this@RecommendActivity, RecommendHistoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
    }

    private fun share() {
        val recommendPoint = FormatUtil.getMoneyType(CountryConfigManager.getInstance().config.properties!!.recommendBol.toString())
        val recommendKey = LoginInfoManager.getInstance().user.recommendKey
        val text = "${getString(R.string.format_invite_description, recommendKey)}\n${getString(R.string.format_msg_invite_url, LoginInfoManager.getInstance().user.recommendKey)}"
//        val text = "${getString(R.string.msg_invite_desc)}\n${getString(R.string.msg_invite_url)}"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
        startActivity(chooserIntent)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_invite), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_recommend_history))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        val intent = Intent(this@RecommendActivity, RecommendHistoryActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                }
            }
        }
    }
}
