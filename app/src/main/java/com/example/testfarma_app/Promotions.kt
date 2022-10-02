package com.example.testfarma_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.promo_layout.*
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class Promotions: AppCompatActivity() {

    private val list = mutableListOf<CarouselItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.promo_layout)
        val carousel: ImageCarousel = findViewById(R.id.carousel)
        list.add(CarouselItem(imageDrawable = R.drawable.imagen_ejemplouno))
        list.add(CarouselItem(imageDrawable = R.drawable.imagen_ejemplouno))
        carousel.addData(list)

        setContentView(R.layout.promo_layout)
        val promoCarousel: ImageCarousel = findViewById(R.id.promo_carousel)
        list.add(CarouselItem(imageDrawable = R.drawable.imagen_ejemplouno))
        list.add(CarouselItem(imageDrawable = R.drawable.imagen_ejemplouno))
        promoCarousel.addData(list)

        setContentView(R.layout.promo_layout)

        menu.setOnClickListener {
            val intent = Intent(this, MenuApp::class.java)
            startActivity(intent)
            finish()
        }

        carrito_icon.setOnClickListener {
            val intent = Intent(this, carrito::class.java)
            startActivity(intent)
            finish()
        }
        perfil.setOnClickListener {
            val intent = Intent( this, UserDataInfo::class.java)
            startActivity(intent)
            finish()
        }
    }

}