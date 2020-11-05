package com.chico.sapper

data class SettingLevels(
    val easyGameAreaSize:Int = 10,
    val normalGameAreaSize:Int = 12,
    val hardGameAreaSize:Int = 14,

    val easyGameMines:Int = 10,
    val normalGameMines:Int = 20,
    val hardGameMines:Int = 40
)