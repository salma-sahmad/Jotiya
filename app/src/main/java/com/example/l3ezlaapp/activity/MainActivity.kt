package com.example.l3ezlaapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.l3ezlaapp.Adapter.PopularAdapter
import com.example.l3ezlaapp.Adapter.SliderAdapter
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.Model.SliderModel
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.ViewModel.MainViewModel
import com.example.l3ezlaapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private val filteredProducts = mutableListOf<ItemModel>()
    private val products = mutableListOf<ItemModel>()
    private lateinit var adapter: PopularAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        if (!isUserLoggedIn()) {
            navigateToLoginActivity()
            return
        }

        val userId = getUserId()

        // Check if there's a posted product
        val postedProduct: ItemModel? = intent.getParcelableExtra("postedProduct")
        if (postedProduct != null) {
            viewModel.addProductToPopular(postedProduct)
        }

        initUserName()

        initBanner()
        userId?.let { initPopular(it) }
        setupNavigation()
        setupSearchView()
    }

    private fun initUserName() {
        val currentUser = auth.currentUser
        val userName = currentUser?.displayName ?: "User"
        binding.textView3.text = userName
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer { items ->
            banners(items)
            binding.progressBarBanner.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    private fun banners(images: List<SliderModel>) {
        binding.viewpagerSlider.adapter = SliderAdapter(images, binding.viewpagerSlider)
        binding.viewpagerSlider.clipToPadding = false
        binding.viewpagerSlider.clipChildren = false
        binding.viewpagerSlider.offscreenPageLimit = 3
        binding.viewpagerSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer)
        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.setViewPager2(binding.viewpagerSlider)
        } else {
            binding.dotIndicator.visibility = View.GONE
        }
    }

    private fun initPopular(userId: String) {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.loadPopular().observe(this, Observer<List<ItemModel>> { items ->
            items?.let {
                products.clear()
                products.addAll(it)
                filteredProducts.clear()
                filteredProducts.addAll(it)
                binding.viewPopular.layoutManager = GridLayoutManager(this@MainActivity, 2)
                adapter = PopularAdapter(filteredProducts)
                binding.viewPopular.adapter = adapter
                binding.progressBarPopular.visibility = View.GONE

                // Setting up the "View All" button
                binding.textView12.setOnClickListener {
                    val intent = Intent(this@MainActivity, ViewAllActivity::class.java)
                    intent.putParcelableArrayListExtra("productsList", ArrayList(items))
                    startActivity(intent)
                }
            }
        })
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
        filteredProducts.clear()
        if (searchQuery.isEmpty()) {
            filteredProducts.addAll(products)
        } else {
            for (product in products) {
                if (product.title.lowercase().contains(searchQuery) ||
                    product.description.lowercase().contains(searchQuery)) {
                    filteredProducts.add(product)
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
                startActivity(Intent(this@MainActivity, CartActivity::class.java))
            }
        }

        if (profileImageView == null) {
            Log.e("MainActivity", "profileImageView is null")
        } else {
            profileImageView.setOnClickListener {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            }
        }

        if (posterImageView == null) {
            Log.e("MainActivity", "posterImageView is null")
        } else {
            posterImageView.setOnClickListener {
                startActivity(Intent(this@MainActivity, PosterActivity::class.java))
            }
        }

        if (likeImageView == null) {
            Log.e("MainActivity", "likeImageView is null")
        } else {
            likeImageView.setOnClickListener {
                startActivity(Intent(this@MainActivity, WishlistActivity::class.java))
            }
        }


    }

    private fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun getUserId(): String? {
        return sharedPreferences.getString("userId", null)
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the main activity
    }
}
