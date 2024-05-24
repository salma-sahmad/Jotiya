package com.example.l3ezlaapp.activity

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.l3ezlaapp.Adapter.ImageAdapter
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.databinding.ActivityPosterBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class PosterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPosterBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100
    private lateinit var storageReference: StorageReference
    private lateinit var imageAdapter: ImageAdapter
    private val images: MutableList<Bitmap> = mutableListOf()
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPosterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageReference = FirebaseStorage.getInstance().reference

        viewPager = findViewById(R.id.viewPager)
        imageAdapter = ImageAdapter(images, viewPager)
        viewPager.adapter = imageAdapter

        val takePictureButton: Button = findViewById(R.id.addPicBtn)
        takePictureButton.setOnClickListener {
            checkCameraPermissionAndDispatchIntent()
        }

        val postButton: Button = findViewById(R.id.postButton)
        postButton.setOnClickListener {
            postProductToFirestore()
        }

        // Set up button click listeners
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun checkCameraPermissionAndDispatchIntent() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            dispatchTakePictureIntent()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Camera permission is required to take a picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            images.add(imageBitmap)
            imageAdapter.notifyItemInserted(images.size - 1)
            uploadImageToFirebase(imageBitmap)
        }
    }

    private fun uploadImageToFirebase(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val filePath = "images/${UUID.randomUUID()}.jpg"
        val imageRef = storageReference.child(filePath)

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postProductToFirestore() {
        val title = binding.titleEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val price = binding.priceEditText.text.toString().toDouble() // Convert price to Double

        // Upload images to Firebase Storage and get their URLs
        val imageUrls = mutableListOf<String>()
        for (bitmap in images) {
            val imageUrl = uploadImageToFirebase(bitmap)
            imageUrls.add(imageUrl.toString())
        }

        // Add product to Firestore
        val product = hashMapOf(
            "title" to title,
            "description" to description,
            "price" to price,
            "imageUrls" to imageUrls
        )

        // Add the product to a collection named "products" in Firestore
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                // Show a success message or navigate to the main activity
                Toast.makeText(this, "Product posted successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                // Show an error message
                Toast.makeText(this, "Failed to post product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}