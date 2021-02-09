package com.chico.sapper.viewModel

import android.util.Log
import com.chico.sapper.utils.ParseTime
import kotlinx.coroutines.delay

class GameTime(
    private val timeStart: Long,
    private val viewModelProvider: MyViewModel,
    private var isGameRun: Boolean
) {
    private val parseTime = ParseTime()
    private var timeCurrent = getCurrentTimeInMillis()
    private var timePreviousUpdate:Long = getCurrentTimeInMillis()
    private var timeOfGame: Long = 0

    suspend fun timeGo() {
        val i = 1
        while (isGameRun) {
//        while (!isWin or !isLoose) {
            timeCurrent = System.currentTimeMillis()
            delay(10)

            if ((timeCurrent - timePreviousUpdate) > 100) {

                Log.i("TAG","time update")

                timePreviousUpdate = getCurrentTimeInMillis()

                timeOfGame = timeCurrent - timeStart


                val time = parseTime.parseLongToTime(timeOfGame)

                Log.i("TAG","game time = ${time}")

                viewModelProvider.gameTime.postValue(time)
            }
        }
    }

    //    }
    private fun getCurrentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }

}