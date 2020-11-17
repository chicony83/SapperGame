package com.chico.sapper

import android.util.Log
import android.view.MotionEvent
import android.widget.Toast

class ListenersGame {
    private var move = Move()
    fun handleTouch(m: MotionEvent, touch: Touch){
        touch.yTouch = m.y.toInt()
        touch.xTouch = m.x.toInt()

//        val yTouch = m.y
//        val xTouch = m.x


        Log.i("TAG", "xTouch = ${touch.xTouch}")
        Log.i("TAG", "yTouch = ${touch.yTouch}")

        move.getTouch(touch)
    }

}