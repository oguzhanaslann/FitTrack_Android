package com.oguzhanaslann.commonui

import android.view.View
import androidx.databinding.BindingAdapter

// hide if
@BindingAdapter("app:hideIf")
fun View.hideIf(condition: Boolean) {
    visibility = if (condition) View.GONE else View.VISIBLE
}

// show only if
@BindingAdapter("app:showOnlyIf")
fun View.showOnlyIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.INVISIBLE
}
