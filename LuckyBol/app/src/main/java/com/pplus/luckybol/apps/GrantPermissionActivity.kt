package com.pplus.luckybol.apps

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.pplus.luckybol.apps.common.builder.PPlusPermission
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityGrantPermissionBinding
import com.pplus.utils.part.apps.permission.Permission
import com.pplus.utils.part.apps.permission.PermissionListener
import java.util.*

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
        binding.textGrantPermission.setOnClickListener {
            val pPlusPermission = PPlusPermission(this@GrantPermissionActivity)
            pPlusPermission.addPermission(Permission.PERMISSION_KEY.LOCATION)
            pPlusPermission.addPermission(Permission.PERMISSION_KEY.CONTACTS)
            pPlusPermission.setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    setResult(Activity.RESULT_OK)
                    finish()
                }

                override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {}
            })
            pPlusPermission.checkPermission()
        }
    }
}