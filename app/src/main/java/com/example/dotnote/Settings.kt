package com.example.dotnote

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class Settings : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var darkModeSwitch: Switch
    private lateinit var focusModeSwitch: Switch
    private lateinit var textSizeGroup: RadioGroup
    private lateinit var fontStyleSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {

        prefs = getSharedPreferences("DotNotePrefs", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        darkModeSwitch = findViewById(R.id.switchDarkMode)
        darkModeSwitch.isChecked = isDarkMode
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            recreate()
        }


        focusModeSwitch = findViewById(R.id.switchFocusMode)
        val isFocusMode = prefs.getBoolean("focusMode", false)
        focusModeSwitch.isChecked = isFocusMode
        focusModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("focusMode", isChecked).apply()
        }


        textSizeGroup = findViewById(R.id.radioGroupTextSize)
        val currentSize = prefs.getString("textSize", "Medium") ?: "Medium"
        when (currentSize) {
            "Small" -> textSizeGroup.check(R.id.radioSmall)
            "Medium" -> textSizeGroup.check(R.id.radioMedium)
            "Large" -> textSizeGroup.check(R.id.radioLarge)
        }
        textSizeGroup.setOnCheckedChangeListener { _, checkedId ->
            val selected = when (checkedId) {
                R.id.radioSmall -> "Small"
                R.id.radioMedium -> "Medium"
                R.id.radioLarge -> "Large"
                else -> "Medium"
            }
            prefs.edit().putString("textSize", selected).apply()
        }


        fontStyleSpinner = findViewById(R.id.spinnerFontStyle)
        val styles = listOf("Default", "Serif", "Monospace", "Sans")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, styles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fontStyleSpinner.adapter = adapter


        val currentFont = prefs.getString("fontStyle", "Default")
        val position = styles.indexOf(currentFont)
        if (position >= 0) fontStyleSpinner.setSelection(position)


        fontStyleSpinner.setOnItemSelectedListener(object :
            android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                pos: Int,
                id: Long
            ) {
                prefs.edit().putString("fontStyle", styles[pos]).apply()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        })
    }
}
