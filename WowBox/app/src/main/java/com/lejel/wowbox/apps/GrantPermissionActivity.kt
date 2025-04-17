package com.lejel.wowbox.apps

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityGrantPermissionBinding
import com.pplus.utils.part.pref.PreferenceUtil

class GrantPermissionActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityGrantPermissionBinding

    override fun getLayoutView(): View {
        binding = ActivityGrantPermissionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        binding.textGrantPermission.setOnClickListener {
            PreferenceUtil.getDefaultPreference(this).put(Const.IS_FIRST_PERMISSION, false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            }else{
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        var denied = false
        results.forEach {
            if (!it.value) {
                denied = true
            }
        }

        if(denied){
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_notification_permission_denied), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
            builder.setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                    setResult(Activity.RESULT_OK)
                    finish()
                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    setResult(Activity.RESULT_OK)
                    finish()
                }
            })
            builder.builder().show(this)
        }else{
            setResult(Activity.RESULT_OK)
            finish()
        }

    }
}