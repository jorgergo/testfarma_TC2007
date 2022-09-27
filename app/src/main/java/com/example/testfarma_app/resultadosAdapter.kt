package com.example.testfarma_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class resultadosAdapter: RecyclerView.Adapter<resultadosAdapter.ViewHolder>(){

    val nombres = arrayOf("Estudio 1","Estudio 2","Estudio 3","Estudio 4","Estudio 5")
    val fechas = arrayOf("18-12-2020","18-12-2021","18-12-2021","18-12-2022","19-12-2020")

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.resultado_layout,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.fname_res.text = nombres[i]
        viewHolder.fecha_c.text = fechas[i]
    }

    override fun getItemCount(): Int {
        return nombres.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var fname_res: TextView
        var fecha_c: TextView

        init {
            fname_res = itemView.findViewById(R.id.nom_arch)
            fecha_c = itemView.findViewById(R.id.fecha_ar)
        }
    }
}