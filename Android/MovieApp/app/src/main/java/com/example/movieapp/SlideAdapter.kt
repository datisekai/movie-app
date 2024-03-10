package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.makeramen.roundedimageview.RoundedImageView
import java.math.RoundingMode

class SlideAdapter(private val imageList : ArrayList<slideItem>, private val viewPager2: ViewPager2)
    : RecyclerView.Adapter<SlideAdapter.SlideViewHodler>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlideAdapter.SlideViewHodler {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slide_item_container,parent,false)
        return SlideViewHodler(view)
    }

    override fun onBindViewHolder(holder: SlideAdapter.SlideViewHodler, position: Int) {
        holder.roundedImage.setImageResource(imageList[position].Image)
        if (position == imageList.size-2){
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }



    inner class SlideViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roundedImage = itemView.findViewById<ImageView>(R.id.imageSlide)

    }

    private var runnable : Runnable = Runnable{
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }

}