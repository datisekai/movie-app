package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken

class ReplyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)

        val text = "     Dear ${ClassToken.FULLNAME},\n" +
                "     Thank you for reaching out to us with your feedback regarding our movie streaming application. We appreciate your time and valuable input. We strive to provide the best user experience and your feedback helps us in that endeavor.\n" +
                "\n" +
                "     We have carefully reviewed your feedback and are grateful for your suggestions. Our team will take your comments into consideration as we continue to improve our application. Your feedback plays a crucial role in shaping the future of our platform.\n" +
                "\n" +
                "     We are pleased to inform you that we have noted your request for [specific feature or improvement]. Our development team is actively working on enhancing the application and your suggestion will be taken into account during the planning and implementation process.\n" +
                "\n" +
                "     If you have any further questions, concerns, or additional feedback, please don't hesitate to reach out to us. We value your opinion and are always here to assist you.\n" +
                "\n" +
                "     Once again, thank you for taking the time to share your thoughts with us. We are committed to delivering an exceptional movie streaming experience, and your feedback is instrumental in achieving that goal.\n" +
                "     Best regards,\n     Dương And His Friends"
        var textView = findViewById<TextView>(R.id.reply_TvContent)
        textView.text = text
    }

    fun clickBack(view: View){
        finish()
    }
}