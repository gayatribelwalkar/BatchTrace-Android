package com.batchtrace.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.batchtrace.app.ui.auth.LoginScreen
import com.batchtrace.app.ui.theme.BatchTraceTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            BatchTraceTheme {
                LoginScreen()
            }
        }
    }
}