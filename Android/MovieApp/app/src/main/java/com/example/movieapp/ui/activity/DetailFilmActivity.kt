package com.example.movieapp.ui.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.example.movieapp.R
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapp.Api.MyViewModel
import com.example.movieapp.adapter.CommentAdapter
import com.example.movieapp.adapter.EsopideAdapter
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.Comment
import com.example.movieapp.data.model.CommentDTO
import com.example.movieapp.data.model.Esopide
import com.example.movieapp.data.model.EsopideDTO
import com.example.movieapp.service.DetailFilmLoader
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.RequestComment
import com.example.movieapp.data.model.RequestFilmFavorite


class DetailFilmActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Film>{
    private lateinit var editTextComment : EditText
    private lateinit var data : MutableList<EsopideDTO>
    private lateinit var dataComment : MutableList<CommentDTO>
    private lateinit var btnFavorite : ImageButton
    var check : Boolean = false
    var filmId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)

        editTextComment = findViewById(R.id.edtComment)
        editTextComment.setOnEditorActionListener{_, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) ||
                (actionId == EditorInfo.IME_ACTION_DONE)
            ) {
                   if (filmId!=0){
                       val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
                       val dataComment1 = RequestComment(editTextComment.text.toString(),filmId)
                       viewModel.createComment(dataComment1)
                       getComment(filmId)
                       editTextComment.text.clear()
                   }else
                   {
                       Toast.makeText(this,"Create Comment Fail",Toast.LENGTH_LONG).show()
                   }
                val inputManager : InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                inputManager.hideSoftInputFromWindow(this.editTextComment.windowToken, 0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        btnFavorite = findViewById(R.id.btnFavotite)
        btnFavorite.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                clickBtnFavotite()
            }

        })
        //Check if a Loader is running, if it is, reconnect to it
        if (supportLoaderManager.getLoader<Film>(0)!=null) {
            supportLoaderManager.initLoader(0,null,this).forceLoad()
        }

        getDetailFilm()
    }

    public fun clickWatch(view:View){
        val intent : Intent =  Intent(this,
            PlayerActivity::class.java);
        val bundle = Bundle()
        bundle.putString("URL",data.get(0).url)
        intent.putExtra("videoUrl",bundle)
        startActivity(intent);

    }

    public fun clickBtnFavotite(){
       if (check==false){
           btnFavorite.setImageResource(R.drawable.baseline_check_24)
           check = true
           val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
           val dataFilmFavorite = RequestFilmFavorite(filmId)
           viewModel.postFilmFavoutite(dataFilmFavorite)

       }else
       {
           btnFavorite.setImageResource(R.drawable.baseline_add_24)
           check = false
           val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
           val dataFilmFavorite = RequestFilmFavorite(filmId)
           viewModel.postFilmFavoutite(dataFilmFavorite)
       }
    }
    
    public fun getDetailFilm(){
       val connMgr : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
       val networkInfo = connMgr.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            val intent : Intent = intent
            val bundle : Bundle = intent.getBundleExtra("DataID")!!
            val id = bundle.getInt("ID")
            val bundle2 = Bundle()
            if (id != null) {
                filmId = id
                bundle2.putInt("idFilm",id)
                getEsopide(id)
                getComment(id)
            }
            supportLoaderManager.restartLoader<Film>(0, bundle2, this)

        }
    }

    private fun getEsopide(id : Int){
        data = mutableListOf()
        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        val esopideList : LiveData<Esopide> = viewModel.getAllEsopide(id)
        esopideList.observe(this) { esopdes ->
            val tmp = esopdes.data.toMutableList()
            data.addAll(tmp)
            val recyclerView1 = findViewById<RecyclerView>(R.id.recyclerEsopide)
            recyclerView1.adapter = EsopideAdapter(this, data)
            recyclerView1.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

    }

    private fun getComment(id : Int){
        dataComment = mutableListOf()
        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        val commentList : LiveData<Comment> = viewModel.getAllComment(id)
        commentList.observe(this) { comments ->
            val tmp = comments.data.toMutableList()
            dataComment.addAll(tmp)
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerComment)
            recyclerView.adapter = CommentAdapter(this, dataComment)
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
    }

    public fun clickBack(view: View){
        finish()
    }
    

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Film> {
        Log.e("CREATE","call1")
        val id : Int? = args?.getInt("idFilm")
        if (id!=null){
            return DetailFilmLoader(this,id)
        }
        return DetailFilmLoader(this,0)
    }

    override fun onLoaderReset(loader: Loader<Film>) {

    }

    override fun onLoadFinished(loader: Loader<Film>, data: Film?) {
        try {
            val txtTitle = findViewById<TextView>(R.id.txtTitleFilm)
            val txtDirect = findViewById<TextView>(R.id.txtDirector)
            val txtCate = findViewById<TextView>(R.id.txtCatelogy)
            val imgPoster = findViewById<ImageView>(R.id.imagePoster)
            val imgMain = findViewById<ImageView>(R.id.imageView3)
            val txtCountry = findViewById<TextView>(R.id.txtCountry)
            val txtView = findViewById<TextView>(R.id.txtView)
            val txtYear = findViewById<TextView>(R.id.txtYear)
            val txtMonth = findViewById<TextView>(R.id.txtMonth)
            val txtDescription = findViewById<TextView>(R.id.txtDescription)
            if (data != null){
                txtTitle.text = data.data.title
                txtDirect.text = "Đạo diễn: ${data.data.director}"
                txtCate.text = "Thể loại: ${data.data.type}"
                txtCountry.text = data.data.location
                txtView.text = data.data.view.toString()
                val tmp = data.data.createAt.split("-")
                txtYear.text = tmp[0]
                txtMonth.text = "T"+tmp[1]
                txtDescription.text = data.data.description
                // load poster
                val requestOption = RequestOptions().placeholder(R.drawable.default_movie)
                    .error(R.drawable.default_movie)
                Glide.with(this)
                    .load(data.data.thumbnail)
                    .apply(requestOption)
                    .into(imgPoster)

                Glide.with(this)
                    .load(data.data.thumbnail)
                    .apply(requestOption)
                    .into(imgMain)

            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }




}