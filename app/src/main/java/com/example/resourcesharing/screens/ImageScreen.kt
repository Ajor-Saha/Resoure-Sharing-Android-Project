package com.example.resourcesharing.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.resourcesharing.navigation.PostOfficeAppRouter
import com.example.resourcesharing.navigation.Screen
import com.example.resourcesharing.navigation.SystemBackButtonHandler
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

@Composable
fun ImageScreen(course: String, id: Int) {
    val viewModel: ImageViewModel = viewModel()
    val getContent = viewModel.rememberLauncher(id)

    val courseTitle by remember { mutableStateOf(course) }
    var isFetchingImages by remember { mutableStateOf(false) }




    SystemBackButtonHandler {
        PostOfficeAppRouter.navigateTo(Screen.SubjectScreen(course,id))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { getContent.launch("*/*") },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Upload Image")
        }

        Button(
            onClick = {
                // Clear existing images to ensure fresh fetch
                isFetchingImages = false // Reset state before fetching
                viewModel.filesForCourse.value = emptyList() // Clear local list
                viewModel.fetchFiles(courseTitle)
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Fetch Images")
        }

        // Display uploaded images if fetching is enabled
        if (isFetchingImages) {

            LazyColumn {
                items(viewModel.filesForCourse.value) { image ->
                    Image(
                        painter = rememberAsyncImagePainter(image.url),
                        contentDescription = "Uploaded Image",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }


}

class ImageViewModel : ViewModel() {
    private val storage = FirebaseStorage.getInstance()
    private val database = FirebaseDatabase.getInstance()

    val filesForCourse = MutableStateFlow<List<ImageFile>>(emptyList())

    // Function to upload an image under the corresponding course
    fun uploadImage(courseTitle: String, imageUri: Uri) {
        val storageRef = storage.reference.child("images/$courseTitle")
        val imageId = UUID.randomUUID().toString()
        val imageRef = storageRef.child(imageId)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uploadTask = imageRef.putFile(imageUri)
                val downloadUrl = uploadTask.await().storage.downloadUrl.await()
                val imageFile = ImageFile(courseTitle, downloadUrl.toString())
                filesForCourse.value = filesForCourse.value + imageFile
            } catch (e: Exception) {
                // Handle image upload errors
            }
        }
    }

    // Function to save image metadata (course title and image URL) to Realtime Database

    // Function to fetch image files for a specific course from Realtime Database
    fun fetchFiles(courseTitle: String) {
        val imagesRef = database.getReference("images")
        val query = imagesRef.orderByChild("course").equalTo(courseTitle)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val files = mutableListOf<ImageFile>()
                snapshot.children.forEach { child ->
                    val imageFile = child.getValue(ImageFile::class.java)
                    imageFile?.let { files.add(it) }
                }
                filesForCourse.value = files
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    @Composable
    fun rememberLauncher(id: Int): ActivityResultLauncher<String> {
        return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uploadImage("Course$id", it)
            }
        }
    }
}

// Data class to represent image metadata (course title and image URL)
data class ImageFile(val course: String, val url: String)
