package com.example.testfarma_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class estudio_adapter: RecyclerView.Adapter<estudio_adapter.ViewHolder>(){

    val descripcion = arrayOf("Una brebe descripcion de estudio",
        "Una brebe descripcion de estudio2" ,
        "Una brebe descripcion de estudio3")

    val precio = arrayOf("$1243", "$4231", "$3124")

    val imagen = arrayOf(R.drawable.carrito_icon, R.drawable.carrito_icon, R.drawable.carrito_icon)

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemDes: TextView
        var itemPrice: TextView
        var itemMas: ImageView

        init{
            itemDes = itemView.findViewById(R.id.estudio_des)
            itemPrice = itemView.findViewById(R.id.estudio_price)
            itemMas = itemView.findViewById(R.id.estudio_mas)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.estudio_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemDes.text = descripcion[i]
        viewHolder.itemPrice.text = precio[i]
        viewHolder.itemMas.setImageResource(imagen[i])
    }

    override fun getItemCount(): Int {
        return descripcion.size
    }


}
