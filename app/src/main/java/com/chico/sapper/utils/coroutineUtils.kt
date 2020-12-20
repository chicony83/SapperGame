package com.chico.sapper.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun launchIoNotReturn(task:suspend ()->Unit){
    CoroutineScope(Dispatchers.IO).launch {

        task()
    }
}
fun launchUINotReturn(task: suspend () -> Unit){
    CoroutineScope(Dispatchers.Main).launch {

        task()
    }
}