package com.example.l3ezlaapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.activity.LoginActivity
import com.example.l3ezlaapp.databinding.ActivityIntroBinding
//import com.google.firebase.FirebaseApp
import com.google.firebase.*
import com.google.firebase.database.FirebaseDatabase


class IntroActivity : BaseActivity() {
    private lateinit var binding:ActivityIntroBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater) // Initialize the binding
        setContentView(binding.root)





//        FirebaseApp.initializeApp(this)
//        FirebaseApp.initializeApp(this)
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val options = FirebaseOptions.Builder()
            .setApplicationId("1:550333518952:android:881ba7120c6b80d20a57dd")
            .setApiKey("AIzaSyAVw4GmRM9C5nTt8xziE9qLFEnND5OcKD4")
            .setProjectId("jotiya-37cd0")
            .setGcmSenderId("550333518952")
            .setStorageBucket("jotiya-37cd0.appspot.com")
            // Add other custom options if needed
            .build()

        FirebaseApp.initializeApp(this, options)

        binding.startBtn.setOnClickListener {
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
        }

        binding.startregister.setOnClickListener {
            val intent = Intent(this@IntroActivity, RegisterActivity::class.java)
            startActivity(intent)
        }




//        // Find the TextView containing the "Don't have an account?" text
//        val textViewRegister = findViewById<TextView>(R.id.startregister)
//
//        // Create a SpannableString from the text
//        val spannableString = SpannableString(textViewRegister.text)
//
//        // Create a ClickableSpan for the "Register" text
//        val clickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                // Start the RegisterActivity when the text is clicked
//                startActivity(Intent(this@IntroActivity, RegisterActivity::class.java))
//            }
//        }
//
//
//        // Set the ClickableSpan to the "Register" text
//        spannableString.setSpan(clickableSpan, 23, spannableString.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//        // Set the modified SpannableString to the TextView
//        textViewRegister.text = spannableString
//
//        // Enable clickable links in the TextView
//        textViewRegister.movementMethod = LinkMovementMethod.getInstance()
    }



}
