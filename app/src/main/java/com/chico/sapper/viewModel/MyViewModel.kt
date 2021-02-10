package com.chico.sapper.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    var counterMines = MutableLiveData(0)
    var gameTime = MutableLiveData("")
    var winnerGameTime:MutableLiveData<Long> = MutableLiveData(0)
}
