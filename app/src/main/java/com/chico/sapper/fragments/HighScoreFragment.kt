package com.chico.sapper.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.chico.sapper.R
import com.chico.sapper.database.dao.WinnerGameDao
import com.chico.sapper.database.db
import com.chico.sapper.dto.enums.BundleStringsNames
import com.chico.sapper.dto.enums.FragmentsButtonNames
import com.chico.sapper.dto.enums.HighScoreState
import com.chico.sapper.interfaces.CallBackInterface

class HighScoreFragment : Fragment(), View.OnClickListener {
    private var callBackInterface: CallBackInterface? = null

    private lateinit var bottomText: TextView
    private lateinit var highScoreToHighScoreMenuButton: Button
    private lateinit var result: String

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_high_score, container, false)

        highScoreToHighScoreMenuButton =
            rootView.findViewById(R.id.highScore_to_highScoreMenu_button)
        bottomText = rootView.findViewById(R.id.bottomText)

        highScoreToHighScoreMenuButton.setOnClickListener(this)

        val bundle = arguments
        result = bundle?.getString(BundleStringsNames.HIGH_SCORE_STATE.toString()).toString()
//        Log.i("TAG", "result = $result")
        bundle?.clear()

        val db:WinnerGameDao = db.getDB(requireActivity()).winnerGameDao()

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (result) {
            HighScoreState.VERY_EASY.toString() -> setTextOnTextView(getString(R.string.buttonVeryEasy_text))
            HighScoreState.EASY.toString() -> setTextOnTextView(getString(R.string.buttonEasy_text))
            HighScoreState.NORMAL.toString() -> setTextOnTextView(getString(R.string.buttonNormal_text))
            HighScoreState.HARD.toString() -> setTextOnTextView(getString(R.string.buttonHard_text))
        }
    }

//    @SuppressLint("ResourceType")
    private fun setTextOnTextView(buttonText: String) {
//        Log.i("TAG", "text = $buttonText")
        bottomText.text = buttonText
    }


    fun setCallBackInterface(callBackInterface: CallBackInterface) {
        this.callBackInterface = callBackInterface
    }

    override fun onClick(v: View?) {
        when (v) {
            highScoreToHighScoreMenuButton -> {
                callBack(FragmentsButtonNames.HIGH_SCORE_MENU)
            }
        }
    }

    private fun callBack(buttonName: FragmentsButtonNames) {
        callBackInterface?.callBackFunction(buttonName)
    }

}