package com.example.finanzasms

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

/**
 * Maneja toda la persistencia local de la app: saldo acumulado e historial
 * de transacciones. Usa SharedPreferences con un arreglo JSON serializado
 * como historial, evitando dependencias externas como Room.
 */
object Almacenamiento {

    private const val PREFS_NAME = "finanzas_prefs"
    private const val KEY_SALDO = "saldo_total"
    private const val KEY_HISTORIAL = "historial_transacciones"
    private const val MAX_HISTORIAL = 200

    private fun prefs(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun obtenerSaldo(context: Context): Long {
        return prefs(context).getLong(KEY_SALDO, 0L)
    }

    fun establecerSaldo(context: Context, nuevoSaldo: Long) {
        prefs(context).edit().putLong(KEY_SALDO, nuevoSaldo).apply()
    }

    fun resetear(context: Context) {
        prefs(context).edit()
            .putLong(KEY_SALDO, 0L)
            .putString(KEY_HISTORIAL, JSONArray().toString())
            .apply()
    }

    /**
     * Registra una nueva transacción: actualiza el saldo acumulado (sumando
     * si es INGRESO, restando si es EGRESO) y agrega el registro al historial.
     */
    fun registrarTransaccion(context: Context, transaccion: Transaccion) {
        val saldoActual = obtenerSaldo(context)
        val nuevoSaldo = if (transaccion.tipo == "INGRESO") {
            saldoActual + transaccion.monto
        } else {
            saldoActual - transaccion.monto
        }

        val historial = obtenerHistorialJson(context)
        historial.put(transaccionAJson(transaccion))

        // Limitar el tamaño del historial para no crecer indefinidamente
        val historialRecortado = if (historial.length() > MAX_HISTORIAL) {
            val recortado = JSONArray()
            val inicio = historial.length() - MAX_HISTORIAL
            for (i in inicio until historial.length()) {
                recortado.put(historial.getJSONObject(i))
            }
            recortado
        } else {
            historial
        }

        prefs(context).edit()
            .putLong(KEY_SALDO, nuevoSaldo)
            .putString(KEY_HISTORIAL, historialRecortado.toString())
            .apply()
    }

    /**
     * Devuelve el historial de transacciones ordenado de la más reciente
     * a la más antigua.
     */
    fun obtenerHistorial(context: Context): List<Transaccion> {
        val json = obtenerHistorialJson(context)
        val lista = mutableListOf<Transaccion>()
        for (i in 0 until json.length()) {
            val obj = json.getJSONObject(i)
            lista.add(
                Transaccion(
                    id = obj.getLong("id"),
                    monto = obj.getLong("monto"),
                    tipo = obj.getString("tipo"),
                    descripcion = obj.getString("descripcion"),
                    fecha = obj.getLong("fecha"),
                    remitente = obj.getString("remitente")
                )
            )
        }
        return lista.sortedByDescending { it.fecha }
    }

    private fun obtenerHistorialJson(context: Context): JSONArray {
        val texto = prefs(context).getString(KEY_HISTORIAL, null) ?: return JSONArray()
        return try {
            JSONArray(texto)
        } catch (e: Exception) {
            JSONArray()
        }
    }

    private fun transaccionAJson(t: Transaccion): JSONObject {
        val obj = JSONObject()
        obj.put("id", t.id)
        obj.put("monto", t.monto)
        obj.put("tipo", t.tipo)
        obj.put("descripcion", t.descripcion)
        obj.put("fecha", t.fecha)
        obj.put("remitente", t.remitente)
        return obj
    }
}