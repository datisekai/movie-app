package com.example.movieapp.Api

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.Film

class MyViewModel(private val myrepository: Myrepository) : ViewModel() {
    fun getALLFlim() : LiveData<List<Film>>{
        return myrepository.fecthData()
    }

}