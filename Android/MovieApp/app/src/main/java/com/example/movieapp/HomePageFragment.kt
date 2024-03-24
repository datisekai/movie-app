package com.example.movieapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.NavigationUI
import com.example.movieapp.Api.MyViewModel
import com.example.movieapp.data.model.Film
import com.google.android.material.navigation.NavigationView

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
    private fun generateDataList(): List<cardSize> {
        val dataList: MutableList<cardSize> = ArrayList()
        dataList.add(cardSize(480, 480, false))
        dataList.add(cardSize(640, 320, false))
        dataList.add(cardSize(480, 720, false))
        dataList.add(cardSize(480, 480, true))
        // Thêm các phần tử khác vào danh sách dữ liệu
        return dataList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        val editText = view.findViewById<EditText>(R.id.editTextSearch)
        editText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                // Xử lý sự kiện khi nhấn phím Enter
//                performSearch(editText.text.toString())
//                val searchFragment = SearchFragment()
//                val fragmentManager = requireActivity().supportFragmentManager
//                fragmentManager.beginTransaction()
//                    .add(R.id.navHostFragment, searchFragment)
//                    .addToBackStack(null)
//                    .commit()
//                val navController = Navigation.findNavController(view)
//                navController.navigate(R.id.menu_Search)
                val navigationview = inflater.inflate(R.layout.fragment_home_page, container, false).findViewById<NavigationView>(R.id.navigationView)
                navigationview.itemIconTintList = null

                val navController = Navigation.findNavController(requireView())
                NavigationUI.setupWithNavController(navigationview, navController)
                navController.navigate(R.id.editTextSearch)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        val cardDataList = generateDataList()
        val listCardContainer = view.findViewById<LinearLayout>(R.id.listCardContainer)

        for (cardData in cardDataList) {
            val card = LinearLayout(view.context)
            val layoutParams1 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams1.setMargins(8, 0, 8, 0)
            card.layoutParams = layoutParams1

            val linearLayout = LinearLayout(view.context)
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            linearLayout.orientation = LinearLayout.VERTICAL

            val textView = TextView(view.context)
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            textView.text = "Popular Movie"
            textView.setTextColor(Color.WHITE)
//            textView.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline6)
            textView.setTypeface(Typeface.DEFAULT_BOLD)
            val textSize = 20
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
            textView.setPadding(16,0,0,24)

//            val button = Button(this)
//            button.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            button.text = "View More"
//            button.setTextColor(ContextCompat.getColor(this, R.color.white))
////            button.setBackgroundResource(R.drawable.rounded_button)
//            button.setPadding(8, 0, 8, 0)

//            val layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            layoutParams.marginEnd = 8

//            button.layoutParams = layoutParams

//            val linearLayoutCard = LinearLayout(this)
//            linearLayoutCard.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )

            val horizontalScrollView = HorizontalScrollView(view.context)
            horizontalScrollView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            horizontalScrollView.isHorizontalScrollBarEnabled = false
            horizontalScrollView.isScrollbarFadingEnabled = false
            horizontalScrollView.setPadding(8,0,0,40)

            val cardContainer = LinearLayout(view.context)
            cardContainer.id = View.generateViewId()
            cardContainer.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            cardContainer.orientation = LinearLayout.HORIZONTAL

            horizontalScrollView.addView(cardContainer)

            val cardHomePage = CardHomePage()
            cardHomePage.init(view.context, cardContainer, cardData.widthCard, cardData.heightCard, cardData.isBorderImage)

            linearLayout.addView(textView)
//            linearLayout.addView(button)
            linearLayout.addView(horizontalScrollView)

            card.addView(linearLayout)

            listCardContainer.addView(card)

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
}