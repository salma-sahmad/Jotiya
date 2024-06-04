package com.example.l3ezlaapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.l3ezlaapp.Admin.AdminActivity
import com.example.l3ezlaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import android.Manifest

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        requestNotificationPermission()
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val register = findViewById<TextView>(R.id.registerLogin)

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
                                val isFirstTime = isFirstTimeUser(userId)
                                saveLoginState(true, userId)
                                if (isFirstTime) {
                                    sendWelcomeNotification(userId)
                                }
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

        register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
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

    private fun isFirstTimeUser(userId: String?): Boolean {
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime_$userId", true)
        if (isFirstTime) {
            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstTime_$userId", false)
            editor.apply()
        }
        return isFirstTime
    }

    private fun sendWelcomeNotification(userId: String?) {
        // Use FirebaseMessaging to send a notification
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            // Send notification message
            val message = RemoteMessage.Builder("$token@fcm.googleapis.com")
                .setMessageId(Integer.toString((Math.random() * 1000).toInt()))
                .addData("title", "Welcome to Jotiya")
                .addData("body", "Thank you for signing up!")
                .build()

            FirebaseMessaging.getInstance().send(message)
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the login activity
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

}
