package com.chrispetersnz.triviaisfun.util

import android.view.View
import androidx.databinding.BindingAdapter

class DataBindingUtil {

    companion object {
        @JvmStatic
        @BindingAdapter("android:visibility")
        fun setVisibility(view: View, value: Boolean) {
            view.visibility = (if (value) View.VISIBLE else View.GONE)
        }

    }
}