package com.example.testfarma_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testfarma_app.eventBus.UpdateCartEvent
import com.example.testfarma_app.listener.CarritoLoadListener
import com.example.testfarma_app.listener.EstudioRecyClickListener
import com.example.testfarma_app.modelo.Carrito_modelo
import com.example.testfarma_app.modelo.Estudio_modelo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus

class Estudio_adapter(
    private val context: Context,
    private val list:List<Estudio_modelo>,
    private val cartListener:CarritoLoadListener
    ): RecyclerView.Adapter<Estudio_adapter.MyEstudioViewholder>(){

    class MyEstudioViewholder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
         var imageView: ImageView?=null
         var txtNombre:TextView?=null
         var txtPrecio : TextView?=null
         var txtReque : TextView?=null
         var txtDispo : TextView?=null

        private var clickListener:EstudioRecyClickListener? = null

        fun setClickListener(clickListener: EstudioRecyClickListener){
            this.clickListener = clickListener;
        }

        init {
            imageView = itemView.findViewById(R.id.estudio_mas) as ImageView
            txtNombre = itemView.findViewById(R.id.estudio_des) as TextView
            txtPrecio = itemView.findViewById(R.id.estudio_price) as TextView
            txtReque = itemView.findViewById(R.id.estudio_reque) as TextView
            txtDispo = itemView.findViewById(R.id.estudio_dispo) as TextView

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            clickListener!!.onItemClickListener(view,adapterPosition)
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
        holder.txtPrecio!!.text = StringBuilder("$").append(list[position].precio)
        holder.txtDispo!!.text = StringBuilder("Disponible en: ").append(list[position].dispo)
        holder.txtReque!!.text = StringBuilder("Requerimiento: ").append(list[position].reque)

        holder.setClickListener(object : EstudioRecyClickListener{
            override fun onItemClickListener(view: View?, position: Int) {
                addToCart(list[position])
            }
        })
    }

    private fun addToCart(estudioModelo: Estudio_modelo) {
        val userCart = FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("uid")
        userCart.child(estudioModelo.key!!)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){ //si estudio ya esta en carrito, solo actualiza
                        val cartModel = snapshot.getValue(Carrito_modelo::class.java)
                        val updateData: MutableMap<String,Any> = HashMap()
                        cartModel!!.quantity = cartModel!!.quantity+1;
                        updateData["quantity"] =cartModel!!.quantity
                        updateData["totalPrice"] =
                            cartModel!!.quantity * cartModel.precio!!.toFloat()

                        userCart.child(estudioModelo.key!!)
                            .updateChildren(updateData)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                //cartListener.onLoadCartFailed("Success add to cart")
                            }
                            .addOnFailureListener{ e-> cartListener.onLoadCartFailed(e.message)}
                    }
                    else{ //si estudio no esta en carrito, agrega uno
                        val cartModel = Carrito_modelo()
                        cartModel.key = estudioModelo.key
                        cartModel.nombre = estudioModelo.nombre
                        cartModel.precio = estudioModelo.precio
                        cartModel.reque = estudioModelo.reque
                        cartModel.dispo = estudioModelo.dispo
                        cartModel.icono = estudioModelo.icono
                        cartModel.quantity = 1
                        cartModel.totalPrecio = estudioModelo.precio!!.toFloat()

                        userCart.child(estudioModelo.key!!)
                            .setValue(cartModel)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                //Toast.makeText(this, "Toast",Toast.LENGTH_SHORT).show()
                                //cartListener.onLoadCartFailed("Success add to cart")
                                //Toast.makeText(getApplicationContext(), "Toast",Toast.LENGTH_SHORT).show();
                            }
                            .addOnFailureListener{ e-> cartListener.onLoadCartFailed(e.message)}
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    cartListener.onLoadCartFailed(error.message)
                }
            })
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
