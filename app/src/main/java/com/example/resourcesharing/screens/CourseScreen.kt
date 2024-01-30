package com.example.resourcesharing.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resourcesharing.navigation.PostOfficeAppRouter
import com.example.resourcesharing.navigation.Screen
import com.example.resourcesharing.navigation.SystemBackButtonHandler

@Composable
fun CourseScreen(id: Int) {
    SemesterComponent(id = id)

    SystemBackButtonHandler {
        PostOfficeAppRouter.navigateTo(Screen.HomeScreen)
    }
}


@Composable
fun SemesterComponent(id: Int) {
    val semester = getSemesterData(id)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = " ${semester.name}")
        Text(text = "Courses:")
        // Display courses for the semester, you can use another composable for this
        CourseListComponent(courses = semester.courses)
    }
}

data class Semester(val id: Int, val name: String, val courses: List<String>)

fun getSemesterData(id: Int): Semester {

    return when (id) {
        1 -> Semester(1, "1st semester", listOf("Structured Programming Language", "Discrete Mathematics", "Electrical Circuits", "Matrices, Vector Analysis and Geometry", "English Language"))
        2 -> Semester(2, "2nd semester", listOf("Data Structure", "Electronic Devices and Circuits", "Engineering Graphics", "Mechanics, Wave, Heat & Thermodynamics", "Calculus", "Ethics and Cyber Law","Project Work 1"))
        3 -> Semester(3, "3rd semester", listOf("Digital Logic Design", "Algorithm Design & Analysis", "Cost and Management Accounting", "Basic Statistics & Probability", "Object Oriented Programming Language"))
        4 -> Semester(4, "4th semester", listOf("Data Science", "Numerical Analysis", "Theory of Computation and Concrete Mathematics", "Principles of Economics","Project Work II"))
        5 -> Semester(5, "5th semester", listOf("Data Structure", "Electronic Devices and Circuits", "Engineering Graphics", "Mechanics, Wave, Heat & Thermodynamics", "Calculus"))
        6 -> Semester(6, "6th semester", listOf("Data Structure", "Electronic Devices and Circuits", "Engineering Graphics", "Mechanics, Wave, Heat & Thermodynamics", "Calculus"))
        7 -> Semester(7, "7th semester", listOf("Data Structure", "Electronic Devices and Circuits", "Engineering Graphics", "Mechanics, Wave, Heat & Thermodynamics", "Calculus"))
        8 -> Semester(8, "8th semester", listOf("Data Structure", "Electronic Devices and Circuits", "Engineering Graphics", "Mechanics, Wave, Heat & Thermodynamics", "Calculus"))
        // Add more cases as needed
        else -> Semester(id, "Semester $id", emptyList())
    }

}

@Composable
fun CourseListComponent(courses: List<String>) {
    // Display the list of courses for the semester
    Column {
        courses.forEach { course ->
            Text(text = course)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseScreenPreview() {
    CourseScreen(id = 1)
}