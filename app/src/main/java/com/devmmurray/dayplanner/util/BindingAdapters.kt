package com.devmmurray.dayplanner.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object BindingAdapters {

    @JvmStatic
    @BindingAdapter(value = ["imageUrl"])
    fun bindImageUrl(view: ImageView, icon: String) {
        val url = "https://openweathermap.org/img/wn/$icon@2x.png"
        Picasso.get()
            .load(url)
            .into(view)
    }
}