package com.example.dotnote

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY = 2000L // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Check if app is opened for the first time
        val prefs: SharedPreferences = getSharedPreferences("dotnote_prefs", MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("isFirstRun", true)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isFirstRun) {
                // First launch → go to Splash/Intro page
                prefs.edit().putBoolean("isFirstRun", false).apply()
                startActivity(Intent(this, SignUp::class.java)) // or a tutorial screen
            } else {
                // Not first launch → go to Login
                startActivity(Intent(this, Login::class.java))
            }
            finish()
        }, SPLASH_DELAY)
    }
}
