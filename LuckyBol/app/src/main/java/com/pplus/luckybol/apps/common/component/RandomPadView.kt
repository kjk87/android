package com.pplus.luckybol.apps.common.component

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.pplus.luckybol.R
import java.lang.Exception

/**
 * Created by 김종경 on 2016-08-08.
 */
class RandomPadView : LinearLayout {
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

    private val numberImage = intArrayOf(R.drawable.btn_secu_number_0, R.drawable.btn_secu_number_1,
            R.drawable.btn_secu_number_2,
            R.drawable.btn_secu_number_3,
            R.drawable.btn_secu_number_4,
            R.drawable.btn_secu_number_5,
            R.drawable.btn_secu_number_6,
            R.drawable.btn_secu_number_7,
            R.drawable.btn_secu_number_8,
            R.drawable.btn_secu_number_9,
            R.drawable.btn_secu_number_10,
            R.drawable.btn_secu_number_11)

    fun initialize() {
        removeAllViews()
        orientation = VERTICAL
        setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        val numberList = arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        numberList.shuffle()
        numberList.add(9, "*")
        numberList.add("del")
        var line = LinearLayout(context)
        line.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
        addView(line)
        var dialKeyView: View
        for (i in numberList.indices) {
            if (i != 0 && i % 3 == 0) {
                line = LinearLayout(context)
                line.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
                addView(line)
            }
            dialKeyView = LayoutInflater.from(context).inflate(R.layout.item_dial_key, RelativeLayout(context), false)
            val layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
            //            layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.width_119));
            dialKeyView.layoutParams = layoutParams
            val imageDial = dialKeyView.findViewById<View>(R.id.image_dial_number) as ImageView
            dialKeyView.tag = numberList[i]
            when(numberList[i]){
                "*"->{
                    imageDial.setImageResource(R.drawable.btn_secu_number_10)
                }
                "del"->{
                    imageDial.setImageResource(R.drawable.btn_secu_number_11)
                }
                else->{
                    imageDial.setImageResource(numberImage[numberList[i].toInt()])
                }
            }

            dialKeyView.setOnClickListener { v ->
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
            line.addView(dialKeyView)
        }
    }
}