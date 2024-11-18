package com.exprnc.testpushapp

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.exprnc.testpushapp.ui.theme.TestPushAppTheme
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestNotificationPermission()
        enableEdgeToEdge()
        setContent {
            TestPushAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(innerPadding)
                }
            }
        }
    }

//    private fun requestNotificationPermission() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            val hasPermission = ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//
//            if(!hasPermission) {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
//                    0
//                )
//            }
//        }
//    }
}

@Composable
fun MainScreen(innerPadding: PaddingValues) {
    var token by remember { mutableStateOf("Fetching token...") }

    val context = LocalContext.current
    val activity = context as? Activity

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            println("Notification permission granted.")
        } else {
            println("Notification permission denied.")
        }
    }

    LaunchedEffect(Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            token = if (task.isSuccessful) {
                task.result
            } else {
                "Failed to fetch token"
            }
            Log.d("TOKEN", token)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(modifier = Modifier.padding(horizontal = 16.dp), text = token, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Button(onClick = {
                notificationPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
            }) {
                Text("Request Notification Permission")
            }
        }
    }
}