package com.mcmouse88.testing_basics.util

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.mcmouse88.testing_basics.Event
import com.mcmouse88.testing_basics.R
import com.mcmouse88.testing_basics.ScrollChildRefreshLayout

/**
 * Extension functions and Binding Adapters.
 */

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run { show() }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    owner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int
) {
    snackbarEvent.observe(owner) { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(context.getString(it), timeLength)
        }
    }
}

fun Fragment.setupRefreshLayout(
    refreshLayout: ScrollChildRefreshLayout,
    scrollUpChild: View? = null
) {
    refreshLayout.setColorSchemeColors(
        ContextCompat.getColor(requireContext(), R.color.colorPrimary),
        ContextCompat.getColor(requireContext(), R.color.colorAccent),
        ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
    )
    // Set the scrolling view in the custom SwipeRefreshLayout
    scrollUpChild?.let {
        refreshLayout.scrollUpChild = it
    }
}
