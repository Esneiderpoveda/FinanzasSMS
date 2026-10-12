package com.example.finanzasms

/**
 * Representa una transacción financiera detectada a partir de un SMS bancario.
 *
 * @param id Identificador único (timestamp en milisegundos).
 * @param monto Monto de la transacción en pesos colombianos (COP), siempre positivo.
 * @param tipo "INGRESO" o "EGRESO".
 * @param descripcion Texto corto descriptivo (ej. "Bancolombia - Pago").
 * @param fecha Fecha/hora en milisegundos (System.currentTimeMillis()).
 * @param remitente Nombre del remitente del SMS (ej. "Bancolombia").
 */
data class Transaccion(
    val id: Long,
    val monto: Long,
    val tipo: String,
    val descripcion: String,
    val fecha: Long,
    val remitente: String
)
