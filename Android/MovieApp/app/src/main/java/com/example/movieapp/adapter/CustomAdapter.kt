package com.example.movieapp.adapter

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.example.movieapp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.adapter.model.Movie
import com.example.movieapp.adapter.model.Genre
import com.example.movieapp.adapter.model.PaymentHistory
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.ui.activity.DetailFilmActivity
import com.example.movieapp.ui.activity.ResultGenreActivity
import com.makeramen.roundedimageview.RoundedImageView
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class CustomAdapter(private val dataList: List<Any>, private val view: Int, private val widthCard: Int, private val heightCard: Int, private val isBorderImage: Boolean) :
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

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(data: Any) {
            when (data) {
                is Movie -> {
                    val container: LinearLayout = itemView.findViewById(R.id.containerCard)
                    val image: RoundedImageView = itemView.findViewById(R.id.imageCard)
                    val title: TextView = itemView.findViewById(R.id.title)
                    val textView2: TextView = itemView.findViewById(R.id.textView2)
                    val movieItem = data as Movie
                    image.setImageResource(movieItem.imageResId)
                    title.text = movieItem.title
                    textView2.text = Html.fromHtml(movieItem.description)
                    //Custom
                    val layoutParams = image.layoutParams
                    layoutParams.width = widthCard
                    layoutParams.height = heightCard
                    image.layoutParams = layoutParams

                    image.minimumWidth= widthCard
                    image.maxWidth = widthCard;
                    image.maxHeight = heightCard
                    image.minimumHeight = heightCard

                    title.minimumWidth = widthCard
                    title.maxWidth = widthCard

                    textView2.minimumWidth = widthCard
                    textView2.maxWidth = widthCard

                    if(isBorderImage){
                        val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_radius)
                        image.cornerRadius = cornerRadius.toFloat()
                    }

                    container.setOnClickListener{
                        val intent : Intent =  Intent(itemView.context, DetailFilmActivity::class.java)
                        val bundle = Bundle()
                        bundle.putInt("id", movieItem.id) // Đặt giá trị vào Bundle
                        intent.putExtras(bundle)
                        itemView.context.startActivity(intent);
                    }
                }
                is Genre -> {
                    val buttonGenre: Button = itemView.findViewById(R.id.buttonGenre)
                    val genreItem = data as Genre
                    buttonGenre.text=genreItem.name
                    buttonGenre.tooltipText= genreItem.name
                    buttonGenre.setOnClickListener{
                        Log.d("TAG", ClassToken.FULLNAME)
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