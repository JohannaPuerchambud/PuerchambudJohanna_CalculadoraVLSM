package com.example.puerchambudjohanna_calculadoravlsm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardOptions
import com.example.puerchambudjohanna_calculadoravlsm.ui.theme.PuerchambudJohanna_CalculadoraVLSMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PuerchambudJohanna_CalculadoraVLSMTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    VlsmCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun VlsmCalculatorScreen() {
    var ip by remember { mutableStateOf("") }
    var mask by remember { mutableStateOf("") }
    var subnets by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf<List<Subred>>(emptyList()) }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Calculadora VLSM", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = ip,
            onValueChange = { ip = it },
            label = { Text("Dirección IP") },
            placeholder = { Text("Ej: 192.168.1.0") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = mask,
            onValueChange = { mask = it },
            label = { Text("Máscara (CIDR)") },
            placeholder = { Text("Ej: 24") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = subnets,
            onValueChange = { subnets = it },
            label = { Text("Número de subredes") },
            placeholder = { Text("Ej: 4") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                resultado = try {
                    VlsmUtils.calcularSubredes(ip, mask.toInt(), subnets.toInt())
                } catch (e: Exception) {
                    emptyList()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (resultado.isEmpty()) {
            Text("No se han generado subredes o los datos son inválidos.")
        } else {
            resultado.forEachIndexed { index, subred ->
                Text("Subred ${index + 1}")
                Text("Dirección de red: ${subred.direccionRed}")
                Text("Rango de hosts: ${subred.primerHost} - ${subred.ultimoHost}")
                Text("Broadcast: ${subred.broadcast}")
                Text("Máscara: /${subred.mascara}")
                Text("Hosts disponibles: ${subred.hostsDisponibles}")
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
