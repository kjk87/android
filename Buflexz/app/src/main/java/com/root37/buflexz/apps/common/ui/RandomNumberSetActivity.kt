package com.root37.buflexz.apps.common.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.component.RandomPadView2
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.databinding.ActivityRandomNumberSetBinding
import java.util.Random

class RandomNumberSetActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityRandomNumberSetBinding

    override fun getLayoutView(): View {
        binding = ActivityRandomNumberSetBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPass = ""
    var numberSb = StringBuilder()

    override fun initializeView(savedInstanceState: Bundle?) {


        val random = Random()
        val value = random.nextInt(100)+1
        if(value < 10){
            mPass = "0${value}"
        }else{
            mPass = value.toString()
        }

        binding.textRandomNumberQuestion.text = getString(R.string.format_input_number, mPass)

        binding.textRandomNumberSetInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.length == 2){
                    check()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        numberSb = StringBuilder()
        binding.randomNumberSet.mOnKeyClickListener = object : RandomPadView2.OnKeyClickListener{
            override fun onClick(value: String, view: View) {
                if (value == "del") {
                    if (numberSb.isNotEmpty()) {
                        numberSb.setLength(numberSb.length - 1)
                        binding.textRandomNumberSetInput.text = numberSb.toString()
                    }
                }else{
                    numberSb.append(value)
                    binding.textRandomNumberSetInput.text = numberSb.toString()
                }

            }
        }


    }

    private fun check(){
        val inputPass = binding.textRandomNumberSetInput.text.toString()
        if(mPass == inputPass){
            setResult(Activity.RESULT_OK)
            finish()
        }else{
            binding.textRandomNumberSetInvalid.visibility = View.VISIBLE
            binding.textRandomNumberSetInput.text = ""
            numberSb = StringBuilder()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}
