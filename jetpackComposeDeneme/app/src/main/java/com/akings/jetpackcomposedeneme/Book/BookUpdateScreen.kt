package com.akings.jetpackcomposedeneme.Book

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.akings.jetpackcomposedeneme.ekranlar.firebase.Destinations

@Composable
fun BookUpdateScreen(
    navController: NavHostController,
    state: BookState,
    updateBook: (String, String,String, String,String, String,String, String) -> Unit,
)
{
    Column(modifier = Modifier.padding(8.dp).fillMaxWidth()) {

        var kitap by rememberSaveable(state.book?.kitapAdi){ mutableStateOf(state.book?.kitapAdi?:"") }
        var ders by rememberSaveable(state.book?.ders){ mutableStateOf(state.book?.ders?:"") }
        var konu by rememberSaveable(state.book?.konu){ mutableStateOf(state.book?.konu?:"") }
        var altkonu by rememberSaveable(state.book?.altKonu){ mutableStateOf(state.book?.altKonu?:"") }
        var sorusayi by rememberSaveable(state.book?.soruSayi){ mutableStateOf(state.book?.soruSayi?:"") }
        var dogru by rememberSaveable(state.book?.dogru){ mutableStateOf(state.book?.dogru?:"") }
        var yanlis by rememberSaveable(state.book?.yanlis){ mutableStateOf(state.book?.yanlis?:"") }
        var bos by rememberSaveable(state.book?.bos){ mutableStateOf(state.book?.bos?:"") }

        TextField(value = kitap , onValueChange = { kitap = it }, label = { Text(text = "Kitap Adı")} )
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(value = ders, onValueChange = { ders = it }, label = { Text(text = "Ders Adı")} )
        Spacer(modifier = Modifier.padding(4.dp))
        TextField(value = konu , onValueChange = { konu = it }, label = { Text(text = "Konu Adı")} )
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(value = altkonu, onValueChange = { altkonu = it }, label = { Text(text = "Alt Konu Adı")} )
        Spacer(modifier = Modifier.padding(4.dp))
        TextField(value = sorusayi , onValueChange = { sorusayi = it }, label = { Text(text = "Soru Sayısı")} )
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(value = dogru, onValueChange = { dogru = it }, label = { Text(text = "Doğru Sayısı")} )
        Spacer(modifier = Modifier.padding(4.dp))
        TextField(value = yanlis , onValueChange = { yanlis = it }, label = { Text(text = "Yanlış Sayısı")} )
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(value = bos, onValueChange = { bos = it }, label = { Text(text = "Boş Sayısı")} )
        Spacer(modifier = Modifier.padding(4.dp))
        println("BookUpdateScreen Çağrıldı")
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                updateBook(kitap,ders,konu,altkonu,sorusayi,dogru,yanlis,bos)
                navController.navigate(Destinations.ThreeScreen)
            }
        ){
            Text(text = "Update")
        }

    }

}
