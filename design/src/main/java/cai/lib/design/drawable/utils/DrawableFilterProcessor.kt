package cai.lib.design.drawable.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils
import cai.lib.design.Design

object DrawableFilterProcessor {
    private const val MAX_ALPHA = 0xFF

    @JvmStatic
    fun process(
        drawable: Drawable, @ColorInt filterColor: Int,
        @FloatRange(from = 0.0, to = 1.0) alphaRatio: Float
    ): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createColorFilterDrawableApi21(drawable, filterColor, alphaRatio)
        } else {
            createColorFilterDrawableBelowApi21(drawable, filterColor, alphaRatio)
        }
    }

    private fun createColorFilterDrawableBelowApi21(
        drawable: Drawable,
        @ColorInt filterColor: Int, @FloatRange(from = 0.0, to = 1.0) alphaRatio: Float
    ): Drawable {
        if (filterColor == Color.TRANSPARENT && alphaRatio == 1.0f) {
            return drawable
        }

        val filterDrawable = drawable.constantState!!.newDrawable().mutate().apply {
            if (filterColor != Color.TRANSPARENT) {
                colorFilter =
                    PorterDuffColorFilter(
                        ColorUtils.setAlphaComponent(filterColor, MAX_ALPHA),
                        SRC_ATOP
                    )
                alpha = (Color.alpha(filterColor) * alphaRatio).toInt()
            } else {
                alpha = (MAX_ALPHA * alphaRatio).toInt()
            }
        }

        val bitmap = createBitmap(filterDrawable)
        val canvas = Canvas(bitmap)
        filterDrawable.setBounds(
            0,
            0,
            filterDrawable.intrinsicWidth,
            filterDrawable.intrinsicHeight
        )
        filterDrawable.draw(canvas)

        return BitmapDrawable(Design.context.resources, bitmap)
    }

    private fun createColorFilterDrawableApi21(
        drawable: Drawable,
        @ColorInt filterColor: Int, @FloatRange(from = 0.0, to = 1.0) alphaRatio: Float
    ): Drawable {
        if (filterColor == Color.TRANSPARENT && alphaRatio == 1.0f) {
            return drawable
        }

        return drawable.constantState!!.newDrawable().mutate().apply {
            if (filterColor != Color.TRANSPARENT) {
                colorFilter =
                    PorterDuffColorFilter(
                        ColorUtils.setAlphaComponent(filterColor, MAX_ALPHA),
                        SRC_ATOP
                    )
                alpha = (Color.alpha(filterColor) * alphaRatio).toInt()
            } else {
                alpha = (MAX_ALPHA * alphaRatio).toInt()
            }
        }
    }

    private fun createBitmap(drawable: Drawable): Bitmap {
        return if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, ARGB_8888)
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, ARGB_8888)
        }
    }
}