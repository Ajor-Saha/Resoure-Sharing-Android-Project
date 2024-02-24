package com.example.resourcesharing.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.resourcesharing.R
import com.example.resourcesharing.navigation.PostOfficeAppRouter
import com.example.resourcesharing.navigation.Screen
import com.example.resourcesharing.navigation.SystemBackButtonHandler

@Composable
fun CourseScreen(id: Int) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.study5), // Replace with your background image
            contentDescription = null, // Provide content description if needed
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds // Adjust content scale as needed
        )

        // Course content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SemesterComponent(id = id)
            SystemBackButtonHandler {
                PostOfficeAppRouter.navigateTo(Screen.HomeScreen)
            }
        }
    }
}


@Composable
fun SemesterComponent(id: Int) {
    val semester = getSemesterData(id)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = " ${semester.name}",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

        )

        Spacer(modifier = Modifier.height(8.dp))
        // Display courses for the semester, you can use another composable for this
        CourseListComponent(courses = semester.courses, id = id)
    }
}

data class Semester(val id: Int, val name: String, val courses: List<String>)

fun getSemesterData(id: Int): Semester {

    return when (id) {
        1 -> Semester(1, "1st semester", listOf("Structured Programming Language", "Discrete Mathematics", "Electrical Circuits", "Matrices, Vector Analysis and Geometry", "English Language"))
        2 -> Semester(2, "2nd semester", listOf("Data Structure", "Electronic Devices and Circuits", "Engineering Graphics", "Mechanics, Wave, Heat & Thermodynamics", "Calculus", "Ethics and Cyber Law","Project Work 1"))
        3 -> Semester(3, "3rd semester", listOf("Digital Logic Design", "Algorithm Design & Analysis", "Cost and Management Accounting", "Basic Statistics & Probability", "Object Oriented Programming Language"))
        4 -> Semester(4, "4th semester", listOf("Data Science", "Numerical Analysis", "Theory of Computation and Concrete Mathematics", "Principles of Economics","Project Work II"))
        5 -> Semester(5, "5th semester", listOf("Database System", "Communication Engineering", "Microprocessor & Interfacing", "Operating System and System Programming"))
        6 -> Semester(6, "6th semester", listOf("Software Engineering & Design Patterns", "Computer Networking", "Computer Graphics and Image Processing", "Technical Writing And Presentation"))
        7 -> Semester(7, "7th semester", listOf("Artificial Intelligence", "Compiler Construction", "Web Engineering", "Option I", "Thesis / Project I"))
        8 -> Semester(8, "8th semester", listOf("Digital Signal Processing", "Option II", "Thesis / Project II", "Viva Voce"))
        // Add more cases as needed
        else -> Semester(id, "Semester $id", emptyList())
    }

}

@Composable
fun CourseListComponent(courses: List<String>, id: Int) {
    // Display the list of courses for the semester
    Column {
        courses.forEach { course ->
            Button(
                onClick = {
                   PostOfficeAppRouter.navigateTo(Screen.SubjectScreen(course,id))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Cyan,
                    contentColor = Color.DarkGray
                )
            ) {
                Text(text = course)
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun CourseScreenPreview() {
    CourseScreen(id = 1)
}