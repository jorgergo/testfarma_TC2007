package com.example.testfarma_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testfarma_app.base.BaseViewHolder
import com.example.testfarma_app.modelo.estudio_modelo
import kotlinx.android.synthetic.main.estudio_layout.view.*

class estudio_adapter(private val context: Context, val listaEstudio:List<estudio_modelo>): RecyclerView.Adapter<BaseViewHolder<*>>(){




    /*
    val descripcion = arrayOf("Estudio de sangre",
        "Estudio de sangre2" ,
        "Estudio de sangre3")

    val precio = arrayOf("$1243", "$4231", "$3124")

    val imagen = arrayOf(R.drawable.mas_icon, R.drawable.mas_icon, R.drawable.mas_icon)

     */

   /* inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemDes: TextView
        var itemPrice: TextView
        var itemMas: ImageView

        init{
            itemDes = itemView.findViewById(R.id.estudio_des)
            itemPrice = itemView.findViewById(R.id.estudio_price)
            itemMas = itemView.findViewById(R.id.estudio_mas)
        }
    }

    */

    /*
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.estudio_layout, viewGroup, false)
        return ViewHolder(v)
    }

     */

   /* override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemDes.text = descripcion[i]
        viewHolder.itemPrice.text = precio[i]
        viewHolder.itemMas.setImageResource(imagen[i])
    }
    */

    override fun getItemCount(): Int = listaEstudio.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is EstudioViewHolder -> holder.bind(listaEstudio[position],position)
            else -> java.lang.IllegalArgumentException("Se olvido de pasar el viewholder en el bind")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return EstudioViewHolder(LayoutInflater.from(context).inflate(R.layout.estudio_layout, parent, false))
    }

    inner class EstudioViewHolder(itemView: View): BaseViewHolder<estudio_modelo>(itemView){
        override fun bind(item: estudio_modelo, position: Int) {
            itemView.estudio_des.text = item.nombre
            itemView.estudio_price.text = item.precio
            Glide.with(context).load(item.icono).into(itemView.estudio_mas)
            //itemView.estudio_mas.image = item.icono
        }
    }

}
