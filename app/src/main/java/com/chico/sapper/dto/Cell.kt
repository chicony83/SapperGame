package com.chico.sapper.dto

import android.util.Log

data class Cell(
    val id: String,
    val yMargin: Int,
    val xMargin: Int,
    val yPosition: Int,
    val xPosition: Int
)

object cellsDB {
    val cellsDataBase = mutableListOf<Cell>()

    fun addCell(
        id: String,
        yMargin: Int,
        xMargin: Int,
        yPosition: Int,
        xPosition: Int
    ) {
        cellsDataBase
            .add(
                Cell(
                    id = id,
                    yMargin = yMargin,
                    xMargin = xMargin,
                    yPosition = yPosition,
                    xPosition = xPosition
                )

            )
        Log.wtf("TAG", "addCell: id = $id")
    }

}