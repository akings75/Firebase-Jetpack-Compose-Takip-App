package com.akings.jetpackcomposedeneme


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import com.akings.jetpackcomposedeneme.ekranlar.firebase.Navigator
import com.akings.jetpackcomposedeneme.ui.theme.JetpackComposeDenemeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            JetpackComposeDenemeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column (){
                        Navigator()

                    }
                    //ProgressBar()
                    //LinearProgressIndicatorSample()
                }
            }
        }
    }
}




