package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.makeramen.roundedimageview.RoundedImageView
import java.math.RoundingMode

class SlideAdapter : RecyclerView.Adapter<SlideAdapter.SlideViewHodler>{
    lateinit var slideItem: MutableList<slideItem>
    var viewPager2: ViewPager2
    constructor(sildeItem : MutableList<slideItem>, viewPager2: ViewPager2){
        this.slideItem = slideItem
        this.viewPager2 = viewPager2
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlideAdapter.SlideViewHodler {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slide_item_container,parent,false)
        return SlideViewHodler(view)
    }

    override fun onBindViewHolder(holder: SlideAdapter.SlideViewHodler, position: Int) {
        holder.setImage(slideItem[position])
        if (position == slideItem.size-2){
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return slideItem.size
    }



    class SlideViewHodler : RecyclerView.ViewHolder{
        private lateinit var roundedImage : RoundedImageView
        constructor(@NonNull itemView : View) : super(itemView) {
            roundedImage = itemView.findViewById(R.id.imageSlide)
        }

        fun setImage(slideitem : slideItem){
            roundedImage.setImageResource(slideitem.Image)
        }


    }

    private var runnable : Runnable = Runnable{
        slideItem.addAll(slideItem)
        notifyDataSetChanged()
    }

}