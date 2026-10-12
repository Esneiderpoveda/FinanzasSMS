package com.example.finanzasms

data class Factura(
    val id: Long,
    val imagenUri: String,
    val nota: String,
    val fecha: Long
)