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
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Functions", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing")
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
                            "https://drive.google.com/file/d/1V44RnJVbc996OY4ilir0Tb6m5GZNf5vO/view"
                        ),
                        VideoCourse.Chapter.TopicItem("Logic and Propositional Calculus", "https://drive.google.com/file/d/1V44RnJVbc996OY4ilir0Tb6m5GZNf5vO/view?usp=sharing")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Proof Techniques", ""),
                        VideoCourse.Chapter.TopicItem("Graph Theory", "link10")
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
                            "Sets, Relations, and Functions",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Logic and Propositional Calculus", "link8")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Proof Techniques", "https://drive.google.com/file/d/1V44RnJVbc996OY4ilir0Tb6m5GZNf5vO/view"),
                        VideoCourse.Chapter.TopicItem("Graph Theory", "link10")
                    )
                )
            )
        )
        "Matrices, Vector Analysis and Geometry" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Sets, Relations, and Functions",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Logic and Propositional Calculus", "link8")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Proof Techniques", "link9"),
                        VideoCourse.Chapter.TopicItem("Graph Theory", "link10")
                    )
                )
            )
        )
        "English Language" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Sets, Relations, and Functions",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Logic and Propositional Calculus", "link8")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Proof Techniques", "link9"),
                        VideoCourse.Chapter.TopicItem("Graph Theory", "link10")
                    )
                )
            )
        )
        "Data Structure" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Sets, Relations, and Functions",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Logic and Propositional Calculus", "link8")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Proof Techniques", "link9"),
                        VideoCourse.Chapter.TopicItem("Graph Theory", "link10")
                    )
                )
            )
        )
        "Electronic Devices and Circuits" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Sets, Relations, and Functions",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Logic and Propositional Calculus", "link8")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Proof Techniques", "link9"),
                        VideoCourse.Chapter.TopicItem("Graph Theory", "link10")
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
                            "Sets, Relations, and Functions",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Logic and Propositional Calculus", "link8")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Proof Techniques", "link9"),
                        VideoCourse.Chapter.TopicItem("Graph Theory", "link10")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Proof Techniques", "link9"),
                        VideoCourse.Chapter.TopicItem("Graph Theory", "link10"),
                        VideoCourse.Chapter.TopicItem("Graph Theory", "link10")

                )
                )
            )
        )
        "Mechanics, Wave, Heat & Thermodynamics" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Calculus" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Ethics and Cyber Law" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Project Work 1" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Digital Logic Design" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Algorithm Design & Analysis" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Cost and Management Accounting" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Basic Statistics & Probability" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Object Oriented Programming Language" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Data Science" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Numerical Analysis" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Theory of Computation and Concrete Mathematics" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Principles of Economics" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Project Work II" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Database System" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Communication Engineering" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Microprocessor & Interfacing" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Operating System and System Programming" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Software Engineering & Design Patterns" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Computer Networking" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Computer Graphics and Image Processing" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Technical Writing And Presentation" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Artificial Intelligence" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Compiler Construction" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Web Engineering" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Option I" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Thesis / Project I" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Digital Signal Processing" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Option II" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
            )
        )
        "Thesis / Project II" -> VideoCourse(
            courseTitle = courseTitle,
            chapters = listOf(
                VideoCourse.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem(
                            "Introduction to Programming",
                            "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"
                        ),
                        VideoCourse.Chapter.TopicItem("Variables and Data Types", "link2"),
                        VideoCourse.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                ),
                VideoCourse.Chapter(
                    chapterName = "Chapter 3",
                    topicList = listOf(
                        VideoCourse.Chapter.TopicItem("Arrays and Strings", "link4"),
                        VideoCourse.Chapter.TopicItem("Functions", "link5"),
                        VideoCourse.Chapter.TopicItem("Files and Input/Output", "link6")
                    )
                )
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
