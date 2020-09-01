package br.com.ricardo.navigationcomponentapp.presentation.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.ricardo.navigationcomponentapp.R
import br.com.ricardo.navigationcomponentapp.extensions.hideMessageError
import br.com.ricardo.navigationcomponentapp.extensions.navigateWithAnimations
import br.com.ricardo.navigationcomponentapp.presentation.login.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.authenicationStateEvent.observe(viewLifecycleOwner, Observer { status ->
            when(status) {
                is LoginViewModel.AuthenticationState.Authenticated -> {
                    findNavController().popBackStack()
                }
                is LoginViewModel.AuthenticationState.InvalidAuthentication -> {
                    val validationFields : Map<String, TextInputLayout> = initValidationFields()

                    status.fields.forEach { validError ->
                        validationFields[validError.first]?.error = getString(validError.second)
                    }
                }
            }
        })

        login_button_sing_in.setOnClickListener {
            val user = login_edit_user.text.toString()
            val password = login_edit_password.text.toString()

            viewModel.authentication(user, password)
        }

        login_button_sing_up.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.registration_graph)
        }

        login_edit_user.addTextChangedListener { login_imput_user.hideMessageError() }
        login_edit_password.addTextChangedListener { login_imput_password.hideMessageError() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelAuthentication()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelAuthentication()
        return super.onOptionsItemSelected(item)
    }

    private fun cancelAuthentication() {
        viewModel.refuseAuthentication()
        findNavController().popBackStack(R.id.startFragment, false)
    }

    private fun initValidationFields() = mapOf(
        LoginViewModel.IMPUT_USERNAME.first to login_imput_user,
        LoginViewModel.IMPUT_PASSWORD.first to login_imput_password
    )
}