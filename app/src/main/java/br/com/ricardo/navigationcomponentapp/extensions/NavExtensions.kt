package br.com.ricardo.navigationcomponentapp.extensions

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import br.com.ricardo.navigationcomponentapp.R


private val navOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    .setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_right)
    .build()

fun NavController.navigateWithAnimations(destination: Int) {
    this.navigate(destination, null, navOptions)
}