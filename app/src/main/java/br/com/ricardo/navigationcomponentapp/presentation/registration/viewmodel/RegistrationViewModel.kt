package br.com.ricardo.navigationcomponentapp.presentation.registration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ricardo.navigationcomponentapp.R

class RegistrationViewModel: ViewModel() {

    sealed class RegistrationState {
        object CollectRegisterData: RegistrationState()
        object CollectCredentialsData: RegistrationState()
        object Registered: RegistrationState()
        class InvalidRegistration(val fields: List<Pair<String, Int>>): RegistrationState()
        class InvalidCredentials(val fields: List<Pair<String, Int>>): RegistrationState()
    }

    private val _registrationStateEvent = MutableLiveData<RegistrationState>()
    val regitrationStateEvent: LiveData<RegistrationState>
        get() = _registrationStateEvent

    var authToken =  ""

    fun collectRegistration(name: String, bio: String) {
        if (isValidRegistration(name, bio)) {
            _registrationStateEvent.value = RegistrationState.CollectCredentialsData
        }
    }

    private fun isValidRegistration(name: String, bio: String) : Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()

        if (name.isEmpty()) {
            invalidFields.add(INPUT_NAME)
        }

        if (bio.isEmpty()) {
            invalidFields.add(INPUT_BIO)
        }

        if (invalidFields.isNotEmpty()) {
            _registrationStateEvent.value = RegistrationState.InvalidRegistration(invalidFields)
            return false
        }

        return true
    }

    fun createCredentials(user: String, password: String) {
        if (isValidCredentials(user, password)) {
            this.authToken = "token"
            _registrationStateEvent.value = RegistrationState.Registered
        }
    }

    private fun isValidCredentials(user: String, password: String) : Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()

        if (user.isEmpty()) {
            invalidFields.add(INPUT_USER)
        }

        if (password.isEmpty()) {
            invalidFields.add(INPUT_PASS)
        }

        if (invalidFields.isNotEmpty()) {
            _registrationStateEvent.value = RegistrationState.InvalidCredentials(invalidFields)
            return false
        }

        return true
    }

    fun userCancelledRegistration() : Boolean{
        authToken = ""
        _registrationStateEvent.value = RegistrationState.CollectRegisterData
        return true
    }

    companion object {
        val INPUT_NAME = "INPUT_NAME" to R.string.register_message_error_name
        val INPUT_BIO = "INPUT_BIO" to R.string.register_message_error_bio
        val INPUT_USER = "INPUT_USER" to R.string.credentials_message_error_username
        val INPUT_PASS = "INPUT_PASSWORD" to R.string.credentials_message_error_password
    }
    
}