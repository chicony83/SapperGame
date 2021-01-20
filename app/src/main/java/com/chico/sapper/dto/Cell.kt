package com.chico.sapper.dto

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