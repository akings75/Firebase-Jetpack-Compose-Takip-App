package com.akings.jetpackcomposedeneme.Book

import android.annotation.SuppressLint
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.akings.jetpackcomposedeneme.ekranlar.firebase.updateList
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

@Singleton
class BookRepository
@Inject
constructor() {

    @SuppressLint("StaticFieldLeak")
    val db= FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    fun addBook(book: Book) {
        println("BookRepository fun addBook Çağrıldı")
        auth= Firebase.auth

        try {
            val veri= db.collection("book").document(auth.uid.toString()).collection("detail").document(book.id) // bu şekilde yaparsak Authentication kısmındaki User UID ile Firestore
            //Database deki document'in ID si aynı oluyor

            //val veri= db.collection("üyelik").document(uyelikKayit.id) // firebase te document'in ID sini bizim model sınıfımızın id'sine
            // eşitledim böylece silme işlemi için "db.collection("books").document(book.id).delete()" kodunu kullanarak silme işlemini yapabildim
            veri.set(book) // set işlemi sayesinde de kullanıcıdan alınan verileri database kaydetmeyi başardık

        } catch (e: Exception){
            e.printStackTrace()
        }
    }
    fun getAllBook(books:SnapshotStateList<Book>) {
        println("BookRepository fun getAllBook çağrıldı")
        val auth = Firebase.auth
        db.collection("book").document(auth.uid.toString()).collection("detail")
            .get().addOnSuccessListener {
                books.updateList(it.toObjects(Book::class.java))

            }.addOnFailureListener {
                books.updateList(listOf())
            }
    }

    fun getBookById(bookId: String): Flow<ResultBook<Book>> = flow {
        println("BookRepository fun getBookById Çağrıldı")
        try {
            emit(ResultBook.Loading<Book>())
            val book = db.collection("book").document(auth.uid.toString()).collection("detail")
                .whereGreaterThanOrEqualTo("id", bookId)
                .get()
                .await()
                .toObjects(Book::class.java)
                .first()

            emit(ResultBook.Success<Book>(data = book))
            println("getBookById:$book")

        } catch (e: Exception) {
            emit(ResultBook.Error<Book>(message = e.localizedMessage ?: "Error Desconocido"))
        }

    }

    fun updateBook1(bookId: String, book: Book) {
        println("BookRepository fun updateBook1 Çağrıldı")
        try {
            val map = mapOf(
                "kitapAdi" to book.kitapAdi,
                "ders" to book.ders,
                "konu" to book.konu ,
                "altKonu" to book.altKonu,
                "soruSayi" to book.soruSayi,
                "dogru" to book.dogru,
                "yanlis" to book.yanlis ,
                "bos" to book.bos,
                "date" to book.date,
            )

            db.collection("book").document(auth.uid.toString()).collection("detail").document(bookId).update(map)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

