package com.chico.sapper

import com.chico.sapper.dto.cellsDB
import com.chico.sapper.settings.CurrentGameSetting

class ModificationDB {
    fun addCellsInDB(
        metrics: Metrics,
        currentGameSetting: CurrentGameSetting,
        sizeCell: Int,
        cellsDB: cellsDB
    ) {
        var widthArraySizeOfGameArray = currentGameSetting.sizeGameAreaArray - 1
        var heightArraySizeOfGameArray = currentGameSetting.sizeGameAreaArray - 1

        var idX = ""
        var idY = ""
        var id: String

        for (y in 0..heightArraySizeOfGameArray) {

            for (x in 0..widthArraySizeOfGameArray) {
                id = idY + idX

                val name: String = id

                val yMargin = y * sizeCell
                val xMargin = x * sizeCell
                var yPosition = y + 1
                val xPosition = x + 1

                cellsDB.fillingDB(
                    id = name,
                    yMargin = yMargin,
                    xMargin = xMargin,
                    yPosition = yPosition,
                    xPosition = xPosition
                )
            }
        }
    }

}
