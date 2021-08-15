package com.sonusourav.movies.utils

import android.content.Context
import android.util.TypedValue
import android.view.MenuItem
import androidx.annotation.ColorRes
import androidx.core.graphics.drawable.DrawableCompat

object UiUtils {
    // this method is used to tint menu icons for toolbars and other components
    @JvmStatic
    fun tintMenuIcon(context: Context, item: MenuItem, @ColorRes color: Int) {
        val itemIcon = item.icon
        val iconWrapper = DrawableCompat.wrap(itemIcon)
        DrawableCompat.setTint(iconWrapper, context.resources.getColor(color))
        item.icon = iconWrapper
    }

    // convert dip to float
    @JvmStatic
    fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }
}