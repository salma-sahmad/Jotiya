package com.example.l3ezlaapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.l3ezlaapp.Adapter.PopularAdapter
import com.example.l3ezlaapp.Adapter.SliderAdapter
//import com.example.l3ezlaapp.Adapter.BrandAdapter
//import com.example.l3ezlaapp.Adapter.PopularAdapter
//import com.example.l3ezlaapp.Adapter.SliderAdapter
import com.example.l3ezlaapp.Model.SliderModel
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.ViewModel.MainViewModel
import com.example.l3ezlaapp.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    private  val  viewModel= MainViewModel()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        FirebaseDatabase.getInstance().setPersistenceEnabled(true) // Enable disk persistence, if needed

        initBanner()
        initPopular()

        /**
         * to navigate to the details activity
         */
//        val clothesCategory = findViewById<ImageView>(R.id.imageView10)
//        clothesCategory.setOnClickListener {
//            // Navigate to the product detail page here
//            val intent = Intent(this@MainActivity, DetailActivity::class.java)
//            startActivity(intent)
//        }

        /**
         * to navigate to cart page
         */
        val cartImageView = findViewById<ImageView>(R.id.imageView163)
        cartImageView.setOnClickListener {
            val intent = Intent(this@MainActivity, CartActivity::class.java)
            startActivity(intent)
        }

        /**
         * to navigate to profile page
         */
        val profileImageView = findViewById<ImageView>(R.id.imageView166)
        profileImageView.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        val Poster = findViewById<ImageView>(R.id.imageView12)
        Poster.setOnClickListener {
            val intent = Intent(this@MainActivity, PosterActivity::class.java)
            startActivity(intent)
        }
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
//            binding.dotIndicator.attachTo(binding.viewpagerSlider)
            binding.dotIndicator.setViewPager2(binding.viewpagerSlider)

        }else {
            binding.dotIndicator.visibility = View.GONE
        }
    }


    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.popular.observe(this, Observer {
            binding.viewPopular.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.viewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        })
        viewModel.loadPupolar()
    }

}