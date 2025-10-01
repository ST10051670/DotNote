package com.example.dotnote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dotnote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // For testing: ensure no user is logged in
        auth.signOut()

        val username = findViewById<EditText>(R.id.edtUsername)
        val email = findViewById<EditText>(R.id.edtEmail)
        val password = findViewById<EditText>(R.id.edtPassword)
        val confirmPassword = findViewById<EditText>(R.id.edtConfirmPassword)
        val signUpBtn = findViewById<Button>(R.id.btnSignUp)

        signUpBtn.setOnClickListener {
            val userNameText = username.text.toString().trim()
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val confirmPasswordText = confirmPassword.text.toString().trim()

            if (!isValidEmail(emailText)) {
                Toast.makeText(this, "Please enter a valid Gmail address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(passwordText)) {
                Toast.makeText(this, "Password must contain at least 3 numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordText != confirmPasswordText) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(userNameText, emailText, passwordText)
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val userId = currentUser?.uid

                    if (userId != null) {
                        val newUser = User(
                            id = 0,
                            username = username,
                            email = email,
                            password = "" // Do NOT store raw password
                        )

                        db.collection("users").document(userId)
                            .set(newUser)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                // Rollback Auth account if Firestore fails
                                currentUser.delete().addOnCompleteListener {
                                    Toast.makeText(
                                        this,
                                        "Sign Up Failed: ${e.message}. Auth account removed.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }
                } else {
                    val exceptionMessage = task.exception?.message
                    if (exceptionMessage?.contains("email address is already in use") == true) {
                        Toast.makeText(
                            this,
                            "This email is already registered. Please log in.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this, "Sign Up Failed: $exceptionMessage", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[A-Za-z0-9+_.-]+@gmail\\.com$"
        return email.matches(emailPattern.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        val numberCount = password.count { it.isDigit() }
        return numberCount >= 3
    }
}
