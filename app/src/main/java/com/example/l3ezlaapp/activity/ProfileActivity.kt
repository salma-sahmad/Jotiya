package com.example.l3ezlaapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.databinding.ActivityDetailBinding
import com.example.l3ezlaapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val cartImageView = findViewById<ImageView>(R.id.logout)
//        cartImageView.setOnClickListener {
//            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
//            startActivity(intent)
//        }
    }



}