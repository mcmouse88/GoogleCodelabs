package com.mcmouse88.sunflower.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import com.google.android.material.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapeAppearancePathProvider

/**
 * A Card view that clips the content of any shape, this should be done upstream in card,
 * working around it for now.
 */
class MaskedCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyle) {

    private val pathProvider = ShapeAppearancePathProvider()
    private val path = Path()
    private val shapeAppearance: ShapeAppearanceModel = ShapeAppearanceModel.builder(
        context,
        attrs,
        defStyle,
        R.style.Widget_MaterialComponents_CardView
    ).build()

    private val rect = RectF(0f, 0f, 0f, 0f)

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(path)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rect.right = w.toFloat()
        rect.bottom = h.toFloat()
        pathProvider.calculatePath(shapeAppearance, 1f, rect, path)
        super.onSizeChanged(w, h, oldw, oldh)
    }
}