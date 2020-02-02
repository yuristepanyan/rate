package com.rate.am.extensions

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

fun Fragment.intentTo(cls: Class<out Activity>) = Intent(context, cls)