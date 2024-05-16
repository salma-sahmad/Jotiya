package com.example.l3ezlaapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.databinding.ActivityDetailBinding

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.l3ezlaapp.Helper.ManagmentCart
import com.example.l3ezlaapp.Adapter.ColorAdapter
//import com.example.l3ezlaapp.Adapter.SizeAdapter
import com.example.l3ezlaapp.Adapter.SliderAdapter
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.Model.SliderModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemModel // Assuming ItemModel has a field isLiked

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get item from intent
        item = intent.getParcelableExtra("object") ?: ItemModel()

        // Set initial like button state
//        updateLikeButtonState(item.isLiked)

        val favicon = findViewById<ImageView>(R.id.favBtn)

        // Set click listener for the favicon
        favicon.setOnClickListener {
            // Update the isLiked field of the ItemModel
            item.isLiked = !item.isLiked

            // Change the icon based on the isLiked field
            if (item.isLiked) {
                favicon.setImageResource(R.drawable.liked) // Change to liked icon
            } else {
                favicon.setImageResource(R.drawable.fav_icon) // Change to unliked icon
            }
        }
    }
    // Update like button state based on isLiked field
//    private fun updateLikeButtonState(isLiked: Boolean) {
//        if (isLiked) {
//            binding.favBtn.setImageResource(R.drawable.liked)
//        } else {
//            binding.favBtn.setImageResource(R.drawable.fav_icon)
//        }
//    }
}


//class DetailActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityDetailBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDetailBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
////        val cartImageView = findViewById<ImageView>(R.id.imageView3)
////        cartImageView.setOnClickListener {
////            val intent = Intent(this@DetailActivity, MainActivity::class.java)
////            startActivity(intent)
////        }
//    }
//
//
//
//}