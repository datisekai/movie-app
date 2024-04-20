package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.example.movieapp.R

class PrivacyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)
        val titleText = "Privacy Policy"
        val titileText = findViewById<TextView>(R.id.title_privacy_TvContent)
        titileText.setText(titleText)
        val text: String = "     Welcome to our movie streaming application! We are committed to protecting your privacy and personal information. This privacy policy explains how we collect, use, and safeguard your information when you use our application." + "\n\n     1.Collection of Personal Information \n" + "When using the movie streaming application, we may ask you to provide certain personal information such as your name, email address, or login information. We only use this information to provide services to you and do not share your personal information with any third parties without your consent."+ "\n\n     2.Collection of Non-Personal Information \n" +"We may collect non-personal information about the usage of the application, such as IP address, device type, operating system, and usage information. This information is used to improve the user experience and analyze usage trends of the application."+ "\n\n     3.Advertising \n" +"Our application may display advertisements from third parties. We do not provide your personal information to these advertising parties. However, third parties may use tracking technologies (e.g., cookies) to customize advertisements based on non-personal information about the usage of the application."+ "\n\n     4.Information Security \n"+"We ensure that your personal information is kept secure and safe. We implement reasonable physical, electronic, and managerial measures to protect personal information from loss, misuse, unauthorized access, or disclosure."+"Updates to the Privacy Policy \n\n"+"     We may update this privacy policy from time to time. Any changes to the policy will be posted on the website or notified within the application. By continuing to use the application after any changes, you agree to the updated privacy policy. \n     We will strive to respond to your inquiries as promptly as possible and address any concerns you may have."
        var textView = findViewById<TextView>(R.id.privacy_TvContent)
        textView.text = text
        textView.gravity = Gravity.CENTER_HORIZONTAL
        textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    }

    fun clickBack(view: View){
        finish()
    }
}