package com.mcmouse88.incorporate_lifecycle_aware_components.step3

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mcmouse88.incorporate_lifecycle_aware_components.R


class ChronoActivity3 : AppCompatActivity() {

    private lateinit var mLiveDataTimerViewModel: LiveDataTimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.chrono_activity_3)

        mLiveDataTimerViewModel = ViewModelProvider(this)[LiveDataTimerViewModel::class.java]

        subscribe()
    }

    private fun subscribe() {
        val elapsedTimeObserver = Observer<Long> { aLong ->
            val newText = getString(R.string.seconds, aLong)
            findViewById<TextView>(R.id.timer_textview).text = newText
            Log.d("ChronoActivity3", "Updating timer")
        }
        //TODO: observe the ViewModel's elapsed time
    }
}
