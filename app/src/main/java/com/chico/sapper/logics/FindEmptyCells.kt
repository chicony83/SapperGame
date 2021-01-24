package com.chico.sapper.logics

import android.util.Log
import com.chico.sapper.GameArea
import com.chico.sapper.settings.CurrentGameSetting


class FindEmptyCells(
    currentGameSetting: CurrentGameSetting,
    private var firstCell: Boolean = false,
    private var gameArea: GameArea,
    private var fixedEmptyCells: Int = 0,
    private var newEmptyCells: Int = 0,
    private val heightGameArea: Int = currentGameSetting.sizeGameAreaArray - 1,
    private val widthGameArea: Int = currentGameSetting.sizeGameAreaArray - 1

) {
    val TAG = "TAG"
    fun clickOnEmptyCell(gameArea: GameArea, yTouchOnAreaInt: Int, xTouchOnAreaInt: Int) {
        this.gameArea = gameArea

        setFirstCell(yTouchOnAreaInt, xTouchOnAreaInt)
//        countFixedCells()


//            Log.i(TAG," fixed = $fixedEmptyCells")
//            Log.i(TAG," new = $newEmptyCells")
//            newEmptyCells = fixedEmptyCells

        if (gameArea.isCloseMarkerHire(yTouchOnAreaInt, xTouchOnAreaInt)) {

            checkCellsNearTargetCell(yTouchOnAreaInt, xTouchOnAreaInt)

            if (newEmptyCells > 0) {
                Log.i(TAG, "1 new = $newEmptyCells")

                while (newEmptyCells != fixedEmptyCells) {

                  Log.i(TAG, "2 fixed = $fixedEmptyCells")
                  Log.i(TAG, "2 new = $newEmptyCells")

                     fixedEmptyCells = newEmptyCells

                    for (y in 0..heightGameArea) {
                        for (x in 0..widthGameArea) {
                            if (gameArea.isMarkerForOpenCellHire(y, x)) {
                                checkCellsNearTargetCell(y, x)
                            }
                        }
                    }
                }
            }
        }

        findOpenCells()

    }

    private fun checkCellsNearTargetCell(y: Int, x: Int) {
        if (y <= heightGameArea) {
            checkCell((y + 1), x)
        }
        //снизу
        if (y >= 0) {
            checkCell((y - 1), x)
        }
        //право
        if (x <= widthGameArea) {
            checkCell(y, (x + 1))
        }
        //лево
        if (x >= 0) {
            checkCell(y, (x - 1))
        }
    }

    private fun checkCell(y: Int, x: Int) {
//        Log.i(TAG, "check y $y, x $x")
        if (
            ((y >= 0) and (y <= heightGameArea))
            and
            ((x >= 0) and (x <= widthGameArea))
        ) {
            if (
                (checkGameAreaCell(y, x))
                and
                (checkMarkerInAreaForOpen(y, x))
            ) {
                setMarkerInAreaForOpen(y, x)
//                Log.i(TAG, " set marker on y $y, x $x")
            }

        }
    }

    private fun checkMarkerInAreaForOpen(y: Int, x: Int): Boolean {
        return !gameArea.isMarkerForOpenCellHire(y, x)
    }

    private fun checkGameAreaCell(y: Int, x: Int): Boolean {
        return gameArea.isCellEmpty(y, x)
    }

    private fun setFirstCell(yTouchOnAreaInt: Int, xTouchOnAreaInt: Int) {
        setMarkerInAreaForOpen(yTouchOnAreaInt, xTouchOnAreaInt)
        if (!firstCell) firstCell = true
    }

    private fun setMarkerInAreaForOpen(y: Int, x: Int) {
        gameArea.setMarkerInAreaForOpen(y, x)
        newEmptyCells++
        Log.i(TAG, " new Empty Cells = $newEmptyCells")
    }

    private fun findOpenCells() {
        for (y in 0..heightGameArea) {
            for (x in 0..widthGameArea) {
                if (gameArea.isMarkerForOpenCellHire(y, x)) {
                    gameArea.setOpenMarker(y, x)
                }
            }
        }
    }

//    private fun countFixedCells() {
//        for (y in 0..heightGameArea) {
//            for (x in 0..widthGameArea) {
//                if (gameArea.isMarkerForOpenCellHire(y, x)) fixedEmptyCells++
//            }
//        }
//    }

}