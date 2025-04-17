//package com.pplus.prnumberuser.apps.use.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.model.dto.Advertise
//import com.pplus.prnumberuser.core.network.model.dto.Coupon
//
//class UseActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_use
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        var advertise = intent.getParcelableExtra<Advertise>(Const.ADVERTISE)
//        var coupon = intent.getParcelableExtra<Coupon>(Const.COUPON)
//
//        val ft = supportFragmentManager.beginTransaction()
//        if(advertise != null){
//            ft.replace(R.id.use_container, UsePostFragment.newInstance(advertise), UsePostFragment::class.java!!.getSimpleName())
//        }else if(coupon != null){
//            ft.replace(R.id.use_container, UseCouponFragment.newInstance(coupon), UseCouponFragment::class.java!!.getSimpleName())
//        }
//
//        ft.commit()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//
//
//}
