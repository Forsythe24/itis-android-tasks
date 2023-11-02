package com.itis.itis_android_tasks.utils

import android.util.DisplayMetrics

fun Int.getValueInPx(dm: DisplayMetrics): Int {
    return (this * dm.density).toInt()
}
