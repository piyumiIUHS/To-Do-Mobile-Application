package com.example.myapplication1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.myapplication1.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    @SuppressLint("UnsafeOptInUsageError")
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the user is already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToMainActivity()
        }

        binding.HaveAccount.setOnClickListener {
            val intent = Intent(this@Register, MainActivity6::class.java)
            startActivity(intent)
            finish()
        }

        binding.RegisterB.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val email = binding.LoginEmail.text.toString().trim()
            val password = binding.LoginPassword.text.toString().trim()

            if (!validateName(name)) {
                Toast.makeText(
                    baseContext,
                    "Please enter a valid name. It should start with a capital letter and contain no numbers.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!validateEmail(email)) {
                Toast.makeText(
                    baseContext,
                    "Please enter a valid email address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!validatePassword(password)) {
                Toast.makeText(
                    baseContext,
                    "Password must be at least 6 characters long",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail: success")
                        Toast.makeText(
                            baseContext,
                            " successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToMainActivity()
                    } else {
                        val exception = task.exception
                        Log.w(TAG, "createUserWithEmail: failure", exception)
                        Toast.makeText(
                            baseContext,
                            "failed: ${exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun validateName(name: String): Boolean {
        val regex = "^[A-Z][a-zA-Z]*$".toRegex()
        return regex.matches(name)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@Register, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToMainActivity()
        }
    }

    companion object {
        private const val TAG = "Register"
    }
}
