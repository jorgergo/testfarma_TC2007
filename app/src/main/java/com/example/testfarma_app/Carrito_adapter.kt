package com.example.testfarma_app

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testfarma_app.eventBus.UpdateCartEvent
import com.example.testfarma_app.modelo.Carrito_modelo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.greenrobot.eventbus.EventBus


class Carrito_adapter(
    private val context: Context,
    private  val cartModelList: List<(Carrito_modelo)>
):RecyclerView.Adapter<Carrito_adapter.CarritoViewHolder>() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var btnMinus : ImageView ?= null
        var btnPlus : ImageView ?= null
        var btnDelete : ImageView ?= null
        var txtName : TextView ?= null
        var txtPrice : TextView ?= null
        var txtReque : TextView ?= null
        var txtQuantity : TextView ?= null

        init {
            btnMinus = itemView.findViewById(R.id.btnMinus) as ImageView
            btnPlus = itemView.findViewById(R.id.btnPlus) as ImageView
            btnDelete = itemView.findViewById(R.id.btnDelete) as ImageView
            txtName = itemView.findViewById(R.id.carrito_nombre) as TextView
            txtPrice = itemView.findViewById(R.id.carrito_price) as TextView
            txtReque = itemView.findViewById(R.id.carrito_Reque) as TextView
            txtQuantity = itemView.findViewById(R.id.txtQuantity) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        return CarritoViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.carrito_layout, parent, false))
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {

        databaseReference = FirebaseDatabase.getInstance().getReference("Cart")
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        holder.txtName!!.text = StringBuilder().append(cartModelList[position].nombre)
        holder.txtPrice!!.text = StringBuilder("$").append(cartModelList[position].precio)
        holder.txtReque!!.text = StringBuilder("Requerimiento: ").append(cartModelList[position].reque)
        holder.txtQuantity!!.text = StringBuilder("").append(cartModelList[position].quantity)

        //botones
        holder.btnMinus!!.setOnClickListener {_ -> minusCartItem(holder,cartModelList[position]) }
        holder.btnPlus!!.setOnClickListener {_ -> plusCartItem(holder,cartModelList[position]) }
        holder.btnDelete!!.setOnClickListener {_ ->
            val dialog = AlertDialog.Builder(context)
                .setTitle("Eliminar producto")
                .setMessage("¿Desea eliminar el producto?")
                .setNegativeButton("Cancelar"){dialog, _ -> dialog.dismiss()}
                .setPositiveButton("Eliminar") {dialog, _ ->

                    notifyItemRemoved(position)
                    databaseReference.child(uid!!)
                        .child(cartModelList[position].key!!)
                        .removeValue()
                        .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent()) }
                }
                .create()
            dialog.show()
        }
    }

    private fun plusCartItem(holder: CarritoViewHolder, carritoModelo: Carrito_modelo) {
        carritoModelo.quantity += 1
        carritoModelo.totalPrecio = carritoModelo.quantity * carritoModelo.precio!!.toFloat()
        holder.txtQuantity!!.text = StringBuilder("").append(carritoModelo.quantity)
        updateFirebase(carritoModelo)
    }

    private fun minusCartItem(holder: CarritoViewHolder, carritoModelo: Carrito_modelo) {
        if(carritoModelo.quantity > 1){
            carritoModelo.quantity -= 1
            carritoModelo.totalPrecio = carritoModelo.quantity * carritoModelo.precio!!.toFloat()
            holder.txtQuantity!!.text = StringBuilder("").append(carritoModelo.quantity)
            updateFirebase(carritoModelo)
        }
    }

    private fun updateFirebase(carritoModelo: Carrito_modelo) {

        databaseReference = FirebaseDatabase.getInstance().getReference("Cart")
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        databaseReference.child(uid!!)
            .child(carritoModelo.key!!)
            .setValue(carritoModelo)
            .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent()) }

    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }

}