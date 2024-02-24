package com.example.resourcesharing.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.resourcesharing.navigation.PostOfficeAppRouter
import com.example.resourcesharing.navigation.Screen
import com.example.resourcesharing.screens.CourseScreen
import com.example.resourcesharing.screens.HomeScreen
import com.example.resourcesharing.screens.ImageScreen
import com.example.resourcesharing.screens.LoginScreen
import com.example.resourcesharing.screens.SignUpScreen
import com.example.resourcesharing.screens.SubjectScreen
import com.example.resourcesharing.screens.TermsAndConditionsScreen


@Composable
fun PostOfficeApp() {


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {



        Crossfade(targetState = PostOfficeAppRouter.currentScreen, label = "") { currentState ->
            when(currentState.value) {
                is Screen.SignUpScreen -> {
                    SignUpScreen()
                }

                is Screen.TermsAndConditionsScreen -> {
                    TermsAndConditionsScreen()
                }

                is Screen.LoginScreen -> {
                    LoginScreen()
                }

                is Screen.HomeScreen -> {
                    HomeScreen()
                }

                is Screen.CourseScreen -> {
                    // Check if it's the CourseScreen with an id
                    val screen = currentState.value as Screen.CourseScreen
                    CourseScreen(id = screen.id)
                }

                is Screen.SubjectScreen -> {
                    val screen = currentState.value as Screen.SubjectScreen
                    SubjectScreen(course = screen.course, id = screen.id)
                }

                is Screen.ImageScreen -> {
                    val screen = currentState.value as Screen.ImageScreen
                    ImageScreen(course = screen.course, id = screen.id)
                }
            }
            
        }
    }
}