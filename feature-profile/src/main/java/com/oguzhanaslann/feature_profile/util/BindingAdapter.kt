package com.oguzhanaslann.feature_profile.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter(value = ["imageUrl", "placeholder", "error"], requireAll = false)
fun ImageView.loadImage(imageUrl: String?, placeholder: Int, error: Int) {
    this.load(imageUrl) {
        placeholder(placeholder)
        error(error)
    }
}

@BindingAdapter("hide")
fun ImageView.hide(hide: Boolean) {
    this.visibility = if (hide) {
        ImageView.GONE
    } else {
        ImageView.VISIBLE
    }
}
