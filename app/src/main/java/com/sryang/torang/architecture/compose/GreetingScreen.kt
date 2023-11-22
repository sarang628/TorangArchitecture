package com.sryang.torang.architecture.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.sryang.torang.architecture.viewmodel.GreetingViewModel

/**
 * Hello world!를 한 자씩 보여주기
 */
@Composable
fun GreetingScreen(greetingViewModel: GreetingViewModel) {
    val greeting by greetingViewModel.greetingState.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = greeting,
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}