package com.mcmouse88.android_accessibility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class ExpandTouchAreaFragment : Fragment() {

    private lateinit var toggleImageButton: ImageButton
    private var playing: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expand_touch_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleImageButton = view.findViewById(R.id.play_pause_toggle_view)
        setUi()
        toggleImageButton.setOnClickListener {
            playing = !playing
            setUi()
        }
    }

    private fun setUi() {
        if (playing) {
            toggleImageButton.setImageResource(R.drawable.ic_cancel)
            toggleImageButton.contentDescription = getString(R.string.cancel)
        } else {
            toggleImageButton.setImageResource(R.drawable.ic_play_circle_outline)
            toggleImageButton.contentDescription = getString(R.string.refresh)
        }
    }
}