package com.example.chaletbnb.data.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthService {

    fun registerUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    Log.d("Firebase", "Registered: ${user?.uid}")
                    onResult(true, null)
                } else {
                    Log.e("Firebase", "Register failed: ${task.exception?.message}")
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    Log.d("Firebase", "Logged in: ${user?.uid}")
                    onResult(true, null)
                } else {
                    Log.e("Firebase", "Login failed: ${task.exception?.message}")
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
    }
}
