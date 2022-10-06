package com.example.testfarma_app
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testfarma_app.eventBus.UpdateCartEvent
import com.example.testfarma_app.listener.CarritoLoadListener
import com.example.testfarma_app.listener.IDEstudioListener
import com.example.testfarma_app.modelo.Carrito_modelo
import com.example.testfarma_app.modelo.Estudio_modelo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_estudio.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class estudio : AppCompatActivity(), IDEstudioListener,  CarritoLoadListener{
    lateinit var estudioLoadListener: IDEstudioListener
    lateinit var cartLoadListener: CarritoLoadListener

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onStart() {
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
        countCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudio)

        irCarrito.setOnClickListener {
            val intent = Intent(this, Carrito::class.java)
            startActivity(intent)
            finish()
        }

        menu.setOnClickListener {
            val intent = Intent(this, MenuApp::class.java)
            startActivity(intent)
            finish()
        }

        init()
        loadEstudioFromFirebase()
        countCartFromFirebase()
    }

    private fun countCartFromFirebase() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Cart")
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        val cartModels : MutableList<Carrito_modelo> = ArrayList()
        databaseReference.child(uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children){
                        val cartModel = cartSnapshot.getValue(Carrito_modelo::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }
            })
    }

    private fun loadEstudioFromFirebase() {
        val estudioModelos : MutableList<Estudio_modelo> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Estudio")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(estudioSnapshot in snapshot.children){
                            val estudioModelo = estudioSnapshot.getValue(Estudio_modelo::class.java)
                            estudioModelo!!.key = estudioSnapshot.key
                            estudioModelos.add(estudioModelo)
                        }
                        estudioLoadListener.onLoadSucces(estudioModelos)
                    }
                    else
                        estudioLoadListener.onLoadFailed("Error,Estudio no existe")
                }
                override fun onCancelled(error: DatabaseError) {
                    estudioLoadListener.onLoadFailed(error.message)
                }
            })

    }
    private fun init(){
        estudioLoadListener = this
        cartLoadListener = this

        val gridLayoutManager = GridLayoutManager(this,1)
        estudio_rec.layoutManager = gridLayoutManager
        //estudio_rec.addItemDecoration(SpaceItemDecoration())
    }

    override fun onLoadSucces(estudioModelList: List<Estudio_modelo>?) {
        val adapter = Estudio_adapter(this,estudioModelList!!, cartLoadListener)
        estudio_rec.adapter = adapter
    }
    override fun onLoadFailed(message: String?) {
        Snackbar.make(findViewById(R.id.estudio_relativeLayout),message!!, Snackbar.LENGTH_LONG).show()
    }

    override fun onLoadCartSuccess(cartModelList: List<Carrito_modelo>) {
        var cartSum = 0
        for(cartModel in cartModelList!!) cartSum+= cartModel!!.quantity
        badge!!.setNumber(cartSum)

    }

    override fun onLoadCartFailed(message: String?) {

        Snackbar.make(findViewById(R.id.estudio_relativeLayout),message!!, Snackbar.LENGTH_LONG).show()
    }
}
