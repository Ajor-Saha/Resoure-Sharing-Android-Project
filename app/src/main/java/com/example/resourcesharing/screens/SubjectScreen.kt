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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Programming", "https://drive.google.com/file/d/1M_KROr_UKVO4WGRf5pYGzYL9LrOzKLyl/view?usp=sharing"),
                        Course.Chapter.TopicItem("Variables and Data Types", "https://drive.google.com/file/d/1M_KROr_UKVO4WGRf5pYGzYL9LrOzKLyl/view?usp=sharing"),
                        Course.Chapter.TopicItem("Control Structures", "https://drive.google.com/file/d/1M_KROr_UKVO4WGRf5pYGzYL9LrOzKLyl/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Sets, Relations, and Functions", "https://drive.google.com/file/d/1SiRsQRJTjqDJUD_mHmqDBR2khoA55FK5/view?usp=sharing"),
                        Course.Chapter.TopicItem("Logic and Propositional Calculus", "https://drive.google.com/file/d/1SiRsQRJTjqDJUD_mHmqDBR2khoA55FK5/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction", "https://drive.google.com/file/d/191SVDI62pxA-KM1rZQuVXVJ_MKo5v81e/view?usp=sharing"),
                        Course.Chapter.TopicItem("Charge and Current", "https://drive.google.com/file/d/191SVDI62pxA-KM1rZQuVXVJ_MKo5v81e/view?usp=sharing"),
                        Course.Chapter.TopicItem("Power and Energy", "https://drive.google.com/file/d/191SVDI62pxA-KM1rZQuVXVJ_MKo5v81e/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Systems of Linear Equations and Matrices", "https://drive.google.com/file/d/1V-UFcd8UW7H4ieVrT6IUXz-6QHXxnuEH/view?usp=sharing"),
                        Course.Chapter.TopicItem("Determinants by Cofactor Expansion", "https://drive.google.com/file/d/1V-UFcd8UW7H4ieVrT6IUXz-6QHXxnuEH/view?usp=sharing"),
                        Course.Chapter.TopicItem("Evaluating Determinants by Row Reduction", "https://drive.google.com/file/d/1V-UFcd8UW7H4ieVrT6IUXz-6QHXxnuEH/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Grammar Essentials", "https://docs.google.com/document/d/12jfnNhNXZ4SN0fXezg8dk9Kd40HwoPkl/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Mastering Vocabulary", "https://docs.google.com/document/d/13dZ-AOeQsSHvUojoxBiVrGourB8uweTC/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Effective Communication Skills", "https://docs.google.com/document/d/1zv6wTUl5L-8anhPFTExpUBAY2hrt85nK/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Basic Terminology, Elementary Data Organization", "https://drive.google.com/file/d/1EhWox7BIhkFrMQAwpIeaJ-7y1RsTA61R/view?usp=sharing"),
                        Course.Chapter.TopicItem("Variables and Data Types", "https://drive.google.com/file/d/1EhWox7BIhkFrMQAwpIeaJ-7y1RsTA61R/view?usp=sharing"),
                        Course.Chapter.TopicItem("Data Structure Operation", "https://drive.google.com/file/d/1EhWox7BIhkFrMQAwpIeaJ-7y1RsTA61R/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Semiconductor Materials: Ge, Si, and GaAs", "https://drive.google.com/file/d/1sreEbMme27lN4D-K9mDn43ZnikOQjvLA/view?usp=sharing"),
                        Course.Chapter.TopicItem("Variables and Data Types", "https://drive.google.com/file/d/1sreEbMme27lN4D-K9mDn43ZnikOQjvLA/view?usp=sharing"),
                        Course.Chapter.TopicItem("Covalent Bonding and Intrinsic Materials", "https://drive.google.com/file/d/1cw_Z3buueKHQvDLhVKRhPMX9_llqcrc7/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("MEASURING THINGS, INCLUDING LENGTHS", "https://drive.google.com/file/d/1kwD3W9AaPrNBD2o0DaDhRxc6zCtTBa1h/view?usp=sharing"),
                        Course.Chapter.TopicItem("POSITION, DISPLACEMENT, AND AVERAGE VELOCITY", "https://drive.google.com/file/d/1vPCdX2cbuugxOGiXj8LZqz6-MCmDfVWg/view?usp=sharing"),
                        Course.Chapter.TopicItem("Average Velocity and Average Speed", "https://drive.google.com/file/d/1vPCdX2cbuugxOGiXj8LZqz6-MCmDfVWg/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Integration", "https://docs.google.com/presentation/d/1_730x4pWgDgZhxHRARwS0dTQ5RCTceAf/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("Beta ,Gamma & Improper Integral (1)", "https://docs.google.com/presentation/d/1_730x4pWgDgZhxHRARwS0dTQ5RCTceAf/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true"),
                        Course.Chapter.TopicItem("reduction formula", "https://docs.google.com/presentation/d/1_730x4pWgDgZhxHRARwS0dTQ5RCTceAf/edit?usp=sharing&ouid=110142282340470283003&rtpof=true&sd=true")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Cyber Ethics", "https://drive.google.com/file/d/19reG0mrdBCnKqMpUSCChJARjAIlb6OFF/view?usp=drive_link"),
                        Course.Chapter.TopicItem("Cyber-security and Privacy Laws", "https://drive.google.com/file/d/19reG0mrdBCnKqMpUSCChJARjAIlb6OFF/view?usp=drive_link"),
                        Course.Chapter.TopicItem("Ethical Hacking and Penetration Testing:", "https://drive.google.com/file/d/1ML0S-tSqcNMgrG28KfDk_achimY9CkS3/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Renewable Energy Solutions", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing"),
                        Course.Chapter.TopicItem("Urban Planning and Sustainable Development", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing"),
                        Course.Chapter.TopicItem("Environmental Conservation Initiatives", "https://drive.google.com/file/d/1I1nu56xtmPRMJckKZ81XSTn0KKBbQfmJ/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Binary, Decimal, Octal, and Hexadecimal Number Systems", "https://drive.google.com/file/d/1kAQf7aA3xzn6BpHVjpAPdmmWiJolLWby/view?usp=sharing"),
                        Course.Chapter.TopicItem("Complements (1's complement, 2's complement)", "https://drive.google.com/file/d/1kAQf7aA3xzn6BpHVjpAPdmmWiJolLWby/view?usp=sharing"),
                        Course.Chapter.TopicItem("Basic Logic Operations (AND, OR, NOT)", "https://drive.google.com/file/d/1kAQf7aA3xzn6BpHVjpAPdmmWiJolLWby/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Overview of Algorithms", "https://drive.google.com/file/d/1jCIQrKZhkl1QW9V7DVJcFZk_JXT0Qd6S/view?usp=sharing"),
                        Course.Chapter.TopicItem("Asymptotic Notation (Big O, Big Omega, Big Theta)", "https://drive.google.com/file/d/1jCIQrKZhkl1QW9V7DVJcFZk_JXT0Qd6S/view?usp=sharing"),
                        Course.Chapter.TopicItem("Time Complexity and Space Complexity", "https://drive.google.com/file/d/1jCIQrKZhkl1QW9V7DVJcFZk_JXT0Qd6S/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Cost and Management Accounting:", "https://drive.google.com/file/d/1xR6hZIgssm8sOHAHU4r-3g-UyJAaJLgO/view?usp=sharing"),
                        Course.Chapter.TopicItem("Cost Classification and Behavior:", "https://drive.google.com/file/d/1xR6hZIgssm8sOHAHU4r-3g-UyJAaJLgO/view?usp=sharing"),
                        Course.Chapter.TopicItem("Cost-Volume-Profit Analysis", "https://drive.google.com/file/d/1xR6hZIgssm8sOHAHU4r-3g-UyJAaJLgO/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Definition and Scope of Statistics", "https://drive.google.com/file/d/1VfGjxupqni_NQBcYHqItje4ciQfjFqdq/view?usp=sharing"),
                        Course.Chapter.TopicItem("Measures of Central Tendency (Mean, Median, Mode)", "https://drive.google.com/file/d/1VfGjxupqni_NQBcYHqItje4ciQfjFqdq/view?usp=sharing"),
                        Course.Chapter.TopicItem("Importance of Statistics in Various Fields", "https://drive.google.com/file/d/1VfGjxupqni_NQBcYHqItje4ciQfjFqdq/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Object-Oriented Programming", "https://drive.google.com/file/d/1o6d610n5W671kMgbx2qJK02R85GqG7E9/view?usp=sharing"),
                        Course.Chapter.TopicItem("Inheritance", "https://drive.google.com/file/d/1o6d610n5W671kMgbx2qJK02R85GqG7E9/view?usp=sharing"),
                        Course.Chapter.TopicItem("Polymorphism", "https://drive.google.com/file/d/1o6d610n5W671kMgbx2qJK02R85GqG7E9/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Data Science:", "https://drive.google.com/file/d/1VUExuzZvP9AuDZZ_ii8tZqV8CU5QPnSA/view?usp=sharing"),
                        Course.Chapter.TopicItem("Data Collection Methods", "https://drive.google.com/file/d/1zl5wATp2bs5XwJ3VwSesv_5pondmEMOM/view?usp=sharing"),
                        Course.Chapter.TopicItem("Data Integration and Aggregation", "https://drive.google.com/file/d/1zl5wATp2bs5XwJ3VwSesv_5pondmEMOM/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Numerical Analysis", "https://drive.google.com/file/d/1rkYIiC17ZarCKD_cMpWRfQ4cyNJHVE-G/view?usp=sharing"),
                        Course.Chapter.TopicItem("Root Finding Methods", "https://drive.google.com/file/d/1V2Wijl7PfpE7taxrhfBIt9yBHtiLzaed/view?usp=sharing"),
                        Course.Chapter.TopicItem("Bisection Method", "https://drive.google.com/file/d/1V2Wijl7PfpE7taxrhfBIt9yBHtiLzaed/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Overview of Automata Theory", "https://drive.google.com/file/d/1M4kjeMovsH36ULMNKK-LQHdptE2vLSrN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Finite Automata", "https://drive.google.com/file/d/1M4kjeMovsH36ULMNKK-LQHdptE2vLSrN/view?usp=sharing"),
                        Course.Chapter.TopicItem("Regular Expressions", "https://drive.google.com/file/d/1M4kjeMovsH36ULMNKK-LQHdptE2vLSrN/view?usp=sharing")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
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
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Economics", "https://drive.google.com/file/d/1W_KLVNwsnMqZgSaitklBqZXjS3ATL0Af/view?usp=sharing"),
                        Course.Chapter.TopicItem("Demand and Supply Analysis", "https://drive.google.com/file/d/1W_KLVNwsnMqZgSaitklBqZXjS3ATL0Af/view?usp=sharing"),
                        Course.Chapter.TopicItem("Production and Costs", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Consumer Behavior and Utility Theory", "link4"),
                        Course.Chapter.TopicItem("National Income Accounting", "link5"),
                        Course.Chapter.TopicItem("Fiscal Policy", "link6")
                    )
                ),

            )
        )
        "Project Work II" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Digital Marketing Strategies", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("E-commerce Platform Development", "link2"),
                        Course.Chapter.TopicItem("Artificial Intelligence in Business", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Cyber-security Solutions", "link4"),
                        Course.Chapter.TopicItem("Sustainable Business Practices", "link5"),
                        Course.Chapter.TopicItem("Financial Portfolio Management", "link6")
                    )
                ),

            )
        )
        "Database System" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Definition and Importance of Databases", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Relational Model", "link2"),
                        Course.Chapter.TopicItem("Characteristics of Databases", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Entity-Relationship (ER) Model", "link4"),
                        Course.Chapter.TopicItem("Introduction to Relational Databases", "link5"),
                        Course.Chapter.TopicItem("SQL (Structured Query Language)", "link6")
                    )
                ),

            )
        )

        "Communication Engineering" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Overview of Communication Systems", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Basic Components of a Communication System", "link2"),
                        Course.Chapter.TopicItem("Amplitude Modulation (AM)", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Frequency Modulation (FM)", "link4"),
                        Course.Chapter.TopicItem("Phase Modulation (PM)", "link5"),
                        Course.Chapter.TopicItem("Pulse Amplitude Modulation (PAM)", "link6")
                    )
                ),

            )
        )
        "Microprocessor & Interfacing" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Basics of Microprocessors and Microcontrollers", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Architecture of Microprocessors", "link2"),
                        Course.Chapter.TopicItem("Control Structures", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("CPU Architecture (ALU, Control Unit)", "link4"),
                        Course.Chapter.TopicItem("Registers and Register Organization", "link5"),
                        Course.Chapter.TopicItem("Memory Mapping Techniques", "link6")
                    )
                )
            )
        )
        "Operating System and System Programming" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Operating Systems", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Process Concept", "link2"),
                        Course.Chapter.TopicItem("Deadlock Handling", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Memory Hierarchy", "link4"),
                        Course.Chapter.TopicItem("Logical vs. Physical Address Space", "link5"),
                        Course.Chapter.TopicItem("Virtual Memory", "link6")
                    )
                ),

            )
        )
        "Software Engineering & Design Patterns" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Software Engineering", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Principles of Software Engineering", "link2"),
                        Course.Chapter.TopicItem("DevOps Practices", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Software Requirements Document", "link4"),
                        Course.Chapter.TopicItem("Design Patterns", "link5"),
                        Course.Chapter.TopicItem("Object-Oriented Design (OOD)", "link6")
                    )
                ),

            )
        )
        "Computer Networking" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Computer Networks", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("TCP/IP Protocol Suite", "link2"),
                        Course.Chapter.TopicItem("Ethernet, IP, TCP, UDP Protocols", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Physical Layer", "link4"),
                        Course.Chapter.TopicItem("Multiplexing Techniques", "link5"),
                        Course.Chapter.TopicItem("Introduction to Data Link Layer", "link6")
                    )
                ),

            )
        )
        "Computer Graphics and Image Processing" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Basics of Computer Graphics", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Applications of Computer Graphics", "link2"),
                        Course.Chapter.TopicItem("Components of a Graphics System", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("3D Modeling and Rendering", "link4"),
                        Course.Chapter.TopicItem("Projection and Viewing", "link5"),
                        Course.Chapter.TopicItem("Lighting and Shading Techniques", "link6")
                    )
                ),

            )
        )
        "Technical Writing And Presentation" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Technical Writing", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Audience Analysis", "link2"),
                        Course.Chapter.TopicItem("Defining Objectives and Deliverables", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Outlining and Structuring Documents", "link4"),
                        Course.Chapter.TopicItem("Writing Style and Tone", "link5"),
                        Course.Chapter.TopicItem("Use of Plain Language", "link6")
                    )
                )
            )
        )
        "Artificial Intelligence" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Introduction to Artificial Intelligence", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Definition and Goals of Artificial Intelligence", "link2"),
                        Course.Chapter.TopicItem("History and Evolution of AI", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Problem Formulation", "link4"),
                        Course.Chapter.TopicItem("State Space Search", "link5"),
                        Course.Chapter.TopicItem("Logic-Based Representation", "link6")
                    )
                ),

            )
        )
        "Compiler Construction" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Role of the Lexer (Scanner)", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Regular Expressions and Finite Automata", "link2"),
                        Course.Chapter.TopicItem("Role of the Parser", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Overview of Compilers", "link4"),
                        Course.Chapter.TopicItem("Compiler Structure and Components", "link5"),
                        Course.Chapter.TopicItem("Phases of Compilation", "link6")
                    )
                ),

            )
        )
        "Web Engineering" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Overview of Web Engineering", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Client-Side Technologies", "link2"),
                        Course.Chapter.TopicItem("Characteristics of Web Applications", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("JavaScript", "link4"),
                        Course.Chapter.TopicItem("Responsive Web Design", "link5"),
                        Course.Chapter.TopicItem("Database Integration (SQL, NoSQL)", "link6")
                    )
                ),

            )
        )
        "Option I" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("The Impact of Climate Change", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Gender Equality in Education", "link2"),
                        Course.Chapter.TopicItem("Urbanization and Public Health", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Renewable Energy Technologies", "link4"),
                        Course.Chapter.TopicItem("Wildlife Conservation Efforts", "link5"),
                        Course.Chapter.TopicItem("Urbanization and Public Health", "link6")
                    )
                )

            )
        )
        "Thesis / Project I" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("the Impact of Social Media", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Online Learning Platforms", "link2"),
                        Course.Chapter.TopicItem("Artificial Intelligence", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("The Future of Renewable Energy", "link4"),
                        Course.Chapter.TopicItem("Employee Motivation", "link5"),
                        Course.Chapter.TopicItem("Organizational Performance", "link6")
                    )
                ),

            )
        )
        "Digital Signal Processing" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Basics of Signals and Systems", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("Analog vs. Digital Signal Processing", "link2"),
                        Course.Chapter.TopicItem("Discrete-Time Signals", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("Difference Equations", "link4"),
                        Course.Chapter.TopicItem("Aliasing and Anti-Aliasing Filters", "link5"),
                        Course.Chapter.TopicItem("DFT Definition and Properties", "link6")
                    )
                ),

            )
        )
        "Option II" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("topic 1", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("topic 2", "link2"),
                        Course.Chapter.TopicItem("topic 3", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("topic 4", "link4"),
                        Course.Chapter.TopicItem("topic 5", "link5"),
                        Course.Chapter.TopicItem("topic 6", "link6")
                    )
                ),

            )
        )
        "Viva Voce" -> Course(
            courseTitle = courseTitle,
            chapters = listOf(
                Course.Chapter(
                    chapterName = "Chapter 1",
                    topicList = listOf(
                        Course.Chapter.TopicItem("topic 1", "https://drive.google.com/file/d/151EFWQGLYJ50iUvhx0zgJeBYMVoW94kB/view?usp=sharing"),
                        Course.Chapter.TopicItem("topic 2", "link2"),
                        Course.Chapter.TopicItem("topic 3", "link3")
                    )
                ),
                Course.Chapter(
                    chapterName = "Chapter 2",
                    topicList = listOf(
                        Course.Chapter.TopicItem("topic 4", "link4"),
                        Course.Chapter.TopicItem("topic 5", "link5"),
                        Course.Chapter.TopicItem("topic 6", "link6")
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


