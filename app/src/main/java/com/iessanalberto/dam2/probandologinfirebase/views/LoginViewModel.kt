package com.iessanalberto.dam2.probandologinfirebase.views

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.iessanalberto.dam2.probandologinfirebase.states.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {
    private  val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> =_loginState.asStateFlow()

    // Para el firebase (implementar el auth en el module)
    private lateinit var auth: FirebaseAuth
    init {
        auth = Firebase.auth
    }

    // Funciones para actualizar el estado
    fun onUsuarioChanged(usuario: String){
        _loginState.update { currentState ->
            currentState.copy(usuario = usuario)
        }
    }
    fun onPasswordChanged(password: String){
        _loginState.update { it.copy(password = password) }
    }

    // Funciones de "negocio"
    fun conectar(): Boolean {
        _loginState.update {it.copy(numIntentos = it.numIntentos.inc())} // Incrementa número intentos
        val currenState = _loginState.value
        auth.signInWithEmailAndPassword(
            currenState.usuario, currenState.password)

        return (_loginState.value.usuario == "Admin" && _loginState.value.password == "Admin")
    }


    /**
     * Funcion antigua conectar:
     *  fun conectar(): Boolean{
     *         _loginState.update {it.copy(numIntentos = it.numIntentos.inc()) // Incrementa número intentos
     *         }
     *         return (_loginState.value.usuario == "Admin" && _loginState.value.password == "Admin")
     *     }
     */

}