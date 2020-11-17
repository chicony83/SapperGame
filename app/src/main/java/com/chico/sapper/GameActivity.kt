package com.chico.sapper

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity(){

    private var settingLevels = SettingLevels()
    private val currentGameSetting = CurrentGameSetting()
    private val metrics = Metrics()
    private val gameArea = GameArea(currentGameSetting)
    private var cellsDB = com.chico.sapper.dto.cellsDB

    private var touch = Touch(0,0)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setActivityFlags()
        setActivityOrientation()
        var LEVEL_GAME: Int = intent.getIntExtra("LEVEL_GAME", 1)

        preparationOfLevelData(LEVEL_GAME, currentGameSetting, settingLevels)

        sizeDisplay(metrics)
        countCellSize(metrics, currentGameSetting)

        gameArea.newCleanArea()
        gameArea.setMinesOnMinesArea()

        Log.wtf("TAG", "onCreateView: ")
        addCellsInDB(metrics, currentGameSetting)

//        infoToast(metrics)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        val gameElementsHolder = findViewById<RelativeLayout>(R.id.game_elements_holder)

        fillingThePlayingArea(gameElementsHolder)

        val listenersGame = ListenersGame()
        gameElementsHolder.setOnTouchListener { v: View, m: MotionEvent ->
            listenersGame.handleTouch(m,touch)
            false
        }



    }

    private fun fillingThePlayingArea(gameElementsHolder: RelativeLayout) {
        val sizeDB = cellsDB.cellsDataBase.size
        for (id in 0 until sizeDB) {
            createGameElement(id, gameElementsHolder)
        }
    }

    private fun createGameElement(id: Int, gameElementsHolder: RelativeLayout) {
        val sizeCell = metrics.gameCellSize.toInt()
        val param = RelativeLayout.LayoutParams(sizeCell, sizeCell)

        val gameElement = ImageView(this)
        param.topMargin = cellsDB.cellsDataBase[id].yMargin
        param.leftMargin = cellsDB.cellsDataBase[id].xMargin
        gameElement.setImageResource(R.drawable.shirt)

        gameElementsHolder.addView(gameElement, param)
        Log.i("TAG", "game Element Created")

    }

    private fun countCellSize(metrics: Metrics, currentGameSetting: CurrentGameSetting) {
        metrics.gameCellSize =
            (metrics.sizeDisplayX / currentGameSetting.widthArrayOfGameArea).toDouble()
    }

    private fun addCellsInDB(
        metrics: Metrics,
        currentGameSetting: CurrentGameSetting
    ) {
//        Log.wtf("TAG", "addCellsInDB: ", )
        var numberOfCells = currentGameSetting.numberOfCellsOnGameArea

        val widthArraySizeOfGameArray = currentGameSetting.widthArrayOfGameArea - 1
        val heightArraySizeOfGameArray = currentGameSetting.heightArrayOfGameArea - 1

//        val gameElementsHolder = findViewById<RelativeLayout>(R.id.game_elements_holder)

        val sizeCell = metrics.gameCellSize.toInt()
//        val param = RelativeLayout.LayoutParams(sizeCell, sizeCell)

        var idX: String = ""
        var idY: String = ""
        var id: String

        for (y in 0..heightArraySizeOfGameArray) {

//            Log.i("TAG", "y = $y")

            idY = "Y$y"

            for (x in 0..widthArraySizeOfGameArray) {
                idX = "X$x"
                id = idY + idX

                val name: String = id

//                Log.i("TAG", "x = $x")
//                val shirtCell = ImageView(this)
//                val shirtCell2 = ImageView(this)
//                shirtCell.setImageResource(R.drawable.shirt)
//                shirtCell2.setImageResource(R.drawable.shirt)

//                param.topMargin = y * sizeCell
//                param.leftMargin = x * sizeCell

                val yMargin = y * sizeCell
                val xMargin = x * sizeCell

//                shirtCell.id = id
//
//                gameElementsHolder.addView(shirtCell, param)
//                gameElementsHolder.addView(shirtCell2)

                cellsDB.addCell(id = name, yMargin = yMargin, xMargin = xMargin)

//                Log.i(
//                    "TAG",
//                    "leftMargin = ${y * sizeCell} , topMargin = ${x * sizeCell}"
//                )
            }
        }
    }

    private fun setActivityOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setActivityFlags() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun preparationOfLevelData(
        LEVEL_GAME: Int,
        currentGameSetting: CurrentGameSetting,
        settingLevels: SettingLevels
    ) {
        when (LEVEL_GAME) {
            1 -> {
                currentGameSetting.widthArrayOfGameArea = settingLevels.easyGameAreaSize
                currentGameSetting.mines = settingLevels.easyGameMines
            }
            2 -> {
                currentGameSetting.widthArrayOfGameArea = settingLevels.normalGameAreaSize
                currentGameSetting.mines = settingLevels.normalGameMines
            }
            3 -> {
                currentGameSetting.widthArrayOfGameArea = settingLevels.hardGameAreaSize
                currentGameSetting.mines = settingLevels.hardGameMines
            }
        }
        currentGameSetting.numberOfCellsOnGameArea = countCellsOnGameArea(
            currentGameSetting.widthArrayOfGameArea
        )
    }

    private fun countCellsOnGameArea(sizeGameArea: Int): Int {
        return sizeGameArea * sizeGameArea
    }

    private fun infoToast(metrics: Metrics) {
        Toast.makeText(
            this,
            "size display x= ${metrics.sizeDisplayX},y = ${metrics.sizeDisplayY}",
//            "size cell = ${metrics.gameCellSize}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun sizeDisplay(metrics: Metrics) {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        metrics.sizeDisplayX = size.x
        metrics.sizeDisplayY = size.y

    }

//    override fun onClick(v: View?) {
//
////        val perent = v as ViewGroup
////        var count = perent.childCount
////        for(i in 0 ..count){
//
////        }
//
//
//        val id:String = v?.id.toString()
//        Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show()
//
//    }

}