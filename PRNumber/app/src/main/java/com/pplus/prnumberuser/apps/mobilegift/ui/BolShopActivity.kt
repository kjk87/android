//package com.pplus.prnumberuser.apps.mobilegift.ui
//
//import android.os.Bundle
//import androidx.fragment.app.FragmentTransaction
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//
//class BolShopActivity : BaseActivity(){
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_bol_shop
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        setMobileGiftFragment()
//    }
//
//    private fun setMobileGiftFragment() {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
//        ft.replace(R.id.bol_shop_container, MobileGiftFragment.newInstance(), MobileGiftFragment::class.java.simpleName)
//        ft.commitNow()
//    }
//
////    override fun getToolbarOption(): ToolbarOption {
////
////        val toolbarOption = ToolbarOption(this)
////        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_shopping_by_bol), ToolbarOption.ToolbarMenu.LEFT)
////        val textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
////        textRightTop.setText(R.string.word_charge_station)
////        textRightTop.isClickable = true
////        textRightTop.gravity = Gravity.CENTER
////        textRightTop.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
////        textRightTop.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_579ffb))
////        textRightTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
////        textRightTop.setSingleLine()
////        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
////        return toolbarOption
////    }
////
////    override fun getOnToolbarClickListener(): OnToolbarListener {
////
////        return OnToolbarListener { v, toolbarMenu, tag ->
////            when (toolbarMenu) {
////                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
////                    onBackPressed()
////                }
////                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
////                    val pPlusPermission = PPlusPermission(this@BolShopActivity)
////                    pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE)
////                    pPlusPermission.setPermissionListener(object : PermissionListener {
////
////                        override fun onPermissionGranted() {
////
////                            IgawCommon.setUserId(this@BolShopActivity, LoginInfoManager.getInstance().user.no.toString())
////                            val optionMap = HashMap<String, Any>()
////                            optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_TITLE_TEXT, getString(R.string.word_charge_station))
////                            optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_THEME_COLOR, Color.parseColor("#579ffb"))
////                            optionMap.put(ApStyleManager.CustomStyle.TOP_BAR_BG_COLOR, Color.parseColor("#579ffb"))
////                            optionMap.put(ApStyleManager.CustomStyle.BOTTOM_BAR_BG_COLOR, Color.parseColor("#579ffb"))
////                            ApStyleManager.setCustomOfferwallStyle(optionMap)
////                            IgawAdpopcornExtension.setCashRewardAppFlag(this@BolShopActivity, true)
////                            IgawAdpopcorn.openOfferWall(this@BolShopActivity)
////                            IgawAdbrix.retention("Main_mypage_luckybol_a_charging")
////                            IgawAdbrix.firstTimeExperience("Main_mypage_luckybol_a_charging")
////                        }
////
////                        override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
////
////                        }
////                    })
////                    pPlusPermission.checkPermission()
////                }
////            }
////        }
////    }
//}
