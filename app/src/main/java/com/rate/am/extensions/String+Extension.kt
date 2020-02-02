package com.rate.am.extensions

import com.rate.am.BuildConfig

fun String.getFullUrl() = BuildConfig.BASE_IMAGE_URL + this