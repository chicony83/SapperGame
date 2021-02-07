package com.chico.sapper.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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

    private var callBackInterface: CallBackInterface? = null

    private val spName = SharedPreferencesConst().SP_NAME
    private val spTheme = SharedPreferencesConst().THEME
    private val spPlayerName = SharedPreferencesConst().PLAYER_NAME

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

    @SuppressLint("UseRequireInsteadOfGet")
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

        sharedPreferences = this.activity?.getSharedPreferences(spName, Context.MODE_PRIVATE)!!

        setSPThemeTry()
        getSPPlayerName()
    }

    private fun getSPPlayerName() {

        playerName_editText?.setText(sharedPreferences.getString(spPlayerName, ""))
//        Log.i("TAG", "text = $text")
//        playerName = sharedPreferences.getString(spPlayerName,"").toString()
//        val newText= playerName
//        Log.i("TAG", "player name = $playerName")
//        if (this.playerName.isNotEmpty()){
//            val text = playerName
//            playerName_editText?.hint = text
//            Log.i("TAG","text = $text")
//        }
    }

    private fun setSPThemeTry() {
        when (sharedPreferences.getString(spTheme, Themes.CLASSIC.toString())) {
            Themes.CLASSIC.toString() -> classicThemeButton.isChecked = true
            Themes.FOREST.toString() -> forestThemeButton.isChecked = true
            Themes.VANILLA.toString() -> vanillaThemeButton.isChecked = true
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
                if (classicThemeButton.isChecked) {
                    editorPutString(Themes.CLASSIC.toString())
                }
                if (forestThemeButton.isChecked) {
                    editorPutString(Themes.FOREST.toString())
                }
                if (vanillaThemeButton.isChecked) {
                    editorPutString(Themes.VANILLA.toString())
                }
                callBackInterface?.callBackFunction(FragmentsButtonNames.EXITSETTING)
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

    private fun editorPutString(theme: String) {
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