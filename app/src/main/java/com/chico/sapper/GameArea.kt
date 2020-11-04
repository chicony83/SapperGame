package com.chico.sapper

import android.util.Log
import kotlin.random.Random
import kotlin.random.nextInt

class GameArea(
    val widthGameArea:Int = 10,
    val heightGameArea:Int = 10,
    val maxMines:Int = 10
) {

        private val gameArea = Array(widthGameArea) {
            IntArray(
                heightGameArea
            )
    }


    fun newCleanGameArea() {
        for (x in 0..widthGameArea) {
            for (y in 0..heightGameArea) {
                gameArea[x][y] = 0

            }
        }
        Log.i("TAG","clean game Area created")
    }

    fun setMinesOnGameArea() {
        var mines = 0
        while (mines < maxMines) {
            val x = widthGameArea.rndNum()
            val y = heightGameArea.rndNum()
            gameArea[x][y] = 1
            mines++
            Log.i("TAG","mine $mines create on x = $x y = $y")
        }
        Log.i("TAG","all mines created")
    }

    private fun Int.rndNum(): Int {
        return Random.nextInt(0..this)
    }


}