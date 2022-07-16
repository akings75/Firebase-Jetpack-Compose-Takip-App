package com.akings.jetpackcomposedeneme.ekranlar.firebase

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

@SuppressLint("LogNotTimber")
@HiltViewModel
class UserViewModel
@Inject
constructor(private val repository: Repository,
            savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableState<TextFieldState> = mutableStateOf(TextFieldState())
    val state: State<TextFieldState>
        get() = _state

    init {
        Log.d("BookDetailViewModel", "SavedStateHandle...")
        savedStateHandle.get<String>("uyeId")?.let { uyeId ->
            Log.d("BookDetailViewModel", "UyeId: $uyeId")
            getUye(uyeId)

        }
    }

    fun addNewUye(ad: String, soyad: String,sinif:String,danOgrt:String,kullaniciKod:String,kullaniciType:String) {
        val uyelikKayit = UyelikKayit(
            id = Firebase.auth.uid.toString(),
            ad = ad,
            soyad = soyad,
            sinif = sinif,
            danOgrt = danOgrt,
            kullaniciKod = kullaniciKod,
            kullaniciType = kullaniciType

        )
        repository.addUye(uyelikKayit)
    }

    fun updateUye(newAd: String, newSoyad: String,newSinif:String,newDanOgrt: String) {
        if (state.value.uye == null) {
            _state.value = TextFieldState(error = "Book is null")
            return
        }
        val uyeEdited = state.value.uye!!.copy(ad = newAd, soyad = newSoyad, sinif = newSinif, danOgrt = newDanOgrt)
        repository.updateUye1(uyeEdited.id, uyeEdited)
    }

    private fun getUye(uyeId: String) {
        repository.getUyeId(uyeId).onEach { result ->
            when(result) {
                is Result.Error ->{
                    _state.value = TextFieldState(error = result.message ?: "Unexpected error")
                }
                is Result.Loading -> {
                    _state.value = TextFieldState(isLoading = true)
                }
                is Result.Success -> {
                    _state.value = TextFieldState(uye = result.data)
                }


            }
        }.launchIn(viewModelScope)


    }

}