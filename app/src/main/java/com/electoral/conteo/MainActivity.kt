package com.electoral.conteo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre1: TextInputEditText
    private lateinit var etPartido1: TextInputEditText
    private lateinit var etNombre2: TextInputEditText
    private lateinit var etPartido2: TextInputEditText
    private lateinit var etNombre3: TextInputEditText
    private lateinit var etPartido3: TextInputEditText

    private lateinit var layoutVotos1: LinearLayout
    private lateinit var layoutVotos2: LinearLayout
    private lateinit var layoutVotos3: LinearLayout

    private lateinit var tvNombreVotos1: TextView
    private lateinit var tvNombreVotos2: TextView
    private lateinit var tvNombreVotos3: TextView

    private lateinit var tvContador1: TextView
    private lateinit var tvContador2: TextView
    private lateinit var tvContador3: TextView

    private lateinit var btnVotar1: Button
    private lateinit var btnVotar2: Button
    private lateinit var btnVotar3: Button

    private lateinit var btnIniciar: Button
    private lateinit var btnEditar: Button
    private lateinit var btnResultados: Button
    private lateinit var btnReiniciar: Button

    private lateinit var tvTotalVotos: TextView
    private lateinit var candidatos: List<Candidato>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializarVistas()
        inicializarCandidatos()
        configurarBotones()
    }

    private fun inicializarVistas() {
        etNombre1  = findViewById(R.id.etNombre1)
        etPartido1 = findViewById(R.id.etPartido1)
        etNombre2  = findViewById(R.id.etNombre2)
        etPartido2 = findViewById(R.id.etPartido2)
        etNombre3  = findViewById(R.id.etNombre3)
        etPartido3 = findViewById(R.id.etPartido3)
        layoutVotos1 = findViewById(R.id.layoutVotos1)
        layoutVotos2 = findViewById(R.id.layoutVotos2)
        layoutVotos3 = findViewById(R.id.layoutVotos3)
        tvNombreVotos1 = findViewById(R.id.tvNombreVotos1)
        tvNombreVotos2 = findViewById(R.id.tvNombreVotos2)
        tvNombreVotos3 = findViewById(R.id.tvNombreVotos3)
        tvContador1 = findViewById(R.id.tvContador1)
        tvContador2 = findViewById(R.id.tvContador2)
        tvContador3 = findViewById(R.id.tvContador3)
        btnVotar1 = findViewById(R.id.btnVotar1)
        btnVotar2 = findViewById(R.id.btnVotar2)
        btnVotar3 = findViewById(R.id.btnVotar3)
        btnIniciar    = findViewById(R.id.btnIniciar)
        btnEditar     = findViewById(R.id.btnEditar)
        btnResultados = findViewById(R.id.btnResultados)
        btnReiniciar  = findViewById(R.id.btnReiniciar)
        tvTotalVotos  = findViewById(R.id.tvTotalVotos)
    }

    private fun inicializarCandidatos() {
        candidatos = listOf(
            Candidato(getString(R.string.default_nombre1), getString(R.string.default_partido1)),
            Candidato(getString(R.string.default_nombre2), getString(R.string.default_partido2)),
            Candidato(getString(R.string.default_nombre3), getString(R.string.default_partido3))
        )
    }

    private fun configurarBotones() {
        btnIniciar.setOnClickListener    { onIniciarConteo() }
        btnEditar.setOnClickListener     { onEditarCandidatos() }
        btnVotar1.setOnClickListener     { onRegistrarVoto(0) }
        btnVotar2.setOnClickListener     { onRegistrarVoto(1) }
        btnVotar3.setOnClickListener     { onRegistrarVoto(2) }
        btnResultados.setOnClickListener { onVerResultados() }
        btnReiniciar.setOnClickListener  { onConfirmarReinicio() }
    }

    private fun onIniciarConteo() {
        val nombre1  = etNombre1.text.toString().trim()
        val partido1 = etPartido1.text.toString().trim()
        val nombre2  = etNombre2.text.toString().trim()
        val partido2 = etPartido2.text.toString().trim()
        val nombre3  = etNombre3.text.toString().trim()
        val partido3 = etPartido3.text.toString().trim()

        if (nombre1.isEmpty() || partido1.isEmpty() ||
            nombre2.isEmpty() || partido2.isEmpty() ||
            nombre3.isEmpty() || partido3.isEmpty()) {
            mostrarAlerta("Campos incompletos",
                "Por favor completa el nombre y partido de todos los candidatos.")
            return
        }
        candidatos[0].nombre = nombre1;  candidatos[0].partido = partido1
        candidatos[1].nombre = nombre2;  candidatos[1].partido = partido2
        candidatos[2].nombre = nombre3;  candidatos[2].partido = partido3
        activarModoVotacion()
    }

    private fun onEditarCandidatos() {
        if (candidatos.sumOf { it.votos } > 0) {
            mostrarAlerta("No es posible editar",
                "Ya hay votos registrados. Reinicia el conteo para cambiar los candidatos.")
            return
        }
        activarModoEdicion()
    }

    private fun onRegistrarVoto(indice: Int) {
        candidatos[indice].registrarVoto()
        actualizarContadores()
    }

    private fun onVerResultados() {
        val total = candidatos.sumOf { it.votos }
        if (total == 0) { mostrarAlerta("Sin votos", "No se han registrado votos aún."); return }
        mostrarDialogoResultados(total)
    }

    private fun onConfirmarReinicio() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar reinicio")
            .setMessage("¿Desea reiniciar el conteo? Se perderán todos los votos.")
            .setPositiveButton("Sí, reiniciar") { _, _ -> reiniciarConteo() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun activarModoVotacion() {
        setFieldsEnabled(false)
        tvNombreVotos1.text = candidatos[0].nombre
        tvNombreVotos2.text = candidatos[1].nombre
        tvNombreVotos3.text = candidatos[2].nombre
        layoutVotos1.visibility = View.VISIBLE
        layoutVotos2.visibility = View.VISIBLE
        layoutVotos3.visibility = View.VISIBLE
        btnIniciar.visibility    = View.GONE
        btnEditar.visibility     = View.VISIBLE
        btnResultados.visibility = View.VISIBLE
        btnReiniciar.visibility  = View.VISIBLE
        tvTotalVotos.visibility  = View.VISIBLE
        actualizarContadores()
    }

    private fun activarModoEdicion() {
        setFieldsEnabled(true)
        layoutVotos1.visibility = View.GONE
        layoutVotos2.visibility = View.GONE
        layoutVotos3.visibility = View.GONE
        btnIniciar.visibility    = View.VISIBLE
        btnEditar.visibility     = View.GONE
        btnResultados.visibility = View.GONE
        btnReiniciar.visibility  = View.GONE
        tvTotalVotos.visibility  = View.GONE
    }

    private fun setFieldsEnabled(enabled: Boolean) {
        etNombre1.isEnabled = enabled;  etPartido1.isEnabled = enabled
        etNombre2.isEnabled = enabled;  etPartido2.isEnabled = enabled
        etNombre3.isEnabled = enabled;  etPartido3.isEnabled = enabled
    }

    private fun actualizarContadores() {
        tvContador1.text = candidatos[0].votos.toString()
        tvContador2.text = candidatos[1].votos.toString()
        tvContador3.text = candidatos[2].votos.toString()
        tvTotalVotos.text = "Total de votos emitidos: ${candidatos.sumOf { it.votos }}"
    }

    private fun reiniciarConteo() {
        candidatos.forEach { it.reiniciarVotos() }
        activarModoEdicion()
    }

    private fun mostrarDialogoResultados(totalVotos: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_resultados, null)
        val nombres     = listOf(R.id.tvResultNombre1,     R.id.tvResultNombre2,     R.id.tvResultNombre3)
        val partidos    = listOf(R.id.tvResultPartido1,    R.id.tvResultPartido2,    R.id.tvResultPartido3)
        val votos       = listOf(R.id.tvResultVotos1,      R.id.tvResultVotos2,      R.id.tvResultVotos3)
        val porcentajes = listOf(R.id.tvResultPorcentaje1, R.id.tvResultPorcentaje2, R.id.tvResultPorcentaje3)
        val barras      = listOf(R.id.progressBar1,        R.id.progressBar2,        R.id.progressBar3)

        candidatos.forEachIndexed { i, c ->
            val pct = c.calcularPorcentaje(totalVotos)
            dialogView.findViewById<TextView>(nombres[i]).text     = c.nombre
            dialogView.findViewById<TextView>(partidos[i]).text    = c.partido
            dialogView.findViewById<TextView>(votos[i]).text       = "${c.votos} votos"
            dialogView.findViewById<TextView>(porcentajes[i]).text = String.format("%.1f%%", pct)
            dialogView.findViewById<ProgressBar>(barras[i]).progress = pct.toInt()
        }

        val maxVotos  = candidatos.maxOf { it.votos }
        val ganadores = candidatos.filter { it.votos == maxVotos }
        val tvNG  = dialogView.findViewById<TextView>(R.id.tvNombreGanador)
        val tvPG  = dialogView.findViewById<TextView>(R.id.tvPartidoGanador)
        val tvVG  = dialogView.findViewById<TextView>(R.id.tvVotosGanador)
        val layG  = dialogView.findViewById<LinearLayout>(R.id.layoutGanador)

        if (ganadores.size == 1) {
            val g = ganadores[0]
            tvNG.text = g.nombre
            tvPG.text = g.partido
            tvVG.text = "${g.votos} votos (${String.format("%.1f", g.calcularPorcentaje(totalVotos))}%)"
        } else {
            tvNG.text = "🤝 Empate técnico"
            tvPG.text = ganadores.joinToString(" — ") { it.nombre }
            tvVG.text = "$maxVotos votos cada uno"
            layG.setBackgroundColor(getColor(R.color.colorBotonIniciar))
        }
        dialogView.findViewById<TextView>(R.id.tvDialogTotalVotos).text =
            "Total de votos emitidos: $totalVotos"

        AlertDialog.Builder(this).setView(dialogView).setPositiveButton("Cerrar", null).show()
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        AlertDialog.Builder(this).setTitle(titulo).setMessage(mensaje)
            .setPositiveButton("Aceptar", null).show()
    }
}
