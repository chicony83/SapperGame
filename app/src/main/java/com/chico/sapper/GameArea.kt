/*--------------gameArea
* when shirtArea [y][x]
* 0-shirt
* 1-open Cell
*
* when minesArea[y][x]
* 0-mine
* 1 to 9 mine/s near
* */

package com.chico.sapper

import android.util.Log
import kotlin.random.Random
import kotlin.random.nextInt

class GameArea(
    currentGameSetting: CurrentGameSetting,
    val heightGameArea: Int = currentGameSetting.sizeArrayOfGameArea,
    val widthGameArea: Int = currentGameSetting.sizeArrayOfGameArea,
    val maxMines: Int = currentGameSetting.mines,

    private val minesArea: Array<IntArray> = Array(widthGameArea) {
        IntArray(
            heightGameArea
        )
    },

    private val isCellOpen: Array<BooleanArray> = Array(widthGameArea) {
        BooleanArray(
            heightGameArea
        )
    }

) {


    fun newCleanArea() {
//        Log.i("TAG","size gameArea height = $heightGameArea, width = $widthGameArea")
        for (y in 0 until heightGameArea) {
            for (x in 0 until widthGameArea) {
                minesArea[y][x] = 0
                isCellOpen[y][x] = false

//                Log.i("TAG","minesArea y $y , x $x = ${minesArea[y][x]}")
            }
        }
        Log.i("TAG", "clean game Area created")
    }

    fun setMinesOnMinesArea(currentGameSetting: CurrentGameSetting) {
        var mines = 0
        var x: Int
        var y: Int
        while (mines < currentGameSetting.mines) {
            x = rndNum()
            y = rndNum()

            minesArea[x][y] = 9
            mines++
//            Log.i("TAG", "mine $mines create on x = $x y = $y")
            Log.i("TAG", "mine $mines create on x = $x y = $y = ${minesArea[x][y]}")
        }
        Log.i("TAG", "all mines created")
    }

//    fun getMinesCellValue(
//        Int: y,
//        Int: x
//    ): Int {
//
//        return minesArea[y][x]
//    }


    private fun rndNum(): Int {
        return Random.nextInt(0 until heightGameArea)
    }
//    private fun Int.rndNum(): Int {
//        return Random.nextInt(1..this)
//    }

    fun getMinesCellValue(yTouchOnArea: Int, xTouchOnArea: Int): Int {
        val size = minesArea.size
//        Log.i("TAG", "size = $size")
//        Log.i("TAG", "yTouch = $yTouchOnArea , xTouch = $xTouchOnArea")
        val result = minesArea[yTouchOnArea][xTouchOnArea]
        Log.i("TAG", "result = $result")
        return result
        //return minesArea[yTouchOnArea][xTouchOnArea]
    }

    fun isCellOpenCheck(yTouchOnArea: Int, xTouchOnArea: Int): Boolean {
        return isCellOpen[yTouchOnArea][xTouchOnArea]
    }
    fun isCellOpenSetTry(yTouchOnArea: Int, xTouchOnArea: Int){
        isCellOpen[yTouchOnArea][xTouchOnArea] = true

        Log.i("TAG","cell y = $yTouchOnArea , x = $xTouchOnArea is open")
    }

    fun setNumberNearMines() {

    }


}