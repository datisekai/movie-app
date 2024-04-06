package com.example.movieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.model.CommentDTO
import com.makeramen.roundedimageview.RoundedImageView

class CommentAdapter(private val context: Context, private val data : List<CommentDTO>)
    : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentAdapter.CommentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        holder.content.text = data.get(position).content
        holder.dateCreated.text = formatDate(data.get(position).createAt)
        holder.imgUser.setImageResource(R.drawable.user_comment)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun formatDate(createAt : String) : String{
        var result : String
        val tmp = createAt.split("T")
        val date = tmp.get(0).split("-")
        result = date[2]+"/"+date[1]+"/"+date[0]
        return result
    }

    inner class CommentViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val imgUser = item.findViewById<RoundedImageView>(R.id.imageUserComment_item)
        val content = item.findViewById<TextView>(R.id.txtComment)
        val dateCreated = item.findViewById<TextView>(R.id.txtDateCreated)
    }


}