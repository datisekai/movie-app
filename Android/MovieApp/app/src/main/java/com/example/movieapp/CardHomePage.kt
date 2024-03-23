package com.example.movieapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.ui.activity.DetailFilmActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class CardHomePage{
     fun init(context: Context, linearLayout: LinearLayout, widthCard: Int, heightCard: Int, isBorderImage: Boolean) {
         val cardContainer: LinearLayout = linearLayout
         val cardDataList = listOf("Card 1", "Card 2", "Card 3","Card 1", "Card 2", "Card 3")
         val maxWidthCard = widthCard

         for (cardData in cardDataList) {
             val cardView = LinearLayout(context)
             val layoutParams = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT
             )
//             cardView.apply {
//                 radius = resources.getDimension(com.google.android.material.R.dimen.mtrl_card_corner_radius)
//                 preventCornerOverlap = false
//                 useCompatPadding = true
//             }

             layoutParams.setMargins(16, 16, 16, 16)
             cardView.layoutParams = layoutParams
//            cardView.strokeWidth = resources.getDimensionPixelSize(com.google.android.material.R.dimen.m3_card_stroke_width)
//             cardView.maxCardElevation = linearLayout.resources.getDimension(com.google.android.material.R.dimen.mtrl_card_elevation)

             val linearLayout = LinearLayout(context)
             linearLayout.orientation = LinearLayout.VERTICAL
             linearLayout.layoutParams = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT
             )
//            linearLayout.setPadding(4, 4, 4, 4)

             val imageView = ImageView(context)
             imageView.id = View.generateViewId()
             imageView.setOnClickListener(View.OnClickListener {
                   val intent : Intent =  Intent(context,DetailFilmActivity::class.java)
                   context.startActivity(intent);
             })
             imageView.layoutParams = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 heightCard
             )
             imageView.minimumWidth= maxWidthCard
             imageView.maxWidth = maxWidthCard;
             imageView.scaleType = ImageView.ScaleType.CENTER_CROP
             imageView.setImageResource(R.drawable.anime1)
             imageView.contentDescription = "Image Anime1"

             if(isBorderImage){
                 Glide.with(context)
                     .load(R.drawable.anime1)
                     .transform(RoundedCorners(200))
                     .into(imageView)
             }



             val secondaryLayoutParams = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT
             )
             secondaryLayoutParams.setMargins(8,8,8,8)

             val titleTextView = TextView(context)
             titleTextView.layoutParams = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT
             )
             titleTextView.maxLines = 1
             titleTextView.ellipsize = TextUtils.TruncateAt.END
             titleTextView.maxWidth = maxWidthCard;
             titleTextView.layoutParams = secondaryLayoutParams
             titleTextView.setTextColor(Color.WHITE)
             titleTextView.gravity = Gravity.CENTER_HORIZONTAL
             titleTextView.text = "Chú thuật hồi chiến"
             titleTextView.minimumWidth= maxWidthCard
             titleTextView.setTypeface(Typeface.DEFAULT_BOLD)
             val textSize = 20
             titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())

             val secondaryTextView = TextView(context)
             secondaryTextView.maxWidth = maxWidthCard;
             secondaryTextView.minimumWidth= maxWidthCard
             secondaryTextView.gravity = Gravity.CENTER_HORIZONTAL
             secondaryTextView.layoutParams = secondaryLayoutParams
             secondaryTextView.maxLines = 1
             secondaryTextView.ellipsize = TextUtils.TruncateAt.END
             secondaryTextView.text = "2023"
//             secondaryTextView.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Body2)
             secondaryTextView.setTextColor(Color.WHITE)

             linearLayout.addView(imageView)
             linearLayout.addView(titleTextView)
             linearLayout.addView(secondaryTextView)

             cardView.addView(linearLayout)
             cardContainer.addView(cardView)
         }
    }


}