package com.example.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.authentication.databinding.ActivityLoggedInBinding
import com.google.firebase.auth.FirebaseAuth

class LoggedInActivity : AppCompatActivity() {
    private var binding: ActivityLoggedInBinding? = null
    private val auth: FirebaseAuth? by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoggedInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpListeners()
    }

    private fun setUpListeners() {
        binding?.uiBtnLogOut?.setOnClickListener {
            auth?.signOut()
            navigateToLoginActivity()
        }
    }

    private fun navigateToLoginActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}