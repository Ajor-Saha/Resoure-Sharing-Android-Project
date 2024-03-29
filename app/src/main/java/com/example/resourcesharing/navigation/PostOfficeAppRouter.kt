package com.example.resourcesharing.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


sealed class Screen {
    object SignUpScreen : Screen()
    object TermsAndConditionsScreen : Screen()
    object LoginScreen : Screen()
    object HomeScreen : Screen()
    data class CourseScreen(val id: Int) : Screen()
    data class  SubjectScreen(val course: String , val id: Int) : Screen()
    data class ImageScreen(val course: String, val id: Int) : Screen()
}

object PostOfficeAppRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUpScreen)

    fun navigateTo(destination : Screen) {
        currentScreen.value = destination
    }


}