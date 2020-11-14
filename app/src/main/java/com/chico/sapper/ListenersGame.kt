package com.chico.sapper

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View

class ListenersGame{

     fun handleTouch(m: MotionEvent) {
        val xTouch = m.x
        val yTouch = m.y

       Log.i("TAG","xTouch = $xTouch")
       Log.i("TAG","yTouch = $yTouch")

    }

}