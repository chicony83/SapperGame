package com.chico.sapper.utils


class ParseTime {

    fun parseLongToString(timeInMills: Long): String {
        val hour = ((timeInMills / 100) / 60) / 60
        val minute = (timeInMills / 1000) / 60
        val seconds = (timeInMills / 1000) % 60

        return "$hour : $minute : $seconds"
    }
}