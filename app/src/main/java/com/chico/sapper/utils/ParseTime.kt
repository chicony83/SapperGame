package com.chico.sapper.utils


class ParseTime {

    fun parseLongToTime(timeInMills: Long): String {
        val millis = timeInMills % 1000
        val seconds = (timeInMills / 1000) % 60
        val minute = seconds / 60
        val hour = minute / 60

        return "$hour : $minute : $seconds: $millis"
    }
}