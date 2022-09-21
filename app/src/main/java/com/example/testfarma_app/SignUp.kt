package com.example.testfarma_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reg.*

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        cancelarReg.setOnClickListener {
            val intent = Intent(this, MainActivity_NotSignedIn::class.java)
            startActivity(intent)
            finish()
        }
    }
}


