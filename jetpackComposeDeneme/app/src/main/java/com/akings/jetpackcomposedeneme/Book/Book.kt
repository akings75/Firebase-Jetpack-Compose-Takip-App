package com.akings.jetpackcomposedeneme.Book

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Book(
    val id:String="",
    val kitapAdi:String="",
    val ders:String="",
    val konu:String="",
    val altKonu:String="",
    val soruSayi:String="",
    val dogru:String="",
    val yanlis:String="",
    val bos:String="",
    @ServerTimestamp
    var date: Date?=null
)
