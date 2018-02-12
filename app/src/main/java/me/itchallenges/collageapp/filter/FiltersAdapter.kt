package me.itchallenges.collageapp.filter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import me.itchallenges.collageapp.R


class FiltersAdapter(private val context: Context, private var filters: List<Filter>?) : RecyclerView.Adapter<FiltersAdapter.FilterViewHolder>() {
    constructor(context: Context) : this(context, null)

    fun setData(data: List<Filter>) {
        filters = data
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FilterViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_filter, parent, false) as ViewGroup
        return FilterViewHolder(view)
    }

    fun getFilter(position: Int): Filter = filters!![position]

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = filters?.size ?: 0

    override fun onBindViewHolder(holder: FilterViewHolder?, position: Int) {
        val filter = filters!![position]
        holder?.preview?.setImageDrawable(getBitmap(filter))
        holder?.title?.text = filter.name
    }

    private fun getBitmap(filter: Filter): Drawable {
        return context.resources.getDrawable(R.drawable.filter_placeholder)
    }

    class FilterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val preview: ImageView = view.findViewById(R.id.filter_preview)
        val title: TextView = view.findViewById(R.id.filter_title)
    }
}