package me.itchallenges.collageapp.filter

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.item_collage_filter.view.*
import me.itchallenges.collageapp.R


class FilterCellView(context: Context,
                     image: Uri,
                     val filter: Filter,
                     val checked: Boolean,
                     onClickListener: OnClickListener) : FrameLayout(context) {

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.item_collage_filter, this, true)
        collage_image.isDrawingCacheEnabled = true
        collage_checkbox.isChecked = checked
        collage_checkbox.isDrawingCacheEnabled = false
        collage_checkbox.setOnClickListener(onClickListener)
        filter.apply(context, image, collage_image)
    }

    fun getBitmap(): Bitmap {
        collage_image.buildDrawingCache()
        val bitmap = collage_image.getDrawingCache(false)
                .copy(Bitmap.Config.ARGB_8888, false)
        collage_image.destroyDrawingCache()
        return bitmap
    }

    fun isChecked(): Boolean =
            collage_checkbox.isChecked
}