package com.example.puerchambudjohanna_calculadoravlsm

import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow

data class Subred(
    val direccionRed: String,
    val broadcast: String,
    val primerHost: String,
    val ultimoHost: String,
    val mascara: String,
    val hostsDisponibles: Int
)

object VlsmUtils {

    fun calcularSubredes(ip: String, mascaraInicial: Int, cantidadSubredes: Int): List<Subred> {
        val resultado = mutableListOf<Subred>()

        val bitsNecesarios = ceil(log2(cantidadSubredes.toDouble())).toInt()
        val nuevaMascara = mascaraInicial + bitsNecesarios
        if (nuevaMascara > 30) return emptyList()

        val totalHosts = 2.0.pow((32 - nuevaMascara).toDouble()).toInt()
        val bloques = totalHosts
        val ipNumerica = ipToInt(ip)

        for (i in 0 until cantidadSubredes) {
            val direccionRed = ipNumerica + (i * bloques)
            val broadcast = direccionRed + bloques - 1

            resultado.add(
                Subred(
                    direccionRed = intToIp(direccionRed),
                    broadcast = intToIp(broadcast),
                    primerHost = intToIp(direccionRed + 1),
                    ultimoHost = intToIp(broadcast - 1),
                    mascara = nuevaMascara.toString(),
                    hostsDisponibles = bloques - 2
                )
            )
        }

        return resultado
    }

    private fun ipToInt(ip: String): Int {
        val octetos = ip.split(".")
        return (octetos[0].toInt() shl 24) or
                (octetos[1].toInt() shl 16) or
                (octetos[2].toInt() shl 8) or
                octetos[3].toInt()
    }

    private fun intToIp(ip: Int): String {
        return "${(ip shr 24) and 0xFF}." +
                "${(ip shr 16) and 0xFF}." +
                "${(ip shr 8) and 0xFF}." +
                "${ip and 0xFF}"
    }
}

