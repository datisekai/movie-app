package com.example.movieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.makeramen.roundedimageview.RoundedImageView

class PaymentsAdapter(private val context: Context,private val dataImage : List<Int>) :
   RecyclerView.Adapter<PaymentsAdapter.PaymentViewHolders>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentsAdapter.PaymentViewHolders {
        val view = LayoutInflater.from(context).inflate(R.layout.item_payment,parent,false)
        return PaymentViewHolders(view)
    }

    override fun onBindViewHolder(holder: PaymentsAdapter.PaymentViewHolders, position: Int) {
        var indexTmp = position
        holder.iconPay.setImageResource(dataImage.get(position))
        holder.iconPay.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if ( holder.checkContainer.visibility == View.VISIBLE &&
                    holder.iconCheck.visibility == View.VISIBLE){
                    holder.checkContainer.visibility = View.INVISIBLE
                    holder.iconCheck.visibility = View.INVISIBLE
                    check.checkChoose = false
                    check.index=-1

                }else{
                    holder.checkContainer.visibility = View.VISIBLE
                    holder.iconCheck.visibility = View.VISIBLE
                    check.checkChoose = true
                    check.index = indexTmp
                }


            }

        })
    }

    override fun getItemCount(): Int {
        return dataImage.size
    }

    inner class PaymentViewHolders(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val iconPay = itemView.findViewById<RoundedImageView>(R.id.iconPayMent)
        val checkContainer = itemView.findViewById<RoundedImageView>(R.id.checkContainer)
        val iconCheck = itemView.findViewById<ImageView>(R.id.iconCheck)
    }

    class check{
        companion object{
            var checkChoose : Boolean = false;
            var index : Int = -1
        }
    }
}