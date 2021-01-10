package com.chico.sapper.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat", "NewApi")
fun Long.parseTimeFromMills(pattern: String = "hh-mm-ss"): String {

    val formatter = SimpleDateFormat(pattern, Locale.getDefault())

    val tz = SimpleTimeZone(0,"Out TimeZone")
    TimeZone.setDefault(tz)

    return formatter.format(this)
}