package com.example.finanzasms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import java.util.Locale
import java.util.regex.Pattern

/**
 * Intercepta los SMS entrantes, detecta si provienen de una notificación
 * bancaria colombiana y, de ser así, extrae el monto y lo clasifica como
 * INGRESO o EGRESO, actualizando el saldo persistente.
 */
class SmsReceiver : BroadcastReceiver() {

    companion object {
        // Patrón para montos en pesos colombianos: $25.000 / $ 150.000,00 / $1.000
        private val PATRON_MONTO: Pattern = Pattern.compile(
            "\\$\\s?([0-9]{1,3}(?:\\.[0-9]{3})*(?:,[0-9]{1,2})?)"
        )

        private val PALABRAS_EGRESO = listOf(
            "compra", "compraste", "enviaste", "retiro", "retiraste",
            "pago", "pagaste", "transferiste", "débito", "debito",
            "cargo", "compra por"
        )

        private val PALABRAS_INGRESO = listOf(
            "recepción", "recepcion", "transfirieron", "recibiste",
            "abono", "crédito", "credito", "consignación", "consignacion",
            "te transfirieron", "recibiste una transferencia"
        )

        private val REMITENTES_BANCARIOS = listOf(
            "bancolombia", "nequi", "daviplata", "banco de bogota",
            "banco de bogotá", "davivienda", "bbva", "banco popular",
            "colpatria", "scotiabank", "banco caja social", "av villas"
        )
    }

    override fun onReceive(context: Context, intent: Intent) {
        android.util.Log.d("FINANZAS_SMS", "El receptor se ha ejecutado")
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val mensajes = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        if (mensajes.isNullOrEmpty()) return

        for (mensaje in mensajes) {
            val cuerpo = mensaje.messageBody ?: continue
            val remitente = mensaje.originatingAddress ?: ""

            if (!pareceMensajeBancario(cuerpo, remitente)) continue

            val monto = extraerMonto(cuerpo) ?: continue
            val tipo = clasificarTipo(cuerpo) ?: continue
            val nombreRemitente = identificarBanco(cuerpo, remitente)

            val transaccion = Transaccion(
                id = System.currentTimeMillis(),
                monto = monto,
                tipo = tipo,
                descripcion = construirDescripcion(cuerpo, tipo),
                fecha = System.currentTimeMillis(),
                remitente = nombreRemitente
            )

            Almacenamiento.registrarTransaccion(context, transaccion)
        }
    }

    /**
     * Verifica que el mensaje realmente parezca una notificación bancaria:
     * debe contener el nombre de un banco conocido o al menos un símbolo de
     * moneda junto con una palabra clave de ingreso/egreso.
     */
    private fun pareceMensajeBancario(cuerpo: String, remitente: String): Boolean {
        val textoNormalizado = cuerpo.lowercase(Locale.getDefault())
        val remitenteNormalizado = remitente.lowercase(Locale.getDefault())

        val bancoEncontrado = REMITENTES_BANCARIOS.any {
            textoNormalizado.contains(it) || remitenteNormalizado.contains(it)
        }

        val tieneMonto = PATRON_MONTO.matcher(cuerpo).find()
        val tienePalabraClave = PALABRAS_EGRESO.any { textoNormalizado.contains(it) } ||
            PALABRAS_INGRESO.any { textoNormalizado.contains(it) }

        return tieneMonto && (bancoEncontrado || tienePalabraClave)
    }

    /**
     * Extrae el primer monto en pesos colombianos encontrado en el texto y
     * lo convierte a un Long (valor entero en pesos, sin centavos).
     */
    private fun extraerMonto(cuerpo: String): Long? {
        val matcher = PATRON_MONTO.matcher(cuerpo)
        if (!matcher.find()) return null

        val crudo = matcher.group(1) ?: return null
        // Quitar separador de miles (.) y quedarnos solo con la parte entera
        // antes de una eventual coma decimal.
        val sinMiles = crudo.replace(".", "")
        val parteEntera = sinMiles.substringBefore(",")

        return parteEntera.toLongOrNull()
    }

    /**
     * Determina si el mensaje corresponde a un INGRESO o un EGRESO según
     * las palabras clave presentes. Si encuentra ambas, prioriza la que
     * aparece primero en el texto.
     */
    private fun clasificarTipo(cuerpo: String): String? {
        val textoNormalizado = cuerpo.lowercase(Locale.getDefault())

        val posicionEgreso = PALABRAS_EGRESO
            .mapNotNull { palabra ->
                val idx = textoNormalizado.indexOf(palabra)
                if (idx >= 0) idx else null
            }
            .minOrNull()

        val posicionIngreso = PALABRAS_INGRESO
            .mapNotNull { palabra ->
                val idx = textoNormalizado.indexOf(palabra)
                if (idx >= 0) idx else null
            }
            .minOrNull()

        return when {
            posicionEgreso != null && posicionIngreso != null ->
                if (posicionEgreso <= posicionIngreso) "EGRESO" else "INGRESO"
            posicionEgreso != null -> "EGRESO"
            posicionIngreso != null -> "INGRESO"
            else -> null
        }
    }

    private fun identificarBanco(cuerpo: String, remitente: String): String {
        val textoNormalizado = cuerpo.lowercase(Locale.getDefault())
        val remitenteNormalizado = remitente.lowercase(Locale.getDefault())

        for (banco in REMITENTES_BANCARIOS) {
            if (textoNormalizado.contains(banco) || remitenteNormalizado.contains(banco)) {
                return banco.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            }
        }
        return if (remitente.isNotBlank()) remitente else "Desconocido"
    }

    private fun construirDescripcion(cuerpo: String, tipo: String): String {
        val etiqueta = if (tipo == "INGRESO") "Ingreso detectado" else "Egreso detectado"
        val resumen = cuerpo.take(60).trim()
        return "$etiqueta: $resumen"
    }
}