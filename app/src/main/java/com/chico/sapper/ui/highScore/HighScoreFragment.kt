package com.chico.sapper.ui.highScore

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chico.sapper.R
import com.chico.sapper.recyclerView.WinnerAdapter
import com.chico.sapper.database.dao.WinnerGameDao
import com.chico.sapper.database.db
import com.chico.sapper.database.entity.Winner
import com.chico.sapper.dto.enums.BundleStringsNames
import com.chico.sapper.dto.enums.FragmentsButtonNames
import com.chico.sapper.dto.enums.HighScoreState
import com.chico.sapper.interfaces.CallBackInterface
import com.chico.sapper.utils.launchForResult
import com.chico.sapper.utils.launchIo
import com.chico.sapper.utils.launchUI
import kotlin.properties.Delegates

class HighScoreFragment : Fragment(), View.OnClickListener {
    private var callBackInterface: CallBackInterface? = null

    private lateinit var bottomText: TextView

    private lateinit var highScoreToHighScoreMenuButton: Button
    private lateinit var highScoreState: String
    private lateinit var result: List<Winner>
    private var level by Delegates.notNull<Int>()

    private lateinit var recyclerViewHolder: RecyclerView

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_high_score, container, false)

        highScoreToHighScoreMenuButton =
            rootView.findViewById(R.id.highScore_to_highScoreMenu_button)
        bottomText = rootView.findViewById(R.id.bottomText)

        recyclerViewHolder = rootView.findViewById(R.id.recyclerView_holder)

        highScoreToHighScoreMenuButton.setOnClickListener(this)

        val bundle = arguments
        highScoreState =
            bundle?.getString(BundleStringsNames.HIGH_SCORE_STATE.toString()).toString()
        bundle?.clear()

        getSettingHighScoreFragment()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db: WinnerGameDao = db.getDB(requireActivity()).winnerGameDao()

        launchIo {
            launchForResult {
                result = db.getWinners(level)

                launchUI {
                    recyclerViewHolder.adapter = WinnerAdapter(winnerList = result)
                    recyclerViewHolder.layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }


    private fun getSettingHighScoreFragment() {
        when (highScoreState) {
            HighScoreState.VERY_EASY.toString() -> setSettings(0)
            HighScoreState.EASY.toString() -> setSettings(1)
            HighScoreState.NORMAL.toString() -> setSettings(2)
            HighScoreState.HARD.toString() -> setSettings(3)
        }
    }

    private fun setSettings(l: Int) {
        var text = ""
        when (l) {
            0->text = getString(R.string.buttonVeryEasy_text)
            1 -> text = getString(R.string.buttonEasy_text)
            2 -> text = getString(R.string.buttonNormal_text)
            3 -> text = getString(R.string.buttonHard_text)
        }
        level = l
        setTextOnTextView(buttonText = text)
    }


    private fun setTextOnTextView(buttonText: String) {
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