
package com.example.finanzasms

import java.text.Normalizer
import java.util.Locale

object IdentificadorEntidad {

    fun identificar(transaccion: Transaccion): String {

        // Revisar tanto el remitente como la descripción
        val texto = normalizar(
            transaccion.remitente + " " +
                    transaccion.descripcion
        )

        return when {

            "daviplata" in texto ||
                    "davi plata" in texto -> "DaviPlata"

            "nequi" in texto -> "Nequi"

            "bancolombia" in texto -> "Bancolombia"

            "davivienda" in texto -> "Davivienda"

            "bbva" in texto -> "BBVA"

            "colpatria" in texto ||
                    "scotiabank" in texto -> "Colpatria"

            "banco de bogota" in texto -> "Banco de Bogotá"

            "banco de occidente" in texto -> "Banco de Occidente"

            "banco popular" in texto -> "Banco Popular"

            "av villas" in texto ||
                    "avvillas" in texto -> "AV Villas"

            "movii" in texto -> "Movii"

            "dale!" in texto ||
                    "dale " in texto -> "Dale!"

            else -> "Otros"
        }
    }

    private fun normalizar(texto: String): String {

        val minusculas = texto.lowercase(Locale.ROOT)

        val sinTildes = Normalizer.normalize(
            minusculas,
            Normalizer.Form.NFD
        )

        return sinTildes.replace(
            Regex("\\p{M}+"),
            ""
        )
    }
}
