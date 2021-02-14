package com.chico.sapper.viewModel

import com.chico.sapper.utils.ParseTime
import kotlinx.coroutines.delay

class GameTime(
    private val timeStart: Long,
    private val viewModelProvider: MyViewModel,
    private var isGameRun: Boolean
) {
    private val parseTime = ParseTime()
    private var timeCurrent = getCurrentTimeInMillis()
    private var timePreviousUpdate: Long = getCurrentTimeInMillis()
    private var timeOfGame: Long = 0

    suspend fun timeGo() {
        while (isGameRun) {
            timeCurrent = System.currentTimeMillis()
            delay(10)

            if ((timeCurrent - timePreviousUpdate) > 100) {

                timePreviousUpdate = getCurrentTimeInMillis()

                timeOfGame = timeCurrent - timeStart

                viewModelProvider.gameTime.postValue(parseTime.parseLongToTime(timeOfGame))
                viewModelProvider.winnerGameTime.postValue(timeOfGame)
            }
        }
    }

    private fun getCurrentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }

}