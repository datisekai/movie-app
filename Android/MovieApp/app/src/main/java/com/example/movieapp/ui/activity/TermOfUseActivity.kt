package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.movieapp.R

class TermOfUseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_of_use)

        val text: String = "     Terms of Use\n" +
                "\n" +
                "     Welcome to our movie streaming application! These Terms of Use govern your use of the application and its services. By accessing or using the application, you agree to comply with these Terms of Use. If you do not agree with any part of these terms, please refrain from using the application.\n" +
                "\n" +
                "     Eligibility\n" +
                "You must be at least 18 years old or have the legal capacity to enter into a binding agreement in your jurisdiction to use this application. By using the application, you represent and warrant that you meet the eligibility requirements.\n" +
                "\n" +
                "     User Account\n" +
                "You may be required to create a user account to access certain features of the application. You are responsible for maintaining the confidentiality of your account credentials and for all activities that occur under your account. You agree to provide accurate and complete information when creating your account.\n" +
                "\n" +
                "     Content\n" +
                "The application allows you to access and stream movies and other related content. All content provided through the application is for personal, non-commercial use only. You agree not to reproduce, distribute, modify, or create derivative works of the content without prior authorization.\n" +
                "\n" +
                "     Intellectual Property\n" +
                "The application and its content, including but not limited to text, graphics, logos, and images, are protected by intellectual property laws. All rights, title, and interest in the application and its content belong to the application provider or the respective content owners.\n" +
                "\n" +
                "      Prohibited Conduct\n" +
                "You agree not to engage in any of the following prohibited activities:\n" +
                "\n" +
                "Violating any applicable laws or regulations\n" +
                "Interfering with the operation of the application\n" +
                "Attempting to gain unauthorized access to the application or its servers\n" +
                "Uploading or transmitting any malicious software or harmful content\n" +
                "Engaging in any fraudulent or deceptive activities\n" +
                "Disclaimer of Warranties\n" +
                "The application and its services are provided on an \"as is\" and \"as available\" basis without any warranties or representations, express or implied. The application provider disclaims any warranties of merchantability, fitness for a particular purpose, or non-infringement.\n" +
                "\n" +
                "     Limitation of Liability\n" +
                "To the maximum extent permitted by law, the application provider shall not be liable for any indirect, incidental, consequential, or punitive damages arising out of or in connection with your use of the application or its services.\n" +
                "\n" +
                "     Modifications to the Terms\n" +
                "The application provider reserves the right to modify or update these Terms of Use at any time. Any changes will be effective immediately upon posting the revised terms within the application. It is your responsibility to review the Terms of Use periodically for any updates.\n" +
                "\n" +
                "     Governing Law and Jurisdiction\n" +
                "These Terms of Use shall be governed by and construed in accordance with the laws of [Jurisdiction]. Any disputes arising out of or relating to these terms shall be subject to the exclusive jurisdiction of the courts of [Jurisdiction].\n" +
                "\n" +
                "If you have any questions or concerns regarding these Terms of Use, please contact us at datly030102@gmail.com"
        var textViewContent = findViewById<TextView>(R.id.term_of_user_TvContent)
        textViewContent.text = text
    }

    fun clickBack(view: View){
        finish()
    }
}