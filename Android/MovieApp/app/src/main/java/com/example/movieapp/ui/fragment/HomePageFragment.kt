package com.example.movieapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo

import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.GridSpacingItemDecoration
import com.example.movieapp.R
import com.example.movieapp.adapter.CustomAdapter
import com.example.movieapp.adapter.model.CardHome
import com.example.movieapp.adapter.model.Movie
import com.example.movieapp.service.GenreMovieViewModel
import com.example.movieapp.service.GenreViewModel
import com.example.movieapp.ui.activity.ResultGenreActivity
import com.example.movieapp.ui.activity.SearchActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
data class cardSize(val widthCard: Int, val heightCard: Int,val isBorderImage: Boolean)
class HomePageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        val viewModelGenre = ViewModelProvider(this).get(GenreViewModel::class.java)
        viewModelGenre.getListGenre(1).observe(viewLifecycleOwner){genre->

            val dataList: MutableList<CardHome> = ArrayList()

            dataList.add(CardHome(genre.data[0].id, genre.data[0].title,R.id.recycler_view1,480, 480, false, R.id.progressBar1, R.id.viewAllList1, R.id.txtMoveGroup_1))
            dataList.add(CardHome(genre.data[1].id, genre.data[1].title,R.id.recycler_view2,640, 320, false, R.id.progressBar2, R.id.viewAllList2, R.id.txtMoveGroup_2))
            dataList.add(CardHome(genre.data[2].id, genre.data[2].title,R.id.recycler_view3,480, 720, false, R.id.progressBar3, R.id.viewAllList3, R.id.txtMoveGroup_3))
            dataList.add(CardHome(genre.data[3].id, genre.data[3].title,R.id.recycler_view4,480, 480, true , R.id.progressBar4, R.id.viewAllList4, R.id.txtMoveGroup_4))

            for(item in dataList){
                val progressbar: ProgressBar = view.findViewById(item.progressbarId)
                view.findViewById<TextView?>(item.titleGenreId).setText(item.title)
                view.findViewById<Button?>(item.viewAllId).setOnClickListener{
                    handleSubmit(view, item.id, item.title)
                }
                callApi(view, item, progressbar)
            }
        }


        val editText = view.findViewById<EditText>(R.id.editTextSearch)
        editText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) ||
                (actionId == EditorInfo.IME_ACTION_DONE)
            ) {
                val inputManager : InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                inputManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                val intent = Intent(view.context, SearchActivity::class.java)
                intent.putExtra("q", editText.text.toString())
                startActivity(intent)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun handleSubmit(view: View,id: Int, title: String){
        val intent = Intent(view.context, ResultGenreActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("name", title)
        intent.putExtras(bundle)
        view.context.startActivity(intent)
    }
    fun callApi(view: View, data: CardHome, progressbar: ProgressBar){
        val spacing = 24

        val viewModel = ViewModelProvider(this).get(GenreMovieViewModel::class.java)
        viewModel.getListGenreMovie(data.id,1).observe(viewLifecycleOwner) { films ->

            val dataListMovie: MutableList<Movie> = ArrayList()
            for (o in films.data){
                dataListMovie.add(Movie(o.id ,o.thumbnail, o.title.toString(), o.description.toString(), o.isRequiredPremium))
            }

            progressbar.visibility = View.GONE

            val recyclerView1 = view.findViewById<RecyclerView>(data.view)
            if(dataListMovie.isNotEmpty()){
                recyclerView1.addItemDecoration(
                    GridSpacingItemDecoration(
                        dataListMovie.size,
                        spacing,
                        false
                    )
                )
            }

            recyclerView1.layoutManager =
                LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)


            val adapter = dataListMovie?.let {
                CustomAdapter(
                    this.requireActivity(),
                    it,
                    R.layout.card, data.widthCard, data.heightCard, data.isBorderImage
                )
            }
            recyclerView1.adapter = adapter
        }

    }

}