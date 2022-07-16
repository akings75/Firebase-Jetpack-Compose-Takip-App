package com.akings.jetpackcomposedeneme.Book

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akings.jetpackcomposedeneme.Book.Book
import com.akings.jetpackcomposedeneme.ekranlar.firebase.Result
import com.akings.jetpackcomposedeneme.ekranlar.firebase.TextFieldState
import com.akings.jetpackcomposedeneme.ekranlar.firebase.UyelikKayit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@SuppressLint("LogNotTimber")
@HiltViewModel
class BooksViewModel
@Inject
constructor(
    private val repo:BookRepository,savedStateHandle: SavedStateHandle
    ): ViewModel() {

        var openDialog by mutableStateOf(false)
        val db= FirebaseFirestore.getInstance()
        private lateinit var auth: FirebaseAuth


    private val _state: MutableState<BookState> = mutableStateOf(BookState())
    val state: State<BookState>
        get() = _state

    init {
        Log.d("BookDetailViewModel", "SavedStateHandle...")
        savedStateHandle.get<String>("bookId")?.let { bookId ->
            Log.d("BookDetailViewModel", "BookId: $bookId")
            getBook(bookId)
            println("BooksViewModel init getBook çağrıldı:+$bookId")

        }
    }

    fun addNewBook(kitapAdi: String, ders: String,konu:String,altKonu:String,soruSayi:String,dogru:String, yanlis: String,bos:String,date: Date?) {
        println("BooksViewModel fun addNewBook Çağrıldı")
        val book = Book(
            id = UUID.randomUUID().toString(),
            kitapAdi = kitapAdi,
            ders = ders,
            konu = konu,
            altKonu = altKonu,
            soruSayi = soruSayi ,
            dogru = dogru,
            yanlis = yanlis,
            bos = bos,
            date = date,

        )
        repo.addBook(book)
    }

    fun updateBook(newkitapAdi: String, newders: String,newkonu:String,newaltKonu:String,newsoruSayi:String,newdogru:String, newyanlis: String,newbos:String) {
        println("BooksViewModel fun updateBook Çağrıldı")
        if (state.value.book == null) {
            _state.value = BookState(error = "Book is null")
            return
        }
        val bookEdited = state.value.book!!.copy(kitapAdi = newkitapAdi, ders = newders, konu = newkonu, altKonu = newaltKonu, soruSayi = newsoruSayi, dogru = newdogru, yanlis = newyanlis, bos = newbos)
        repo.updateBook1(bookEdited.id, bookEdited)
    }

    private fun getBook(bookId: String) {
        println("BooksViewModel getBook çağrıldı")
        repo.getBookById(bookId).onEach { result ->
            println("onEach: ${result.data?.kitapAdi}")
            when(result) {
                is ResultBook.Error -> {
                    _state.value = BookState(error = result.message ?: "Unexpected error")
                }
                is ResultBook.Loading -> {
                    _state.value = BookState(isLoading = true)
                }
                is ResultBook.Success -> {
                    _state.value = BookState( book = result.data)
                    println("ResultBook.Success:${_state.value}")
                }
            }
        }.launchIn(viewModelScope)
    }
    fun deleteBook(book: Book, books: SnapshotStateList<Book>) {
        auth= Firebase.auth
        try {
            //books.removeAt(books.indexOf(book))
            books.remove(book)
            db.collection("book").document(auth.uid.toString()).collection("detail").document(book.id).delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun getBooks(books:SnapshotStateList<Book>)  {
        println("BooksViewModel getBooks çağrıldı")

        viewModelScope.launch {
            repo.getAllBook(books)
        }

    }
}