package me.itchallenges.collageapp.filter

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import me.itchallenges.collageapp.R


class FilterCellView(context: Context,
                     image: Uri,
                     val filter: Filter,
                     val checked: Boolean,//TODO
                     onClickListener: OnClickListener) : FrameLayout(context) {

    private var imageView: ImageView
    private var checkBox: CheckBox

    init {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_collage_filter, this, true)
        imageView = view.findViewById(R.id.collage_image)
        imageView.isDrawingCacheEnabled = true
        checkBox = view.findViewById(R.id.collage_checkbox)
        checkBox.isChecked = checked
        checkBox.isDrawingCacheEnabled = false
        checkBox.setOnClickListener(onClickListener)
        filter.apply(context, image, imageView)
    }

    fun getBitmap(): Bitmap {
        imageView.buildDrawingCache()
        val bitmap = imageView.getDrawingCache(false).copy(Bitmap.Config.ARGB_8888, false)
        imageView.destroyDrawingCache()
        return bitmap
    }

    fun isChecked(): Boolean =
            checkBox.isChecked
}