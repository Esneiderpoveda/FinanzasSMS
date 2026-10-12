
package com.example.finanzasms

import android.content.Context
import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object AlmacenamientoFacturas {

    private const val PREFS = "facturas_prefs"
    private const val CLAVE_FACTURAS = "lista_facturas"

    // Copiar la fotografía al almacenamiento privado
    fun guardarImagen(
        context: Context,
        uri: Uri
    ): String {

        val carpeta = File(context.filesDir, "facturas")

        if (!carpeta.exists()) {
            carpeta.mkdirs()
        }

        val archivo = File(
            carpeta,
            "factura_${System.nanoTime()}.jpg"
        )

        context.contentResolver.openInputStream(uri).use { entrada ->

            requireNotNull(entrada) {
                "No se pudo abrir la imagen"
            }

            archivo.outputStream().use { salida ->
                entrada.copyTo(salida)
            }
        }

        return Uri.fromFile(archivo).toString()
    }

    // Guardar los datos de todas las facturas
    fun guardarFacturas(
        context: Context,
        facturas: List<Factura>
    ) {

        val arreglo = JSONArray()

        facturas.forEach { factura ->

            val objeto = JSONObject().apply {
                put("id", factura.id)
                put("imagenUri", factura.imagenUri)
                put("nota", factura.nota)
                put("fecha", factura.fecha)
            }

            arreglo.put(objeto)
        }

        context.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        ).edit()
            .putString(CLAVE_FACTURAS, arreglo.toString())
            .apply()
    }

    // Recuperar las facturas al abrir la aplicación
    fun cargarFacturas(context: Context): MutableList<Factura> {

        val resultado = mutableListOf<Factura>()

        val datos = context.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        ).getString(CLAVE_FACTURAS, "[]") ?: "[]"

        val arreglo = JSONArray(datos)

        for (i in 0 until arreglo.length()) {

            val objeto = arreglo.getJSONObject(i)

            resultado.add(
                Factura(
                    id = objeto.getLong("id"),
                    imagenUri = objeto.getString("imagenUri"),
                    nota = objeto.getString("nota"),
                    fecha = objeto.getLong("fecha")
                )
            )
        }

        return resultado
    }
}
