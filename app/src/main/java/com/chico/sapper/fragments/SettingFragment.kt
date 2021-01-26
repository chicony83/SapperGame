package com.chico.sapper.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import com.chico.sapper.R
import com.chico.sapper.dto.SharedPreferencesConst
import com.chico.sapper.dto.enums.FragmentsButtonNames
import com.chico.sapper.dto.enums.Themes
import com.chico.sapper.interfaces.CallBackInterface

class SettingFragment : Fragment(), View.OnClickListener {

    private lateinit var classicThemeButton: RadioButton
    private lateinit var forestThemeButton: RadioButton
    private lateinit var vanillaThemeButton: RadioButton

    private lateinit var exitSettingsButton: Button
    private lateinit var saveSettingsButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private var callBackInterface: CallBackInterface? = null

    private val spName = SharedPreferencesConst().SP_NAME
    private val spTheme = SharedPreferencesConst().THEME

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

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classicThemeButton = view.findViewById(R.id.classicTheme_radioButton)
        forestThemeButton = view.findViewById(R.id.forestTheme_radioButton)
        vanillaThemeButton = view.findViewById(R.id.vanillaTheme_radioButton)

        classicThemeButton.setOnClickListener(this)
        forestThemeButton.setOnClickListener(this)
        vanillaThemeButton.setOnClickListener(this)

        sharedPreferences = this.activity?.getSharedPreferences(spName, Context.MODE_PRIVATE)!!

        when (sharedPreferences?.getString(spTheme, Themes.CLASSIC.toString())) {
            Themes.CLASSIC.toString() -> classicThemeButton.isChecked = true
            Themes.FOREST.toString() -> forestThemeButton.isChecked = true
            Themes.VANILLA.toString() ->vanillaThemeButton.isChecked = true
        }
    }

    override fun onClick(v: View?) {

        when (v) {
            exitSettingsButton -> {
                val text = "settings don't saved"
                callBackInterface?.callBackFunction(FragmentsButtonNames.EXITSETTING)
                showMessage(text)
            }

            saveSettingsButton -> {
                if (classicThemeButton.isChecked){
                    editorPutString(Themes.CLASSIC.toString())
                }
                if (forestThemeButton.isChecked){
                    editorPutString(Themes.FOREST.toString())
                }
                if (vanillaThemeButton.isChecked){
                    editorPutString(Themes.VANILLA.toString())
                }
                callBackInterface?.callBackFunction(FragmentsButtonNames.EXITSETTING)
            }
        }
    }

    private fun editorPutString( theme: String) {
        val editor = sharedPreferences.edit()
        editor.putString(spTheme, theme)
        editor.apply()
    }

    private fun showMessage(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    fun setCallBackInterface(callBackInterface: CallBackInterface) {
        this.callBackInterface = callBackInterface
    }

}