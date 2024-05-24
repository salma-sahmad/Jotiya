package com.example.l3ezlaapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.l3ezlaapp.Adapter.PopularAdapter
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.R
import androidx.appcompat.widget.SearchView
import com.example.l3ezlaapp.databinding.ActivityViewallBinding

class ViewAllActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewallBinding
    private lateinit var adapter: PopularAdapter
    private val productsList = mutableListOf<ItemModel>()
    private val filteredList = mutableListOf<ItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the list of products from the intent
        val intentProductsList: ArrayList<ItemModel>? = intent.getParcelableArrayListExtra("productsList")
        if (intentProductsList != null) {
            productsList.addAll(intentProductsList)
            filteredList.addAll(intentProductsList)
        }

        // Set up the RecyclerView
        binding.viewAllRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = PopularAdapter(filteredList)
        binding.viewAllRecyclerView.adapter = adapter

        findViewById<ImageView>(R.id.imageView11).setOnClickListener {
            startActivity(Intent(this@ViewAllActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

        binding.backBtn.setOnClickListener { finish() }
        setupNavigation()
        setupSearchView()
    }


    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterProducts(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProducts(newText)
                return true
            }
        })
    }

    private fun filterProducts(query: String?) {
        val searchQuery = query?.lowercase() ?: ""
        filteredList.clear()
        if (searchQuery.isEmpty()) {
            filteredList.addAll(productsList)
        } else {
            for (product in productsList) {
                if (product.title.lowercase().contains(searchQuery) || product.description.lowercase().contains(searchQuery)) {
                    filteredList.add(product)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun setupNavigation() {
        val cartImageView = findViewById<ImageView>(R.id.imageView163)
        val profileImageView = findViewById<ImageView>(R.id.imageView166)
        val posterImageView = findViewById<ImageView>(R.id.imageView12)
        val likeImageView = findViewById<ImageView>(R.id.imageView111)

        if (cartImageView == null) {
            Log.e("MainActivity", "cartImageView is null")
        } else {
            cartImageView.setOnClickListener {
                startActivity(Intent(this@ViewAllActivity, CartActivity::class.java))
            }
        }

        if (profileImageView == null) {
            Log.e("MainActivity", "profileImageView is null")
        } else {
            profileImageView.setOnClickListener {
                startActivity(Intent(this@ViewAllActivity, ProfileActivity::class.java))
            }
        }

        if (posterImageView == null) {
            Log.e("MainActivity", "posterImageView is null")
        } else {
            posterImageView.setOnClickListener {
                startActivity(Intent(this@ViewAllActivity, PosterActivity::class.java))
            }
        }

        if (likeImageView == null) {
            Log.e("MainActivity", "likeImageView is null")
        } else {
            likeImageView.setOnClickListener {
                startActivity(Intent(this@ViewAllActivity, WishlistActivity::class.java))
            }
        }


    }

}
