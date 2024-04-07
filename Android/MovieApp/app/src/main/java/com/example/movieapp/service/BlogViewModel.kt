package com.example.movieapp.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.adapter.model.Article
import com.example.movieapp.data.model.Articles
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class BlogViewModel : ViewModel() {
    private var dataList: MutableLiveData<Articles> = MutableLiveData()

    var currentPage: MutableLiveData<Int> = MutableLiveData<Int>()
        private set

    init {
        loadFirtArticles()
    }

    fun getDataList(): MutableLiveData<Articles>{
        Log.e("GetBlog",dataList.value?.data.toString())
        return dataList
    }
    fun setCurrentPage(newPage: Int){
        currentPage.value = newPage
    }

    fun loadFirtArticles(){
        try {
            val call = ServiceBuilder().apiService.getFirtListBlog()
            call.enqueue(object: Callback<Articles> {
                override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
                    if(response.isSuccessful){
                        val listBlog = response.body()
                        if(listBlog != null && listBlog.data.isNotEmpty()){
                            dataList.value = listBlog
                            currentPage.value = listBlog.page
                            Log.e("errorBlog2",listBlog.data.toString())
                        }
                        else{
                            currentPage.value = 0
                        }
                    }
                }
                override fun onFailure(call: Call<Articles>?, t: Throwable?) {
                    t!!.printStackTrace()
                }
            })

        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun LoadMoreArticle(nextPage: Int){
        try {
            val call = ServiceBuilder().apiService.getListBlogPage(nextPage)
            call.enqueue(object : Callback<Articles>{
                override fun onResponse(call: Call<Articles>?, response: Response<Articles>?) {
                    if(response!!.isSuccessful){
                        val getData = response.body()

                        val tempDataList = dataList

                        var currentData = tempDataList.value?.data?.toMutableList()
                        currentData?.addAll(getData.data)

                        tempDataList.value?.data = currentData!!.toList()
                        tempDataList.value?.page = getData.page
                        tempDataList.value?.limit = getData.limit
                        tempDataList.value?.totalEntries = getData.totalEntries

                        dataList.postValue(tempDataList.value)

                        currentPage.value = dataList.value?.page
                        Log.e("errorBlogLoadMore",dataList.value?.data.toString())
                    }else{
                        Log.e("loadMoreBlog","Fail")
                    }
                }

                override fun onFailure(call: Call<Articles>?, t: Throwable?) {
                    t?.printStackTrace()
                }

            })
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}