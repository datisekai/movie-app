package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileDetailsFragment : Fragment(), View.OnClickListener {
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
        val view =  inflater.inflate(R.layout.fragment_profile_details, container, false)
        //set onclick for profile to detail
        val buttonclickFullname: RelativeLayout = view.findViewById(R.id.profile_detail_fullname_onclick)
        buttonclickFullname.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickEmail: RelativeLayout = view.findViewById(R.id.profile_detail_email_onclick)
        buttonclickEmail.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickPhonenumber: RelativeLayout = view.findViewById(R.id.profile_detail_phonenumber_onclick)
        buttonclickPhonenumber.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickBirthday: RelativeLayout = view.findViewById(R.id.profile_detail_birthday_onclick)
        buttonclickBirthday.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickPassword: RelativeLayout = view.findViewById(R.id.profile_detail_password_onclick)
        buttonclickPassword.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickGender: RelativeLayout = view.findViewById(R.id.profile_detail_gender_onclick)
        buttonclickGender.setOnClickListener(this)

        return view
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.profile_detail_fullname_onclick -> {
                val intent = Intent(requireContext(),ProfileFullNameActivity::class.java)
                startActivity(intent)
            }

            R.id.profile_detail_email_onclick -> {
                val intent = Intent(requireContext(),ProfileEmailActivity::class.java)
                startActivity(intent)
            }

            R.id.profile_detail_phonenumber_onclick ->{
                val intent = Intent(requireContext(),ProfilePhoneNumberActivity::class.java)
                startActivity(intent)
            }

            R.id.profile_detail_birthday_onclick ->{
            }

            R.id.profile_detail_password_onclick ->{
                val intent = Intent(requireContext(),ProfilePasswordActivity::class.java)
                startActivity(intent)
            }

            R.id.profile_detail_gender_onclick ->{
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
         * @return A new instance of fragment ProfileDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}