package com.mcmouse88.android_accessibility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonContentLabeling = view.findViewById<Button>(R.id.content_labeling_button)
        buttonContentLabeling.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_homeFragment_to_contentLabelingFragment, null
            )
        )

        val buttonContentGrouping = view.findViewById<Button>(R.id.content_grouping_button)
        buttonContentGrouping.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_homeFragment_to_contentGroupingFragment, null
            )
        )

        val buttonLiveRegion = view.findViewById<Button>(R.id.live_region_button)
        buttonLiveRegion.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_homeFragment_to_liveRegionFragment, null
            )
        )

        val buttonExpandTouchArea = view.findViewById<Button>(R.id.expand_touch_area_button)
        buttonExpandTouchArea.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_homeFragment_to_expandTouchAreaFragment, null
            )
        )

        val buttonInsufficientContrast = view.findViewById<Button>(R.id.insufficient_contrast_button)
        buttonInsufficientContrast.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_homeFragment_to_insufficientContrastFragment, null
            )
        )

        val buttonToCounter = view.findViewById<Button>(R.id.counter_button)
        buttonToCounter.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_homeFragment_to_counterFragment, null
            )
        )
    }
}