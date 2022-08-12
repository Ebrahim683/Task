package com.example.task.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task.R
import com.example.task.ui.admin.AdminActivity
import com.example.task.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        id_btn_login.setOnClickListener {
            logInUser()
        }

        id_btn_admin.setOnClickListener {
            val code = admin_code.text.toString().trim()
            if (code.isEmpty()) {
                Toast.makeText(this, "Enter admin code", Toast.LENGTH_SHORT).show()
            } else if (code != "845") {
                Toast.makeText(this, "Invalid admin code", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, AdminActivity::class.java))
            }
        }

    }

    private fun logInUser() {
        var email = id_email.editText?.text.toString().trim()
        var password = id_password.editText?.text.toString().trim()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "logInUser: ${task.result}")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.d(TAG, "logInUser: ${task.exception}")
                Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}