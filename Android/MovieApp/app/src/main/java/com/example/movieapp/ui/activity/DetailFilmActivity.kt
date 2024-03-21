package com.example.movieapp.ui.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.movieapp.PlayerActivity
import com.example.movieapp.R
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.example.movieapp.Api.DetailFilmLoader
import com.example.movieapp.Api.ServiceBuilder
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.ui.login.classToken
import java.util.concurrent.Executors

class DetailFilmActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Film>{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)

        //Check if a Loader is running, if it is, reconnect to it
        if (supportLoaderManager.getLoader<Film>(0)!=null) {
            supportLoaderManager.initLoader(0,null,this).forceLoad()
        }

        getDetailFilm()
    }

    public fun clickWatch(view:View){
        val intent : Intent =  Intent(this,PlayerActivity::class.java);
        startActivity(intent);

    }

    public fun getDetailFilm(){
       val connMgr : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
       val networkInfo = connMgr.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            val bundle2 = Bundle()
            bundle2.putString("header",classToken.MY_TOKEN)
            supportLoaderManager.restartLoader<Film>(0, bundle2, this)

        }
    }

    public fun clickBack(view: View){
        finish()
    }
    

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Film> {
        Log.e("CREATE","call1")
        return DetailFilmLoader(this, args?.getString("header").toString())
    }

    override fun onLoaderReset(loader: Loader<Film>) {

    }

    override fun onLoadFinished(loader: Loader<Film>, data: Film?) {
        try {
            val txtTitle = findViewById<TextView>(R.id.txtTitleFilm)
            if (data != null){
                txtTitle.text = data.data.title
                Log.e("DATA", "result")
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


}