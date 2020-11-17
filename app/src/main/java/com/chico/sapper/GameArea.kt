package com.chico.sapper

import android.util.Log
import kotlin.random.Random
import kotlin.random.nextInt

class GameArea(currentGameSetting: CurrentGameSetting) {

    val heightGameArea: Int = currentGameSetting.widthArrayOfGameArea
    val widthGameArea: Int = currentGameSetting.widthArrayOfGameArea
    val maxMines: Int = currentGameSetting.mines

    private val minesArea = Array(widthGameArea) {
        IntArray(
            heightGameArea
        )
    }
    private val shirtArea = Array(widthGameArea) {
        IntArray(
            heightGameArea
        )
    }

    fun newCleanArea() {
        for (x in 0 until widthGameArea) {
            for (y in 0 until heightGameArea) {
                minesArea[x][y] = 0
                shirtArea[x][y] = 0
            }
        }
        Log.i("TAG", "clean game Area created")
    }

    fun setMinesOnMinesArea() {
        var mines = 0
        var x: Int
        var y: Int
        while (mines < maxMines) {
            x = (widthGameArea.rndNum()) - 1
            y = (heightGameArea.rndNum()) - 1

            minesArea[x][y] = 9
            mines++
            Log.i("TAG", "mine $mines create on x = $x y = $y")
        }
        Log.i("TAG", "all mines created")
    }

    private fun Int.rndNum(): Int {
        return Random.nextInt(1..this)
    }


}