package com.chico.sapper.dto

import android.util.Log

data class Cell(
    val id: String,
    val yMargin: Int,
    val xMargin: Int
)

object cellsDB {
    val cellsDataBase = mutableListOf<Cell>()

    fun addCell(
        id: String,
        yMargin: Int,
        xMargin: Int
    ) {
        cellsDataBase
            .add(
                Cell(
                    id = id,
                    yMargin = yMargin,
                    xMargin = xMargin
                )

            )
        Log.wtf("TAG", "addCell: id = $id", )
    }

}