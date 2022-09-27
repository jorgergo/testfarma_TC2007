package com.example.testfarma_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class resultado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_res)
        val adapter = resultadosAdapter()

        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = adapter


        val recyclerView2 = findViewById<RecyclerView>(R.id.recycler_view_res)
        val adapter2 = resultadosAdapter()

        recyclerView2.layoutManager = GridLayoutManager(this,2)
        recyclerView2.adapter = adapter2
    }


}