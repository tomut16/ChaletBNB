package com.example.chaletbnb.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import com.example.chaletbnb.data.services.FirebaseAuthService


@Composable
fun LogoutButton(
    navController: NavController,
    destination: String = "splash",
    buttonText: String = "Disconnect"
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to disconnect?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        FirebaseAuthService.signOutUser()
                        navController.navigate(destination) {
                            popUpTo(0) // Clear backstack
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel", )
                }
            }
        )
    }

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text(buttonText)
    }
}
