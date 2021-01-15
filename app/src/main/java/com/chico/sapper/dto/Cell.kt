package com.chico.sapper.dto

import android.util.Log

data class Cell(
    val id: String,
    val value:Int,
    val isOpen:Boolean,
    val yMargin: Int,
    val xMargin: Int,
    val yPosition: Int,
    val xPosition: Int
)

object cellsDB {
    val cellsDataBase = mutableListOf<Cell>()

    fun fillingDB(
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
                    value = 0,
                    isOpen = false,
                    yMargin = yMargin,
                    xMargin = xMargin,
                    yPosition = yPosition,
                    xPosition = xPosition
                )

            )
    }

}