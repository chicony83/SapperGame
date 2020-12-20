package com.chico.sapper.utils

import android.annotation.SuppressLint
import android.util.Log
import com.chico.sapper.TAG
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

@SuppressLint("SimpleDateFormat", "NewApi")
fun Long.parseTimeFromMills(pattern: String = "hh-mm-ss"): String {

    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
//    formatter.timeZone = TimeZone.getTimeZone("GMT")

    val tz = SimpleTimeZone(0,"Out TimeZone")
    TimeZone.setDefault(tz)

    Log.i(TAG, "${formatter.timeZone}")

    return formatter.format(this)
}