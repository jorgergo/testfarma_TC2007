package com.example.testfarma_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.testfarma_app.adapterResultados.resultadosAdapter
import com.example.testfarma_app.dataResultados.datasourceResultados

class resultado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)
        val myDataset = datasourceResultados().loadResultados()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_res)
        //should work

        recyclerView.adapter = resultadosAdapter(this, myDataset)
        recyclerView.setHasFixedSize(true)




    }
}