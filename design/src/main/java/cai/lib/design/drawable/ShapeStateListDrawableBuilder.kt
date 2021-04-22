package cai.lib.design.drawable

import android.R.attr
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import cai.lib.design.Design
import cai.lib.design.StateListBuilder

class ShapeStateListDrawableBuilder(shape: Int) : StateListBuilder<Drawable>() {
    private val mShape: Int = shape

    private var mSolidColor = Color.TRANSPARENT
    private var mStrokeColor = Color.TRANSPARENT
    private var mStrokeWidth = 0
    private var mDashWidth = 0.0f
    private var mDashGap = 0.0f

    private var mSelected = false
    private var mSelectedColor = Color.TRANSPARENT
    private var mSelectedStrokeColor = Color.TRANSPARENT
    private var mSelectedStrokeWidth = 0
    private var mSelectedDashWidth = 0.0f
    private var mSelectedDashGap = 0.0f

    private var mCornerRadii = floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)

    private var mGradientType = GradientDrawable.LINEAR_GRADIENT
    private var mGradientOrientation = LEFT_RIGHT
    private var mGradientColors: IntArray? = null

    private var mWidth = -1
    private var mHeight = -1

    fun pressed(enabled: Boolean) = apply {
        mPressed = enabled
    }

    fun disabled(enabled: Boolean) = apply {
        mDisabled = enabled
    }

    fun solidColorRes(@ColorRes solidColor: Int) =
        solidColor(Design.context.resources.getColor(solidColor))

    fun solidColor(@ColorInt solidColor: Int) = apply {
        mSolidColor = solidColor
    }

    fun selectedColorRes(@ColorRes colorRes: Int) =
        selectedColor(Design.context.resources.getColor(colorRes))

    fun selectedColor(@ColorInt color: Int) = apply {
        mSelected = mSelected || color != Color.TRANSPARENT
        mSelectedColor = color
    }

    @JvmOverloads
    fun strokeRes(
        @ColorRes strokeColorRes: Int,
        strokeWidth: Int,
        dashWidth: Float = 0.0f,
        dashGap: Float = 0.0f
    ) = stroke(Design.context.resources.getColor(strokeColorRes), strokeWidth, dashWidth, dashGap)

    @JvmOverloads
    fun stroke(
        @ColorInt strokeColor: Int,
        strokeWidth: Int, dashWidth:
        Float = 0.0f,
        dashGap: Float = 0.0f
    ) = apply {
        mStrokeColor = strokeColor
        mStrokeWidth = strokeWidth
        mDashWidth = dashWidth
        mDashGap = dashGap
    }

    @JvmOverloads
    fun selectedStrokeRes(
        @ColorRes strokeColorRes: Int,
        strokeWidth: Int,
        dashWidth: Float = 0.0f,
        dashGap: Float = 0.0f
    ) = selectedStroke(Design.context.resources.getColor(strokeColorRes), strokeWidth, dashWidth,
        dashGap)

    @JvmOverloads
    fun selectedStroke(
        @ColorInt strokeColor: Int,
        strokeWidth: Int,
        dashWidth: Float = 0.0f,
        dashGap: Float = 0.0f
    ) = apply {
        mSelected = mSelected || strokeColor != Color.TRANSPARENT

        mSelectedStrokeColor = strokeColor
        mSelectedStrokeWidth = strokeWidth
        mSelectedDashWidth = dashWidth
        mSelectedDashGap = dashGap
    }

    @JvmOverloads
    fun gradientRes(
        @ColorRes colors: IntArray,
        orientation: Orientation = LEFT_RIGHT,
        type: Int = GradientDrawable.LINEAR_GRADIENT
    ) =
        gradient(colors.map { Design.context.resources.getColor(it) }.toIntArray(), orientation, type)

    @JvmOverloads
    fun gradient(
        @ColorInt colors: IntArray,
        orientation: Orientation = LEFT_RIGHT,
        type: Int = GradientDrawable.LINEAR_GRADIENT
    ) = apply {
        mGradientColors = colors
        mGradientOrientation = orientation
        mGradientType = type
    }

    fun radius(radius: Float) = apply {
        mCornerRadii = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
    }

    fun radius(
        leftTopRadius: Float,
        rightTopRadius: Float,
        leftBottomRadius: Float,
        rightBottomRadius: Float
    ) = apply {
        mCornerRadii = floatArrayOf(leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius,
            rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius)
    }

    fun size(width: Int, height: Int) = apply {
        mWidth = width
        mHeight = height
    }

    override fun build(): Drawable {
        val disabledColor = Design.shapeConfig.disabledColor
        val pressedMaskColor = Design.shapeConfig.pressedMaskColor

        return StateListDrawable().apply {
            if (mDisabled) {
                addState(intArrayOf(-attr.state_enabled), createFillColorDrawable(disabledColor))
            }

            if (mSelected && mPressed) {
                val selectedPressedDrawable = LayerDrawable(
                    arrayOf(
                        createFillAndStrokeColorDrawable(mSelectedColor, mSelectedStrokeColor,
                            mSelectedStrokeWidth, mSelectedDashWidth, mSelectedDashGap),
                        createFillAndStrokeColorDrawable(
                            if (mSelectedColor == Color.TRANSPARENT) Color.TRANSPARENT else pressedMaskColor,
                            if (mSelectedStrokeColor == Color.TRANSPARENT) Color.TRANSPARENT else pressedMaskColor,
                            mSelectedStrokeWidth, mSelectedDashWidth, mSelectedDashGap)
                    )
                )

                addState(intArrayOf(attr.state_enabled, attr.state_selected, attr.state_pressed),
                    selectedPressedDrawable)
            }

            if (mSelected) {
                addState(intArrayOf(attr.state_enabled, attr.state_selected),
                    createFillAndStrokeColorDrawable(mSelectedColor, mSelectedStrokeColor,
                        mSelectedStrokeWidth, mSelectedDashWidth, mSelectedDashGap))
            }

            if (mPressed) {
                val pressedDrawable = LayerDrawable(
                    arrayOf(
                        createNormalDrawable(),
                        createFillAndStrokeColorDrawable(
                            if (mSolidColor == Color.TRANSPARENT) Color.TRANSPARENT else pressedMaskColor,
                            if (mStrokeColor == Color.TRANSPARENT) Color.TRANSPARENT else pressedMaskColor,
                            mStrokeWidth, mDashWidth, mDashGap)
                    )
                )
                addState(intArrayOf(attr.state_enabled, attr.state_pressed), pressedDrawable)
            }

            addState(StateSet.WILD_CARD, createNormalDrawable())
        }
    }

    override fun statelessBuild(): Drawable {
        return createNormalDrawable()
    }

    private fun createNormalDrawable(): Drawable {
        return GradientDrawable().apply {
            shape = mShape
            cornerRadii = mCornerRadii

            setColor(mSolidColor)
            setStroke(mStrokeWidth, mStrokeColor, mDashWidth, mDashGap)

            if (mGradientColors?.isNotEmpty() == true) {
                gradientType = mGradientType
                orientation = mGradientOrientation
                colors = mGradientColors
            }

            setSize(mWidth, mHeight)
        }
    }

    private fun createFillAndStrokeColorDrawable(
        @ColorInt fillColor: Int,
        @ColorInt strokeColor: Int,
        strokeWidth: Int,
        dashWidth: Float,
        dashGap: Float
    ): Drawable {
        return GradientDrawable().apply {
            shape = mShape
            cornerRadii = mCornerRadii

            setColor(fillColor)

            if (strokeColor != fillColor && strokeColor != Color.TRANSPARENT) {
                setStroke(strokeWidth, strokeColor, dashWidth, dashGap)
            }

            setSize(mWidth, mHeight)
        }
    }

    private fun createFillColorDrawable(@ColorInt fillColor: Int): Drawable {
        return GradientDrawable().apply {
            shape = mShape
            cornerRadii = mCornerRadii

            setColor(fillColor)

            setSize(mWidth, mHeight)
        }
    }
}