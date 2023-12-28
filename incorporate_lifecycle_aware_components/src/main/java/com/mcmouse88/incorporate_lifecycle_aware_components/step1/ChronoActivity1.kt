package com.mcmouse88.incorporate_lifecycle_aware_components.step1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Chronometer
import com.mcmouse88.incorporate_lifecycle_aware_components.R

class ChronoActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chronometer = findViewById<Chronometer>(R.id.chronometer)
        chronometer.start()
    }
}