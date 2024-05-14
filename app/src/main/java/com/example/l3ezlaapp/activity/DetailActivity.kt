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

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemModel
    private var numberOder = 1
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)

        getBundle()
        banners()
        initLists()
    }

    private fun initLists() {
//        val sizeList = ArrayList<String>()
//        for (size in item.size) {
//            sizeList.add(size.toString())
//        }

//        binding.sizeList.adapter = SizeAdapter(sizeList)
//        binding.sizeList.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl) {
            colorList.add(imageUrl)
        }

        binding.colorList.adapter = ColorAdapter(colorList)
        binding.colorList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun banners() {
        val sliderItems = ArrayList<SliderModel>()
        for (imageUrl in item.picUrl) {
            sliderItems.add(SliderModel(imageUrl))
        }

        binding.slider.adapter = SliderAdapter(sliderItems, binding.slider)
        binding.slider.clipToPadding = true
        binding.slider.clipChildren = true
        binding.slider.offscreenPageLimit = 1


        if (sliderItems.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
//            binding.dotIndicator.attachTo(binding.viewpagerSlider)
            binding.dotIndicator.setViewPager2(binding.slider)

        }else {
            binding.dotIndicator.visibility = View.GONE

        }
    }

    private fun getBundle() {
        item = intent.getParcelableExtra("object")!!

        binding.titleTxt.text = item.title
        binding.descriptionTxt.text = item.description
        binding.priceTxt.text = "DH" + item.price
        binding.addToCartBtn.setOnClickListener {
//            item.numberInCart = numberOder
            managmentCart.insertFood(item)
        }
        binding.backBtn.setOnClickListener { finish() }
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, CartActivity::class.java))
        }
    }
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