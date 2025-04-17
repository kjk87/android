package com.root37.buflexz.apps.common.component

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.root37.buflexz.R


class MaxHeightRecyclerView : RecyclerView {
    var LOG_TAG = this.javaClass.simpleName
    var WITHOUT_MAX_HEIGHT_VALUE = -1

    var maxHeight = WITHOUT_MAX_HEIGHT_VALUE

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MaxHeightScrollView,
                0, 0)

        try {
            maxHeight = a.getDimensionPixelSize(R.styleable.MaxHeightScrollView_maxHeight, 0)
        } finally {
            a.recycle()
        }
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var spec = 0

        if (maxHeight > 0) {
            spec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        }else{
            spec = heightSpec
        }

        super.onMeasure(widthSpec, spec)
    }
}