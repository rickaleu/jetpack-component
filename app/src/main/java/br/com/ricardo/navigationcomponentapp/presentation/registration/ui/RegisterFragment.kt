package br.com.ricardo.navigationcomponentapp.presentation.registration.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.ricardo.navigationcomponentapp.R
import br.com.ricardo.navigationcomponentapp.extensions.hideMessageError
import br.com.ricardo.navigationcomponentapp.presentation.registration.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val validationFields = initValidationFields()
        listenToRegistrationStateEvents(validationFields)
        registerViewListeners()
        registerDeviceCallBackStackCallback()
    }

    private fun listenToRegistrationStateEvents(validationFields: Map<String, TextInputLayout>) {
        registrationViewModel.regitrationStateEvent.observe(viewLifecycleOwner, Observer { status ->
            when(status) {
                is RegistrationViewModel.RegistrationState.CollectCredentialsData -> {
                    val name = register_edit_name.text.toString()
                    val directions = RegisterFragmentDirections
                        .actionRegisterFragmentToCredentialsFragment(name)

                    findNavController().navigate(directions)
                }
                is RegistrationViewModel.RegistrationState.InvalidRegistration -> {
                    status.fields.forEach { fieldError ->
                        validationFields[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
            }
        })
    }

    private fun registerViewListeners() {
        register_button_next.setOnClickListener {
            val name = register_edit_name.text.toString()
            val bio = register_edit_bio.text.toString()

            registrationViewModel.collectRegistration(name, bio)
        }

        register_edit_name.addTextChangedListener { register_imput_name.hideMessageError() }
        register_edit_bio.addTextChangedListener { register_imput_bio.hideMessageError() }
    }

    private fun registerDeviceCallBackStackCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelRegistration()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelRegistration()
        return true
    }

    private fun cancelRegistration() {
        registrationViewModel.userCancelledRegistration()
        findNavController().popBackStack(R.id.loginFragment, false)
    }

    private fun initValidationFields() = mapOf(
        RegistrationViewModel.INPUT_NAME.first to register_imput_name,
        RegistrationViewModel.INPUT_BIO.first to register_imput_bio
    )

}