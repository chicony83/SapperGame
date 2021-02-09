package com.chico.sapper.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chico.sapper.R
import com.chico.sapper.dto.SharedPreferencesConst
import com.chico.sapper.dto.enums.FragmentsButtonNames
import com.chico.sapper.dto.enums.Themes
import com.chico.sapper.interfaces.CallBackInterface

class SettingFragment : Fragment(), View.OnClickListener {

    private lateinit var classicThemeButton: RadioButton
    private lateinit var forestThemeButton: RadioButton
    private lateinit var vanillaThemeButton: RadioButton

    private lateinit var classicImageButton: ImageButton
    private lateinit var forestImageButton: ImageButton
    private lateinit var vanillaImageButton: ImageButton

    private lateinit var exitSettingsButton: Button
    private lateinit var saveSettingsButton: Button

    private var playerName_editText: EditText? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var callBackInterface: CallBackInterface? = null
    private val SP_NAME = SharedPreferencesConst().SP_NAME
    private val SP_THEME = SharedPreferencesConst().THEME

    private val SP_PLAYER_NAME = SharedPreferencesConst().PLAYER_NAME
    private lateinit var playerName: String

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

    @SuppressLint("UseRequireInsteadOfGet", "CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classicThemeButton = view.findViewById(R.id.classicTheme_radioButton)
        forestThemeButton = view.findViewById(R.id.forestTheme_radioButton)
        vanillaThemeButton = view.findViewById(R.id.vanillaTheme_radioButton)

        classicImageButton = view.findViewById(R.id.classicTheme_imageButton)
        forestImageButton = view.findViewById(R.id.forestTheme_imageButton)
        vanillaImageButton = view.findViewById(R.id.vanillaTheme_imageButton)

        playerName_editText = view.findViewById(R.id.playerName_editText)

        classicThemeButton.setOnClickListener(this)
        forestThemeButton.setOnClickListener(this)
        vanillaThemeButton.setOnClickListener(this)

        classicImageButton.setOnClickListener(this)
        forestImageButton.setOnClickListener(this)
        vanillaImageButton.setOnClickListener(this)

        sharedPreferences = this.activity?.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()

        setSPThemeTry()
        getSPPlayerName()
    }

    private fun getSPPlayerName() {
        playerName = sharedPreferences.getString(SP_PLAYER_NAME, "").orEmpty().toString()
        playerName_editText?.setText(playerName)
    }

    private fun setSPThemeTry() {
        when (sharedPreferences.getString(SP_THEME, Themes.CLASSIC.toString())) {
            Themes.CLASSIC.toString() -> classicThemeButton.isChecked = true
            Themes.FOREST.toString() -> forestThemeButton.isChecked = true
            Themes.VANILLA.toString() -> vanillaThemeButton.isChecked = true
        }
    }

    override fun onClick(v: View?) {

        when (v) {
            exitSettingsButton -> {
                callBackInterface?.callBackFunction(FragmentsButtonNames.TO_MAIN_MENU)
                val text = "settings don't saved"
                showMessage(text)
            }

            saveSettingsButton -> {
                if (classicThemeButton.isChecked) {
                    editorPutString(SP_THEME, Themes.CLASSIC.toString())
                }
                if (forestThemeButton.isChecked) {
                    editorPutString(SP_THEME, Themes.FOREST.toString())
                }
                if (vanillaThemeButton.isChecked) {
                    editorPutString(SP_THEME, Themes.VANILLA.toString())
                }
                editorPutString(SP_PLAYER_NAME,playerName_editText?.text.toString())

                callBackInterface?.callBackFunction(FragmentsButtonNames.TO_MAIN_MENU)
            }

            classicImageButton -> {
                classicThemeButton.isChecked = true
            }
            forestImageButton -> {
                forestThemeButton.isChecked = true
            }
            vanillaImageButton -> {
                vanillaThemeButton.isChecked = true
            }
        }
    }

    private fun editorPutString(SP_CONST: String, value: String) {
        editor.putString(SP_CONST, value)
        editor.apply()
    }

    private fun showMessage(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    fun setCallBackInterface(callBackInterface: CallBackInterface) {
        this.callBackInterface = callBackInterface
    }
}