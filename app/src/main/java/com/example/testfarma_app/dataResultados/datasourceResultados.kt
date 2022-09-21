package com.example.testfarma_app.dataResultados

import com.example.testfarma_app.R
import com.example.testfarma_app.resultadosModel.resultadosArchivos

class datasourceResultados {
    fun loadResultados():List<resultadosArchivos>{
        return listOf<resultadosArchivos>(
            resultadosArchivos(R.string.descarga_pdf),
            resultadosArchivos(R.string.descarga_pdf),
            resultadosArchivos(R.string.descarga_pdf),
            resultadosArchivos(R.string.descarga_pdf),
            resultadosArchivos(R.string.descarga_pdf)
        )

    }
}