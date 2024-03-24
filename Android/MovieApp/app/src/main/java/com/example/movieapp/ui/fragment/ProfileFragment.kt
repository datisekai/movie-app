package com.example.movieapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.ui.activity.RegisterPremiumActivity
import com.example.movieapp.ui.activity.ProfileDetailsActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), View.OnClickListener {
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
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        val buttonBuyPremium = view.findViewById<Button>(R.id.button2)
        buttonBuyPremium.setOnClickListener(this)
        //set onclick for profile to detail
        val buttonclickProfile: RelativeLayout = view.findViewById(R.id.fragment_profile_click_details)
        buttonclickProfile.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickHistory: RelativeLayout = view.findViewById(R.id.fragment_profile_click_history)
        buttonclickHistory.setOnClickListener(this)

        //Get Username
        val id = ClassToken.ID
        val fullname = ClassToken.FULLNAME
        val email = ClassToken.EMAIL

        val tvFullname = view.findViewById<TextView>(R.id.profile_username_textview)
        val tvEmail = view.findViewById<TextView>(R.id.profile_email_textview)

        tvFullname.setText(fullname)
        tvEmail.setText(email)

        return view
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.button2 -> {
                val intent = Intent(requireContext(), RegisterPremiumActivity::class.java)
                startActivity(intent)
            }

            R.id.fragment_profile_click_details -> {
                val intent = Intent(requireContext(),ProfileDetailsActivity::class.java)
                startActivity(intent)
            }

            R.id.fragment_profile_click_history ->{
            }

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}