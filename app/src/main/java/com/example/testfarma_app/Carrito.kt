package com.example.testfarma_app

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testfarma_app.eventBus.UpdateCartEvent
import com.example.testfarma_app.listener.CarritoLoadListener
import com.example.testfarma_app.modelo.Carrito_modelo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_acercadenos.menu
import kotlinx.android.synthetic.main.activity_carrito.*
import kotlinx.android.synthetic.main.activity_sign_in.back
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class Carrito : AppCompatActivity(), CarritoLoadListener {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    var cartLoadListener: CarritoLoadListener ?= null

    override fun onStart() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun onUpdateCartEvent(event: UpdateCartEvent){
        loadCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        init()
        loadCartFromFirebase()

        menu.setOnClickListener {
            val intent = Intent(this, MenuApp::class.java)
            startActivity(intent)
            finish()
        }

        back.setOnClickListener {
            val intent = Intent(this, estudio::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun loadCartFromFirebase() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Cart")
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        val cartModels : MutableList<Carrito_modelo> = ArrayList()
        databaseReference.child(uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children){
                        val cartModel = cartSnapshot.getValue(Carrito_modelo::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener!!.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener!!.onLoadCartFailed(error.message)
                }
            })
    }

    private fun init(){
        cartLoadListener = this
        val layoutManager = LinearLayoutManager(this)
        carrito_recy!!.layoutManager = layoutManager
        carrito_recy!!.addItemDecoration(DividerItemDecoration(this,layoutManager.orientation))

    }

    override fun onLoadCartSuccess(cartModelList: List<Carrito_modelo>) {
        var sum = 0.0
        for (cartModel in cartModelList!!){
            sum+= cartModel!!.totalPrecio
        }
        txtTotal.text = StringBuilder("$").append(sum)
        val adapter = Carrito_adapter(this, cartModelList)
        carrito_recy!!.adapter = adapter

    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(findViewById(R.id.estudio_relativeLayout),message!!, Snackbar.LENGTH_LONG).show()
    }
}