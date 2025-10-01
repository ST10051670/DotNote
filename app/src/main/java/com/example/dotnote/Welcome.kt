package com.example.dotnote

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val prefs: SharedPreferences = getSharedPreferences("dotnote_prefs", MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("isFirstRun", true)

        if (!isFirstRun) {
            // Not first run → skip welcome screen
            startActivity(Intent(this, Login::class.java))
            finish()
            return
        }

        val continueButton = findViewById<Button>(R.id.btnContinue)
        continueButton.setOnClickListener {
            // Mark that the welcome screen has been seen
            prefs.edit().putBoolean("isFirstRun", false).apply()

            // Go to registration page
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }
    }
}
