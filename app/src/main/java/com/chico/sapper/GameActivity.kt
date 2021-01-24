package com.chico.sapper

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.chico.sapper.dto.CellsDB
import com.chico.sapper.dto.enums.CellState
import com.chico.sapper.dto.enums.WhatDo
import com.chico.sapper.logics.FindEmptyCells
import com.chico.sapper.settings.CurrentGameSetting
import com.chico.sapper.settings.SettingLevels
import com.chico.sapper.utils.ParseTime
import com.chico.sapper.utils.launchIoNotReturn
import com.chico.sapper.viewModel.CounterViewModel
import kotlinx.coroutines.*
import kotlin.math.ceil
import kotlin.properties.Delegates

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private var settingLevels = SettingLevels()
    private val currentGameSetting = CurrentGameSetting()
    private val metrics = Metrics()
    private val parseTime = ParseTime()
    private val modificationDB = ModificationDB()
    private lateinit var gameArea: GameArea
    private lateinit var findEmptyCells:FindEmptyCells

    private var sizeCell by Delegates.notNull<Int>()

    //    private var cellsDB = com.chico.sapper.dto.cellsDB
    private var cellsDB = CellsDB()
    private val touch = Touch()

    private lateinit var gameElementsHolder: RelativeLayout
    private lateinit var looseGameMessageLayout: LinearLayout
    private lateinit var winGameMessageLayout: LinearLayout

    private lateinit var param: RelativeLayout.LayoutParams

    private lateinit var buttonOpen: Button
    private lateinit var buttonMayBe: Button
    private lateinit var buttonMineIsHire: Button

    private lateinit var buttonSelectLevelIsWIN: Button
    private lateinit var buttonPlayAgainIsWIN: Button
    private lateinit var buttonSelectLevelIsLOOSE: Button
    private lateinit var buttonPlayAgainIsLOOSE: Button

    private lateinit var minesLeftValue: TextView
    private lateinit var timePassedValue: TextView
    private lateinit var timeOfEndGameValue: TextView

    private lateinit var toastTextOpen: String
    private lateinit var toastTextMayBeMineIsHere: String
    private lateinit var toastTextMineHere: String
    private lateinit var toastTextRunOutOfMineMarkers: String

    private lateinit var viewModelProvider: CounterViewModel

    private var selectStateWhatDo: WhatDo = WhatDo.OPEN

    private var leftToFindMines = -1

    private var isLoose: Boolean = false
    private var isWin: Boolean = false

    private var timeStart by Delegates.notNull<Long>()
    private var timeCurrent by Delegates.notNull<Long>()
    private var timePreviousUpdate: Long = 0
    private var timeOfGame: Long = 0

    private var isGameRun = false

    private var colorPrimary by Delegates.notNull<Int>()
    private var colorPrimaryVariant by Delegates.notNull<Int>()
    private var colorOnPrimary by Delegates.notNull<Int>()

    private lateinit var job: Job

    private var gameLevel by Delegates.notNull<Int>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setActivityFlags()
        setActivityOrientation()

        gameLevel = intent.getIntExtra("LEVEL_GAME", 0)

        currentGameSetting.preparationLevelSetting(gameLevel, settingLevels)
        currentGameSetting.numberOfCellsOnGameArea =
            countCellsOnGameArea(currentGameSetting.sizeGameAreaArray)


        viewModelProvider = ViewModelProvider(this).get(CounterViewModel::class.java)

        sizeDisplay(metrics)
        countCellSize(metrics)

        sizeCell = metrics.gameCellSize.toInt()

        observersCounterViewModel(viewModelProvider)

        gameArea = GameArea(currentGameSetting)
        gameArea.newCleanAreas()
        gameArea.setMinesOnMinesArea(currentGameSetting)

        findEmptyCells = FindEmptyCells(
            currentGameSetting = currentGameSetting,
            gameArea = gameArea
        )

        modificationDB.addCellsInDB(currentGameSetting, sizeCell, gameArea, cellsDB)

        buttonOpen = findViewById(R.id.button_open)
        buttonMayBe = findViewById(R.id.button_mayBe)
        buttonMineIsHire = findViewById(R.id.button_mineIsHire)

        buttonSelectLevelIsWIN = findViewById(R.id.button_selectLevel_isWIN)
        buttonPlayAgainIsWIN = findViewById(R.id.button_playAgain_isWIN)
        buttonSelectLevelIsLOOSE = findViewById(R.id.button_selectLevel_isLOOSE)
        buttonPlayAgainIsLOOSE = findViewById(R.id.button_playAgain_isLOOSE)


        minesLeftValue = findViewById(R.id.mines_left_value)
        timePassedValue = findViewById(R.id.time_passed_value)
        timeOfEndGameValue = findViewById(R.id.gameTime_textView)

        buttonOpen.setOnClickListener(this)
        buttonMayBe.setOnClickListener(this)
        buttonMineIsHire.setOnClickListener(this)
        buttonSelectLevelIsWIN.setOnClickListener(this)
        buttonPlayAgainIsWIN.setOnClickListener(this)
        buttonSelectLevelIsLOOSE.setOnClickListener(this)
        buttonPlayAgainIsLOOSE.setOnClickListener(this)

        setValuesOnGameArea(currentGameSetting)

        leftToFindMines = currentGameSetting.mines

        initLayouts()

        changeResourcesOfDayNightMode()

        getStringResources()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()

        gameElementsHolder.layoutParams.height = metrics.sizeDisplayX

        fillingThePlayingArea()

        gameElementsHolder.setOnTouchListener { v: View, m: MotionEvent ->
            handleTouch(m)
            false
        }

        isGameRun = true
        timeStart = getCurrentTimeInMillis()
        timePreviousUpdate = getCurrentTimeInMillis()

        launchIoNotReturn { gameTime() }
        pressButtonOpen("", buttonOpen)
    }

    private fun initLayouts() {
        gameElementsHolder = findViewById(R.id.game_elements_holder)
        looseGameMessageLayout = findViewById(R.id.looseGameMessage_layout)
        winGameMessageLayout = findViewById(R.id.winGameMessage_layout)
    }

    private fun changeResourcesOfDayNightMode() {
        val mode = this?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)

        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                getNightColorsResource()
                setNightBackgroundsOnMessageLayouts()
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                getDayColorsResource()
                setDayBackgroundsOnMessageLayouts()
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                getDayColorsResource()
                setDayBackgroundsOnMessageLayouts()
            }
        }
    }

    private fun setDayBackgroundsOnMessageLayouts() {
        looseGameMessageLayout.setBackgroundColor(colorPrimaryVariant)
        winGameMessageLayout.setBackgroundColor(colorPrimaryVariant)
    }

    private fun setNightBackgroundsOnMessageLayouts() {
        looseGameMessageLayout.setBackgroundColor(colorOnPrimary)
        winGameMessageLayout.setBackgroundColor(colorOnPrimary)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startMainMenu()
    }

    private fun getDayColorsResource() {
        colorPrimary = ContextCompat.getColor(this, R.color.gray_200)
        colorPrimaryVariant = ContextCompat.getColor(this, R.color.gray_140)
        colorOnPrimary = ContextCompat.getColor(this, R.color.gray_80)
    }

    private fun getNightColorsResource() {
        colorPrimary = ContextCompat.getColor(this, R.color.gray_140)
        colorPrimaryVariant = ContextCompat.getColor(this, R.color.gray_80)
        colorOnPrimary = ContextCompat.getColor(this, R.color.black)
    }

    private fun getStringResources() {
        toastTextOpen = getString(R.string.toastText_openCell)
        toastTextMayBeMineIsHere = getString(R.string.toastText_mayBeMineIsHere)
        toastTextMineHere = getString(R.string.toastText_mineHire)
        toastTextRunOutOfMineMarkers = getString(R.string.toastText_runOutOfMineMarkers)
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private fun gameTime() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (!isWin or !isLoose) {

                timeCurrent = System.currentTimeMillis()
                delay(10)

                if ((timeCurrent - timePreviousUpdate) > 1000) {

                    timePreviousUpdate = getCurrentTimeInMillis()

                    timeOfGame = timeCurrent - timeStart

                    viewModelProvider.gameTime.postValue(parseTime.parseLongToString(timeOfGame))
                }
            }
        }
    }

    private fun getCurrentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }

    private fun setValuesOnGameArea(currentGameSetting: CurrentGameSetting) {
        viewModelProvider.counterMines.postValue(currentGameSetting.mines)
        viewModelProvider.gameTime.postValue("0")
    }

    private fun observersCounterViewModel(viewModelProvider: CounterViewModel) {
        viewModelProvider.counterMines.observe(
            this, {
                minesLeftValue.text = it.toString()
            }
        )

        viewModelProvider.gameTime.observe(
            this, {
                timePassedValue.text = it.toString()
            }
        )
    }

    private fun handleTouch(m: MotionEvent) {
        if ((!isLoose) and (!isWin)) {
            if ((m.y > 0) and (m.y < metrics.gameCellSize * currentGameSetting.sizeGameAreaArray)) {
                if ((m.x > 0) and (m.x < metrics.gameCellSize * currentGameSetting.sizeGameAreaArray)) {
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

        param = RelativeLayout.LayoutParams(sizeCell, sizeCell)

        param.topMargin = yMargin.toInt()
        param.leftMargin = xMargin.toInt()

        when (selectStateWhatDo) {
            WhatDo.OPEN -> {
                if (gameArea.isMineMarkerHire(yTouchOnAreaInt, xTouchOnAreaInt)) {
                    if (!gameArea.isCellOpen(yTouchOnAreaInt, xTouchOnAreaInt)) {
                        //gameArea.setOpenMarker(yTouchOnAreaInt, xTouchOnAreaInt)
                        findEmptyCells.clickOnEmptyCell(gameArea,yTouchOnAreaInt, xTouchOnAreaInt)
                        Log.i("TAG", "y = $yTouchOnAreaInt , x = $xTouchOnAreaInt")
                    }
                }
                Log.i("TAG", " select state what do = $selectStateWhatDo")
            }
            WhatDo.MAYbE -> {
                Log.i("TAG", " select state what do = $selectStateWhatDo")
                gameArea.setMayBeMarker(yTouchOnAreaInt, xTouchOnAreaInt)
            }
            WhatDo.MINEiShIRE -> {
                if (leftToFindMines == 0) {
                    pressButtonOpen("", buttonOpen)
                    selectStateWhatDo = WhatDo.OPEN
                    showMessage(toastTextRunOutOfMineMarkers)
                } else {
                    Log.i("TAG", " select state what do = $selectStateWhatDo")
                    gameArea.setMineMarker(yTouchOnAreaInt, xTouchOnAreaInt)
                }
            }
        }

        modificationDB.modificationCellState(cellsDB, gameArea, currentGameSetting)

        fillingThePlayingArea()

        val markers = gameArea.countMineMarkers()

        leftToFindMines = currentGameSetting.mines - markers

        viewModelProvider.counterMines.postValue(leftToFindMines)

        if (leftToFindMines == 0) {
            isWin = gameArea.checkTheFlagsSet()
            if (isWin) {
                endLevel()
            }
            if (!isWin) {

            }
        }
    }

    private fun setImageSource(imageSource: ImageView, img: Int) {
        imageSource.setImageResource(img)
    }

    private fun endLevel() {
        isGameRun = false

        job.cancel()

        buttonOpen.setOnClickListener(null)
        buttonMayBe.setOnClickListener(null)
        buttonMineIsHire.setOnClickListener(null)
        gameElementsHolder.setOnClickListener(null)

        timeOfEndGameValue.text = parseTime.parseLongToString(timeOfGame)

        Log.i("TAG", buttonPlayAgainIsWIN.toString())

        if (isWin) {
            winGameMessageLayout.visibility = View.VISIBLE
        } else if (isLoose) {
            looseGameMessageLayout.visibility = View.VISIBLE
        }
    }

    private fun fillingThePlayingArea() {
        gameElementsHolder.removeAllViews()
        val sizeDB = cellsDB.cellsDataBase.size

        for (id in 0 until sizeDB) {
            createGameElementById(id)
        }
    }

    private fun createGameElementById(id: Int) {
        val param = RelativeLayout.LayoutParams(sizeCell, sizeCell)
        val cell = cellsDB.cellsDataBase[id]
        param.topMargin = cell.yMargin
        param.leftMargin = cell.xMargin

        val imageSource = ImageView(this)

        when (cell.state) {

            CellState.CLOSE -> setImageSource(imageSource, R.drawable.shirt4)
            CellState.OPEN -> {
                if (cell.value == 0) {
                    setImageSource(imageSource, R.drawable.open)
                } else {
                    when (cell.value) {
                        1 -> setImageSource(imageSource, R.drawable.one)
                        2 -> setImageSource(imageSource, R.drawable.two)
                        3 -> setImageSource(imageSource, R.drawable.three)
                        4 -> setImageSource(imageSource, R.drawable.four)
                        5 -> setImageSource(imageSource, R.drawable.five)
                        6 -> setImageSource(imageSource, R.drawable.six)
                        7 -> setImageSource(imageSource, R.drawable.seven)
                        8 -> setImageSource(imageSource, R.drawable.eight)

                        9 -> {
                            setImageSource(imageSource, R.drawable.mineexploded)
                            isLoose = true
                            endLevel()
                        }
                    }
                }
            }
            CellState.MINE_MARKER -> setImageSource(imageSource, R.drawable.mineishire)
            CellState.MAYBE_MARKER -> setImageSource(imageSource, R.drawable.maybe)
        }
        drawGameElement(imageSource, param)
    }

    private fun drawGameElement(
        imageSource: ImageView,
        param: RelativeLayout.LayoutParams
    ) {
        gameElementsHolder.addView(imageSource, param)
    }

    private fun countCellSize(metrics: Metrics) {
        metrics.gameCellSize =
            (metrics.sizeDisplayX / currentGameSetting.sizeGameAreaArray).toDouble()
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

    private fun countCellsOnGameArea(sizeGameArea: Int): Int {
        return sizeGameArea * sizeGameArea
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
            text = pressButtonOpen(text, v)
        }
        if (v == buttonMayBe) {
            selectStateWhatDo = WhatDo.MAYbE
            text = toastTextMayBeMineIsHere
            setColorPrimary(v)
            setColorPrimaryVariant(buttonOpen, buttonMineIsHire)
        }
        if (v == buttonMineIsHire) {
            if (leftToFindMines > -1) {
                selectStateWhatDo = WhatDo.MINEiShIRE
                text = toastTextMineHere
                setColorPrimary(v)
                setColorPrimaryVariant(buttonOpen, buttonMayBe)
            }
        }
        if ((v == buttonSelectLevelIsWIN)
            or
            (v == buttonSelectLevelIsLOOSE)
        ) {
            startMainMenu()
        }
        if ((v == buttonPlayAgainIsWIN)
            or
            (v == buttonPlayAgainIsLOOSE)
        ) {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("LEVEL_GAME", gameLevel)
            startActivity(intent)
        }
        if (text.isNotEmpty()) {
            showMessage(text)
        }
    }

    private fun showMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun pressButtonOpen(text: String, v: View?): String {
        var text1 = text
        selectStateWhatDo = WhatDo.OPEN
        text1 = toastTextOpen
        v?.let { setColorPrimary(it) }
        setColorPrimaryVariant(buttonMayBe, buttonMineIsHire)
        return text1
    }

    private fun startMainMenu() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("IS_MENU_STARTING", true)
        startActivity(intent)
    }

    private fun setColorPrimaryVariant(button1: Button, button2: Button) {
        button1.setBackgroundColor(colorPrimaryVariant)
        button2.setBackgroundColor(colorPrimaryVariant)
    }

    private fun setColorPrimary(v: View) {
        v.setBackgroundColor(colorPrimary)
    }
}