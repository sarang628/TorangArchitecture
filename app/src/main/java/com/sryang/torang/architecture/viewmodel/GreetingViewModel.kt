package com.sryang.torang.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GreetingViewModel : ViewModel() {
    private val helloWorld = "hello world!"
    private var count = 0

    private val _greetingState = MutableStateFlow<String>("")
    val greetingState = _greetingState.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                _greetingState.update {
                    delay(100)
                    count++
                    helloWorld.substring(0, count % (helloWorld.length + 1))
                }
            }
        }
    }
}