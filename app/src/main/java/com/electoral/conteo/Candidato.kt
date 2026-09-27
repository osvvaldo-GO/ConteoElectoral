package com.electoral.conteo

data class Candidato(
    var nombre: String,
    var partido: String,
    var votos: Int = 0
) {
    fun registrarVoto() {
        votos++
    }

    fun reiniciarVotos() {
        votos = 0
    }

    fun calcularPorcentaje(totalVotos: Int): Double {
        return if (totalVotos == 0) 0.0
        else (votos.toDouble() / totalVotos.toDouble()) * 100.0
    }

    override fun toString(): String {
        return "$nombre ($partido): $votos votos"
    }
}
