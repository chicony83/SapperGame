package com.chico.sapper


import com.chico.sapper.dto.CellsDB
import com.chico.sapper.dto.enums.CellState
import com.chico.sapper.settings.CurrentGameSetting

class ModificationDB {
    fun addCellsInDB(
        currentGameSetting: CurrentGameSetting,
        sizeCell: Int,
        gameArea: GameArea,
        cellsDB: CellsDB,

        ) {
        val widthArraySizeOfGameArray = currentGameSetting.sizeGameAreaArray
        val heightArraySizeOfGameArray = currentGameSetting.sizeGameAreaArray

        val idX = ""
        val idY = ""
        var id: String

        for (y in 0 until heightArraySizeOfGameArray) {

            for (x in 0 until widthArraySizeOfGameArray) {
                id = idY + idX

                val name: String = id

                val yMargin = y * sizeCell
                val xMargin = x * sizeCell
                var yPosition = y + 1
                val xPosition = x + 1

                val value = gameArea.getMinesCellValue(y, x)

                cellsDB.fillingDB(
                    id = name,
                    value = value,
                    yMargin = yMargin,
                    xMargin = xMargin,
                    yPosition = yPosition,
                    xPosition = xPosition
                )
            }
        }
    }

    fun modificationCellState(
        cellsDB: CellsDB,
        gameArea: GameArea,
        currentGameSetting: CurrentGameSetting
    ) {
        var counter = 0

        val heightArraySizeOfGameArray = currentGameSetting.sizeGameAreaArray
        val widthArraySizeOfGameArray = currentGameSetting.sizeGameAreaArray

        for (y in 0 until heightArraySizeOfGameArray) {
            for (x in 0 until widthArraySizeOfGameArray) {
                var cellState = CellState.CLOSE
                when (gameArea.getMarker(y, x)) {
                    0 -> cellState = CellState.OPEN

                    1 -> cellState = CellState.MAYBE_MARKER
                    2 -> cellState = CellState.MINE_MARKER
                }

                cellsDB.changeCellState(
                    index = counter,
                    state = cellState
                )
                counter++
            }
        }
    }
}
