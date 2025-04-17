package com.pplus.prnumberbiz.apps.main.ui


import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.code.common.EnumData
import kotlinx.android.synthetic.main.fragment_main_customer.*

class MainCustomerFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_main_customer
    }

    override fun initializeView(container: View?) {

    }

    override fun init() {

        text_main_customer_plus_tab.setOnClickListener {
            setPlusFragment()
        }

        text_main_customer_contact_tab.setOnClickListener {
            setContactFragment()
        }

        if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.person.name) {
            setContactFragment()
        } else {
            setPlusFragment()
        }
    }

    private fun setContactFragment() {
        selectedTextView(text_main_customer_contact_tab, text_main_customer_plus_tab)
        text_main_customer_contact_tab.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact_tab_address_sel, 0, 0, 0)
        text_main_customer_plus_tab.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact_tab_plus_nor, 0, 0, 0)

        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.main_customer_container, ContactConfigFragment.newInstance(), ContactConfigFragment::class.java.simpleName)
        ft.commitNow()
    }

    private fun setPlusFragment() {
        selectedTextView(text_main_customer_plus_tab, text_main_customer_contact_tab)
        text_main_customer_contact_tab.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact_tab_address_nor, 0, 0, 0)
        text_main_customer_plus_tab.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact_tab_plus_sel, 0, 0, 0)

        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.main_customer_container, PlusConfigFragment.newInstance(), PlusConfigFragment::class.java.simpleName)
        ft.commitNow()
    }

    private fun selectedTextView(textView1: TextView, textView2: TextView) {
        textView1.setTypeface(Typeface.SERIF, Typeface.BOLD)
        textView2.setTypeface(Typeface.SERIF, Typeface.NORMAL)
        textView1.isSelected = true
        textView2.isSelected = false
    }

    override fun getPID(): String {
        return ""
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                MainCustomerFragment().apply {
                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
