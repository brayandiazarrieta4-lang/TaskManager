package com.example.taskmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.domain.model.User
import com.example.taskmanager.domain.usecase.auth.GetCurrentUserUseCase
import com.example.taskmanager.domain.usecase.auth.LoginUserUseCase
import com.example.taskmanager.domain.usecase.auth.LogoutUserUseCase
import com.example.taskmanager.domain.usecase.auth.RegisterUserUseCase
import com.example.taskmanager.ui.state.AuthUiState
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AuthUiState>(
            AuthUiState.Idle
        )

    val uiState: StateFlow<AuthUiState> =
        _uiState.asStateFlow()

    fun register(
        email: String,
        password: String,
        confirmPassword: String
    ) {

        if (_uiState.value is AuthUiState.Loading) {
            return
        }

        if (email.isBlank()) {
            _uiState.value =
                AuthUiState.Error(
                    "El correo es obligatorio"
                )
            return
        }

        if (!isValidEmail(email)) {
            _uiState.value =
                AuthUiState.Error(
                    "El correo no tiene un formato válido"
                )
            return
        }

        if (password.isBlank()) {
            _uiState.value =
                AuthUiState.Error(
                    "La contraseña es obligatoria"
                )
            return
        }

        if (password.length < 6) {
            _uiState.value =
                AuthUiState.Error(
                    "La contraseña debe tener mínimo 6 caracteres"
                )
            return
        }

        if (confirmPassword.isBlank()) {
            _uiState.value =
                AuthUiState.Error(
                    "Debes confirmar la contraseña"
                )
            return
        }

        if (password != confirmPassword) {
            _uiState.value =
                AuthUiState.Error(
                    "Las contraseñas no coinciden"
                )
            return
        }

        viewModelScope.launch {

            _uiState.value =
                AuthUiState.Loading

            val result =
                registerUserUseCase(
                    email,
                    password
                )

            result
                .onSuccess { user ->
                    _uiState.value =
                        AuthUiState.Success(user)
                }
                .onFailure { exception ->
                    _uiState.value =
                        AuthUiState.Error(
                            firebaseErrorMessage(exception)
                        )
                }
        }
    }

    fun login(
        email: String,
        password: String
    ) {

        if (_uiState.value is AuthUiState.Loading) {
            return
        }

        if (email.isBlank()) {
            _uiState.value =
                AuthUiState.Error(
                    "El correo es obligatorio"
                )
            return
        }

        if (!isValidEmail(email)) {
            _uiState.value =
                AuthUiState.Error(
                    "El correo no tiene un formato válido"
                )
            return
        }

        if (password.isBlank()) {
            _uiState.value =
                AuthUiState.Error(
                    "La contraseña es obligatoria"
                )
            return
        }

        viewModelScope.launch {

            _uiState.value =
                AuthUiState.Loading

            val result =
                loginUserUseCase(
                    email,
                    password
                )

            result
                .onSuccess { user ->
                    _uiState.value =
                        AuthUiState.Success(user)
                }
                .onFailure { exception ->
                    _uiState.value =
                        AuthUiState.Error(
                            firebaseErrorMessage(exception)
                        )
                }
        }
    }

    fun logout() {

        logoutUserUseCase()

        _uiState.value =
            AuthUiState.Idle
    }

    fun getCurrentUser(): User? {

        val user =
            getCurrentUserUseCase()

        if (user != null) {

            _uiState.value =
                AuthUiState.Success(user)

        } else {

            _uiState.value =
                AuthUiState.Idle
        }

        return user
    }

    private fun isValidEmail(
        email: String
    ): Boolean {

        return android.util.Patterns
            .EMAIL_ADDRESS
            .matcher(email)
            .matches()
    }

    private fun firebaseErrorMessage(
        exception: Throwable
    ): String {

        val authException =
            exception as? FirebaseAuthException

        return when (authException?.errorCode) {

            "ERROR_EMAIL_ALREADY_IN_USE" ->
                "El correo ya está registrado"

            "ERROR_INVALID_EMAIL" ->
                "El correo no es válido"

            "ERROR_WEAK_PASSWORD" ->
                "La contraseña es demasiado débil"

            "ERROR_USER_NOT_FOUND" ->
                "No existe una cuenta con este correo"

            "ERROR_WRONG_PASSWORD" ->
                "La contraseña es incorrecta"

            "ERROR_INVALID_CREDENTIAL" ->
                "El correo o la contraseña son incorrectos"

            "ERROR_USER_DISABLED" ->
                "Esta cuenta está deshabilitada"

            "ERROR_TOO_MANY_REQUESTS" ->
                "Demasiados intentos. Intenta más tarde"

            else ->
                exception.message
                    ?: "Ocurrió un error de autenticación"
        }
    }
}