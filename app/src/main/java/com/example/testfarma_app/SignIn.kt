package com.example.testfarma_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity_NotSignedIn::class.java)
            startActivity(intent)
            finish()
        }
        // Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics.logEvent("Lgn_Screen", bundle)
    }
}




