package com.akings.jetpackcomposedeneme.Book

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
//import com.akings.jetpackcomposedeneme.ekranlar.firebase.AddBookAlertDialog
import com.akings.jetpackcomposedeneme.ekranlar.firebase.OptionMenu

@Composable
fun AddBookFloatingActionButton (viewModel: BooksViewModel= hiltViewModel()) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openDialog = true},
                backgroundColor = Color.Red,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
            }
        }
    ){

    }

}