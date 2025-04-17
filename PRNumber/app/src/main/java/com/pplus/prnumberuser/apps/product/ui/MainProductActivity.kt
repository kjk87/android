package com.pplus.prnumberuser.apps.product.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityMainProductBinding

class MainProductActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityMainProductBinding

    override fun getLayoutView(): View {
        binding = ActivityMainProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {


        binding.layoutMainProductStoreTab.setOnClickListener {
            setSelect(binding.layoutMainProductStoreTab, binding.layoutMainProductShipTab)
            setBold(binding.textMainProductStoreTab, binding.textMainProductShipTab)
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.store_product_container, MainProductStoreFragment.newInstance(), MainProductStoreFragment::class.java.simpleName)
            ft.commit()
        }

        binding.layoutMainProductShipTab.setOnClickListener {
            setSelect(binding.layoutMainProductShipTab, binding.layoutMainProductStoreTab)
            setBold(binding.textMainProductShipTab, binding.textMainProductStoreTab)
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.store_product_container, MainProductShipFragment.newInstance(), MainProductShipFragment::class.java.simpleName)
            ft.commit()
        }
    }

    private fun setSelect(view1: View, view2: View) {

        view1.isSelected = true
        view2.isSelected = false
    }

    private fun setBold(view1: TextView, view2: TextView) {

        view1.typeface = Typeface.DEFAULT_BOLD
        view2.typeface = Typeface.DEFAULT
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_8282_title), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
