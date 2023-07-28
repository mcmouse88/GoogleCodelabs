package com.mcmouse88.android_accessibility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class LiveRegionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_region, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val androidVersion = ArrayList(listOf(*resources.getStringArray(R.array.android_versions)))
        val correctAnswerIndex = androidVersion.indexOf(getString(R.string.lollipop))
        val radioGroup: RadioGroup = view.findViewById(R.id.radio_group)
        val feedbackTextView: TextView = view.findViewById(R.id.feedback_text_view)

        androidVersion.indices.forEach { i ->
            val radioButton = RadioButton(context)
            radioButton.text = androidVersion[i]
            radioButton.text = androidVersion[i]
            radioButton.id = i
            radioButton.setPadding(36, 36, 36, 36)
            radioButton.textSize = 18f
            radioGroup.addView(radioButton)
        }

        context?.let {
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (radioGroup.indexOfChild(view.findViewById(checkedId))) {
                    correctAnswerIndex -> {
                        feedbackTextView.setText(R.string.correct)
                        feedbackTextView.setBackgroundColor(
                            ContextCompat.getColor(it, R.color.green)
                        )
                    }
                    else -> {
                        feedbackTextView.setText(R.string.incorrect)
                        feedbackTextView.setBackgroundColor(
                            ContextCompat.getColor(it, R.color.red)
                        )
                    }
                }
            }
        }
    }
}