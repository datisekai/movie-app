package com.example.movieapp.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.Articles
import com.example.movieapp.data.model.GetArticle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleDetailsViewModel: ViewModel() {
    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded : LiveData<Boolean>
        get() = _dataLoaded

    fun GetArticleById(id: Int?) : LiveData<GetArticle>{
        var article = MutableLiveData<GetArticle>()

        if(id != null){
            val call = ServiceBuilder().apiService.getArticleById(id)
            call.enqueue(object : Callback<GetArticle>{
                override fun onResponse(call: Call<GetArticle>?, response: Response<GetArticle>?) {
                    if (response != null) {
                        if(response.isSuccessful){
                            val result = response.body()
                            article.value = result
                            _dataLoaded.value = true
                        }
                        else{
                            Log.e("ArticleError","Get Fail")
                        }
                    }
                }

                override fun onFailure(call: Call<GetArticle>?, t: Throwable?) {
                    t?.printStackTrace()
                }

            })
        }
        return article
    }
}