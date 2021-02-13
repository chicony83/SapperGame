package com.chico.sapper.fragments.menu

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.chico.sapper.dto.SharedPreferencesConst


class FirstLaunchDialogFragment(sP: SharedPreferences) : DialogFragment() {

    //    private val spName = SharedPreferencesConst().SP_NAME
//    private val sharedPreferences = activity?.applicationContext?.getSharedPreferences(spName,Context.MODE_PRIVATE)
//    private val sharedPreferences = activity?.getSharedPreferences(spName,Context.MODE_PRIVATE)
    private val sharedPreferences = sP

    private val spPlayerName = SharedPreferencesConst().PLAYER_NAME
    private val spEditor = this.sharedPreferences.edit()

    private var text = ""
    private lateinit var input: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            input = EditText(context)
            input.apply {
                maxLines = 1
                hint = "Your name"
                isSingleLine = true
            }

            builder.setTitle("it's first Launch")
            builder.setView(input)
                .setMessage("enter You name")

                .setPositiveButton(
                    "save"
                ) { _, _ ->
                    saveText()
                }

                .setNegativeButton(
                    "later"
                ) { dialog, id ->
                    dialog.cancel()
                }

            builder.create()
        } ?: throw IllegalAccessException("activity can not be null")
    }

    private fun saveText() {
        if (input.text.isNotEmpty()) {

//            val inputText = input.text.toString()
            spEditor?.putString(spPlayerName, input.text.toString())
            spEditor?.apply()

//            val newText = sharedPreferences.getString(spPlayerName, "")
//            Toast.makeText(context, newText.toString(), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "имя не заполнено", Toast.LENGTH_LONG).show()
        }

    }
}