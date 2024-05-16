package com.example.l3ezlaapp.activity

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
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
//import com.example.l3ezlaapp.adapter.ImageAdapter
import com.example.l3ezlaapp.databinding.ActivityPosterBinding
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class PosterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPosterBinding
    private lateinit var auth: FirebaseAuth
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100
    private lateinit var storageReference: StorageReference
    private lateinit var imageAdapter: ImageAdapter
    private val images: MutableList<Bitmap> = mutableListOf()
    private val imageUrls: MutableList<String> = mutableListOf()
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPosterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        checkUserAuthentication()

        // Check if user is authenticated
        if (auth.currentUser == null) {
            // Redirect to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

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
    }

    private fun checkCameraPermissionAndDispatchIntent() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
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

    private fun uploadImageToFirebase(bitmap: Bitmap): Task<Uri> {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val filePath = "images/${UUID.randomUUID()}.jpg"
        val imageRef = storageReference.child(filePath)

        val uploadTask = imageRef.putBytes(data)
        return uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            // Continue with the task to get the download URL
            imageRef.downloadUrl
        }
    }


//    private fun postProductToFirestore() {
//        val title = binding.titleEditText.text.toString()
//        val description = binding.descriptionEditText.text.toString()
//        val price = binding.priceEditText.text.toString().toDouble() // Convert price to Double
//
//        // Upload images to Firebase Storage and get their URLs
//        val imageUploadTasks = mutableListOf<Task<Uri>>()
//        for (bitmap in images) {
//            val imageUploadTask = uploadImageToFirebase(bitmap)
//            imageUploadTasks.add(imageUploadTask)
//        }
//
//        Tasks.whenAllSuccess<Uri>(imageUploadTasks).addOnSuccessListener { imageUrls ->
//            // All images uploaded successfully, imageUrls contains URLs of all uploaded images
//            val product = hashMapOf(
//                "title" to title,
//                "description" to description,
//                "price" to price,
//                "imageUrls" to imageUrls.map { it.toString() } // Convert Uri to String
//            )
//
//            // Add the product to a collection named "products" in Firestore
//            val db = FirebaseFirestore.getInstance()
//            db.collection("products")
//                .add(product)
//                .addOnSuccessListener { documentReference ->
//                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                    // Show a success message or navigate to the main activity
//                    Toast.makeText(this, "Product posted successfully!", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this@PosterActivity, MainActivity::class.java))
//                    finish()
//                }
//                .addOnFailureListener { e ->
//                    Log.w(TAG, "Error adding document", e)
//                    // Show an error message
//                    Toast.makeText(this, "Failed to post product: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//        }.addOnFailureListener { e ->
//            // Handle failure when uploading images
//            Log.w(TAG, "Error uploading images", e)
//            Toast.makeText(this, "Failed to upload images: ${e.message}", Toast.LENGTH_SHORT).show()
//        }


    private fun postProductToFirestore() {
        val title = binding.titleEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val price = binding.priceEditText.text.toString().toDouble() // Convert price to Double

        // Upload images to Firebase Storage and get their URLs
        val imageUploadTasks = mutableListOf<Task<Uri>>()
        for (bitmap in images) {
            val imageUploadTask = uploadImageToFirebase(bitmap)
            imageUploadTasks.add(imageUploadTask)
        }

        Tasks.whenAllSuccess<Uri>(imageUploadTasks).addOnSuccessListener { imageUrls ->
            // All images uploaded successfully, imageUrls contains URLs of all uploaded images
            val product = Product(
                title = title,
                description = description,
                imageUrls = ArrayList(imageUrls.map { it.toString() }), // Convert Uri to String
                price = price
            )

            // Add the product to a collection named "products" in Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("products")
                .add(product)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    // Show a success message or navigate to the main activity
                    Toast.makeText(this, "Product posted successfully!", Toast.LENGTH_SHORT).show()
                    // Redirect to the main activity
                    startActivity(Intent(this@PosterActivity, MainActivity::class.java).apply {
                        // Pass the posted product to the main activity
                        putExtra("postedProduct", product)
                    })
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    // Show an error message
                    Toast.makeText(this, "Failed to post product: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }.addOnFailureListener { e ->
            // Handle failure when uploading images
            Log.w(TAG, "Error uploading images", e)
            Toast.makeText(this, "Failed to upload images: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }



    private fun checkUserAuthentication() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            Log.d("AuthCheck", "User is signed in: ${user.uid}")
        } else {
            // No user is signed in
            Log.e("AuthCheck", "No user is signed in")
        }
    }

}