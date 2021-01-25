package com.chico.sapper.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.chico.sapper.GameActivity
import com.chico.sapper.R
import com.chico.sapper.interfaces.CallBackInterface

class MainMenuFragment : Fragment(), View.OnClickListener {

    private lateinit var veryEasyGameButton: Button
    private lateinit var easyGameButton: Button
    private lateinit var normalGameButton: Button
    private lateinit var hardGameButton: Button

    private lateinit var settingButton:ImageButton

    private var callBackInterface: CallBackInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_main_menu, container, false)

        veryEasyGameButton = rootView.findViewById<Button>(R.id.very_easy_game_button)
        easyGameButton = rootView.findViewById<Button>(R.id.easy_game_button)
        normalGameButton = rootView.findViewById<Button>(R.id.normal_game_button)
        hardGameButton = rootView.findViewById<Button>(R.id.hard_game_button)

//        settingButton = rootView.findViewById(R.id.settingButton)

        veryEasyGameButton.setOnClickListener(this)
        easyGameButton.setOnClickListener(this)
        normalGameButton.setOnClickListener(this)
        hardGameButton.setOnClickListener(this)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingButton = activity?.findViewById(R.id.settingButton)!!

        settingButton.setOnClickListener(this)

    }
    private fun translateToIndex(id: Int): Int {
        var index = -1
        when (id) {
            R.id.very_easy_game_button -> index = 0
            R.id.easy_game_button -> index = 1
            R.id.normal_game_button -> index = 2
            R.id.hard_game_button -> index = 3
        }
        return index
    }

    override fun onClick(v: View?) {
        if ((v == veryEasyGameButton)
            or (v == easyGameButton)
            or (v == normalGameButton)
            or (v == hardGameButton)
        ){
            val levelGame = translateToIndex(v!!.id)
            val intent = Intent(activity, GameActivity::class.java)

            intent.putExtra("LEVEL_GAME", levelGame)

            startActivity(intent)
        }
        if(v==settingButton){
            val i = 1
            callBackInterface?.callBackFunction(v = i)
        }


    }
    fun setCallBackInterface(callBackInterface: CallBackInterface){
        this.callBackInterface = callBackInterface
    }
}