package com.chico.sapper.settings

class CurrentGameSetting{
    var sizeGameAreaArray:Int = 0
    var widthGameArea :Int= 0
    var heightGameArea:Int = 0

    var mines:Int = 0
    var numberOfCellsOnGameArea:Int = 0

    fun preparationLevelSetting(
        LEVEL_GAME: Int,
        settingLevels: SettingLevels
    ) {
        when (LEVEL_GAME) {
            1 -> {
                setLevelsSetting(
                    settingLevels.easyGameAreaSize,
                    settingLevels.easyGameMines
                )
            }
            2 -> {
                setLevelsSetting(
                    settingLevels.normalGameAreaSize,
                    settingLevels.normalGameMines
                )
            }
            3 -> {
                setLevelsSetting(
                    settingLevels.hardGameAreaSize,
                    settingLevels.hardGameMines
                )
            }
        }
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