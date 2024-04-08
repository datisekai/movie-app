package com.example.movieapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.model.EsopideDTO
import com.example.movieapp.data.model.Film
import com.example.movieapp.ui.activity.PlayerActivity
import java.util.zip.Inflater

class EsopideAdapter(private val context: Context, private val data : List<EsopideDTO>)
    : RecyclerView.Adapter<EsopideAdapter.EsopideViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EsopideAdapter.EsopideViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.esopide_item,parent,false)
        return EsopideViewHolder(view)
    }

    override fun onBindViewHolder(holder: EsopideAdapter.EsopideViewHolder, position: Int) {
        val urlImage : String? = data.get(position).thumbnail
        holder.txt.text = data.get(position).title
        var index: Int = position
        if (urlImage.isNullOrBlank()){
            Glide.with(context).load(R.drawable.default_esopide).into(holder.img)
        }else{
            Glide.with(context).load(urlImage).into(holder.img)
        }
        holder.img.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val intent : Intent =  Intent(context,
                    PlayerActivity::class.java);
                val bundle = Bundle()
                bundle.putString("URL",data.get(index).url)
                bundle.putString("TITLE",data.get(index).title)
                intent.putExtra("videoUrl",bundle)
                context.startActivity(intent);
            }

        })

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class EsopideViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<CardView>(R.id.cardView)
        val img = itemView.findViewById<ImageView>(R.id.imageEsopide)
        val txt = itemView.findViewById<TextView>(R.id.numberEsopide)

    }

}