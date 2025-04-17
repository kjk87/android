package com.pplus.prnumberuser.apps.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.main.ui.AppMainActivity
import com.pplus.prnumberuser.databinding.ActivityOrderPayCompleteBinding

class OrderPayCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityOrderPayCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityOrderPayCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val salesType = intent.getIntExtra(Const.TYPE, 1)

        when(salesType){
            1->{
                binding.textOrderPayCompleteTitle.text = getString(R.string.word_visit_order_complete)
                binding.textOrderPayCompleteDesc.text = getString(R.string.msg_visit_order_complete_desc)
                binding.imageOrderPayComplete.setImageResource(R.drawable.img_visit_pay_complete)
            }
            2->{
                binding.textOrderPayCompleteTitle.text = getString(R.string.word_delivery_order_complete)
                binding.textOrderPayCompleteDesc.text = getString(R.string.msg_delivery_order_complete_desc)
                binding.imageOrderPayComplete.setImageResource(R.drawable.img_delivery_pay_complete)
            }
            5->{
                binding.textOrderPayCompleteTitle.text = getString(R.string.word_package_order_complete)
                binding.textOrderPayCompleteDesc.text = getString(R.string.msg_package_order_complete_desc)
                binding.imageOrderPayComplete.setImageResource(R.drawable.img_package_pay_complete)
            }
        }


        binding.textOrderPayCompleteHistory.setOnClickListener {
            val intent = Intent(this, AppMainActivity::class.java)
            intent.putExtra(Const.KEY, Const.HISTORY)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textOrderPayCompleteMain.setOnClickListener {
            val intent = Intent(this, AppMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

    }


    override fun onBackPressed() {

    }
}
