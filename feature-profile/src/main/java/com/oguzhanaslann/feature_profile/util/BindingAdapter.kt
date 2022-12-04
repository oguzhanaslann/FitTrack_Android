package com.oguzhanaslann.feature_profile.util

import android.view.View
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

// hide if
@BindingAdapter("app:hideIf")
fun View.hideIf(condition: Boolean) {
    visibility = if (condition) View.GONE else View.VISIBLE
}
