package com.iessanalberto.dam2.probandologinfirebase.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iessanalberto.dam2.probandologinfirebase.states.LoginState
import com.iessanalberto.dam2.probandologinfirebase.views.LoginViewModel


/**
 * COSAS A TENER EN CUENTA:
 * Se modificaron cosas a nivel de build.gradel Project y Modele
 * el json google_services que va a nivel de "Project, app"
 *
 */
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    Scaffold() {innerpadding ->
        LoginBodyContent(modifier = Modifier.padding(paddingValues = innerpadding),
            onLoginSucces = onLoginSuccess, // Pasamos la funcion para que el de abajo la pueda llamar
            )
    }
}

@Composable
fun LoginBodyContent(modifier: Modifier, onLoginSucces: () -> Unit) {

    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel()
    val loginState by loginViewModel.loginState.collectAsState()

    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            value = loginState.usuario,
            label = { Text(text = "Introduce usuario") },
            onValueChange = {loginViewModel.onUsuarioChanged(usuario = it)}
        )
        OutlinedTextField(
            value = loginState.password,
            label = { Text(text = "Introduce contraseña") },
            onValueChange = {loginViewModel.onPasswordChanged(password = it)}
        )
        Button(onClick = {
            if (loginViewModel.conectar()){
                onLoginSucces() // Navega a la ventana del menú (MenúScreen). Ejecuta la funcion
            } else {
                Toast.makeText(context,"Usuario/Contraseña incorrectos", Toast.LENGTH_SHORT).show()
                if (loginState.numIntentos >= 2) { // Por el estado, vamos con restraso
                    Toast.makeText(context,"Has superado el número de intentos", Toast.LENGTH_SHORT).show()
                    System.exit(10) // Mata todo
                }
            }
        }) {
            Text(text = "Conectar")
        }
    }
}