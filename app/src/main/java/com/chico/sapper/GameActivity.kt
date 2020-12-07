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
import kotlin.math.ceil

val TAG = "TAG"

class GameActivity : AppCompatActivity() {


    private var settingLevels = SettingLevels()
    private val currentGameSetting = CurrentGameSetting()
    private val metrics = Metrics()
    lateinit var gameArea: GameArea

    private var cellsDB = com.chico.sapper.dto.cellsDB
    private val touch = Touch()

    private lateinit var gameElementsHolder: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setActivityFlags()
        setActivityOrientation()

        var LEVEL_GAME: Int = intent.getIntExtra("LEVEL_GAME", 1)

        preparationOfLevelData(LEVEL_GAME, currentGameSetting, settingLevels)

        sizeDisplay(metrics)
        countCellSize(metrics)

        gameArea = GameArea(currentGameSetting)
        gameArea.newCleanArea()
        gameArea.setMinesOnMinesArea(currentGameSetting)


        Log.wtf("TAG", "onCreateView: ")
        addCellsInDB(metrics, currentGameSetting)


//        infoToast(metrics)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        gameElementsHolder = findViewById(R.id.game_elements_holder)

        fillingThePlayingArea()

        gameElementsHolder.setOnTouchListener { v: View, m: MotionEvent ->
            handleTouch(m)
            false
        }

    }

    fun handleTouch(m: MotionEvent) {

        if ((m.y > 0) and (m.y < metrics.gameCellSize*currentGameSetting.sizeArrayOfGameArea)) {
            if ((m.x > 0) and (m.x < metrics.gameCellSize*currentGameSetting.sizeArrayOfGameArea)) {
                touch.yTouch = m.y.toInt()
                touch.xTouch = m.x.toInt()

                nextMove()
            }
        }
    }

    private fun nextMove() {

        var xTouchOnAreaDoule = touch.xTouch / metrics.gameCellSize
        var yTouchOnAreaDouble = touch.yTouch / metrics.gameCellSize
        xTouchOnAreaDoule = ceil(xTouchOnAreaDoule)
        yTouchOnAreaDouble = ceil(yTouchOnAreaDouble)

        var yTouchOnAreaInt = yTouchOnAreaDouble.toInt() - 1
        var xTouchOnAreaInt = xTouchOnAreaDoule.toInt() - 1

        if (!gameArea.isCellOpenCheck(yTouchOnAreaInt, xTouchOnAreaInt)) {

            gameArea.isCellOpenSetTry(yTouchOnAreaInt, xTouchOnAreaInt)

            val value = gameArea.getMinesCellValue(yTouchOnAreaInt, xTouchOnAreaInt)
            val yMargin = yTouchOnAreaInt * metrics.gameCellSize
            val xMargin = xTouchOnAreaInt * metrics.gameCellSize

            val sizeCell = metrics.gameCellSize.toInt()
            val param = RelativeLayout.LayoutParams(sizeCell, sizeCell)

            param.topMargin = yMargin.toInt()
            param.leftMargin = xMargin.toInt()
            val imageSource = ImageView(this)
            if (value == 0) {
                imageSource.setImageResource(R.drawable.open)
            }
            if (value == 1) {
                imageSource.setImageResource(R.drawable.one)
            }
            if (value == 2) {
                imageSource.setImageResource(R.drawable.two)
            }
            if (value == 3) {
                imageSource.setImageResource(R.drawable.three)
            }
            if (value == 4) {
                imageSource.setImageResource(R.drawable.four)
            }
            if (value == 5) {
                imageSource.setImageResource(R.drawable.five)
            }
            if (value == 6) {
                imageSource.setImageResource(R.drawable.six)
            }
            if (value == 7) {
                imageSource.setImageResource(R.drawable.seven)
            }
            if (value == 8) {
                imageSource.setImageResource(R.drawable.eight)
            }
            if (value == 9) {
                imageSource.setImageResource(R.drawable.mine)

                Log.i("TAG", "---WARNING MINE IS HIRE!!!---")
            }
            Log.i("TAG", "value in mines area $value")

            gameElementsHolder.addView(imageSource, param)
        }
    }

    private fun fillingThePlayingArea() {
        val sizeDB = cellsDB.cellsDataBase.size
        val cellsDB = cellsDB.cellsDataBase
        var idCell: String


        for (id in 0 until sizeDB) {
            idCell = cellsDB[id].toString()

            createGameElementById(id)
        }
    }

    private fun createGameElementById(id: Int) {
        val sizeCell = metrics.gameCellSize.toInt()
        val param = RelativeLayout.LayoutParams(sizeCell, sizeCell)

        param.topMargin = cellsDB.cellsDataBase[id].yMargin
        param.leftMargin = cellsDB.cellsDataBase[id].xMargin

        val imageSource = ImageView(this)
        imageSource.setImageResource(R.drawable.shirt2)

        gameElementsHolder.addView(imageSource, param)
        Log.i("TAG", "game Element Created")
    }

    private fun countCellSize(metrics: Metrics) {
        metrics.gameCellSize =
            (metrics.sizeDisplayX / currentGameSetting.sizeArrayOfGameArea).toDouble()
    }

    private fun addCellsInDB(
        metrics: Metrics,
        currentGameSetting: CurrentGameSetting
    ) {
        var numberOfCells = currentGameSetting.numberOfCellsOnGameArea

        var widthArraySizeOfGameArray = currentGameSetting.sizeArrayOfGameArea - 1
        var heightArraySizeOfGameArray = currentGameSetting.sizeArrayOfGameArea - 1

        val sizeCell = metrics.gameCellSize.toInt()

        var idX: String = ""
        var idY: String = ""
        var id: String

        for (y in 0..heightArraySizeOfGameArray) {

            idY = "Y$y"

            for (x in 0..widthArraySizeOfGameArray) {
                idX = "X$x"
                id = idY + idX

                val name: String = id

                val yMargin = y * sizeCell
                val xMargin = x * sizeCell
                var yPosition = y + 1
                val xPosition = x + 1

                cellsDB.addCell(
                    id = name,
                    yMargin = yMargin,
                    xMargin = xMargin,
                    yPosition = yPosition,
                    xPosition = xPosition
                )

//                Log.i(
//                    "TAG",
//                    "yPosition = $yPosition , xPosition = $xPosition "
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
                currentGameSetting.sizeArrayOfGameArea = settingLevels.easyGameAreaSize
                currentGameSetting.mines = settingLevels.easyGameMines
            }
            2 -> {
                currentGameSetting.sizeArrayOfGameArea = settingLevels.normalGameAreaSize
                currentGameSetting.mines = settingLevels.normalGameMines
            }
            3 -> {
                currentGameSetting.sizeArrayOfGameArea = settingLevels.hardGameAreaSize
                currentGameSetting.mines = settingLevels.hardGameMines
            }
        }
        currentGameSetting.numberOfCellsOnGameArea = countCellsOnGameArea(
            currentGameSetting.sizeArrayOfGameArea
        )
        Log.i("TAG", "size game area = ${currentGameSetting.sizeArrayOfGameArea}")
    }

    private fun countCellsOnGameArea(sizeGameArea: Int): Int {
        return sizeGameArea * sizeGameArea
    }

    private fun infoToast(metrics: Metrics) {
        Toast.makeText(
            this,
            "size display x= ${metrics.sizeDisplayX},y = ${metrics.sizeDisplayY}",
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
}