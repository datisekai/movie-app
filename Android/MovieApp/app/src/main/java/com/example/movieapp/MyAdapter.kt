import com.example.movieapp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.MovieItem
import com.example.movieapp.GenreItem
import com.example.movieapp.PaymentHistoryItem
import com.example.movieapp.ResultGenreFragment

class MyAdapter(private val dataList: List<Any>, private val view: Int) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

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
                is MovieItem -> {
                    val image: ImageView = itemView.findViewById(R.id.imageCard)
                    val title: TextView = itemView.findViewById(R.id.title)
                    val textView2: TextView = itemView.findViewById(R.id.textView2)
                    val movieItem = data as MovieItem
                    image.setImageResource(movieItem.imageResId)
                    title.text = movieItem.title
                    textView2.text = movieItem.year
                }
                is GenreItem -> {
                    val textView: TextView = itemView.findViewById(R.id.textView)
                    val genreItem = data as GenreItem
                    textView.text=genreItem.name
                    textView.tooltipText= genreItem.name
                }
                is PaymentHistoryItem -> {
                    val title: TextView = itemView.findViewById(R.id.textTitle)
                    val date: TextView = itemView.findViewById(R.id.textDate)
                    val money: TextView = itemView.findViewById(R.id.textMoney)
                    val paymentHistoryItem = data as PaymentHistoryItem
                    title.text = paymentHistoryItem.title
                    date.text = paymentHistoryItem.date
                    money.text = "-" + paymentHistoryItem.money.toString() +"đ"
                }
            }
        }
    }
}