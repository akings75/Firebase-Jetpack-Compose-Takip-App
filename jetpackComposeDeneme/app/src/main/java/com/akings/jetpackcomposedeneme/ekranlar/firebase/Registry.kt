package com.akings.jetpackcomposedeneme.ekranlar.firebase

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*
private lateinit var auth: FirebaseAuth
@SuppressLint("LogNotTimber")
@Composable
fun RegistryScreen(navController: NavController,addNewUye: (String, String,String, String,String, String) -> Unit) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp))
    {
        var selectedItem by remember{ mutableStateOf("")}
        var emailValue by remember { mutableStateOf(TextFieldValue())}
        var passwordValue by remember { mutableStateOf(TextFieldValue())}
        var nameValue by remember { mutableStateOf(TextFieldValue())}
        var soyAd by remember { mutableStateOf(TextFieldValue()) }
        var sinif by remember { mutableStateOf(TextFieldValue()) }
        var danOgrt by remember { mutableStateOf(TextFieldValue()) }
        var kulKod by  remember { mutableStateOf(TextFieldValue()) }
        val kulType = selectedItem

        val context = LocalContext.current
        auth= Firebase.auth
        auth.currentUser?.providerData?.forEach { item -> item.displayName }
        Row() {
            OutlinedTextField(
                label = {
                    Text("Email")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                value = emailValue,
                singleLine = true,
                modifier = Modifier.width(210.dp),
                onValueChange = {
                    emailValue = it
                }
            )
            Spacer(modifier = Modifier.padding(2.dp))
            OutlinedTextField(
                label = {
                    Text("Password")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                value = passwordValue,

                modifier = Modifier.width(210.dp),
                onValueChange = {
                    passwordValue = it
                }
            )

        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row() {
            OutlinedTextField(
                label = {
                    Text("Adınız")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = nameValue,
                singleLine = true,
                modifier = Modifier.width(210.dp),
                onValueChange = {
                    nameValue = it
                }
            )
            Spacer(modifier = Modifier.padding(2.dp))
            OutlinedTextField(value =soyAd,
                onValueChange = { soyAd = it },
                modifier = Modifier.width(210.dp),
                label = { Text(text = "Soy Adınız") }
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row() {
            OutlinedTextField(value =sinif,
                onValueChange = { sinif = it },
                label = { Text(text = "Sınıfınız") },
                modifier = Modifier.width(210.dp)
            )
            Spacer(modifier = Modifier.padding(2.dp))
            OutlinedTextField(value =danOgrt,
                onValueChange = { danOgrt = it },
                label = { Text(text = "Danışman Öğretmeniniz") }
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row {
            var expanded by remember { mutableStateOf(false)}
            val list= listOf("yonetici","ogretmen","ogrenci")

            //var selectedItem by remember{ mutableStateOf("")}
            var textFiledSize by remember{ mutableStateOf(Size.Zero)}

            val context = LocalContext.current

            val icon = if (expanded) {
                Icons.Filled.KeyboardArrowUp
            } else{
                Icons.Filled.KeyboardArrowDown
            }
            Column() {

                OutlinedTextField(
                    value = selectedItem ,
                    onValueChange = {selectedItem=it},
                    modifier = Modifier
                        .width(210.dp)
                        .onGloballyPositioned { coordinates ->
                            textFiledSize = coordinates.size.toSize()
                        },
                    label = {Text(text="Kullanıcı Türü")},
                    trailingIcon = {
                        Icon(icon,"",Modifier.clickable { expanded = !expanded
                            selectedItem=""})
                    }
                )

                DropdownMenu(
                    expanded = expanded ,
                    onDismissRequest = { expanded=false },
                    modifier = Modifier
                        .width(with(LocalDensity.current){textFiledSize.width.toDp()})
                ) {

                    list.forEach { label ->
                        DropdownMenuItem(onClick = {
                            selectedItem = label
                            expanded = false
                        }) {
                            Text(text=label)

                        }
                    }
                    when (selectedItem) {
                        //println("selecteItem:$selectedItem")

                        //KullaniciType.ogretmen.toString() -> uyelikKayit.kullaniciType=KullaniciType.ogretmen.toString()
                        KullaniciType.ogretmen.toString() -> println("selecteItem:$selectedItem")
                        KullaniciType.ogrenci.toString() -> println("selecteItem:$selectedItem")



                        /*
                        KullaniciType.ogretmen.toString() -> println(KullaniciType.ogretmen)
                        KullaniciType.ogrenci.toString() ->  println(KullaniciType.ogrenci)
                        KullaniciType.yonetici.toString() -> println(KullaniciType.yonetici)

                         */
                    }

                }

            }
            Spacer(modifier = Modifier.padding(2.dp))
            OutlinedTextField(value =kulKod,
                onValueChange = { kulKod = it },
                modifier = Modifier.width(210.dp),
                label = { Text(text = "Kullanıcı Kodu") }
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            if (emailValue.text=="" || passwordValue.text=="") {
            Toast.makeText(context, "Email ve Password Alanlarını Boş Bırakamazsınız", Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(
                emailValue.text.trim(),passwordValue.text.trim()
            ).addOnCompleteListener { task ->
                addNewUye(nameValue.text.lowercase(),soyAd.text.uppercase(),sinif.text.lowercase(),danOgrt.text.lowercase(),kulKod.text,kulType)
                navController.navigate(Destinations.LoginScreen)
                if (task.isSuccessful) {
                    //Bu kod ile kullanıcının adını ekranda görebiliyoruz
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(nameValue.text).build()
                    user?.updateProfile(profileUpdates)
                    //Bu kod ile kullanıcının adını ekranda görebiliyoruz




                    /*Buradaki kod üyelik oluşturulurken eğer E-posta doğrulama istersek kullanılıyor
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        if (task.isSuccessful) {
                            Toast.makeText(context, "E-posta hesabınızı doğrulayın", Toast.LENGTH_LONG).show()
                            navController.navigate(Destinations.LoginScreen)
                        }
                    }
                     Buradaki kod üyelik oluşturulurken eğer E-posta doğrulama istersek kullanılıyor*/
                }
            }.addOnFailureListener {
                println("HATA")
                Toast.makeText(context, "Üyelik işleminiz başarısız", Toast.LENGTH_LONG).show()
            }
        }}) {
            Text(text = "Register")

        }

    }

}
