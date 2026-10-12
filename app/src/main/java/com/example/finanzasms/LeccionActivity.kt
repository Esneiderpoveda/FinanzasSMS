package com.example.finanzasms

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LeccionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leccion)

        // Referencias de la pantalla
        val botonVolver = findViewById<TextView>(
            R.id.botonVolverLeccion
        )

        val numeroLeccion = findViewById<TextView>(
            R.id.textNumeroLeccion
        )

        val titulo = findViewById<TextView>(
            R.id.textTituloLeccion
        )

        val contenido = findViewById<TextView>(
            R.id.textContenidoLeccion
        )

        val ejemplo = findViewById<TextView>(
            R.id.textEjemploLeccion
        )

        val pregunta = findViewById<TextView>(
            R.id.textPreguntaLeccion
        )

        val grupoRespuestas = findViewById<RadioGroup>(
            R.id.grupoRespuestas
        )

        val respuestaUno = findViewById<RadioButton>(
            R.id.respuestaUno
        )

        val respuestaDos = findViewById<RadioButton>(
            R.id.respuestaDos
        )

        val respuestaTres = findViewById<RadioButton>(
            R.id.respuestaTres
        )

        val botonComprobar = findViewById<Button>(
            R.id.botonComprobarRespuesta
        )

        val resultado = findViewById<TextView>(
            R.id.textResultadoLeccion
        )

        botonVolver.setOnClickListener {
            finish()
        }

        // Identificar qué lección seleccionó el usuario
        val leccionSeleccionada = intent.getIntExtra(
            "numeroLeccion",
            1
        )

        // Cargar el contenido correspondiente
        when (leccionSeleccionada) {


            4 -> {
                numeroLeccion.text = "LECCIÓN 4"
                titulo.text = "Introducción a las inversiones"

                contenido.text = """
                    ¿QUÉ ES INVERTIR?

                    Invertir significa destinar dinero a un activo
                    con la expectativa de obtener una rentabilidad
                    en el futuro.

                    A diferencia del ahorro, una inversión puede
                    aumentar o disminuir de valor.

                    CONCEPTOS IMPORTANTES

                    • Capital: dinero que invertimos inicialmente.

                    • Rentabilidad: ganancia o pérdida que
                      obtenemos de una inversión.

                    • Riesgo: posibilidad de perder parte
                      o la totalidad del dinero invertido.

                    • Diversificación: distribuir el dinero
                      entre diferentes inversiones para
                      reducir la concentración del riesgo.

                    ¿QUÉ ES EL INTERÉS COMPUESTO?

                    Es el efecto de generar rendimientos
                    sobre el capital inicial y también
                    sobre los rendimientos acumulados.

                    CONSEJOS PARA COMENZAR

                    • Aprende antes de invertir.
                    • No inviertas dinero que necesitas
                      para gastos esenciales.
                    • Conoce los riesgos.
                    • Desconfía de las ganancias garantizadas.
                    • Piensa en tus objetivos y plazos.
                """.trimIndent()

                ejemplo.text = """
                    📈 EJEMPLO PRÁCTICO

                    Imagina que inviertes $1.000.000 COP.

                    Supongamos una rentabilidad
                    hipotética del 10% anual.

                    Después del primer año:

                    Capital: $1.000.000
                    Rendimiento: $100.000

                    Total: $1.100.000 COP

                    Si reinviertes todo y obtienes
                    otro 10% el segundo año:

                    Total: $1.210.000 COP

                    Este ejemplo es ilustrativo.
                    La rentabilidad real no está garantizada
                    y no incluye impuestos ni comisiones.
                """.trimIndent()

                pregunta.text = """
                    Comprueba lo aprendido:

                    Si inviertes $2.000.000 COP
                    y obtienes una rentabilidad
                    del 10% durante un año,
                    ¿cuánto tendrás al finalizar,
                    sin comisiones ni impuestos?
                """.trimIndent()

                respuestaUno.text = "$2.100.000 COP"
                respuestaDos.text = "$2.200.000 COP"
                respuestaTres.text = "$2.400.000 COP"
            }




            3 -> {
                numeroLeccion.text = "LECCIÓN 3"
                titulo.text = "Créditos y deudas"

                contenido.text = """
                    ¿QUÉ ES UN CRÉDITO?

                    Un crédito es dinero que una entidad financiera presta y que debemos devolver según unas condiciones acordadas.

                    Generalmente, además del dinero prestado, debemos pagar intereses.

                    ¿QUÉ SON LOS INTERESES?

                    Son el coste de utilizar dinero prestado.

                    Antes de solicitar un crédito es importante conocer:

                    • La tasa de interés.
                    • El plazo de devolución.
                    • El valor de las cuotas.
                    • Las comisiones y otros gastos.
                    • El importe total que terminaremos pagando.

                    ¿CÓMO EVITAR EL SOBREENDEUDAMIENTO?

                    • No solicites créditos innecesarios.
                    • Compara diferentes ofertas.
                    • Revisa tu capacidad de pago.
                    • Evita utilizar nuevos créditos para pagar deudas anteriores sin un plan sostenible.
                    • Paga puntualmente tus obligaciones.

                    Recuerda: una cuota pequeña no significa necesariamente que un crédito sea barato.
                """.trimIndent()

                ejemplo.text = """
                    💳 EJEMPLO PRÁCTICO

                    Imagina que solicitas un préstamo de:

                    $1.000.000 COP

                    Para simplificar, supongamos que se aplica un interés simple del 10% anual durante un año, sin comisiones.

                    Intereses:
                    $100.000 COP

                    Total que devolverías:
                    $1.100.000 COP

                    En un crédito real, el cálculo puede variar según las condiciones, las cuotas y el sistema de amortización.
                """.trimIndent()

                pregunta.text = """
                    Comprueba lo aprendido:

                    Si solicitas un préstamo de
                    $2.000.000 COP con un interés
                    simple del 10% anual durante
                    un año, sin comisiones,
                    ¿cuánto devolverías en total?
                """.trimIndent()

                respuestaUno.text = "$2.100.000 COP"
                respuestaDos.text = "$2.200.000 COP"
                respuestaTres.text = "$2.400.000 COP"
            }



            2 -> {
                numeroLeccion.text = "LECCIÓN 2"
                titulo.text = "Ahorro inteligente"

                contenido.text = """
                    ¿QUÉ ES EL AHORRO?

                    Ahorrar consiste en reservar una parte de nuestros ingresos para utilizarla en el futuro.

                    Ahorrar nos ayuda a:

                    • Afrontar gastos inesperados.
                    • Alcanzar metas personales.
                    • Evitar depender de créditos.
                    • Tener mayor tranquilidad económica.

                    FONDO DE EMERGENCIA

                    Es dinero reservado para situaciones imprevistas, como una reparación urgente o la pérdida del empleo.

                    Una referencia habitual es ahorrar el equivalente a entre 3 y 6 meses de gastos esenciales, adaptándolo a cada situación.

                    CONSEJOS PARA AHORRAR

                    • Establece una meta concreta.
                    • Separa el ahorro al recibir tus ingresos.
                    • Evita gastos innecesarios.
                    • Revisa tu progreso cada mes.
                """.trimIndent()

                ejemplo.text = """
                    🐷 EJEMPLO PRÁCTICO

                    Imagina que decides ahorrar
                    $150.000 COP cada mes.

                    Después de 6 meses:
                    $900.000 COP

                    Después de 12 meses:
                    $1.800.000 COP

                    Este ejemplo no incluye intereses ni rendimientos.
                """.trimIndent()


                pregunta.text = """
    Comprueba lo aprendido:

    Si ahorras $150.000 COP cada mes,
    ¿cuánto dinero habrás acumulado
    después de 24 meses?
""".trimIndent()

                respuestaUno.text = "$3.200.000 COP"
                respuestaDos.text = "$3.600.000 COP"
                respuestaTres.text = "$4.200.000 COP"

            }

            else -> {
                // LECCIÓN 1: PRESUPUESTO PERSONAL
                numeroLeccion.text = "LECCIÓN 1"
                titulo.text = "Presupuesto personal"

                contenido.text = """
                    ¿Qué es un presupuesto personal?

                    Es una herramienta que permite organizar el dinero que recibimos y el dinero que gastamos.

                    Un presupuesto nos ayuda a:

                    • Conocer nuestros ingresos.
                    • Identificar nuestros gastos.
                    • Evitar gastar más de lo que recibimos.
                    • Establecer metas de ahorro.

                    Una estrategia conocida es la regla 50/30/20:

                    50% para necesidades básicas.
                    30% para gustos y deseos.
                    20% para ahorro o pago adicional de deudas.

                    Esta regla es una referencia y puede adaptarse a cada situación económica.
                """.trimIndent()

                ejemplo.text = """
                    💡 EJEMPLO PRÁCTICO

                    Imagina que recibes $2.000.000 COP al mes.

                    Aplicando la regla 50/30/20:

                    🏠 Necesidades (50%): $1.000.000

                    🛍️ Gustos (30%): $600.000

                    🐷 Ahorro (20%): $400.000

                    Total distribuido: $2.000.000 COP
                """.trimIndent()

                pregunta.text = """
                    Comprueba lo aprendido:

                    Si recibes $2.000.000 COP al mes,
                    ¿cuánto corresponde al 20%?
                """.trimIndent()

                respuestaUno.text = "$200.000 COP"
                respuestaDos.text = "$400.000 COP"
                respuestaTres.text = "$800.000 COP"
            }
        }

        // Comprobar la respuesta seleccionada
        botonComprobar.setOnClickListener {

            when (grupoRespuestas.checkedRadioButtonId) {

                R.id.respuestaDos -> {
                    resultado.text =
                        "✅ ¡Correcto! Has comprendido esta lección."

                    resultado.setTextColor(
                        Color.rgb(46, 125, 50)
                    )
                }

                -1 -> {
                    resultado.text =
                        "Selecciona una respuesta antes de continuar."

                    resultado.setTextColor(Color.DKGRAY)
                }

                else -> {
                    resultado.text =
                        "❌ Inténtalo otra vez. Revisa el ejemplo práctico."

                    resultado.setTextColor(
                        Color.rgb(198, 40, 40)
                    )
                }
            }
        }

        grupoRespuestas.setOnCheckedChangeListener { _, _ ->
            resultado.text = ""
        }
    }
}
