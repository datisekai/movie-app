package com.example.movieapp.service.ZaloPay.Api

import android.util.Log
import com.example.movieapp.service.ZaloPay.Constant.AppInfo
import com.example.movieapp.service.ZaloPay.Helper.Helpers
import org.json.JSONObject
import java.util.Date

import okhttp3.FormBody
import okhttp3.RequestBody




class CreateOrder {
   public class CreateOrderData(amount: String) {
        var AppId: String
        var AppUser: String
        var AppTime: String
        var Amount: String
        var AppTransId: String
        var EmbedData: String
        var Items: String
        var BankCode: String
        var Description: String
        var Mac: String

        init {
            val appTime: Long = Date().getTime()
            AppId = AppInfo.APP_ID.toString()
            AppUser = "Android_Demo"
            AppTime = appTime.toString()
            Amount = amount
            AppTransId = Helpers().getAppTransId()
            EmbedData = "{}"
            Items = "[]"
            BankCode = "zalopayapp"
            Description = "Merchant pay for order #" + Helpers().getAppTransId()
            val inputHMac = String.format(
                "%s|%s|%s|%s|%s|%s|%s",
                AppId,
                AppTransId,
                AppUser,
                Amount,
                AppTime,
                EmbedData,
                Items
            )
//            val inputHMac = String.format(
//                "%s|%s|%s",
//                AppId,
//                AppTransId,
//                AppInfo.MAC_KEY
//            )
            Mac = Helpers().getMac(AppInfo.MAC_KEY, inputHMac)
        }
    }

    @Throws(Exception::class)
    fun createOrder(amount: String): JSONObject? {
        val input = CreateOrderData(amount)
        val formBody: RequestBody = FormBody.Builder()
            .add("app_id", input.AppId)
            .add("app_user", input.AppUser)
            .add("app_time", input.AppTime)
            .add("amount", input.Amount)
            .add("app_trans_id", input.AppTransId)
            .add("embed_data", input.EmbedData)
            .add("item", input.Items)
            .add("bank_code", input.BankCode)
            .add("description", input.Description)
            .add("mac", input.Mac)
            .build()

        Log.e("APPTRANS",input.AppTransId)
        return HttpProvider().sendPost(AppInfo.URL_CREATE_ORDER, formBody)
    }

}