package com.akings.jetpackcomposedeneme.ekranlar.firebase

import android.annotation.SuppressLint
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result

@Singleton
class Repository
@Inject
constructor() {

    @SuppressLint("StaticFieldLeak")
    val db= FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth



    fun addUye(uyelikKayit: UyelikKayit) {
        auth= Firebase.auth

        try {
            val veri= db.collection("üyelik").document(auth.uid.toString()) // bu şekilde yaparsak Authentication kısmındaki User UID ile Firestore
            //Database deki document'in ID si aynı oluyor

            //val veri= db.collection("üyelik").document(uyelikKayit.id) // firebase te document'in ID sini bizim model sınıfımızın id'sine
            // eşitledim böylece silme işlemi için "db.collection("books").document(book.id).delete()" kodunu kullanarak silme işlemini yapabildim
            veri.set(uyelikKayit) // set işlemi sayesinde de kullanıcıdan alınan verileri database kaydetmeyi başardık

        } catch (e: Exception){
            e.printStackTrace()
        }
    }
    fun getAllUye(uyeler:SnapshotStateList<UyelikKayit>) {
        val auth = Firebase.auth
        db.collection("üyelik").orderBy("ad",Query.Direction.ASCENDING)
            .get().addOnSuccessListener {
                uyeler.updateList(it.toObjects(UyelikKayit::class.java))

            }.addOnFailureListener {
                uyeler.updateList(listOf())
            }
    }

    fun getUyeId(uyeId: String): Flow<com.akings.jetpackcomposedeneme.ekranlar.firebase.Result<UyelikKayit>> = flow {
        try {
            emit(com.akings.jetpackcomposedeneme.ekranlar.firebase.Result.Loading<UyelikKayit>())
            val uye = db.collection("üyelik")
                .whereGreaterThanOrEqualTo("id", uyeId)
                .get()
                .await()
                .toObjects(UyelikKayit::class.java)
                .first()

            emit(com.akings.jetpackcomposedeneme.ekranlar.firebase.Result.Success<UyelikKayit>(data = uye))

        } catch (e: Exception) {
            emit(com.akings.jetpackcomposedeneme.ekranlar.firebase.Result.Error<UyelikKayit>(message = e.localizedMessage ?: "Error Desconocido"))
        }

    }

    fun updateUye1(bookId: String, uyelikKayit: UyelikKayit) {
        try {
            val map = mapOf(
                "ad" to uyelikKayit.ad,
                "soyad" to uyelikKayit.soyad,
                "sinif" to uyelikKayit.sinif,
                "danOgrt" to uyelikKayit.danOgrt,
            )

            db.collection("üyelik").document(bookId).update(map)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

