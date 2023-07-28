package com.mcmouse88.android_accessibility

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableInt
import androidx.fragment.app.Fragment
import com.mcmouse88.android_accessibility.databinding.FragmentCounterBinding

class CounterFragment : Fragment(R.layout.fragment_counter) {

    private val count = ObservableInt(0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCounterBinding.bind(view)

        binding.count = count

        binding.addButton.setOnClickListener {
            count.set(count.get() + 1)
        }

        binding.subtractButton.setOnClickListener {
            count.set(count.get() - 1)
        }
    }
}