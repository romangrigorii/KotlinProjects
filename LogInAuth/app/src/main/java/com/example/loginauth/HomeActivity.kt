package com.example.loginauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginauth.databinding.ActivityHomeBinding
import com.example.loginauth.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private var binding : ActivityHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()
        val email = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")
        binding?.tvName?.text = name

        binding?.tvSignOut?.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}