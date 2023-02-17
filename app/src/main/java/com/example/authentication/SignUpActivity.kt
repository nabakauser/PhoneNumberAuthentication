package com.example.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.authentication.databinding.ActivityMainBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class SignUpActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    var number: String = ""

    private var storedVerificationId: String? = null

    //private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val auth: FirebaseAuth? by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpListeners()
        checkDb()
    }

    private fun checkDb() {
        if(auth?.currentUser != null) {
            startActivity(Intent(this, LoggedInActivity::class.java))
            finish()
        }
    }

    private fun setUpListeners() {
        binding?.uiBtnSendOtp?.setOnClickListener {
            sendOtp()
        }
    }


    private fun sendOtp() {
        val phoneNumber = binding?.uiEtPhoneNumber?.text
        if (phoneNumber?.isNotEmpty() == true) {
            number = "phoneNumber"
            sendVerificationCode(phoneNumber.toString())
        } else {
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationCode(number: String?) {
        val options = auth?.let {
            callbacks.let { it1 ->
                PhoneAuthOptions.newBuilder(it).setPhoneNumber(number.toString())
                    .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(it1).build()
            }
        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        Log.d("Msg", "Auth started")
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            startActivity(Intent(this@SignUpActivity, LoggedInActivity::class.java))
            finish()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.d("MSg", "onVerificationFailed  $p0")
        }

        override fun onCodeSent(
            verificationId: String, token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d("Msg", "onCodeSent: $verificationId")
            storedVerificationId = verificationId
            //resendToken = token

            val intent = Intent(this@SignUpActivity, OtpActivity::class.java)
            intent.putExtra("storedVerificationId", storedVerificationId)
            startActivity(intent)
            finish()
        }
    }
}