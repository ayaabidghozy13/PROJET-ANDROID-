package com.example.projectandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.projectandroid.navigation.AppNavigation
import com.example.projectandroid.ui.theme.ProjectAndroidTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            ProjectAndroidTheme {

                val navController = rememberNavController()

                AppNavigation(
                    navController = navController
                )
            }
        }
    }
}