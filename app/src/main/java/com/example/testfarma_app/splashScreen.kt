package com.example.testfarma_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class splashScreen : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        Handler().postDelayed({
            if (mAuth!!.currentUser != null) {
                startActivity(Intent(this, acercanos::class.java))
            } else {
                startActivity(Intent(this, MainActivity_NotSignedIn::class.java))
            }
            finish()
        }, 2000)
    }
}