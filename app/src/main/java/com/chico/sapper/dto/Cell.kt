package com.chico.sapper.dto

import android.util.Log
import com.chico.sapper.dto.enums.CellState

data class Cell(
    val id: String,
    val value: Int,
    val state: CellState,
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
                    state = CellState.CLOSE,
                    yMargin = yMargin,
                    xMargin = xMargin,
                    yPosition = yPosition,
                    xPosition = xPosition
                )

            )
    }

    fun changeCellState(
        index: Int,
        state: CellState
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
            state = state,
            yMargin = yMargin,
            xMargin = xMargin,
            yPosition = yPosition,
            xPosition = xPosition
        )
        val description = cellsDataBase[index].state
        Log.i("TAG", description.toString())
    }

}