package com.pplus.luckybol.apps.setting.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.AppInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.setting.ui.ServiceVersionActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.AppVersion
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityServiceVersionBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

/**
 * 버전 정보
 */
class ServiceVersionActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityServiceVersionBinding

    override fun getLayoutView(): View {
        binding = ActivityServiceVersionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.textVersionCurrentVersion.text = AppInfoManager.getInstance().appVersion
        val params: MutableMap<String, String> = HashMap()
        params["appKey"] = packageName
        params["version"] = AppInfoManager.getInstance().appVersion
        ApiBuilder.create().appVersion(params).setCallback(object : PplusCallback<NewResultResponse<AppVersion>> {
            override fun onResponse(call: Call<NewResultResponse<AppVersion>>?, response: NewResultResponse<AppVersion>?) {
                if(response?.data != null){
                    val data = response.data
                    if (data != null && StringUtils.isNotEmpty(data.version)) {
                        val newVersion = data.version
                        binding.textVersionLatestVersion.text = newVersion
                        val isUpdate = AppInfoManager.getInstance().isVersionUpdate(newVersion)

                        // 여기서는 메이저업데이트와는 상관없이 처리
                        if (isUpdate != -1) {
                            binding.textVersionUpdate.setText(R.string.word_update)
                            binding.textVersionUpdate.setBackgroundColor(ResourceUtil.getColor(this@ServiceVersionActivity, R.color.color_8700ff))
                            binding.textVersionUpdate.isSelected = true
                            binding.textVersionUpdate.isClickable = true
                            binding.textVersionUpdate.setOnClickListener {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                                startActivity(intent)
                            }
                        }
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<AppVersion>>?, t: Throwable?, response: NewResultResponse<AppVersion>?) {

            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_version_info), ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        private val TAG = ServiceVersionActivity::class.java.simpleName
    }
}