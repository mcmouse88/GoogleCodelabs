package com.mcmouse88.incorporate_lifecycle_aware_components.step5_solution


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mcmouse88.incorporate_lifecycle_aware_components.R
import com.mcmouse88.incorporate_lifecycle_aware_components.step5.SeekBarViewModel

/**
 * Shows a SeekBar that is synced with a value in a ViewModel.
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

        mSeekBarViewModel = ViewModelProvider(requireActivity())[SeekBarViewModel::class.java]

        subscribeSeekBar()
        return root
    }

    private fun subscribeSeekBar() {

        // Update the ViewModel when the SeekBar is changed.
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    Log.d("Step5", "Progress changed!")
                    mSeekBarViewModel.seekbarValue.setValue(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Update the SeekBar when the ViewModel is changed.
        mSeekBarViewModel.seekbarValue.observe(requireActivity()) { value ->
            if (value != null) {
                mSeekBar.progress = value
            }
        }
    }
}
