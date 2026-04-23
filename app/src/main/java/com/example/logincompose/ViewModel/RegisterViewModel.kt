package com.example.logincompose.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.logincompose.AppDatabase
import com.example.logincompose.usuario.User
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).userDao()

    fun registerUser(
        nome: String,
        email: String,
        telefone: String,
        senha: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            dao.insertUser(
                User(
                    nome = nome,
                    email = email,
                    telefone = telefone,
                    senha = senha
                )
            )
            onSuccess()
        }
    }
}