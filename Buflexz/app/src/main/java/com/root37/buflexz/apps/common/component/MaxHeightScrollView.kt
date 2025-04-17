package com.root37.buflexz.apps.common.component

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.root37.buflexz.R
import com.pplus.utils.part.logs.LogUtil


class MaxHeightScrollView : ScrollView {
    var LOG_TAG = this.javaClass.simpleName
    var WITHOUT_MAX_HEIGHT_VALUE = -1

    var maxHeight = WITHOUT_MAX_HEIGHT_VALUE

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        val a = context!!.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MaxHeightScrollView,
                0, 0)

        try {
            maxHeight = a.getDimensionPixelSize(R.styleable.MaxHeightScrollView_maxHeight, 0)
        } finally {
            a.recycle()
        }
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightSize = 0
        try {
            heightSize = MeasureSpec.getSize(heightMeasureSpec)
            if (maxHeight != WITHOUT_MAX_HEIGHT_VALUE && heightSize > maxHeight) {
                heightSize = maxHeight
            }
            layoutParams.height = heightSize
        } catch (e: Exception) {
            LogUtil.e(LOG_TAG, "onMesure : Error forcing height "+e.toString())
        } finally {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST))
        }
    }

}