package work.curioustools.jb_mobile.utils.third_party_libs

import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


/**
Requires:
implementation("com.github.bumptech.glide:glide:4.11.0")
annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
*/
fun AppCompatImageView.loadImageFromInternet(
    url: String,
    @DrawableRes placeholder: Int,
    @DrawableRes error: Int,
    @DrawableRes fallback: Int,

) {
    Glide
        .with(this.context)
        .load(url)
        .placeholder(placeholder)
        .error(error)
        .fallback(fallback)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}