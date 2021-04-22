package cai.lib.design

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import cai.lib.design.color.ColorStateListBuilder
import cai.lib.design.drawable.IconStateListDrawableBuilder
import cai.lib.design.drawable.ShapeStateListDrawableBuilder

object Design {
    private const val DEFAULT_DISABLED_ALPHA = 0.4f
    private const val DEFAULT_PRESSED_ALPHA = 0.4f

    private val DEFAULT_PRESSED_MASK_COLOR = Color.parseColor("#33000000")
    private val DEFAULT_DISABLED_COLOR = Color.parseColor("#ffe2e2e2")

    private lateinit var mConfig: Config

    val context: Context
        get() = mConfig.context
    val colorConfig: ColorConfig
        get() = mConfig.colorConfig
    val iconConfig: IconDrawableConfig
        get() = mConfig.iconDrawableConfig
    val shapeConfig: ShapeDrawableConfig
        get() = mConfig.shapeDrawableConfig

    @JvmStatic
    fun init(config: Config) {
        mConfig = config
    }

    @JvmStatic
    fun init(context: Context) {
        mConfig = Design.Config(context)
    }

    @JvmStatic
    fun colorRes(@ColorRes colorRes: Int) = color(context.resources.getColor(colorRes))

    @JvmStatic
    fun color(@ColorInt color: Int) = ColorStateListBuilder(color)

    @JvmStatic
    @JvmOverloads
    fun iconDrawable(
        @DrawableRes drawableRes: Int,
        @ColorRes colorRes: Int = 0
    ): IconStateListDrawableBuilder {
        return if (colorRes != 0) {
            IconStateListDrawableBuilder(
                context.resources.getDrawable(drawableRes),
                context.resources.getColor(colorRes)
            )
        } else {
            IconStateListDrawableBuilder(context.resources.getDrawable(drawableRes))
        }
    }

    @JvmStatic
    @JvmOverloads
    fun iconDrawable(drawable: Drawable, @ColorInt color: Int = 0) =
        IconStateListDrawableBuilder(drawable, color)

    @JvmStatic
    @JvmOverloads
    fun rectDrawableRes(@ColorRes solidColorRes: Int, radius: Float = 0.0f) =
        rectDrawable(context.resources.getColor(solidColorRes), radius)

    @JvmStatic
    @JvmOverloads
    fun rectDrawable(@ColorInt solidColor: Int, radius: Float = 0.0f) =
        ShapeStateListDrawableBuilder(GradientDrawable.RECTANGLE)
            .solidColor(solidColor)
            .radius(radius)

    @JvmStatic
    fun ovalDrawableRes(@ColorRes solidColorRes: Int) =
        ovalDrawable(context.resources.getColor(solidColorRes))

    @JvmStatic
    fun ovalDrawable(@ColorInt solidColor: Int) =
        ShapeStateListDrawableBuilder(GradientDrawable.OVAL)
            .solidColor(solidColor)

    class Config(val context: Context) {
        var colorConfig = ColorConfig()
        var iconDrawableConfig = IconDrawableConfig()
        var shapeDrawableConfig = ShapeDrawableConfig()
    }

    class ColorConfig(
        val pressedAlpha: Float = DEFAULT_PRESSED_ALPHA,
        val disabledAlpha: Float = DEFAULT_DISABLED_ALPHA
    )

    class IconDrawableConfig(
        val pressedAlpha: Float = DEFAULT_PRESSED_ALPHA,
        val disabledAlpha: Float = DEFAULT_DISABLED_ALPHA
    )

    class ShapeDrawableConfig(
        @ColorInt
        val pressedMaskColor: Int = DEFAULT_PRESSED_MASK_COLOR,
        @ColorInt
        val disabledColor: Int = DEFAULT_DISABLED_COLOR
    )
}