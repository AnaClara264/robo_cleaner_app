package com.example.covid192

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class OpenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open)
        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@OpenActivity, AlertActivity::class.java)
            startActivity(intent)
        }, 3000)
    }
}