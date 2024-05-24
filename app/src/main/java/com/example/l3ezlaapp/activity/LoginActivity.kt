package com.example.l3ezlaapp.activity

import com.example.l3ezlaapp.Admin.AdminActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.l3ezlaapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // Clear the fields when the activity starts
        usernameEditText.text.clear()
        passwordEditText.text.clear()

        // Check if the user is already logged in
        checkLoginState()

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                if (username == "admin@gmail.com" && password == "admin123") {
                    // Redirect to AdminActivity
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    finish() // Close the login activity
                } else {
                    // Perform login logic with Firebase Authentication
                    auth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid
                                saveLoginState(true, userId)
                                navigateToMainActivity()
                            } else {
                                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            } else {
                Toast.makeText(this, "Please enter username and password.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkLoginState() {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            navigateToMainActivity()
        }
    }

    private fun saveLoginState(isLoggedIn: Boolean, userId: String?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("userId", userId)
        editor.apply()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the login activity
    }
}
