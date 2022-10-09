package com.example.testfarma_app

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_acercadenos.menu
import kotlinx.android.synthetic.main.promo_layout.*

class acercanos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acercadenos)
        menu.setOnClickListener {
            val intent = Intent(this, MenuApp::class.java)
            startActivity(intent)
            finish()
        }
        perfil.setOnClickListener {
            val intent = Intent(this, UserDataInfo::class.java)
            startActivity(intent)
            finish()
        }
    }
}