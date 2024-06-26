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
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.loader.content.Loader
import androidx.loader.app.LoaderManager
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.GetUser
import com.example.movieapp.data.model.User
import com.example.movieapp.service.UserLoader
import com.example.movieapp.ui.activity.PrivacyActivity
import com.example.movieapp.ui.activity.RegisterPremiumActivity
import com.example.movieapp.ui.activity.ProfileDetailsActivity
import com.example.movieapp.ui.activity.ReplyActivity
import com.example.movieapp.ui.activity.TermOfUseActivity
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), View.OnClickListener, LoaderManager.LoaderCallbacks<GetUser> {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var progressBar: ProgressBar
    private lateinit var tvFullname: TextView
    private lateinit var tvEmail: TextView
    private lateinit var linearLayout: LinearLayout
    private lateinit var imgAvtProfile : RoundedImageView
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
        //
        val rolesUser = Helper.TokenManager.getRoles(requireContext())
        if(rolesUser != null){
            var checkRole = true
            val roles = rolesUser.split(",")
            for (role in roles){
                if (role.equals("premium_user")){
                    checkRole = false
                }
                else if(role.equals("admin")){
                    checkRole = false
                }
            }

            if(checkRole){
                buttonBuyPremium.visibility = View.VISIBLE
            }else{
                buttonBuyPremium.visibility = View.GONE
            }
        }

        //set onclick for profile to detail
        val buttonclickProfile: RelativeLayout = view.findViewById(R.id.fragment_profile_click_details)
        buttonclickProfile.setOnClickListener(this)

        val buttonclickPrivacy: RelativeLayout = view.findViewById(R.id.btn_profile_to_privacy)
        buttonclickPrivacy.setOnClickListener(this)
        val buttonclickTermOfUse: RelativeLayout = view.findViewById(R.id.btn_profile_to_term)
        buttonclickTermOfUse.setOnClickListener(this)
        val buttonclickReply: RelativeLayout = view.findViewById(R.id.btn_profile_to_reply)
        buttonclickReply.setOnClickListener(this)

        progressBar = view.findViewById(R.id.fragment_profile_name_progressBar)
        tvFullname = view.findViewById(R.id.profile_username_textview)
        tvEmail = view.findViewById<TextView>(R.id.profile_email_textview)
        linearLayout = view.findViewById<LinearLayout>(R.id.main_layout_fragment_profile)
        imgAvtProfile = view.findViewById(R.id.imgSettingProflie)

        //Init Loader
        loaderManager.initLoader(0, null, this)

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

            R.id.btn_profile_to_privacy -> {
                val intent = Intent(requireContext(),PrivacyActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_profile_to_term -> {
                val intent = Intent(requireContext(),TermOfUseActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_profile_to_reply -> {
                val intent = Intent(requireContext(),ReplyActivity::class.java)
                startActivity(intent)
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

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<GetUser> {
        linearLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        return UserLoader(requireContext())
    }

    override fun onLoaderReset(loader: Loader<GetUser>) {

    }

    override fun onLoadFinished(loader: Loader<GetUser>, data: GetUser?) {
        try {
            linearLayout.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            var fullname = ""
            var email = ""
            if(data != null){
                fullname = data.data.fullname
                email = data.data.email

                tvFullname.setText(fullname)
                tvEmail.setText(email)
                val urlAvt = "https://ui-avatars.com/api/?name=" + fullname
                Picasso.get()
                    .load(urlAvt)
                    .into(imgAvtProfile)
            }else{
                fullname = ClassToken.FULLNAME
                email = ClassToken.EMAIL

                val urlAvt = "https://ui-avatars.com/api/?name=" + fullname
                Picasso.get()
                    .load(urlAvt)
                    .into(imgAvtProfile)
            }


        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}