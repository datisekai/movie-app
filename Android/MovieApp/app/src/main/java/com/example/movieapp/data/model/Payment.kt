package com.example.movieapp.data.model

class Payment {
    var data : ArrayList<PaymentDTO> = arrayListOf()
    var totalEntries: Int = 0
    var page: Int = 1
    var limit: Int = 10
}