package cai.lib.design.drawable

import android.R.attr
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import cai.lib.design.Design
import cai.lib.design.StateListBuilder
import cai.lib.design.drawable.utils.DrawableFilterProcessor

class IconStateListDrawableBuilder(
    drawable: Drawable,
    normalColor: Int = Color.TRANSPARENT
) : StateListBuilder<Drawable>() {
    private var mNormalColor = normalColor
    private var mOriginDrawable = drawable

    private var mDisabledColor = Color.TRANSPARENT
    private var mDisabledDrawable: Drawable? = null

    private var mSelected = false
    private var mSelectedColor = Color.TRANSPARENT
    private var mSelectedDrawable: Drawable? = null

    fun pressed(enabled: Boolean) = apply {
        mPressed = enabled
    }

    fun disabled(enabled: Boolean) = apply {
        mDisabled = enabled
    }

    fun disabledColorRes(@ColorRes colorRes: Int) =
        disabledColor(Design.context.resources.getColor(colorRes))

    fun disabledColor(@ColorInt color: Int) = apply {
        mDisabled = color != Color.TRANSPARENT
        mDisabledColor = color
    }

    fun disabledDrawable(@DrawableRes drawableRes: Int) =
        disabledDrawable(Design.context.resources.getDrawable(drawableRes))

    fun disabledDrawable(drawable: Drawable) = apply {
        mDisabled = true
        mDisabledDrawable = drawable
    }

    fun selectedColorRes(@ColorRes colorRes: Int) =
        selectedColor(Design.context.resources.getColor(colorRes))

    fun selectedColor(@ColorInt color: Int) = apply {
        mSelected = color != Color.TRANSPARENT
        mSelectedColor = color
    }

    fun selectedDrawable(@DrawableRes drawableRes: Int) =
        selectedDrawable(Design.context.resources.getDrawable(drawableRes))

    fun selectedDrawable(drawable: Drawable) = apply {
        mSelected = true
        mSelectedDrawable = drawable
    }

    override fun build(): Drawable {
        val disabledAlpha = Design.iconConfig.disabledAlpha
        val pressedAlpha = Design.iconConfig.pressedAlpha

        return StateListDrawable().apply {
            if (mDisabled) {
                val disabledDrawable = when {
                    mDisabledDrawable != null -> {
                        DrawableFilterProcessor.process(mDisabledDrawable!!, mDisabledColor, 1.0f)
                    }
                    mDisabledColor != Color.TRANSPARENT -> {
                        DrawableFilterProcessor.process(mOriginDrawable, mDisabledColor, 1.0f)
                    }
                    else -> {
                        DrawableFilterProcessor.process(mOriginDrawable, mNormalColor, disabledAlpha)
                    }
                }

                addState(intArrayOf(-attr.state_enabled), disabledDrawable)
            }

            if (mSelected && mPressed) {
                val selectedPressedDrawable = if (mSelectedDrawable != null) {
                    DrawableFilterProcessor.process(mSelectedDrawable!!, mSelectedColor, pressedAlpha)
                } else {
                    DrawableFilterProcessor.process(mOriginDrawable, mSelectedColor, pressedAlpha)
                }

                addState(intArrayOf(attr.state_enabled, attr.state_selected, attr.state_pressed),
                    selectedPressedDrawable);
            }

            if (mSelected) {
                val selectedDrawable = if (mSelectedDrawable != null) {
                    DrawableFilterProcessor.process(mSelectedDrawable!!, mSelectedColor, 1.0f)
                } else {
                    DrawableFilterProcessor.process(mOriginDrawable, mSelectedColor, 1.0f)
                }

                addState(intArrayOf(attr.state_enabled, attr.state_selected), selectedDrawable)
            }

            if (mPressed) {
                addState(intArrayOf(attr.state_enabled, attr.state_pressed),
                    DrawableFilterProcessor.process(mOriginDrawable, mNormalColor, pressedAlpha))
            }

            addState(StateSet.WILD_CARD,
                DrawableFilterProcessor.process(mOriginDrawable, mNormalColor, 1.0f))
        }
    }

    override fun statelessBuild(): Drawable {
        return DrawableFilterProcessor.process(mOriginDrawable, mNormalColor, 1.0f)
    }
}