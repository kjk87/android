package com.root37.buflexz.apps.common.component

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.root37.buflexz.R
import com.root37.buflexz.databinding.ItemDialKeyBinding

/**
 * Created by 김종경 on 2016-08-08.
 */
class RandomPadView2 : LinearLayout {

    interface OnKeyClickListener {
        fun onClick(value: String, view: View)
    }

    var mOnKeyClickListener: OnKeyClickListener? = null

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

    fun initialize() {
        removeAllViews()
        orientation = VERTICAL
        setBackgroundColor(ContextCompat.getColor(context, R.color.color_48b778))
        val numberList = arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        numberList.shuffle()
        numberList.add(9, "*")
        numberList.add("del")
        var line = LinearLayout(context)
        line.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.height_141))
        addView(line)
        for (i in numberList.indices) {
            if (i != 0 && i % 3 == 0) {
                line = LinearLayout(context)
                line.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.height_141))
                addView(line)
            }
            val itemDialBinding = ItemDialKeyBinding.inflate(LayoutInflater.from(context), RelativeLayout(context), false)
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
                mOnKeyClickListener?.onClick(value, v)
            }
            line.addView(itemDialBinding.root)
        }
    }
}