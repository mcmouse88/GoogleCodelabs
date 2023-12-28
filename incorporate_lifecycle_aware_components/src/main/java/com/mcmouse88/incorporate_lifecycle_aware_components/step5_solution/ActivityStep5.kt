package com.mcmouse88.incorporate_lifecycle_aware_components.step5_solution

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mcmouse88.incorporate_lifecycle_aware_components.R

/**
 * Shows two {@link FragmentStep5} fragments.
 */
class ActivityStep5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_step_5_solution)
    }
}
