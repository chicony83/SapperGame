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

import com.chico.sapper.settings.CurrentGameSetting
import kotlin.random.Random
import kotlin.random.nextInt

class GameArea(
    currentGameSetting: CurrentGameSetting,
    private val mineValue: Int = 9,
    private val heightGameAreaForCreate: Int = currentGameSetting.sizeGameAreaArray,
    private val widthGameAreaForCreate: Int = currentGameSetting.sizeGameAreaArray,
    private val heightGameArea: Int = currentGameSetting.sizeGameAreaArray - 1,
    private val widthGameArea: Int = currentGameSetting.sizeGameAreaArray - 1,

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
        for (y in 0..heightGameArea) {
            for (x in 0..widthGameArea) {
                minesArea[y][x] = 0
                isCellOpen[y][x] = false
                markers[y][x] = -1
            }
        }
    }

    fun setMinesOnMinesArea(currentGameSetting: CurrentGameSetting) {
        var minesInstalled = 0
        var x: Int = 0
        var y: Int = 0
        while (minesInstalled < currentGameSetting.mines) {
            x = rndNum()
            y = rndNum()
            while (minesArea[y][x] == mineValue) {
                x = rndNum()
                y = rndNum()
            }

            minesArea[y][x] = mineValue
            minesInstalled++

            setNumbersNearMines(y, x)
        }
    }

    private fun setNumbersNearMines(y: Int, x: Int) {
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
        if (minesArea[y + 1][x + 1] != mineValue) {
            incValue(y + 1, x + 1)
        }
    }

    private fun centerBottom(y: Int, x: Int) {
        if (minesArea[y + 1][x] != mineValue) {
            incValue(y + 1, x)
        }
    }

    private fun leftBottom(y: Int, x: Int) {
        if (minesArea[y + 1][x - 1] != mineValue) {
            incValue(y + 1, x - 1)
        }
    }

    private fun rightMiddle(y: Int, x: Int) {
        if (minesArea[y][x + 1] != mineValue) {
            incValue(y, x + 1)
        }
    }

    private fun leftMiddle(y: Int, x: Int) {
        if (minesArea[y][x - 1] != mineValue) {
            incValue(y, x - 1)
        }
    }

    private fun rightTop(y: Int, x: Int) {
        if (minesArea[y - 1][x + 1] != mineValue) {
            incValue(y - 1, x + 1)
        }
    }

    private fun centerTop(y: Int, x: Int) {
        if (minesArea[y - 1][x] != mineValue) {
            incValue(y - 1, x)
        }
    }

    private fun leftTop(y: Int, x: Int) {
        if (minesArea[y - 1][x - 1] != mineValue) {
            incValue(y - 1, x - 1)
        }
    }

    private fun incValue(y: Int, x: Int) {
        minesArea[y][x]++
    }

    private fun rndNum(): Int {
        return Random.nextInt(0..heightGameArea)
    }

    fun getMinesCellValue(yTouchOnArea: Int, xTouchOnArea: Int): Int {
        return minesArea[yTouchOnArea][xTouchOnArea]
    }

    fun isCellOpenCheck(yTouchOnArea: Int, xTouchOnArea: Int): Boolean {
        return isCellOpen[yTouchOnArea][xTouchOnArea]
    }

    fun isCellOpenSetTry(yTouchOnArea: Int, xTouchOnArea: Int) {
        isCellOpen[yTouchOnArea][xTouchOnArea] = true
    }

    fun isMineMarkerHire(yTouchOnArea: Int, xTouchOnArea: Int): Boolean {
        val isMineMarker = false
        return if (markers[yTouchOnArea][xTouchOnArea] != 2) !isMineMarker
        else return isMineMarker
    }

    fun countMineMarkers(): Int {
        var result = 0
        for (y in 0..heightGameArea) {
            for (x in 0..widthGameArea) {
                if (markers[y][x] == 2) {
                    result++
                }
            }
        }
        return result
    }

    fun checkTheFlagsSet(): Boolean {
        var isFlagsSetWright: Boolean = true

        for (y in 0..heightGameArea) {
            for (x in 0..widthGameArea) {
                if (markers[y][x] == 2) {
                    if (minesArea[y][x] != mineValue)
                        return false
                }
            }
        }
        return isFlagsSetWright
    }

    fun setOpenMarker(yTouchOnAreaInt: Int, xTouchOnAreaInt: Int) {
        markers[yTouchOnAreaInt][xTouchOnAreaInt] = 0
    }

    fun setMayBeMarker(yTouchOnAreaInt: Int, xTouchOnAreaInt: Int) {
        markers[yTouchOnAreaInt][xTouchOnAreaInt] = 1
    }

    fun setMineMarker(yTouchOnAreaInt: Int, xTouchOnAreaInt: Int) {
        markers[yTouchOnAreaInt][xTouchOnAreaInt] = 2
    }

    fun getMarker(yTouchOnAreaInt: Int, xTouchOnAreaInt: Int): Int {
        return markers[yTouchOnAreaInt][xTouchOnAreaInt]
    }
}