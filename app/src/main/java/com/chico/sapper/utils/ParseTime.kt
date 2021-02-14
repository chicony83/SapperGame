package com.chico.sapper.utils

class ParseTime {

    fun parseLongToTime(timeInMills: Long): String {
        val millis = timeInMills % 1000
        val seconds = (timeInMills / 1000) % 60
        val minute = seconds / 60
        val hour = minute / 60

        if (minute > 0) {

            if (hour > 0) return String.format("%02d:%02d:%02d:%02d", hour, minute, seconds, millis)

            return String.format("%02d:%02d:%02d", minute, seconds, millis)

        } else return String.format("%02d:%02d", seconds, millis)

    }

}