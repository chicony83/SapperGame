package com.chico.sapper.settings

data class SettingLevels(
    val veryEasyAreaSize:Int = 6,
    val easyGameAreaSize:Int = 10,
    val normalGameAreaSize:Int = 12,
    val hardGameAreaSize:Int = 14,

    val veryEasyGameMines: Int = 3,
    val easyGameMines:Int = 10,
    val normalGameMines:Int = 16,
    val hardGameMines:Int = 30
)