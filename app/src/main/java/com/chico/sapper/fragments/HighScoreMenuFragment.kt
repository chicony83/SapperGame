package com.chico.sapper.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.chico.sapper.R
import com.chico.sapper.dto.enums.FragmentsButtonNames
import com.chico.sapper.interfaces.CallBackInterface

class HighScoreMenuFragment : Fragment(), View.OnClickListener {

    private var callBackInterface: CallBackInterface? = null

    private lateinit var veryEasyHighScoreButton: Button
    private lateinit var easyHighScoreButton: Button
    private lateinit var normalHighScoreButton: Button
    private lateinit var hardHighScoreButton: Button

    private lateinit var highScoreToMainMenuButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_high_score_menu, container, false)

        highScoreToMainMenuButton = rootView.findViewById(R.id.highScore_to_mainMenu_button)
        veryEasyHighScoreButton = rootView.findViewById(R.id.veryEasy_highScore_button)
        easyHighScoreButton = rootView.findViewById(R.id.easy_highScore_button)
        normalHighScoreButton = rootView.findViewById(R.id.normal_highScore_button)
        hardHighScoreButton = rootView.findViewById(R.id.hard_highScore_button)

        highScoreToMainMenuButton.setOnClickListener(this)
        veryEasyHighScoreButton.setOnClickListener(this)
        easyHighScoreButton.setOnClickListener(this)
        normalHighScoreButton.setOnClickListener(this)
        hardHighScoreButton.setOnClickListener(this)

        return rootView
    }

    fun setCallBackInterface(callBackInterface: CallBackInterface) {
        this.callBackInterface = callBackInterface
    }

    override fun onClick(v: View?) {
        when (v) {
            highScoreToMainMenuButton -> {
                callBack(FragmentsButtonNames.TO_MAIN_MENU)
            }
            veryEasyHighScoreButton -> {
                callBack(FragmentsButtonNames.HIGH_SCORE_VERY_EASY)
            }
            easyHighScoreButton -> {
                callBack(FragmentsButtonNames.HIGH_SCORE_EASY)
            }
            normalHighScoreButton -> {
                callBack(FragmentsButtonNames.HIGH_SCORE_NORMAL)
            }
            hardHighScoreButton -> {
                callBack(FragmentsButtonNames.HIGH_SCORE_HARD)
            }
        }
    }

    private fun callBack(buttonName: FragmentsButtonNames) {
        callBackInterface?.callBackFunction(buttonName)
    }
}
