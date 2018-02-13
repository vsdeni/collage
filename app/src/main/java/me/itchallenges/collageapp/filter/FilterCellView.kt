package me.itchallenges.collageapp.filter

import android.content.Context
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import me.itchallenges.collageapp.R
import java.io.File


class FilterCellView(context: Context, image: File,
                     val filter: Filter,
                     val checked: Boolean,
                     private val onClickListener: OnClickListener) : FrameLayout(context) {

    private var imageView: ImageView
    private var checkBox: CheckBox

    init {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_collage_filter, this, true)
        imageView = view.findViewById(R.id.collage_image)
        checkBox = view.findViewById(R.id.collage_checkbox)
        checkBox.isChecked = checked
        checkBox.setOnClickListener(onClickListener)
        filter.apply(context, image, imageView)
    }

    fun isChecked(): Boolean =
            checkBox.isChecked
}