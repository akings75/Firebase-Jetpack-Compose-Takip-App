package com.akings.jetpackcomposedeneme.ekranlar.firebase

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private lateinit var auth: FirebaseAuth
@SuppressLint("LogNotTimber")
@Composable
fun LoginScreen(navController: NavController) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp))
    {
        auth= Firebase.auth
        val emailValue = rememberSaveable { mutableStateOf("")}
        val passwordValue = rememberSaveable { mutableStateOf("")}
        val context = LocalContext.current
        val updateViewModel:UserViewModel= hiltViewModel()

        OutlinedTextField(
            label = {
                Text("Email")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            value = emailValue.value,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                emailValue.value = it
            }
        )
        OutlinedTextField(
            label = {
                Text("Password")
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = passwordValue.value,

            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                passwordValue.value = it
            }
        )

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            auth.signInWithEmailAndPassword(
            emailValue.value, passwordValue.value
        ).addOnCompleteListener { task ->
                val db = FirebaseFirestore.getInstance()
                val currentUser = auth.currentUser

            if (task.isSuccessful) {

                db.collection("üyelik").document(currentUser?.uid.toString()).get().addOnSuccessListener { value ->
                    val type = value?.data?.getValue("kullaniciType")
                    println("type:"+type)
                    if (type=="ogrenci"){
                        Toast.makeText(context,"Welcome " + auth.currentUser?.email.toString(),Toast.LENGTH_SHORT).show()
                        navController.navigate(Destinations.ThreeScreen)
                    } else if (type=="ogretmen"){
                        Toast.makeText(context,"Welcome " + auth.currentUser?.email.toString(),Toast.LENGTH_SHORT).show()
                        navController.navigate(Destinations.SecondScreen)

                    }
                }
                /*
                db.collection("üyelik").whereEqualTo("kullaniciType","ogrenci")
                    .get().addOnSuccessListener {
                        //uyeler.updateList(it.toObjects(UyelikKayit::class.java))
                        it.forEach { result-> println("result data:${result.data}")
                            println("result id:${result.id}")
                            println("result data getValue:${result.data.getValue("kullaniciType")}")
                            result.data.getValue("kullaniciType")
                        }
                    }.addOnFailureListener {
                       // uyeler.updateList(listOf())
                    }

                 */


                /* Buradaki kod üyelik oluşturulurken eğer E-posta doğrulama istersek kullanılıyor
                if (auth.currentUser?.isEmailVerified!!) {
                    Toast.makeText(context,"Welcome " + auth.currentUser?.email.toString(),Toast.LENGTH_SHORT).show()
                    navController.navigate(Destinations.FirstScreen)

                } else {
                    Toast.makeText(context,"Hesabınız Doğrulanmıyor",Toast.LENGTH_SHORT).show()
                }
                 */
            }

        }.addOnFailureListener {
            println("Hata")

        }
            }) {
            Text(text = "Login")

        }
        Text(text = "Üye değilseniz buraya tıklayıp üye olunuz", modifier = Modifier.clickable { navController.navigate("registry") })

    }

}