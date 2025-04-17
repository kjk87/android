package com.lejel.wowbox.apps.common.component

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.pplus.utils.part.info.DeviceUtil

class CustomBitmapImageViewTarget(view: ImageView?) : DrawableImageViewTarget(view) {
    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        super.onResourceReady(resource, transition)
        val rate = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS.toFloat() / resource.intrinsicWidth.toFloat()
        val changedHeight = resource.intrinsicHeight * rate
        view.layoutParams.height = changedHeight.toInt()
    }
}