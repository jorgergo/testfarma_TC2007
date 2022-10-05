package com.example.testfarma_app.listener

import com.example.testfarma_app.modelo.Carrito_modelo

interface CarritoLoadListener {
    fun onLoadCartSuccess(cartModelList : List<Carrito_modelo>)
    fun onLoadCartFailed(message:String?)
}