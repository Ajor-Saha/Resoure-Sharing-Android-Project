package com.example.resourcesharing.data.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.resourcesharing.navigation.PostOfficeAppRouter
import com.example.resourcesharing.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {

    private val TAG = HomeViewModel::class.simpleName

    fun logout() {

        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                Log.d(TAG, "Inside sign success")
                PostOfficeAppRouter.navigateTo(Screen.LoginScreen)
            } else {
                Log.d(TAG, "Inside sign out is not complete")
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)

    }



}