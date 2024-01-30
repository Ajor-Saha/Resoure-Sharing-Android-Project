package com.example.resourcesharing.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.resourcesharing.R
import com.example.resourcesharing.components.AppToolbar
import com.example.resourcesharing.components.ClickableCard
import com.example.resourcesharing.components.NormalTextComponent
import com.example.resourcesharing.data.home.HomeViewModel
import com.example.resourcesharing.navigation.PostOfficeAppRouter
import com.example.resourcesharing.navigation.Screen


@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {


    Scaffold(
        topBar = {
            AppToolbar(toolbarTitle = stringResource(id = R.string.home),
                logoutButtonClicked = {
                    homeViewModel.logout()
                },

            )
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

            }
        }

    }

    NormalTextComponent(value = stringResource(id = R.string.dashboard))
    CardComponent()

}

@Composable
fun CardComponent() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ClickableCard(id = 1,
            onTextSelected = {
                PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 1))
            }
        )

        ClickableCard(id = 2,
            onTextSelected = {
                PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 2))
            }
        )

        ClickableCard(id = 3,
            onTextSelected = {
                PostOfficeAppRouter.navigateTo(Screen.CourseScreen (id = 3))
            }
        )

        ClickableCard(id = 4,
            onTextSelected = {
                PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 4))
            }
        )

        ClickableCard(id = 5,
            onTextSelected = {
                PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 5))
            }
        )

        ClickableCard(id = 6,
            onTextSelected = {
                PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 6))
            }
        )

        ClickableCard(id = 7,
            onTextSelected = {
                PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 7))
            }
        )

        ClickableCard(id = 8,
            onTextSelected = {
                PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 8))
            }
        )
    }
    

}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}