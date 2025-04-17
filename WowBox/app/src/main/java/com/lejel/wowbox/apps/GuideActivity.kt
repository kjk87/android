package com.lejel.wowbox.apps

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.login.LoginActivity2
import com.lejel.wowbox.apps.main.data.GuideAdapter
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityGuideBinding
import com.pplus.utils.part.pref.PreferenceUtil

class GuideActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityGuideBinding

    override fun getLayoutView(): View {
        binding = ActivityGuideBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        setEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN)

        enableEdgeToEdge()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        binding.textGuideLogin.setOnClickListener {
            PreferenceUtil.getDefaultPreference(this).put(Const.IS_GUIDE_FIRST, false)
            setEvent(FirebaseAnalytics.Event.LOGIN)
            val intent = Intent(this, LoginActivity2::class.java)
            loginLauncher.launch(intent)
        }

        binding.textGuideSkip.setOnClickListener {
            setEvent("guide_skip")
            PreferenceUtil.getDefaultPreference(this).put(Const.IS_GUIDE_FIRST, false)
            setResult(RESULT_OK)
            finish()
        }

        val guideDesc = arrayOf(R.string.html_guide_1, R.string.html_guide_2, R.string.html_guide_3, R.string.html_guide_4)

        val adapter = GuideAdapter()
        binding.pagerGuide.adapter = adapter
        binding.textGuide.text = PplusCommonUtil.fromHtml(getString(guideDesc[0]))
        binding.indicatorGuide.build(LinearLayout.HORIZONTAL, adapter.itemCount)
        binding.pagerGuide.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.textGuide.text = PplusCommonUtil.fromHtml(getString(guideDesc[position]))
                binding.indicatorGuide.setCurrentItem(position)

            }
        })

    }

    val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(RESULT_OK)
        finish()
    }
}