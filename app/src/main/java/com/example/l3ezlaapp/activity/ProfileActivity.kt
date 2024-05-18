package com.example.l3ezlaapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.l3ezlaapp.R
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener { finish() }

        val home = findViewById<ImageView>(R.id.imageView11)
        home.setOnClickListener {
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        val poster = findViewById<ImageView>(R.id.imageView12)
        poster.setOnClickListener {
            val intent = Intent(this@ProfileActivity, PosterActivity::class.java)
            startActivity(intent)
        }

        val like = findViewById<ImageView>(R.id.imageView111)
        like.setOnClickListener {
            val intent = Intent(this@ProfileActivity, WishlistActivity::class.java)
            startActivity(intent)
        }

        val cart = findViewById<ImageView>(R.id.imageView163)
        cart.setOnClickListener {
            val intent = Intent(this@ProfileActivity, CartActivity::class.java)
            startActivity(intent)
        }

        val logout = findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            auth.signOut()
            clearLoginState()
            navigateToLoginActivity()
        }
    }

    private fun clearLoginState() {
        val editor = sharedPreferences.edit()
        editor.clear() // Clears all stored data
        editor.apply()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
