package com.example.movieapp.adapter

import android.content.Intent
import android.os.Bundle
import com.example.movieapp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.model.Movie
import com.example.movieapp.model.Genre
import com.example.movieapp.model.PaymentHistory
import com.example.movieapp.ui.activity.DetailFilmActivity
import com.example.movieapp.ui.activity.ResultGenreActivity
import com.makeramen.roundedimageview.RoundedImageView

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
                    textView2.text = movieItem.year

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
                        itemView.context.startActivity(intent);
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
                    val paymentHistoryItem = data as PaymentHistory
                    title.text = paymentHistoryItem.title
                    date.text = paymentHistoryItem.date
                    money.text = "-" + paymentHistoryItem.money.toString() +"đ"
                }
            }
        }
    }
}