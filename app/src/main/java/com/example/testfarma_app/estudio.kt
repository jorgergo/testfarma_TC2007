package com.example.testfarma_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testfarma_app.modelo.estudio_modelo
import kotlinx.android.synthetic.main.activity_estudio.*
import kotlinx.android.synthetic.main.activity_sign_in.back
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

private val list = mutableListOf<CarouselItem>()

class estudio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudio)
        /*val recyclerView = findViewById<RecyclerView>(R.id.resultados_rec)
        val adapter = estudio_adapter()
         */

        val carousel: ImageCarousel = findViewById(R.id.carousel)
        list.add(CarouselItem(imageDrawable = R.drawable.imagen_ejemplouno))
        list.add(CarouselItem(imageDrawable = R.drawable.imagen_ejemplouno))
        carousel.addData(list)

        /*
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

         */

        //contenido en lista de estudio
        setupRecyclerView()

        //regresar al main
        back.setOnClickListener {
            val intent = Intent(this, MainActivity_NotSignedIn::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun setupRecyclerView() {
        val linkIcono_mas = "https://cdn-icons-png.flaticon.com/128/0/517.png"
        resultados_rec.layoutManager = LinearLayoutManager(this)
        val listEstudios: List<estudio_modelo> = listOf(
            estudio_modelo("Estudio de sangre", "1234", linkIcono_mas),
            estudio_modelo("Estudio de saliva", "1432", linkIcono_mas),
            estudio_modelo("Estudio de orina", "3214", linkIcono_mas)
        )
        resultados_rec.adapter = estudio_adapter(this, listEstudios)
    }
}