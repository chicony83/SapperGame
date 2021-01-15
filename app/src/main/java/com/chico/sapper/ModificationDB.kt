package com.chico.sapper

import com.chico.sapper.dto.cellsDB
import com.chico.sapper.settings.CurrentGameSetting

class ModificationDB {
    fun addCellsInDB(
        currentGameSetting: CurrentGameSetting,
        sizeCell: Int,
        gameArea: GameArea,
        cellsDB: cellsDB
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

                val value = gameArea.getMinesCellValue(y,x)

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

    fun modificationDB(
        cellsDB: cellsDB,
        gameArea: GameArea,
        currentGameSetting: CurrentGameSetting
    ){
//        val size = cellsDB.cellsDataBase.size
        var counter = 0
//        var isOpen = false
//        var value:Int

        val heightArraySizeOfGameArray = currentGameSetting.sizeGameAreaArray
        val widthArraySizeOfGameArray = currentGameSetting.sizeGameAreaArray 
        
        for (y in 0 until heightArraySizeOfGameArray){
            for (x in 0 until widthArraySizeOfGameArray){

                cellsDB.modificationIsOpenDbCell(
                    index = counter,
                    isOpen = gameArea.isCellOpenCheck(y,x)
                )
                counter++
            }
        }

    }

}
