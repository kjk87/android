//package com.pplus.prnumberuser.apps.event.ui
//
//import android.os.Bundle
//import android.view.View
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.ui.NumberEventFragment
//import com.pplus.prnumberuser.databinding.ActivityPlayBinding
//
//class NumberEventActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    private lateinit var binding: ActivityPlayBinding
//
//    override fun getLayoutView(): View {
//        binding = ActivityPlayBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.play_container, NumberEventFragment.newInstance(), NumberEventFragment::class.java.simpleName)
//        ft.commit()
//
//    }
//
//}
