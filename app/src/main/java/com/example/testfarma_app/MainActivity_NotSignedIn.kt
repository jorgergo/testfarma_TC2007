package com.example.testfarma_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity_NotSignedIn : AppCompatActivity() {


    private val list = mutableListOf<CarouselItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val carousel: ImageCarousel = findViewById(R.id.carousel)
        list.add(CarouselItem(imageDrawable = R.drawable.imagen_ejemplouno))
        list.add(CarouselItem(imageDrawable = R.drawable.imagen_ejemplouno))
        carousel.addData(list)

        /*
        //↓↓↓↓↓ Estudio disponible ↓↓↓↓↓
        val recyclerView = findViewById<RecyclerView>(R.id.estudio_lista)
        val adapter = estudio_adapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        //↑↑↑↑↑ Estudio disponible ↑↑↑↑↑

         */

        btn_signin.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
        }

        btn_signup.setOnClickListener {

        }

        carrito_icon.setOnClickListener {
            startActivity(Intent(this, carrito::class.java))

        }


    }
}