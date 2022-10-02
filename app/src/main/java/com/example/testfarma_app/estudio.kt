package com.example.testfarma_app
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testfarma_app.Estudio_adapter
import com.example.testfarma_app.R
import com.example.testfarma_app.listener.IDEstudioListener
import com.example.testfarma_app.modelo.Estudio_modelo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_estudio.*


class estudio : AppCompatActivity(), IDEstudioListener {
    lateinit var estudioLoadListener: IDEstudioListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudio)
        init()
        loadEstudioFromFirebase()
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
        val gridLayoutManager = GridLayoutManager(this,1)
        estudio_rec.layoutManager = gridLayoutManager
        //estudio_rec.addItemDecoration(SpaceItemDecoration())
    }

    override fun onLoadSucces(estudioModelList: List<Estudio_modelo>?) {
        val adapter = Estudio_adapter(this,estudioModelList!!)
        estudio_rec.adapter = adapter
    }
    override fun onLoadFailed(message: String?) {
        Snackbar.make(findViewById(R.id.estudio_relativeLayout),message!!, Snackbar.LENGTH_LONG).show()
    }
}
