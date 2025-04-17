package com.root37.buflexz.apps.my.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.App
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityVersionBinding
import retrofit2.Call

class VersionActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityVersionBinding

    override fun getLayoutView(): View {
        binding = ActivityVersionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.textVersionCrrent.text = PplusCommonUtil.getAppVersion(this)


        binding.textVersionUpdate.setText(R.string.msg_latest_version)
        binding.textVersionUpdate.setBackgroundResource(R.drawable.bg_595959_radius_30)
        binding.textVersionUpdate.isSelected = false
        binding.textVersionUpdate.isClickable = false

        val params = HashMap<String, String>()
        params["version"] = PplusCommonUtil.getAppVersion(this)
        params["platform"] = "aos"
        showProgress("")
        ApiBuilder.create().getApp(params).setCallback(object : PplusCallback<NewResultResponse<App>> {
            override fun onResponse(call: Call<NewResultResponse<App>>?, response: NewResultResponse<App>?) {
                hideProgress()
                if (response?.result != null) {
                    val app = response.result!!
                    binding.textVersionLatest.text = app.version

                    val isUpdate = PplusCommonUtil.isVersionUpdate(this@VersionActivity, app.version)
                    if (isUpdate != -1) {
                        binding.textVersionUpdate.setText(R.string.word_update)
                        binding.textVersionUpdate.setBackgroundResource(R.drawable.bg_48b778_radius_30)
                        binding.textVersionUpdate.isSelected = true
                        binding.textVersionUpdate.isClickable = true
                        binding.textVersionUpdate.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<App>>?, t: Throwable?, response: NewResultResponse<App>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_version_info), ToolbarOption.ToolbarMenu.LEFT)
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