package com.mcmouse88.advanced_compose_state.home

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mcmouse88.advanced_compose_state.R
import com.mcmouse88.advanced_compose_state.base.CraneEditableUserInput
import com.mcmouse88.advanced_compose_state.base.CraneUserInput
import com.mcmouse88.advanced_compose_state.base.rememberEditableUserInputState
import com.mcmouse88.advanced_compose_state.ui.theme.AdvancedStateComposeTheme
import kotlinx.coroutines.flow.filter

enum class PeopleUserInputAnimationState { Valid, Invalid }

class PeopleUserInputState {
    var people by mutableIntStateOf(1)
        private set

    val animationState: MutableTransitionState<PeopleUserInputAnimationState> = MutableTransitionState(PeopleUserInputAnimationState.Valid)

    fun addPerson() {
        people = (people % (MAX_PEOPLE + 1) + 1)
        updateAnimationState()
    }

    private fun updateAnimationState() {
        val newState = if (people > MAX_PEOPLE) PeopleUserInputAnimationState.Invalid
        else PeopleUserInputAnimationState.Valid

        if (animationState.currentState != newState) animationState.targetState = newState
    }
}

@Composable
fun PeopleUserInput(
    titleSuffix: String? = "",
    onPeopleChanged: (Int) -> Unit,
    peopleState: PeopleUserInputState = remember { PeopleUserInputState() }
) {
    Column {
        val transitionState = remember { peopleState.animationState }
        val tint = tintPeopleUserInput(transitionState)

        val people = peopleState.people
        CraneUserInput(
            text = if (people == 1) "$people Adult$titleSuffix" else "$people Adults$titleSuffix",
            vectorImageId = R.drawable.ic_person,
            tint = tint.value,
            onClick = {
                peopleState.addPerson()
                onPeopleChanged.invoke(peopleState.people)
            }
        )
        if (transitionState.targetState == PeopleUserInputAnimationState.Invalid) {
            Text(
                text = "Error: We don't support more than $MAX_PEOPLE people",
                style = MaterialTheme.typography.body1.copy(MaterialTheme.colors.secondary)
            )
        }
    }
}

@Composable
fun FromDestination() {
    CraneUserInput(text = "Seoul, South Korea", vectorImageId = R.drawable.ic_location)
}

@Composable
fun ToDestinationUserInput(onToDestinationChanged: (String) -> Unit) {
    val editableUserInputState = rememberEditableUserInputState(hint = "Choose Destination")
    CraneEditableUserInput(
        state = editableUserInputState,
        caption = "To",
        vectorImageId = R.drawable.ic_plane
    )

    val currentOnDestinationChanged by rememberUpdatedState(newValue = onToDestinationChanged)
    LaunchedEffect(key1 = editableUserInputState) {
        snapshotFlow { editableUserInputState.text }
            .filter { editableUserInputState.isHint.not() }
            .collect {
                currentOnDestinationChanged.invoke(editableUserInputState.text)
            }
    }
}

@Composable
fun DatesUserInput() {
    CraneUserInput(
        text = "",
        caption = "Select Dates",
        vectorImageId = R.drawable.ic_calendar
    )
}

@Composable
fun tintPeopleUserInput(
    transitionState: MutableTransitionState<PeopleUserInputAnimationState>
): State<Color> {
    val validColor = MaterialTheme.colors.onSurface
    val invalidColor = MaterialTheme.colors.secondary

    val transition = updateTransition(transitionState = transitionState, label = "Transition Color")
    return transition.animateColor(
        transitionSpec = { tween(durationMillis = 300) },
        label = "Animate Color"
    ) {
        if (it == PeopleUserInputAnimationState.Valid) validColor
        else invalidColor
    }
}

@Preview
@Composable
fun PeopleUserInputPreview() {
    AdvancedStateComposeTheme {
        PeopleUserInput(onPeopleChanged = {})
    }
}