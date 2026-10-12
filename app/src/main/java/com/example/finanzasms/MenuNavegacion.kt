package com.example.finanzasms

import android.app.Activity
import android.content.Intent
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu

object MenuNavegacion {

    fun mostrarMenu(
        actividad: Activity,
        botonMenu: TextView
    ) {

        val menu = PopupMenu(actividad, botonMenu)

        menu.menu.add("Inicio")
        menu.menu.add("Perfil")
        menu.menu.add("Comprobantes")
        menu.menu.add("Educación financiera")
        menu.menu.add("Mis finanzas")
        menu.menu.add("Herramientas")

        menu.setOnMenuItemClickListener { item ->

            val destino = when (item.title.toString()) {

                "Inicio" -> MainActivity::class.java

                "Perfil" -> PerfilActivity::class.java

                "Comprobantes" -> FotosActivity::class.java

                "Educación financiera" ->
                    EducacionActivity::class.java

                "Mis finanzas" -> FinanzasActivity::class.java

                "Herramientas" ->
                    HerramientasActivity::class.java

                else -> null
            }

            if (destino != null &&
                actividad.javaClass != destino
            ) {
                val intent = Intent(actividad, destino)

                // Evitar acumular copias de las pantallas
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                )

                actividad.startActivity(intent)
            }

            true
        }

        menu.show()
    }
}
