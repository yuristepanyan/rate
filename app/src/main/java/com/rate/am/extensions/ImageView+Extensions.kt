package com.rate.am.extensions

import android.widget.ImageView
import com.bumptech.glide.RequestManager

fun ImageView.loadImageCenterInside(glide: RequestManager, url: String) {
    glide.load(url)
        .centerInside()
        .into(this)
}