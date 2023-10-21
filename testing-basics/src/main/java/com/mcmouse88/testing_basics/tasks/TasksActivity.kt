package com.mcmouse88.testing_basics.tasks

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mcmouse88.testing_basics.R

// https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-basics#0

class TasksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
    }
}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3