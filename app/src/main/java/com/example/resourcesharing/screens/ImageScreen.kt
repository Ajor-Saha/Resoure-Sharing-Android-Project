package com.example.resourcesharing.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.resourcesharing.navigation.PostOfficeAppRouter
import com.example.resourcesharing.navigation.Screen
import com.example.resourcesharing.navigation.SystemBackButtonHandler

@Composable
fun ImageScreen(course: String, id: Int) {

    val context = LocalContext.current

    var  courseTitle by remember { mutableStateOf(course) }

    SystemBackButtonHandler {
        PostOfficeAppRouter.navigateTo(Screen.SubjectScreen(course,id))
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray // Set the background color here
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = course,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 24.sp, // Adjust font size as needed
                fontWeight = FontWeight.Bold // Adjust font weight as needed
            )

            videoSubjectData(courseTitle)?.chapters?.forEach { chapter ->
                VideoChapterSection(chapter = chapter, context = context)
                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }
}


@Composable
fun VideoChapterSection(chapter: VideoCourse.Chapter, context: Context) {
    Column {
        Text(
            text = chapter.chapterName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            chapter.topicList.forEach { topic ->
                VideoTopicCard(topic = topic, context = context)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun VideoTopicCard(topic: VideoCourse.Chapter.TopicItem, context: Context) {
    Card(
        modifier = Modifier.clickable {
            // Handle click, open link in browser
            launchInBrowserVideo(context, topic.link)
        },
    ) {
        Text(
            text = topic.topicName,
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp
        )
    }
}

private fun launchInBrowserVideo(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.`package` = "com.android.chrome"
    if (context.packageManager.resolveActivity(intent, 0) == null) {
        // Chrome browser app not installed, fallback to system default browser
        intent.`package` = null
    }
    context.startActivity(intent)
}

fun videoSubjectData(courseTitle: String): VideoCourse? {
    return when (courseTitle) {
        "Structured Programming Language" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/1o835mT2agXUPzihgT3AkhDKEPCM5aCst/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "https://drive.google.com/file/d/1o835mT2agXUPzihgT3AkhDKEPCM5aCst/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "https://drive.google.com/file/d/1o835mT2agXUPzihgT3AkhDKEPCM5aCst/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "https://drive.google.com/file/d/1o835mT2agXUPzihgT3AkhDKEPCM5aCst/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Functions", "https://drive.google.com/file/d/1o835mT2agXUPzihgT3AkhDKEPCM5aCst/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Entering Input Data ", "https://drive.google.com/file/d/1o835mT2agXUPzihgT3AkhDKEPCM5aCst/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arithmetic Operators", "https://drive.google.com/file/d/1o835mT2agXUPzihgT3AkhDKEPCM5aCst/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Relational and Logical Operators", "https://drive.google.com/file/d/1o835mT2agXUPzihgT3AkhDKEPCM5aCst/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "https://drive.google.com/file/d/1o835mT2agXUPzihgT3AkhDKEPCM5aCst/view?usp=sharing")
                    )
                )
            )
        )

        "Discrete Mathematics" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Sets, Relations, and Functions",
                            "https://drive.google.com/file/d/1V44RnJVbc996OY4ilir0Tb6m5GZNf5vO/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Logic and Propositional Calculus", "https://drive.google.com/file/d/1V44RnJVbc996OY4ilir0Tb6m5GZNf5vO/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Proof Techniques", "https://drive.google.com/file/d/1V44RnJVbc996OY4ilir0Tb6m5GZNf5vO/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Graph Theory", "https://drive.google.com/file/d/1V44RnJVbc996OY4ilir0Tb6m5GZNf5vO/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Euclidean Vector Spaces", "https://drive.google.com/file/d/1V44RnJVbc996OY4ilir0Tb6m5GZNf5vO/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Orthogonality", "https://drive.google.com/file/d/1V44RnJVbc996OY4ilir0Tb6m5GZNf5vO/view?usp=sharing")
                    )
                )
            )
        )
        "Electrical Circuits" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction",
                            "https://drive.google.com/file/d/10wht3xM_yFEC8jAaPmzBLaQAeONEtojn/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Power and Energy", "https://drive.google.com/file/d/10wht3xM_yFEC8jAaPmzBLaQAeONEtojn/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Ohm’s Law",
                            "https://drive.google.com/file/d/10wht3xM_yFEC8jAaPmzBLaQAeONEtojn/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Nodes, Branches, and Loops", "https://drive.google.com/file/d/10wht3xM_yFEC8jAaPmzBLaQAeONEtojn/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Nodal Analysis with Voltage Sources ",
                            "https://drive.google.com/file/d/10wht3xM_yFEC8jAaPmzBLaQAeONEtojn/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Mesh Analysis with Current Sources", "https://drive.google.com/file/d/10wht3xM_yFEC8jAaPmzBLaQAeONEtojn/view?usp=sharing")
                    )
                ),

            )
        )
        "Matrices, Vector Analysis and Geometry" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Systems of Linear Equations and Matrices",
                            "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari"
                        ),
                        VideoCourse.Chapter.TopicItem("Matrices and Matrix Operations", "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Determinants by Cofactor Expansion",
                            "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari"
                        ),
                        VideoCourse.Chapter.TopicItem("Evaluating Determinants by Row Reduction", "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Properties of Determinants; Cramer's Rule",
                            "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari"
                        ),
                        VideoCourse.Chapter.TopicItem("Orthogonality", "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari")
                    )
                ),
            )
        )
        "English Language" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Grammar Essentials",
                            "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari"
                        ),
                        VideoCourse.Chapter.TopicItem("Mastering Vocabulary", "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari"),
                        VideoCourse.Chapter.TopicItem("Mastering Vocabulary", "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari")

                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Grammar Essentials",
                            "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari"
                        ),
                        VideoCourse.Chapter.TopicItem("Mastering Vocabulary", "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari"),
                        VideoCourse.Chapter.TopicItem("Mastering Vocabulary", "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari")

                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Grammar Essentials",
                            "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari"
                        ),
                        VideoCourse.Chapter.TopicItem("Mastering Vocabulary", "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari"),
                        VideoCourse.Chapter.TopicItem("Mastering Vocabulary", "https://drive.google.com/file/d/1um6HSWB3nJn7iQvJW5xQUyi4PA41tFGP/view?usp=shari")

                    )
                ),

            )
        )
        "Data Structure" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction AND Overview",
                            "https://drive.google.com/file/d/1LsvIOxPPfLVzV2mE30oYNg7HDfHVE8Vq/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Data Structure Operation", "https://drive.google.com/file/d/1LsvIOxPPfLVzV2mE30oYNg7HDfHVE8Vq/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Mathematical Notation and Function", "https://drive.google.com/file/d/1LsvIOxPPfLVzV2mE30oYNg7HDfHVE8Vq/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Complexity of Algorithms",
                            "https://drive.google.com/file/d/1LsvIOxPPfLVzV2mE30oYNg7HDfHVE8Vq/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables , Data Types", "https://drive.google.com/file/d/1LsvIOxPPfLVzV2mE30oYNg7HDfHVE8Vq/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("String Processing", "https://drive.google.com/file/d/1LsvIOxPPfLVzV2mE30oYNg7HDfHVE8Vq/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Arrays Records and Pointers",
                            "https://drive.google.com/file/d/1LsvIOxPPfLVzV2mE30oYNg7HDfHVE8Vq/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Inserting and deleting ", "https://drive.google.com/file/d/1LsvIOxPPfLVzV2mE30oYNg7HDfHVE8Vq/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Searching :linear Searching", "https://drive.google.com/file/d/1LsvIOxPPfLVzV2mE30oYNg7HDfHVE8Vq/view?usp=sharing")
                    )
                ),

            )
        )
        "Electronic Devices and Circuits" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Semiconductor Diodes",
                            "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Covalent Bonding and Intrinsic Materials ", "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("n -Type and p -Type Materials ", "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Diode Applications",
                            "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Series Diode Configurations", "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("AND/OR Gate", "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Transistor Construction",
                            "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Common-Base Configuration", "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Limits of Operation", "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing")
                    )
                )
            )
        )
        "Engineering Graphics" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Engineering Drawing Standards",
                            "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Orthographic Projection", "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Isometric and Axonometric Projection", "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Technical Sketching and Visualization",
                            "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Technical Sketching and Visualization", "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Technical Sketching and Visualization", "https://drive.google.com/file/d/1qbPJmtzEZAdWLpfsTcf54Dij4A1B80R6/view?usp=sharing")
                    )
                ),

            )
        )

        "Mechanics, Wave, Heat & Thermodynamics" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "What Is Physics?",
                            "https://drive.google.com/file/d/1P-vDRCBGQEWl3LfrPJ0O8oYmg7FW8n8Y/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Position and Displacement", "https://drive.google.com/file/d/1P-vDRCBGQEWl3LfrPJ0O8oYmg7FW8n8Y/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Average Velocity and Average Speed", "https://drive.google.com/file/d/1P-vDRCBGQEWl3LfrPJ0O8oYmg7FW8n8Y/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "What Is Physics?",
                            "https://drive.google.com/file/d/1P-vDRCBGQEWl3LfrPJ0O8oYmg7FW8n8Y/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Position and Displacement", "https://drive.google.com/file/d/1P-vDRCBGQEWl3LfrPJ0O8oYmg7FW8n8Y/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Average Velocity and Average Speed", "https://drive.google.com/file/d/1P-vDRCBGQEWl3LfrPJ0O8oYmg7FW8n8Y/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "What Is Physics?",
                            "https://drive.google.com/file/d/1P-vDRCBGQEWl3LfrPJ0O8oYmg7FW8n8Y/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Position and Displacement", "https://drive.google.com/file/d/1P-vDRCBGQEWl3LfrPJ0O8oYmg7FW8n8Y/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Average Velocity and Average Speed", "https://drive.google.com/file/d/1P-vDRCBGQEWl3LfrPJ0O8oYmg7FW8n8Y/view?usp=sharing")
                    )
                ),
            )
        )
        "Calculus" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Integration",
                            "https://drive.google.com/file/d/1k-ltDjgd3HfFb9g6P5zivri738kT4a-L/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Differentiation", "https://drive.google.com/file/d/1k-ltDjgd3HfFb9g6P5zivri738kT4a-L/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("basic differentiation", "https://drive.google.com/file/d/1k-ltDjgd3HfFb9g6P5zivri738kT4a-L/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Integration",
                            "https://drive.google.com/file/d/1k-ltDjgd3HfFb9g6P5zivri738kT4a-L/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Differentiation", "https://drive.google.com/file/d/1k-ltDjgd3HfFb9g6P5zivri738kT4a-L/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("basic differentiation", "https://drive.google.com/file/d/1k-ltDjgd3HfFb9g6P5zivri738kT4a-L/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Integration",
                            "https://drive.google.com/file/d/1k-ltDjgd3HfFb9g6P5zivri738kT4a-L/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Differentiation", "https://drive.google.com/file/d/1k-ltDjgd3HfFb9g6P5zivri738kT4a-L/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("basic differentiation", "https://drive.google.com/file/d/1k-ltDjgd3HfFb9g6P5zivri738kT4a-L/view?usp=sharing")
                    )
                ),
            )
        )
        "Ethics and Cyber Law" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Cyber Ethics:",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Cyber-security and Privacy Laws", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Cybercrime and Law Enforcement", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Cyber Ethics:",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Cyber-security and Privacy Laws", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Cybercrime and Law Enforcement", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Cyber Ethics:",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Cyber-security and Privacy Laws", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Cybercrime and Law Enforcement", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Project Work 1" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Renewable Energy Solutions:",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Urban Planning and Sustainable Development", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Urban Planning and Sustainable Development", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Renewable Energy Solutions:",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Urban Planning and Sustainable Development", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Urban Planning and Sustainable Development", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Renewable Energy Solutions:",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Urban Planning and Sustainable Development", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Urban Planning and Sustainable Development", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Digital Logic Design" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Algorithm Design & Analysis" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Cost and Management Accounting" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Basic Statistics & Probability" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Object Oriented Programming Language" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Data Science" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Numerical Analysis" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Theory of Computation and Concrete Mathematics" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Principles of Economics" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Project Work II" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Database System" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Communication Engineering" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Microprocessor & Interfacing" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Operating System and System Programming" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Software Engineering & Design Patterns" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Computer Networking" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Computer Graphics and Image Processing" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Technical Writing And Presentation" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Artificial Intelligence" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Compiler Construction" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Web Engineering" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Option I" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Thesis / Project I" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Digital Signal Processing" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Option II" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )
        "Thesis / Project II" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Number Systems and Codes",
                            "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1f2iRpXztZzZms8-pSyiWJDASA5mROQlp/view?usp=sharing")
                    )
                ),
            )
        )

        else -> null
    }
}

data class VideoCourse(
    val courseTitle: String,
    val chapters: List<Chapter>
) {
    data class Chapter(
        val chapterName: String,
        val topicList: List<TopicItem>
    ) {
        data class TopicItem(
            val topicName: String,
            val link: String
        )
    }
}
