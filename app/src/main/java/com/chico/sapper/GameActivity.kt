package com.chico.sapper

import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    private var LEVEL_GAME: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        LEVEL_GAME = intent.getIntExtra("LEVEL_GAME", 1)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val settingLevels = SettingLevels()
        val currentGameSetting = CurrentGameSetting()

        preparationOfLevelData(LEVEL_GAME, currentGameSetting, settingLevels)

        val gameArea = GameArea(currentGameSetting)
        val metrics = Metrics()

        sizeDisplay(metrics)

        gameArea.newCleanArea()
        gameArea.setMinesOnMinesArea()



//        val onePicture = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            getDrawable(R.drawable.one)
//        } else {
//            TODO("VERSION.SDK_INT < LOLLIPOP")
//        }


        infoToast(metrics)
    }

    private fun preparationOfLevelData(
        LEVEL_GAME: Int,
        currentGameSetting: CurrentGameSetting,
        settingLevels: SettingLevels
    ) {
        when (LEVEL_GAME) {
            1 -> {
                currentGameSetting.sizeGameArea = settingLevels.easyGameAreaSize
                currentGameSetting.mines = settingLevels.easyGameMines
            }
            2 -> {
                currentGameSetting.sizeGameArea = settingLevels.normalGameAreaSize
                currentGameSetting.mines  = settingLevels.normalGameMines
            }
            3 -> {
                currentGameSetting.sizeGameArea = settingLevels.hardGameAreaSize
                currentGameSetting.mines  = settingLevels.hardGameMines
            }
        }
    }

    private fun infoToast(metrics: Metrics) {
        Toast.makeText(
            this,
            "size display x= ${metrics.sizeDisplayX},y = ${metrics.sizeDisplayY}",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun sizeDisplay(metrics: Metrics) {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        metrics.sizeDisplayX = size.x
        metrics.sizeDisplayY = size.y


        //Log.i("widthDisplay", String.valueOf(SettingGame.getWidthDisplay()));
        //Log.i("heightDisplay", String.valueOf(SettingGame.getHeightDisplay()));
    }

}