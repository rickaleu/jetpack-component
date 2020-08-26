package br.com.ricardo.navigationcomponentapp.extensions

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.hideMessageError () {
    this.error = null
    this.isErrorEnabled = false
}