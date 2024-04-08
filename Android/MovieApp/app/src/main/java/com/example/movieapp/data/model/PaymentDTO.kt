package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

class PaymentDTO {
        @SerializedName("id")
        var id: Int = 0
        @SerializedName("amount")
        var amount: Int = 0
        @SerializedName("order_status")
        var orderStatus: String = ""
        @SerializedName("order_type")
        var orderType: String = ""
        @SerializedName("description")
        var description: String? = null
        @SerializedName("zalo_trans_id")
        var zaloTransId: String = ""
        @SerializedName("created_at")
        var createdAt: String = ""
        @SerializedName("updated_at")
        var updatedAt: String = ""
        @SerializedName("user")
        var user: UserDTO = UserDTO()

    constructor(){}
    constructor(
        id: Int,
        amount: Int,
        orderStatus: String,
        orderType: String,
        description: String?,
        zaloTransId: String,
        createdAt: String,
        updatedAt: String,
        user: UserDTO
    ) {
        this.id = id
        this.amount = amount
        this.orderStatus = orderStatus
        this.orderType = orderType
        this.description = description
        this.zaloTransId = zaloTransId
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.user = user
    }
}