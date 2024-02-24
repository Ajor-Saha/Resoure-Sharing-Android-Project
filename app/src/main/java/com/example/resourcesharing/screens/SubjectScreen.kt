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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
            // Display uploaded files for the selected course title
            LazyColumn {
                items(viewModel.filesForCourse) { file ->
                    FileButton(file = file)
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
                Text(text = "Upload question")
            }
        }
    }




}





@Composable
fun FileButton(file: FileItem) {
    val context = LocalContext.current

    file.fileUrl?.let { url ->
        Text(
            text = url,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable {
                    openFile(context, url)
                }
        )
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


//LaunchedEffect(courseTitle) {
//    // Fetch files for the current course title whenever it changes
//    viewModel.fetchFiles(courseTitle)
//}
//
//Column(
//modifier = Modifier
//.fillMaxSize()
//.padding(16.dp)
//) {
//    OutlinedTextField(
//        value = courseTitle,
//        onValueChange = { courseTitle = it },
//        label = { Text("Course Title") },
//        modifier = Modifier.padding(vertical = 8.dp)
//    )
//
//    Button(
//        onClick = { getContent.launch("*/*") },
//        modifier = Modifier.padding(vertical = 8.dp)
//    ) {
//        Text("Upload File")
//    }
//
//
//    // Display uploaded files for the selected course title
//    viewModel.filesForCourse.forEach { file ->
//        FileButton(file = file)
//    }
//}