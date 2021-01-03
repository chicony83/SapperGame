package com.chico.sapper

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.chico.sapper.fragments.MainMenuFragment
import com.chico.sapper.fragments.SplashScreenFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var isMenuStarting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //        drawView.setOnTouchListener(listenersGameArea.handleTouch);

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val splashScreenFragment = SplashScreenFragment()
        val mainMenuFragment = MainMenuFragment()

        isMenuStarting = intent.getBooleanExtra("IS_MENU_STARTING", false)

        if (isMenuStarting) {
            startMenuFragment(mainMenuFragment)
        }
        if (!isMenuStarting) {
            startActivity(splashScreenFragment)

            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                changeFragment(mainMenuFragment, splashScreenFragment)
            }
        }
    }

    private fun startActivity(splashScreenFragment: SplashScreenFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_holder, splashScreenFragment)
            .commit()
    }

    private fun startMenuFragment(mainMenuFragment: MainMenuFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_holder, mainMenuFragment)
            .commit()
    }

    private fun changeFragment(addFragment: Fragment, remFragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .remove(remFragment)
            .replace(R.id.fragment_holder, addFragment)
            .commit()
    }
}