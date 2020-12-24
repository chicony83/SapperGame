package com.chico.sapper.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.chico.sapper.GameActivity
import com.chico.sapper.R

class MainMenuFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_main_menu, container, false)

        val easyGameButton = rootView.findViewById<Button>(R.id.easy_game_button)
        val normalGameButton = rootView.findViewById<Button>(R.id.normal_game_button)
        val hardGameButton = rootView.findViewById<Button>(R.id.hard_game_button)

        easyGameButton.setOnClickListener(this)
        normalGameButton.setOnClickListener(this)
        hardGameButton.setOnClickListener(this)

        return rootView

    }

    fun translateToIndex(id: Int): Int {
        var index = -1
        when (id) {
            R.id.easy_game_button -> index = 1
            R.id.normal_game_button -> index = 2
            R.id.hard_game_button -> index = 3
        }
        return index
    }

    override fun onClick(v: View?) {
        val levelGame = translateToIndex(v!!.id)
        val intent = Intent(activity, GameActivity::class.java)

        intent.putExtra("LEVEL_GAME",levelGame)

        startActivity(intent)


    }
}