package cai.lib.design.color

import android.R.attr
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.StateSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.graphics.ColorUtils
import cai.lib.design.Design
import cai.lib.design.StateListBuilder
import java.lang.UnsupportedOperationException

class ColorStateListBuilder(@ColorInt color: Int) : StateListBuilder<ColorStateList>() {
    private val mNormalColor = color

    private var mPressedColor = deduceColor(color, Design.colorConfig.pressedAlpha, mPressed)
    private var mDisabledColor = deduceColor(color, Design.colorConfig.disabledAlpha, mDisabled)
    private var mSelectedColor = Color.TRANSPARENT
    private var mSelectedPressedColor = Color.TRANSPARENT

    fun pressed(enabled: Boolean) = apply {
        mPressed = enabled
        mPressedColor = deduceColor(mNormalColor, Design.colorConfig.pressedAlpha, enabled)
    }

    fun disabled(enabled: Boolean) = apply {
        mDisabled = enabled
        mDisabledColor = deduceColor(mNormalColor, Design.colorConfig.disabledAlpha, enabled)
    }

    fun disabledColorRes(@ColorRes colorRes: Int) =
        disabledColor(Design.context.resources.getColor(colorRes))

    fun disabledColor(@ColorInt color: Int) = apply {
        mDisabled = color != Color.TRANSPARENT
        mDisabledColor = color
    }

    fun selectedColorRes(@ColorRes colorRes: Int) =
        selectedColor(Design.context.resources.getColor(colorRes))

    fun selectedColor(@ColorInt color: Int) = apply {
        mSelectedColor = color
        mSelectedPressedColor = deduceColor(color, Design.colorConfig.pressedAlpha, mPressed)
    }

    override fun build(): ColorStateList {
        val states = arrayListOf<IntArray>()
        val colors = arrayListOf<Int>()

        if (mDisabled && mDisabledColor != Color.TRANSPARENT) {
            states += intArrayOf(-attr.state_enabled)
            colors += mDisabledColor
        }

        if (mPressed && mSelectedPressedColor != Color.TRANSPARENT) {
            states += intArrayOf(attr.state_enabled, attr.state_selected, attr.state_pressed)
            colors += mSelectedPressedColor
        }

        if (mSelectedColor != Color.TRANSPARENT) {
            states += intArrayOf(attr.state_enabled, attr.state_selected)
            colors += mSelectedColor
        }

        if (mPressed && mPressedColor != Color.TRANSPARENT) {
            states += intArrayOf(attr.state_enabled, attr.state_pressed)
            colors += mPressedColor
        }

        if (mNormalColor != Color.TRANSPARENT) {
            states += StateSet.WILD_CARD
            colors += mNormalColor
        }

        return ColorStateList(states.toTypedArray(), colors.toIntArray())
    }

    @ColorInt
    private fun deduceColor(color: Int, alphaRatio: Float, enable: Boolean): Int {
        return if (enable)
            ColorUtils.setAlphaComponent(color, (Color.alpha(color) * alphaRatio).toInt())
        else
            Color.TRANSPARENT
    }

    override fun statelessBuild(): ColorStateList {
        throw UnsupportedOperationException()
    }
}