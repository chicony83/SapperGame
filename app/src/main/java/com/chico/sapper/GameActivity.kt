package com.chico.sapper

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.AnimationDrawable
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
import com.chico.sapper.settings.CurrentGameSetting
import com.chico.sapper.settings.SettingLevels
import com.chico.sapper.utils.ParseTime
import com.chico.sapper.utils.launchIoNotReturn
import com.chico.sapper.viewModel.CounterViewModel
import kotlinx.coroutines.*
import kotlin.math.ceil
import kotlin.properties.Delegates


const val TAG = "TAG"

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private var settingLevels = SettingLevels()
    private val currentGameSetting = CurrentGameSetting()
    private val metrics = Metrics()
    private val parseTime = ParseTime()
    private lateinit var gameArea: GameArea

    private var sizeCell by Delegates.notNull<Int>()

    private var cellsDB = com.chico.sapper.dto.cellsDB
    private val touch = Touch()

    private lateinit var gameElementsHolder: RelativeLayout
    private lateinit var looseGameMessageLayout: LinearLayout
    private lateinit var winGameMessageLayout: LinearLayout

    private lateinit var buttonOpen: Button
    private lateinit var buttonMayBe: Button
    private lateinit var buttonMineIsHire: Button
    private lateinit var buttonSelectLevel: Button
    private lateinit var buttonPlayAgain: Button

    private lateinit var minesLeftValue: TextView
    private lateinit var timePassedValue: TextView
    private lateinit var timeOfEndGameValue: TextView

    private lateinit var toastTextOpen: String
    private lateinit var toastTextMayBeMineIsHere: String
    private lateinit var toastTextMineHere: String

    private lateinit var viewModelProvider: CounterViewModel

    private var selectStateWhatDo = 0

    private var leftToFindMines = 0

    private var isLoose: Boolean = false
    private var isWin: Boolean = false
    private var isShowEndGameMessage = false

    private var timeStart by Delegates.notNull<Long>()
    private var timeCurrent by Delegates.notNull<Long>()
    private var timeOfGame by Delegates.notNull<Long>()

    private var isGameRun = false

    private var colorPrimary by Delegates.notNull<Int>()
    private var colorPrimaryVariant by Delegates.notNull<Int>()
    private var colorOnPrimary by Delegates.notNull<Int>()

    private lateinit var colorPrimaryNight: Color
    private lateinit var colorPrimaryVariantNight: Color

    private lateinit var job: Job

    private var gameLevel by Delegates.notNull<Int>()

    private var mAnimationDrawable: AnimationDrawable? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setActivityFlags()
        setActivityOrientation()

        gameLevel = intent.getIntExtra("LEVEL_GAME", 1)

        preparationOfLevelData(gameLevel, currentGameSetting, settingLevels)

        viewModelProvider = ViewModelProvider(this).get(CounterViewModel::class.java)

        sizeDisplay(metrics)
        countCellSize(metrics)

        sizeCell = metrics.gameCellSize.toInt()

        observersCounterViewModel(viewModelProvider)

        gameArea = GameArea(currentGameSetting)
        gameArea.newCleanArea()
        gameArea.setMinesOnMinesArea(currentGameSetting)

        Log.wtf("TAG", "onCreateView: ")
        addCellsInDB(metrics, currentGameSetting)

        buttonOpen = findViewById(R.id.button_open)
        buttonMayBe = findViewById(R.id.button_mayBe)
        buttonMineIsHire = findViewById(R.id.button_mineIsHire)
        buttonSelectLevel = findViewById(R.id.button_selectLevel)
        buttonPlayAgain = findViewById(R.id.button_playAgain)

        minesLeftValue = findViewById(R.id.mines_left_value)
        timePassedValue = findViewById(R.id.time_passed_value)
        timeOfEndGameValue = findViewById(R.id.gameTime_textView)

        buttonOpen.setOnClickListener(this)
        buttonMayBe.setOnClickListener(this)
        buttonMineIsHire.setOnClickListener(this)
        buttonSelectLevel.setOnClickListener(this)
        buttonPlayAgain.setOnClickListener(this)

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
        timeStart = System.currentTimeMillis()

        launchIoNotReturn { gameTime() }
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
        colorOnPrimary = ContextCompat.getColor(this,R.color.gray_80)
    }
    private fun getNightColorsResource() {
        colorPrimary = ContextCompat.getColor(this, R.color.gray_140)
        colorPrimaryVariant = ContextCompat.getColor(this, R.color.gray_80)
        colorOnPrimary = ContextCompat.getColor(this,R.color.black)
    }

    private fun getStringResources() {
        toastTextOpen = getString(R.string.toastText_openCell)
        toastTextMayBeMineIsHere = getString(R.string.toastText_mayBeMineIsHere)
        toastTextMineHere = getString(R.string.toastText_mineHire)
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

                if ((timeCurrent - timeStart) > 1000) {

                    timeOfGame = timeCurrent - timeStart

                    viewModelProvider.gameTime.postValue(parseTime.parseLongToString(timeOfGame))
                }
            }
        }
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
        if (isShowEndGameMessage) {
            if (isLoose) {
                looseGameMessageLayout.visibility = View.VISIBLE
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

        val param = RelativeLayout.LayoutParams(sizeCell, sizeCell)

        var mineMarkerForMarkerArea = 0

        param.topMargin = yMargin.toInt()
        param.leftMargin = xMargin.toInt()

        when (selectStateWhatDo) {

            0 -> {
//                Log.i(TAG, " select state what do = $selectStateWhatDo")
                openCell(yTouchOnAreaInt, xTouchOnAreaInt, param)
                mineMarkerForMarkerArea = 0
            }
            1 -> {
                mayBeMineIsHere(yTouchOnAreaInt, xTouchOnAreaInt, param)
                mineMarkerForMarkerArea = 1
            }
            2 -> {
                mineIsHere(yTouchOnAreaInt, xTouchOnAreaInt, param)
                mineMarkerForMarkerArea = 2
            }
        }

        gameArea.setMarkerOnMarkerArea(yTouchOnAreaInt, xTouchOnAreaInt, mineMarkerForMarkerArea)

        val markers = gameArea.countMarkers()

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

    private fun mineIsHere(
        yTouchOnAreaInt: Int,
        xTouchOnAreaInt: Int,
        param: RelativeLayout.LayoutParams
    ) {
        if (leftToFindMines > 0) {
            drawMineIsHere(param)
        }
    }

    private fun mayBeMineIsHere(
        yTouchOnAreaInt: Int,
        xTouchOnAreaInt: Int,
        param: RelativeLayout.LayoutParams
    ) {
        drawMayBeMineIsHere(param)
    }

    private fun openCell(
        yTouchOnAreaInt: Int,
        xTouchOnAreaInt: Int,
        param: RelativeLayout.LayoutParams
    ) {

        if (
            (!gameArea.isMineMarkerHire(yTouchOnAreaInt, xTouchOnAreaInt))
//            or
//            (gameArea.isMayByMineIsHire(yTouchOnAreaInt, xTouchOnAreaInt))
        ) {
            Log.i(TAG, "open cell")
            drawOpenGameCell(yTouchOnAreaInt, xTouchOnAreaInt, param)
        }
    }

    private fun drawMineIsHere(
        param: RelativeLayout.LayoutParams
    ) {
        val imageSource = ImageView(this)
        imageSource.setImageResource(R.drawable.mineishire)
        gameElementsHolder.addView(imageSource, param)
    }

    private fun drawMayBeMineIsHere(
        param: RelativeLayout.LayoutParams
    ) {
        val imageSource = ImageView(this)
        imageSource.setImageResource(R.drawable.maybe)
        gameElementsHolder.addView(imageSource, param)
    }

    private fun drawOpenGameCell(
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
                imageSource.setImageResource(R.drawable.mineexploded)

//                mAnimationDrawable = imageSource.background as AnimationDrawable
//                mAnimationDrawable!!.start()

//                mAnimationDrawable =
                isLoose = true

                endLevel()
            }
            gameElementsHolder.addView(imageSource, param)
        }
    }

    private fun endLevel() {

        isGameRun = false

        job.cancel()

        buttonOpen.setOnClickListener(null)
        buttonMayBe.setOnClickListener(null)
        buttonMineIsHire.setOnClickListener(null)
        gameElementsHolder.setOnClickListener(null)

        timeOfEndGameValue.text = parseTime.parseLongToString(timeOfGame)

        if (isWin) {
            winGameMessageLayout.visibility = View.VISIBLE
        }
        if (isLoose) {

            CoroutineScope(Dispatchers.IO).launch {
                delay(500)
                isShowEndGameMessage = true
            }

        }
    }

    private fun fillingThePlayingArea() {
        val sizeDB = cellsDB.cellsDataBase.size

        for (id in 0 until sizeDB) {
            createGameElementById(id)
        }
    }

    private fun createGameElementById(id: Int) {
        val param = RelativeLayout.LayoutParams(sizeCell, sizeCell)

        param.topMargin = cellsDB.cellsDataBase[id].yMargin
        param.leftMargin = cellsDB.cellsDataBase[id].xMargin

        val imageSource = ImageView(this)
        imageSource.setImageResource(R.drawable.shirt2)

        gameElementsHolder.addView(imageSource, param)
    }

    private fun countCellSize(metrics: Metrics) {
        metrics.gameCellSize =
            (metrics.sizeDisplayX / currentGameSetting.sizeArrayOfGameArea).toDouble()
    }

    private fun addCellsInDB(
        metrics: Metrics,
        currentGameSetting: CurrentGameSetting
    ) {
        var widthArraySizeOfGameArray = currentGameSetting.sizeArrayOfGameArea - 1
        var heightArraySizeOfGameArray = currentGameSetting.sizeArrayOfGameArea - 1


        var idX: String = ""
        var idY: String = ""
        var id: String

        for (y in 0..heightArraySizeOfGameArray) {

            for (x in 0..widthArraySizeOfGameArray) {
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
            selectStateWhatDo = 0
            text = toastTextOpen
            setColorPrimary(v)
            setColorPrimaryVariant(buttonMayBe, buttonMineIsHire)
        }
        if (v == buttonMayBe) {
            selectStateWhatDo = 1
            text = toastTextMayBeMineIsHere
            setColorPrimary(v)
            setColorPrimaryVariant(buttonOpen, buttonMineIsHire)
        }
        if (v == buttonMineIsHire) {
            selectStateWhatDo = 2
            text = toastTextMineHere
            setColorPrimary(v)
            setColorPrimaryVariant(buttonOpen, buttonMayBe)
        }
        if (v == buttonSelectLevel) {
            startMainMenu()

        }
        if (v == buttonPlayAgain) {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("LEVEL_GAME", gameLevel)
            startActivity(intent)
        }
        if (text.isNotEmpty()) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
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