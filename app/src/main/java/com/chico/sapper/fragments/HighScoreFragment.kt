package com.chico.sapper.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.chico.sapper.R
import com.chico.sapper.dto.enums.BundleStringsNames
import com.chico.sapper.dto.enums.FragmentsButtonNames
import com.chico.sapper.interfaces.CallBackInterface
import com.chico.sapper.viewModel.MyViewModel

class HighScoreFragment : Fragment(), View.OnClickListener {
    private var callBackInterface: CallBackInterface? = null

    private lateinit var viewModelProvider: MyViewModel

    private lateinit var highScoreToHighScoreMenuButton:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_high_score, container, false)

        highScoreToHighScoreMenuButton = rootView.findViewById(R.id.highScore_to_highScoreMenu_button)

        highScoreToHighScoreMenuButton.setOnClickListener(this)

        val bundle = arguments
        val result = bundle?.getString(BundleStringsNames.HIGH_SCORE_STATE.toString())
//        Log.i("TAG", "result = $result")

        bundle?.clear()


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    fun setCallBackInterface(callBackInterface: CallBackInterface) {
        this.callBackInterface = callBackInterface
    }

    override fun onClick(v: View?) {
        when(v){
            highScoreToHighScoreMenuButton -> {
                callBack(FragmentsButtonNames.HIGH_SCORE_MENU)

            }
        }
    }
    private fun callBack(buttonName: FragmentsButtonNames) {
        callBackInterface?.callBackFunction(buttonName)
    }

}