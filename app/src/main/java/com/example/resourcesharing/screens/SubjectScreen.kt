package com.example.resourcesharing.screens

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.resourcesharing.navigation.PostOfficeAppRouter
import com.example.resourcesharing.navigation.Screen
import com.example.resourcesharing.navigation.SystemBackButtonHandler
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


@Composable
fun SubjectScreen(course: String, id: Int) {

    val viewModel: SubjectViewModel = viewModel()
    val context = LocalContext.current

    val getContent = viewModel.rememberLauncher(id)

    var  courseTitle by remember { mutableStateOf(course) }
    var isReturningFromOtherPage by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = courseTitle) {
        // Initialize files for the initially selected course title
        if (isReturningFromOtherPage) {
            viewModel.fetchFiles(courseTitle)
        }
    }

    SystemBackButtonHandler {
        isReturningFromOtherPage = true
        PostOfficeAppRouter.navigateTo(Screen.CourseScreen(id))
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

            Column( // Make elements horizontally centered
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                OutlinedTextField(
                    value = courseTitle,
                    onValueChange = {
                        courseTitle = it
                        Log.d("SubjectScreen", "Course title changed to: $courseTitle")
                        viewModel.fetchFiles(courseTitle) // Trigger fetch on title change
                    },
                    label = { Text("Course Title") },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Button(
                    onClick = { getContent.launch("*/*") },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {
                    Text("Upload File")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    PostOfficeAppRouter.navigateTo(Screen.ImageScreen(course, id))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(color = Color.DarkGray),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(text = "See Video content(chapter-wise)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Separate LazyColumn for FileButton from the main Column
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(viewModel.filesForCourse) { index, file ->
                    val topicNumber = index + 1
                    FileButton(file = file, courseTitle = courseTitle, topicNumber = topicNumber)
                    Spacer(modifier = Modifier.height(8.dp)) // Add spacing between FileButton elements
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Display getSubjectData separately
            getSubjectData(courseTitle)?.chapters?.forEach { chapter ->
                ChapterSection(chapter = chapter, context = context)
                Spacer(modifier = Modifier.height(16.dp))
            }


        }
    }
}



@Composable
fun ChapterSection(chapter: Course.Chapter, context: Context) {
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
                TopicCard(topic = topic, context = context)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun TopicCard(topic: Course.Chapter.TopicItem, context: Context) {
    Card(
        modifier = Modifier.clickable {
            // Handle click, open link in browser
            launchInBrowser(context, topic.link)
        },
    ) {
        Text(
            text = topic.topicName,
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp
        )
    }
}


private fun launchInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.`package` = "com.android.chrome"
    if (context.packageManager.resolveActivity(intent, 0) == null) {
        // Chrome browser app not installed, fallback to system default browser
        intent.`package` = null
    }
    context.startActivity(intent)
}


data class Course(
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

fun getSubjectData(courseTitle: String): Course? {
    return when (courseTitle) {
        "Structured Programming Language" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Programming", "https://drive.google.com/file/d/1M_KROr_UKVO4WGRf5pYGzYL9LrOzKLyl/view?usp=sharing"),
                        Course.Chapter.TopicItem("Variables and Data Types", "https://drive.google.com/file/d/1M_KROr_UKVO4WGRf5pYGzYL9LrOzKLyl/view?usp=sharing"),
                        Course.Chapter.TopicItem("Control Structures", "https://drive.google.com/file/d/1M_KROr_UKVO4WGRf5pYGzYL9LrOzKLyl/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Arrays and Strings", "https://drive.google.com/file/d/1M_KROr_UKVO4WGRf5pYGzYL9LrOzKLyl/view?usp=sharing"),
                        Course.Chapter.TopicItem("Functions", "https://drive.google.com/file/d/1M_KROr_UKVO4WGRf5pYGzYL9LrOzKLyl/view?usp=sharing"),
                        Course.Chapter.TopicItem("Files and Input/Output", "https://drive.google.com/file/d/1M_KROr_UKVO4WGRf5pYGzYL9LrOzKLyl/view?usp=sharing")
                    )
                ),

            )
        )
        "Discrete Mathematics" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Sets, Relations, and Functions", "https://drive.google.com/file/d/1SiRsQRJTjqDJUD_mHmqDBR2khoA55FK5/view?usp=sharing"),
                        Course.Chapter.TopicItem("Logic and Propositional Calculus", "https://drive.google.com/file/d/1SiRsQRJTjqDJUD_mHmqDBR2khoA55FK5/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Proof Techniques", "https://drive.google.com/file/d/1LgS8C_moFZvr8_n9KymakFvbfwrMhZq6/view?usp=sharing"),
                        Course.Chapter.TopicItem("Graph Theory", "https://drive.google.com/file/d/1LgS8C_moFZvr8_n9KymakFvbfwrMhZq6/view?usp=sharing")
                    )
                ),


            )
        )
        "Electrical Circuits" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction", "https://drive.google.com/file/d/191SVDI62pxA-KM1rZQuVXVJ_MKo5v81e/view?usp=sharing"),
                        Course.Chapter.TopicItem("Charge and Current", "https://drive.google.com/file/d/191SVDI62pxA-KM1rZQuVXVJ_MKo5v81e/view?usp=sharing"),
                        Course.Chapter.TopicItem("Power and Energy", "https://drive.google.com/file/d/191SVDI62pxA-KM1rZQuVXVJ_MKo5v81e/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Nodes, Branches, and Loops", "https://drive.google.com/file/d/1eBFyQC8_PgZ7_qUR7CDJ1Jf3DoZdhiN1/view?usp=sharing"),
                        Course.Chapter.TopicItem("Kirchhoff’s Laws", "https://drive.google.com/file/d/1eBFyQC8_PgZ7_qUR7CDJ1Jf3DoZdhiN1/view?usp=sharing"),
                        Course.Chapter.TopicItem("Series Resistors and Voltage Division", "https://drive.google.com/file/d/1eBFyQC8_PgZ7_qUR7CDJ1Jf3DoZdhiN1/view?usp=sharing")
                    )
                ),

            )
        )
        "Matrices, Vector Analysis and Geometry" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Systems of Linear Equations and Matrices", "https://drive.google.com/file/d/1V-UFcd8UW7H4ieVrT6IUXz-6QHXxnuEH/view?usp=sharing"),
                        Course.Chapter.TopicItem("Determinants by Cofactor Expansion", "https://drive.google.com/file/d/1V-UFcd8UW7H4ieVrT6IUXz-6QHXxnuEH/view?usp=sharing"),
                        Course.Chapter.TopicItem("Evaluating Determinants by Row Reduction", "https://drive.google.com/file/d/1V-UFcd8UW7H4ieVrT6IUXz-6QHXxnuEH/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Vectors in 2-Space, 3-Space, and n-Space", "https://drive.google.com/file/d/1V-UFcd8UW7H4ieVrT6IUXz-6QHXxnuEH/view?usp=sharing"),
                        Course.Chapter.TopicItem("Euclidean Vector Spaces", "https://drive.google.com/file/d/1V-UFcd8UW7H4ieVrT6IUXz-6QHXxnuEH/view?usp=sharing"),
                        Course.Chapter.TopicItem("The Geometry of Linear Systems", "https://drive.google.com/file/d/1V-UFcd8UW7H4ieVrT6IUXz-6QHXxnuEH/view?usp=sharing")
                    )
                ),
            )
        )
        "English Language" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Grammar Essentials", "https://docs.google.com/document/d/12jfnNhNXZ4SN0fXezg8dk9Kd40HwoPkl/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Mastering Vocabulary", "https://docs.google.com/document/d/13dZ-AOeQsSHvUojoxBiVrGourB8uweTC/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Effective Communication Skills", "https://docs.google.com/document/d/1zv6wTUl5L-8anhPFTExpUBAY2hrt85nK/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Reading Comprehension", "https://docs.google.com/document/d/1_fvbB34GG3d_o7O_WwrWdJFYM7VIWklH/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Writing Skills Development", "https://docs.google.com/document/d/1_fvbB34GG3d_o7O_WwrWdJFYM7VIWklH/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Pronunciation and Accent Training", "https://docs.google.com/document/d/1_fvbB34GG3d_o7O_WwrWdJFYM7VIWklH/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                )
            )
        )
        "Data Structure" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Basic Terminology, Elementary Data Organization", "https://drive.google.com/file/d/1EhWox7BIhkFrMQAwpIeaJ-7y1RsTA61R/view?usp=sharing"),
                        Course.Chapter.TopicItem("Variables and Data Types", "https://drive.google.com/file/d/1EhWox7BIhkFrMQAwpIeaJ-7y1RsTA61R/view?usp=sharing"),
                        Course.Chapter.TopicItem("Data Structure Operation", "https://drive.google.com/file/d/1EhWox7BIhkFrMQAwpIeaJ-7y1RsTA61R/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Mathematical Notation and Function", "https://drive.google.com/file/d/1lr9emd4VEC7FNP7i8sdPNwZCJRd-ua6b/view?usp=sharing"),
                        Course.Chapter.TopicItem("Complexity of Algorithms", "https://drive.google.com/file/d/1lr9emd4VEC7FNP7i8sdPNwZCJRd-ua6b/view?usp=sharing"),
                        Course.Chapter.TopicItem("Searching :linear Searching", "https://drive.google.com/file/d/1lr9emd4VEC7FNP7i8sdPNwZCJRd-ua6b/view?usp=sharing")
                    )
                ),

            )
        )
        "Electronic Devices and Circuits" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Semiconductor Materials: Ge, Si, and GaAs", "https://drive.google.com/file/d/1sreEbMme27lN4D-K9mDn43ZnikOQjvLA/view?usp=sharing"),
                        Course.Chapter.TopicItem("Variables and Data Types", "https://drive.google.com/file/d/1sreEbMme27lN4D-K9mDn43ZnikOQjvLA/view?usp=sharing"),
                        Course.Chapter.TopicItem("Covalent Bonding and Intrinsic Materials", "https://drive.google.com/file/d/1cw_Z3buueKHQvDLhVKRhPMX9_llqcrc7/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Parallel and Series–Parallel Configurations", "https://drive.google.com/file/d/1wfGblFsNsaej4FC0aXhZFophwxTZKIQ8/view?usp=sharing"),
                        Course.Chapter.TopicItem("Sinusoidal Inputs; Half-Wave Rectification", "https://drive.google.com/file/d/1wfGblFsNsaej4FC0aXhZFophwxTZKIQ8/view?usp=sharing"),
                        Course.Chapter.TopicItem("Transistor Construction", "https://drive.google.com/file/d/1wfGblFsNsaej4FC0aXhZFophwxTZKIQ8/view?usp=sharing")
                    )
                ),

            )
        )
        "Mechanics, Wave, Heat & Thermodynamics" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("MEASURING THINGS, INCLUDING LENGTHS", "https://drive.google.com/file/d/1kwD3W9AaPrNBD2o0DaDhRxc6zCtTBa1h/view?usp=sharing"),
                        Course.Chapter.TopicItem("POSITION, DISPLACEMENT, AND AVERAGE VELOCITY", "https://drive.google.com/file/d/1vPCdX2cbuugxOGiXj8LZqz6-MCmDfVWg/view?usp=sharing"),
                        Course.Chapter.TopicItem("Average Velocity and Average Speed", "https://drive.google.com/file/d/1vPCdX2cbuugxOGiXj8LZqz6-MCmDfVWg/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Adding Vectors Geometrically", "https://drive.google.com/file/d/1ocdbgnmwsUIEm29YT8Y3osw50OKiH1Z9/view?usp=sharing"),
                        Course.Chapter.TopicItem("Newtonian Mechanics", "https://drive.google.com/file/d/1ocdbgnmwsUIEm29YT8Y3osw50OKiH1Z9/view?usp=sharing"),
                        Course.Chapter.TopicItem("Vectors and Scalars", "https://drive.google.com/file/d/1ocdbgnmwsUIEm29YT8Y3osw50OKiH1Z9/view?usp=sharing")
                    )
                )
            )
        )
        "Calculus" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Integration", "https://docs.google.com/presentation/d/1_730x4pWgDgZhxHRARwS0dTQ5RCTceAf/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Beta ,Gamma & Improper Integral (1)", "https://docs.google.com/presentation/d/1_730x4pWgDgZhxHRARwS0dTQ5RCTceAf/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("reduction formula", "https://docs.google.com/presentation/d/1_730x4pWgDgZhxHRARwS0dTQ5RCTceAf/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Differentiation", "https://docs.google.com/presentation/d/1CYVJ9SL2U1ldvAutMK88tUH2bPLfYRCL/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Differential Equation", "https://docs.google.com/presentation/d/1CYVJ9SL2U1ldvAutMK88tUH2bPLfYRCL/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("application of 1st order 1st degree", "https://docs.google.com/presentation/d/1CYVJ9SL2U1ldvAutMK88tUH2bPLfYRCL/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),

            )
        )
        "Ethics and Cyber Law" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Cyber Ethics", "https://drive.google.com/file/d/19reG0mrdBCnKqMpUSCChJARjAIlb6OFF/view?usp=drive_link"),
                        Course.Chapter.TopicItem("Cyber-security and Privacy Laws", "https://drive.google.com/file/d/19reG0mrdBCnKqMpUSCChJARjAIlb6OFF/view?usp=drive_link"),
                        Course.Chapter.TopicItem("Ethical Hacking and Penetration Testing:", "https://drive.google.com/file/d/1ML0S-tSqcNMgrG28KfDk_achimY9CkS3/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Intellectual Property Rights in the Digital Age", "https://drive.google.com/file/d/1ML0S-tSqcNMgrG28KfDk_achimY9CkS3/view?usp=sharing"),
                        Course.Chapter.TopicItem("Cybercrime and Law Enforcement", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing"),
                        Course.Chapter.TopicItem("Ethical Considerations in Data Management", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing")
                    )
                ),

            )
        )
        "Project Work 1" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Renewable Energy Solutions", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing"),
                        Course.Chapter.TopicItem("Urban Planning and Sustainable Development", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing"),
                        Course.Chapter.TopicItem("Environmental Conservation Initiatives", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Healthcare Access and Equity", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing"),
                        Course.Chapter.TopicItem("Education Reform:", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing"),
                        Course.Chapter.TopicItem("Technology for Social Good", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing")
                    )
                )

            )
        )
        "Digital Logic Design" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Binary, Decimal, Octal, and Hexadecimal Number Systems", "https://drive.google.com/file/d/1kAQf7aA3xzn6BpHVjpAPdmmWiJolLWby/view?usp=sharing"),
                        Course.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1kAQf7aA3xzn6BpHVjpAPdmmWiJolLWby/view?usp=sharing"),
                        Course.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1kAQf7aA3xzn6BpHVjpAPdmmWiJolLWby/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Sum-of-Products (SOP) and Product-of-Sums (POS) Forms", "https://drive.google.com/file/d/1VLT-MQjbm2mGxvDIrSmzi_9JLjzo0vhu/view?usp=sharing"),
                        Course.Chapter.TopicItem("Karnaugh Maps (K-Maps) and Simplification Techniques", "https://drive.google.com/file/d/1VLT-MQjbm2mGxvDIrSmzi_9JLjzo0vhu/view?usp=sharing"),
                        Course.Chapter.TopicItem("Analysis and Design of Combination Circuits", "https://drive.google.com/file/d/1VLT-MQjbm2mGxvDIrSmzi_9JLjzo0vhu/view?usp=sharing")
                    )
                )
            )
        )
        "Algorithm Design & Analysis" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Overview of Algorithms", "https://drive.google.com/file/d/1jCIQrKZhkl1QW9V7DVJcFZk_JXT0Qd6S/view?usp=sharing"),
                        Course.Chapter.TopicItem("Asymptotic Notation (Big O, Big Omega, Big Theta)", "https://drive.google.com/file/d/1jCIQrKZhkl1QW9V7DVJcFZk_JXT0Qd6S/view?usp=sharing"),
                        Course.Chapter.TopicItem("Time Complexity and Space Complexity", "https://drive.google.com/file/d/1jCIQrKZhkl1QW9V7DVJcFZk_JXT0Qd6S/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Divide and Conquer Paradigm", "https://drive.google.com/file/d/1jCIQrKZhkl1QW9V7DVJcFZk_JXT0Qd6S/view?usp=sharing"),
                        Course.Chapter.TopicItem("Greedy Strategy and Selection Criteria", "https://drive.google.com/file/d/1jCIQrKZhkl1QW9V7DVJcFZk_JXT0Qd6S/view?usp=sharing"),
                        Course.Chapter.TopicItem("Activity Selection Problem", "https://drive.google.com/file/d/1jCIQrKZhkl1QW9V7DVJcFZk_JXT0Qd6S/view?usp=sharing")
                    )
                ),

            )
        )
        "Cost and Management Accounting" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Cost and Management Accounting:", "https://drive.google.com/file/d/1xR6hZIgssm8sOHAHU4r-3g-UyJAaJLgO/view?usp=sharing"),
                        Course.Chapter.TopicItem("Cost Classification and Behavior:", "https://drive.google.com/file/d/1xR6hZIgssm8sOHAHU4r-3g-UyJAaJLgO/view?usp=sharing"),
                        Course.Chapter.TopicItem("Cost-Volume-Profit Analysis", "https://drive.google.com/file/d/1xR6hZIgssm8sOHAHU4r-3g-UyJAaJLgO/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Job Costing", "https://drive.google.com/file/d/1xR6hZIgssm8sOHAHU4r-3g-UyJAaJLgO/view?usp=sharing"),
                        Course.Chapter.TopicItem("Cost Estimation and Costing Systems", "https://drive.google.com/file/d/1xR6hZIgssm8sOHAHU4r-3g-UyJAaJLgO/view?usp=sharing"),
                        Course.Chapter.TopicItem("Standard Costing and Variance Analysis", "https://drive.google.com/file/d/1xR6hZIgssm8sOHAHU4r-3g-UyJAaJLgO/view?usp=sharing")
                    )
                ),

            )
        )
        "Basic Statistics & Probability" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Definition and Scope of Statistics", "https://drive.google.com/file/d/1VfGjxupqni_NQBcYHqItje4ciQfjFqdq/view?usp=sharing"),
                        Course.Chapter.TopicItem("Measures of Central Tendency (Mean, Median, Mode)", "https://drive.google.com/file/d/1VfGjxupqni_NQBcYHqItje4ciQfjFqdq/view?usp=sharing"),
                        Course.Chapter.TopicItem("Importance of Statistics in Various Fields", "https://drive.google.com/file/d/1VfGjxupqni_NQBcYHqItje4ciQfjFqdq/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Definition of Probability", "https://drive.google.com/file/d/10PUuPvF04JffEl_b7cti7FWYJ9rG9yfl/view?usp=sharing"),
                        Course.Chapter.TopicItem("Binomial Distribution", "https://drive.google.com/file/d/10PUuPvF04JffEl_b7cti7FWYJ9rG9yfl/view?usp=sharing"),
                        Course.Chapter.TopicItem("Hyper-geometric Distribution", "https://drive.google.com/file/d/10PUuPvF04JffEl_b7cti7FWYJ9rG9yfl/view?usp=sharing")
                    )
                ),

            )
        )
        "Object Oriented Programming Language" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Object-Oriented Programming", "https://drive.google.com/file/d/1o6d610n5W671kMgbx2qJK02R85GqG7E9/view?usp=sharing"),
                        Course.Chapter.TopicItem("Inheritance", "https://drive.google.com/file/d/1o6d610n5W671kMgbx2qJK02R85GqG7E9/view?usp=sharing"),
                        Course.Chapter.TopicItem("Polymorphism", "https://drive.google.com/file/d/1o6d610n5W671kMgbx2qJK02R85GqG7E9/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Function Overloading", "https://drive.google.com/file/d/1o6d610n5W671kMgbx2qJK02R85GqG7E9/view?usp=sharing"),
                        Course.Chapter.TopicItem("Operator Overloading", "https://drive.google.com/file/d/1o6d610n5W671kMgbx2qJK02R85GqG7E9/view?usp=sharing"),
                        Course.Chapter.TopicItem("Base and Derived Classes", "https://drive.google.com/file/d/1o6d610n5W671kMgbx2qJK02R85GqG7E9/view?usp=sharing")
                    )
                ),

            )
        )
        "Data Science" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Data Science:", "https://drive.google.com/file/d/1VUExuzZvP9AuDZZ_ii8tZqV8CU5QPnSA/view?usp=sharing"),
                        Course.Chapter.TopicItem("Data Collection Methods", "https://drive.google.com/file/d/1zl5wATp2bs5XwJ3VwSesv_5pondmEMOM/view?usp=sharing"),
                        Course.Chapter.TopicItem("Data Integration and Aggregation", "https://drive.google.com/file/d/1zl5wATp2bs5XwJ3VwSesv_5pondmEMOM/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Exploratory Data Analysis (EDA)", "https://drive.google.com/file/d/1poE6djcFc4vyz-V_fdaS9kbhjaa7fPCV/view?usp=sharing"),
                        Course.Chapter.TopicItem("Correlation Analysis", "https://drive.google.com/file/d/1poE6djcFc4vyz-V_fdaS9kbhjaa7fPCV/view?usp=sharing"),
                        Course.Chapter.TopicItem("Probability Distributions", "https://drive.google.com/file/d/1poE6djcFc4vyz-V_fdaS9kbhjaa7fPCV/view?usp=sharing")
                    )
                ),

            )
        )
        "Numerical Analysis" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Numerical Analysis", "https://drive.google.com/file/d/1rkYIiC17ZarCKD_cMpWRfQ4cyNJHVE-G/view?usp=sharing"),
                        Course.Chapter.TopicItem("Root Finding Methods", "https://drive.google.com/file/d/1V2Wijl7PfpE7taxrhfBIt9yBHtiLzaed/view?usp=sharing"),
                        Course.Chapter.TopicItem("Bisection Method", "https://drive.google.com/file/d/1V2Wijl7PfpE7taxrhfBIt9yBHtiLzaed/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Linear Algebraic Equations", "https://drive.google.com/file/d/1IT18hyeaND9QrdG4eFNv-P0N6c0v0UeC/view?usp=sharing"),
                        Course.Chapter.TopicItem("Gaussian Elimination", "https://drive.google.com/file/d/1IT18hyeaND9QrdG4eFNv-P0N6c0v0UeC/view?usp=sharing"),
                        Course.Chapter.TopicItem("LU Decomposition", "https://drive.google.com/file/d/1IT18hyeaND9QrdG4eFNv-P0N6c0v0UeC/view?usp=sharing")
                    )
                ),

            )
        )
        "Theory of Computation and Concrete Mathematics" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Overview of Automata Theory", "https://drive.google.com/file/d/1M4kjeMovsH36ULMNKK-LQHdptE2vLSrN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Finite Automata", "https://drive.google.com/file/d/1M4kjeMovsH36ULMNKK-LQHdptE2vLSrN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Regular Expressions", "https://drive.google.com/file/d/1M4kjeMovsH36ULMNKK-LQHdptE2vLSrN/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Deterministic Finite Automata (DFA)", "https://drive.google.com/file/d/1XArmlbtgIc5KZus-z0hEySDgSFiPNl8K/view?usp=sharing"),
                        Course.Chapter.TopicItem("Non-deterministic Finite Automata (NFA)", "https://drive.google.com/file/d/1XArmlbtgIc5KZus-z0hEySDgSFiPNl8K/view?usp=sharing"),
                        Course.Chapter.TopicItem("Pushdown Automata (PDA)", "https://drive.google.com/file/d/1XArmlbtgIc5KZus-z0hEySDgSFiPNl8K/view?usp=sharing")
                    )
                ),

            )
        )
        "Principles of Economics" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Economics", "https://drive.google.com/file/d/1W_KLVNwsnMqZgSaitklBqZXjS3ATL0Af/view?usp=sharing"),
                        Course.Chapter.TopicItem("Demand and Supply Analysis", "https://drive.google.com/file/d/1W_KLVNwsnMqZgSaitklBqZXjS3ATL0Af/view?usp=sharing"),
                        Course.Chapter.TopicItem("Production and Costs", "https://drive.google.com/file/d/1W_KLVNwsnMqZgSaitklBqZXjS3ATL0Af/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Consumer Behavior and Utility Theory", "https://drive.google.com/file/d/187FrpTRN7SfBAH_k8j9jXid1wm2gdMt0/view?usp=sharing"),
                        Course.Chapter.TopicItem("National Income Accounting", "https://drive.google.com/file/d/187FrpTRN7SfBAH_k8j9jXid1wm2gdMt0/view?usp=sharing"),
                        Course.Chapter.TopicItem("Fiscal Policy", "https://drive.google.com/file/d/187FrpTRN7SfBAH_k8j9jXid1wm2gdMt0/view?usp=sharing")
                    )
                ),

            )
        )
        "Project Work II" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Digital Marketing Strategies", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing"),
                        Course.Chapter.TopicItem("E-commerce Platform Development", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Artificial Intelligence in Business", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Cyber-security Solutions", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Sustainable Business Practices", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Financial Portfolio Management", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing")
                    )
                ),

            )
        )
        "Database System" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Definition and Importance of Databases", "https://docs.google.com/presentation/d/1-61Ty6f4k4rKIH6_7H6mjDtdTluHqRHT/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Relational Model", "https://docs.google.com/presentation/d/1QANYDZM3FFmh4QFN-4fwXQO6-iR-wtkX/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Characteristics of Databases", "https://docs.google.com/presentation/d/1QANYDZM3FFmh4QFN-4fwXQO6-iR-wtkX/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Entity-Relationship (ER) Model", "https://docs.google.com/presentation/d/1-61Ty6f4k4rKIH6_7H6mjDtdTluHqRHT/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Introduction to Relational Databases", "https://docs.google.com/presentation/d/1-61Ty6f4k4rKIH6_7H6mjDtdTluHqRHT/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("SQL (Structured Query Language)", "https://docs.google.com/presentation/d/1-61Ty6f4k4rKIH6_7H6mjDtdTluHqRHT/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),

            )
        )

        "Communication Engineering" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Overview of Communication Systems", "https://drive.google.com/file/d/1tzMCksC8JFz_sRi34ZhaJtpup6oxecmG/view?usp=sharing"),
                        Course.Chapter.TopicItem("Basic Components of a Communication System", "https://drive.google.com/file/d/1tzMCksC8JFz_sRi34ZhaJtpup6oxecmG/view?usp=sharing"),
                        Course.Chapter.TopicItem("Amplitude Modulation (AM)", "https://drive.google.com/file/d/1tzMCksC8JFz_sRi34ZhaJtpup6oxecmG/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Frequency Modulation (FM)", "https://drive.google.com/file/d/1GEcbAPIEz5sOIP-Vn78eHHPUQx7D-HCT/view?usp=sharing"),
                        Course.Chapter.TopicItem("Phase Modulation (PM)", "https://drive.google.com/file/d/1GEcbAPIEz5sOIP-Vn78eHHPUQx7D-HCT/view?usp=sharing"),
                        Course.Chapter.TopicItem("Pulse Amplitude Modulation (PAM)", "https://drive.google.com/file/d/1GEcbAPIEz5sOIP-Vn78eHHPUQx7D-HCT/view?usp=sharing")
                    )
                ),

            )
        )
        "Microprocessor & Interfacing" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Basics of Microprocessors and Microcontrollers", "https://drive.google.com/file/d/1_99agJ3lCKlBGVFpAzSSM73PbLuxXWuD/view?usp=sharing"),
                        Course.Chapter.TopicItem("Architecture of Microprocessors", "https://drive.google.com/file/d/1oo76Jb14QNAP7aPhmFwxMopPv-HMaody/view?usp=sharing"),
                        Course.Chapter.TopicItem("Control Structures", "https://drive.google.com/file/d/1oo76Jb14QNAP7aPhmFwxMopPv-HMaody/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("CPU Architecture (ALU, Control Unit)", "https://drive.google.com/file/d/1xSZ3PzRXNnpAhhStEVfNphAUmsOJL_gf/view?usp=sharing"),
                        Course.Chapter.TopicItem("Registers and Register Organization", "https://drive.google.com/file/d/1xSZ3PzRXNnpAhhStEVfNphAUmsOJL_gf/view?usp=sharing"),
                        Course.Chapter.TopicItem("Memory Mapping Techniques", "https://drive.google.com/file/d/1xSZ3PzRXNnpAhhStEVfNphAUmsOJL_gf/view?usp=sharing")
                    )
                )
            )
        )
        "Operating System and System Programming" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Operating Systems", "https://docs.google.com/presentation/d/1rZualYhp9XtTfoeHvrioI0jzM0R6aQAu/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Process Concept", "https://docs.google.com/presentation/d/1rZualYhp9XtTfoeHvrioI0jzM0R6aQAu/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Deadlock Handling", "https://docs.google.com/presentation/d/1rZualYhp9XtTfoeHvrioI0jzM0R6aQAu/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Memory Hierarchy", "https://docs.google.com/presentation/d/10j8_3lWS1R9H1N_E_VTLd3WKng4LEdEQ/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Logical vs. Physical Address Space", "https://docs.google.com/presentation/d/10j8_3lWS1R9H1N_E_VTLd3WKng4LEdEQ/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Virtual Memory", "https://docs.google.com/presentation/d/10j8_3lWS1R9H1N_E_VTLd3WKng4LEdEQ/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),

            )
        )
        "Software Engineering & Design Patterns" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Software Engineering", "https://docs.google.com/presentation/d/1Xho3OcX2OPmBT00T-1vt1W6tU1PXRIFk/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Principles of Software Engineering", "https://docs.google.com/presentation/d/1Xho3OcX2OPmBT00T-1vt1W6tU1PXRIFk/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("DevOps Practices", "https://docs.google.com/presentation/d/1Xho3OcX2OPmBT00T-1vt1W6tU1PXRIFk/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Software Requirements Document", "https://docs.google.com/presentation/d/18X-kAiqu5QYIeOVyDzkJqZRogl64LfV8/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Design Patterns", "https://docs.google.com/presentation/d/18X-kAiqu5QYIeOVyDzkJqZRogl64LfV8/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Object-Oriented Design (OOD)", "https://docs.google.com/presentation/d/18X-kAiqu5QYIeOVyDzkJqZRogl64LfV8/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),

            )
        )
        "Computer Networking" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Computer Networks", "https://drive.google.com/file/d/1ZBqMT_ZUxi40UVEH-ZkU1iyg3vxYA2YQ/view?usp=sharing"),
                        Course.Chapter.TopicItem("TCP/IP Protocol Suite", "https://drive.google.com/file/d/1ZBqMT_ZUxi40UVEH-ZkU1iyg3vxYA2YQ/view?usp=sharing"),
                        Course.Chapter.TopicItem("Ethernet, IP, TCP, UDP Protocols", "https://drive.google.com/file/d/1ZBqMT_ZUxi40UVEH-ZkU1iyg3vxYA2YQ/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Physical Layer", "https://drive.google.com/file/d/1lIePQ5McNF3UYPjhmzRZO9U5CdpyCedg/view?usp=sharing"),
                        Course.Chapter.TopicItem("Multiplexing Techniques", "https://drive.google.com/file/d/1lIePQ5McNF3UYPjhmzRZO9U5CdpyCedg/view?usp=sharing"),
                        Course.Chapter.TopicItem("Introduction to Data Link Layer", "https://drive.google.com/file/d/1lIePQ5McNF3UYPjhmzRZO9U5CdpyCedg/view?usp=sharing")
                    )
                ),

            )
        )
        "Computer Graphics and Image Processing" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Basics of Computer Graphics", "https://drive.google.com/file/d/19FgHGKusX5vosUugFHxOxjgw2XsM1rJ6/view?usp=sharing"),
                        Course.Chapter.TopicItem("Applications of Computer Graphics", "https://drive.google.com/file/d/19FgHGKusX5vosUugFHxOxjgw2XsM1rJ6/view?usp=sharing"),
                        Course.Chapter.TopicItem("Components of a Graphics System", "https://drive.google.com/file/d/19FgHGKusX5vosUugFHxOxjgw2XsM1rJ6/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("3D Modeling and Rendering", "https://drive.google.com/file/d/1E-ez-NEO91ucXM_Gssr2sU_AcCNKS_75/view?usp=sharing"),
                        Course.Chapter.TopicItem("Projection and Viewing", "https://drive.google.com/file/d/1E-ez-NEO91ucXM_Gssr2sU_AcCNKS_75/view?usp=sharing"),
                        Course.Chapter.TopicItem("Lighting and Shading Techniques", "https://drive.google.com/file/d/1E-ez-NEO91ucXM_Gssr2sU_AcCNKS_75/view?usp=sharing")
                    )
                ),

            )
        )
        "Technical Writing And Presentation" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Technical Writing", "https://drive.google.com/file/d/1WFPTb2pWHMdgXfQpX4zTcekVDqCAKqPo/view?usp=sharing"),
                        Course.Chapter.TopicItem("Audience Analysis", "https://drive.google.com/file/d/1WFPTb2pWHMdgXfQpX4zTcekVDqCAKqPo/view?usp=sharing"),
                        Course.Chapter.TopicItem("Defining Objectives and Deliverables", "https://drive.google.com/file/d/1WFPTb2pWHMdgXfQpX4zTcekVDqCAKqPo/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Outlining and Structuring Documents", "https://drive.google.com/file/d/1WFPTb2pWHMdgXfQpX4zTcekVDqCAKqPo/view?usp=sharing"),
                        Course.Chapter.TopicItem("Writing Style and Tone", "https://drive.google.com/file/d/1WFPTb2pWHMdgXfQpX4zTcekVDqCAKqPo/view?usp=sharing"),
                        Course.Chapter.TopicItem("Use of Plain Language", "https://drive.google.com/file/d/1WFPTb2pWHMdgXfQpX4zTcekVDqCAKqPo/view?usp=sharing")
                    )
                )
            )
        )
        "Artificial Intelligence" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Artificial Intelligence", "https://docs.google.com/presentation/d/1j55u2Z7he_sC7YhD_U3SvwkvfJkA_4lQ/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Definition and Goals of Artificial Intelligence", "https://docs.google.com/presentation/d/1j55u2Z7he_sC7YhD_U3SvwkvfJkA_4lQ/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("History and Evolution of AI", "https://docs.google.com/presentation/d/1j55u2Z7he_sC7YhD_U3SvwkvfJkA_4lQ/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Problem Formulation", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("State Space Search", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Logic-Based Representation", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),

            )
        )
        "Compiler Construction" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Role of the Lexer (Scanner)", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Regular Expressions and Finite Automata", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Role of the Parser", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Overview of Compilers", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Compiler Structure and Components", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Phases of Compilation", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),

            )
        )
        "Web Engineering" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Overview of Web Engineering", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Client-Side Technologies", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Characteristics of Web Applications", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("JavaScript", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Responsive Web Design", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Database Integration (SQL, NoSQL)", "https://docs.google.com/presentation/d/1ZFyurYAfAdZZ76z2_PqWRNrO042VPU1m/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),

            )
        )
        "Option I" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("The Impact of Climate Change", "https://drive.google.com/file/d/1nSU_uFr2jSRwN9R3o9a2GyEqTU-jxN4m/view?usp=sharing"),
                        Course.Chapter.TopicItem("Gender Equality in Education", "https://drive.google.com/file/d/1nSU_uFr2jSRwN9R3o9a2GyEqTU-jxN4m/view?usp=sharing"),
                        Course.Chapter.TopicItem("Urbanization and Public Health", "https://drive.google.com/file/d/1nSU_uFr2jSRwN9R3o9a2GyEqTU-jxN4m/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Renewable Energy Technologies", "https://drive.google.com/file/d/1nSU_uFr2jSRwN9R3o9a2GyEqTU-jxN4m/view?usp=sharing"),
                        Course.Chapter.TopicItem("Wildlife Conservation Efforts", "https://drive.google.com/file/d/1nSU_uFr2jSRwN9R3o9a2GyEqTU-jxN4m/view?usp=sharing"),
                        Course.Chapter.TopicItem("Urbanization and Public Health", "https://drive.google.com/file/d/1nSU_uFr2jSRwN9R3o9a2GyEqTU-jxN4m/view?usp=sharing")
                    )
                )

            )
        )
        "Thesis / Project I" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("the Impact of Social Media", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Online Learning Platforms", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Artificial Intelligence", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("The Future of Renewable Energy", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Employee Motivation", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Organizational Performance", "https://drive.google.com/file/d/1zbqRWsYzT_mZVmtZ42bUGpiKV7FamDWN/view?usp=sharing")
                    )
                ),

            )
        )
        "Digital Signal Processing" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Basics of Signals and Systems", "https://drive.google.com/file/d/1oG5YwHXuWV1i2ArxPFvnoO_OJgPBu93T/view?usp=sharing"),
                        Course.Chapter.TopicItem("Analog vs. Digital Signal Processing", "https://drive.google.com/file/d/1oG5YwHXuWV1i2ArxPFvnoO_OJgPBu93T/view?usp=sharing"),
                        Course.Chapter.TopicItem("Discrete-Time Signals", "https://drive.google.com/file/d/1oG5YwHXuWV1i2ArxPFvnoO_OJgPBu93T/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Difference Equations", "https://drive.google.com/file/d/1oG5YwHXuWV1i2ArxPFvnoO_OJgPBu93T/view?usp=sharing"),
                        Course.Chapter.TopicItem("Aliasing and Anti-Aliasing Filters", "https://drive.google.com/file/d/1oG5YwHXuWV1i2ArxPFvnoO_OJgPBu93T/view?usp=sharing"),
                        Course.Chapter.TopicItem("DFT Definition and Properties", "https://drive.google.com/file/d/1oG5YwHXuWV1i2ArxPFvnoO_OJgPBu93T/view?usp=sharing")
                    )
                ),

            )
        )
        "Option II" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("topic 1", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing"),
                        Course.Chapter.TopicItem("topic 2", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing"),
                        Course.Chapter.TopicItem("topic 3", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("topic 4", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing"),
                        Course.Chapter.TopicItem("topic 5", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing"),
                        Course.Chapter.TopicItem("topic 6", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing")
                    )
                ),

            )
        )
        "Viva Voce" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("topic 1", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing"),
                        Course.Chapter.TopicItem("topic 2", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing"),
                        Course.Chapter.TopicItem("topic 3", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2(pdf/docs/pptx)",
                    topicList = listOf(
                        Course.Chapter.TopicItem("topic 4", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing"),
                        Course.Chapter.TopicItem("topic 5", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing"),
                        Course.Chapter.TopicItem("topic 6", "https://drive.google.com/file/d/1-n0lh5_JA8tsccyb9OrFYOJx2clbm8jW/view?usp=sharing")
                    )
                ),

            )
        )


        // Add cases for other course titles here
        else -> null
    }
}



@Composable
fun FileButton(file: FileItem, courseTitle: String, topicNumber: Int) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                file.fileUrl?.let { url ->
                    openFile(context, url)
                }
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display the course title concatenated with the topic number
            Text(text = "$courseTitle Topic $topicNumber")
        }
    }
}

fun openFile(context: Context, fileUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(fileUrl)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // Handle the case where no activity can handle the intent, e.g., no web browser installed
        // You can provide a fallback option or show a message to the user
    }
}

@SuppressLint("CommitPrefEdits")
class SubjectViewModel : ViewModel() {
    private val storage = FirebaseStorage.getInstance()
    private val database = FirebaseDatabase.getInstance()
    var filesForCourse by mutableStateOf<List<FileItem>>(emptyList())

    fun updateFilesForCourse(filteredFiles: List<FileItem>) {
        filesForCourse = filteredFiles
    }


    fun uploadFile(courseTitle: String, uri: Uri) {
        val storageRef = storage.reference
        val fileRef = storageRef.child("files/$courseTitle/${uri.lastPathSegment}")
        val uploadTask = fileRef.putFile(uri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Get the download URL of the uploaded file
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                // Store the file details in the Realtime Database
                val fileItem = FileItem(courseTitle, uri.toString())
                database.getReference("files").push().setValue(fileItem)
                    .addOnSuccessListener {
                        updateFilesForCourse(filesForCourse + fileItem)
                    }
                    .addOnFailureListener { exception ->
                        Log.w("SubjectViewModel", "Error adding file to database", exception)
                    }
            }.addOnFailureListener { exception ->
                Log.w("SubjectViewModel", "Error getting download URL", exception)
                // Handle download URL retrieval error
            }
        }.addOnFailureListener { exception ->
            // Handle unsuccessful uploads
            Log.w("SubjectViewModel", "Error uploading file", exception)
        }
    }

    fun fetchFiles(courseTitle: String) {
        val filesRef = database.getReference("files")
        val newFiles = mutableListOf<FileItem>()

        filesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                newFiles.clear()
                for (childSnapshot in snapshot.children) {
                    val fileItem = childSnapshot.getValue(FileItem::class.java)
                    if (fileItem != null && fileItem.courseTitle == courseTitle) {
                        newFiles.add(fileItem)
                    }
                }
                // Update the UI state
                updateFilesForCourse(newFiles)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


    @Composable
    fun rememberLauncher(id: Int) = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadFile("Course$id", uri)
        }
    }
}



data class FileItem(val courseTitle: String? = "", val fileUrl: String? = "")

@Preview
@Composable
fun SubjectScreenPreview() {
    SubjectScreen(course = "Data Structure", id = 2)
}


