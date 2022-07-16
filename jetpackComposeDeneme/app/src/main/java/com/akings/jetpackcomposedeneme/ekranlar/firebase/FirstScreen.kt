package com.akings.jetpackcomposedeneme.ekranlar.firebase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
//import com.akings.jetpackcomposedeneme.ekranlar.Sınıflar.DropDownKullaniciMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

private lateinit var auth: FirebaseAuth
@Composable
fun FirstScreen (navController: NavController,
                 state: TextFieldState,
                 updateUye: (String, String,String,String) -> Unit) {
    auth= Firebase.auth

    val context= LocalContext.current
    var ad by rememberSaveable(state.uye?.ad){ mutableStateOf(state.uye?.ad?:"") }
    var soyad by rememberSaveable(state.uye?.soyad){ mutableStateOf(state.uye?.soyad?:"") }
    var sinif by rememberSaveable(state.uye?.sinif){ mutableStateOf(state.uye?.sinif?:"") }
    var danOgrt by rememberSaveable(state.uye?.danOgrt){ mutableStateOf(state.uye?.danOgrt?:"") }

    Column(modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth())
    {
        Row() {
            Text(text = "First Screen", fontSize = 40.sp, color = Color.Magenta )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Sign Out", fontSize = 40.sp, color = Color.Red, modifier = Modifier.clickable { auth.signOut()
                navController.navigate(Destinations.LoginScreen)} )
        }
        Text(text = "Merhaba ${auth.currentUser?.displayName} kullanıcı bilgilerini güncelleyebilirsin.", fontSize = 20.sp, color = Color.Magenta )

        Row() {
            OutlinedTextField(value =ad,
                onValueChange = { ad = it },
                label = { Text(text = "Adınız") },
                modifier = Modifier.width(210.dp)
            )
            Spacer(modifier = Modifier.padding(2.dp))
            OutlinedTextField(value =soyad,
                onValueChange = { soyad = it },
                label = { Text(text = "Soy Adınız") }
            )

        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(value =sinif,
                onValueChange = { sinif= it },
                label = { Text(text = "Sınıfınız") },
                modifier = Modifier.width(210.dp)
            )
            Spacer(modifier = Modifier.padding(2.dp))
            OutlinedTextField(value =danOgrt,
                onValueChange = { danOgrt = it },
                label = { Text(text = "Danışman Öğretmeniniz") }
            )

        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            updateUye(ad,soyad,sinif,danOgrt)

        }) {

            Text(text = "Update", fontSize = 20.sp)
            
        }

    }

}

fun <T> SnapshotStateList<T>.updateList(newList:List<T>) {
    clear()
    addAll(newList)
}