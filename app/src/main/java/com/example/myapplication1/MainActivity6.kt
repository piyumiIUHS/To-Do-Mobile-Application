package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication1.databinding.ActivityMain6Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity6 : AppCompatActivity() {
    private lateinit var binding: ActivityMain6Binding
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val TAG = "MainActivity6"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        binding = ActivityMain6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the user is already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToMainActivity()
        }

        binding.Logintext.setOnClickListener {
            val intent = Intent(this@MainActivity6, Register::class.java)
            startActivity(intent)
            finish()
        }

        binding.LoginB.setOnClickListener {
            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "LoginWithEmail:success")
                            Toast.makeText(
                                baseContext,
                                "Login success",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val intent = Intent(this@MainActivity6, MainActivity2::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.w(TAG, "LoginWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Login failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    baseContext,
                    "Please enter email and password.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@MainActivity6, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }
}