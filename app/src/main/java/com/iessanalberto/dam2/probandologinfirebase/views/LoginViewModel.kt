package com.iessanalberto.dam2.probandologinfirebase.views

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth
import com.iessanalberto.dam2.probandologinfirebase.states.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel: ViewModel() {
    private  val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> =_loginState.asStateFlow()

    // Para el firebase (implementar el auth en el module)
    private lateinit var auth: FirebaseAuth // La inicializo tarde
    init {
        auth = Firebase.auth
    }

    // Funciones para actualizar el estado
    fun onUsuarioChanged(usuario: String){
        _loginState.update { currentState ->
            currentState.copy(usuario = usuario)
        }
    }
    fun clearErrorMessage () {
        _loginState.update { it.copy(errorMessage = "")}
    }
    fun onPasswordChanged(password: String){
        _loginState.update { it.copy(password = password) }
    }

    // Funciones de "negocio"
    fun conectar(){
        // Actualziar el estado a isLoading
        _loginState.update { it.copy(isLoading = true) }
        // crea una co-rutina dentro del viewmodel - resiste cambios de estado (abrimos un hilo)
        viewModelScope.launch {
            val currenState = _loginState.value // Foto del estado
            Firebase.auth.signInWithEmailAndPassword(
                currenState.usuario, currenState.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginState.update { it.copy(
                            isLogged = true,
                            isLoading = false
                        ) }
                    } else {
                        _loginState.update { it.copy(
                            isLogged = false,
                            numIntentos = it.numIntentos.inc(), // Accedemos por le it del copy // Incrementa número intentos
                            isLoading = false,
                            errorMessage = "Error usuario/contraseña incorrectos"
                        ) }
                    }
                }
        }
    }

    // Otro ejemplo, mucho mejor
    fun otraConexion() {
        _loginState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val currenState = _loginState.value
            try {
                auth.signInWithEmailAndPassword(currenState.usuario,currenState.password)
                    .await()
                _loginState.update { it.copy(
                    isLogged = true,
                    isLoading = false
                ) }
            }  catch (exception: FirebaseAuthInvalidCredentialsException ) { // TODO cuando no es un correo
                _loginState.update { it.copy(
                    isLoading = false,
                    errorMessage = "Usuario/contraseña mal escritos",
                    numIntentos = currenState.numIntentos.inc()
                ) }
            } catch (exception: FirebaseException) { //TODO ESTA ES LA EXCEPCION CUANDO NO ENCUENTRA UN USUARIO Y CONTRASEÑA
                _loginState.update { it.copy(
                    isLoading = false,
                    errorMessage = "Usuario/contraseña incorrectos",
                    numIntentos = currenState.numIntentos.inc()
                ) }
            }
            catch (exception: Exception) {
                Log.e("DANI",exception.toString()) // TODO FORMA DE SABER EL LOG LAS EXCEPCIONES
                _loginState.update { it.copy(
                    isLoading = false,
                    errorMessage = "Something is wrong"
                ) }
            }
        }
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