package com.chico.sapper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val splashScreenFragment = SplashScreenFragment()
        val mainMenuFragment = MainMenuFragment()

        CoroutineScope(Dispatchers.IO).launch {
            delay(5000)
            launchFragment(mainMenuFragment,splashScreenFragment)
        }

    }

    private fun logIsLaunch(isLaunch: Boolean) {
        Log.i("TAG", "isLaunch = $isLaunch")
    }


    private fun launchFragment(addFragment: Fragment,remFragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
//            .remove(remFragment)
            .hide(remFragment)
            .replace(R.id.fragment_holder, addFragment)
            .commit()
    }
}