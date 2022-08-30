package com.example.covid192

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class AlertActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        supportActionBar?.hide()

        val buttonAlert: TextView = findViewById(R.id.button_alert)
        buttonAlert.setOnClickListener {
            val intent = Intent(this@AlertActivity, AmbienteActivity::class.java)
            startActivity(intent)
        }
    }
}