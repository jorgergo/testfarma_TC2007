package com.example.testfarma_app.listener

import com.example.testfarma_app.modelo.Estudio_modelo

interface IDEstudioListener {
    fun onLoadSucces(estudioModelList: List<Estudio_modelo>?)
    fun onLoadFailed(message:String?)
}