package com.example.movieapp.service.ZaloPay.Api

import java.util.concurrent.TimeUnit
import android.util.Log
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



class HttpProvider {
    fun sendPost(URL: String, formBody: RequestBody): JSONObject? {
        var data: JSONObject? = JSONObject()
        try {
            val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()
            val client: OkHttpClient = OkHttpClient.Builder()
                .connectionSpecs(listOf<ConnectionSpec>(spec))
                .callTimeout(5000, TimeUnit.MILLISECONDS)
                .build()
            val request: Request = Request.Builder()
                .url(URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build()
            val response = client.newCall(request).execute()
            data = if (!response.isSuccessful) {
                Log.println(Log.ERROR, "BAD_REQUEST", response.body()!!.string())
                null
            } else {
                JSONObject(response.body()!!.string())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return data
    }

}