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

//        gameArea.setMinesOnMinesArea()


        Log.wtf("TAG", "onCreateView: ")
        addCellsInDB(metrics, currentGameSetting)


//        infoToast(metrics)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        gameElementsHolder = findViewById(R.id.game_elements_holder)

        fillingThePlayingArea()
        val listenersGame = ListenersGame()

        gameElementsHolder.setOnTouchListener { v: View, m: MotionEvent ->
            handleTouch(m)
            false
        }

    }

    fun handleTouch(m: MotionEvent) {
//        val xTouch = m.x.toInt()
//        val yTouch = m.y.toInt()
        touch.yTouch = m.y.toInt()
        touch.xTouch = m.x.toInt()

        gameMove()
//        Log.i("TAG","xTouch = $xTouch")
//        Log.i("TAG","yTouch = $yTouch")

    }

    private fun gameMove() {

        var xTouchOnAreaDoule = touch.xTouch / metrics.gameCellSize
        var yTouchOnAreaDouble = touch.yTouch / metrics.gameCellSize
        xTouchOnAreaDoule = ceil(xTouchOnAreaDoule)
        yTouchOnAreaDouble = ceil(yTouchOnAreaDouble)

        var yTouchOnAreaInt = yTouchOnAreaDouble.toInt()
        var xTouchOnAreaInt = xTouchOnAreaDoule.toInt()

        Log.i("TAG", "xTouchOnArea = $xTouchOnAreaInt")
        Log.i("TAG", "yTouchOnArea = $yTouchOnAreaInt")
        val result = gameArea.getMinesCellValue(yTouchOnAreaInt,xTouchOnAreaInt)
        Log.i("TAG","value un mines area $result")
//        val isOpen:Boolean = gameArea.isCellOpenCheck(yTouchOnAreaInt,xTouchOnAreaInt)
//        Log.i("TAG","is cell open $isOpen")
    }

    private fun fillingThePlayingArea() {
        val sizeDB = cellsDB.cellsDataBase.size
        val cellsDB = cellsDB.cellsDataBase
        var idCell: String
        for (id in 0 until sizeDB) {
            idCell = cellsDB[id].toString()

            //            Log.i("TAG","id =$id  idCell =$idCell")
            createGameElement(id)
        }
    }

    private fun createGameElement(id: Int) {
        val sizeCell = metrics.gameCellSize.toInt()
        val param = RelativeLayout.LayoutParams(sizeCell, sizeCell)

        val gameElement = ImageView(this)
        param.topMargin = cellsDB.cellsDataBase[id].yMargin
        param.leftMargin = cellsDB.cellsDataBase[id].xMargin
        gameElement.setImageResource(R.drawable.shirt)

        gameElementsHolder.addView(gameElement, param)
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

                Log.i(
                    "TAG",
//                    "leftMargin = ${y * sizeCell} , topMargin = ${x * sizeCell}"
                    "yPosition = $yPosition , xPosition = $xPosition "
                )
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
        Log.i("TAG","size game area = ${currentGameSetting.sizeArrayOfGameArea}")
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