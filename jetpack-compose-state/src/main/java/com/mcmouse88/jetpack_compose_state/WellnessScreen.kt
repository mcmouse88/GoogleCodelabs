package com.mcmouse88.jetpack_compose_state

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier,
    wellnessViewModel: WellnessViewModel = viewModel()
) {
    Column(modifier = modifier) {
        StatefulCounter()

        WellnessTaskList(
            list = wellnessViewModel.task,
            onCloseTask = { task ->
                wellnessViewModel.remove(task)
            },
            onCheckedTask = { task, checked ->
                wellnessViewModel.changeTaskChecked(task, checked)
            }
        )
    }
}