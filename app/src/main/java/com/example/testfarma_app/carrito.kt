package com.example.testfarma_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_acercadenos.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class carrito : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        menu.setOnClickListener {
            val intent = Intent(this, MenuApp::class.java)
            startActivity(intent)
            finish()
        }

        back.setOnClickListener {
            val intent = Intent(this, estudio::class.java)
            startActivity(intent)
            finish()
        }


    }
}