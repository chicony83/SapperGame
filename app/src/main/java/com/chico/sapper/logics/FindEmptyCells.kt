package com.chico.sapper.logics

import com.chico.sapper.GameArea
import com.chico.sapper.settings.CurrentGameSetting

class FindEmptyCells(
    currentGameSetting: CurrentGameSetting,
    private var firstCell: Boolean = false,
    private var gameArea: GameArea,
    private var fixedEmptyCells: Int = 0,
    private var newEmptyCells:Int = 0,
    private val heightGameArea: Int = currentGameSetting.sizeGameAreaArray - 1,
    private val widthGameArea: Int = currentGameSetting.sizeGameAreaArray - 1

) {

    fun clickOnEmptyCell(gameArea: GameArea, yTouchOnAreaInt: Int, xTouchOnAreaInt: Int) {
        this.gameArea = gameArea

        setFirstCell(yTouchOnAreaInt, xTouchOnAreaInt)
        countFixedCells()

        while(fixedEmptyCells !=newEmptyCells){
            newEmptyCells == fixedEmptyCells

            for (y in 0..heightGameArea){
                for (x in 0 .. widthGameArea){
                    if (gameArea.isMarkerForOpenCellHire(y,x)){
                        var yForCheck:Int
                        var xForCheck:Int
                        //сверху
                        checkCellUp((y-1),x)
                        //снизу
                        //право
                        //лево
                    }
                }
            }

            countFixedCells()
        }

        findEmptyCells(yTouchOnAreaInt, xTouchOnAreaInt)

        gameArea.setOpenMarker(yTouchOnAreaInt, xTouchOnAreaInt)


    }

    private fun checkCellUp(y: Int, x: Int) {
        if(gameArea.isCellEmpty(y,x)){
            gameArea.setMarkerInAreaForOpen(y,x)
        }
    }

    private fun setFirstCell(yTouchOnAreaInt: Int, xTouchOnAreaInt: Int) {
        gameArea.setMarkerInAreaForOpen(yTouchOnAreaInt, xTouchOnAreaInt)
        firstCell = true
    }

    private fun findEmptyCells(yTouchOnAreaInt: Int, xTouchOnAreaInt: Int) {

    }

    private fun countFixedCells() {
        for (y in 0..heightGameArea) {
            for (x in 0..widthGameArea) {
                if (gameArea.isMarkerForOpenCellHire(y,x)) fixedEmptyCells++
            }
        }
    }

}