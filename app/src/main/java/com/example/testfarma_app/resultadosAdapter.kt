package com.example.testfarma_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class resultadosAdapter(private val archList: ArrayList<arch_resultado>): RecyclerView.Adapter<resultadosAdapter.resViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): resViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.resultado_layout,
        parent,false)
        return resViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: resViewHolder, position: Int) {

        val currentItem = archList[position]
        holder.f_name.text = currentItem.f_name
        holder.date.text = currentItem.date
    }

    override fun getItemCount(): Int {

        return archList.size
    }


    class resViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val f_name : TextView = itemView.findViewById(R.id.nom_arch)
        val date : TextView = itemView.findViewById(R.id.fecha_ar)

    }
}