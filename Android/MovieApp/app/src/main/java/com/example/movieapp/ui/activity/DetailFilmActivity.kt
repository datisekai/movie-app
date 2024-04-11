package com.example.movieapp.ui.activity

import android.content.ContentValues
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
import com.example.movieapp.DBHelper
import com.example.movieapp.Helper
import com.example.movieapp.adapter.CommentAdapter
import com.example.movieapp.adapter.EsopideAdapter
import com.example.movieapp.data.model.Comment
import com.example.movieapp.data.model.CommentDTO
import com.example.movieapp.data.model.Esopide
import com.example.movieapp.data.model.EsopideDTO
import com.example.movieapp.service.DetailFilmLoader
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.RequestComment
import com.example.movieapp.data.model.RequestFilmFavorite
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class DetailFilmActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Film> {
    private lateinit var editTextComment: EditText
    private lateinit var data: MutableList<EsopideDTO>
    private lateinit var dataComment: MutableList<CommentDTO>
    private lateinit var btnFavorite: ImageButton
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var recyclerView: RecyclerView
    private final val TAG = "MainActivity"
    private lateinit var adapterComment: CommentAdapter
    var check: Boolean = false
    var checkPremium: Boolean = false
    var filmId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)

        editTextComment = findViewById(R.id.edtComment)
        editTextComment.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) ||
                (actionId == EditorInfo.IME_ACTION_DONE)
            ) {
                if (filmId != 0 && editTextComment.text.toString().isNullOrEmpty() == false) {
                    val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
                    val dataComment1 = RequestComment(editTextComment.text.toString(), filmId)
                    viewModel.createComment(dataComment1)
                    adapterComment.notifyDataSetChanged()
                    getComment(filmId)
                    editTextComment.text.clear()
                } else {
                    Toast.makeText(this, "Create Comment Fail", Toast.LENGTH_LONG).show()
                }
                val inputManager: InputMethodManager =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE)
                            as InputMethodManager
                inputManager.hideSoftInputFromWindow(this.editTextComment.windowToken, 0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        btnFavorite = findViewById(R.id.btnFavotite)
        btnFavorite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                clickBtnFavotite()
            }

        })
        //Check if a Loader is running, if it is, reconnect to it
        if (supportLoaderManager.getLoader<Film>(0) != null) {
            supportLoaderManager.initLoader(0, null, this).forceLoad()
        }

        // Google ads
        serviceGoogleAds()

        getDetailFilm()
    }

    private fun serviceGoogleAds() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.e(TAG, "Ad was clicked.")
                            }

                            override fun onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                Log.e(TAG, "Ad dismissed fullscreen content.")
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.")
                                mInterstitialAd = null
                            }

                            override fun onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.e(TAG, "Ad recorded an impression.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.e(TAG, "Ad showed fullscreen content.")
                            }
                        }
                }
            })


    }

    public fun clickWatch(view: View) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
            startPlayerActivity()
            addHistory(data )
        } else {
            startPlayerActivity()
            addHistory(data )
        }
    }


    private fun checkPremiumFilm() : Boolean{
        if (checkPremium==true){
           if (Helper.TokenManager.getRoles(this).isNullOrEmpty()){
               Log.e("ERRROR","roles null")
           }else{
               Log.e("ROLES", Helper.TokenManager.getRoles(this)?.get(0).toString())
           }
        }
        return false
    }
    private fun startPlayerActivity(){
        val intent : Intent =  Intent(this,
            PlayerActivity::class.java);
        val bundle = Bundle()
        if (data.isEmpty()==false){
            bundle.putInt("ID", data.get(0).id)
            bundle.putString("URL",data.get(0).url)
            bundle.putString("TITLE",data.get(0).title)
            intent.putExtra("videoUrl",bundle)
            startActivity(intent);
        }else{
            Toast.makeText(this,"ERROR: NO DATA",Toast.LENGTH_LONG).show()
        }

    }

    private fun addHistory(data: MutableList<EsopideDTO>){
        val userId = Helper.TokenManager.getId(this)
        val dbFile = this.getDatabasePath("history")
        val dataFilePath = dbFile.absolutePath
        Log.e("database",dataFilePath)
        val dbHelper = DBHelper(this)

        if(userId != null){

           val resultAddItem = dbHelper.insert(userId, data.get(0).id)
           if(resultAddItem != -1L){
               Log.e("Insert Database ","Successfully")
           }
           else{
               Log.e("Insert Database ","Fail")
           }
            val resultCurrent = dbHelper.getListId(userId)
            for (id in resultCurrent){
                Log.e("listId",id.toString())
            }
            Log.e("Insert Database ",userId.toString())
       }
        dbHelper.getAll()
    }

    public fun clickBtnFavotite(){
       if (check==false){
           btnFavorite.setImageResource(R.drawable.baseline_check_24)
           check = true
           val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
           Log.e("CREATE",filmId.toString())
           val dataFilmFavorite = RequestFilmFavorite(filmId)
           viewModel.postFilmFavoutite(dataFilmFavorite)

       }else {
           btnFavorite.setImageResource(R.drawable.baseline_add_24)
           check = false
           val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
           Log.e("CREATE",filmId.toString())
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
            val check = bundle.getBoolean("IS_PREMIUM")
            if (check!=null){
                checkPremium = check
                Log.e("ISPREMIUM",checkPremium.toString())
            }
            val bundle2 = Bundle()
            if (id != null) {
                filmId = id
                bundle2.putInt("idFilm",id)
                getEsopide(id)
                getComment(id)
                checkFavoriteFilm(id)
            }
            supportLoaderManager.restartLoader<Film>(0, bundle2, this)

        }
    }

    private fun checkFavoriteFilm(id : Int){
        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        val dataList : LiveData<Film1> = viewModel.getAllFilmFavourite()
        dataList.observe(this){datas->
            val tmp = datas.data.toMutableList()
            for (i in tmp){
                if (id == i.id){
                    val btn = findViewById<ImageButton>(R.id.btnFavotite)
                    btn.setImageResource(R.drawable.baseline_check_24)
                    check=true
                }
            }
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
            recyclerView = findViewById(R.id.recyclerComment)
            adapterComment = CommentAdapter(this,dataComment)
            recyclerView.adapter = adapterComment
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
                checkPremium = data.data.isRequiredPremium
                txtTitle.text = data.data.title
                txtDirect.text = "Director: ${data.data.director}"
                txtCate.text = "Category: ${data.data.type}"
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