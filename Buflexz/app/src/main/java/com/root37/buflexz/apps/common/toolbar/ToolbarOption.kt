package com.root37.buflexz.apps.common.toolbar

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.pplus.utils.part.resource.ResourceUtil
import com.root37.buflexz.R

/**
 * Created by j2n on 2016. 8. 16..
 *
 *
 * <pre>
 * 1. toolbar menu tag 기능 추가
</pre> *
 * <pre>
 * @TODO int 타입으로 기능을 추가 하였으나 개발자의 실수로인한 int타입이 중복으로 등록될 가능성이 있음.. 그럼으로 태그를 별도의 타입으로 변경기능을
 * 생각해봐야함.
</pre> *
 */
class ToolbarOption(private val context: Context) {

    private val toolbarMenuArrayListMap: HashMap<ToolbarMenu, ArrayList<View>>
    private val defaultMargin: Float
    private var actionBarHeight = 0

    /**
     * 타이틀 위치 변경 (left , center , right)
     */
    var titleGravity = ToolbarGravity.CENTER
    var title = ""
    var isScrollFlags = false
    private var imageButton: ImageView? = null

    enum class ToolbarGravity {
        LEFT, CENTER, RIGHT
    }

    enum class ToolbarMenu {
        LEFT, RIGHT
    }

    fun setToolbarMenu(toolbarMenu: ToolbarMenu, view: View, tag: Int) {
        var tag = tag
        if (!toolbarMenuArrayListMap.containsKey(toolbarMenu)) {
            toolbarMenuArrayListMap[toolbarMenu] = ArrayList()
        }
        val viewArrayList = toolbarMenuArrayListMap[toolbarMenu]!!
        view.setPadding(0, 0, context.resources.getDimensionPixelSize(R.dimen.width_44), 0)
        viewArrayList.add(view)
        if (tag == 0) {
            tag = viewArrayList.size
        }
        view.tag = tag
    }

    fun setToolbarMenu(toolbarMenu: ToolbarMenu, @DrawableRes res: Int, tag: Int) {
        imageButton = ImageView(context)
        imageButton!!.foregroundGravity = Gravity.CENTER
        imageButton!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageButton!!.setImageResource(res)
        if (toolbarMenu == ToolbarMenu.RIGHT && res != R.drawable.ic_top_close) {
            imageButton!!.setPadding(0, 0, context.resources.getDimensionPixelSize(R.dimen.width_44), 0)
        } else if (toolbarMenu == ToolbarMenu.LEFT) {
            imageButton!!.setPadding(context.resources.getDimensionPixelSize(R.dimen.width_44), 0, 0, 0)
        }
        imageButton!!.minimumWidth = actionBarHeight
        setToolbarMenu(toolbarMenu, imageButton!!, tag)
    }

    fun setToolbarMenu(toolbarMenu: ToolbarMenu, @DrawableRes res: Int) {
        setToolbarMenu(toolbarMenu, res, 0)
    }

    fun setToolbarMenu(toolbarMenu: ToolbarMenu, value: String?, tag: Int) {
        val textView = TextView(context)
        textView.text = value
        textView.isClickable = true
        textView.gravity = Gravity.CENTER
        textView.setPadding(0, 0, context.resources.getDimensionPixelSize(R.dimen.width_44), 0)
        textView.setTextColor(ResourceUtil.getColorStateList(context, R.color.white))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimensionPixelSize(R.dimen.textSize_38pt).toFloat())
        textView.setSingleLine()
        textView.minimumWidth = actionBarHeight
        setToolbarMenu(toolbarMenu, textView, tag)
    }

    fun setToolbarMenu(toolbarMenu: ToolbarMenu, value: String?) {
        setToolbarMenu(toolbarMenu, value, 0)
    }

    fun getToolbarMenuArrayList(toolbarMenu: ToolbarMenu): ArrayList<View>? {
        return toolbarMenuArrayListMap[toolbarMenu]
    }

    /**
     * 높이 = 액션바의 높이만큼 (48dp)
     * 넓이 = 액션바의 높이 - 액션바의 높이 3/1
     */
    fun getViewParams(): RelativeLayout.LayoutParams {
        val tv = TypedValue()
        if (actionBarHeight == 0 && context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }
        /**
         * 높이 = 액션바의 높이만큼 (48dp)
         * 넓이 = 액션바의 높이 - 액션바의 높이 3/1
         */
        val viewParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, actionBarHeight)
        viewParams.addRule(RelativeLayout.CENTER_VERTICAL)
        return viewParams
    }

    fun initializeDefaultToolbar(title: String, toolbarMenu: ToolbarMenu?) {
        this.title = title
        if (toolbarMenu == null) {
            titleGravity = ToolbarGravity.CENTER
        } else {
            when (toolbarMenu) {
                ToolbarMenu.LEFT -> {
                    titleGravity = ToolbarGravity.CENTER
                    setToolbarMenu(toolbarMenu, R.drawable.ic_top_prev)
                }

                ToolbarMenu.RIGHT -> {
                    titleGravity = ToolbarGravity.CENTER
                    setToolbarMenu(toolbarMenu, R.drawable.ic_top_close)
                }
            }
        }
    }

    fun setRightImageRes(@DrawableRes res: Int) {
        if (res != -1 && imageButton != null) {
            imageButton!!.setImageResource(res)
        } else {
            imageButton!!.setImageDrawable(null)
        }
    }

    init {
        toolbarMenuArrayListMap = HashMap()
        defaultMargin = ResourceUtil.getDpToPixel(context, 2)
    }
}