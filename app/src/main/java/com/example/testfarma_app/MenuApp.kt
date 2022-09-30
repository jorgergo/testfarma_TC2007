package com.example.testfarma_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu_app.*

class MenuApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_app)

        btn_citas.setOnClickListener{
            val intent = Intent(this, Appointment::class.java)
            startActivity(intent)
            finish()
        }

        btn_resultados.setOnClickListener{
            val intent = Intent(this, resultado::class.java)
            startActivity(intent)
            finish()
        }

        btn_estudios.setOnClickListener{
            val intent = Intent(this, estudio::class.java)
            startActivity(intent)
            finish()
        }

        btn_promo.setOnClickListener{
            val intent = Intent(this, Promotions::class.java)
            startActivity(intent)
            finish()
        }

        btn_nosotros.setOnClickListener{
            val intent = Intent(this, acercanos::class.java)
            startActivity(intent)
            finish()
        }
    }
}