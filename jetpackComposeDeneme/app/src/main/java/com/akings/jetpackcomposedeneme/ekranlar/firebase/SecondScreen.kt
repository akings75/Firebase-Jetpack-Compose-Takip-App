package com.akings.jetpackcomposedeneme.ekranlar.firebase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth
@Composable
fun SecondScreen (navController: NavController,onItemClick: (String) -> Unit) {
    val uyeler= remember { mutableStateListOf(UyelikKayit()) }
    auth= Firebase.auth
    Repository().getAllUye(uyeler)
    Column(modifier = Modifier.padding(8.dp))
    {
        Row() {
            //uyeler.forEach {uye -> Text(text = uye.danOgrt)}
            Text(text = "Second Screen", fontSize = 40.sp, color = Color.Magenta)
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Sign Out",
                fontSize = 40.sp,
                color = Color.Red,
                modifier = Modifier.clickable {
                    auth.signOut()
                    navController.navigate(Destinations.LoginScreen)
                })

        }
        LazyColumn() {
            items(uyeler) { uye ->
                UyeListItem(uyelikKayit = uye, onItemClick = onItemClick)
            }
        }

    }
}

@Composable
fun UyeListItem(uyelikKayit: UyelikKayit,onItemClick: (String) -> Unit) {

    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(48.dp).clickable { onItemClick(uyelikKayit.id) },
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        backgroundColor = Color.LightGray)
    {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Ad:${uyelikKayit.ad}", fontSize = 26.sp)

        }

    }

}