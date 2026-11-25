package com.iessanalberto.dam2.probandologinfirebase.states

data class LoginState(
    val usuario: String = "",
    val password: String = "",
    val numIntentos: Int = 0
)