package com.chico.sapper.fragments.menu

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.chico.sapper.GameActivity
import com.chico.sapper.R
import com.chico.sapper.dto.SharedPreferencesConst
import com.chico.sapper.dto.enums.FragmentsButtonNames
import com.chico.sapper.interfaces.CallBackInterface
import kotlin.properties.Delegates

class MainMenuFragment : Fragment(), View.OnClickListener {

    private lateinit var veryEasyGameButton: Button
    private lateinit var easyGameButton: Button
    private lateinit var normalGameButton: Button
    private lateinit var hardGameButton: Button

    private lateinit var settingButton: ImageButton
    private lateinit var highScoreButton:ImageButton

    private var callBackInterface: CallBackInterface? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spEditor:SharedPreferences.Editor

    private val spName = SharedPreferencesConst().SP_NAME
    private val spIsFirstLaunch = SharedPreferencesConst().IS_FIRST_LAUNCH


//    private val spPlayerName = SharedPreferencesConst().PLAYER_NAME
//    private var playerName = ""

    private var isFirstLaunch by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_main_menu, container, false)

        veryEasyGameButton = rootView.findViewById<Button>(R.id.veryEasy_game_button)
        easyGameButton = rootView.findViewById<Button>(R.id.easy_game_button)
        normalGameButton = rootView.findViewById<Button>(R.id.normal_game_button)
        hardGameButton = rootView.findViewById<Button>(R.id.hard_game_button)

        veryEasyGameButton.setOnClickListener(this)
        easyGameButton.setOnClickListener(this)
        normalGameButton.setOnClickListener(this)
        hardGameButton.setOnClickListener(this)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingButton = view.findViewById(R.id.setting_button)
        highScoreButton = view.findViewById(R.id.highScore_button)

        settingButton.setOnClickListener(this)
        highScoreButton.setOnClickListener(this)

        getSharedPreferences()
        getSharedPreferencesData()
        showDialogFirstLaunch()

        setSharedPreferenceData()
//        if ()
    }

    private fun setSharedPreferenceData() {
        spEditor.putBoolean(spIsFirstLaunch.toString(),isFirstLaunch)
        spEditor.commit()
    }

    private fun showDialogFirstLaunch() {
        if(isFirstLaunch){
            val firstLaunchDialogFragment = FirstLaunchDialogFragment(sharedPreferences)
            val manager = childFragmentManager
            firstLaunchDialogFragment.show(manager,"first launch")
            isFirstLaunch = false

        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun getSharedPreferences() {
        sharedPreferences = activity?.getSharedPreferences(spName, Context.MODE_PRIVATE)!!
        spEditor = sharedPreferences.edit()
    }

    private fun getSharedPreferencesData() {
        isFirstLaunch = sharedPreferences.getBoolean(spIsFirstLaunch.toString(), true)
//        playerName = sharedPreferences.getString(spPlayerName.toString()," ")
    }

    override fun onClick(v: View?) {
        when (v) {
            veryEasyGameButton -> startGameActivity(v)
            easyGameButton -> startGameActivity(v)
            normalGameButton -> startGameActivity(v)
            hardGameButton -> startGameActivity(v)

            settingButton -> callBackInterface?.callBackFunction(FragmentsButtonNames.SETTING)
            highScoreButton ->callBackInterface?.callBackFunction(FragmentsButtonNames.HIGH_SCORE_MENU)
        }
    }

    private fun startGameActivity(v: View?) {
        val levelGame = translateToIndex(v!!.id)
        val intent = Intent(activity, GameActivity::class.java)

        intent.putExtra("LEVEL_GAME", levelGame)

        startActivity(intent)
    }

    private fun translateToIndex(id: Int): Int {
        var index = -1
        when (id) {
            R.id.veryEasy_game_button -> index = 0
            R.id.easy_game_button -> index = 1
            R.id.normal_game_button -> index = 2
            R.id.hard_game_button -> index = 3
        }
        return index
    }

    fun setCallBackInterface(callBackInterface: CallBackInterface) {
        this.callBackInterface = callBackInterface
    }
}