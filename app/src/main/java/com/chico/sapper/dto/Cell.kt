package com.chico.sapper.dto

import android.util.Log

data class Cell(
    val id: String,
    val value: Int,
    val isOpen: Boolean,
    val yMargin: Int,
    val xMargin: Int,
    val yPosition: Int,
    val xPosition: Int
)

object cellsDB {
    val cellsDataBase = mutableListOf<Cell>()

    fun fillingDB(
        id: String,
        value: Int,
        yMargin: Int,
        xMargin: Int,
        yPosition: Int,
        xPosition: Int
    ) {
        cellsDataBase
            .add(
                Cell(
                    id = id,
                    value = value,
                    isOpen = false,
                    yMargin = yMargin,
                    xMargin = xMargin,
                    yPosition = yPosition,
                    xPosition = xPosition
                )

            )
    }

    fun modificationIsOpenDbCell(
        index: Int,
        isOpen: Boolean,

        ) {
        val cell = cellsDataBase[index]
        val id = cell.id
        val value = cell.value
        val yMargin = cell.yMargin
        val xMargin = cell.xMargin
        val yPosition = cell.yPosition
        val xPosition = cell.xPosition


        cellsDataBase[index] = Cell(
            id = id,
            value = value,
            isOpen = isOpen,
            yMargin = yMargin,
            xMargin = xMargin,
            yPosition = yPosition,
            xPosition = xPosition
        )
        val none = cellsDataBase[index].isOpen
        Log.i("TAG", none.toString())
    }

}