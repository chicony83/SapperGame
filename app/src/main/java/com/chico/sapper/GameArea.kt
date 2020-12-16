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
    private val mine: Int = 9,
    private val heightGameAreaForCreate: Int = currentGameSetting.sizeArrayOfGameArea,
    private val widthGameAreaForCreate: Int = currentGameSetting.sizeArrayOfGameArea,
    private val heightGameArea: Int = currentGameSetting.sizeArrayOfGameArea - 1,
    private val widthGameArea: Int = currentGameSetting.sizeArrayOfGameArea - 1,
    val maxMines: Int = currentGameSetting.mines,

    private val minesArea: Array<IntArray> = Array(heightGameAreaForCreate) {
        IntArray(
            widthGameAreaForCreate
        )
    },

    private val isCellOpen: Array<BooleanArray> = Array(heightGameAreaForCreate) {
        BooleanArray(
            widthGameAreaForCreate
        )
    },
    private val markers: Array<IntArray> = Array(heightGameAreaForCreate) {
        IntArray(
            widthGameAreaForCreate
        )
    }


) {

    fun newCleanArea() {
        Log.i("TAG", "size gameArea height = $heightGameArea, width = $widthGameArea")
        for (y in 0..heightGameArea) {
            for (x in 0..widthGameArea) {
                minesArea[y][x] = 0
                isCellOpen[y][x] = false
                markers[y][x] = 0

//                Log.i("TAG","minesArea y $y , x $x = ${minesArea[y][x]}")
            }
        }
        Log.i("TAG", "clean game Area created")
    }

    fun setMinesOnMinesArea(currentGameSetting: CurrentGameSetting) {
        var minesInstalled = 0
        var x: Int = 0
        var y: Int = 0
        while (minesInstalled < currentGameSetting.mines) {
            x = rndNum()
            y = rndNum()
            while (minesArea[y][x] == mine) {
                x = rndNum()
                y = rndNum()
            }

            minesArea[y][x] = mine
            minesInstalled++

            setNumbersNearMines(y, x)

            Log.i("TAG", "mine $minesInstalled create on x = $x y = $y = ${minesArea[x][y]}")
        }
//        testMines()
        Log.i("TAG", "all mines created")
    }

//    private fun testMines() {
//        testTopMines()
//        testBottomMines()
//        testLeftMines()
//        testRightMines()
//    }

    private fun testRightMines() {
        minesArea[1][widthGameArea] = mine
        setNumbersNearMines(1, widthGameArea)

        minesArea[4][widthGameArea] = mine
        setNumbersNearMines(4, widthGameArea)

        minesArea[8][widthGameArea] = mine
        setNumbersNearMines(8, widthGameArea)
    }

    private fun testLeftMines() {
        minesArea[1][0] = mine
        setNumbersNearMines(1, 0)

        minesArea[2][0] = mine
        setNumbersNearMines(2, 0)

        minesArea[5][0] = mine
        setNumbersNearMines(5, 0)
    }

    private fun testBottomMines() {
        minesArea[heightGameArea][0] = mine
        setNumbersNearMines(heightGameArea, 0)

        minesArea[heightGameArea][1] = mine
        setNumbersNearMines(heightGameArea, 1)

        minesArea[heightGameArea][5] = mine
        setNumbersNearMines(heightGameArea, 5)

        minesArea[heightGameArea][widthGameArea] = mine
        setNumbersNearMines(heightGameArea, widthGameArea)
    }

    private fun testTopMines() {
        //leftTop
        minesArea[0][0] = mine
        setNumbersNearMines(0, 0)

        minesArea[0][1] = mine
        setNumbersNearMines(0, 1)

        //RightTop
        minesArea[0][widthGameArea] = mine
        setNumbersNearMines(0, widthGameArea)

        minesArea[0][5] = mine
        setNumbersNearMines(0, 5)
    }

    private fun setNumbersNearMines(y: Int, x: Int) {

        Log.i(TAG, "x = $x, y = $y")
//        minesArea[y][x]
        if (y == 0) {
            setNumbersNearLeftTopMine(x, y)
            setNumbersNearTopMines(x, y)
            setNumbersNearRightTopMine(x, y)
        }
        if (y == heightGameArea) {
            setNumbersNearLeftBottomMine(y, x)
            setNumbersNearBottomMines(y, x)
            setNumbersNearRightBottomMines(y, x)
        }
        if (x == 0) {
            setNumbersNearLeftMines(y, x)
        }
        if (x == widthGameArea) {
            setNumbersNearRightMines(y, x)
        }

        setNumbersNearMinesWithoutBorders(y, x)
    }

    private fun setNumbersNearRightMines(y: Int, x: Int) {
        if ((y > 0) and (y < heightGameArea)) {
            leftTop(y, x)
            centerTop(y, x)
            leftMiddle(y, x)
            leftBottom(y, x)
            centerBottom(y, x)
        }
    }

    private fun setNumbersNearLeftMines(y: Int, x: Int) {
        if ((y > 0) and (y < heightGameArea)) {
            centerTop(y, x)
            rightTop(y, x)
            rightMiddle(y, x)
            centerBottom(y, x)
            rightBottom(y, x)
        }
    }

    private fun setNumbersNearBottomMines(y: Int, x: Int) {
        if ((x > 0) and (x < widthGameArea)) {
            leftTop(y, x)
            centerTop(y, x)
            rightTop(y, x)
            leftMiddle(y, x)
            rightMiddle(y, x)
        }
    }

    private fun setNumbersNearRightBottomMines(y: Int, x: Int) {
        if (x == widthGameArea) {
            leftTop(y, x)
            centerTop(y, x)
            leftMiddle(y, x)
        }
    }

    private fun setNumbersNearLeftBottomMine(y: Int, x: Int) {
        if (x == 0) {
            centerTop(y, x)
            rightTop(y, x)
            rightMiddle(y, x)
        }
    }

    private fun setNumbersNearMinesWithoutBorders(y: Int, x: Int) {
        if ((y > 0) and (y < heightGameArea)) {
            if ((x > 0) and (x < widthGameArea)) {
                leftTop(y, x)
                centerTop(y, x)
                rightTop(y, x)
                leftMiddle(y, x)
                rightMiddle(y, x)
                leftBottom(y, x)
                centerBottom(y, x)
                rightBottom(y, x)
            }
        }
    }

    private fun setNumbersNearRightTopMine(x: Int, y: Int) {
        if (x == widthGameArea) {
            leftMiddle(y, x)
            leftBottom(y, x)
            centerBottom(y, x)
        }
    }

    private fun setNumbersNearLeftTopMine(x: Int, y: Int) {
        if (x == 0) {
            rightMiddle(y, x)
            centerBottom(y, x)
            rightBottom(y, x)
        }
    }

    private fun setNumbersNearTopMines(x: Int, y: Int) {
        if ((x > 0) and (x < widthGameArea)) {
            leftMiddle(y, x)
            rightMiddle(y, x)
            leftBottom(y, x)
            centerBottom(y, x)
            rightBottom(y, x)
        }
    }

    private fun rightBottom(y: Int, x: Int) {
        if (minesArea[y + 1][x + 1] != mine) {
            incValue(y + 1, x + 1)
        }
    }

    private fun centerBottom(y: Int, x: Int) {
        if (minesArea[y + 1][x] != mine) {
            incValue(y + 1, x)
        }
    }

    private fun leftBottom(y: Int, x: Int) {
        if (minesArea[y + 1][x - 1] != mine) {
            incValue(y + 1, x - 1)
        }
    }

    private fun rightMiddle(y: Int, x: Int) {
        if (minesArea[y][x + 1] != mine) {
            incValue(y, x + 1)
        }
    }

    private fun leftMiddle(y: Int, x: Int) {
        if (minesArea[y][x - 1] != mine) {
            incValue(y, x - 1)
        }
    }

    private fun rightTop(y: Int, x: Int) {
        if (minesArea[y - 1][x + 1] != mine) {
            incValue(y - 1, x + 1)
        }
    }

    private fun centerTop(y: Int, x: Int) {
        if (minesArea[y - 1][x] != mine) {
            incValue(y - 1, x)
        }
    }

    private fun leftTop(y: Int, x: Int) {
        if (minesArea[y - 1][x - 1] != mine) {
            incValue(y - 1, x - 1)
        }
    }

    private fun incValue(y: Int, x: Int) {
//        Log.i(TAG,"number at y = $y, x = $x = ${minesArea[y][x]}")
        minesArea[y][x]++
        Log.i(TAG, "up number at y = $y, x = $x ${minesArea[y][x]}")
    }

//    fun getMinesCellValue(
//        Int: y,
//        Int: x
//    ): Int {
//
//        return minesArea[y][x]
//    }


    private fun rndNum(): Int {
        return Random.nextInt(0..heightGameArea)
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

    fun isCellOpenSetTry(yTouchOnArea: Int, xTouchOnArea: Int) {
        isCellOpen[yTouchOnArea][xTouchOnArea] = true

        Log.i("TAG", "cell y = $yTouchOnArea , x = $xTouchOnArea is open")
    }

    fun setMarkerOnMarkerArea(yTouchOnAreaInt: Int, xTouchOnAreaInt: Int, marker: Int) {
        markers[yTouchOnAreaInt][xTouchOnAreaInt] = marker
    }

    fun countMarkers(): Int {
        var result = 0
        for (y in 0..heightGameArea){
            for (x in 0..widthGameArea){
                if (markers[y][x]==2){
                    result++
                }
            }
        }
        return result
    }
}