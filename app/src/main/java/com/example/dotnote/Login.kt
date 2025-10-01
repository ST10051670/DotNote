package com.example.dotnote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        auth.signOut()

        // If user is already logged in, go to MainActivity
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val emailEditText = findViewById<EditText>(R.id.edtEmail)
        val passwordEditText = findViewById<EditText>(R.id.edtPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val signUpLink = findViewById<TextView>(R.id.txtSignUp)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                        // ✅ Redirect to your notes list page
                        val intent = Intent(this, NotesList::class.java)
                        startActivity(intent)
                        finish() // closes Login so back button won't return here
                    } else {
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }

        }

        // Go to SignUp page
        signUpLink.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
}
