package me.itchallenges.collageapp.filter

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import jp.wasabeef.picasso.transformations.BlurTransformation
import jp.wasabeef.picasso.transformations.gpu.ContrastFilterTransformation
import jp.wasabeef.picasso.transformations.gpu.SepiaFilterTransformation
import jp.wasabeef.picasso.transformations.gpu.ToonFilterTransformation


enum class Filter {
    NONE,
    TOON,
    BLUR,
    SEPIA,
    CONTRAST;

    fun apply(context: Context, image: Uri, imageView: ImageView) {
        val request = Picasso.with(context)
                .load(image)

        getTransformation(context, this)?.let {
            request.transform(it)
        }

        request.into(imageView)
    }

    fun apply(context: Context, resourceId: Int, imageView: ImageView) {
        val request = Picasso.with(context)
                .load(resourceId)

        getTransformation(context, this)?.let {
            request.transform(it)
        }

        request.into(imageView)
    }

    private fun getTransformation(context: Context, filter: Filter): Transformation? {
        return when (filter) {
            Filter.NONE -> null
            Filter.BLUR -> BlurTransformation(context)
            Filter.SEPIA -> SepiaFilterTransformation(context, 0.5f)
            Filter.TOON -> ToonFilterTransformation(context)
            Filter.CONTRAST -> ContrastFilterTransformation(context, 3.0f)
        }
    }
}