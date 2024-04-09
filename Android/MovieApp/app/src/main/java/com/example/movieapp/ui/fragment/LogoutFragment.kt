package com.example.movieapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.example.movieapp.data.LoginDataSource
import com.example.movieapp.data.LoginRepository
import com.example.movieapp.ui.activity.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


/**
 * A simple [Fragment] subclass.
 * Use the [LogoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class LogoutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    private lateinit var logoutLogin: LoginRepository
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
        val view = inflater.inflate(R.layout.fragment_logout, container, false)
        // Inflate the layout for this fragment
        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)

        val acct = GoogleSignIn.getLastSignedInAccount(view.context)
        if (acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email
        }

        signOut(inflater, container, view)
        return view
    }
    fun signOut(inflater: LayoutInflater, container: ViewGroup?, view: View) {
        gsc.signOut().addOnCompleteListener {
            val dataSource = LoginDataSource()
            logoutLogin = LoginRepository(dataSource)
            logoutLogin.logout(view.context)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogoutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}