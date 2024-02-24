package com.example.resourcesharing.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.resourcesharing.R
import com.example.resourcesharing.components.AppToolbar
import com.example.resourcesharing.components.ClickableCarda
import com.example.resourcesharing.components.NormalTextComponent
import com.example.resourcesharing.data.home.HomeViewModel
import com.example.resourcesharing.navigation.PostOfficeAppRouter
import com.example.resourcesharing.navigation.Screen
import com.example.resourcesharing.ui.theme.Primary


@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Primary // Set the background color here
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppToolbar(
                toolbarTitle = stringResource(id = R.string.home),
                logoutButtonClicked = { homeViewModel.logout() }
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add space between toolbar and content
            NormalTextComponent(value = stringResource(id = R.string.dashboard))
            CardComponent()
        }
    }

}

@Composable
fun CardComponent() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ClickableCarda(
                id = 1,
                onTextSelected = {
                    PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 1))
                },
            )

            ClickableCarda(
                id = 2,
                onTextSelected = {
                    PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 2))
                }
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ClickableCarda(id = 3,
                onTextSelected = {
                    PostOfficeAppRouter.navigateTo(Screen.CourseScreen (id = 3))
                }
            )

            ClickableCarda(id = 4,
                onTextSelected = {
                    PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 4))
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ClickableCarda(
                id = 5,
                onTextSelected = {
                    PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 5))
                },
            )

            ClickableCarda(
                id = 6,
                onTextSelected = {
                    PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 6))
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ClickableCarda(id = 7,
                onTextSelected = {
                    PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 7))
                }
            )

            ClickableCarda(id = 8,
                onTextSelected = {
                    PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id = 8))
                }
            )
        }
    }


}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}