package br.com.ricardo.navigationcomponentapp.presentation.credentials.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.ricardo.navigationcomponentapp.R
import br.com.ricardo.navigationcomponentapp.extensions.hideMessageError
import br.com.ricardo.navigationcomponentapp.presentation.login.viewmodel.LoginViewModel
import br.com.ricardo.navigationcomponentapp.presentation.registration.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_credentials.*

class CredentialsFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val args: CredentialsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credentials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        credentials_text_welcome.text = getString(R.string.credentials_text_message, args.name)

        val invalidFields = initCredentialsFields()
        listenToCredentialsStateEvents(invalidFields)
        credentialsViewListeners()

    }

    private fun listenToCredentialsStateEvents(invalidationfields: Map<String, TextInputLayout>) {
        registrationViewModel.regitrationStateEvent.observe(viewLifecycleOwner, Observer { status ->
            when(status) {
                is RegistrationViewModel.RegistrationState.Registered -> {
                    val token = registrationViewModel.authToken
                    val username = credentials_edit_user.text.toString()

                    loginViewModel.authenticationToken(token, username)
                    findNavController().popBackStack(R.id.profileFragment, false)
                }
                is RegistrationViewModel.RegistrationState.InvalidCredentials -> {
                    status.fields.forEach { fieldsError ->
                        invalidationfields[fieldsError.first]?.error = getString(fieldsError.second)
                    }
                }
            }
        })
    }

    private fun credentialsViewListeners() {
        credentials_button_finish.setOnClickListener {
            val user = credentials_edit_user.text.toString()
            val password = credentials_edit_password.text.toString()

            registrationViewModel.createCredentials(user, password)
        }

        credentials_edit_user.addTextChangedListener { credentials_imput_user.hideMessageError() }
        credentials_edit_password.addTextChangedListener { credentials_imput_password.hideMessageError() }
    }

    private fun initCredentialsFields() = mapOf(
        RegistrationViewModel.INPUT_USER.first to credentials_imput_user,
        RegistrationViewModel.INPUT_PASS.first to credentials_imput_password
    )

}