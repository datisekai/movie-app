package com.example.movieapp.data.model

class HistoryDTO{
    var Id: Int = 0
    var userID: Int = 0
    var itemId: Int = 0
    var time: Long = 0

    constructor()

    constructor(Id: Int, userID: Int, itemId: Int, time: Long) {
        this.Id = Id
        this.userID = userID
        this.itemId = itemId
        this.time = time
    }


    override fun toString(): String {
        return Id.toString()+ " " + userID.toString()+ " " + itemId.toString()+ " " + time.toString()
    }
}
