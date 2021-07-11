package com.chrispetersnz.triviaisfun.util

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

class DataBindingUtil {

    companion object {
        @JvmStatic
        @BindingAdapter("android:visibility")
        fun setVisibility(view: View, value: Boolean) {
            view.visibility = (if (value) View.VISIBLE else View.GONE)
        }


        @JvmStatic
        @BindingAdapter("android:background")
        fun setBackground(view: View, @ColorRes value: Int) {
            view.setBackgroundColor(ContextCompat.getColor(view.context, value))
        }
    }

}