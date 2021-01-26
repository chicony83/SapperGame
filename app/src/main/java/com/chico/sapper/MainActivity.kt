package com.chico.sapper

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.chico.sapper.dto.enums.FragmentsButtonNames
import com.chico.sapper.fragments.MainMenuFragment
import com.chico.sapper.fragments.SettingFragment
import com.chico.sapper.fragments.SplashScreenFragment
import com.chico.sapper.interfaces.CallBackInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(),CallBackInterface {

    private var isMenuStarting = false
    private var isDoubleBackOnPressedOnce = false

    private lateinit var settingFragment: SettingFragment
    private lateinit var mainMenuFragment:MainMenuFragment
    private lateinit var splashScreenFragment: SplashScreenFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        splashScreenFragment = SplashScreenFragment()
        mainMenuFragment = MainMenuFragment()
        settingFragment = SettingFragment()

        mainMenuFragment.setCallBackInterface(this)
        settingFragment.setCallBackInterface(this)
        isMenuStarting = intent.getBooleanExtra("IS_MENU_STARTING", true)

        if (isMenuStarting) {
            startFragment(mainMenuFragment)
        }
        if (!isMenuStarting) {
            startActivity(splashScreenFragment)

            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                changeFragment(mainMenuFragment, splashScreenFragment)
            }
        }
    }

    override fun onBackPressed() {
        if (isDoubleBackOnPressedOnce){
            super.onBackPressed()
            moveTaskToBack(true);
            exitProcess(-1)
            return
        }
        this.isDoubleBackOnPressedOnce = true
        Toast.makeText(this,"press back again to exit",Toast.LENGTH_SHORT).show()
        Handler().postDelayed({isDoubleBackOnPressedOnce = false}, 2000)
    }

    private fun startActivity(splashScreenFragment: SplashScreenFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_holder, splashScreenFragment)
            .commit()
    }

    private fun startFragment(startFragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_holder, startFragment)
            .commit()
    }

    private fun changeFragment(addFragment: Fragment, remFragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .remove(remFragment)
            .replace(R.id.fragment_holder, addFragment)
            .commit()
    }

    override fun callBackFunction(i: FragmentsButtonNames) {
        when(i){
            FragmentsButtonNames.SETTING->startFragment(settingFragment)
            FragmentsButtonNames.EXITSETTING->startFragment(mainMenuFragment)
        }
    }


}