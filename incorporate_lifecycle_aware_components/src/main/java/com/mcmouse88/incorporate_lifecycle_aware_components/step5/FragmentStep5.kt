package com.mcmouse88.incorporate_lifecycle_aware_components.step5


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.mcmouse88.incorporate_lifecycle_aware_components.R

/**
 * Shows a SeekBar that should be synced with a value in a ViewModel.
 */
class FragmentStep5 : Fragment() {

    private lateinit var mSeekBar: SeekBar

    private lateinit var mSeekBarViewModel: SeekBarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_step5, container, false)
        mSeekBar = root.findViewById(R.id.seekBar)

        // TODO: get ViewModel
        subscribeSeekBar()

        return root
    }

    private fun subscribeSeekBar()   {

        // Update the ViewModel when the SeekBar is changed.

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // TODO: Set the ViewModel's value when the change comes from the user.
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        // TODO: Update the SeekBar when the ViewModel is changed.
        // mSeekBarViewModel.seekbarValue.observe(...
    }
}