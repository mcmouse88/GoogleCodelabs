package com.mcmouse88.jetpack_compose_migration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.mcmouse88.jetpack_compose_migration.databinding.ActivityGardenBinding

// https://developer.android.com/codelabs/jetpack-compose-migration#0

class GardenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGardenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Displaying edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityGardenBinding.inflate(layoutInflater).also { setContentView(it.root) }
    }
}