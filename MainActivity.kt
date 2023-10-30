package com.example.tpintegrador1_mjoloidovskyb

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    //para poder acceder a sharedPreferences fuera del onCreate
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)

        //PARA MOSTRAR POP UP AL PRINCIPIO DE LA APP
        mostrarPopupNombre()

        // IMC DE AHORA Y EL ULTIMO
        val buttonCalcular = findViewById<MaterialButton>(R.id.btnCalcular)
        val textViewUltimoIMC = findViewById<TextView>(R.id.tvUltimoIMC)

        val ultimoIMC = obtenerUltimoIMC()
        if (ultimoIMC >= 0) {
            textViewUltimoIMC.text = "Último IMC: $ultimoIMC"
        } else {
            textViewUltimoIMC.text = "No hay IMC anterior"
        }


        //Para no mostrar la explicación del IMC
        //val sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)
        val cardIMC = findViewById<MaterialCardView>(R.id.cardIMC)
        val btnCardIMC = findViewById<MaterialButton>(R.id.btnCardIMC)

        // Verificar si el usuario ha marcado "No volver a mostrar" previamente
        val noMostrar = sharedPreferences.getBoolean("noMostrar", false)

        if (noMostrar) {
            // Ocultar la MaterialCardView
            cardIMC.visibility = View.GONE
            btnCardIMC.visibility = View.GONE
        }

        btnCardIMC.setOnClickListener {
            // Guardar el estado en SharedPreferences
            sharedPreferences.edit().putBoolean("noMostrar", true).apply()

            // Ocultar la Card View y el Boton
            cardIMC.visibility = View.GONE
            btnCardIMC.visibility = View.GONE
        }



        //Calcular el IMC
        val etCalculo1 = findViewById<EditText>(R.id.etCalculo1)
        val etCalculo2 = findViewById<EditText>(R.id.etCalculo2)
        val btnCalcular = findViewById<MaterialButton>(R.id.btnCalcular)
        val textViewResultado = findViewById<TextView>(R.id.resultado)
        //ESTO ES PARA MOSTRAR EL MSJ DE FELICITACION O ALIENTO
        val textViewMensajeSalud = findViewById<TextView>(R.id.textViewMensajeSalud)


        btnCalcular.setOnClickListener {
            // Obtén los valores de los EditText como cadenas
            val numero1Str = etCalculo1.text.toString()
            val numero2Str = etCalculo2.text.toString()

            // Convierte las cadenas a números (si es posible)
            val numero1 = numero1Str.toDoubleOrNull()
            val numero2 = numero2Str.toDoubleOrNull()

            if (numero1 != null && numero2 != null && numero1 > 0 && numero2 > 0) {
                // Realiza el cálculo
                val resultado = numero1 / (numero2*numero2)
                val resultadoFormateado = String.format("%.2f", resultado)

                // Muestra el resultado en el TextView
                textViewResultado.text = "Resultado: $resultadoFormateado"

                // Verifica el rango del IMC y muestra el mensaje correspondiente
                val estadoSalud = when {
                    resultado in 18.5..24.5 -> "¡Sigue así!"
                    else -> "Deberías consultar a tu médico"
                }

                // Muestra el estado de salud en el TextView correspondiente
                textViewMensajeSalud.text = estadoSalud

                // Guarda el IMC en SharedPreferences
                guardarIMC(resultado)

            } else {
                // Muestra un mensaje de error si los números ingresados no son válidos
                textViewResultado.text = "Ingrese números válidos"

                // Limpia el mensaje de estado de salud
                textViewMensajeSalud.text = ""
            }
        }

        }



    //PARA QUE FUNCIONE EL POP UP - dsp lo muestro en OnCreate
    private fun mostrarPopupNombre() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_nombre_layout, null)
        val editTextNombre = dialogLayout.findViewById<EditText>(R.id.editTextNombre)

        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val nombre = editTextNombre.text.toString()
            mostrarSaludo(nombre)
            dialog.dismiss()
        }
        builder.show()
    }

    //MUESTRA EL NOMBRE EN ID textViewSaludo
    private fun mostrarSaludo(nombre: String) {
        val textViewSaludo = findViewById<TextView>(R.id.textViewSaludo)
        textViewSaludo.text = "Hola, $nombre"
    }

    private fun guardarIMC(imc: Double) {
        val editor = sharedPreferences.edit()
        editor.putFloat("ultimo_imc", imc.toFloat())
        editor.apply()
    }

    private fun obtenerUltimoIMC(): Float {
        return sharedPreferences.getFloat("ultimo_imc", -1f)
    }



    }




