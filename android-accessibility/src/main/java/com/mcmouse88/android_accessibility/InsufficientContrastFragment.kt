package com.mcmouse88.android_accessibility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InsufficientContrastFragment : Fragment() {

    private lateinit var loremIpsumContainer: RelativeLayout
    private lateinit var loremIpsumTitle: TextView
    private lateinit var loremIpsumText: TextView
    private lateinit var addItemFab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_insufficient_contrast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loremIpsumContainer = view.findViewById(R.id.lorem_ipsum_container)
        loremIpsumTitle = view.findViewById(R.id.lorem_ipsum_title)
        loremIpsumText = view.findViewById(R.id.lorem_ipsum_text)
        addItemFab = view.findViewById(R.id.add_item_fab)

        val colorContrastCheckbox = view.findViewById<CheckBox>(R.id.color_contrast_checkbox)
        colorContrastCheckbox.setOnCheckedChangeListener {_, isChecked ->
            when (isChecked) {
                true -> useHighContrast()
                false -> useLowContrast()
            }
        }
    }

    private fun useHighContrast() {
        context?.let {
            loremIpsumContainer.setBackgroundColor(ContextCompat.getColor(it, R.color.white))
            loremIpsumTitle.setTextColor(ContextCompat.getColor(it, R.color.medium_grey))
            loremIpsumText.setTextColor(ContextCompat.getColor(it, R.color.dark_grey))
            addItemFab.backgroundTintList = ContextCompat.getColorStateList(
                it,
                R.color.colorPrimaryLight
            )
        }
    }

    private fun useLowContrast() {
        context?.let {
            loremIpsumContainer.setBackgroundColor(ContextCompat.getColor(it, R.color.very_light_grey))
            loremIpsumTitle.setTextColor(ContextCompat.getColor(it, R.color.light_grey))
            loremIpsumText.setTextColor(ContextCompat.getColor(it, R.color.medium_grey))
            addItemFab.backgroundTintList = ContextCompat.getColorStateList(
                it,
                R.color.colorPrimaryLight
            )
        }
    }
}