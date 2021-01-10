package com.chico.sapper.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {
    var counterMines = MutableLiveData(0)
    var gameTime = MutableLiveData("")
}
