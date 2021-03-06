package me.itchallenges.collageapp.pattern

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import me.itchallenges.collageapp.R


class PatternsAdapter(private val context: Context, private var patterns: List<Pattern>?) : RecyclerView.Adapter<PatternsAdapter.PatternViewHolder>() {

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
        val pattern: Pattern = patterns!![position]

        holder?.preview?.let {
            Picasso.with(context)
                    .load(pattern.preview)
                    .into(it)
        }

        holder?.title?.text = pattern.name
    }

    class PatternViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val preview: ImageView = view.findViewById(R.id.pattern_preview)
        val title: TextView = view.findViewById(R.id.pattern_title)
    }
}