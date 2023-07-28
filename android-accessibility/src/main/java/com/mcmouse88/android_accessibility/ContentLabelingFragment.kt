package com.mcmouse88.android_accessibility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class ContentLabelingFragment : Fragment() {

    private var playing = false
    private lateinit var playPauseToggleImageView: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_content_labeling, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playPauseToggleImageView = view.findViewById(R.id.play_pause_toggle_view)
        updateImageButton()
        playPauseToggleImageView.setOnClickListener {
            playing = !playing
            updateImageButton()
        }
    }

    private fun updateImageButton() {
        if (playing) {
            playPauseToggleImageView.setImageResource(R.drawable.ic_pause)
        } else {
            playPauseToggleImageView.setImageResource(R.drawable.ic_play_arrow)
        }
    }
}