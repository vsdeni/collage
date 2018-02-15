package me.itchallenges.collageapp.pattern

import android.content.Context
import android.graphics.*
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import me.itchallenges.collageapp.R


class PatternsAdapter(private val context: Context, private var patterns: List<Pattern>?) : RecyclerView.Adapter<PatternsAdapter.PatternViewHolder>() {

    constructor(context: Context) : this(context, null)

    fun setData(data: List<Pattern>) {
        patterns = data
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PatternViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_pattern, parent, false) as ViewGroup
        return PatternViewHolder(view)
    }

    fun getPattern(position: Int): Pattern = patterns!![position]

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = patterns?.size ?: 0

    override fun onBindViewHolder(holder: PatternViewHolder?, position: Int) {
        val pattern = patterns!![position]

        if (pattern.preview == null) {
            pattern.preview = getPreview(pattern)
        }

        holder?.preview?.setImageBitmap(pattern.preview)
        holder?.title?.text = pattern.name
    }

    class PatternViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val preview: ImageView = view.findViewById(R.id.pattern_preview)
        val title: TextView = view.findViewById(R.id.pattern_title)
    }

    private fun getPreview(pattern: Pattern): Bitmap {
        val width = 150
        val height = 150
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val fillPaint = Paint()
        fillPaint.style = Paint.Style.FILL
        fillPaint.color = Color.parseColor("#c6c6c6")

        val strokePaint = Paint()
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 2.toFloat()
        strokePaint.color = Color.parseColor("#747474")

        for (position in pattern.positions) {
            val rect = Rect()
            rect.top = position.y * (height / 10)
            rect.left = position.x * (width / 10)
            rect.right = rect.left + position.width * (width / 10)
            rect.bottom = rect.top + position.height * (height / 10)

            canvas.drawRect(rect, fillPaint)
            canvas.drawRect(rect, strokePaint)
        }

        return bitmap
    }
}