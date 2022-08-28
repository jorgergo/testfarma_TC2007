package com.example.testfarma_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class MainActivity : AppCompatActivity() {

    private val list = mutableListOf<CarouselItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val carousel: ImageCarousel = findViewById(R.id.carousel)
        list.add(CarouselItem(imageDrawable = R.drawable.imagen_ejemplouno))
        list.add(CarouselItem(imageDrawable = R.drawable.imagen_ejemplodos))
        carousel.addData(list)

    }
}