package com.akings.jetpackcomposedeneme.ekranlar.firebase
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akings.jetpackcomposedeneme.Book.BookUpdateScreen
import com.akings.jetpackcomposedeneme.Book.BooksViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth
@Composable
fun Navigator(){
    auth= Firebase.auth
    val currentUser = auth.currentUser
    val navController= rememberNavController()

    NavHost(navController = navController, startDestination = if (currentUser != null){Destinations.SecondScreen}
    else{
        Destinations.LoginScreen
    }){
        composable(route="login"){
            LoginScreen(navController)
        }
        composable(route = Destinations.RegistryScreen,
         ){
            val updateViewModel:UserViewModel= hiltViewModel()
            RegistryScreen(navController, addNewUye = updateViewModel::addNewUye)
        }
        composable(route = Destinations.FirstScreen+"?uyeId={uyeId}",
        ){
            val updateViewModel:UserViewModel= hiltViewModel()
            val state = updateViewModel.state.value
            FirstScreen(navController, updateUye = updateViewModel::updateUye, state = state)
        }
        composable(route = Destinations.SecondScreen,
        ){

            SecondScreen(navController,onItemClick = {uyeId ->navController.navigate(Destinations.FirstScreen+"?uyeId=$uyeId")})
        }
        composable(route = Destinations.ThreeScreen,
        ){
            val viewModel: BooksViewModel = hiltViewModel()
            val state = viewModel.state.value
            ThreeScreen(navController, onItemClick = {uyeId ->navController.navigate(Destinations.FirstScreen+"?uyeId=$uyeId") },
                onItemClickBookUpdate = {bookId ->navController.navigate(Destinations.BookUpdateScreen+"?bookId=$bookId")}, state = state)
        }

        composable(route = Destinations.BookUpdateScreen+"?bookId={bookId}",
        ){
            val viewModel: BooksViewModel = hiltViewModel()
            val state = viewModel.state.value
            BookUpdateScreen(navController, updateBook = viewModel::updateBook,state=state)
        }
    }
}
object Destinations {
    const val LoginScreen="login"
    const val RegistryScreen = "registry"
    const val FirstScreen = "first"
    const val SecondScreen = "second"
    const val ThreeScreen = "three"
    const val BookUpdateScreen = "bookupdate"
}




