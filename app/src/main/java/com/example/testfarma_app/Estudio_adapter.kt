package com.example.testfarma_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testfarma_app.modelo.Estudio_modelo

class Estudio_adapter(
    private val context: Context,
    private val list:List<Estudio_modelo>
    ): RecyclerView.Adapter<Estudio_adapter.MyEstudioViewholder>(){

    class MyEstudioViewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
         var imageView: ImageView?=null
         var txtNombre:TextView?=null
         var txtPrecio : TextView?=null
         var txtReque : TextView?=null
         var txtDispo : TextView?=null

        init {
            imageView = itemView.findViewById(R.id.estudio_mas) as ImageView
            txtNombre = itemView.findViewById(R.id.estudio_des) as TextView;
            txtPrecio = itemView.findViewById(R.id.estudio_price) as TextView;
            txtReque = itemView.findViewById(R.id.estudio_reque) as TextView;
            txtDispo = itemView.findViewById(R.id.estudio_dispo) as TextView;
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyEstudioViewholder {
        return MyEstudioViewholder(LayoutInflater.from(context)
            .inflate(R.layout.estudio_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyEstudioViewholder, position: Int) {
        Glide.with(context)
            .load(list[position].icono)
            .into(holder.imageView!!)
        holder.txtNombre!!.text = StringBuilder().append(list[position].nombre)
        holder.txtPrecio!!.text = StringBuilder().append(list[position].precio)
        holder.txtDispo!!.text = StringBuilder().append(list[position].dispo)
        holder.txtReque!!.text = StringBuilder().append(list[position].reque)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
