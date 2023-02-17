package com.example.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.authentication.databinding.ActivityMainBinding
import com.example.authentication.databinding.ActivityOtpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpActivity : AppCompatActivity() {
    private var binding: ActivityOtpBinding? = null
    private val auth: FirebaseAuth? by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpListeners()
    }

    private fun verifyOtp() {
        val otp = binding?.uiEtOtp?.text?.trim().toString()
        val storedVerificationId= intent.getStringExtra("storedVerificationId")
        if(otp.isNotEmpty()){
            val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                storedVerificationId.toString(), otp)
            signInWithPhoneAuthCredential(credential)
        }else{
            Toast.makeText(this,"Enter OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpListeners() {
        binding?.uiBtnVerifyOtp?.setOnClickListener {
            verifyOtp()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this , LoggedInActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this,"Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}