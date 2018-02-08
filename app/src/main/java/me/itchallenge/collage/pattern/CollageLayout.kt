package me.itchallenge.collage.pattern

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import me.itchallenge.collage.R

/*
* Layout manager that organizes child views in grid manner
 * Based on solution from https://github.com/TheHiddenDuck/cell-layout
* */
open class CollageLayout : ViewGroup {
    private var columns = 0
    private var spacing = 0
    private var cellSize: Float = 0.toFloat()
    private val defColumns = 10
    private val defSpacing = 0
    private val defCellSize = 24

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context) : super(context)

    private fun initAttrs(context: Context, attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CollageLayout, 0, 0)

        try {
            columns = a.getInt(R.styleable.CollageLayout_columns, defColumns)
            spacing = a.getDimensionPixelSize(R.styleable.CollageLayout_spacing, defSpacing)
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        var width = 0
        var height = 0

        if (widthMode == View.MeasureSpec.AT_MOST || widthMode == View.MeasureSpec.EXACTLY) {
            width = View.MeasureSpec.getSize(widthMeasureSpec)
            val cellSizeMeasuredByWidth = (measuredWidth - paddingLeft - paddingRight).toFloat() / columns.toFloat()
            val cellSizeMeasuredByHeight = (measuredHeight - paddingTop - paddingBottom).toFloat() / columns.toFloat()

            if (cellSizeMeasuredByHeight != 0.toFloat()) {
                cellSize = Math.min(cellSizeMeasuredByHeight, cellSizeMeasuredByWidth)
            } else {
                cellSize = cellSizeMeasuredByWidth
            }
        } else {
            cellSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defCellSize.toFloat(), resources
                    .displayMetrics)
            width = (columns * cellSize).toInt()
        }

        val childCount = childCount
        var child: View

        var maxRow = 0

        for (i in 0 until childCount) {
            child = getChildAt(i)

            val layoutParams = child.layoutParams as LayoutParams

            val top = layoutParams.top
            val w = layoutParams.cellWidth
            val h = layoutParams.cellHeight

            val bottom = top + h

            val childWidthSpec = View.MeasureSpec.makeMeasureSpec((w * cellSize).toInt() - spacing * 2,
                    View.MeasureSpec.EXACTLY)
            val childHeightSpec = View.MeasureSpec.makeMeasureSpec((h * cellSize).toInt() - spacing * 2,
                    View.MeasureSpec.EXACTLY)
            child.measure(childWidthSpec, childHeightSpec)

            if (bottom > maxRow) {
                maxRow = bottom
            }
        }

        val measuredHeight = Math.round(maxRow * cellSize) + paddingTop + paddingBottom
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = View.MeasureSpec.getSize(heightMeasureSpec)
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            val atMostHeight = View.MeasureSpec.getSize(heightMeasureSpec)
            height = Math.min(atMostHeight, measuredHeight)
        } else {
            height = measuredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount

        var child: View
        for (i in 0 until childCount) {
            child = getChildAt(i)

            val layoutParams = child.layoutParams as LayoutParams

            val top = (layoutParams.top * cellSize).toInt() + paddingTop + spacing
            val left = (layoutParams.left * cellSize).toInt() + paddingLeft + spacing
            val right = ((layoutParams.left + layoutParams.cellWidth) * cellSize).toInt() + paddingLeft - spacing
            val bottom = ((layoutParams.top + layoutParams.cellHeight) * cellSize).toInt() + paddingTop - spacing

            child.layout(left, top, right, bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return CollageLayout.LayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is CollageLayout.LayoutParams
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return CollageLayout.LayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams()
    }

    class LayoutParams : ViewGroup.LayoutParams {

        /**
         * An Y coordinate of the top most cell the view resides in.
         */
        internal var top = 0

        /**
         * An X coordinate of the left most cell the view resides in.
         */
        internal var left = 0

        /**
         * Number of cells occupied by the view horizontally.
         */
        internal var cellWidth = 1

        /**
         * Number of cells occupied by the view vertically.
         */
        internal var cellHeight = 1

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CollageLayout)
            left = a.getInt(R.styleable.CollageLayout_layout_left, 0)
            top = a.getInt(R.styleable.CollageLayout_layout_top, 0)
            cellHeight = a.getInt(R.styleable.CollageLayout_layout_cellsHeight, -1)
            cellWidth = a.getInt(R.styleable.CollageLayout_layout_cellsWidth, -1)

            a.recycle()
        }

        constructor(params: ViewGroup.LayoutParams) : super(params) {

            if (params is LayoutParams) {
                left = params.left
                top = params.top
                cellHeight = params.height
                cellWidth = params.width
            }
        }

        @JvmOverloads constructor(width: Int = ViewGroup.LayoutParams.MATCH_PARENT, height: Int = ViewGroup.LayoutParams.MATCH_PARENT) : super(width, height) {}

    }
}