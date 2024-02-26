import com.example.movieapp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.DataItem

class MyAdapter(private val dataList: List<DataItem>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.bindData(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView
        private val title: TextView
        private val textView2: TextView
        init {
            image = itemView.findViewById<ImageView>(R.id.imageCard)
            title = itemView.findViewById<TextView>(R.id.title)
            textView2 = itemView.findViewById<TextView>(R.id.textView2)
        }

        fun bindData(data: DataItem?) {
            image.setImageResource(data?.imageResId ?: 0)
            title.text = data?.title
            textView2.text = data?.year
        }
    }
}