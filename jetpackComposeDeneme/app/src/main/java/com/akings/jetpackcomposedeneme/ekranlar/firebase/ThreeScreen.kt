package com.akings.jetpackcomposedeneme.ekranlar.firebase

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.akings.jetpackcomposedeneme.Book.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.job

private lateinit var auth: FirebaseAuth
@Composable
fun ThreeScreen (navController: NavController,
                 state: BookState,
                 onItemClick: (String) -> Unit,
                 onItemClickBookUpdate: (String) -> Unit,
                 viewModel: BooksViewModel= hiltViewModel())
{
    val books = remember { mutableStateListOf(Book()) }
    LaunchedEffect(Unit) {
        viewModel.getBooks(books)
    }
    println("ThreeScreen:${viewModel.getBooks(books)}")
    auth= Firebase.auth
    Scaffold(

        topBar = {OptionMenu(navController,onItemClick)},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openDialog = true},
                backgroundColor = Color.Red,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
            }
        }

    ) {
        Column(modifier = Modifier.padding(8.dp, top = 56.dp))
        {

            Text(text = "Three Screen:Merhaba ${auth.currentUser?.displayName}", fontSize = 24.sp, color = Color.Magenta )

            LazyColumn() {
                items(books) { book ->
                    println("items:$book")
                    BookListItem(book = book, onItemClickBookUpdate = onItemClickBookUpdate, books = books)
                }
            }
        }

    }
    if (viewModel.openDialog) {
        AddBookAlertDialog(state = state)
    }
}
@Composable
fun BookListItem(book: Book,onItemClickBookUpdate: (String) -> Unit,books:SnapshotStateList<Book>) {
    val viewModel:BooksViewModel= hiltViewModel()

    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(48.dp)
        .clickable {
                   onItemClickBookUpdate(book.id)
                   println("clickable:$book")},
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        backgroundColor = Color.LightGray)
    {
        Row() {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "Ad:${book.kitapAdi}", fontSize = 26.sp)

            }
            IconButton(
                onClick = {
                   viewModel .deleteBook(book,books)
                },

                ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete item",
                    tint = Color.White
                )
            }

        }


    }

}

@Composable
fun OptionMenu(navController: NavController,onItemClick: (String) -> Unit) {

    var showMenu by remember { mutableStateOf(false)}
    val context = LocalContext.current

    TopAppBar (
        title = { Text(text = "My AppBar")},
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.MoreVert,"")

            }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false}) {

                DropdownMenuItem(onClick = { onItemClick(auth.currentUser?.uid.toString())
                    //navController.navigate(Destinations.FirstScreen)
                    Toast.makeText(context,"Update",Toast.LENGTH_LONG).show()}) {
                    Text(text = "Update")
                }
                DropdownMenuItem(onClick = { auth.signOut()
                    navController.navigate(Destinations.LoginScreen)
                    Toast.makeText(context,"Logout",Toast.LENGTH_LONG).show()}) {
                    Text(text = "Logout")
                }

            }
        }
    )

}

@Composable
fun AddBookAlertDialog (viewModel: BooksViewModel = hiltViewModel(),
                        state: BookState
) {
    /*
    var kitapAdi by rememberSaveable { mutableStateOf("")}
    var ders by rememberSaveable { mutableStateOf("")}
    var konu by rememberSaveable { mutableStateOf("")}
    var altkonu by rememberSaveable { mutableStateOf("")}
    var sorusayi by rememberSaveable { mutableStateOf("")}
    var dogru by rememberSaveable { mutableStateOf("")}
    var yanlis by rememberSaveable { mutableStateOf("")}
    var bos by rememberSaveable { mutableStateOf("")}

     */

    var kitapAdi by rememberSaveable(state.book?.kitapAdi){ mutableStateOf(state.book?.kitapAdi?:"") }
    var ders by rememberSaveable(state.book?.ders){ mutableStateOf(state.book?.ders?:"") }
    var konu by rememberSaveable(state.book?.konu){ mutableStateOf(state.book?.konu?:"") }
    var altkonu by rememberSaveable(state.book?.altKonu){ mutableStateOf(state.book?.altKonu?:"") }
    var sorusayi by rememberSaveable(state.book?.soruSayi){ mutableStateOf(state.book?.soruSayi?:"") }
    var dogru by rememberSaveable(state.book?.dogru){ mutableStateOf(state.book?.dogru?:"") }
    var yanlis by rememberSaveable(state.book?.yanlis){ mutableStateOf(state.book?.yanlis?:"") }
    var bos by rememberSaveable(state.book?.bos){ mutableStateOf(state.book?.bos?:"") }

    val focusRequester = FocusRequester()
    
    if (viewModel.openDialog) {
        AlertDialog(onDismissRequest = { viewModel.openDialog = false
        },
        title = {
            Text(
                text = "ADD_BOOK"
            )
        },
        text = {
            Column {
                TextField(
                    value = kitapAdi ,
                    onValueChange ={ kitapAdi = it},
                    placeholder = {
                    Text(
                        text = "KİTAP ADI"
                    )
                },
                modifier= Modifier.focusRequester(focusRequester)
                )
                LaunchedEffect(Unit){
                    coroutineContext.job.invokeOnCompletion {
                        focusRequester.requestFocus()
                    }
                }
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                TextField(
                    value = ders ,
                    onValueChange ={ ders = it},
                    placeholder = {
                        Text(
                            text = "DERS"
                        )
                    },
                    modifier= Modifier.focusRequester(focusRequester)
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                TextField(value = konu ,
                    onValueChange ={ konu = it},
                    placeholder = {
                        Text(
                            text = "KONU"
                        )
                    },
                    modifier= Modifier.focusRequester(focusRequester)
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                TextField(value = altkonu ,
                    onValueChange ={ altkonu = it},
                    placeholder = {
                        Text(
                            text = "ALT KONU"
                        )
                    },
                    modifier= Modifier.focusRequester(focusRequester)
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                TextField(value = sorusayi ,
                    onValueChange ={ sorusayi = it},
                    placeholder = {
                        Text(
                            text = "SORU SAYISI"
                        )
                    },
                    modifier= Modifier.focusRequester(focusRequester)
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                TextField(value = dogru ,
                    onValueChange ={ dogru = it},
                    placeholder = {
                        Text(
                            text = "DOĞRU"
                        )
                    },
                    modifier= Modifier.focusRequester(focusRequester)
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                TextField(value = yanlis ,
                    onValueChange ={ yanlis = it},
                    placeholder = {
                        Text(
                            text = "YANLIŞ"
                        )
                    },
                    modifier= Modifier.focusRequester(focusRequester)
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                TextField(value = bos ,
                    onValueChange ={ bos = it},
                    placeholder = {
                        Text(
                            text = "BOŞ"
                        )
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.openDialog = false
                    viewModel.addNewBook(kitapAdi,ders,konu,altkonu,sorusayi,dogru, yanlis,bos, date = null)
                }
            ) {
                Text(
                    text = "ADD"
                )
            }
        },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.openDialog = false
                    }
                ) {
                    Text(
                        text = "DISMISS"
                    )
                }
            }
        )
    }

}
