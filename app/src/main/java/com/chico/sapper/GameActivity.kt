package com.chico.sapper

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.chico.sapper.settings.CurrentGameSetting
import com.chico.sapper.settings.SettingLevels
import com.chico.sapper.viewModel.CounterViewModel
import kotlin.math.ceil

val TAG = "TAG"

class GameActivity : AppCompatActivity(), View.OnClickListener {


    private var settingLevels = SettingLevels()
    private val currentGameSetting = CurrentGameSetting()
    private val metrics = Metrics()
    private lateinit var gameArea: GameArea

    private var cellsDB = com.chico.sapper.dto.cellsDB
    private val touch = Touch()

    private lateinit var gameElementsHolder: RelativeLayout
    private lateinit var looseGameMessageLayout: LinearLayout
    private lateinit var winGameMessageLayout: LinearLayout

    private lateinit var buttonOpen: Button
    private lateinit var buttonMayBe: Button
    private lateinit var buttonMineIsHire: Button
    private lateinit var buttonSelectLevel: Button
    private lateinit var buttonSelectLevel2: Button

    private lateinit var minesLeftValue: TextView

    private lateinit var viewModelProvider: CounterViewModel

    private var selectStateWhatDo = 0

    private var isLoose: Boolean = false
    private var isWin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setActivityFlags()
        setActivityOrientation()

        var LEVEL_GAME: Int = intent.getIntExtra("LEVEL_GAME", 1)

        preparationOfLevelData(LEVEL_GAME, currentGameSetting, settingLevels)

        viewModelProvider = ViewModelProvider(this).get(CounterViewModel::class.java)

        sizeDisplay(metrics)
        countCellSize(metrics)

        observersCounterViewModel(viewModelProvider)

        gameArea = GameArea(currentGameSetting)
        gameArea.newCleanArea()
        gameArea.setMinesOnMinesArea(currentGameSetting)

        Log.wtf("TAG", "onCreateView: ")
        addCellsInDB(metrics, currentGameSetting)

        buttonOpen = findViewById(R.id.button_open)
        buttonMayBe = findViewById<Button>(R.id.button_mayBe)
        buttonMineIsHire = findViewById<Button>(R.id.button_mineIsHire)
        buttonSelectLevel = findViewById(R.id.button_selectLevel)
        buttonSelectLevel2 = findViewById(R.id.button_selectLevel2)

        minesLeftValue = findViewById(R.id.mines_left_value)

        buttonOpen.setOnClickListener(this)
        buttonMayBe.setOnClickListener(this)
        buttonMineIsHire.setOnClickListener(this)
        buttonSelectLevel.setOnClickListener(this)
        buttonSelectLevel2.setOnClickListener(this)

        setValuesOnGameArea(currentGameSetting)

//        infoToast(metrics)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        gameElementsHolder = findViewById(R.id.game_elements_holder)
        looseGameMessageLayout = findViewById(R.id.looseGameMessage_layout)
        winGameMessageLayout = findViewById(R.id.winGameMessage_layout)

        gameElementsHolder.layoutParams.height = metrics.sizeDisplayX

        fillingThePlayingArea()

        gameElementsHolder.setOnTouchListener { v: View, m: MotionEvent ->
            handleTouch(m)
            false
        }

    }


    private fun setValuesOnGameArea(currentGameSetting: CurrentGameSetting) {
        viewModelProvider.counterMines.postValue(currentGameSetting.mines)
    }

    private fun observersCounterViewModel(viewModelProvider: CounterViewModel) {
        viewModelProvider.counterMines.observe(
            this, {
                minesLeftValue.text = it.toString()
            }
        )
    }

    fun handleTouch(m: MotionEvent) {
        if (!isLoose) {
            if ((m.y > 0) and (m.y < metrics.gameCellSize * currentGameSetting.sizeArrayOfGameArea)) {
                if ((m.x > 0) and (m.x < metrics.gameCellSize * currentGameSetting.sizeArrayOfGameArea)) {
                    touch.yTouch = m.y.toInt()
                    touch.xTouch = m.x.toInt()
                    nextMove()
                }
            }
        }
    }

    private fun nextMove() {
        var xTouchOnAreaDouble = touch.xTouch / metrics.gameCellSize
        var yTouchOnAreaDouble = touch.yTouch / metrics.gameCellSize
        xTouchOnAreaDouble = ceil(xTouchOnAreaDouble)
        yTouchOnAreaDouble = ceil(yTouchOnAreaDouble)

        var yTouchOnAreaInt = yTouchOnAreaDouble.toInt() - 1
        var xTouchOnAreaInt = xTouchOnAreaDouble.toInt() - 1

        val yMargin = yTouchOnAreaInt * metrics.gameCellSize
        val xMargin = xTouchOnAreaInt * metrics.gameCellSize

        val sizeCell = metrics.gameCellSize.toInt()
        val param = RelativeLayout.LayoutParams(sizeCell, sizeCell)

        var mineMarkerForMarkerArea = 0

        param.topMargin = yMargin.toInt()
        param.leftMargin = xMargin.toInt()

        if (!gameArea.isMineMarkerHire(yTouchOnAreaInt, xTouchOnAreaInt)) {
            if (selectStateWhatDo == 0) {
                openGameCell(yTouchOnAreaInt, xTouchOnAreaInt, param)
            }
        }
        if (selectStateWhatDo == 1) {
            mayBeMineIsHere(yTouchOnAreaInt, xTouchOnAreaInt, param)
            mineMarkerForMarkerArea = 1
        }
        if (selectStateWhatDo == 2) {
            mineIsHere(yTouchOnAreaInt, xTouchOnAreaInt, param)
            mineMarkerForMarkerArea = 2
        }
        gameArea.setMarkerOnMarkerArea(yTouchOnAreaInt, xTouchOnAreaInt, mineMarkerForMarkerArea)
        val markers = gameArea.countMarkers()
        var mines = currentGameSetting.mines - markers
        viewModelProvider.counterMines.postValue(mines)

        if (mines == 0) {
            isWin = gameArea.checkTheFlagsSet()
            if (isWin) {
                endLevel()
            }
            if (!isWin) {

            }
        }

    }

    private fun mineIsHere(
        yTouchOnAreaInt: Int,
        xTouchOnAreaInt: Int,
        param: RelativeLayout.LayoutParams
    ) {
        val imageSource = ImageView(this)
        imageSource.setImageResource(R.drawable.mineishire)
        gameElementsHolder.addView(imageSource, param)
    }

    private fun mayBeMineIsHere(
        yTouchOnAreaInt: Int,
        xTouchOnAreaInt: Int,
        param: RelativeLayout.LayoutParams
    ) {
        val imageSource = ImageView(this)
        imageSource.setImageResource(R.drawable.maybe)
        gameElementsHolder.addView(imageSource, param)

    }

    private fun openGameCell(
        yTouchOnAreaInt: Int,
        xTouchOnAreaInt: Int,
        param: RelativeLayout.LayoutParams
    ) {
        if (!gameArea.isCellOpenCheck(yTouchOnAreaInt, xTouchOnAreaInt)) {

            gameArea.isCellOpenSetTry(yTouchOnAreaInt, xTouchOnAreaInt)

            val value = gameArea.getMinesCellValue(yTouchOnAreaInt, xTouchOnAreaInt)

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

                isLoose = true

                endLevel()

            }
            Log.i("TAG", "value in mines area $value")

            gameElementsHolder.addView(imageSource, param)
        }
    }

    private fun endLevel() {
        buttonOpen.setOnClickListener(null)
        buttonMayBe.setOnClickListener(null)
        buttonMineIsHire.setOnClickListener(null)
        gameElementsHolder.setOnClickListener(null)
        if (isWin) {
            winGameMessageLayout.visibility = View.VISIBLE
        }
        if (isLoose) {
            looseGameMessageLayout.visibility = View.VISIBLE
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

    override fun onClick(v: View?) {
        var text: String = ""
        if (v == buttonOpen) {
            selectStateWhatDo = 0
            text = "open"
        }
        if (v == buttonMayBe) {
            selectStateWhatDo = 1
            text = "may be"
        }
        if (v == buttonMineIsHire) {
            selectStateWhatDo = 2
            text = "mine is here"
        }
        if ((v == buttonSelectLevel) or (v == buttonSelectLevel2)) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        if (text.isNotEmpty()) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }
}