package com.chico.sapper.settings

class CurrentGameSetting {
    var sizeGameAreaArray: Int = 0
    var widthGameArea: Int = 0
    var heightGameArea: Int = 0

    var mines: Int = 0
    var numberOfCellsOnGameArea: Int = 0

    fun preparationLevelSetting(
        LEVEL_GAME: Int,
        settingLevels: SettingLevels
    ) {
        var size = 0
        var mines = 0
        when (LEVEL_GAME) {
            0 -> {
                size = settingLevels.veryEasyAreaSize
                mines = settingLevels.veryEasyGameMines
            }
            1 -> {
                size = settingLevels.easyGameAreaSize
                mines = settingLevels.easyGameMines
            }
            2 -> {
                size = settingLevels.normalGameAreaSize
                mines = settingLevels.normalGameMines
            }
            3 -> {
                size = settingLevels.hardGameAreaSize
                mines = settingLevels.hardGameMines
            }
        }
        setLevelsSetting(
            gameAreaSize = size,
            gameMines = mines
        )
    }

    private fun setLevelsSetting(
        gameAreaSize: Int,
        gameMines: Int
    ) {
        heightGameArea = gameAreaSize
        widthGameArea = gameAreaSize
        sizeGameAreaArray = gameAreaSize
        mines = gameMines
    }
}