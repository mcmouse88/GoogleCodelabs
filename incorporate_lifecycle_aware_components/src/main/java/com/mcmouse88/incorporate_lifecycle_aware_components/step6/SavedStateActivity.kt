package com.mcmouse88.incorporate_lifecycle_aware_components.step6

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mcmouse88.incorporate_lifecycle_aware_components.R

/**
 * Shows a simple form with a button and displays the value of a property in a ViewModel.
 */
class SavedStateActivity : AppCompatActivity() {

    private lateinit var mSavedStateViewModel: SavedStateViewModel

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_state_activity)

        // Obtain the ViewModel
        mSavedStateViewModel = ViewModelProvider(this)[SavedStateViewModel::class.java]

        // Show the ViewModel property's value in a TextView
        mSavedStateViewModel.name.observe(this) { savedString ->

            findViewById<TextView>(R.id.saved_vm_tv).text = getString(
                R.string.saved_in_vm,
                savedString
            )
        }

        // Save button
        findViewById<Button>(R.id.save_bt).setOnClickListener {
            val newName = findViewById<EditText>(R.id.name_et).text.toString()
            mSavedStateViewModel.saveNewName(newName)
        }
    }
}
