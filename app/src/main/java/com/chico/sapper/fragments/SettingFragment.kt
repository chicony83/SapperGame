package com.chico.sapper.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.chico.sapper.R
import com.chico.sapper.dto.enums.FragmentsButtonNames
import com.chico.sapper.interfaces.CallBackInterface

class SettingFragment : Fragment(), View.OnClickListener {

    private lateinit var exitSettingsButton: Button
    private lateinit var saveSettingsButton: Button

    private var callBackInterface: CallBackInterface? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_setting, container, false)

        exitSettingsButton = rootView.findViewById(R.id.exit_settings_button)
        saveSettingsButton = rootView.findViewById(R.id.save_settings_button)

        exitSettingsButton.setOnClickListener(this)
        saveSettingsButton.setOnClickListener(this)

        return rootView
    }

    override fun onClick(v: View?) {
        when (v) {
            exitSettingsButton -> {
                val text = "settings don't saved"
                callBackInterface?.callBackFunction(FragmentsButtonNames.EXITSETTING)
                showMessage(text)
            }

            saveSettingsButton -> {
                val text = "save don't work"
                callBackInterface?.callBackFunction(FragmentsButtonNames.EXITSETTING)
                showMessage(text)

            }
        }


    }

    private fun showMessage(text: String) {
        Toast.makeText(activity,text,Toast.LENGTH_SHORT).show()
    }

    fun setCallBackInterface(callBackInterface: CallBackInterface) {
        this.callBackInterface = callBackInterface
    }

}