package br.com.ricardo.navigationcomponentapp.presentation.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ricardo.navigationcomponentapp.R

class LoginViewModel : ViewModel() {

    sealed class AuthenticationState {
        object Authenticated: AuthenticationState()
        object Unauthenticated: AuthenticationState()
        class InvalidAuthentication(val fields: List<Pair<String, Int>>) : AuthenticationState()
    }

    val authenicationStateEvent = MutableLiveData<AuthenticationState>()
    var user = ""

    init {
        refuseAuthentication()
    }

    fun refuseAuthentication() {
        authenicationStateEvent.value = AuthenticationState.Unauthenticated
    }

    fun authentication(user: String, password: String) {
        if (isValidForm(user, password)) {
            this.user = user
            authenicationStateEvent.value = AuthenticationState.Authenticated
        }
    }

    private fun isValidForm(user: String, password: String) : Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()

        if (user.isEmpty()) {
            invalidFields.add(IMPUT_USERNAME)
        }

        if (password.isEmpty()) {
            invalidFields.add(IMPUT_PASSWORD)
        }

        if (invalidFields.isNotEmpty()) {
            authenicationStateEvent.value = AuthenticationState.InvalidAuthentication(invalidFields)
            return false
        }

        return true
    }

    companion object {
        val IMPUT_USERNAME = "IMPUT_USERNAME" to R.string.login_message_error_username
        val IMPUT_PASSWORD = "IMPUT_PASSWORD" to R.string.login_message_error_password
    }
}