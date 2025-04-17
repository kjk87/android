package com.lejel.wowbox.apps.common.component

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.lejel.wowbox.R
import com.lejel.wowbox.databinding.ItemLuckyDrawDialKeyBinding

/**
 * Created by 김종경 on 2016-08-08.
 */
class LuckyDrawPadView : LinearLayout {
    private var inputConnection: InputConnection? = null

    constructor(context: Context?) : super(context) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initialize()
    }

    fun setInputConnection(ic: InputConnection?) {
        inputConnection = ic
    }

    fun initialize() {
        removeAllViews()
        orientation = VERTICAL
//        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        setBackgroundResource(R.drawable.bg_f7f7f7_radius_33)
        val numberList = arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
//        numberList.shuffle()
        numberList.add(9, "*")
        numberList.add("del")
        var line = LinearLayout(context)
        line.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.height_227))
        addView(line)
        for (i in numberList.indices) {

            if (i != 0 && i % 3 == 0) {
                line = LinearLayout(context)
                line.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.height_227))
                addView(line)
            }

            val itemDialBinding = ItemLuckyDrawDialKeyBinding.inflate(LayoutInflater.from(context), RelativeLayout(context), false)
            val layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
            //            layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.width_119));
            itemDialBinding.root.layoutParams = layoutParams
            val textDial = itemDialBinding.textDialNumber
            val imageDel = itemDialBinding.imageDialDel
            itemDialBinding.root.tag = numberList[i]
            when(numberList[i]){
                "*"->{
                    textDial.text = ""
                }
                "del"->{
                    textDial.visibility = View.GONE
                    imageDel.visibility = View.VISIBLE
                }
                else->{
                    textDial.text = numberList[i]
                }
            }

            itemDialBinding.root.setOnClickListener { v ->
                val value = v.tag.toString()
                if (value == "del") {
                    val selectedText = inputConnection!!.getSelectedText(0)
                    val beforeText = inputConnection!!.getTextBeforeCursor(1, 0).toString()
                    if (TextUtils.isEmpty(selectedText)) {
                        try {
                            inputConnection!!.deleteSurroundingText(1, 0)
                        }catch (e:Exception){
                        }

                    } else {
                        inputConnection!!.commitText("", 1)
                    }
                } else {
                    inputConnection!!.commitText(value, 1)
                }
            }

            line.addView(itemDialBinding.root)
        }
    }
}