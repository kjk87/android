//package com.pplus.luckybol.apps.event.ui
//
//import android.os.Bundle
//import android.view.View
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.databinding.ActivityLottoBinding
//
//class LottoActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    private lateinit var binding: ActivityLottoBinding
//
//    override fun getLayoutView(): View {
//        binding = ActivityLottoBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.lotto_container, LottoEventFragment.newInstance(), LottoEventFragment::class.java.simpleName)
//        ft.commit()
//
//    }
//
//}
