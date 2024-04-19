package com.example.movieapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.adapter.model.Genre
import com.example.movieapp.adapter.model.Movie
import com.example.movieapp.adapter.model.PaymentHistory
import com.example.movieapp.service.NetworkManager
import com.example.movieapp.ui.activity.DetailFilmActivity
import com.example.movieapp.ui.activity.ResultGenreActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class CustomAdapter(private val activity: Activity,private val dataList: List<Any>, private val view: Int, private val widthCard: Int, private val heightCard: Int, private val isBorderImage: Boolean) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.bindData(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private var mInterstitialAd: InterstitialAd? = null
    private final val TAG = "MainActivity"

    private fun showInerAd(itemView : View,data : Movie){
        if (mInterstitialAd != null){
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.d(TAG, "onAdClicked: ")
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.d(TAG, "onAdDismissedFullScreenContent: ")
                    val intent : Intent =  Intent(itemView.context, DetailFilmActivity::class.java)
                    val bundle = Bundle()
                    val movie = data as Movie
                    bundle.putInt("ID",movie.id)
                    Log.e("PRMIUM",movie.is_required_premium.toString())
                    bundle.putBoolean("IS_PREMIUM",movie.is_required_premium)
                    intent.putExtra("DataID",bundle)
                    intent.putExtras(bundle)
                    itemView.context.startActivity(intent);
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    Log.d(TAG, "onAdFailedToShowFullScreenContent: ")
                    val intent : Intent =  Intent(itemView.context, DetailFilmActivity::class.java)
                    val bundle = Bundle()
                    val movie = data as Movie
                    bundle.putInt("ID",movie.id)
                    Log.e("PRMIUM",movie.is_required_premium.toString())
                    bundle.putBoolean("IS_PREMIUM",movie.is_required_premium)
                    intent.putExtra("DataID",bundle)
                    intent.putExtras(bundle)
                    itemView.context.startActivity(intent);
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.d(TAG, "onAdImpression: ")
                }
            }
            mInterstitialAd!!.show(activity)
        }else{
            val intent : Intent =  Intent(itemView.context, DetailFilmActivity::class.java)
            val bundle = Bundle()
            val movie = data as Movie
            bundle.putInt("ID",movie.id)
            Log.e("PRMIUM",movie.is_required_premium.toString())
            bundle.putBoolean("IS_PREMIUM",movie.is_required_premium)
            intent.putExtra("DataID",bundle)
            intent.putExtras(bundle)
            itemView.context.startActivity(intent);
        }
    }

    private fun loadInerAd(){
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity.applicationContext,"ca-app-pub-3940256099942544/1033173712",adRequest,
            object : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.d(TAG, "onAdFailedToLoad: ")
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    Log.d(TAG, "onAdLoaded: ")
                    mInterstitialAd = interstitialAd
                }
            })
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    private fun customeToast(message : String){
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.custome_toast,activity.findViewById(R.id.CustomToast))
        val toast = Toast(activity)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        val txt : TextView = view.findViewById(R.id.txtMessage)
        txt.text = message
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(data: Any) {
            when (data) {
                is Movie -> {
                    loadInerAd()
                    val container: LinearLayout = itemView.findViewById(R.id.containerCard)
                    val image: RoundedImageView = itemView.findViewById(R.id.imageCard)
                    val title: TextView = itemView.findViewById(R.id.title)
                    val textView2: TextView = itemView.findViewById(R.id.textView2)
                    val iconPremium: ImageView= itemView.findViewById(R.id.icon_premium)
                    val movieItem = data as Movie

                    title.text = movieItem.title
                    textView2.text = Html.fromHtml(movieItem.description)

                    Picasso.get().load(movieItem.imageResId)
                        .error(R.drawable.default_esopide)
                        .into(image)

                    //Custom
                    val layoutParams = image.layoutParams


                    image.layoutParams = layoutParams


                    if(widthCard !== 0){
                        layoutParams.width = widthCard
                        image.minimumWidth= widthCard
                        image.maxWidth = widthCard;

                        title.minimumWidth = widthCard
                        title.maxWidth = widthCard

                        textView2.minimumWidth = widthCard
                        textView2.maxWidth = widthCard
                    }
                    if(heightCard !== 0){
                        layoutParams.height = heightCard
                        image.maxHeight = heightCard
                        image.minimumHeight = heightCard
                    }

                    if(isBorderImage){
                        val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_radius)
                        image.cornerRadius = cornerRadius.toFloat()
                    }

                    if(movieItem.is_required_premium==false){
                        iconPremium.visibility = View.GONE
                    }else{
                        iconPremium.visibility = View.VISIBLE
                    }

                    container.setOnClickListener{
                      if (isNetworkConnected(activity)){
                          showInerAd(itemView,movieItem)
                      }else{
                          customeToast("Đã có lỗi xảy ra")
                      }
                    }
                }
                is Genre -> {
                    val buttonGenre: Button = itemView.findViewById(R.id.buttonGenre)
                    val genreItem = data as Genre
                    buttonGenre.text=genreItem.name
                    buttonGenre.tooltipText= genreItem.name
                    buttonGenre.setOnClickListener{
                        val intent = Intent(itemView.context, ResultGenreActivity::class.java)
                        val bundle = Bundle()
                        bundle.putInt("id", genreItem.id)
                        bundle.putString("name", genreItem.name)
                        intent.putExtras(bundle)
                        itemView.context.startActivity(intent)
                    }
                }
                is PaymentHistory -> {
                    val title: TextView = itemView.findViewById(R.id.textTitle)
                    val date: TextView = itemView.findViewById(R.id.textDate)
                    val money: TextView = itemView.findViewById(R.id.textMoney)
                    val status: TextView= itemView.findViewById(R.id.textStatus)
                    val paymentHistoryItem = data as PaymentHistory
                    title.text = paymentHistoryItem.title

                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    val dateTime: LocalDateTime = LocalDateTime.parse(paymentHistoryItem.date, formatter)
                    val formattedDateTime: String = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))

                    date.text = formattedDateTime

                    val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                    val formattedMoney: String = format.format(paymentHistoryItem.money.toLong())
                    money.text = formattedMoney
                    status.text = paymentHistoryItem.status
                }
            }
        }
    }
}